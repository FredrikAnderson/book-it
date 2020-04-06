package com.anderson.bookit.ui.controller;

import java.util.List;

import com.anderson.bookit.ui.service.ItemService;
import com.anderson.bookit.ui.view.ItemBookingFormView;
import com.anderson.bookit.ui.view.ItemDialog;
import com.fredrik.bookit.ui.rest.model.ItemDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class ItemBookingFormController {

	ItemService itemService = new ItemService();

	ItemBookingFormView view = new ItemBookingFormView();

	ObservableList<ItemDTO> itemsModel;

	ItemDTO toEdit = null;
	
	public ItemBookingFormController() {

//		view.addRightClickMenu();
//		view.addActionListener(new ItemInListActionHandler());

//		view.addEventHandler(KeyEvent.KEY_RELEASED, (keyEvent) -> {
//			if (keyEvent.getCode() == KeyCode.DELETE) {
//				ItemDTO selectedItem = view.getSelectedItem();
//				deleteItem(selectedItem);
//			}
//			if (keyEvent.getCode() == KeyCode.F5) {
//				updateData();
//			}
//		});
		
		updateData();
	}

	public void updateData() {
		List<ItemDTO> items = itemService.getItems();
		itemsModel = FXCollections.observableArrayList(items);
		
//		view.setModelItems(itemsModel);
	}

	public Node getView() {
		return view;
	}

	private void showEditItemDialog(String text, ItemDTO toEdit, int indexinView) {
		this.toEdit = toEdit;
		
		System.out.println("Should show Item Edit dialog with: " + text + ", " + toEdit.toString());
		
		ItemDialog itemDialog = new ItemDialog(text, toEdit, indexinView);

		itemDialog.addActionHandler(new ItemActionHandler(itemDialog));
//		itemDialog.editModel(text, toEdit, indexinView);
		
		itemDialog.show();
	}

	class ItemInListActionHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			MenuItem mi = (MenuItem) event.getSource();
			String label = mi.getText();

//			if (mi.getUserData() != null) {
//				System.out.println("UserData: " + mi.getUserData().toString());
//			}
			if (mi.getUserData() != null) {
				actionOnObject(mi.getText(), (ItemDTO) mi.getUserData()); // , view.getSelectionModel().getSelectedIndex());
			}
		}
	}
	
	class ItemActionHandler implements EventHandler<ActionEvent> {

		ItemDialog itemDialog = null;
		
		public ItemActionHandler(ItemDialog itemDialog) {
			this.itemDialog = itemDialog;			
		}
		
		@Override
        public void handle(ActionEvent event) {
			System.out.println("Inv: " + itemDialog.getInventory()); // getSelectionModel().getSelectedItem().toString());
            
//            String inventory = itemDialog.getInventory();
            
            itemDialog.close();

            ItemDTO itemDTO = itemDialog.getModel();
            
            // Update backend
            itemDTO = itemService.saveItem(itemDTO);
            
            // Update UI
            if (itemDialog.getAction().equalsIgnoreCase("new")) {
            	itemsModel.add(itemDTO);
            	
//            	view.setModelItems(itemsModel);
            } else {
            	// Edit
            	itemsModel.remove(itemDTO);
            	itemsModel.add(itemDTO);
            	
//            	view.setModelItems(itemsModel);            	
            }
            
            
//            toEdit.setName(inventory);
//            toEdit.setStartDate(localStartDate.toLocalDate());
//            toEdit.setEndDate(localEndDate.toLocalDate());
//                        t
//    		ProjectDTO proj = itemService.saveProject(toEdit);
//    		toEdit = proj;
//    		
//    		if (projDialog.getAction().toLowerCase().contains("new")) {
//    			itemsModel.add(toEdit);
//    		} else {
//    			// Update project in model. First find project in model
//    			int indexOf = itemsModel.indexOf(toEdit);
//    			if (indexOf != -1) {
//    				itemsModel.set(indexOf, toEdit);
//    			}
//    		}
        }	
	}

	public void actionOnObject(String action, ItemDTO item) {
		ItemDTO toEdit = item;
		if (action.toLowerCase().contains("new")) {
			toEdit = new ItemDTO();
		} 
		if (action.toLowerCase().contains("delete")) {
			deleteItem(toEdit);
			
		} else {
			showEditItemDialog(action, toEdit, 1); // , indexinView);
		}			
		
	}

	public void deleteItem(ItemDTO toEdit) {
		itemService.deleteItem(toEdit);
		
		itemsModel.remove(toEdit);
		
//		view.setModelItems(itemsModel);
	}

}
