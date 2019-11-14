package com.fredrik.bookit.model.mapper;

import org.mapstruct.Mapper;

import com.fredrik.bookit.model.Resource;
import com.fredrik.bookit.web.rest.model.ResourceDTO;


@Mapper
public interface ResourceMapper {

	ResourceDTO toResourceDTO(Resource resource);
    
}