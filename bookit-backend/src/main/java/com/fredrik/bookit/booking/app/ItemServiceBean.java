package com.fredrik.bookit.booking.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.mapstruct.factory.Mappers;

import com.fredrik.bookit.infra.ItemRepository;
import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.model.ItemProperties;
import com.fredrik.bookit.model.Project;
import com.fredrik.bookit.model.mapper.ItemMapper;
import com.fredrik.bookit.web.rest.model.ItemDTO;
import com.fredrik.bookit.web.rest.model.ProjectDTO;

import lombok.extern.slf4j.Slf4j;

@Named
@Slf4j
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

		// If itemProperties not containing id then lookup based on name
		if (Objects.isNull(entity.getProperties().getId())) {
			log.info("ItemPropertis should be looked up.");
			
			ItemProperties itemProperties = itemRepo.findByName(entity.getProperties().getName());
			if (Objects.nonNull(itemProperties)) {
				entity.setProperties(itemProperties);
			}			
		}
		
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
	public List<ItemDTO> findBy(String itemName) {
		
		List<ItemProperties> findAll = itemRepo.findBy(itemName);
		List<Item> items = findAll.stream().map(ip -> new Item(ip)).collect(Collectors.toList());		
		List<ItemDTO> dtos = mapEntitiesToDtos(items);
		
		return dtos;
	}
	
	@Override
	public void delete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int nrOfItemProperties() {
		return itemRepo.nrOfItemProperties();		
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