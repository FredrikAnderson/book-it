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

    loggedInUser : boolean = false;
    user : User = null;
    
    constructor(
        private userService: UsersServiceService ) {

        console.log("app comp () called");
        
        this.userService.currentUser.subscribe( currentUser => {
            console.log("Current user changed to: " + JSON.stringify(currentUser));
            this.user = currentUser;
            this.update();
        });        
    }

    public update() {

        // User logged in
        if ( this.user ) {
            this.loggedInUser = true;
                        
        } else {
            this.loggedInUser = false;

        }

        this.updateUIForUser();
        
        console.log("app comp after cont. init called");
    }
    
    updateUIForUser() {
        
        if (this.user && this.user.role.includes('admin')) {
            this.showUsers = true;
            this.showItems = true;
            this.showProjects = true;            
        } else {
            this.showUsers = false;
            this.showItems = false;
            this.showProjects = false;
        }
        
    }

}
