package com.fredrik.bookit.booking.app;

import java.util.List;

import com.fredrik.bookit.web.rest.model.ItemDTO;

public interface ItemService {

	List<ItemDTO> findAll();
	
    ItemDTO findOne(Long id);

    ItemDTO findByName(String itemName);

	ItemDTO save(ItemDTO body);

	void delete(Long id);

	
}