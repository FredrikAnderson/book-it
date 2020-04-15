package com.fredrik.bookit;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fredrik.bookit.booking.app.ItemService;
import com.fredrik.bookit.booking.app.UserService;
import com.fredrik.bookit.infra.ProjectRepository;
import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.model.ItemProperties;
import com.fredrik.bookit.model.Project;
import com.fredrik.bookit.model.User;
import com.fredrik.bookit.model.mapper.ItemMapper;
import com.fredrik.bookit.model.mapper.UserMapper;
import com.fredrik.bookit.web.rest.model.ItemDTO;
import com.fredrik.bookit.web.rest.model.UserDTO;
import com.fredrik.bookit.web.servlet.AppInfoServlet;
import com.fredrik.bookit.web.servlet.UserStartServlet;

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

	// Register Servlet
	@Bean
	public ServletRegistrationBean userStartServletBean() {
		ServletRegistrationBean bean = new ServletRegistrationBean(new UserStartServlet(), "/userstart");
		return bean;
	}

	@Bean
	public ServletRegistrationBean appInfoServletBean() {
		ServletRegistrationBean bean = new ServletRegistrationBean(new AppInfoServlet(), "/support");
		return bean;
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/api/users").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST","PUT", "DELETE");
			}
		};
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("Command runner");

		loadInitData();
	}

//	@Inject
//	ResourceRepository repository;

	@Inject
	ProjectRepository projRepo;

	@Inject
	ItemService itemService;

	ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

	@Inject
	UserService userService;

	UserMapper userMapper = Mappers.getMapper(UserMapper.class);

	@Transactional
	private void loadInitData() {

		// save a couple of customers
//		repository.save(new Resource("Jack"));
//		repository.save(new Resource("Chloe"));
//		repository.save(new Resource("Kim"));
//
//		// fetch all customers
//		log.info("Resources found with findAll():");
//		log.info("-------------------------------");
//		for (Resource res : repository.findAll()) {
//			log.info(res.toString());
//		}
//		log.info("");

		// save a couple of customers
		projRepo.save(new Project(1L, "Proj #1", LocalDate.now().minusDays(1), LocalDate.now().plusDays(5)));
		projRepo.save(new Project(2L, "Project testing", LocalDate.now().minusDays(2), LocalDate.now().plusMonths(2)));
		projRepo.save(new Project(3L, "Project three", LocalDate.now().minusMonths(2), LocalDate.now().plusDays(10)));

		
		// save a couple of items
		ItemProperties props = ItemProperties.builder()
			.name("Hammare")
			.description("Hammare, vanlig")
			.id(1L)
			.height(1.0)
			.width(2.0)
			.length(3.0)
			.weight(11.12)
			.price(39.95)
			.build();
		
		Item entity = null;
		ItemDTO itemDTO = null;
		ItemDTO savedDto = null;
		
		if (Objects.isNull(itemService.findByPublicId("EAN 1234"))) {
		
			entity = new Item(1L, "EAN 1234", props, "Hylla 2A");
			itemDTO = itemMapper.mapEntityToDTO(entity);
			savedDto = itemService.save(itemDTO);			
		}
		if (Objects.isNull(itemService.findByPublicId("EAN 4321"))) {

			entity = new Item(2L, "EAN 4321", props, "Hylla 2B");
			itemDTO = itemMapper.mapEntityToDTO(entity);
			savedDto = itemService.save(itemDTO);
		}
		if (Objects.isNull(itemService.findByPublicId("EAN 5555"))) {

			entity = new Item(3L, "EAN 5555", props, "Hylla 2C");
			itemDTO = itemMapper.mapEntityToDTO(entity);
			savedDto = itemService.save(itemDTO);
		}
		
		ItemProperties storStol = ItemProperties.builder()
				.name("Stor stol")
				.description("Stol, stor")
				.id(2L)
				.height(1.0)
				.width(0.5)
				.length(0.8)
				.weight(2.22)
				.price(39.95)
				.build();

		if (Objects.isNull(itemService.findByPublicId("EAN 98765"))) {
			entity = new Item(4L, "EAN 98765", storStol, "Hylla 5B");
			itemDTO = itemMapper.mapEntityToDTO(entity);
			savedDto = itemService.save(itemDTO);
		}
		
		User myself = new User();
		myself.setUserid("me");
		myself.setName("Fredrik Anderson");
		myself.setEmail("fanderson75@gmail.com");
		myself.setRole("admin");
		
		UserDTO userDTO = userMapper.mapEntityToDTO(myself);
		userDTO = userService.save(userDTO);

		User booker = new User();
		booker.setUserid("booker");
		booker.setName("Mr Booker");
		booker.setEmail("booker@gmail.com");
		booker.setRole("booker");
		
		userDTO = userMapper.mapEntityToDTO(booker);
		userDTO = userService.save(userDTO);

		User user = new User();
		user.setUserid("User1");
		user.setName("User#1");
		user.setEmail("user1@gmail.com");
		user.setRole("booker");
		
		userDTO = userMapper.mapEntityToDTO(user);
		userDTO = userService.save(userDTO);

		user.setUserid("User2");
		user.setName("User#2");
		user.setEmail("user2@gmail.com");
		user.setRole("booker");
		
		userDTO = userMapper.mapEntityToDTO(user);
		userDTO = userService.save(userDTO);

		user.setUserid("User3");
		user.setName("User#3");
		user.setEmail("user3@gmail.com");
		user.setRole("booker");
		
		userDTO = userMapper.mapEntityToDTO(user);
		userDTO = userService.save(userDTO);

		log.info("All init data, done.");
	}

}
