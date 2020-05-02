package com.fredrik.bookit.model.mapper;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import com.fredrik.bookit.hello.IntegrationTest;
import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.model.Project;
import com.fredrik.bookit.web.rest.model.ProjectDTO;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@IntegrationTest
@Slf4j
public class ProjectMapperTest {

//	ProjectMapper projMapper = Mappers.getMapper(ProjectMapper.class);

	@Autowired
	ProjectMapper projMapper;

//	Mapper projMapper;
	
	@Test
	public void fullItemToItemProperties_correctly() {
		
		// Given
		Project proj = new Project();
		proj.setId(123L);
		proj.setName("Proj #1");
		proj.setStartDate(LocalDate.of(2020, 01, 02));
		proj.setEndDate(LocalDate.of(2020, 03, 04));

		Item item = new Item();
		item.setId(21L);

		proj.bookItem(item);
		
		// When
		ProjectDTO projDTO = projMapper.mapEntityToDTO(proj);
		
		// Then
		assertEquals(new Long(123), projDTO.getId());
		assertEquals("Proj #1", projDTO.getName());
		assertEquals(LocalDate.of(2020,  01,  02), projDTO.getStartDate());
		assertEquals(LocalDate.of(2020,  03,  04), projDTO.getEndDate());

		assertEquals(1, projDTO.getBookedItems().size());
		
	}

}