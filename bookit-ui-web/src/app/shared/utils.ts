//import {Injectable} from '@angular/core';
//
//@Injectable()

export class Utils {


	public static getApiUrl() {	
		let port = window.location.port;
		if (port == "4200") {
			port = "8888";
		}
		
		let apiUrl = window.location.protocol + "//" + window.location.hostname + ":" + port + "/api";
		
		console.log("apiUrl is: " + apiUrl);
		return apiUrl;
	}

}