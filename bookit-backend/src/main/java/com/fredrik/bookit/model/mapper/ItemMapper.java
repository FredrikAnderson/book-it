package com.fredrik.bookit.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.web.rest.model.ItemDTO;

@Mapper
public interface ItemMapper {

	@Mapping(source = "properties.name", target = "name")
	@Mapping(source = "properties.description", target = "description")
	@Mapping(source = "properties.width", target = "width")
	@Mapping(source = "properties.height", target = "height")
	@Mapping(source = "properties.length", target = "length")
	@Mapping(source = "properties.weight", target = "weight")
	@Mapping(source = "properties.price", target = "price")
	ItemDTO mapEntityToDTO(Item entity);

	@Mapping(source = "name", target = "properties.name")
	@Mapping(source = "description", target = "properties.description")
	@Mapping(source = "width", target = "properties.width")
	@Mapping(source = "height", target = "properties.height")
	@Mapping(source = "length", target = "properties.length")
	@Mapping(source = "weight", target = "properties.weight")
	@Mapping(source = "price", target = "properties.price")
	Item mapDTOToEntity(ItemDTO dto);

}