package com.fredrik.bookit.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Item {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String publicId;

    @ManyToOne(cascade = CascadeType.ALL)
    private ItemProperties properties;

    private String inventory;
    
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "bookedItems")    
    private Set<Project> project = new HashSet<>();
    
    public Item(ItemProperties properties) {
    	this.properties = properties;
    }
    
    public Item(Long id, String publicId, ItemProperties itemProps, String invent) {
    	this.id   		= id;
    	this.publicId 	= publicId;
    	this.properties = itemProps;
    	this.inventory  = invent;
    }    
    
}