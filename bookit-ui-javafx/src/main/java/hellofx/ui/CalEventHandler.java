package hellofx.ui;

import com.calendarfx.model.CalendarEvent;

import javafx.event.EventHandler;

public class CalEventHandler implements EventHandler<CalendarEvent> {

	@Override
	public void handle(CalendarEvent event) {
		System.out.println("Event occurr ed" + event);
	}

}
