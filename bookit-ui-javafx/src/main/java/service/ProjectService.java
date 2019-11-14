
package service;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import model.Project;

public class ProjectService {

	public List<Project> getProjects() {
		System.out.println("Get Resources from backend.");

		Project p1 = new Project("P1");
		p1.setStartDate(LocalDate.of(2019, Month.OCTOBER, 29));
		p1.setEndDate(LocalDate.of(2019, Month.DECEMBER, 29));
		
		Project p2 = new Project("P2");
		p2.setStartDate(LocalDate.of(2019, Month.NOVEMBER, 1));
		p2.setEndDate(LocalDate.of(2019, Month.DECEMBER, 14));

		return Arrays.asList(p1, p2);
	}

	
}
