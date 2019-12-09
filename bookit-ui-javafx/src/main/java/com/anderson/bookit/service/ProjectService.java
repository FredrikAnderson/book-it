
package com.anderson.bookit.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import com.anderson.bookit.model.Project;
import com.fredrik.bookit.ui.rest.api.ProjectApi;
import com.fredrik.bookit.ui.rest.api.ProjectsApi;
import com.fredrik.bookit.ui.rest.invoker.ApiException;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;
import com.fredrik.bookit.ui.rest.model.ProjectDTOList;

public class ProjectService {

	ProjectApi projApi = new ProjectApi();
	ProjectsApi projsApi = new ProjectsApi();

	public ProjectService() {
		
	}
	
	public List<ProjectDTO> getProjects() {
		System.out.println("Get Projects from backend.");

		Project p1 = new Project("P1");
		p1.setStartDate(LocalDate.of(2019, Month.OCTOBER, 29));
		p1.setEndDate(LocalDate.of(2019, Month.DECEMBER, 29));
		
		Project p2 = new Project("P2");
		p2.setStartDate(LocalDate.of(2019, Month.NOVEMBER, 1));
		p2.setEndDate(LocalDate.of(2019, Month.DECEMBER, 14));

		
//		return Arrays.asList(p1, p2);		
		
		ProjectDTOList projectDTOList = null;
		try {
			projectDTOList = projApi.getProjects();
		
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
			} else {
				toEdit = projsApi.updateProject(toEdit.getId(), toEdit);				
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
			} catch (ApiException e) {
				e.printStackTrace();
			}		
		}
		
	}
	
}
