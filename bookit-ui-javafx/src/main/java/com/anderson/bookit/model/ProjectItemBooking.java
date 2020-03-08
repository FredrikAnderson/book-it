package com.anderson.bookit.model;

import com.fredrik.bookit.ui.rest.model.ItemDTO;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

public class ProjectItemBooking {

	private ProjectDTO project;	
	private ItemDTO item;


	public ItemDTO getItem() {
		return item;
	}
	
	public void setItem(ItemDTO item) {
		this.item = item;
	}
	
	public ProjectDTO getProject() {
		return project;
	}
	
	public void setProject(ProjectDTO project) {
		this.project = project;
	}

	@Override
	public String toString() {
		return "ProjectItemBooking [item=" + item + ", project=" + project + "]";
	}
		
}
