package com.fredrik.bookit.booking.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.mapstruct.factory.Mappers;

import com.fredrik.bookit.infra.ItemRepository;
import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.model.Project;
import com.fredrik.bookit.model.mapper.ItemMapper;
import com.fredrik.bookit.web.rest.model.ItemDTO;
import com.fredrik.bookit.web.rest.model.ProjectDTO;

@Named
public class ItemServiceBean implements ItemService {

	@Inject 
	ItemRepository itemRepo;

	ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

	@Override
	public ItemDTO findOne(Long id) {
		Optional<Item> byId = itemRepo.findById(id);
		
		ItemDTO dto = itemMapper.mapEntityToDTO(byId.get());
		return dto;
	}

	@Override
	public ItemDTO findByName(String itemName) {
		Item byName = null; // itemRepo.findByName(itemName);		
		
		ItemDTO dto = itemMapper.mapEntityToDTO(byName);
		return dto;
	}

	@Override
	public ItemDTO save(ItemDTO dto) {
		// DTO to entity
		Item entity = itemMapper.mapDTOToEntity(dto);
		
		entity = itemRepo.save(entity);
		
		ItemDTO toret = itemMapper.mapEntityToDTO(entity);
		return toret;
	}

	@Override
	public List<ItemDTO> findAll() {
		
		List<Item> findAll = itemRepo.findAll();
		List<ItemDTO> dtos = mapEntitiesToDtos(findAll);
		
		return dtos;
	}

	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	private List<ItemDTO> mapEntitiesToDtos(List<Item> items) {
		List<ItemDTO> itemDtos = new ArrayList<>();
		ItemMapper mapper = Mappers.getMapper(ItemMapper.class);
		
		for (Item item : items) {
			ItemDTO itemDto = mapper.mapEntityToDTO(item);
			
			itemDtos.add(itemDto);
		}
		
		return itemDtos;
	}

}