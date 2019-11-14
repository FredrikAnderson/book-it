package com.fredrik.bookit.model.mapper;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BookingMapper {

    private Long id;
    private ResourceMapper resource;
    private LocalDateTime from;
    private LocalDateTime to;    

}