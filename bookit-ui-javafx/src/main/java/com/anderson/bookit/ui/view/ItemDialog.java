package com.anderson.bookit.ui.view;

import java.util.Collection;
import java.util.Objects;

import com.anderson.bookit.HelloFX;
import com.anderson.bookit.ui.FilterComboBox;
import com.anderson.bookit.ui.Lookup;
import com.anderson.bookit.ui.service.ItemService;
import com.fredrik.bookit.ui.rest.model.ItemDTO;

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
import javafx.util.StringConverter;

public class ItemDialog extends Stage {

//	Stage dialogStage = null;

    ItemService itemService = new ItemService();

    ItemDTO toEdit = null;

    // Look into resolving / autocomplete as per name
    private FilterComboBox<ItemDTO> itemNameCbx;
    
	private TextField nameTf;
	private TextField widthTf;	
	private TextField heightTf;	
	private TextField lengthTf;	
	private TextField weightTf;	
	private TextField priceTf;	

	// Item
	private TextField inventoryTf;	
	private TextField descTf;	

	
	private String action;
	Button okButton = new Button("OK");
	Button cancelButton = new Button("Cancel");

    private static <T> StringConverter<ItemDTO> itemStringConverter() {
        return new StringConverter<ItemDTO>() {
            @Override public String toString(ItemDTO t) {
                return t == null ? null : t.getName();
            }

            @Override public ItemDTO fromString(String string) {
            	ItemDTO dto = new ItemDTO();
            	dto.setName(string);
                return dto;
            }
        };
    }

	
	public ItemDialog(String reqAction, ItemDTO reqEdit, int indexinView) {		
		super(); 
		initModality(Modality.APPLICATION_MODAL);

		action = reqAction;
		toEdit = reqEdit;
				
		System.out.println("Should make action " + action + " on proj: " + toEdit.toString());		
		
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
		
		if (action.equalsIgnoreCase("new")) {
			setTitle("New Item");
			
			// Name
			Text proj1Lbl = new Text("Name: ");
			itemNameCbx = new FilterComboBox<>();
			itemNameCbx.setConverter(itemStringConverter());
			itemNameCbx.setOnAction(new ItemSelectedHandler());
			
			gridP.add(proj1Lbl, 0, row);
			gridP.add(itemNameCbx, 1, row);
			row++;
			Lookup<ItemDTO> lookup = new Lookup<ItemDTO>() {

				@Override
				public Collection<ItemDTO> lookup(String prefix) {
					return itemService.lookupItems(prefix);
				}
			};
			itemNameCbx.setLookup(lookup);

		} else if (action.equalsIgnoreCase("edit")) {
			setTitle("Edit Item");

			
			Text projLbl = new Text("Name: ");
			nameTf = new TextField();
			gridP.add(projLbl, 0, row);
			gridP.add(nameTf, 1, row);
			row++;

		}
		
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
				
		HBox btnsHbx = new HBox(okButton, cancelButton);
		btnsHbx.setSpacing(10);
		
		gridP.add(btnsHbx, 1, row);

		if (action.equalsIgnoreCase("edit")) {
			
			editItem(toEdit);			
		}
		
		// Fix scene
		setScene(new Scene(gridP, 270, 320));
		
		cancelButton.setOnAction(new CloseDialogActionHandler());
		addEventHandler(KeyEvent.KEY_PRESSED, new CloseDialogKeyHandler());
		
		show();
	}

	private void editItem(ItemDTO item) {
		System.out.println("editItem: " + item);

		// Item stuff
		if (Objects.nonNull(item.getInventory())) {
			inventoryTf.setText(item.getInventory());
		}

		// General item stuff
		// Set name
		if (action.equalsIgnoreCase("edit") && Objects.nonNull(item.getName())) {
//			itemNameCbx.setValue(item);
			nameTf.setText(item.getName());
			setTfReadOnly(nameTf);
		}
		if (Objects.nonNull(item.getDescription())) {
			descTf.setText(item.getDescription());	
			setTfReadOnly(descTf);
		}
		if (Objects.nonNull(item.getHeight())) {
			heightTf.setText(item.getHeight().toString());
			setTfReadOnly(heightTf);
		}
		if (Objects.nonNull(item.getWidth())) {
			widthTf.setText(item.getWidth().toString());
			setTfReadOnly(widthTf);
		}
		if (Objects.nonNull(item.getLength())) {
			lengthTf.setText(item.getLength().toString());
			setTfReadOnly(lengthTf);
		}
		if (Objects.nonNull(item.getWeight())) {
			weightTf.setText(item.getWeight().toString());
			setTfReadOnly(weightTf);
		}
		if (Objects.nonNull(item.getPrice())) {
			priceTf.setText(item.getPrice().toString());
			setTfReadOnly(priceTf);
		}
	}

	private String getName() {
		if (action.equalsIgnoreCase("edit")) {
			return nameTf.getText();
		} else {
			return itemNameCbx.getSelectionModel().getSelectedItem().getName();
		}		
	}
	
	public ItemDTO getModel() {
		
		toEdit.setInventory(inventoryTf.getText());

		// General item stuff
		toEdit.setName(getName());
		
		toEdit.setDescription(descTf.getText());
		if (!heightTf.getText().isEmpty()) {
			toEdit.setHeight(Float.parseFloat(heightTf.getText()));
		}
		if (!widthTf.getText().isEmpty()) {
			toEdit.setWidth(Float.parseFloat(widthTf.getText()));
		}
		if (!lengthTf.getText().isEmpty()) {
			toEdit.setLength(Float.parseFloat(lengthTf.getText()));
		}
		if (!weightTf.getText().isEmpty()) {
			toEdit.setWeight(Float.parseFloat(weightTf.getText()));
		}
		if (!priceTf.getText().isEmpty()) {		
			toEdit.setPrice(Float.parseFloat(priceTf.getText()));
		}
		
		return toEdit;
	}
	
	class ItemSelectedHandler implements EventHandler<ActionEvent> {
		@Override
        public void handle(ActionEvent event) {
//			System.out.println("Event"+ event.toString());

			ItemDTO selectedItem = itemNameCbx.getSelectionModel().getSelectedItem();
//			if (Objects.nonNull(selectedItem) && Objects.nonNull(selectedItem.getId())) {
			if (Objects.nonNull(selectedItem)) {
				System.out.println("Selected item is: " + selectedItem);
				
				editItem(selectedItem);
			}			
        }
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
		
	private void setTfReadOnly(TextField tf) {
		tf.setEditable(false);
		tf.setStyle("-fx-background-color: lightgrey;");		
	}
	
	public String getInventory() {
		return inventoryTf.getText();
	}
	
	public String getAction() {
		return action;
	}

}