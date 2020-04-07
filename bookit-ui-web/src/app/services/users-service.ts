import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { tap, map, retry, catchError } from 'rxjs/operators';
import { User } from '../shared/user';

@Injectable( {
    providedIn: 'root'
} )
export class UsersServiceService {

    apiURL = "http://localhost:8888/api";

    public currentUser = new BehaviorSubject<User>(null);
//    public currentUser = this.userSubject.asObservable();
    
    //Http Options
    httpOptions = {
        headers: new HttpHeaders( {
            'Content-Type': 'application/json'
        } )
    }

    constructor( private http: HttpClient ) {
        this.apiURL = window.location.protocol + "//" + window.location.hostname + ":8888/api"; 

        this.setCurrentUser(JSON.parse(window.localStorage.getItem('currentUser')));  
        
        this.debugData();        
    }

    setCurrentUser(user : User) {
//        console.log("User is: " + user + " name: " + user.name + " id: " + user.userid);
        
        this.currentUser.next(user);

        if (user == null) {
            window.localStorage.removeItem('currentUser');
        } else {
            window.localStorage.setItem('currentUser', JSON.stringify(this.currentUser.getValue()));  
        }
        this.debugData();
    }
    
    logoutUser() {
        this.setCurrentUser(null);        
    }
    
    loginUser( userid: string, password: string ): Observable<User> {

        return this.http.get<User>( this.apiURL + "/users/" + userid + "/login/" + password )
            .pipe(
            tap(( loggedInUser: User ) => {
                console.log( "logged in: ", loggedInUser );
                this.setCurrentUser(loggedInUser);
            } ),
            retry( 1 ),
            catchError( this.handleError )
            );
    }

    saveUser( user: User ): Observable<any> {

        if ( user.userid === undefined || user.userid === "" ) {
            // Post, new
            return this.http.post<User>( this.apiURL + "/users/", user )
                .pipe(
                tap( data => console.log( "posted user: ", data ) ),
                retry( 1 ),
                catchError( this.handleError )
                );
        } else {
            // Put, update
            return this.http.put<User>( this.apiURL + "/users/" + user.userid, user )
                .pipe(
                tap( data => console.log( "updated user: ", data ) ),
                retry( 1 ),
                catchError( this.handleError )
                );
        }
    }

    // HttpClient API get() method => Get users list
    getUsers(): Observable<User> {
        return this.http.get<User>( this.apiURL + "/users" )
            .pipe(
            tap( data => console.log( "getusers: ", data ) ),
            retry( 1 ),
            catchError( this.handleError )
            );
    }

    getUser( id ): Observable<User> {
        return this.http.get<User>( this.apiURL + "/users/" + id )
            .pipe(
            tap( data => console.log( "get user: ", data ) ),
            retry( 1 ),
            catchError( this.handleError )
            );
    }

    deleteUser( id ): Observable<any> {
        return this.http.delete<any>( this.apiURL + "/users/" + id )
            .pipe(
            tap( data => console.log( "deleted user: " + id ) ),
            retry( 1 ),
            catchError( this.handleError )
            );
    }

    debugData() {
        
        console.log("Current user: " + JSON.stringify(this.currentUser.getValue()));
    }
    
    //Error handling 
    handleError( error ) {
        let errorMessage = '';
        if ( error.error instanceof ErrorEvent ) {
            // Get client-side error
            errorMessage = error.error.message;
        } else {
            // Get server-side error
            errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
        }
        window.alert( errorMessage );
        return throwError( errorMessage );
    }

}