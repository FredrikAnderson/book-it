import { Component, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { Project } from '../../shared/project';
import { ProjectsServiceService } from '../../services/projects-service';
import { ProjectItemsListComponent } from '../../projects/project-items-list/project-items-list.component';
import { ProjectsBookItemComponent } from '../../projects/projects-book-item/projects-book-item.component';
import { MatTableDataSource } from '@angular/material/table';
import { DataSource } from '@angular/cdk/table';

@Component( {
    selector: 'app-projects-list',
    templateUrl: './projects-list.component.html',
    styleUrls: ['./projects-list.component.css']
} )
export class ProjectsListComponent implements OnInit {

    displayedColumns: string[] = ['id', 'name', 'startdate', 'enddate'];

    projects = new MatTableDataSource<Project>();

    // Only for type, select
    selectedProject : Project;

    constructor( private router: Router,
        private projectservice: ProjectsServiceService ) {
    }

    ngOnInit(): void {
        this.getProjects();
    }

    getProjects() {
        this.projects = new MatTableDataSource<Project>();
        this.projectservice.getProjects().subscribe(( data: any ) => {
            this.projects = new MatTableDataSource( data );
        } );
    }

    public onNewProject() {
        this.router.navigate( ['/project/new'] );
    }

    public onEditProject() {
        if (this.selectedProject) {
            console.log("onEditProject: " + this.selectedProject.id );      
            
            this.router.navigate( ['/project/' + this.selectedProject.id ] );
        }
    }

    public onRowClicked( project: Project ) {
        console.log("clicked project id: " + project.id);      

        //        this.router.navigate( ['/project/' + project.id] );
        
        if (this.selectedProject === project) {
            // Unselect
            this.selectedProject = null;
        } else {
            // Select
            this.selectedProject = project;              
        }
        
        // 
        this.router.navigate( ['/project/' + project.id ] );

//        this.projectItemsListComponent.setProject(this.selectedProject);
//        this.projectsBookItemComponent.setProject(this.selectedProject);
    }

      public onRowBackgroundColor(project : Project) {
          
          if (this.selectedProject === project) {
//              console.log("selected item: ", item);      
              return 'lightblue';
          } 
        }

    applyFilter( event: Event ) {
        const filterValue = ( event.target as HTMLInputElement ).value;
        this.projects.filter = filterValue.trim().toLowerCase();
    }


}
