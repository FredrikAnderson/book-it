
package com.anderson.bookit.ui.service;

public interface ItemModificationListener<I, T> {

	public enum ItemEvent {
		ADD,
		EDIT,
		DELETE
	};
	
	public void itemModified(I id, ItemEvent event, T item);
	
}
