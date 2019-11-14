package com.fredrik.bookit.config;

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
    
    
    
    
    
}