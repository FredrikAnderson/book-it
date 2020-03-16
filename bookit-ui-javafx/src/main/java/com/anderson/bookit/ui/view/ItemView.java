package com.anderson.bookit.ui.view;

import java.io.InputStream;
import java.time.LocalDate;

import com.anderson.bookit.BookITUI;
import com.fredrik.bookit.ui.rest.model.ItemDTO;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ItemView extends BorderPane {

	ContextMenu cm;
	MenuItem miNew, miEdit, miDel; // miCp

	Text filterLbl = new Text("Item filter:");
	TextField filterTf = new TextField();

	TableView<ItemDTO> itemView = new TableView<ItemDTO>();

	
	public ItemView() {

		setPadding(new Insets(20, 20, 20, 20));

		VBox projFilterVx = new VBox(filterLbl, filterTf);

		Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        InputStream inStream = getClass().getResourceAsStream("/" + BookITUI.getInstance().getLogoImage());
        Image logo = new Image(inStream);
        ImageView image = new ImageView(logo);
        image.setFitWidth(40);
        image.setFitHeight(40);
        image.setPreserveRatio(true);
        
		HBox horisontalPnl = new HBox(projFilterVx, region1, image);

//		HBox horisontalPnl = new HBox(projFilterVx);
		horisontalPnl.setSpacing(10);
		horisontalPnl.setPadding(new Insets(0, 0, 5, 0));
		setTop(horisontalPnl);

		setCenter(itemView);
		
		TableColumn<ItemDTO, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<ItemDTO, String> descColumn = new TableColumn<>("Description");
		descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

		TableColumn<ItemDTO, String> invColumn = new TableColumn<>("Inventory");
		invColumn.setCellValueFactory(new PropertyValueFactory<>("inventory"));

		TableColumn<ItemDTO, String> pubIdColumn = new TableColumn<>("Public ID");
		pubIdColumn.setCellValueFactory(new PropertyValueFactory<>("PublicId"));

		TableColumn<ItemDTO, LocalDate> widthColumn = new TableColumn<>("Width");
		widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));

		TableColumn<ItemDTO, LocalDate> heightColumn = new TableColumn<>("Height");
		heightColumn.setCellValueFactory(new PropertyValueFactory<>("Height"));

		TableColumn<ItemDTO, LocalDate> lengthColumn = new TableColumn<>("Length");
		lengthColumn.setCellValueFactory(new PropertyValueFactory<>("Length"));

		TableColumn<ItemDTO, LocalDate> weightColumn = new TableColumn<>("Weight");
		weightColumn.setCellValueFactory(new PropertyValueFactory<>("Weight"));

		TableColumn<ItemDTO, LocalDate> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<>("Price"));

		itemView.getColumns().add(nameColumn);
		itemView.getColumns().add(descColumn);
		itemView.getColumns().add(invColumn);
		itemView.getColumns().add(pubIdColumn);
		itemView.getColumns().add(widthColumn);
		itemView.getColumns().add(heightColumn);
		itemView.getColumns().add(lengthColumn);
		itemView.getColumns().add(weightColumn);
		itemView.getColumns().add(priceColumn);

		cm = new ContextMenu();
		miNew = new MenuItem("New");
		cm.getItems().add(miNew);
		miNew.setUserData(new ItemDTO());
//		miCp = new MenuItem("Copy");
//		cm.getItems().add(miCp);
		miEdit = new MenuItem("Edit");
		cm.getItems().add(miEdit);
		miDel = new MenuItem("Delete");
		cm.getItems().add(miDel);
		
//		tableResourceView.setItems();
//	resourceView.setTop(filterField);
	}

	public void addRightClickMenu() {
		itemView.addEventHandler(MouseEvent.MOUSE_CLICKED, new RightClickHandler(this));
	}

	public void setModelItems(ObservableList<ItemDTO> items) {
		
		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<ItemDTO> filteredData = new FilteredList<>(FXCollections.observableArrayList(items),
				p -> true);

		// 2. Set the filter Predicate whenever the filter changes.
		filterTf.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(item -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (item.getId().toString().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches first name.
				} else if (item.getDescription().toString().toLowerCase().contains(lowerCaseFilter)) {
					return true;
				} else if (item.getInventory().toString().toLowerCase().contains(lowerCaseFilter)) {
					return true;
					// TODO
//				} else if (item.getPublicId().toString().toLowerCase().contains(lowerCaseFilter)) {
//					return true;
				} else if (item.getName().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});

		// 3. Wrap the FilteredList in a SortedList.
		SortedList<ItemDTO> sortedData = new SortedList<ItemDTO>(filteredData);

		// 4. Bind the SortedList comparator to the TableView comparator.
		sortedData.comparatorProperty().bind(itemView.comparatorProperty());

		// 5. Add sorted (and filtered) data to the table.
//        tableResourceView.setItems(sortedData);
		
		itemView.setItems(sortedData);
	}

	public ItemDTO getSelectedItem() {
		return itemView.getSelectionModel().getSelectedItem();
	}
	
	public void addActionListener(EventHandler<ActionEvent> eventHandler) {
		miNew.setOnAction(eventHandler);
//		miCp.setOnAction(eventHandler);
		miEdit.setOnAction(eventHandler);
		miDel.setOnAction(eventHandler);
	}

	class RightClickHandler implements EventHandler<MouseEvent> {
		Node parent = null;

		public RightClickHandler(Node parent) {
			this.parent = parent;
		}

		@Override
		public void handle(MouseEvent me) {
			if (me.getButton() == MouseButton.SECONDARY) {

//				miCp.setUserData(itemView.getSelectionModel().getSelectedItem());
				miEdit.setUserData(itemView.getSelectionModel().getSelectedItem());
				miDel.setUserData(itemView.getSelectionModel().getSelectedItem());

				cm.show(parent, me.getScreenX(), me.getScreenY());
			}
		}
	}

}
