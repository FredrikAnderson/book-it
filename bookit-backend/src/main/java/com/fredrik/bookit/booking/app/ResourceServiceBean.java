package com.fredrik.bookit.booking.app;

import javax.inject.Named;

import com.fredrik.bookit.model.Resource;
import com.fredrik.bookit.web.rest.model.ResourceDTO;

@Named
public class ResourceServiceBean implements ResourcesService {
	
    public Resource findByName(String productName) {
    	Resource toret = null;
    	
    	
    	return toret;
    }

	@Override
	public void addResource(ResourceDTO body) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ResourceDTO findOne(Long id) {
		ResourceDTO resourceDTO = new ResourceDTO();
		resourceDTO.setId(1L);
		resourceDTO.setName("Resource...");
		
		return resourceDTO;
	}
    
    
    
    
    
}