package hellofx;

import java.time.LocalDate;
import java.time.LocalTime;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;

import hellofx.ui.CalEventHandler;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.stage.Stage;

public class CalendarViewDemo extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {

		CalendarView calendarView = new CalendarView();
		calendarView.setShowAddCalendarButton(false);
		calendarView.setShowDeveloperConsole(true);
		calendarView.setShowPrintButton(false);
		calendarView.showWeekPage();

//		calendarView.setShowSourceTray(true);
		
		Calendar birthdays = new Calendar("Birthdays");
		Calendar holidays = new Calendar("Holidays");

		birthdays.setStyle(Style.STYLE1);
		holidays.setStyle(Style.STYLE2);

		EventHandler<CalendarEvent> evHandler = new CalEventHandler();
		holidays.addEventHandler(evHandler);
		birthdays.addEventHandler(evHandler);
		
		CalendarSource myCalendarSource = new CalendarSource("My Calendars");
		myCalendarSource.getCalendars().addAll(birthdays, holidays);

		calendarView.getCalendarSources().addAll(myCalendarSource);

		calendarView.setRequestedTime(LocalTime.now());
		
		
		Entry<?> entry = new Entry<>("Testing");
//		entry.ch
		holidays.addEntry(entry);
		
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

		Scene scene = new Scene(calendarView);
		primaryStage.setTitle("Calendar");
		primaryStage.setScene(scene);
		primaryStage.setWidth(1300);
		primaryStage.setHeight(1000);
		primaryStage.centerOnScreen();
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}