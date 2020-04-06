package com.fredrik.bookit.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.fredrik.bookit.model.Project;
import com.fredrik.bookit.web.rest.model.ProjectDTO;


@Mapper( uses = ItemMapper.class)
public interface ProjectMapper {

	ProjectDTO mapEntityToDTO(Project project);

	Project mapDTOToEntity(ProjectDTO projectDto);

	List<ProjectDTO> mapEntitiesToDTOs(List<Project> projects);

	List<Project> mapDTOsToEntities(List<ProjectDTO> projects);

	
}