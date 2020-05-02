package com.fredrik.bookit.booking.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;

import com.fredrik.bookit.infra.ItemRepository;
import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.model.ItemProperties;
import com.fredrik.bookit.model.mapper.ItemMapper;
import com.fredrik.bookit.web.rest.model.ItemDTO;

import lombok.extern.slf4j.Slf4j;

@Named
@Slf4j
public class ItemServiceBean implements ItemService {

	@Inject 
	ItemRepository itemRepo;

//	ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
//	Mapper itemMapper;

	@Autowired
	ItemMapper itemMapper;	
	
	@Override
	public ItemDTO findOne(Long id) {
		Optional<Item> byId = itemRepo.findById(id);
		
		ItemDTO dto = itemMapper.mapEntityToDTO(byId.get());
		return dto;
	}

	@Override
	public ItemDTO findByName(String itemName) {
//		itemRepo.find
		Item byName = null; // itemRepo.findByName(itemName);		
		
		ItemDTO dto = itemMapper.mapEntityToDTO(byName);
		return dto;
	}

	@Override
	public ItemDTO findByPublicId(String publicId) {
		Item byPublicId = itemRepo.findByPublicId(publicId);		
		
		ItemDTO dto = itemMapper.mapEntityToDTO(byPublicId);
		return dto;
	}

	@Override
	public ItemDTO save(ItemDTO dto) {
		// DTO to entity
		Item entity = itemMapper.mapDTOToEntity(dto);

		// If itemProperties not containing id then lookup based on name
		if (Objects.isNull(entity.getProperties().getId())) {
			log.info("ItemPropertis should be looked up. By name: " + entity.getProperties().getName());
			
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
		itemRepo.deleteById(id);
	}

	@Override
	public int nrOfItemProperties() {
		return itemRepo.nrOfItemProperties();		
	}
	
	
	private List<ItemDTO> mapEntitiesToDtos(List<Item> items) {
		List<ItemDTO> itemDtos = new ArrayList<>();
		
		for (Item item : items) {
			ItemDTO itemDto = itemMapper.mapEntityToDTO(item);
			
			itemDtos.add(itemDto);
		}
		
		return itemDtos;
	}

}