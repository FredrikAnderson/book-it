
package com.anderson.bookit.ui.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.fredrik.bookit.ui.rest.model.ProjectDTO;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

public class ProjectCache implements ItemModificationListener<Long, ProjectDTO> {

	Cache<Long, ProjectDTO> cache = null;

	List<ItemModificationListener<Long, ProjectDTO>> listeners = new ArrayList<>();

	public ProjectCache() {
		cache = Caffeine.newBuilder()
				.expireAfterWrite(1, TimeUnit.MINUTES)
				.maximumSize(100)
				.build();		
	}

	@Override
	public void itemModified(Long id, ItemEvent event, ProjectDTO item) {

		if (event == ItemEvent.DELETE) {
			cache.invalidate(id);
		} else {
			cache.put(id, item);
		}

		// Notify listeners
		listeners.stream().forEach(l -> l.itemModified(id, event, item));
	}

	public void addItemListener(ItemModificationListener<Long, ProjectDTO> itemListener) {
		listeners.add(itemListener);
	}

	public void removeItemListener(ItemModificationListener<Long, ProjectDTO> itemListener) {
		listeners.remove(itemListener);
	}

	
}
