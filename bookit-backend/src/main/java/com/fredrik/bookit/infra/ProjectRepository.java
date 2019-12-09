package com.fredrik.bookit.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fredrik.bookit.model.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	
    Project findByName(String projName);

}