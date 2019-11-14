package com.fredrik.bookit.config;

import com.fredrik.bookit.model.Resource;
import com.fredrik.bookit.web.rest.model.ResourceDTO;

public interface ResourcesService {
	
    Resource findByName(String productName);

	void addResource(ResourceDTO body);
}