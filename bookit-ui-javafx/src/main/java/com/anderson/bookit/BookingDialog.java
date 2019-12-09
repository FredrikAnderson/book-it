package com.anderson.bookit;

import java.time.LocalDateTime;

import com.anderson.bookit.model.Booking;
import com.anderson.bookit.model.Project;
import com.anderson.bookit.model.Resource;
import com.anderson.bookit.service.ResourceService;
import com.anderson.bookit.ui.DateTimePicker;
import com.calendarfx.model.Entry;

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

public class BookingDialog {

	Stage dialogStage = null;

    ResourceService resourceService = new ResourceService();

    Entry<Booking> entryToEdit = null;
    Booking bookingToEdit = null;

	private ComboBox<Project> projCbx;
	private ComboBox<Resource> resCbx;
	
	private DateTimePicker startDateTimePr = new DateTimePicker();
	private DateTimePicker endDateTimePr = new DateTimePicker();
    
	public BookingDialog() {
		
		dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.setTitle("Edit Booking");
		
		GridPane gridP = new GridPane();
		
		// Setting the vertical and horizontal gaps between the columns
		gridP.setVgap(5);
		gridP.setHgap(5);

		// Setting the Grid alignment
		gridP.setAlignment(Pos.CENTER);

		// Resource
		Text resLbl = new Text("Resource: ");
		resCbx = new ComboBox<Resource>();
//		addResources(resCbx);
		
		
		
		gridP.add(resLbl, 0, 0);
		gridP.add(resCbx, 1, 0);

//		HBox resHbx = new HBox(resLbl, resCbx);
//		resHbx.setAlignment(Pos.CENTER_LEFT);
//		resHbx.setPadding(new Insets(10));

		// Project
		Text projLbl = new Text("Project: ");
		projCbx = new ComboBox<Project>();
				
		projCbx.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				Project value = projCbx.getValue();
				
//				System.out.println("SHould set" + value.getStartDate() + value.getEndDate());

				startDateTimePr.setLocalDate(value.getStartDate());
				endDateTimePr.setLocalDate(value.getEndDate());
			}			
		});
//		addProjects(projCbx);

		// CHoosing a proj would set times for the booking
		
		gridP.add(projLbl, 0, 1);
		gridP.add(projCbx, 1, 1);

		// Times
		// Start
		Text startTimeLbl = new Text("Start time: ");
				
		gridP.add(startTimeLbl, 0, 2);
		gridP.add(startDateTimePr, 1, 2);
		
		
		// End Time
		Text endTimeLbl = new Text("End time: ");
		
		gridP.add(endTimeLbl, 0, 3);
		gridP.add(endDateTimePr, 1, 3);
		
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
	                
	                Entry<Booking> entry = entryToEdit;
	                Booking booking = bookingToEdit;
	                
	                booking.setResource(resCbx.getValue());
	                booking.setProject(projCbx.getValue());
	                booking.setStartTime(startDateTimePr.getLocalDateTime());
	                booking.setEndTime(endDateTimePr.getLocalDateTime());
	                
	                BookITUI.getInstance().getCalenderManager().updateEntryToBooking(entry, booking);
	            }
		});
		gridP.add(button, 1, 4);
		
		// Fix scene
		dialogStage.setScene(new Scene(gridP, 400, 180));
	}
	
	public void editModel(Entry<Booking> toEdit) {
		entryToEdit = toEdit;
		
		if (entryToEdit != null) {
			bookingToEdit = toEdit.getUserObject();
		}
		System.out.println("Should edit booking: " + bookingToEdit.toString());		
		
		// Set select resource
		resCbx.getSelectionModel().select(bookingToEdit.getResource());
		// Set select project
		projCbx.getSelectionModel().select(bookingToEdit.getProject());

		// Set select time
		startDateTimePr.setLocalDateTime(bookingToEdit.getStartTime());
		// Set selected time
		endDateTimePr.setLocalDateTime(bookingToEdit.getEndTime());
		
		dialogStage.show();
	}
	
}