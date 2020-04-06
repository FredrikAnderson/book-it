package com.fredrik.bookit.booking.app;

import java.util.List;

import com.fredrik.bookit.web.rest.model.UserDTO;

public interface UserService {

	List<UserDTO> findAll();

	List<UserDTO> findBy(String name);

    UserDTO findOne(String id);

    UserDTO findByName(String name);

	UserDTO save(UserDTO user);

	void delete(String id);

	
}