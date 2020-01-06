package com.fredrik.bookit.model.mapper;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mapstruct.factory.Mappers;

import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.model.ItemProperties;
import com.fredrik.bookit.web.rest.model.ItemDTO;

public class ItemMapperTest {

	ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
	
	@Test
	public void fullItemToItemProperties_correctly() {
		// Given
		Item item = new Item();
		item.setPublicId("EAN12345678");
		item.setInventory("Hylla 2A");
		
		ItemProperties properties = new ItemProperties();
		properties.setName("Hammare");
		properties.setDescription("Item test");
		properties.setHeight(12.34);
		properties.setWidth(43.34);
		properties.setLength(123.0);
		properties.setWeight(1.345);
		properties.setPrice(99.95);
		item.setProperties(properties);

		// When
		ItemDTO itemDTO = itemMapper.mapEntityToDTO(item);
		
		
		// Then
		assertEquals("Hammare", itemDTO.getName());
		assertEquals("Item test", itemDTO.getDescription());
		assertEquals("EAN12345678", itemDTO.getPublicId());		
		assertEquals("Hylla 2A", itemDTO.getInventory());		
		
		assertEquals(new Float(12.34), itemDTO.getHeight());
		assertEquals(new Float(123.0), itemDTO.getLength());
		assertEquals(new Float(99.95), itemDTO.getPrice());		
		assertEquals(new Float(43.34), itemDTO.getWidth());		
		assertEquals(new Float(1.345), itemDTO.getWeight());		
		
	}

}