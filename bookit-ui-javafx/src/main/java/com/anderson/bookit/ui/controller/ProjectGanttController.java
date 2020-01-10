package com.anderson.bookit.ui.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.anderson.bookit.ui.service.ProjectService;
import com.anderson.bookit.ui.view.ProjectDialog;
import com.anderson.bookit.ui.view.ProjectGanttView;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class ProjectGanttController {

	ProjectService projectService = new ProjectService();

	ProjectGanttView view = new ProjectGanttView();

	ObservableList<ProjectDTO> projectsModel;

	ProjectDTO toEdit = null;
	
	public ProjectGanttController() {

		view.addActionListener(new ProjectInListActionHandler());
		
		List<ProjectDTO> projects = projectService.getProjects();
		projectsModel = FXCollections.observableArrayList(projects);
		
		view.setProjects(projectsModel);
	}

	public Node getView() {
		return view;
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
			
		} else {
			showEditProjectDialog(action, toEdit, 1); // , indexinView);
		}			
		
	}

}
