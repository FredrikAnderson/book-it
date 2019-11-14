package model;

import java.time.LocalDate;

public class Project {

	private String name;

	private LocalDate startDate;
	private LocalDate endDate;
		
	public Project() {
	}

	public Project(String str) {
		this.name = str;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}	
	
	public String toString() {
		return name;
	}
	
}
