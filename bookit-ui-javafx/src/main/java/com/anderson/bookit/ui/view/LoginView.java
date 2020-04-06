package com.anderson.bookit.ui.view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class LoginView extends BorderPane {

	Text userLbl = new Text("Username:");
	TextField usernameTf = new TextField();

	Text passwordLbl = new Text("Password:");
	PasswordField passwordTf = new PasswordField();

	Button okBtn = new Button("OK");

	public LoginView() {

		setPadding(new Insets(20, 20, 20, 20));

		HBox userVx = new HBox(20, userLbl, usernameTf);
		userVx.setPadding(new Insets(20, 20, 0, 0));

		HBox passVx = new HBox(22, passwordLbl, passwordTf);
		passVx.setPadding(new Insets(10, 20, 0, 0));

		HBox okVx = new HBox(okBtn);
		okVx.setPadding(new Insets(10, 20, 0, 185));

		VBox userPassVx = new VBox(userVx, passVx, okVx);

		setCenter(userPassVx);	
	}

	public void addActionHandler(EventHandler<ActionEvent> handler) {
		
		okBtn.setOnAction(handler);
	}
	
	public String getUsername() {
		return usernameTf.getText();
	}
	
	public String getPassword() {
		return passwordTf.getText();
	}

}
