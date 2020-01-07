package com.fredrik.bookit.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fredrik.bookit.model.Item;
import com.fredrik.bookit.model.ItemProperties;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
	
//    Item findByName(String itemName);

	@Query("select ip from ItemProperties ip where ip.name = :name")
	ItemProperties findByName(String name);

	@Query("select count(ip) from ItemProperties ip")
	int nrOfItemProperties();
	
}