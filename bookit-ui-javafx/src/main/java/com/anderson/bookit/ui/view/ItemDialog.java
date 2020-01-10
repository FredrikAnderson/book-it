package com.anderson.bookit.ui.view;

import com.anderson.bookit.ui.service.ItemService;
import com.fredrik.bookit.ui.rest.model.ItemDTO;

import javafx.event.ActionEvent;
import javafx.event.Event;
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

public class ItemDialog extends Stage {

//	Stage dialogStage = null;

    ItemService itemService = new ItemService();

    ItemDTO toEdit = null;

    // Look into resolving / autocomplete as per name
	private TextField nameTf;
	private TextField widthTf;	
	private TextField heightTf;	
	private TextField lengthTf;	
	private TextField weightTf;	
	private TextField priceTf;	

	// Item
	private TextField inventoryTf;	
	private TextField descTf;	

	
	private int editingIndex;

	private String action;
	Button okButton = new Button("OK");
	Button cancelButton = new Button("Cancel");
    
	public ItemDialog() {		
		super(); 
		initModality(Modality.APPLICATION_MODAL);
		setTitle("Edit Item");
		
		GridPane gridP = new GridPane();
		
		// Setting the vertical and horizontal gaps between the columns
		gridP.setVgap(5);
		gridP.setHgap(5);

		// Setting the Grid alignment
		gridP.setAlignment(Pos.CENTER);


//		HBox resHbx = new HBox(resLbl, resCbx);
//		resHbx.setAlignment(Pos.CENTER_LEFT);
//		resHbx.setPadding(new Insets(10));

		int row = 0;
		
		// Name
		Text projLbl = new Text("Name: ");
		nameTf = new TextField();
		gridP.add(projLbl, 0, row);
		gridP.add(nameTf, 1, row);
		row++;

		Text widthLbl = new Text("Width: ");
		widthTf = new TextField();
		gridP.add(widthLbl, 0, row);
		gridP.add(widthTf, 1, row);
		row++;


		Text heightLbl = new Text("Heigth: ");
		heightTf = new TextField();
		gridP.add(heightLbl, 0, row);
		gridP.add(heightTf, 1, row);
		row++;

		Text legnthLbl = new Text("Length: ");
		lengthTf = new TextField();
		gridP.add(legnthLbl, 0, row);
		gridP.add(lengthTf, 1, row);
		row++;

		Text weightLbl = new Text("Weight: ");
		weightTf = new TextField();
		gridP.add(weightLbl, 0, row);
		gridP.add(weightTf, 1, row);
		row++;

		Text priceLbl = new Text("Price: ");
		priceTf = new TextField();
		gridP.add(priceLbl, 0, row);
		gridP.add(priceTf, 1, row);
		row++;

		
		// Inventory
		Text inventoryLbl = new Text("Inventory: ");				
		inventoryTf = new TextField();
		gridP.add(inventoryLbl, 0, row);
		gridP.add(inventoryTf, 1, row);
		row++;
		
		// Description
		Text descLbl = new Text("Description: ");				
		descTf = new TextField();
		gridP.add(descLbl, 0, row);
		gridP.add(descTf, 1, row);
		row++;

		HBox btnsHbx = new HBox(okButton, cancelButton);
		btnsHbx.setSpacing(10);
		
		gridP.add(btnsHbx, 1, row);
		
		// Fix scene
		setScene(new Scene(gridP, 270, 320));
		
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
	
	public void editModel(String reqAction, ItemDTO reqEdit, int indexinView) {
		action = reqAction;
		toEdit = reqEdit;
		editingIndex = indexinView;
		
		System.out.println("Should make action " + action + " on proj: " + toEdit.toString());		

		if (action.equalsIgnoreCase("edit")) {
			// Set name
			nameTf.setText(toEdit.getName());
			inventoryTf.setText(toEdit.getInventory());
			descTf.setText(toEdit.getDescription());
	
			heightTf.setText(toEdit.getHeight().toString());
			widthTf.setText(toEdit.getWidth().toString());
			lengthTf.setText(toEdit.getLength().toString());
			weightTf.setText(toEdit.getWeight().toString());
			priceTf.setText(toEdit.getPrice().toString());
		}
		
		show();
	}
	
	public String getProjectName() {
		return nameTf.getText();
	}
	
//	public LocalDateTime getStartDate() {
//		return startDateTimePr.getLocalDateTime();
//	}
//
//	public LocalDateTime getEndDate() {
//		return endDateTimePr.getLocalDateTime();
//	}

	public String getAction() {
		return action;
	}

}