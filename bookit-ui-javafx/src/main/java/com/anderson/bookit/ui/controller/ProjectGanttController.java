package com.anderson.bookit.ui.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.anderson.bookit.model.ProjectItemBooking;
import com.anderson.bookit.ui.service.ItemModificationListener;
import com.anderson.bookit.ui.service.ProjectService;
import com.anderson.bookit.ui.view.ProjectDialog;
import com.anderson.bookit.ui.view.ProjectGanttView;
import com.anderson.bookit.ui.view.ProjectItemBookingDialog;
import com.calendarfx.model.Entry;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class ProjectGanttController implements ItemModificationListener<Long, ProjectDTO> {

	ProjectService projectService = ProjectService.getInstance();

	ProjectGanttView view = new ProjectGanttView();

	ObservableList<ProjectDTO> projectsModel;

	ProjectDTO toEdit = null;
	
	public ProjectGanttController() {

		view.addActionListener(new ProjectInListActionHandler());

		projectService.addItemListener(this);
		
		List<ProjectDTO> projects = projectService.getProjects();
		projectsModel = FXCollections.observableArrayList(projects);
		updateData();
	}

	private void updateData() {
		
		view.setProjects(projectsModel);
	}

	public Node getView() {
		return view;
	}

	@Override
	public void itemModified(Long id, ItemEvent event, ProjectDTO item) {
		System.out.println("itemModified: " + id + ", " + event + ": " + item.toString());

		if (event == ItemEvent.ADD) {
			projectsModel.add(item);
			
		} else if (event == ItemEvent.EDIT) {
			int indexOf = projectsModel.indexOf(item);
			if (indexOf != -1) {
				projectsModel.set(indexOf, item);
			}			
			
		} else if (event == ItemEvent.DELETE) {
			projectsModel.remove(item);			
			
		}

		updateData();
	}

	private void showEditProjectDialog(String text, ProjectDTO toEdit, int indexinView) {
		this.toEdit = toEdit;
		
		ProjectDialog projDialog = new ProjectDialog();

		projDialog.addActionHandler(new ProjectActionHandler(projDialog));
		projDialog.editModel(text, toEdit, indexinView);
		
//		projDialog.s
	}

	class ProjectInListActionHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent event) {
//			System.out.println("MenuChoice handling: " + event.getSource());

			MenuItem mi = (MenuItem) event.getSource();
			String label = mi.getText();

			System.out.println("Event: " + event);
			System.out.println("EventSrc: " + event.getSource());
			System.out.println("MenuItem: " + mi.getText() + ", " + mi.getClass());
			if (mi.getUserData() != null) {
				System.out.println("UserData: " + mi.getUserData().toString());
			}
			if (mi.getUserData() != null) {
				actionOnObject(mi.getText(), (ProjectDTO) mi.getUserData()); // , view.getSelectionModel().getSelectedIndex());
			}

//			showScene(label);
		}
	}
	
	class ProjectActionHandler implements EventHandler<ActionEvent> {

		ProjectDialog projDialog = null;
		
		public ProjectActionHandler(ProjectDialog projDialog) {
			this.projDialog = projDialog;			
		}
		
		@Override
        public void handle(ActionEvent event) {
			System.out.println("Name: " + projDialog.getProjectName()); // getSelectionModel().getSelectedItem().toString());
            
            String projectName = projDialog.getProjectName();
            LocalDateTime localStartDate = projDialog.getStartDate();
            LocalDateTime localEndDate = projDialog.getEndDate();
            
            projDialog.close();
            
            toEdit.setName(projectName);
            toEdit.setStartDate(localStartDate.toLocalDate());
            toEdit.setEndDate(localEndDate.toLocalDate());
                        
    		ProjectDTO proj = projectService.saveProject(toEdit);
    		toEdit = proj;
    		
    		if (projDialog.getAction().toLowerCase().contains("new")) {
    			projectsModel.add(toEdit);
    		} else {
    			// Update project in model. First find project in model
    			int indexOf = projectsModel.indexOf(toEdit);
    			if (indexOf != -1) {
    				projectsModel.set(indexOf, toEdit);
    			}
    		}
        }
	}

	public void actionOnObject(String action, ProjectDTO proj) {
		ProjectDTO toEdit = proj;
		if (action.toLowerCase().contains("new")) {
			toEdit = new ProjectDTO();
		} 
		if (action.toLowerCase().contains("delete")) {
			projectService.deleteProject(toEdit);
			
			projectsModel.remove(toEdit);
			
//			view.getItems().remove(toEdit);
			
		} else if (action.toLowerCase().contains("book item")) {
			System.out.println("BOOK an Item");
			
			ProjectItemBookingDialog bookingDialog = new ProjectItemBookingDialog();
			ProjectItemBooking projItemBooking = new ProjectItemBooking();
			projItemBooking.setProject(toEdit);
			
			bookingDialog.editModel(projItemBooking);

		} else {
			// Edit
			showEditProjectDialog(action, toEdit, 1); // , indexinView);
		}			
		
	}

}
