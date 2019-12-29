package com.anderson.bookit.ui;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;

public class DateTimePicker extends HBox {

	TimeChooserType type = TimeChooserType.DateTime;
	
	DatePicker datePr = new DatePicker();
	TimePicker timePr = new TimePicker();

	enum TimeChooserType {
		Date,
		DateTime
	};
	
	public DateTimePicker() {
		super();
		
		createPicker();
	}

	public DateTimePicker(TimeChooserType type) {
		super();
		this.type = type;
		
		createPicker();
	}
	
	private void createPicker() {

//		hbox = new HBox();
		super.setSpacing(2);
		super.getChildren().add(datePr);
		if (type.equals(TimeChooserType.DateTime)) {
			super.getChildren().add(timePr);			
		}
	}

	public void setLocalDate(LocalDate localDate) {
		datePr.setValue(localDate);
		timePr.setValue(LocalTime.of(0, 0));		
	}

	public void setLocalDateTime(LocalDateTime localDateTime) {
		if (localDateTime != null) {
			datePr.setValue(localDateTime.toLocalDate());
			timePr.setValue(localDateTime.toLocalTime());		
		}
	}

	public LocalDateTime getLocalDateTime() {
		LocalDate localDate = datePr.getValue();
		LocalTime localTime = timePr.getValue();
		
		LocalDateTime toret = LocalDateTime.of(localDate, localTime);		
		return toret;		
	}
	
}
