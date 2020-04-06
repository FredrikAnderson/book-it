package com.fredrik.bookit.model.mapper;

import org.mapstruct.Mapper;

import com.fredrik.bookit.model.User;
import com.fredrik.bookit.web.rest.model.UserDTO;

@Mapper
public interface UserMapper {

	UserDTO mapEntityToDTO(User entity);

	User mapDTOToEntity(UserDTO dto);

}