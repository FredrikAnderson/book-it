package com.fredrik.bookit.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
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

    @EqualsAndHashCode.Exclude
    @ManyToMany
    @JoinTable(
    		name = "project_booked_items", 
    		joinColumns = @JoinColumn(name = "project_id"), 
    		inverseJoinColumns = @JoinColumn(name = "item_id"))
    private Set<Item> bookedItems = new HashSet<Item>();

    
    public Project(String name) {
    	this.name = name;
    }    

    public Project(Long id, String name, LocalDate start, LocalDate end) {
    	this.id   		= id;
    	this.name 		= name;
    	this.startDate 	= start;
    	this.endDate	= end;
    }    

    public void bookItem(Item item) {
    	bookedItems.add(item);
    }

    public void cancelItem(Item item) {
    	bookedItems.remove(item);
    }

}