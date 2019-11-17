import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private http: HttpClient) { }

  fetchStudentProfile(url, data, func){
    return this.http.get(url, {
      params: data
    }).pipe(
        map((response: Response) => {
          return response;
        })
      )
  }

  updateStudentProfile(url, data, func){
    return this.http.post(url, data)
      .pipe(
        map((response: Response) => {
          return response;
        })
      )
  }
}
