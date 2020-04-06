
package com.anderson.bookit.ui.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import com.anderson.bookit.model.Project;
import com.fredrik.bookit.ui.rest.api.UsersApi;
import com.fredrik.bookit.ui.rest.invoker.ApiException;
import com.fredrik.bookit.ui.rest.model.UserDTO;
import com.fredrik.bookit.ui.rest.model.UserDTOList;

public class UserService {

	UsersApi usersApi = new UsersApi();

	public UserService() {
		
	}
	
	public List<UserDTO> getUsers() {
		System.out.println("Get Projects from backend.");

		Project p1 = new Project("P1");
		p1.setStartDate(LocalDate.of(2019, Month.OCTOBER, 29));
		p1.setEndDate(LocalDate.of(2019, Month.DECEMBER, 29));
		
		Project p2 = new Project("P2");
		p2.setStartDate(LocalDate.of(2019, Month.NOVEMBER, 1));
		p2.setEndDate(LocalDate.of(2019, Month.DECEMBER, 14));

		
//		return Arrays.asList(p1, p2);		
		
		UserDTOList userDTOList = null;
		try {
			userDTOList = usersApi.getUsers(null);
		
		} catch (ApiException e) {
			e.printStackTrace();
		}
		
		System.out.println("Users: " + userDTOList.toString());
		List<UserDTO> users = userDTOList.getItems();
//		
//		items.stream().forEach( proj -> { 
//			proj.startDate(LocalDate.now().minusDays(1)); 
//			proj.endDate(LocalDate.now().plusDays(3)); } );
		
		return users;
	}

	public UserDTO saveItem(UserDTO toEdit) {
		System.out.println("Save Project to backend: " + toEdit);		
		
		try {
			if (toEdit.getUserid() == null) {
				toEdit = usersApi.addUser(toEdit);				
			} else {
				usersApi.updateUser(toEdit.getUserid(), toEdit);
			}
			
		} catch (ApiException e) {
			e.printStackTrace();
		}		
		return toEdit;
	}

	public void deleteItem(UserDTO toEdit) {
		System.out.println("Delete user: " + toEdit);
		
		if (toEdit.getUserid() != null) {
			try {
				usersApi.deleteUser(toEdit.getUserid());
			} catch (ApiException e) {
				e.printStackTrace();
			}		
		}
	}

	public UserDTO loginUser(String userid, String password) {
		UserDTO loggedInUser = null;
		try {
			loggedInUser = usersApi.getLoggedInUser(userid, password);
		} catch (ApiException e) {
			e.printStackTrace();
		}		
		return loggedInUser;
	}

	
}
