package com.anderson.bookit.ui.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.anderson.bookit.model.Project;
import com.anderson.bookit.ui.service.ItemModificationListener;
import com.anderson.bookit.ui.service.ProjectService;
import com.anderson.bookit.ui.view.ProjectDialog;
import com.anderson.bookit.ui.view.ProjectView;
import com.fredrik.bookit.ui.rest.model.ProjectDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;

public class ProjectController implements ItemModificationListener<Long, ProjectDTO> {

	ProjectService projectService = ProjectService.getInstance();

	ProjectView view = new ProjectView();

	ObservableList<ProjectDTO> projectsModel;

	ProjectDTO toEdit = null;
	
	public ProjectController() {

		view.addActionListener(new ProjectInListActionHandler());
		
		projectService.addItemListener(this);
		
		List<ProjectDTO> projects = projectService.getProjects();
		projectsModel = FXCollections.observableArrayList(projects);
		updateData();
	}

	private void updateData() {
//		List<ProjectDTO> projects = projectService.getProjects();
//		projectsModel = FXCollections.observableArrayList(projects);
		
		view.setProjects(projectsModel);
	}

	public Node getView() {
		return view;
	}

//	private void showEditProjectDialog(String text, ProjectDTO toEdit, int indexinView) {
//		this.toEdit = toEdit;
//		
//		ProjectDialog projDialog = new ProjectDialog();
//
//		projDialog.addActionHandler(new ProjectActionHandler(projDialog));
//		projDialog.editModel(text, toEdit, indexinView);
//		
////		projDialog.s
//	}

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
    		
//    		if (projDialog.getAction().toLowerCase().contains("new")) {
//    			projectsModel.add(toEdit);
//
//    			view.setProjects(projectsModel);
//    		} else {
//    			// Update project in model. First find project in model
//    			int indexOf = projectsModel.indexOf(toEdit);
//    			if (indexOf != -1) {
//    				projectsModel.set(indexOf, toEdit);
//        			view.setProjects(projectsModel);
//    			}
//    		}
        }
	}

	public void actionOnObject(String action, ProjectDTO proj) {
		toEdit = proj;
		if (action.toLowerCase().contains("new")) {
			toEdit = new ProjectDTO();
		} 
		if (action.toLowerCase().contains("delete")) {
			projectService.deleteProject(toEdit);
			
//			projectsModel.remove(toEdit);
//			view.setProjects(projectsModel);
			
		} else {
			ProjectDialog projDialog = new ProjectDialog();

			projDialog.addActionHandler(new ProjectActionHandler(projDialog));
			projDialog.editModel(action, toEdit, 1);
//			showEditProjectDialog(action, toEdit, 1); // , indexinView);
		}			
		
	}

}
