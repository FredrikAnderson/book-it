import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { tap, map, retry, catchError } from 'rxjs/operators';
import { Project } from '../shared/project';

@Injectable({
  providedIn: 'root'
})
export class ProjectsServiceService {

    apiURL = "http://localhost:8888/api";

    public currentProject = new BehaviorSubject<Project>(null);
    
    constructor(private http : HttpClient) { 

        this.apiURL = window.location.protocol + "//" + window.location.hostname + ":8888/api"; 
        
    }
    
    //Http Options
    httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    }

    public setCurrentProject(project : Project) {
//        console.log("Current project changed to: " + JSON.stringify(project));

        this.currentProject.next(project);        
    }

    public saveProject(project : Project) : Observable<Project> {
        
        if (project.id === undefined || project.id === "") {
            // Post, new
            return this.http.post<Project>(this.apiURL + "/projects/", project)
            .pipe(
                    tap( data => console.log("posted project: ", data)),          
                    retry(1), 
                    catchError(this.handleError)
            );            
        } else {
            // Put, update
            return this.http.put<Project>(this.apiURL + "/projects/" + project.id, project)
            .pipe(
                    tap( data => console.log("updated project: ", data)),          
                    retry(1), 
                    catchError(this.handleError)
            );
        }
      }

    // HttpClient API get() method => Get Items list
    getProjects() : Observable<Project> {
      return this.http.get<any>(this.apiURL + "/projects")
          .pipe(
                  map( response => response.items),
                  tap(data => console.log("Got projects: ", data)),
                  retry(1), 
                  catchError(this.handleError)
          );
    }

    getProject(id) : Observable<Project> {
        return this.http.get<Project>(this.apiURL + "/projects/" + id)
            .pipe(
                    tap(data => console.log("Got project: ", data)),
                    retry(1), 
                    catchError(this.handleError)
            );
      }
    
    deleteProject(id) : Observable<any> {
        return this.http.delete<any>(this.apiURL + "/projects/" + id)
            .pipe(
                    tap( data => console.log("deleted project: " + id)),          
                    retry(1), 
                    catchError(this.handleError)
            );
      }

    bookItemForProject(itemId : string, projId : string) : Observable<any> {
        return this.http.put<any>(this.apiURL + "/projects/" + projId + "/book/" + itemId, projId)
            .pipe(
                    tap( data => console.log("book item " + itemId + " for project: " + projId + ", returned: " + data)),          
                    retry(1), 
                    catchError(this.handleError)
            );
      }

    cancelItemForProject(itemId : string, projId : string) : Observable<any> {
        return this.http.delete<any>(this.apiURL + "/projects/" + projId + "/cancel/" + itemId)
            .pipe(
                    tap( data => console.log("book item " + itemId + " for project: " + projId + ", returned: " + data)),          
                    retry(1), 
                    catchError(this.handleError)
            );
      }

    
    //Error handling 
    handleError(error) {
       let errorMessage = '';
       if(error.error instanceof ErrorEvent) {
         // Get client-side error
         errorMessage = error.error.message;
       } else if(error.error.message != null) {
           errorMessage = error.error.message;
       
       } else {
//           console.log("Body", error.error.message);
           
         // Get server-side error
         errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
       }
       window.alert(errorMessage);
       return throwError(errorMessage);
    }    
}