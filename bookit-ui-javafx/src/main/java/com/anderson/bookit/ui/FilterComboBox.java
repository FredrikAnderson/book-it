package com.anderson.bookit.ui;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.joda.time.Instant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class FilterComboBox<T> extends ComboBox<T> {

	private Instant lastTypedTime = null;
	
	private Lookup<T> lookup = null;

	public FilterComboBox() {
		setEditable(true);
		
		this.setOnKeyReleased(new KeyHandler<T>());
	}

	public void setLookup(Lookup<T> aLookup) {
		this.lookup = aLookup;
	}
	
	private Collection<T> lookupForPrefix(String toLookup) {

		Collection<T> collection = lookup.lookup(toLookup);

		return collection;
	}
	
	void setList(Collection<?> collection) {
		
		ObservableList<T> observableList = (ObservableList<T>) FXCollections.observableArrayList(collection);
		
		setItems(observableList);
	}
	
	private String getText() {
		String text = getEditor().getText().toLowerCase();

		return text;
	}

	public void setItems(List<T> items) {

		ObservableList<T> obsList = FXCollections.observableArrayList(items);
		this.setItems(obsList);
	}

	public void keyTyped() {
		lastTypedTime = Instant.now();
		
		System.out.println("In UI: " + getText());
		Task<Collection<T>> lookupTask = new Task<Collection<T>>() {
			
			@Override
			protected Collection<T> call() throws Exception {

				System.out.println("Typed: " + getText());
				// Maybe check if string is longer than 2 or 3!?
						
				// Wait until no key presses for within 700 ms
				// CHeck if something new is typed
							
				try {
					Thread.sleep(800);
				} catch (InterruptedException e1) {
				}					
				Instant now = Instant.now().minus(790);
				if (lastTypedTime.isAfter(now)) {
					// Then user has typed something					
					return null;
				}
				
				// Lookup string from server
				System.out.println("Looking up: " + getText());

				return (Collection<T>) lookupForPrefix(getText());
			}
			
			@Override
			protected void succeeded() {
				Collection<T> collection = null;
				try {
					collection = get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				// If not the set model...
				if (collection != null) {
					setList(collection);
					
					show();					
				}
			}
		};

		new Thread(lookupTask).start();
	}
	
	class KeyHandler<T> implements EventHandler<KeyEvent> {

		@Override
		public void handle(KeyEvent event) {
			System.out.println("Typed: " + event.toString());
			
			if (event.getCode() != KeyCode.CONTROL && event.getCode() != KeyCode.SHIFT 
//					&& event.getCode() != KeyCode.BACK_SPACE
					&& event.getCode() != KeyCode.ENTER
					&& event.getCode() != KeyCode.LEFT && event.getCode() != KeyCode.RIGHT
					&& event.getCode() != KeyCode.UP && event.getCode() != KeyCode.DOWN) {
				
				System.out.println("Typed, reacting, code: " + event.getCode());				
				keyTyped();
			}
			if (event.getCode() == KeyCode.ENTER) {
				
			}			
		}
	}

}
