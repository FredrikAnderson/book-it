package com.anderson.bookit.ui.view;

import java.util.Collection;
import java.util.Objects;

import com.anderson.bookit.ui.FilterComboBox;
import com.anderson.bookit.ui.Lookup;
import com.anderson.bookit.ui.service.UserService;
import com.fredrik.bookit.ui.rest.model.ItemDTO;
import com.fredrik.bookit.ui.rest.model.UserDTO;

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

public class UserDialog extends Stage {

//	Stage dialogStage = null;

    UserService userService = new UserService();

    UserDTO toEdit = null;

    private TextField usernameTf;    
	private TextField nameTf;
	private TextField emailTf;	
	private TextField roleTf;	

	
	private String action;
	Button okButton = new Button("OK");
	Button cancelButton = new Button("Cancel");
	
	public UserDialog(String reqAction, UserDTO reqEdit, int indexinView) {		
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
			setTitle("New User");
			
		} else if (action.equalsIgnoreCase("edit")) {
			setTitle("Edit User");

		}
		
		// Username
		Text usernameLbl = new Text("Username: ");				
		usernameTf = new TextField();
		gridP.add(usernameLbl, 0, row);
		gridP.add(usernameTf, 1, row);
		row++;

		// Description
		Text nameLbl = new Text("Name: ");				
		nameTf = new TextField();
		gridP.add(nameLbl, 0, row);
		gridP.add(nameTf, 1, row);
		row++;

		Text emailLbl = new Text("Email: ");
		emailTf = new TextField();
		gridP.add(emailLbl, 0, row);
		gridP.add(emailTf, 1, row);
		row++;

		Text roleLbl = new Text("Role: ");
		roleTf = new TextField();
		gridP.add(roleLbl, 0, row);
		gridP.add(roleTf, 1, row);
		row++;

				
		HBox btnsHbx = new HBox(okButton, cancelButton);
		btnsHbx.setSpacing(10);
		
		gridP.add(btnsHbx, 1, row);

		if (action.equalsIgnoreCase("edit")) {
			
			editItem(toEdit);			
		}
		
		// Fix scene
		setScene(new Scene(gridP, 250, 180));
		
		cancelButton.setOnAction(new CloseDialogActionHandler());
		addEventHandler(KeyEvent.KEY_PRESSED, new CloseDialogKeyHandler());
		
		show();
	}

	private void editItem(UserDTO user) {
		System.out.println("editItem: " + user);

		// General item stuff
		usernameTf.setText(user.getUserid());
		nameTf.setText(user.getName());
		emailTf.setText(user.getEmail());
		roleTf.setText(user.getRole());
	}
	
	public UserDTO getModel() {
		
		toEdit.setUserid(usernameTf.getText());
		toEdit.setName(nameTf.getText());
		toEdit.setEmail(emailTf.getText());
		toEdit.setRole(roleTf.getText());
		
		return toEdit;
	}
	
	class ItemSelectedHandler implements EventHandler<ActionEvent> {
		@Override
        public void handle(ActionEvent event) {

			UserDTO selectedItem = null; // nameCbx.getSelectionModel().getSelectedItem();
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
	
	public String getAction() {
		return action;
	}

}