package hellofx;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl.ContextMenuParameter;
import com.calendarfx.view.DateControl.EntryContextMenuParameter;
import com.calendarfx.view.DateControl.EntryDetailsParameter;

import hellofx.ui.DateTimePicker;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
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
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Booking;
import model.Person;
import model.Project;
import model.Resource;
import service.ProjectService;
import service.ResourceService;

public class BookITApp extends Application {

	private static BookITApp myInstance = null;

	Stage window;
	private Scene rootScene = null;
	
	private BorderPane borderPane = new BorderPane();
	
	private CalendarView calendarView = new CalendarView();
	private BorderPane tableView = new BorderPane();
	
	private Entry<Booking> currentEntry;

	Booking bookingToEdit = new Booking();
	
	CalendarManager calMan = CalendarManager.getInstance();
	
	ProjectService projectService = new ProjectService();
	ResourceService resourceService = new ResourceService();

	ArrayList<Calendar> calendars = new ArrayList<Calendar>();
	
	public static BookITApp getInstance() {
		if (myInstance == null) {
			myInstance = new BookITApp();
		}
		return myInstance;
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

		MenuChoiceHandler mch = new MenuChoiceHandler();
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
		MenuItem bookingCalMi = new MenuItem("Bookings - Calender");
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
		tableView.setTop(filterField);
		tableView.setCenter(tableResourceView);
		
		rootScene = new Scene(borderPane);

		borderPane.setCenter(calendarView);
		
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
		} else {
			borderPane.setCenter(tableView);
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

		getInstance().showEditDialog(getInstance().currentEntry);
	}

	public static void newEntry() {
		System.out.println("SHould create a new entry... ");
		getInstance().showEditDialog(null);
	}

	private void showEditDialog(Entry<Booking> toEdit) {

		if (toEdit != null) {
			bookingToEdit = toEdit.getUserObject();
		}
		System.out.println("Should edit booking: " + bookingToEdit.toString());		
			
		Stage dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.setTitle("Edit Booking");
		
		GridPane gridP = new GridPane();

		DateTimePicker startDateTimePr = new DateTimePicker();
		DateTimePicker endDateTimePr = new DateTimePicker();

		
		// Setting the vertical and horizontal gaps between the columns
		gridP.setVgap(5);
		gridP.setHgap(5);

		// Setting the Grid alignment
		gridP.setAlignment(Pos.CENTER);

		// Resource
		Text resLbl = new Text("Resource: ");
		ComboBox<Resource> resCbx = new ComboBox<Resource>();
		addResources(resCbx);
		resCbx.getSelectionModel().select(bookingToEdit.getResource());
		
		
		gridP.add(resLbl, 0, 0);
		gridP.add(resCbx, 1, 0);

//		HBox resHbx = new HBox(resLbl, resCbx);
//		resHbx.setAlignment(Pos.CENTER_LEFT);
//		resHbx.setPadding(new Insets(10));

		// Project
		Text projLbl = new Text("Project: ");
		ComboBox<Project> projCbx = new ComboBox<Project>();
		projCbx.getSelectionModel().select(bookingToEdit.getProject());
		
		projCbx.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Project value = projCbx.getValue();
				
//				System.out.println("SHould set" + value.getStartDate() + value.getEndDate());

				startDateTimePr.setLocalDate(value.getStartDate());
				endDateTimePr.setLocalDate(value.getEndDate());
			}			
		});
		addProjects(projCbx);

		// CHoosing a proj would set times for the booking
		
		gridP.add(projLbl, 0, 1);
		gridP.add(projCbx, 1, 1);

		// Times
		// Start
		Text startTimeLbl = new Text("Start time: ");
				
		gridP.add(startTimeLbl, 0, 2);
		gridP.add(startDateTimePr, 1, 2);
		startDateTimePr.setLocalDateTime(bookingToEdit.getStartTime());
		
		// End Time
		Text endTimeLbl = new Text("End time: ");
		
		gridP.add(endTimeLbl, 0, 3);
		gridP.add(endDateTimePr, 1, 3);
		endDateTimePr.setLocalDateTime(bookingToEdit.getEndTime());

		Button button = new Button("OK");
		button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
	            public void handle(ActionEvent event) {
	                System.out.println("Hello World! Should save booking:");
	                
	                System.out.println("Res: " + resCbx.getValue()); // getSelectionModel().getSelectedItem().toString());
	                System.out.println("Proj: " + projCbx.getValue()); // getSelectionModel().getSelectedItem().toString());
	                
	                LocalDateTime localStateDate = startDateTimePr.getLocalDateTime();
	                LocalDateTime localEndDate = endDateTimePr.getLocalDateTime();
	                
	                dialogStage.close();
	                
	                Entry<Booking> entry = toEdit;
	                Booking booking = bookingToEdit;
	                
	                booking.setResource(resCbx.getValue());
	                booking.setProject(projCbx.getValue());
	                booking.setStartTime(startDateTimePr.getLocalDateTime());
	                booking.setEndTime(endDateTimePr.getLocalDateTime());
	                
	                entry = resourceService.updateEntryWithBooking(toEdit, booking);
	                calMan.linkEntryToBooking(entry);
	            }
		});
		gridP.add(button, 1, 4);
		
		// Fix scene
		dialogStage.setScene(new Scene(gridP, 400, 180));
		dialogStage.show();
	}

	private void addProjects(ComboBox cbx) {
		List<Project> projects = projectService.getProjects();
		
		for (Project proj : projects) {
			cbx.getItems().add(proj);			
		}
	}

	private void addResources(ComboBox cbx) {
		
		List<Resource> resources = resourceService.getResources();
		
		for (Resource resource : resources) {
			cbx.getItems().add(resource);			
		}
		
	}

	class MenuChoiceHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
//			System.out.println("MenuChoice handling: " + event.getSource());

			MenuItem mi = (MenuItem)event.getSource();
			String label = mi.getText();
			
			showScene(label);
		}
	}
	
}