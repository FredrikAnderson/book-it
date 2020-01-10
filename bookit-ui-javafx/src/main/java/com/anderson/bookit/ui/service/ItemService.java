
package com.anderson.bookit.ui.service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import com.anderson.bookit.model.Project;
import com.fredrik.bookit.ui.rest.api.ItemsApi;
import com.fredrik.bookit.ui.rest.invoker.ApiException;
import com.fredrik.bookit.ui.rest.model.ItemDTO;
import com.fredrik.bookit.ui.rest.model.ItemDTOList;
import com.fredrik.bookit.ui.rest.model.ItemDTO;

public class ItemService {

	ItemsApi itemsApi = new ItemsApi();

	public ItemService() {
		
	}
	
	public List<ItemDTO> getItems() {
		System.out.println("Get Projects from backend.");

		Project p1 = new Project("P1");
		p1.setStartDate(LocalDate.of(2019, Month.OCTOBER, 29));
		p1.setEndDate(LocalDate.of(2019, Month.DECEMBER, 29));
		
		Project p2 = new Project("P2");
		p2.setStartDate(LocalDate.of(2019, Month.NOVEMBER, 1));
		p2.setEndDate(LocalDate.of(2019, Month.DECEMBER, 14));

		
//		return Arrays.asList(p1, p2);		
		
		ItemDTOList itemDTOList = null;
		try {
			itemDTOList = itemsApi.getItems();
		
		} catch (ApiException e) {
			e.printStackTrace();
		}
		
		System.out.println("Items: " + itemDTOList.toString());
		List<ItemDTO> items = itemDTOList.getItems();
//		
//		items.stream().forEach( proj -> { 
//			proj.startDate(LocalDate.now().minusDays(1)); 
//			proj.endDate(LocalDate.now().plusDays(3)); } );
		
		return items;
	}

	public ItemDTO saveItem(ItemDTO toEdit) {
		System.out.println("Save Project to backend: " + toEdit);		
		
		try {
			if (toEdit.getId() == null) {
				toEdit = itemsApi.addItem(toEdit);				
			} else {
				itemsApi.updateItem(toEdit.getId(), toEdit);				
			}
			
		} catch (ApiException e) {
			e.printStackTrace();
		}		
		return toEdit;
	}

	public void deleteItem(ItemDTO toEdit) {
		System.out.println("Delete proj: " + toEdit);
		
		if (toEdit.getId() != null) {
			try {
				itemsApi.deleteItem(toEdit.getId());
			} catch (ApiException e) {
				e.printStackTrace();
			}		
		}
		
	}
	
}
