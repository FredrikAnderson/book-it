package com.fredrik.bookit.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fredrik.bookit.model.Resource;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
	
    Resource findByName(String productName);
}