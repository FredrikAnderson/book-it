package com.anderson.bookit;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.anderson.bookit.model.Booking;
import com.anderson.bookit.model.Project;
import com.anderson.bookit.model.Resource;
import com.anderson.bookit.service.ProjectService;
import com.anderson.bookit.service.ResourceService;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl.ContextMenuParameter;
import com.calendarfx.view.DateControl.EntryContextMenuParameter;
import com.calendarfx.view.DateControl.EntryDetailsParameter;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class BookITUI extends Application {

	private static BookITUI myInstance = null;

	Stage window;
	private Scene rootScene = null;
	
	private BorderPane borderPane = new BorderPane();
	
	private CalendarView calendarView = new CalendarView();
	private BorderPane resourceView = new BorderPane();

	private BorderPane projectView = new BorderPane();

	private Entry<Booking> currentEntry;

	Booking bookingToEdit = new Booking();
	
	private CalendarManager calMan = CalendarManager.getInstance();
	
	ProjectService projectService = new ProjectService();
	ResourceService resourceService = new ResourceService();

	ArrayList<Calendar> calendars = new ArrayList<Calendar>();

	ProjectController projectController;
	
	public static BookITUI getInstance() {
		if (myInstance == null) {
			myInstance = new BookITUI();
		}
		return myInstance;
	}

	public CalendarManager getCalenderManager() {
		return calMan;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		
//		calendarView = new CalendarView();
		calendarView.setShowAddCalendarButton(false);
		calendarView.setShowDeveloperConsole(true);
		calendarView.setShowPrintButton(false);
		calendarView.showWeekPage();

//		calendarView.setShowSourceTray(true);23

		CalendarSource myCalendarSource = new CalendarSource("My Calendars");
		myCalendarSource.getCalendars().addAll(calMan.getCalendars());

		calendarView.getCalendarSources().addAll(myCalendarSource);

		calendarView.setRequestedTime(LocalTime.now());

		Callback<EntryDetailsParameter, Boolean> callback = new EntryDetailsManager<EntryDetailsParameter, Boolean>();
		calendarView.setEntryDetailsCallback(callback);

		Callback<ContextMenuParameter, ContextMenu> contextMenuCallback = new ContextMenuCallback<ContextMenuParameter, ContextMenu>();
		calendarView.setContextMenuCallback(contextMenuCallback);

		Callback<EntryContextMenuParameter, ContextMenu> entryContextMenuCallback = new EntryContextMenuCallback<EntryContextMenuParameter, ContextMenu>();
		calendarView.setEntryContextMenuCallback(entryContextMenuCallback);

		// calendarView.setContextMenu(ContextMenuCallback.createContextMenu());
//		EventHandler<? super ContextMenuEvent> value;
//		calendarView.setOnContextMenuRequested(value);

		Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
			@Override
			public void run() {
				while (true) {
					Platform.runLater(() -> {
						calendarView.setToday(LocalDate.now());
						calendarView.setTime(LocalTime.now());
					});

					try {
						// update every 10 seconds
						sleep(10000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}
			};
		};

		updateTimeThread.setPriority(Thread.MIN_PRIORITY);
		updateTimeThread.setDaemon(true);
		updateTimeThread.start();

		ActionHandler mch = new ActionHandler();
		MenuBar menubar = new MenuBar();
		Menu file = new Menu("File");
		MenuItem exitMi = new MenuItem("Exit");
		exitMi.setOnAction(mch);
		file.getItems().add(exitMi);		
		
		Menu view = new Menu("View");
		MenuItem resourcesMi = new MenuItem("Resources");
		resourcesMi.setOnAction(mch);
		MenuItem projectsMi = new MenuItem("Projects");
		projectsMi.setOnAction(mch);
		MenuItem bookingTlMi = new MenuItem("Bookings - Timeline");
		bookingTlMi.setOnAction(mch);
		MenuItem bookingCalMi = new MenuItem("Bookings - Calendar");
		bookingCalMi.setOnAction(mch);
		
		view.getItems().add(resourcesMi);		
		view.getItems().add(projectsMi);		
		view.getItems().add(bookingTlMi);
		view.getItems().add(bookingCalMi);
		menubar.getMenus().add(file);
		menubar.getMenus().add(view);

		borderPane.setTop(menubar);
				
//		
		TableView<Resource> tableResourceView = new TableView<Resource>();
	
//		EventType eventType;
//		tableResourceView.addEventHandler(eventType, eventHandler);
		
//		tableView = new BorderPane();
		
		TextField filterField = new TextField();

		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Resource> filteredData = 
        		new FilteredList<>(FXCollections.observableArrayList(resourceService.getResources()), p -> true);
        
        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(resource -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                
                if (resource.getId().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (resource.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Resource> sortedData = new SortedList<Resource>(filteredData);
        
        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tableResourceView.comparatorProperty());
        
        // 5. Add sorted (and filtered) data to the table.
        tableResourceView.setItems(sortedData);
		
		TableColumn<Resource, String> idColumn = new TableColumn<>("ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

		TableColumn<Resource, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		tableResourceView.getColumns().add(idColumn);
		tableResourceView.getColumns().add(nameColumn);
		
// 		tableResourceView.setItems();
		resourceView.setTop(filterField);
		resourceView.setCenter(tableResourceView);
		

				
// 		tableResourceView.setItems();
//		resourceView.setTop(filterField);
		
		Button addProjBtn = new Button("New");
		Button editProjBtn = new Button("Edit");
		Button deleteProjBtn = new Button("Delete");		
		HBox projCrudBar = new HBox(20, addProjBtn, editProjBtn, deleteProjBtn);
		
		projectController = new ProjectController();		
		
//		projectView.setTop(projCrudBar);
		projectView.setCenter(projectController.getView());
		
		
		// What should be view by "default"
		borderPane.setCenter(calendarView);

		rootScene = new Scene(borderPane);
		
		primaryStage.setTitle("Book IT");
		primaryStage.setScene(rootScene);
		primaryStage.setWidth(1300);
		primaryStage.setHeight(1000);
		primaryStage.centerOnScreen();
		primaryStage.show();	
		
		window = primaryStage;
	}

	public void showScene(String scene) {
		if (scene.equalsIgnoreCase("Bookings")) {
			borderPane.setCenter(calendarView);
//			stage.setScene(bookingScene);
		} else if (scene.equalsIgnoreCase("Projects")) {
				borderPane.setCenter(projectView);
//				stage.setScene(bookingScene);
		} else if (scene.contains("Calendar")) {

			borderPane.setCenter(calendarView);
//			stage.setScene(bookingScene);
			
		} else if (scene.contains("Exit")) {
			System.exit(0);

		} else {
			System.out.println("ShowScene: " + scene);
		}

		window.getScene().setRoot(borderPane);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public static void setCurrentEntry(Entry entry) {
		getInstance().currentEntry = entry;

		printEntry(getInstance().currentEntry);
	}

	public static void printEntry(Entry entry) {
		System.out.print("Calendar: " + entry.getCalendar().getName() + "\n " + "Starting: "
				+ entry.getStartAsLocalDateTime() + "\n " + "Ending: " + entry.getEndAsLocalDateTime() + "\n ");
	}

	public static void deleteCurrentEntry() {
		System.out.println("SHould delete current entry: ");
		printEntry(getInstance().currentEntry);

		// Delete on backend
		// TODO

		// Local edit
		getInstance().currentEntry.setCalendar(null);

	}

	public static void editCurrentEntry() {
		System.out.println("SHould edit current entry: ");
		printEntry(getInstance().currentEntry);

		getInstance().showEditBookingDialog(getInstance().currentEntry);
	}

	public static void newEntry() {
		System.out.println("SHould create a new entry... ");
		getInstance().showEditBookingDialog(null);
	}

	private void showEditBookingDialog(Entry<Booking> toEdit) {

		BookingDialog bookingDialog = new BookingDialog();
		
		bookingDialog.editModel(toEdit);
	}


	private void addProjects(ComboBox cbx) {
		List<ProjectDTO> projects = projectService.getProjects();
		
		for (ProjectDTO proj : projects) {
			cbx.getItems().add(proj);			
		}
	}

	
	private void addResources(ComboBox cbx) {
		
		List<Resource> resources = resourceService.getResources();
		
		for (Resource resource : resources) {
			cbx.getItems().add(resource);			
		}
		
	}

	private void actionOnObject(String action, Object userData, int indexinView) {
		System.out.println("actionObObj: " + action + ", " + userData.toString() + ", " + indexinView);
		
		if (userData instanceof ProjectDTO) {
			projectController.actionOnObject(action, (ProjectDTO) userData);
		}
	}

	
	class ActionHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
//			System.out.println("MenuChoice handling: " + event.getSource());

			MenuItem mi = (MenuItem)event.getSource();
			String label = mi.getText();

			System.out.println("Event: " + event);
			System.out.println("EventSrc: " + event.getSource());
			System.out.println("MenuItem: " + mi.getText() + ", " + mi.getClass());
			if (mi.getUserData() != null) {
				System.out.println("UserData: " + mi.getUserData().toString());
			}
//			if (mi.getUserData() != null) {				
//				actionOnObject(mi.getText(), mi.getUserData(), tableProjectView.getSelectionModel().getSelectedIndex());
//			}			
			
			showScene(label);
		}

	}


	
}