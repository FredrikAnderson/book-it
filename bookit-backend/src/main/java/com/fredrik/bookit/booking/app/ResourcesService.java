package com.fredrik.bookit.booking.app;

import com.fredrik.bookit.model.Resource;
import com.fredrik.bookit.web.rest.model.ResourceDTO;

public interface ResourcesService {

    ResourceDTO findOne(Long id);

    Resource findByName(String productName);

	void addResource(ResourceDTO body);
}