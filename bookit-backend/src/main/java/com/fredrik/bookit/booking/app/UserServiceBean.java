package com.fredrik.bookit.booking.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.mapstruct.factory.Mappers;

import com.fredrik.bookit.infra.UserRepository;
import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.model.User;
import com.fredrik.bookit.model.mapper.UserMapper;
import com.fredrik.bookit.web.rest.model.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Named
@Slf4j
public class UserServiceBean implements UserService {

	@Inject 
	UserRepository userRepo;

	UserMapper userMapper = Mappers.getMapper(UserMapper.class);

	@Override
	public UserDTO findOne(String id) {
		Optional<User> byId = userRepo.findById(id);
		
		UserDTO dto = userMapper.mapEntityToDTO(byId.get());
		return dto;
	}

	@Override
	public UserDTO findByName(String itemName) {
		Item byName = null; // itemRepo.findByName(itemName);		
		
		UserDTO dto = null; // itemMapper.mapEntityToDTO(byName);
		return dto;
	}

	@Override
	public UserDTO save(UserDTO dto) {
		// DTO to entity
		User entity = userMapper.mapDTOToEntity(dto);

//		// If itemProperties not containing id then lookup based on name
//		if (Objects.isNull(entity.getProperties().getId())) {
//			log.info("ItemPropertis should be looked up. By name: " + entity.getProperties().getName());
//			
//			ItemProperties itemProperties = itemRepo.findByName(entity.getProperties().getName());
//			if (Objects.nonNull(itemProperties)) {
//				entity.setProperties(itemProperties);
//			}			
//		}
		
		entity = userRepo.save(entity);		
		
		UserDTO toret = userMapper.mapEntityToDTO(entity);
		return toret;
	}

	@Override
	public List<UserDTO> findAll() {
		
		List<User> findAll = userRepo.findAll();
		List<UserDTO> dtos = mapEntitiesToDtos(findAll);
		
		return dtos;
	}

	@Override
	public List<UserDTO> findBy(String itemName) {
		
//		List<ItemProperties> findAll = itemRepo.findBy(itemName);
//		List<Item> items = findAll.stream().map(ip -> new Item(ip)).collect(Collectors.toList());		
		List<UserDTO> dtos = null; // mapEntitiesToDtos(items);
		
		return dtos;
	}
	
	@Override
	public void delete(String id) {
		userRepo.deleteById(id);
	}
	
	private List<UserDTO> mapEntitiesToDtos(List<User> users) {
		List<UserDTO> UserDTOs = new ArrayList<>();
		
		for (User user : users) {
			UserDTO UserDTO = userMapper.mapEntityToDTO(user);
			
			UserDTOs.add(UserDTO);
		}
		
		return UserDTOs;
	}

}