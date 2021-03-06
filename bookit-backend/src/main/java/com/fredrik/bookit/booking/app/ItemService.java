package com.fredrik.bookit.booking.app;

import java.util.List;

import com.fredrik.bookit.web.rest.model.ItemDTO;

public interface ItemService {

	List<ItemDTO> findAll();

	List<ItemDTO> findBy(String itemName);

    ItemDTO findOne(Long id);

    ItemDTO findByName(String itemName);

    ItemDTO findByPublicId(String publicId);

	ItemDTO save(ItemDTO body);

	void delete(Long id);

	int nrOfItemProperties();
	
}