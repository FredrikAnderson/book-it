import { Component, OnInit, ViewChild, Input } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { ProjectEditComponent } from '../../projects/project-edit/project-edit.component';
import { Project } from '../../shared/project';
import { ProjectsServiceService } from '../../services/projects-service';
import { ItemsListComponent } from '../../items/items-list/items-list.component';

@Component( {
    selector: 'app-projects-book-item',
    templateUrl: './projects-book-item.component.html',
    styleUrls: ['./projects-book-item.component.css']
} )
export class ProjectsBookItemComponent implements OnInit {

    // Possible values: new-project-and-item, select-item
    @Input('type') type : string = "new-project-and-item";

    @ViewChild( ItemsListComponent )
    private itemsListComponent: ItemsListComponent;

    @ViewChild( ProjectEditComponent )
    private projectEditComponent: ProjectEditComponent;

    project : Project = null;

    constructor(
            private route: ActivatedRoute,
            private router: Router,
            private projectService: ProjectsServiceService
            ) {}

    ngOnInit(): void {
        if (this.type.includes('select-item')) {
            this.project = new Project();
            
            this.projectService.currentProject.subscribe( proj => {
                if (proj) {    
                    console.log("Current project changed to: " + JSON.stringify(proj));
                    this.setProject(proj);
                }
            });        
        }
        console.log("Type: " + this.type + ", proj: " + this.project);
    }

    setProject(project : Project) {
        this.project = project;
    }
    
    resetForm() {
        this.itemsListComponent.resetForm();

        if (this.projectEditComponent) {
            this.projectEditComponent.resetForm();
        }
    }
    
    public onNewProjectItemBooking() {
        if (this.itemsListComponent.selectedItem == null) {
            // error situation, notify user and do nothing
            // TODO
            console.log("error situation, notify user and do nothing");
            
            return;
        }
//        console.log("Sel item ", this.itemsListComponent.selectedItem);
        var itemId = this.itemsListComponent.selectedItem.id;

        if (this.project) {
            this.bookItemForProjectId(itemId, this.project.id);
            
        } else {
        
            // Save project data using project component
            this.projectEditComponent.saveProject().subscribe((proj : Project) => {
    
                // Store project for possible usage
    //            var project = proj;
    //            console.log("Sel project ", JSON.stringify(proj));
                                
                const txt = "Should book item, id: " + itemId + ", for project: " + JSON.stringify(proj);
                console.log( txt );
    //            window.alert( txt );
    
                this.bookItemForProjectId(itemId, proj.id);
            })        
        }
    }    
    
    bookItemForProjectId(itemId : string, projId : string) {
        const txt = "Should book item, id: " + itemId + ", for project id: " + projId;
        console.log( txt );
        
        this.projectService.bookItemForProject(itemId, projId).subscribe((savedProj : Project) => {

            console.log("Saved project ", JSON.stringify(savedProj));

            // window.alert( "For Project: " + savedProj.name + ", you have now booked item: " + itemId );

            // TODO
//            this.router.navigate( ['/projects-book-item'] );                
            this.resetForm();
            window.location.reload();

        });
    }
    
}
