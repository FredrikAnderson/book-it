package com.anderson.bookit.ui.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.anderson.bookit.ui.service.ItemService;
import com.anderson.bookit.ui.view.ItemDialog;
import com.anderson.bookit.ui.view.ItemView;
import com.fredrik.bookit.ui.rest.model.ItemDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class ItemController {

	ItemService itemService = new ItemService();

	ItemView view = new ItemView();

	ObservableList<ItemDTO> itemsModel;

	ItemDTO toEdit = null;
	
	public ItemController() {

		view.addActionListener(new ItemInListActionHandler());
		
		List<ItemDTO> items = itemService.getItems();
		itemsModel = FXCollections.observableArrayList(items);
		
		view.setModelItems(itemsModel);
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
//			System.out.println("MenuChoice handling: " + event.getSource());

			MenuItem mi = (MenuItem) event.getSource();
			String label = mi.getText();

			System.out.println("Event: " + event);
			System.out.println("EventSrc: " + event.getSource());
			System.out.println("MenuItem: " + mi.getText() + ", " + mi.getClass());
			if (mi.getUserData() != null) {
				System.out.println("UserData: " + mi.getUserData().toString());
			}
			if (mi.getUserData() != null) {
				actionOnObject(mi.getText(), (ItemDTO) mi.getUserData()); // , view.getSelectionModel().getSelectedIndex());
			}

//			showScene(label);
		}
	}
	
	class ItemActionHandler implements EventHandler<ActionEvent> {

		ItemDialog itemDialog = null;
		
		public ItemActionHandler(ItemDialog itemDialog) {
			this.itemDialog = itemDialog;			
		}
		
		@Override
        public void handle(ActionEvent event) {
			System.out.println("Name: " + itemDialog.getProjectName()); // getSelectionModel().getSelectedItem().toString());
            
            String projectName = itemDialog.getProjectName();
//            LocalDateTime localStartDate = itemDialog.getStartDate();
//            LocalDateTime localEndDate = itemDialog.getEndDate();
            
            itemDialog.close();
            
            toEdit.setName(projectName);
//            toEdit.setStartDate(localStartDate.toLocalDate());
//            toEdit.setEndDate(localEndDate.toLocalDate());
//                        
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
			itemService.deleteItem(toEdit);
			
			itemsModel.remove(toEdit);
			
//			view.getItems().remove(toEdit);
			
		} else {
			showEditItemDialog(action, toEdit, 1); // , indexinView);
		}			
		
	}

}
