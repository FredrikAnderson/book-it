package com.fredrik.bookit.booking.app;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import com.fredrik.bookit.infra.ItemRepository;
import com.fredrik.bookit.infra.ProjectRepository;
import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.model.Project;
import com.fredrik.bookit.model.mapper.ProjectMapper;
import com.fredrik.bookit.web.rest.model.ProjectDTO;

@Named
public class ProjectServiceBean implements ProjectService {

//	@Inject 
//	ItemService itemService;

	@Inject 
	ItemRepository itemRepo;

	@Inject 
	ProjectRepository projRepo;

	
//	ProjectMapper projMapper = Mappers.getMapper(ProjectMapper.class);
//	Mapper projMapper;

	@Inject 
	ProjectMapper projMapper;
	
	@Override
	public List<ProjectDTO> getProjects() {
		
		List<Project> all = projRepo.findAll();

		List<ProjectDTO> dtos = projMapper.mapEntitiesToDTOs(all);
		return dtos;
	}

	@Override
	public ProjectDTO findOne(Long id) {
		Optional<Project> byId = projRepo.findById(id);
		
		ProjectDTO dto = projMapper.mapEntityToDTO(byId.get());
		return dto;
	}

	@Override
	public ProjectDTO findByName(String productName) {
		Project byName = projRepo.findByName(productName);		
		
		ProjectDTO dto = projMapper.mapEntityToDTO(byName);
		return dto;
	}

	@Override
	public ProjectDTO saveProject(ProjectDTO proj) {
		// DTO to entity
		Project entity = projMapper.mapDTOToEntity(proj);		
		entity = projRepo.save(entity);
		
		ProjectDTO dto = projMapper.mapEntityToDTO(entity);
		return dto;		
	}

	@Override
	public void deleteProject(Long id) {

		projRepo.deleteById(id);
	}

	@Override
	public ProjectDTO bookItemToProject(Long projId, Long itemId) {
//		proj.addBookedItemsItem(item);
//		item.addProjectsItem(proj);
		
		Project project = projRepo.getOne(projId);
		Item item = itemRepo.getOne(itemId);
		
		assureItemNotBookedDuringTimePeriod(item, project);
		
		project.bookItem(item);

		Project savedProj = projRepo.save(project);

		ProjectDTO dto = projMapper.mapEntityToDTO(savedProj);
		return dto;				
	}

	private void assureItemNotBookedDuringTimePeriod(Item item, Project reqProj) {

		for (Project proj : item.getProjects()) {
			
			if (isOverlapping(proj.getStartDate(), proj.getEndDate(), reqProj.getStartDate(), reqProj.getEndDate())) {
				// 
				System.out.println("Exception!!! not allowed....");
				throw new RuntimeException("Item already booked for time period");
			}
		}		
	}

	public static boolean isOverlapping(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
	    return !start1.isAfter(end2) && !start2.isAfter(end1);
	}
	
	@Override
	public ProjectDTO cancelItemToProject(Long projId, Long itemId) {
		Project project = projRepo.getOne(projId);

		Item item = itemRepo.getOne(itemId);

		project.cancelItem(item);
//		item.getProject().add(project);

		Project savedProj = projRepo.save(project);

		ProjectDTO dto = projMapper.mapEntityToDTO(savedProj);
		return dto;				
	}
    
}