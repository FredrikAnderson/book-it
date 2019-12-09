package com.fredrik.bookit.web.controller;

import java.util.Arrays;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fredrik.bookit.booking.app.ResourcesService;
import com.fredrik.bookit.web.rest.api.ResourcesApi;
import com.fredrik.bookit.web.rest.model.ResourceDTO;
import com.fredrik.bookit.web.rest.model.ResourceDTOList;

@RestController
@RequestMapping("/api")
public class ResourcesResource implements ResourcesApi {

	@Inject
	private ResourcesService resourcesService;


	@Override
	public ResponseEntity<ResourceDTOList> getResources() {
		
		ResourceDTO r1 = new ResourceDTO();
		r1.setId(1L);
		r1.setName("Resource 1");
		
		ResourceDTO r2 = new ResourceDTO();
		r2.setId(2L);
		r2.setName("Resource 2");
		
		ResourceDTOList rList = new ResourceDTOList();
		rList.setItems(Arrays.asList(r1, r2));
		
		return ResponseEntity.ok(rList);
	}

	@Override
	public ResponseEntity<ResourceDTO> addResource(@Valid ResourceDTO resourceDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<Void> deleteResource(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResponseEntity<ResourceDTO> getResourceById(Long id) {
		// TODO Auto-generated method stub
		ResourceDTO resourceDTO = resourcesService.findOne(id);
		return ResponseEntity.ok(resourceDTO);
	}

	@Override
	public ResponseEntity<Void> updateResource(Long id, @Valid ResourceDTO resourceDTO) {
		// TODO Auto-generated method stub
		return null;
	}


//	@Override
//	public Response addResource(@NotNull @Valid ResourceDTO resourceDTO, SecurityContext securityContext) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Response deleteResource(Long id, SecurityContext securityContext) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Response getResourceById(Long id, SecurityContext securityContext) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public Response updateResource(Long id, @NotNull @Valid ResourceDTO resourceDTO, SecurityContext securityContext) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//

	
}