import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, tap, retry, catchError } from 'rxjs/operators';
import JSOG from 'jsog';
import { Item } from '../shared/item';

@Injectable( {
    providedIn: 'root'
} )
export class ItemsServiceService {

    apiURL = "http://localhost:8888/api";

    constructor( private http: HttpClient ) {
        this.apiURL = window.location.protocol + "//" + window.location.hostname + ":8888/api";
    }

    //Http Options
    httpOptions = {
        headers: new HttpHeaders( {
            'Content-Type': 'application/json'
        } )
    }

    // HttpClient API get() method => Get Items list
    getItems(name : string = null): Observable< Item[] > {
        let requestUrl = this.apiURL + "/items";
        if (name != null) {
            requestUrl = this.apiURL + "/items&name=" + name;
        }
        
        return this.http.get<any>( requestUrl )
            .pipe(
                    map( response => JSOG.decode(response.items) ),
                    tap(data => console.log("Got items: ", data)),
                    retry( 1 ),
                    catchError( this.handleError )
            );
    }

    getItem( id ): Observable<Item> {
        return this.http.get<Item>( this.apiURL + "/items/" + id )
            .pipe(
            retry( 1 ),
            catchError( this.handleError )
            );
    }

    deleteItem( id ): Observable<any> {
        return this.http.delete<any>( this.apiURL + "/items/" + id )
            .pipe(
            retry( 1 ),
            catchError( this.handleError )
            );
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