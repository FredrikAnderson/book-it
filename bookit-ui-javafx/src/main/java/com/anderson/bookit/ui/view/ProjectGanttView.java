package com.anderson.bookit.ui.view;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.time.temporal.WeekFields;
import java.util.Locale;

import com.anderson.bookit.ui.component.DateTimePicker;
import com.anderson.bookit.ui.component.DateTimePicker.TimeChooserType;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

	DateChangeListener dateListener = new DateChangeListener();

	TableView<ProjectDTO> ganttView = new TableView<ProjectDTO>();

	String GREEN_COLOR_HEX_CODE = "#a4f3a9";
	String YELLOW_COLOR_HEX_CODE = "#ffff1a";
	
	enum TimeView {
		DAYS, WEEKS, MONTHS
	};

	LocalDate viewStartDate = LocalDate.now();

	int maxNrOfColumns = 40;
	TimeView ganttTimeView = TimeView.MONTHS;

	public ProjectGanttView() {

		setPadding(new Insets(20, 20, 20, 20));

		timeViewChoice.getItems().add("Days");
		timeViewChoice.getItems().add("Weeks");
		timeViewChoice.getItems().add("Months");

		VBox projFilterVx = new VBox(projFilterLbl, projFilterTf);

		HBox dividerHx = new HBox(20);
		dividerHx.setPrefWidth(80);
		VBox dateViewFromVx = new VBox(dateViewFromLbl, dateViewStart);
		VBox dateViewToVx = new VBox(dateViewToLbl, dateViewEnd);
		VBox timeViewVx = new VBox(timeViewLbl, timeViewChoice);

		HBox horisontalPnl = new HBox(projFilterVx, dividerHx, timeViewVx, dateViewFromVx, dateViewToVx);
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

		// Adding listeners for user actions
		timeViewChoice.setOnAction(this::timeViewChanged);

		super.addEventHandler(MouseEvent.MOUSE_CLICKED, new RightClickHandler(this));
	}

	private void updateGanttColumns() {
		// Remove gantt columns
		int size = ganttView.getColumns().size();
		ganttView.getColumns().remove(3, size);

		addGanttColumns();
	}

	private void addGanttColumns() {

		System.out.println("Adding Gantt columns from: " + dateViewStart.getLocalDate().toString() + " to "
				+ dateViewEnd.getLocalDate().toString() + " for view: " + ganttTimeView.toString());

		LocalDate curViewDate = dateViewStart.getLocalDate();
		while (curViewDate.isBefore(dateViewEnd.getLocalDate())) {

			// Top gantt column header
			TableColumn<ProjectDTO, String> topGanttCol = null;
			if (ganttTimeView == TimeView.DAYS) {
				// Add month col, if day view
				topGanttCol = new TableColumn<>(monthInHumanFormat(curViewDate.getMonth().toString()));
				ganttView.getColumns().add(topGanttCol);

			} else if (ganttTimeView == TimeView.WEEKS) {
				topGanttCol = new TableColumn<>("Weeks");
				ganttView.getColumns().add(topGanttCol);

			} else if (ganttTimeView == TimeView.MONTHS) {
				topGanttCol = new TableColumn<>("" + curViewDate.getYear());
				ganttView.getColumns().add(topGanttCol);

			}

			boolean newHeaderColumn = false;
			while (columnWithoutNewHeaderColumn(newHeaderColumn, curViewDate, dateViewEnd.getLocalDate())) {

				TableColumn<ProjectDTO, LocalDate> projDateColumn = null;
				if (ganttTimeView == TimeView.DAYS) {
					projDateColumn = new TableColumn<>("" + curViewDate.getDayOfMonth());

				} else if (ganttTimeView == TimeView.WEEKS) {
					int weekNr = curViewDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
					projDateColumn = new TableColumn<>("" + weekNr);

				} else if (ganttTimeView == TimeView.MONTHS) {
					String monthInHumanFormat = monthInHumanFormat(curViewDate.getMonth().toString()).substring(0, 3);
					projDateColumn = new TableColumn<>("" + monthInHumanFormat);

				}

				projDateColumn.setCellValueFactory(new PropertyValueFactory<>("endDate"));
				projDateColumn.setUserData(curViewDate);

				projDateColumn.setCellFactory(column -> {
					return new TableCell<ProjectDTO, LocalDate>() {
						@Override
						protected void updateItem(LocalDate item, boolean empty) {
							super.updateItem(item, empty);
							String color = null;
							LocalDate userData = (LocalDate) column.getUserData();

							int index = getIndex();
							ObservableList<ProjectDTO> items = getTableView().getItems();
							if (index != -1 && index < items.size()) {
								ProjectDTO projectDTO = items.get(index);

								color = getColorFor(projectDTO, userData);
							}

							if (item == null || empty) {
								setText(null);
								setStyle("");
							} else {
								setText("");
								if (color != null && !color.isEmpty()) {
									setStyle("-fx-background-color: " + color);
								}
							}
						}
					};
				});
				// Add column to table model
				topGanttCol.getColumns().add(projDateColumn);

				// Next column
				newHeaderColumn = false;
				LocalDate prevViewDate = curViewDate;
				if (ganttTimeView == TimeView.DAYS) {
					curViewDate = curViewDate.plusDays(1);
					newHeaderColumn = prevViewDate.getMonthValue() != curViewDate.getMonthValue();

				} else if (ganttTimeView == TimeView.WEEKS) {
					curViewDate = curViewDate.plusDays(7);
				} else if (ganttTimeView == TimeView.MONTHS) {
					curViewDate = curViewDate.plusMonths(1);
					newHeaderColumn = prevViewDate.getYear() != curViewDate.getYear();
				}
//				System.out.println("CurrDate: " + curViewDate.toString());
			}
		}
	}

	private String getColorFor(ProjectDTO projectDTO, LocalDate viewDate) {
		String toret = "";

		if (ganttTimeView == TimeView.DAYS) {
			Boolean inc = (projectDTO.getStartDate().compareTo(viewDate)
					* viewDate.compareTo(projectDTO.getEndDate()) >= 0);

//			System.out.println("Date: " + viewDate + " is " + inc + " in " + projectDTO.getStartDate()
//				+ " - " + projectDTO.getEndDate());
			if (inc) {
				toret = GREEN_COLOR_HEX_CODE; // "#99ff66";
			}

		} else if (ganttTimeView == TimeView.WEEKS) {
			LocalDate weekStartDate = viewDate.with(ChronoField.DAY_OF_WEEK, 1);
			LocalDate weekEndDate = viewDate.with(ChronoField.DAY_OF_WEEK, 7);
			toret = getColorForPeriod(weekStartDate, weekEndDate, projectDTO.getStartDate(), projectDTO.getEndDate());

		} else if (ganttTimeView == TimeView.MONTHS) {
			LocalDate monthStartDate = viewDate.withDayOfMonth(1);
			LocalDate monthEndDate = viewDate.withDayOfMonth(viewDate.lengthOfMonth());
			toret = getColorForPeriod(monthStartDate, monthEndDate, projectDTO.getStartDate(), projectDTO.getEndDate());

		}

		return toret;
	}

	private String getColorForPeriod(LocalDate startPer, LocalDate endPer, LocalDate mdlStart, LocalDate mdlEnd) {
		String toret = "";

		Boolean startDateInc = (mdlStart.compareTo(startPer) * startPer.compareTo(mdlEnd) >= 0);
		Boolean endDateInc = (mdlStart.compareTo(endPer) * endPer.compareTo(mdlEnd) >= 0);

//		System.out.println("Period start Date: " + startPer  + " is " + startDateInc + " in " + mdlStart + " - " + mdlEnd);
//		System.out.println("Period end Date: " + endPer + " is " + endDateInc + " in " + mdlStart + " - " + mdlEnd);		
		if (startDateInc && endDateInc) {
			toret = GREEN_COLOR_HEX_CODE; // "#99ff66"; // Green
		} else if (startDateInc || endDateInc) {
			toret = YELLOW_COLOR_HEX_CODE; // "#ffff1a"; // Yellow
		}

		return toret;
	}

	private void timeViewChanged(ActionEvent event) {
		setGanttTimeView(timeViewChoice.getSelectionModel().getSelectedItem());
	}

	private void setGanttTimeView(String timeView) {
		timeViewChoice.getSelectionModel().select(timeView);
		removeDateChangeListeners();

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
		addDateChangeListeners();

		//
		updateGanttColumns();
	}

	private boolean columnWithoutNewHeaderColumn(boolean newHeaderColumn, LocalDate curViewDate,
			LocalDate endViewDate) {

		if (!newHeaderColumn && curViewDate.isBefore(endViewDate)) {
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
		FilteredList<ProjectDTO> filteredData = new FilteredList<>(FXCollections.observableArrayList(projects),
				p -> true);

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

	private void removeDateChangeListeners() {
		dateViewStart.removeChangeListener(dateListener);
		dateViewEnd.removeChangeListener(dateListener);
	}

	private void addDateChangeListeners() {
		dateViewStart.addChangeListener(dateListener);
		dateViewEnd.addChangeListener(dateListener);
	}

	class DateChangeListener implements ChangeListener<LocalDate> {

		@Override
		public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
			updateGanttColumns();
		}
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
