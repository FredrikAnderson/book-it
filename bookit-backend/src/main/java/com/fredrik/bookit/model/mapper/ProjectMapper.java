package com.fredrik.bookit.model.mapper;

import org.mapstruct.Mapper;

import com.fredrik.bookit.model.Project;
import com.fredrik.bookit.web.rest.model.ProjectDTO;


@Mapper( uses = ItemMapper.class, componentModel = "spring")
public interface ProjectMapper extends BaseMapper<ProjectDTO, Project> {
	
}