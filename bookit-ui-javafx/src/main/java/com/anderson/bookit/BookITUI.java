package com.anderson.bookit;

import com.anderson.bookit.model.Booking;
import com.anderson.bookit.model.Resource;
import com.anderson.bookit.ui.controller.CalendarController;
import com.anderson.bookit.ui.controller.ItemController;
import com.anderson.bookit.ui.controller.LoginController;
import com.anderson.bookit.ui.controller.ProjectController;
import com.anderson.bookit.ui.controller.ProjectGanttController;
import com.anderson.bookit.ui.controller.UserController;
import com.anderson.bookit.ui.view.BookingDialog;
import com.anderson.bookit.ui.view.ItemBookingFormView;
import com.calendarfx.model.Entry;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class BookITUI extends Application {

	private static BookITUI myInstance = null;

	Stage window;
	private Scene rootScene = null;
	
	private BorderPane borderPane = new BorderPane();
	
//	private BorderPane resourceView = new BorderPane();

	private CalendarController calController = CalendarController.getInstance();
//	CalendarSource myCalendarSource = new CalendarSource("My Calendars");

	LoginController loginController = new LoginController();
	private BorderPane loginPane = new BorderPane();

	UserController userController;
	private BorderPane userPane = new BorderPane();

	ItemController itemController;
	private BorderPane itemPane = new BorderPane();

	ProjectController projectController;
	private BorderPane projectPane = new BorderPane();

	ProjectGanttController projectGanttController;
	private BorderPane projectGanttPane = new BorderPane();

	private BorderPane itemBookingFormPane = new BorderPane();
		
	MenuBar menubar = new MenuBar();
	
	Menu file = new Menu("File");
	Menu view = new Menu("View");
	Menu help = new Menu("Help");
	
	private Entry<Booking> currentEntry;
	Booking bookingToEdit = new Booking();

	public static BookITUI getInstance() {
		if (myInstance == null) {
			myInstance = new BookITUI();
		}
		return myInstance;
	}

	public String getLogoImage() {
		
		return "volvo-logo.png";
	}
	
	public CalendarController getCalenderManager() {
		return calController;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {

		myInstance = this;

		Thread updateTimeThread = new Thread("Calendar: Update Time Thread") {
			@Override
			public void run() {
				while (true) {
					Platform.runLater(() -> {
						// TODO
//						calendarView.setToday(LocalDate.now());
//						calendarView.setTime(LocalTime.now());
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
		MenuItem exitMi = new MenuItem("Exit");
		exitMi.setOnAction(mch);
		file.getItems().add(exitMi);		
		
		MenuItem resourcesMi = new MenuItem("Resources");
		resourcesMi.setOnAction(mch);
		MenuItem usersMi = new MenuItem("Users");
		usersMi.setOnAction(mch);
		MenuItem itemsMi = new MenuItem("Items");
		itemsMi.setOnAction(mch);
		MenuItem projectsMi = new MenuItem("Projects");
		projectsMi.setOnAction(mch);
		MenuItem bookingTlMi = new MenuItem("Bookings - Timeline");
		bookingTlMi.setOnAction(mch);
		MenuItem bookingCalMi = new MenuItem("Bookings - Calendar");
		bookingCalMi.setOnAction(mch);
		
//		view.getItems().add(resourcesMi);
		
		view.getItems().add(usersMi);		
		view.getItems().add(itemsMi);		
		view.getItems().add(projectsMi);		
		view.getItems().add(bookingTlMi);
		view.getItems().add(bookingCalMi);
		
		MenuItem aboutMi = new MenuItem("About");
		aboutMi.setOnAction(mch);
		help.getItems().add(aboutMi);		
				
		TableView<Resource> tableResourceView = new TableView<Resource>();
					
		Button addProjBtn = new Button("New");
		Button editProjBtn = new Button("Edit");
		Button deleteProjBtn = new Button("Delete");		
		HBox projCrudBar = new HBox(20, addProjBtn, editProjBtn, deleteProjBtn);

		loginPane.setCenter(loginController.getView());

		userController = new UserController();		
		userPane.setCenter(userController.getView());

		itemController = new ItemController();		
		itemPane.setCenter(itemController.getView());

		projectController = new ProjectController();		
		projectPane.setCenter(projectController.getView());

		projectGanttController = new ProjectGanttController();		
		projectGanttPane.setCenter(projectGanttController.getView());

		itemBookingFormPane.setCenter(new ItemBookingFormView());

		// What should be view by "default"
//		borderPane.setCenter(calController.getView());
		borderPane.setCenter(loginController.getView());

		rootScene = new Scene(borderPane);
		
		primaryStage.setTitle("Book IT");
		primaryStage.setScene(rootScene);
		primaryStage.setWidth(1200);
		primaryStage.setHeight(1000);
//		primaryStage.centerOnScreen();
		
		Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2 + 20);
		
		primaryStage.show();	
		
		window = primaryStage;

		Image im = new Image(getLogoImage(), 64, 64, true, false);
		window.getIcons().add(im);
		
		window.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		        try {
					stop();
					System.exit(0);

				} catch (Exception e) {
					e.printStackTrace();
				}
		    }
		});
				
	}

	public void addMenuAtTop(String role) {
		
		menubar.getMenus().add(file);
		
		if (!role.equalsIgnoreCase("booker")) {
			menubar.getMenus().add(view);
		}
		menubar.getMenus().add(help);

		borderPane.setTop(menubar);
	}
	
	public void showScene(String scene) {
		scene = scene.toLowerCase();
			
		if (scene.equalsIgnoreCase("Bookings")) {
			borderPane.setCenter(calController.getView());
			
//			stage.setScene(bookingScene);
		} else if (scene.equalsIgnoreCase("Users")) {
			userController.updateData();
			borderPane.setCenter(userPane);

		} else if (scene.equalsIgnoreCase("Items")) {
			itemController.updateData();
			borderPane.setCenter(itemPane);

		} else if (scene.equalsIgnoreCase("Projects")) {
			borderPane.setCenter(projectPane);
			
		} else if (scene.contains("timeline")) {
			borderPane.setCenter(projectGanttPane);
			
		} else if (scene.contains("calendar")) {
//			calController.updateData();
			
			borderPane.setCenter(calController.getView());
			
		} else if (scene.contains("exit")) {
			System.exit(0);

		} else if (scene.contains("itembookingform")) {
			borderPane.setCenter(itemBookingFormPane);

		} else if (scene.contains("about")) {
			System.out.println("Show about info");
				
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