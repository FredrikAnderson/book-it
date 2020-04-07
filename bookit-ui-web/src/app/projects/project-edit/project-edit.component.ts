import { Component, OnInit, Input } from '@angular/core';
import { Router, ActivatedRoute, ParamMap } from '@angular/router';
import { switchMap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { FormGroup, FormControl } from '@angular/forms';
import { Project } from '../../shared/project';
import { ProjectsServiceService } from '../../services/projects-service';

@Component( {
    selector: 'app-project-edit',
    templateUrl: './project-edit.component.html',
    styleUrls: ['./project-edit.component.css']
} )
export class ProjectEditComponent implements OnInit {

    // Possible values: operations, nooperations
    @Input('type') type : string = "operations";

    projectForm = new FormGroup( {
        id: new FormControl( '' ),
        name: new FormControl( '' ),
        startDate: new FormControl( '' ),
        endDate: new FormControl( '' )
    } );

    showId : boolean = true;
    showSaveBtn : boolean = true;
    showDeleteBtn : boolean = true;
    
    project = new Project();

    constructor( private route: ActivatedRoute,
        private router: Router,
        private projectService: ProjectsServiceService ) {
    }

    ngOnInit(): void {
        const projId = this.route.snapshot.paramMap.get( 'id' );
        console.log( "Project id: " + projId );

        if (this.type.includes('nooperations')) {
            this.showId = false;
            this.showSaveBtn = false;
            this.showDeleteBtn = false;            
        }
        
        // New project
        if ( projId == null || projId.includes( 'new' ) ) {

            this.updateModelAndUpdateView( new Project() );

        } else {
            // Existing project
            this.projectService.getProject( projId ).subscribe( data => {

                this.updateModelAndUpdateView( data );
            } );
        }
    }
    
    resetForm() {
        
        for (let name in this.projectForm.controls) {
            this.projectForm.controls[name].setValue('');
            this.projectForm.controls[name].setErrors(null);
        }
    }

    updateModelAndUpdateView( proj: Project ) {
        this.project = proj;
        this.updateViewFromModel();
    }

    updateViewFromModel() {
        //      console.log("updateView: " + this.project.id + ", " + this.project.startDate);

        this.projectForm.get( 'id' ).setValue( this.project.id );
        this.projectForm.get( 'name' ).setValue( this.project.name );
        this.projectForm.get( 'startDate' ).setValue( this.project.startDate );
        this.projectForm.get( 'endDate' ).setValue( this.project.endDate );
    }

    updateModelFromView() {
        this.project = Object.assign( {}, this.projectForm.value );
        
        this.timesToUTC();        
    }
    
    timesToUTC() {
        this.project.startDate = this.toUTC( new Date(this.project.startDate) );
        this.project.endDate = this.toUTC( new Date(this.project.endDate) );
    }

    toUTC( date: Date ): Date {
        //      return new Date(date.getTime() + date.getTimezoneOffset() * 60000);
        return new Date( date.getTime() + 6 * 3600 * 1000 );    // Add 6 hours from 00:00, makes dates correct
    }

    public onSubmit() {
        console.warn( "onSubmit" );
    }

    public onSaveProject() {
        this.updateModelFromView();

        this.saveProject().subscribe(( data: Project ) => {
            this.project = data;
            
            this.router.navigate( ['/projects'] );
        } );
    }

    public saveProject() : Observable<Project> {
        //      console.warn(this.projectForm.value);
        this.updateModelFromView();

        return this.projectService.saveProject( this.project );
    }
    
    public onDeleteProject() {
        const id = this.projectForm.get( 'id' ).value;
        //    window.alert("Should delete project. " + id);

        this.projectService.deleteProject( id ).subscribe(( data: any ) => {
            this.router.navigate( ['/projects'] );
        } );
    }

    debugData() {        
        console.log("Component Type: ", this.type);
        console.log("Model, Project: ", this.project );        
    }

}
