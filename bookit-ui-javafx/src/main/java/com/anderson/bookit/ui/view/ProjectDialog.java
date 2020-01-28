package com.anderson.bookit.ui.view;

import java.time.LocalDateTime;

import com.anderson.bookit.ui.component.DateTimePicker;
import com.anderson.bookit.ui.component.DateTimePicker.TimeChooserType;
import com.anderson.bookit.ui.service.ProjectService;
import com.anderson.bookit.ui.view.ItemDialog.CloseDialogActionHandler;
import com.anderson.bookit.ui.view.ItemDialog.CloseDialogKeyHandler;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProjectDialog extends Stage {

//	Stage dialogStage = null;

    ProjectService projectService = ProjectService.getInstance();

    ProjectDTO toEdit = null;

	private TextField nameTf;	
	private DateTimePicker startDateTimePr = new DateTimePicker(TimeChooserType.Date);
	private DateTimePicker endDateTimePr = new DateTimePicker(TimeChooserType.Date);

	private int editingIndex;

	private String action;
	Button okButton = new Button("OK");
	Button cancelButton = new Button("Cancel");
    
	public ProjectDialog() {		
		super(); 
		initModality(Modality.APPLICATION_MODAL);
		setTitle("Edit Project");
		
		GridPane gridP = new GridPane();
		
		// Setting the vertical and horizontal gaps between the columns
		gridP.setVgap(5);
		gridP.setHgap(5);

		// Setting the Grid alignment
		gridP.setAlignment(Pos.CENTER);


//		HBox resHbx = new HBox(resLbl, resCbx);
//		resHbx.setAlignment(Pos.CENTER_LEFT);
//		resHbx.setPadding(new Insets(10));

		// Project
		Text projLbl = new Text("Name: ");
		nameTf = new TextField();
				
//		addProjects(projCbx);

		// Choosing a proj would set times for the booking		
		gridP.add(projLbl, 0, 1);
		gridP.add(nameTf, 1, 1);

		// Times
		// Start
		Text startTimeLbl = new Text("Start time: ");
				
		gridP.add(startTimeLbl, 0, 2);
		gridP.add(startDateTimePr, 1, 2);
		
		
		// End Time
		Text endTimeLbl = new Text("End time: ");
		
		gridP.add(endTimeLbl, 0, 3);
		gridP.add(endDateTimePr, 1, 3);
		
		HBox btnsHbx = new HBox(okButton, cancelButton);
		btnsHbx.setSpacing(10);
		
		gridP.add(btnsHbx, 1, 4);
		
		// Fix scene
		setScene(new Scene(gridP, 270, 180));
		
		cancelButton.setOnAction(new CloseDialogActionHandler());
		addEventHandler(KeyEvent.KEY_PRESSED, new CloseDialogKeyHandler());
	}

	class CloseDialogActionHandler implements EventHandler<ActionEvent> {
		@Override
        public void handle(ActionEvent event) {
			close();
        }
	}

	class CloseDialogKeyHandler implements EventHandler<KeyEvent> {

		@Override
        public void handle(KeyEvent event) {
			if (event.getCode() == KeyCode.ESCAPE) {
				close();
			}
        }
	}
	
	public void addActionHandler(EventHandler<ActionEvent> handler) {
		
		okButton.setOnAction(handler);
	}
	
	public void editModel(String reqAction, ProjectDTO reqEdit, int indexinView) {
		action = reqAction;
		toEdit = reqEdit;
		editingIndex = indexinView;
		
		System.out.println("Should edit proj: " + toEdit.toString());		
		
		// Set project name
		nameTf.setText(toEdit.getName());

		// Set select time
		startDateTimePr.setLocalDate(toEdit.getStartDate());
		// Set selected time
		endDateTimePr.setLocalDate(toEdit.getEndDate());
		
		show();
	}
	
	public String getProjectName() {
		return nameTf.getText();
	}
	
	public LocalDateTime getStartDate() {
		return startDateTimePr.getLocalDateTime();
	}

	public LocalDateTime getEndDate() {
		return endDateTimePr.getLocalDateTime();
	}

	public String getAction() {
		return action;
	}

}