package com.fredrik.bookit.web.controller;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fredrik.bookit.booking.app.UserService;
import com.fredrik.bookit.web.rest.api.UsersApi;
import com.fredrik.bookit.web.rest.model.UserDTO;
import com.fredrik.bookit.web.rest.model.UserDTOList;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class UsersResource implements UsersApi {

	@Inject
	private UserService userService;

	@Override
	public ResponseEntity<UserDTO> addUser(@Valid UserDTO userDTO) {
		log.info("addUser: " + userDTO);
		UserDTO toret = null;
		
		toret = userService.save(userDTO);		
		return ResponseEntity.ok(toret);
	}

	@Override
    public ResponseEntity<UserDTO> getLoggedInUser(
    		@ApiParam(value = "ID of user to return", required=true) @PathVariable("id") String id,
    		@ApiParam(value = "Password for the user wanting to login", required=true) @PathVariable("password") String password) {
		
		UserDTO toret = userService.findOne(id);
		
//		toret = userService.save(userDTO);		
		return ResponseEntity.ok(toret);		
	}

	@Override
	public ResponseEntity<Void> deleteUser(String id) {
		log.info("deleteUser: " + id);

		userService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<UserDTO> getUserById(String id) {
		log.info("getUserById: " + id);
		
		UserDTO userDTO = userService.findOne(id);
		
		return ResponseEntity.ok(userDTO);
	}

	@Override
    public ResponseEntity<UserDTOList> getUsers(@Valid String name) {
		log.info("getUsers");
		
		List<UserDTO> dtos = null;
		if (Objects.nonNull(name) && !name.isEmpty()) {
			dtos = userService.findBy(name);
		} else {		
			dtos = userService.findAll();
		}
		UserDTOList dtoList = new UserDTOList();
		dtoList.setItems(dtos);
		
		return ResponseEntity.ok(dtoList);
	}

	@Override
	public ResponseEntity<Void> updateUser(String id, @Valid UserDTO userDTO) {
		log.info("updateItem: " + id);
//		ItemDTO toret = null;
		Void toret = null;

		userService.save(userDTO);		

		return ResponseEntity.ok(toret);
	}


	
}