package com.fredrik.bookit.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
    private String name;
    
    private LocalDate startDate;
    
    private LocalDate endDate;

    public Project(String name) {
    	this.name = name;
    }
    
}