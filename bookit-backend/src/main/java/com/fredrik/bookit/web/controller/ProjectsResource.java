package com.fredrik.bookit.web.controller;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fredrik.bookit.booking.app.ProjectService;
import com.fredrik.bookit.web.rest.api.ProjectsApi;
import com.fredrik.bookit.web.rest.model.ProjectDTO;
import com.fredrik.bookit.web.rest.model.ProjectDTOList;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class ProjectsResource implements ProjectsApi {
	
	@Inject
	private ProjectService projectService;


	@Override
	public ResponseEntity<ProjectDTOList> getProjects() {
		
		List<ProjectDTO> projects = projectService.getProjects();

		ProjectDTOList projList = new ProjectDTOList();
		projList.setItems(projects);
		
		return ResponseEntity.ok(projList);
	}

	@Override
	public ResponseEntity<ProjectDTO> addProject(@Valid ProjectDTO projectDTO) {
		log.info("addProject: " + projectDTO);
		ProjectDTO toret = null;
		
		toret = projectService.saveProject(projectDTO);		
		return ResponseEntity.ok(toret);
	}

	@Override
	public ResponseEntity<Void> deleteProject(Long id) {
		log.info("deleteProject: " + id);

		projectService.deleteProject(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	@Override
	public ResponseEntity<ProjectDTO> getProjectById(Long id) {
		log.info("getProjectById: " + id);
		
		ProjectDTO projectDTO = projectService.findOne(id);
		
		return ResponseEntity.ok(projectDTO);
	}

	@Override
	public ResponseEntity<ProjectDTO> updateProject(Long id, @Valid ProjectDTO projectDTO) {
		log.info("updateProject: " + id);
		ProjectDTO toret = null;
		
		toret = projectService.saveProject(projectDTO);		
		return ResponseEntity.ok(toret);
	}


	
//	public ResponseEntity<ProjectDTO> addProject(@Valid ProjectDTO projectDTO) {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public ResponseEntity<Void> deleteProject(Long id) {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public ResponseEntity<ProjectDTO> getProjectById(Long id) {
//	// TODO Auto-generated method stub
//	return null;
//}
//
//@Override
//public ResponseEntity<Void> updateProject(Long id, @Valid ProjectDTO projectDTO) {
//	// TODO Auto-generated method stub
//	return null;
//}


}