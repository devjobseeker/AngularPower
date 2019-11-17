import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class DataUploadService {

  constructor(private http: HttpClient) { }

  get(url){
    return this.http.get(url).pipe(
      map((response) => {
        return response;
      })
    )
  }

  post(url, data){
    return this.http.post(url, data)
      .pipe(
        map((response) => {
          return response;
        })
      )
  }
}