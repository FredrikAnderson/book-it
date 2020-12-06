import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { map, tap, retry, catchError } from 'rxjs/operators';
import JSOG from 'jsog';
import { Item } from '../shared/item';
import { Utils } from '../shared/utils';

@Injectable( {
    providedIn: 'root'
} )
export class ItemsServiceService {

    apiURL = 'http://localhost:8888/api';

    constructor(private http: HttpClient) {
        this.apiURL = Utils.getApiUrl();
    }

    // Http Options
    httpOptions = {
        headers: new HttpHeaders( {
            'Content-Type': 'application/json'
        } )
    };

    saveItem( item: Item ): Observable<any> {

        // 		console.log( "should save item: ", item);

        if ( item.id  === undefined || item.id === '' ) {
            // Post, new
            return this.http.post<Item>( this.apiURL + '/items/', item )
                .pipe(
                tap( data => console.log( 'posted item: ', data ) ),
                retry( 1 ),
                catchError( this.handleError )
                );
        }

// else {
//            // Put, update
//            return this.http.put<User>( this.apiURL + "/users/" + user.userid, user )
//                .pipe(
//                tap( data => console.log( "updated user: ", data ) ),
//                retry( 1 ),
//                catchError( this.handleError )
//                );
//        }

    }

    // HttpClient API get() method => Get Items list
    getItems(name: string = null): Observable< Item[] > {
        let requestUrl = this.apiURL + '/items';
        if (name != null) {
            requestUrl = this.apiURL + '/items&name=' + name;
        }

        return this.http.get<any>( requestUrl )
            .pipe(
                    map( response => JSOG.decode(response.items) ),
                    tap(data => console.log('Got items: ', data)),
                    retry( 1 ),
                    catchError( this.handleError )
            );
    }

    getItem( id ): Observable<Item> {
        return this.http.get<Item>( this.apiURL + '/items/' + id )
            .pipe(
            tap(data => console.log('Got item: ', data)),
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