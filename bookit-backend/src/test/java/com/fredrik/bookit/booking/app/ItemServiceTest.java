package com.fredrik.bookit.booking.app;

import static org.junit.Assert.assertEquals;

import java.util.List;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.fredrik.bookit.hello.IntegrationTest;
import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.model.ItemProperties;
import com.fredrik.bookit.model.mapper.ItemMapper;
import com.fredrik.bookit.web.rest.model.ItemDTO;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@IntegrationTest
@Slf4j
public class ItemServiceTest {

//	ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);
//	Mapper itemMapper;
	
	@Autowired
	ItemMapper itemMapper;
	
	@Inject 
	private ItemService itemService;
	
	@Test 
	public void saveItems_correctly() {
		// Given
		int nrOfItemProperties = itemService.nrOfItemProperties();
		// save a couple of items
		ItemProperties props = ItemProperties.builder()
			.name("Affisch")
			.description("Stor affisch")
//			.id()
			.height(1.0)
			.width(2.0)
			.length(3.0)
			.build();

		Item entity = new Item(0L, "EAN 1234", props, "Hylla 2A");
		ItemDTO itemDTO = itemMapper.mapEntityToDTO(entity);
		
		// When
		ItemDTO savedDto = itemService.save(itemDTO);

		entity = new Item(0L, "EAN 4321", props, "Hylla 2B");
		itemDTO = itemMapper.mapEntityToDTO(entity);
		savedDto = itemService.save(itemDTO);

		entity = new Item(0L, "EAN 5555", props, "Hylla 2C");
		itemDTO = itemMapper.mapEntityToDTO(entity);
		savedDto = itemService.save(itemDTO);

		// Then
		log.info("All init data, done.");
		int nrOfItemPropertiesAfter = itemService.nrOfItemProperties();

		assertEquals(1, nrOfItemPropertiesAfter - nrOfItemProperties); 		
		
	}

	@Test 
	public void givenAFilter_findItems_correct() {
		// Given
		
//		// save a couple of items
//		ItemProperties props = ItemProperties.builder()
//			.name("Stor hammare")
//			.description("Stor hammare")
////			.id()
//			.height(1.0)
//			.width(2.0)
//			.length(3.0)
//			.build();
//
//		Item entity = new Item(0L, "EAN 1234", props, "Hylla 2A");
//		ItemDTO itemDTO = itemMapper.mapEntityToDTO(entity);
		
		// When
		List<ItemDTO> list = itemService.findBy("ham");

		assertEquals(1, list.size()); 		

		list = itemService.findBy("gam");

		assertEquals(0, list.size()); 		
		
	}

	
//	@Test
//	public void fullItemToItemProperties_correctly() {
//		// Given
//		Item item = new Item();
//		item.setPublicId("EAN12345678");
//		item.setInventory("Hylla 2A");
//		
//		ItemProperties properties = new ItemProperties();
//		properties.setName("Hammare");
//		properties.setDescription("Item test");
//		properties.setHeight(12.34);
//		properties.setWidth(43.34);
//		properties.setLength(123.0);
//		properties.setWeight(1.345);
//		properties.setPrice(99.95);
//		item.setProperties(properties);
//
//		// When
//		ItemDTO itemDTO = itemMapper.mapEntityToDTO(item);
//		
//		
//		// Then
//		assertEquals("Hammare", itemDTO.getName());
//		assertEquals("Item test", itemDTO.getDescription());
//		assertEquals("EAN12345678", itemDTO.getPublicId());		
//		assertEquals("Hylla 2A", itemDTO.getInventory());		
//		
//		assertEquals(new Float(12.34), itemDTO.getHeight());
//		assertEquals(new Float(123.0), itemDTO.getLength());
//		assertEquals(new Float(99.95), itemDTO.getPrice());		
//		assertEquals(new Float(43.34), itemDTO.getWidth());		
//		assertEquals(new Float(1.345), itemDTO.getWeight());		
//		
//	}

}