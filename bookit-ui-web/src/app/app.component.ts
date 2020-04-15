import { Component } from '@angular/core';
import { UsersServiceService } from './services/users-service';
import { User } from './shared/user';

@Component( {
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
} )
export class AppComponent {
    title = 'bookit-webui';

    showUsers: boolean = false;
    showItems: boolean = false;
    showProjects: boolean = false;
    showBookItem: boolean = false;

    loggedInUser : boolean = false;
    user : User = null;
    
    constructor(
        private userService: UsersServiceService ) {
        
        this.userService.currentUser.subscribe( currentUser => {
            console.log("Current user changed to: " + JSON.stringify(currentUser));
            this.user = currentUser;
            this.update();
        });        
    }

    public update() {

        // User logged in
        if ( this.user && this.user != null) {
            this.loggedInUser = true;
                        
        } else {
            this.loggedInUser = false;

        }

        this.updateUIForUser();
    }
    
    updateUIForUser() {
//        console.log("user: " + this.user + ", loggedIn: " + this.loggedInUser);
        
        if (this.user && this.user != null) {
            if (this.user.role.includes('admin')) {
                this.showUsers = true;
                this.showItems = true;
                this.showProjects = true;            
                this.showBookItem = true;            
            } else {
                this.showUsers = false;
                this.showItems = false;
                this.showProjects = false;
                this.showBookItem = true;            
            }
        } else {
            
            this.showUsers = false;
            this.showItems = false;
            this.showProjects = false;
            this.showBookItem = false;                        
        }
    }

}
