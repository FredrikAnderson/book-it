package com.fredrik.bookit;

import java.time.LocalDate;
import java.util.Collections;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fredrik.bookit.booking.app.ItemService;
import com.fredrik.bookit.infra.ProjectRepository;
import com.fredrik.bookit.infra.ResourceRepository;
import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.model.ItemProperties;
import com.fredrik.bookit.model.Project;
import com.fredrik.bookit.model.Resource;
import com.fredrik.bookit.model.mapper.ItemMapper;
import com.fredrik.bookit.web.rest.model.ItemDTO;

@SpringBootApplication
public class BookITBackend implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(BookITBackend.class);

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(Application.class);
//	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(BookITBackend.class);
		app.setDefaultProperties(Collections.singletonMap("server.port", 8888));
		
		app.run(args);			 
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Command runner");

		loadInitData();
	}

	@Inject
	ResourceRepository repository;

	@Inject
	ProjectRepository projRepo;

	@Inject
	ItemService itemService;

	ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

	@Transactional
	private void loadInitData() {

		// save a couple of customers
		repository.save(new Resource("Jack"));
		repository.save(new Resource("Chloe"));
		repository.save(new Resource("Kim"));

		// fetch all customers
		log.info("Resources found with findAll():");
		log.info("-------------------------------");
		for (Resource res : repository.findAll()) {
			log.info(res.toString());
		}
		log.info("");

		// save a couple of customers
		projRepo.save(new Project(0L, "Proj #1", LocalDate.now().minusDays(1), LocalDate.now().plusDays(5)));
		projRepo.save(new Project(0L, "Project testing", LocalDate.now().minusDays(2), LocalDate.now().plusMonths(2)));
		projRepo.save(new Project(0L, "Project three", LocalDate.now().minusMonths(2), LocalDate.now().plusDays(10)));


		// save a couple of items
		ItemProperties props = ItemProperties.builder()
			.name("Hammare")
			.description("Hammare, vanlig")
//			.id()
			.height(1.0)
			.width(2.0)
			.length(3.0)
			.weight(11.12)
			.price(39.95)
			.build();
		
		Item entity = new Item(0L, "EAN 1234", props, "Hylla 2A");
		ItemDTO itemDTO = itemMapper.mapEntityToDTO(entity);
		ItemDTO savedDto = itemService.save(itemDTO);
		
		entity = new Item(0L, "EAN 4321", props, "Hylla 2B");
		itemDTO = itemMapper.mapEntityToDTO(entity);
		savedDto = itemService.save(itemDTO);

		entity = new Item(0L, "EAN 5555", props, "Hylla 2C");
		itemDTO = itemMapper.mapEntityToDTO(entity);
		savedDto = itemService.save(itemDTO);
		
		log.info("All init data, done.");
		
		// fetch an individual customer by ID
//		Resource one = repository.getOne(1L);
//		log.info(one.toString());

//				.ifPresent(resource -> {
//					log.info("Resource found with findById(1L):");
//					log.info("--------------------------------");
//					log.info(resource.toString());
//					log.info("");
//				});

		// fetch customers by last name
//			log.info("Customer found with findByName('Chloe'):");
//			log.info("--------------------------------------------");
//			repository.findByName("Chloe").forEach(bauer -> {
//				log.info(bauer.toString());
//			});
//			// for (Customer bauer : repository.findByLastName("Bauer")) {
//			// 	log.info(bauer.toString());
//			// }
//			log.info("");
	}

}
