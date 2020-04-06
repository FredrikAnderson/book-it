
package com.anderson.bookit.ui.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import com.anderson.bookit.model.Project;
import com.anderson.bookit.ui.service.ItemModificationListener.ItemEvent;
import com.fredrik.bookit.ui.rest.api.ProjectsApi;
import com.fredrik.bookit.ui.rest.invoker.ApiException;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;
import com.fredrik.bookit.ui.rest.model.ProjectDTOList;

public class ProjectService {

	ProjectsApi projsApi = new ProjectsApi();

	ProjectCache projCache = new ProjectCache();
	
	private static ProjectService myInstance = null;
	
	private ProjectService() {
	}

	public static ProjectService getInstance() {
		if (myInstance == null) {
			myInstance = new ProjectService();
		}
		return myInstance;
	}
	
	
	public List<ProjectDTO> getProjects() {
		System.out.println("Get Projects from backend.");

//		Project p1 = new Project("P1");
//		p1.setStartDate(LocalDate.of(2019, Month.OCTOBER, 29));
//		p1.setEndDate(LocalDate.of(2019, Month.DECEMBER, 29));
//		
//		Project p2 = new Project("P2");
//		p2.setStartDate(LocalDate.of(2019, Month.NOVEMBER, 1));
//		p2.setEndDate(LocalDate.of(2019, Month.DECEMBER, 14));

		
//		return Arrays.asList(p1, p2);		
		
		ProjectDTOList projectDTOList = null;
		try {
			projectDTOList = projsApi.getProjects();
		
		} catch (ApiException e) {
			e.printStackTrace();
		}
		
		System.out.println("Projects: " + projectDTOList.toString());
		List<ProjectDTO> items = projectDTOList.getItems();
//		
//		items.stream().forEach( proj -> { 
//			proj.startDate(LocalDate.now().minusDays(1)); 
//			proj.endDate(LocalDate.now().plusDays(3)); } );
		
		return items;
	}

	public ProjectDTO saveProject(ProjectDTO toEdit) {
		System.out.println("Save Project to backend: " + toEdit);		
		
		try {
			if (toEdit.getId() == null) {
				toEdit = projsApi.addProject(toEdit);				
				
				projCache.itemModified(toEdit.getId(), ItemEvent.ADD, toEdit);
			} else {
				toEdit = projsApi.updateProject(toEdit.getId(), toEdit);				
				projCache.itemModified(toEdit.getId(), ItemEvent.EDIT, toEdit);
			}
			
		} catch (ApiException e) {
			e.printStackTrace();
		}		
		return toEdit;
	}

	public void deleteProject(ProjectDTO toEdit) {
		System.out.println("Delete proj: " + toEdit);
		
		if (toEdit.getId() != null) {
			try {
				projsApi.deleteProject(toEdit.getId());
				projCache.itemModified(toEdit.getId(), ItemEvent.DELETE, toEdit);
			} catch (ApiException e) {
				e.printStackTrace();
			}		
		}
		
	}

	public ProjectDTO bookItemForProject(ProjectDTO proj, Long item) {
		System.out.println("Book Item " + item + " for Project " + proj + " to backend.");		
		
		try {
			proj = projsApi.bookItemToProject(proj.getId(), item);				
			
			projCache.itemModified(proj.getId(), ItemEvent.EDIT, proj);
			
		} catch (ApiException e) {
			e.printStackTrace();
		}		
		return proj;
	}

	
	public void addItemListener(ItemModificationListener<Long, ProjectDTO> itemListener) {
		projCache.addItemListener(itemListener);
	}
	
}
