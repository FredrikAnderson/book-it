import { Component, OnInit, ViewChild } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsersServiceService } from '../../services/users-service';
import { User } from '../../shared/user';
import { AppComponent } from '../../app.component';


@Component( {
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.css']
} )
export class LoginComponent implements OnInit {

    @ViewChild( AppComponent )
    private appComponent: AppComponent;

    loginForm: FormGroup;

    showLoginBtn : boolean = true;

    loading = false;
    submitted = false;
    returnUrl: string;

    constructor(
        private formBuilder: FormBuilder,
        private route: ActivatedRoute,
        private router: Router,
        private userService : UsersServiceService,
    ) {
        const url = this.route.snapshot.url[0].path;
        console.log("Login component incoming URL: " + url);

        // Support for logout
        if (url.includes('logout')) {
            this.userService.logoutUser();
            
//            this.appComponent.update();            
        }
        
        console.log("CurUser: " + this.userService.currentUser.getValue());
        
        // redirect to home if already logged in
        if ( this.userService.currentUser.getValue() ) {
            this.router.navigate( ['/home'] );
        }
    }

    ngOnInit() {
        this.loginForm = this.formBuilder.group( {
            username: ['', Validators.required],
            password: ['', Validators.required]
        } );

        // get return url from route parameters or default to '/'
//        this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
    }


    public onSubmit() {
        console.warn( "onSubmit" );
    }

    public onLogin() {
        this.submitted = true;

        console.log("on login() ");

//        console.log("Should login: " + Object.assign( {}, this.loginForm.value ));

        // stop here if form is invalid
//        if ( this.loginForm.invalid ) {
//            return;
//        }

        this.loading = true;
        
        console.log("Should login: " + JSON.stringify(Object.assign( {}, this.loginForm.value )));

        this.userService
            .loginUser(this.loginForm.get('username').value, 
                       this.loginForm.get('password').value).subscribe(
              (loggedInUser : User) => {
                  
                  console.log("CurUser: " + this.userService.currentUser.getValue());

                  this.router.navigate( ['/home'] );                  
              });
        
//        this.authenticationService.login( this.f.username.value, this.f.password.value )
//            .pipe( first() )
//            .subscribe(
//            data => {
//                this.router.navigate( [this.returnUrl] );
//            },
//            error => {
//                this.alertService.error( error );
//                this.loading = false;
//            } );
    }

}
