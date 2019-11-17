import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(private http: HttpClient) { }

  addUser(username, password){
    let formData = { "username": username, "password": password };

    return this.http.post("./api/admin/user/add", formData, {responseType: "text"})
      .pipe(
        map(response => response)
      );
  }

  deactivateUser(username){
    let formData = {"username": username};
    return this.http.post("./api/admin/user/deactivate", formData, {responseType: "text"})
      .pipe(
        map(response => response)
      );
  }

  fetchUsers(pageIndex?, pageSize?, sort?, filter?){
    let formData = {
      "pageIndex": pageIndex,
      "pageSize": pageSize,
      "sort": sort,
      "filter": filter
    };

    return this.http.post("./api/admin/users", formData, {responseType: "json"})
      .pipe(
        map((response: Response) => {
          //console.log(response);
          return response;
          //return response;
        })
      );
  }
}