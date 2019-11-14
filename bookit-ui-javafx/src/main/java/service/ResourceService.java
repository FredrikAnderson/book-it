
package service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.calendarfx.model.Entry;

import hellofx.CalendarManager;
import model.Booking;
import model.Resource;

public class ResourceService {

	public List<Resource> getResources() {
		System.out.println("Get Resources from backend.");

		Resource r1 = new Resource();
		r1.setId("R1");
		r1.setName("Resurs 1");
		Resource r2 = new Resource();
		r2.setId("R2");
		r2.setName("Res2");
		Resource r3 = new Resource();
		r3.setId("R3");
		r3.setName("Chair");
		Resource r4 = new Resource();
		r4.setId("R4");
		r4.setName("Flag");

		return Arrays.asList(r1, r2, r3, r4);
	}

	public List<Booking> getBookings(Resource res) {
		System.out.println("Get Bookings from backend for resource: " + res.getName());
		
		List<Booking> toret = new ArrayList<Booking>();
		
		if (res.getName().toLowerCase().contains("res")) {
		
			Booking b1 = new Booking();
			b1.setResource(res);
			LocalDateTime startTime = LocalDateTime.of(2019, Month.NOVEMBER, 12, 13, 30);
			b1.setStartTime(startTime);
			LocalDateTime endTime = LocalDateTime.of(2019, Month.NOVEMBER, 15, 17, 30);
			b1.setEndTime(endTime);		
			toret.add(b1);
			
			Booking b2 = new Booking();
			b2.setResource(res);
			startTime = LocalDateTime.of(2019, Month.NOVEMBER, 14, 11, 20);
			b2.setStartTime(startTime);
			endTime = LocalDateTime.of(2019, Month.NOVEMBER, 17, 16, 20);
			b2.setEndTime(endTime);
			toret.add(b2);
		}
		
		return toret;
	}

	public Entry<Booking> updateEntryWithBooking(Entry<Booking> toEdit, Booking booking) {

		toEdit = CalendarManager.updateEntryFromBooking(toEdit, booking);		
		
		// Save Booking to backend
		System.out.println("Saving booking to backend: " + booking.toString());
		
		return toEdit;
	}


}
