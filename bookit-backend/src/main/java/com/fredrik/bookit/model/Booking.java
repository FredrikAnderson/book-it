package com.fredrik.bookit.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Booking {

    private Long id;
    private Resource resource;
    private Long resourceId;
    private Long projectId;
    private LocalDateTime from;
    private LocalDateTime to;    

}