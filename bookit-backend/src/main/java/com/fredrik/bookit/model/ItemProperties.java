package com.fredrik.bookit.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class ItemProperties {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
    private String name;

    private String description;

    private Double width;

    private Double height;
    
    private Double length;

    private Double weight;

    private Double price;


    @OneToMany(mappedBy = "properties", cascade = CascadeType.ALL)
    private Set<Item> items = new HashSet<>();
    
    public ItemProperties(String name) {
    	this.name = name;
    }
    
}