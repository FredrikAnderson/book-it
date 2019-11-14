package hellofx;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.calendarfx.model.Calendar;
import com.calendarfx.model.Calendar.Style;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.Entry;
import com.calendarfx.view.DateControl.CreateCalendarSourceParameter;

import hellofx.ui.CalEventHandler;
import hellofx.ui.DateTimePicker;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Booking;
import model.Project;
import model.Resource;
import service.ProjectService;
import service.ResourceService;

public class CalendarManager {

	private static CalendarManager myInstance = null;
	
	private ArrayList<Calendar> calendars = new ArrayList<Calendar>();

	private ResourceService resourceService = new ResourceService();

	private ProjectService projectService = new ProjectService();

	
//	private Entry currentEntry;
	
	
	private CalendarManager() {
		
		createCalendars();
	}
	
	public static CalendarManager getInstance() {
		if (myInstance == null) {
			myInstance = new CalendarManager();
		}
		return myInstance;
	}

	private List<Calendar> createCalendars() {

		List<Project> projects = projectService.getProjects();

		for (Project proj : projects) {
			calendars.add(createCalendarForProject(proj));
		}

		List<Resource> resources = resourceService.getResources();
		for (Resource res : resources) {
			calendars.add(createCalendarForResource(res));
		}

		Calendar birthdays = new Calendar("Birthdays");
		Calendar holidays = new Calendar("Holidays");

		birthdays.setStyle(Style.STYLE1);
		holidays.setStyle(Style.STYLE2);

		Entry<?> entry = new Entry<>("Testing");
//		entry.ch
		holidays.addEntry(entry);

		EventHandler<CalendarEvent> evHandler = new CalEventHandler();
		holidays.addEventHandler(evHandler);
		birthdays.addEventHandler(evHandler);

		calendars.add(birthdays);
		calendars.add(holidays);

		return calendars;
	}

	public List<Calendar> getCalendars() {
		return calendars;
	}
	
	public Calendar createCalendarForResource(Resource res) {
		Calendar cal = new Calendar(res.getName());

//		Entry<?> entry = new Entry<>("Resource: " + res.getName());
////			entry.ch
//		entry.setFullDay(true);
//		entry.setInterval(proj.getStartDate(), proj.getEndDate());
//		cal.addEntry(entry);		

		List<Booking> bookings = resourceService.getBookings(res);

		List<Entry<Booking>> entries = createEntriesFromBookings(bookings);
		linkEntriesToCalendar(cal, entries);
		
		EventHandler<CalendarEvent> evHandler = new CalEventHandler();
		cal.addEventHandler(evHandler);
		return cal;
	}

	public Calendar getCalendar(String resourceName) {
		
		return calendars.stream().filter(cal -> cal.getName().equalsIgnoreCase(resourceName)).findFirst().get();		
	}
	

	public void linkEntryToBooking(Entry<Booking> entry) {
		
		Booking booking = entry.getUserObject();
		Calendar calendar = getCalendar(booking.getResource().getName());			
		linkEntriesToCalendar(calendar, Arrays.asList(entry));
	}

//	private void addBookings(List<Booking> bookings) {
//		
//		for (Booking booking : bookings) {			
//			Calendar calendar = getCalendar(booking.getResource().getName());			
//			linkEntriesToCalendar(calendar, Arrays.asList(booking));
//		}
//	}

	private void linkEntriesToCalendar(Calendar cal, List<Entry<Booking>> entries) {
		
		for (Entry<Booking> entry : entries) {

			if (entry.getCalendar() == null) {
				entry.setCalendar(cal);
			}			
		}
	}

	private Calendar createCalendarForProject(Project proj) {
		Calendar cal = new Calendar(proj.getName());

		Entry<?> entry = new Entry<>("Project: " + proj.getName());
//			entry.ch
		entry.setFullDay(true);
		
		entry.setInterval(proj.getStartDate(), proj.getEndDate());
		cal.addEntry(entry);

		EventHandler<CalendarEvent> evHandler = new CalEventHandler();
		cal.addEventHandler(evHandler);

		return cal;
	}

	public static List<Entry<Booking>> createEntriesFromBookings(List<Booking> bookings) {
		List<Entry<Booking>> toret = new ArrayList<Entry<Booking>>(bookings.size());
		
		for (Booking booking : bookings) {
			Entry<Booking> fromBooking = updateEntryFromBooking(null, booking);
			fromBooking.setUserObject(booking);
			toret.add(fromBooking);
		}
		
		return toret;		
	}
	
	public static Entry<Booking> updateEntryFromBooking(Entry<Booking> entry, Booking booking) {
		if (entry == null) {
			entry = new Entry<Booking>();
		}
		entry.setInterval(booking.getStartTime(), booking.getEndTime());
		entry.setTitle("Resource: " + booking.getResource().getName());
		entry.setUserObject(booking);
	
		return entry;
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

}