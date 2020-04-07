import { Component, OnInit, ViewChild } from '@angular/core';
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

    @ViewChild( ItemsListComponent )
    private itemsListComponent: ItemsListComponent;

    @ViewChild( ProjectEditComponent )
    private projectEditComponent: ProjectEditComponent;


    constructor(
            private route: ActivatedRoute,
            private router: Router,
            private projectService: ProjectsServiceService,
        ) { }

    ngOnInit(): void {
    }
    
    resetForm() {
        this.itemsListComponent.resetForm();
        
        this.projectEditComponent.resetForm();
    }
    
    public onNewProjectItemBooking() {
        if (this.itemsListComponent.selectedItem == null) {
            // error situation, notify user and do nothing
            // TODO
            console.log("error situation, notify user and do nothing");
            
            return;
        }
//        console.log("Sel item ", this.itemsListComponent.selectedItem);

        // Save project data using project component
        this.projectEditComponent.saveProject().subscribe((proj : Project) => {

            // Store project for possible usage
//            var project = proj;
//            console.log("Sel project ", JSON.stringify(proj));
            
            var itemId = this.itemsListComponent.selectedItem.id;
            
            const txt = "Should book item, id: " + itemId + ", for project: " + JSON.stringify(proj);
            console.log( txt );
//            window.alert( txt );

            this.projectService.bookItemForProject(itemId, proj.id).subscribe((savedProj : Project) => {

                console.log("Saved project ", JSON.stringify(savedProj));

                window.alert( "For Project: " + proj.name + ", you have now booked item: " + itemId );

                this.router.navigate( ['/projects-book-item'] );                
                this.resetForm();
            });
        })
    }
    
    
}
