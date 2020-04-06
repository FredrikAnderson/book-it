package com.anderson.bookit.ui.view;

import com.anderson.bookit.model.ProjectItemBooking;
import com.anderson.bookit.ui.component.DateTimePicker;
import com.anderson.bookit.ui.component.DateTimePicker.TimeChooserType;
import com.anderson.bookit.ui.service.ItemService;
import com.anderson.bookit.ui.service.ProjectService;
import com.fredrik.bookit.ui.rest.model.ItemDTO;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;


public class ItemBookingFormView extends BorderPane {

	ItemService itemService = new ItemService();

	ProjectService projService = ProjectService.getInstance();

	ProjectItemBooking bookingToEdit = null;
	ProjectDTO projectToEdit = null;

	private TextField projectTf;
	private DateTimePicker startDateTimePr = new DateTimePicker(TimeChooserType.Date);
	private DateTimePicker endDateTimePr = new DateTimePicker(TimeChooserType.Date);
	
	private ItemView itemView;

	public ItemBookingFormView() {

		// Project
		Text projLbl = new Text("Project: ");
		projectTf = new TextField();

		GridPane gridP = new GridPane();
		gridP.setPadding(new Insets(20, 20, 0, 20));
		
		// Setting the vertical and horizontal gaps between the columns
		gridP.setVgap(5);
		gridP.setHgap(5);

		// Setting the Grid alignment
//		gridP.setAlignment(Pos.CENTER);

		// Choosing a proj would set times for the booking		
		gridP.add(projLbl, 0, 1);
		gridP.add(projectTf, 1, 1);
		
		// CHoosing a proj would set times for the booking
//		HBox projHbx = new HBox(projLbl, projectTf);
//		projHbx.setPadding(new Insets(20, 20, 0, 20));

		// Times, Start
		Text startDateLbl = new Text("Start date: ");
		gridP.add(startDateLbl, 0, 2);
		gridP.add(startDateTimePr, 1, 2);
		
//		HBox startDateHbx = new HBox(startDateLbl, startDateTimePr);
//		startDateHbx.setPadding(new Insets(20, 20, 0, 20));

		// End Time
		Text endDateLbl = new Text("End date: ");
		gridP.add(endDateLbl, 0, 3);
		gridP.add(endDateTimePr, 1, 3);

//		HBox endDateHbx = new HBox(endDateLbl, endDateTimePr);
//		endDateHbx.setPadding(new Insets(20, 20, 0, 20));
				
		itemView = new ItemView();
		itemView.setModelItems(FXCollections.observableArrayList(itemService.getItems()));

		Button button = new Button("OK");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				ItemDTO selectedItem = itemView.getSelectedItem();

				System.out.println("Should save project/item booking:");
				System.out.println("Proj: " + projectTf.getText()); // getSelectionModel().getSelectedItem().toString());
				System.out.println("Item: " + selectedItem);


				ProjectItemBooking booking = bookingToEdit;

				booking.setItem(selectedItem);
				booking.setProject(projectToEdit);

				projService.bookItemForProject(projectToEdit, selectedItem.getId());
			}
		});

		HBox btnHbx = new HBox(button);
		btnHbx.setAlignment(Pos.BASELINE_RIGHT);
		btnHbx.setPadding(new Insets(0, 20, 20, 0));

		VBox vbox = new VBox(gridP, itemView, btnHbx);

		setCenter(vbox);
	}

	public void editModel(ProjectItemBooking toEdit) {
		bookingToEdit = toEdit;

		if (bookingToEdit != null) {
			bookingToEdit = toEdit;
		}
		System.out.println("Should edit booking: " + bookingToEdit.toString());

		// Set select resource
		projectToEdit = bookingToEdit.getProject();

		projectTf.setText(bookingToEdit.getProject().getName());
		projectTf.setEditable(false);
		projectTf.setStyle("-fx-background-color: lightgrey;");

	}

}