package com.anderson.bookit.ui.controller;

import java.util.List;

import com.anderson.bookit.ui.service.UserService;
import com.anderson.bookit.ui.view.UserDialog;
import com.anderson.bookit.ui.view.UserView;
import com.fredrik.bookit.ui.rest.model.UserDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class UserController {

	UserService userService = new UserService();

	UserView view = new UserView();

	ObservableList<UserDTO> itemsModel;

	UserDTO toEdit = null;
	
	public UserController() {

		view.addRightClickMenu();
		view.addActionListener(new UserInListActionHandler());

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
		List<UserDTO> items = userService.getUsers();
		itemsModel = FXCollections.observableArrayList(items);
		
		view.setModelItems(itemsModel);
	}

	public Node getView() {
		return view;
	}

	private void showEditItemDialog(String text, UserDTO toEdit, int indexinView) {
		this.toEdit = toEdit;
		
		System.out.println("Should show Item Edit dialog with: " + text + ", " + toEdit.toString());
		
		UserDialog userDialog = new UserDialog(text, toEdit, indexinView);

		userDialog.addActionHandler(new UserActionHandler(userDialog));
//		itemDialog.editModel(text, toEdit, indexinView);
		
		userDialog.show();
	}

	class UserInListActionHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
			MenuItem mi = (MenuItem) event.getSource();
			String label = mi.getText();

//			if (mi.getUserData() != null) {
//				System.out.println("UserData: " + mi.getUserData().toString());
//			}
			if (mi.getUserData() != null) {
				actionOnObject(mi.getText(), (UserDTO) mi.getUserData()); // , view.getSelectionModel().getSelectedIndex());
			}
		}
	}
	
	class UserActionHandler implements EventHandler<ActionEvent> {

		UserDialog userDialog = null;
		
		public UserActionHandler(UserDialog userDialog) {
			this.userDialog = userDialog;			
		}
		
		@Override
        public void handle(ActionEvent event) {
			System.out.println("Action: " + userDialog.getAction()); // getSelectionModel().getSelectedItem().toString());
            
//            String inventory = itemDialog.getInventory();
            
            userDialog.close();

//            UserDTO userDTO = itemDialog.getModel();
//            
//            // Update backend
//            userDTO = userService.saveUser(userDTO);
            
            // Update UI
//            if (itemDialog.getAction().equalsIgnoreCase("new")) {
//            	itemsModel.add(userDTO);
//            	
//            	view.setModelItems(itemsModel);
//            } else {
//            	// Edit
//            	itemsModel.remove(userDTO);
//            	itemsModel.add(userDTO);
//            	
//            	view.setModelItems(itemsModel);            	
//            }
            
            
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

	public void actionOnObject(String action, UserDTO item) {
		UserDTO toEdit = item;
		if (action.toLowerCase().contains("new")) {
			toEdit = new UserDTO();
		} 
		if (action.toLowerCase().contains("delete")) {
			deleteItem(toEdit);
			
		} else {
			showEditItemDialog(action, toEdit, 1); // , indexinView);
		}			
		
	}

	public void deleteItem(UserDTO toEdit) {
		userService.deleteItem(toEdit);
		
		itemsModel.remove(toEdit);
		
		view.setModelItems(itemsModel);
	}

}
