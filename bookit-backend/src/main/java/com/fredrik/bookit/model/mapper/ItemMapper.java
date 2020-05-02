package com.fredrik.bookit.model.mapper;


import java.util.List;
import java.util.Objects;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.Mapper;

import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.model.ItemProperties;
import com.fredrik.bookit.web.rest.model.ItemDTO;

@Mapper(componentModel = "spring")
public abstract class ItemMapper { // extends BaseMapper<ItemDTO, Item> {

	// Entity to DTO
	
	// This is the method used for the conversions
	@Named("originalFromDtoToEntity")
	public ItemDTO mapEntityToDTO(Item entity) {
		return mapEntityToDTO(entity, new CycleAvoidingMappingContext());
	}

	public abstract ItemDTO mapEntityToDTO(Item entity, @Context CycleAvoidingMappingContext context);

	@AfterMapping
    protected void itemPropertiesToItemDTOProperties(Item item, @MappingTarget ItemDTO dto, @Context CycleAvoidingMappingContext context) {
		ItemProperties itemProperties = item.getProperties();
		
		if (Objects.nonNull(itemProperties)) {
	        dto.setName( itemProperties.getName() );
	        dto.setDescription( itemProperties.getDescription() );
	        if ( itemProperties.getWidth() != null ) {
	            dto.setWidth( itemProperties.getWidth().floatValue() );
	        }
	        if ( itemProperties.getPrice() != null ) {
	            dto.setPrice( itemProperties.getPrice().floatValue() );
	        }
	        if ( itemProperties.getLength() != null ) {
	            dto.setLength( itemProperties.getLength().floatValue() );
	        }
	        if ( itemProperties.getWeight() != null ) {
	            dto.setWeight( itemProperties.getWeight().floatValue() );
	        }
	        if ( itemProperties.getHeight() != null ) {
	            dto.setHeight( itemProperties.getHeight().floatValue() );
	        }
		}
    }

	// DTO to entity
	
	// This is the method used for the conversions
	@Named("originalFromDtoToEntity")
	public Item mapDTOToEntity(ItemDTO dto) {
		return mapDTOToEntity(dto, new CycleAvoidingMappingContext());
	}

	public abstract Item mapDTOToEntity(ItemDTO dto, @Context CycleAvoidingMappingContext context);

	@AfterMapping
    protected void itemDTOToItemProperties(ItemDTO itemDTO, @MappingTarget Item item, @Context CycleAvoidingMappingContext context) {
		if (Objects.isNull(item.getProperties())) {
			item.setProperties(new ItemProperties());
		}
		ItemProperties itemProperties = item.getProperties();
		
        if ( itemDTO.getWidth() != null ) {
            itemProperties.setWidth( itemDTO.getWidth().doubleValue() );
        }
        if ( itemDTO.getPrice() != null ) {
            itemProperties.setPrice( itemDTO.getPrice().doubleValue() );
        }
        itemProperties.setName( itemDTO.getName() );
        if ( itemDTO.getLength() != null ) {
            itemProperties.setLength( itemDTO.getLength().doubleValue() );
        }
        if ( itemDTO.getWeight() != null ) {
            itemProperties.setWeight( itemDTO.getWeight().doubleValue() );
        }
        itemProperties.setDescription( itemDTO.getDescription() );
        if ( itemDTO.getHeight() != null ) {
            itemProperties.setHeight( itemDTO.getHeight().doubleValue() );
        }
    }

}