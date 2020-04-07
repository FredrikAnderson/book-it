import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router'; 
import { Project } from '../../shared/project';
import { ProjectsServiceService } from '../../services/projects-service';
import { MatTableDataSource } from '@angular/material/table';
import { DataSource } from '@angular/cdk/table';

@Component({
  selector: 'app-projects-list',
  templateUrl: './projects-list.component.html',
  styleUrls: ['./projects-list.component.css']
})
export class ProjectsListComponent implements OnInit {

    displayedColumns: string[] = ['id', 'name', 'startdate', 'enddate'];

    projects = new MatTableDataSource<Project>();
        
    constructor(private router: Router,
                private projectservice : ProjectsServiceService) { }
    
    ngOnInit(): void {
      this.getProjects();
    }
    
    getProjects() {
      this.projects = new MatTableDataSource<Project>();
      this.projectservice.getProjects().subscribe((data : any) => {
          this.projects = new MatTableDataSource(data);  
      });      
    }

    public onNewProject() {
        this.router.navigate(['/project/new']);
      }
    
    public onRowClicked(project : Project) {    
//      console.log("clicked" + project.id);      
      
      this.router.navigate(['/project/' + project.id]);
    }
    
    applyFilter(event: Event) {
      const filterValue = (event.target as HTMLInputElement).value;
      this.projects.filter = filterValue.trim().toLowerCase();
    }


}
