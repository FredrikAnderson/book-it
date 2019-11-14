package model;

import java.time.LocalDateTime;

public class Booking {

	private Resource resource;
	private Project project;	
	private LocalDateTime startTime;
	private LocalDateTime endTime;


	public Resource getResource() {
		return resource;
	}
	
	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public Project getProject() {
		return project;
	}
	
	public void setProject(Project project) {
		this.project = project;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}
	
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	
	public LocalDateTime getEndTime() {
		return endTime;
	}
	
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "Booking [resource=" + resource + ", project=" + project + ", startTime=" + startTime + ", endTime="
				+ endTime + "]";
	}
		
}
