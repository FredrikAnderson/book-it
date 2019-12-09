package com.fredrik.bookit.booking.app;

import java.util.List;

import com.fredrik.bookit.web.rest.model.ProjectDTO;

public interface ProjectService {

	List<ProjectDTO> getProjects();

	ProjectDTO findOne(Long id);

    ProjectDTO findByName(String productName);

	ProjectDTO saveProject(ProjectDTO proj);
	
	void deleteProject(Long id);

}