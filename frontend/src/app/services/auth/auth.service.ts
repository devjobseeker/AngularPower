import { Injectable } from '@angular/core';
import { Http, Headers, RequestOptions, Response } from '@angular/http';
import { catchError, map, tap } from 'rxjs/operators';
import { AppConfig } from '../../app.config';
//import 'rxjs/add/operator/map';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  constructor(private http: Http) { }

  public logIn(username, password){
    let headers = new Headers();
    headers.append("Accept", "application/json");

    var base64Credential: String = btoa(username + ":" + password); //encode a string in base-64
    headers.append("Authorization", "Basic " + base64Credential);

    headers.append("X-Requested-With", "XMLHttpRequest");

    let options = new RequestOptions();
    options.headers = headers;
    let formData = {
      "username": username,
      "password": password
    };
    //let baseUrl = AppConfig.API_BASE_URL;

    return this.http.get("./api/login", options)
      .pipe(
        map((response: Response) => {
          let user = response.json();
          if(user != null){
            sessionStorage.setItem("currentUser", user.username);
            sessionStorage.setItem("currentRole", user.role);
            //console.log(user.role);
            return true;
          }else{
            return false;
          }
        })
      )
  }

  public logOut(){
    return this.http.post("./api/logout", {})
      .pipe(
        map((response: Response) => {
          sessionStorage.clear();
        })
      )
  }
}
