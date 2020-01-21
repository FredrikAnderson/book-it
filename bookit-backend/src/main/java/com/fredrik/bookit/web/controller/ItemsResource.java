package com.fredrik.bookit.web.controller;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fredrik.bookit.booking.app.ItemService;
import com.fredrik.bookit.web.rest.api.ItemsApi;
import com.fredrik.bookit.web.rest.model.ItemDTO;
import com.fredrik.bookit.web.rest.model.ItemDTOList;
import com.fredrik.bookit.web.rest.model.ProjectDTO;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class ItemsResource implements ItemsApi {

	@Inject
	private ItemService itemService;

	@Override
	public ResponseEntity<ItemDTO> addItem(@Valid ItemDTO itemDTO) {
		log.info("addItem: " + itemDTO);
		ItemDTO toret = null;
		
		toret = itemService.save(itemDTO);		
		return ResponseEntity.ok(toret);
	}

	@Override
	public ResponseEntity<Void> deleteItem(Long id) {
		log.info("deleteItem: " + id);

		itemService.delete(id);
		return (ResponseEntity<Void>) ResponseEntity.ok();
	}

	@Override
	public ResponseEntity<ItemDTO> getItemById(Long id) {
		log.info("getItemById: " + id);
		
		ItemDTO itemDTO = itemService.findOne(id);
		
		return ResponseEntity.ok(itemDTO);
	}

	@Override
    public ResponseEntity<ItemDTOList> getItems(@Valid String name) {
//	public ResponseEntity<ItemDTOList> getItems() {
		log.info("getItems");
		
		List<ItemDTO> dtos = null;
		if (Objects.nonNull(name) && !name.isEmpty()) {
			dtos = itemService.findBy(name);
		} else {		
			dtos = itemService.findAll();
		}
		ItemDTOList dtoList = new ItemDTOList();
		dtoList.setItems(dtos);
		
		return ResponseEntity.ok(dtoList);
	}

	@Override
	public ResponseEntity<Void> updateItem(Long id, @Valid ItemDTO itemDTO) {
		log.info("updateItem: " + id);
//		ItemDTO toret = null;
		Void toret = null;
		
//		toret = projectService.save(itemDTO);		
		return ResponseEntity.ok(toret);
	}


	
}