package com.fredrik.bookit.model;

import javax.persistence.Entity;

import lombok.Data;

@Data
public class Booking {

    private Project project;
    
    private Item item;

}