package com.fredrik.bookit.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private String name;
    
    private LocalDate startDate;
    
    private LocalDate endDate;

    @EqualsAndHashCode.Exclude
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE } )
    @JoinTable(
    		name = "project_booked_items", 
    		joinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"), 
    		inverseJoinColumns = @JoinColumn(name = "item_id", referencedColumnName = "id"))
    @Builder.Default
    private Set<Item> bookedItems = new HashSet<>();
    
    public Project(String name) {
    	this.name = name;
    }    

    public Project(String name, LocalDate start, LocalDate end) {
    	this.name 		= name;
    	this.startDate 	= start;
    	this.endDate	= end;
    }    

    public void bookItem(Item item) {
    	bookedItems.add(item);
    	item.getProjects().add(this);
    }

    public void cancelItem(Item item) {
    	bookedItems.remove(item);
    	item.getProjects().remove(this);
    }

}