package com.anderson.bookit.ui;

import java.time.LocalDate;

import com.anderson.bookit.model.Project;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

public class ProjectView extends TableView<ProjectDTO> {

	ContextMenu cm;
	MenuItem mi1, mi2, mi3;

	public ProjectView() {

		setPadding(new Insets(50, 100, 50, 100));

//	EventType eventType;
//	tableResourceView.addEventHandler(eventType, eventHandler);

//	tableView = new BorderPane();

//	TextField filterField = new TextField();
//
//	// 1. Wrap the ObservableList in a FilteredList (initially display all data).
//    FilteredList<Resource> filteredData = 
//    		new FilteredList<>(FXCollections.observableArrayList(resourceService.getResources()), p -> true);
//    
//    // 2. Set the filter Predicate whenever the filter changes.
//    filterField.textProperty().addListener((observable, oldValue, newValue) -> {
//        filteredData.setPredicate(resource -> {
//            // If filter text is empty, display all persons.
//            if (newValue == null || newValue.isEmpty()) {
//                return true;
//            }
//            
//            // Compare first name and last name of every person with filter text.
//            String lowerCaseFilter = newValue.toLowerCase();
//            
//            if (resource.getId().toLowerCase().contains(lowerCaseFilter)) {
//                return true; // Filter matches first name.
//            } else if (resource.getName().toLowerCase().contains(lowerCaseFilter)) {
//                return true; // Filter matches last name.
//            }
//            return false; // Does not match.
//        });
//    });
//    
//    // 3. Wrap the FilteredList in a SortedList. 
//    SortedList<Resource> sortedData = new SortedList<Resource>(filteredData);
//    
//    // 4. Bind the SortedList comparator to the TableView comparator.
//    sortedData.comparatorProperty().bind(tableResourceView.comparatorProperty());
//    
//    // 5. Add sorted (and filtered) data to the table.
//    tableResourceView.setItems(sortedData);

		TableColumn<ProjectDTO, String> projNameColumn = new TableColumn<>("Name");
		projNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<ProjectDTO, LocalDate> projStartDateColumn = new TableColumn<>("Start date");
		projStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

		TableColumn<ProjectDTO, LocalDate> projEndDateColumn = new TableColumn<>("End date");
		projEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

		getColumns().add(projNameColumn);
		getColumns().add(projStartDateColumn);
		getColumns().add(projEndDateColumn);

		cm = new ContextMenu();
		mi1 = new MenuItem("New");
		cm.getItems().add(mi1);
		mi1.setUserData(new ProjectDTO());
		mi2 = new MenuItem("Edit");
		cm.getItems().add(mi2);
		mi3 = new MenuItem("Delete");
		cm.getItems().add(mi3);

		super.addEventHandler(MouseEvent.MOUSE_CLICKED, new RightClickHandler(this));

//		tableResourceView.setItems();
//	resourceView.setTop(filterField);
	}

	public void setProjects(ObservableList<ProjectDTO> projects) {
		setItems(projects);
	}

	public void addActionListener(EventHandler<ActionEvent> eventHandler) {
		mi1.setOnAction(eventHandler);
		mi2.setOnAction(eventHandler);
		mi3.setOnAction(eventHandler);
	}

	class RightClickHandler implements EventHandler<MouseEvent> {
		Node parent = null;

		public RightClickHandler(Node parent) {
			this.parent = parent;
		}

		@Override
		public void handle(MouseEvent me) {
			if (me.getButton() == MouseButton.SECONDARY) {

				mi2.setUserData(getSelectionModel().getSelectedItem());
				mi3.setUserData(getSelectionModel().getSelectedItem());

				cm.show(parent, me.getScreenX(), me.getScreenY());
			}
		}
	}

}
