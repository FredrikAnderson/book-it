package com.fredrik.bookit.model.mapper;

import org.mapstruct.Mapper;

import com.fredrik.bookit.model.User;
import com.fredrik.bookit.web.rest.model.UserDTO;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<UserDTO, User> {

}