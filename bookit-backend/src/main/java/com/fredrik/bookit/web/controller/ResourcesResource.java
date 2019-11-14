package com.fredrik.bookit.web.controller;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.springframework.web.bind.annotation.RestController;

import com.fredrik.bookit.config.ResourcesService;
import com.fredrik.bookit.web.rest.api.ResourcesApi;
import com.fredrik.bookit.web.rest.model.ResourceDTO;

@RestController
public class ResourcesResource implements ResourcesApi {

	@Inject
	private ResourcesService resourcesService;

	@Override
	public Response addResource(ResourceDTO body, SecurityContext securityContext) {
		// TODO Auto-generated method stub
		return null;
		
//		resourcesService.addResource(body);
	}

	@Override
	public Response deleteResource(Long resourceId, String apiKey, SecurityContext securityContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getResourceById(Long resourceId, SecurityContext securityContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateResource(Long resourceId, ResourceDTO body, SecurityContext securityContext) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updateResourceWithForm(Long resourceId, String name, String status,
			SecurityContext securityContext) {
		// TODO Auto-generated method stub
		return null;
	}

	
}