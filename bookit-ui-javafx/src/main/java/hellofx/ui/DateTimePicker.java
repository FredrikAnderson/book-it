package hellofx.ui;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class DateTimePicker extends HBox {

	DatePicker datePr = new DatePicker();
	TimePicker timePr = new TimePicker();

	public DateTimePicker() {
		super();
		
		createPicker();
	}
	
	private void createPicker() {

//		hbox = new HBox();
		super.setSpacing(2);
		super.getChildren().add(datePr);		
		super.getChildren().add(timePr);				
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
