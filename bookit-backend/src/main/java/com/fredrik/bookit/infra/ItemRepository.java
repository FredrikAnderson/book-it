package com.fredrik.bookit.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fredrik.bookit.model.Item;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	
//    Item findByName(String itemName);

}