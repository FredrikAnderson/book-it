package com.fredrik.bookit.booking.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import com.fredrik.bookit.infra.ProjectRepository;
import com.fredrik.bookit.model.Project;
import com.fredrik.bookit.web.rest.model.ProjectDTO;

@Named
public class ProjectServiceBean implements ProjectService {

	@Inject 
	ProjectRepository projRepo;
	
	@Override
	public List<ProjectDTO> getProjects() {
		
		List<Project> all = projRepo.findAll();

		List<ProjectDTO> dtos = mapEntitiesToDtos(all);		
		return dtos;
	}

	private List<ProjectDTO> mapEntitiesToDtos(List<Project> projs) {
		List<ProjectDTO> projDtos = new ArrayList<>();
		for (Project project : projs) {
			ProjectDTO projDto = mapEntityToDTO(project);
			
			projDtos.add(projDto);
		}
		
		return projDtos;
	}

	private ProjectDTO mapEntityToDTO(Project project) {
		ProjectDTO projectDTO = new ProjectDTO();
		projectDTO.setId(project.getId());
		projectDTO.setName(project.getName());
		projectDTO.setStartDate(project.getStartDate());
		projectDTO.setEndDate(project.getEndDate());
		
		return projectDTO;
	}

	@Override
	public ProjectDTO findOne(Long id) {
		Optional<Project> byId = projRepo.findById(id);
		
		ProjectDTO dto = mapEntityToDTO(byId.get());
		return dto;
	}

	@Override
	public ProjectDTO findByName(String productName) {
		Project byName = projRepo.findByName(productName);		
		
		ProjectDTO dto = mapEntityToDTO(byName);
		return dto;
	}

	@Override
	public ProjectDTO saveProject(ProjectDTO proj) {
		// DTO to entity
		Project entity = new Project(proj.getName());
		entity.setId(proj.getId());
		entity.setStartDate(proj.getStartDate());
		entity.setEndDate(proj.getEndDate());
		
		entity = projRepo.save(entity);
		
		ProjectDTO dto = mapEntityToDTO(entity);
		return dto;		
	}

	@Override
	public void deleteProject(Long id) {

		projRepo.deleteById(id);
	}
    
}