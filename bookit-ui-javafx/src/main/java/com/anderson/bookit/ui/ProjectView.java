package com.anderson.bookit.ui;

import java.time.LocalDate;

import com.fredrik.bookit.ui.rest.model.ItemDTO;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ProjectView extends BorderPane {

	ContextMenu cm;
	MenuItem mi1, mi2, mi3;

	
	Text filterLbl = new Text("Project filter:");
	TextField filterTf = new TextField();

	TableView<ProjectDTO> projView = new TableView<ProjectDTO>();

	public ProjectView() {

		setPadding(new Insets(20, 20, 20, 20));

		VBox projFilterVx = new VBox(filterLbl, filterTf);

		HBox horisontalPnl = new HBox(projFilterVx);
		horisontalPnl.setSpacing(10);
		horisontalPnl.setPadding(new Insets(0, 0, 5, 0));
		setTop(horisontalPnl);

		setCenter(projView);

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

		projView.getColumns().add(projNameColumn);
		projView.getColumns().add(projStartDateColumn);
		projView.getColumns().add(projEndDateColumn);

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
		
		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<ProjectDTO> filteredData = new FilteredList<>(FXCollections.observableArrayList(projects),
				p -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		filterTf.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(proj -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (proj.getId().toString().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches first name.
				} else if (proj.getName().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<ProjectDTO> sortedData = new SortedList<ProjectDTO>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(projView.comparatorProperty());

		// 5. Add sorted (and filtered) data to the table.
//        tableResourceView.setItems(sortedData);
		
		projView.setItems(sortedData);
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

				mi2.setUserData(projView.getSelectionModel().getSelectedItem());
				mi3.setUserData(projView.getSelectionModel().getSelectedItem());

				cm.show(parent, me.getScreenX(), me.getScreenY());
			}
		}
	}

}
