package com.anderson.bookit.ui.component;


import java.time.LocalTime;

import javax.swing.tree.VariableHeightLayoutCache;

import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

public class TimePicker extends HBox {

	ComboBox<String> hourCbx = new ComboBox<String>();
	ComboBox<String> minsCbx = new ComboBox<String>();

	public TimePicker() {
		super();
		
		createPicker();		
	}
	
	private void createPicker() {

		addHours(hourCbx);
		Text colonTxt = new Text(":");
		addMinutes(minsCbx);
		
//		hbox = new HBox();
		super.setSpacing(2);
		
		super.getChildren().add(hourCbx);
		super.getChildren().add(colonTxt);
		super.getChildren().add(minsCbx);
	}

	private void addMinutes(ComboBox cbx) {

		for (int i = 0; i < 60; i++) {
			addValueInComboxBox(cbx, nrToString(i));
		}
	}

	private void addHours(ComboBox cbx) {

		for (int i = 0; i < 24; i++) {
			addValueInComboxBox(cbx, nrToString(i));
		}		
	}

	public LocalTime getValue() {
		String hour = ensureNumber(hourCbx.getValue());
		String min = ensureNumber(minsCbx.getValue());
		LocalTime toret = LocalTime.of(Integer.parseInt(hour), Integer.parseInt(min));

//		System.out.println("LocalTime: " + toret.toString());
		return toret;
	}

	private String ensureNumber(String value) {
		String toret = value;
		if (toret == null) {
			toret = "0";
		}
		return toret;
	}

	public void setValue(LocalTime localTime) {
		int hour = localTime.getHour();
		setValueInComboxBox(hourCbx, nrToString(hour));
		
		int minute = localTime.getMinute();
		setValueInComboxBox(minsCbx, nrToString(minute));		
	}
	
	private String nrToString(int nr) {
		String toret = "" + nr;
		if (nr < 10) {
			toret = "0" + nr;
		}
		return toret;
	}

	private void setValueInComboxBox(ComboBox<String> cbx, String value) {
		String first = cbx.getItems().stream().filter(it -> it.startsWith(value)).findFirst().get();
//		System.out.println("Found: " + first);
		cbx.getSelectionModel().select(first);
	}

	private void addValueInComboxBox(ComboBox<String> cbx, String value) {
		cbx.getItems().add(value);		
	}

}
