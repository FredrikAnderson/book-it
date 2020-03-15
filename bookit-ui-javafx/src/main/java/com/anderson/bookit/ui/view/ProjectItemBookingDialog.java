package com.anderson.bookit.ui.view;

import com.anderson.bookit.model.ProjectItemBooking;
import com.anderson.bookit.ui.service.ItemService;
import com.fredrik.bookit.ui.rest.model.ItemDTO;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProjectItemBookingDialog {

	Stage dialogStage = null;

    ItemService itemService = new ItemService();

    ProjectItemBooking bookingToEdit = null;
    ProjectDTO projectToEdit = null;
    
//	private ComboBox<ProjectDTO> projCbx;
	private TextField projectTf;
	private ComboBox<ItemDTO> itemCbx;

	private ItemView itemView;
	
	public ProjectItemBookingDialog() {
		
		dialogStage = new Stage();
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.setTitle("Book an Item to a Project");
		
		// Project
		Text projLbl = new Text("Project: ");
		projectTf = new TextField();

		// CHoosing a proj would set times for the booking
		HBox projHbx = new HBox(projLbl, projectTf);
		projHbx.setPadding(new Insets(20, 20, 0, 20));
		
		// Resource
		Text itemLbl = new Text("Item: ");
		itemCbx = new ComboBox<ItemDTO>();
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
	            	                
	                dialogStage.close();
	                
	                ProjectItemBooking booking = bookingToEdit;
	                
	                booking.setItem(selectedItem);
	                booking.setProject(projectToEdit);

	                // TODO DO backend save
//	                BookITUI.getInstance().getCalenderManager().updateEntryToBooking(entry, booking);
	            }
		});

		HBox btnHbx = new HBox(button);
		btnHbx.setAlignment(Pos.BASELINE_RIGHT);
		btnHbx.setPadding(new Insets(0, 20, 20, 0));
			
		VBox vbox = new VBox(projHbx, itemView, btnHbx);
		
		// Fix scene
		dialogStage.setScene(new Scene(vbox, 650, 400));
	}
	
	public void editModel(ProjectItemBooking toEdit) {
		bookingToEdit = toEdit;
		
		if (bookingToEdit != null) {
			bookingToEdit = toEdit;
		}
		System.out.println("Should edit booking: " + bookingToEdit.toString());		
		
		// Set select resource
		itemCbx.getSelectionModel().select(bookingToEdit.getItem());
		// Set select project
//		projCbx.getSelectionModel().select(bookingToEdit.getProject());
//		projCbx.setEditable(false);
//		projCbx.setDisable(true);
		projectToEdit = bookingToEdit.getProject();

		projectTf.setText(bookingToEdit.getProject().getName());
		projectTf.setEditable(false);
		projectTf.setStyle("-fx-background-color: lightgrey;");		
		
		dialogStage.show();
	}
	
}