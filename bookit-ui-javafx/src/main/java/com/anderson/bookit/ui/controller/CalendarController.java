package com.anderson.bookit.ui.controller;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.anderson.bookit.ContextMenuCallback;
import com.anderson.bookit.EntryContextMenuCallback;
import com.anderson.bookit.EntryDetailsManager;
import com.anderson.bookit.model.Booking;
import com.anderson.bookit.model.Resource;
import com.anderson.bookit.ui.CalEventHandler;
import com.anderson.bookit.ui.service.ItemModificationListener;
import com.anderson.bookit.ui.service.ProjectService;
import com.anderson.bookit.ui.service.ResourceService;
import com.anderson.bookit.ui.service.ItemModificationListener.ItemEvent;
import com.calendarfx.model.Calendar;
import com.calendarfx.model.CalendarEvent;
import com.calendarfx.model.CalendarSource;
import com.calendarfx.model.Entry;
import com.calendarfx.view.CalendarView;
import com.calendarfx.view.DateControl.ContextMenuParameter;
import com.calendarfx.view.DateControl.EntryContextMenuParameter;
import com.calendarfx.view.DateControl.EntryDetailsParameter;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.util.Callback;

public class CalendarController implements ItemModificationListener<Long, ProjectDTO> {

	private static CalendarController myInstance = null;
	
	private CalendarView view = new CalendarView();

	private ResourceService resourceService = new ResourceService();
	private ProjectService projectService = ProjectService.getInstance();

	private CalendarSource calendarSrc = new CalendarSource("Calendars");
	
	private ArrayList<Calendar> calendars = new ArrayList<Calendar>();
	ObservableList<ProjectDTO> projectsModel;	
	
	private CalendarController() {

		initView();

		projectService.addItemListener(this);
		
		List<ProjectDTO> projects = projectService.getProjects();
		projectsModel = FXCollections.observableArrayList(projects);

		updateData();
	}
	
	private void initView() {
		view.setShowAddCalendarButton(false);
		view.setShowDeveloperConsole(true);
		view.setShowPrintButton(false);
		view.showWeekPage();

//		calendarView.setShowSourceTray(true);23

		view.setRequestedTime(LocalTime.now());

		Callback<EntryDetailsParameter, Boolean> callback = new EntryDetailsManager<EntryDetailsParameter, Boolean>();
		view.setEntryDetailsCallback(callback);

		Callback<ContextMenuParameter, ContextMenu> contextMenuCallback = new ContextMenuCallback<ContextMenuParameter, ContextMenu>();
		view.setContextMenuCallback(contextMenuCallback);

		Callback<EntryContextMenuParameter, ContextMenu> entryContextMenuCallback = new EntryContextMenuCallback<EntryContextMenuParameter, ContextMenu>();
		view.setEntryContextMenuCallback(entryContextMenuCallback);

	}

	public static CalendarController getInstance() {
		if (myInstance == null) {
			myInstance = new CalendarController();
		}
		return myInstance;
	}

	public Node getView() {
		return view;
	}

	public void updateData() {

		calendars.clear();
		calendarSrc.getCalendars().clear();
		view.getCalendarSources().clear();

		for (ProjectDTO proj : projectsModel) {
			calendars.add(createCalendarForProject(proj));
		}
		calendarSrc.getCalendars().addAll(calendars);
		view.getCalendarSources().addAll(calendarSrc);
		
		view.refreshData();
	}

	@Override
	public void itemModified(Long id, ItemEvent event, ProjectDTO item) {
		System.out.println("itemModified: " + id + ", " + event + ": " + item.toString());

		if (event == ItemEvent.ADD) {
			projectsModel.add(item);
			
		} else if (event == ItemEvent.EDIT) {
			int indexOf = projectsModel.indexOf(item);
			if (indexOf != -1) {
				projectsModel.set(indexOf, item);
			}			
			
		} else if (event == ItemEvent.DELETE) {
			projectsModel.remove(item);			
			
		}

		updateData();
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
	

	public void updateEntryToBooking(Entry<Booking> entry, Booking editedBooking) {
		
        entry = resourceService.updateEntryWithBooking(entry, editedBooking);

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

	private Calendar createCalendarForProject(ProjectDTO proj) {
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

}