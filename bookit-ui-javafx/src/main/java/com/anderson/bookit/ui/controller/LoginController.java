package com.anderson.bookit.ui.controller;

import com.anderson.bookit.BookITUI;
import com.anderson.bookit.ui.service.UserService;
import com.anderson.bookit.ui.view.LoginView;
import com.fredrik.bookit.ui.rest.model.UserDTO;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;

public class LoginController {

	UserService userService = new UserService();

	LoginView view = new LoginView();
	
	public LoginController() {

		view.addActionHandler(new LoginActionHandler());
	}

	public Node getView() {
		return view;
	}

	class LoginActionHandler implements EventHandler<ActionEvent> {

		
		public LoginActionHandler() {
		}
		
		@Override
        public void handle(ActionEvent event) {
			System.out.println("Logging in user: " + view.getUsername() + ", passwd: " + view.getPassword()); 

			UserDTO userDTO = userService.loginUser(view.getUsername(), view.getPassword());
			
			BookITUI.getInstance().addMenuAtTop(userDTO.getRole());
			if (userDTO.getRole().equals("admin")) {
				// Default ui-view for user
				BookITUI.getInstance().showScene("timeline");				
				
			} else if (userDTO.getRole().equals("booker")) {
				BookITUI.getInstance().showScene("itembookingform");				
				
			}
        }	
	}

}
