package com.anderson.bookit.model;

import java.time.LocalDate;

public class Project {

	private Long id;
	
	private String name;

	private LocalDate startDate;
	private LocalDate endDate;
		
	public Project() {
	}

	public Project(String str) {
		this.name = str;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public boolean equals(Object aObj) {
		if (this == aObj) {  
	          return true;  
	    }  
		if (aObj instanceof Project) {
			Project anotherProj = (Project) aObj;
			
			if (this.getId() != null && anotherProj.getId() != null && 
					this.getId().equals(anotherProj.getId()) && 
				this.getName() != null && anotherProj.getName() != null && 
					this.getName().equals(anotherProj.getName())) {
				return true;
			}
		}		
		
		return false;		
	}
	
	public String toString() {
		return name;
	}

	
}
