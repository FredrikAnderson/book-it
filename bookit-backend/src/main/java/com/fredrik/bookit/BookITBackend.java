package com.fredrik.bookit;

import java.time.LocalDate;
import java.util.Collections;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fredrik.bookit.infra.ProjectRepository;
import com.fredrik.bookit.infra.ResourceRepository;
import com.fredrik.bookit.model.Project;
import com.fredrik.bookit.model.Resource;

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

	@Transactional
	private void loadInitData() {

		// save a couple of customers
		repository.save(new Resource("Jack"));
		repository.save(new Resource("Chloe"));
		repository.save(new Resource("Kim"));

		// fetch all customers
		log.info("Customers found with findAll():");
		log.info("-------------------------------");
		for (Resource res : repository.findAll()) {
			log.info(res.toString());
		}
		log.info("");

		// save a couple of customers
		projRepo.save(new Project(0L, "Proj #1", LocalDate.now().minusDays(1), LocalDate.now().plusDays(5)));
		projRepo.save(new Project(0L, "Project testing", LocalDate.now().minusDays(2), LocalDate.now().plusMonths(2)));
		projRepo.save(new Project(0L, "Project three", LocalDate.now().minusMonths(2), LocalDate.now().plusDays(10)));

		
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
