package com.anderson.bookit.ui;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.Locale;

import com.anderson.bookit.ui.DateTimePicker.TimeChooserType;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
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

public class ProjectGanttView extends BorderPane {

	ContextMenu cm;
	MenuItem mi1, mi2, mi3;

	Text projFilterLbl = new Text("Project filter:");
	TextField projFilterTf = new TextField();

	Text timeViewLbl = new Text("Time view:");
	ChoiceBox<String> timeViewChoice = new ChoiceBox<String>();

	Text dateViewFromLbl = new Text("Date view, from:");
	DateTimePicker dateViewStart = new DateTimePicker(TimeChooserType.Date);
	
	Text dateViewToLbl = new Text("Date view, to:");
	DateTimePicker dateViewEnd = new DateTimePicker(TimeChooserType.Date);
	
	TableView<ProjectDTO> ganttView = new TableView<ProjectDTO>();

	enum TimeView {
		DAYS, WEEKS, MONTHS
	};

	LocalDate viewStartDate = LocalDate.now();

	int maxNrOfColumns = 40;
	TimeView ganttTimeView = TimeView.MONTHS;

	public ProjectGanttView() {

		setPadding(new Insets(20, 50, 20, 50));

		timeViewChoice.getItems().add("Days");
		timeViewChoice.getItems().add("Weeks");
		timeViewChoice.getItems().add("Months");
		timeViewChoice.setOnAction(this::timeViewChanged);

		VBox projFilterVx = new VBox(projFilterLbl, projFilterTf);

		VBox dateViewFromVx = new VBox(dateViewFromLbl, dateViewStart);
		VBox dateViewToVx = new VBox(dateViewToLbl, dateViewEnd);

		VBox timeViewVx = new VBox(timeViewLbl, timeViewChoice);

		HBox horisontalPnl = new HBox(projFilterVx, timeViewVx, dateViewFromVx, dateViewToVx);		
		horisontalPnl.setSpacing(10);
		horisontalPnl.setPadding(new Insets(0, 0, 5, 0));
		setTop(horisontalPnl);

		setCenter(ganttView);

		TableColumn<ProjectDTO, String> projNameColumn = new TableColumn<>("Name");
		projNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn<ProjectDTO, LocalDate> projStartDateColumn = new TableColumn<>("Start date");
		projStartDateColumn.setCellValueFactory(new PropertyValueFactory<>("startDate"));

		TableColumn<ProjectDTO, LocalDate> projEndDateColumn = new TableColumn<>("End date");
		projEndDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));

		ganttView.getColumns().add(projNameColumn);
		ganttView.getColumns().add(projStartDateColumn);
		ganttView.getColumns().add(projEndDateColumn);

		setGanttTimeView("Days");

		super.addEventHandler(MouseEvent.MOUSE_CLICKED, new RightClickHandler(this));
	}

	private void updateGanttColumns() {
		// Remove gantt columns
		int size = ganttView.getColumns().size();
		ganttView.getColumns().remove(3, size);
		
		addGanttColumns();
	}
	
	private void addGanttColumns() {

//		if (ganttTimeView == TimeView.DAYS) {			

		LocalDate curViewDate = dateViewStart.getLocalDateTime().toLocalDate();
		int totalNrOfAddedCols = 0;

		while (totalNrOfAddedCols < maxNrOfColumns) {

			// Top gantt column header
			TableColumn<ProjectDTO, String> topGanttCol = null;
			int maxInnerColumns = maxNrOfColumns;
			int currentInnerColumns = 0;
			if (ganttTimeView == TimeView.DAYS) {
				// Add month col, if day view
				topGanttCol = new TableColumn<>(monthInHumanFormat(curViewDate.getMonth().toString()));
				ganttView.getColumns().add(topGanttCol);

				YearMonth yearMonth = YearMonth.of(curViewDate.getYear(), curViewDate.getMonth());
				maxInnerColumns = yearMonth.lengthOfMonth();
				currentInnerColumns = curViewDate.getDayOfMonth();

			} else if (ganttTimeView == TimeView.WEEKS) {
				topGanttCol = new TableColumn<>("Weeks");
				ganttView.getColumns().add(topGanttCol);

			} else if (ganttTimeView == TimeView.MONTHS) {
				topGanttCol = new TableColumn<>("" + curViewDate.getYear());
				ganttView.getColumns().add(topGanttCol);
				maxInnerColumns = 12;
				currentInnerColumns = curViewDate.getMonthValue();

			}

			while (columnWithoutNewHeaderColumn(curViewDate, currentInnerColumns, maxInnerColumns, totalNrOfAddedCols,
					maxNrOfColumns)) {

				TableColumn<ProjectDTO, LocalDate> projDateColumn = null;
				if (ganttTimeView == TimeView.DAYS) {
					projDateColumn = new TableColumn<>("" + curViewDate.getDayOfMonth());

				} else if (ganttTimeView == TimeView.WEEKS) {

					WeekFields weekFields = WeekFields.of(Locale.getDefault());
					int weekNr = curViewDate.get(weekFields.weekOfWeekBasedYear());
					System.out.println("WeekNr: #" + weekNr);

					projDateColumn = new TableColumn<>("" + weekNr);
				} else if (ganttTimeView == TimeView.MONTHS) {

					String monthInHumanFormat = monthInHumanFormat(curViewDate.getMonth().toString()).substring(0, 3);
					System.out.println("MonthNr: #" + monthInHumanFormat);

					projDateColumn = new TableColumn<>("" + monthInHumanFormat);
				}

				projDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
				projDateColumn.setUserData(curViewDate);

				projDateColumn.setCellFactory(column -> {
					return new TableCell<ProjectDTO, LocalDate>() {
						@Override
						protected void updateItem(LocalDate item, boolean empty) {
							super.updateItem(item, empty);

							String text = column.getText();
							LocalDate userData = (LocalDate) column.getUserData();

							String color = null;
							int index = getIndex();
							ObservableList<ProjectDTO> items = getTableView().getItems();
							// System.out.println("Index:"+ index);
							if (index != -1 && index < items.size()) {
								System.out.println("Col, text:" + text);
								ProjectDTO projectDTO = items.get(index);
								System.out.println("Proj(" + index + "): " + projectDTO);

								color = getColorFor(text, projectDTO);
								System.out.println("Color is " + color);
							}

							if (item == null || empty) {
								setText(null);
								setStyle("");
								// setStyle("-fx-background-color: lightgreen");
							} else {
								// Format date.
								setText("");
								// setBack(value);

								if (color != null) {
									setStyle("-fx-background-color: " + color);
								}
							}
						}

						private String getColorFor(String text, ProjectDTO projectDTO) {
							String toret = "";

//								LocalDate dateForCol = (LocalDate) getUserData();
							LocalDate dateForCol = (LocalDate) column.getUserData();
//								System.out.println("LocalDate: " + dateForCol.toString());

							Boolean inc = (projectDTO.getStartDate().compareTo(dateForCol)
									* dateForCol.compareTo(projectDTO.getEndDate()) >= 0);

							System.out.println("Date: " + dateForCol + " is " + inc + " in " + projectDTO.getStartDate()
									+ " - " + projectDTO.getEndDate());
							if (inc) {
								toret = "#99ff66";
							}
							return toret;
						}
					};
				});

				topGanttCol.getColumns().add(projDateColumn);

				// Increasing column/view
				currentInnerColumns++;
				totalNrOfAddedCols++;

				// Next column
				if (ganttTimeView == TimeView.DAYS) {
					curViewDate = curViewDate.plusDays(1);

				} else if (ganttTimeView == TimeView.WEEKS) {
					curViewDate = curViewDate.plusDays(7);
				} else if (ganttTimeView == TimeView.MONTHS) {
					curViewDate = curViewDate.plusMonths(1);
				}

//					curViewDate = curViewDate.plusMonths(1).withDayOfMonth(1);
				System.out.println("CurrDate: " + curViewDate.toString());
			}
		}
	}

	private void timeViewChanged(ActionEvent event) {
		setGanttTimeView(timeViewChoice.getSelectionModel().getSelectedItem());
		
	}

	private void setGanttTimeView(String timeView) {
		timeViewChoice.getSelectionModel().select(timeView);
		
		if (timeView.equalsIgnoreCase("Days")) {
			ganttTimeView = TimeView.DAYS;
			dateViewStart.setLocalDate(viewStartDate);
			dateViewEnd.setLocalDate(viewStartDate.plusDays(maxNrOfColumns));

		} else if (timeView.equalsIgnoreCase("Weeks")) {
			ganttTimeView = TimeView.WEEKS;
			dateViewStart.setLocalDate(viewStartDate);
			dateViewEnd.setLocalDate(viewStartDate.plusWeeks(maxNrOfColumns));
			
		} else if (timeView.equalsIgnoreCase("Months")) {
			ganttTimeView = TimeView.MONTHS;
			dateViewStart.setLocalDate(viewStartDate);
			dateViewEnd.setLocalDate(viewStartDate.plusMonths(maxNrOfColumns));
		}
		
		// 
		updateGanttColumns();
	}
	
	private boolean columnWithoutNewHeaderColumn(LocalDate curViewDate, int currentInnerColumns, int maxInnerColumns,
			int totalNrOfAddedCols, int maxNrOfColumns) {

//		for (int i = curViewDate.getDayOfMonth(); i <= lengthOfMonth && nrOfAddedCols < columns; i++) {

		if (currentInnerColumns <= maxInnerColumns && totalNrOfAddedCols < maxNrOfColumns) {
			return true;
		}

		return false;
	}

	private String monthInHumanFormat(String monthName) {
		String lowerCase = monthName.toLowerCase();
		String toret = lowerCase.substring(0, 1).toUpperCase() + lowerCase.substring(1);
		return toret;
	}

	public void setProjects(ObservableList<ProjectDTO> projects) {
		
		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ProjectDTO> filteredData = 
        		new FilteredList<>(FXCollections.observableArrayList(projects), p -> true);
        
        // 2. Set the filter Predicate whenever the filter changes.
        projFilterTf.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(project -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                
                if (project.getId().toString().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (project.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<ProjectDTO> sortedData = new SortedList<ProjectDTO>(filteredData);
        
        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(ganttView.comparatorProperty());
        
        // 5. Add sorted (and filtered) data to the table.
//        tableResourceView.setItems(sortedData);
		
		ganttView.setItems(sortedData);
	}

	public void addActionListener(EventHandler<ActionEvent> eventHandler) {
//		mi1.setOnAction(eventHandler);
//		mi2.setOnAction(eventHandler);
//		mi3.setOnAction(eventHandler);
	}

	class RightClickHandler implements EventHandler<MouseEvent> {
		Node parent = null;

		public RightClickHandler(Node parent) {
			this.parent = parent;
		}

		@Override
		public void handle(MouseEvent me) {
			if (me.getButton() == MouseButton.SECONDARY) {

//				mi2.setUserData(getSelectionModel().getSelectedItem());
//				mi3.setUserData(getSelectionModel().getSelectedItem());
//
//				cm.show(parent, me.getScreenX(), me.getScreenY());
			}
		}
	}

}
