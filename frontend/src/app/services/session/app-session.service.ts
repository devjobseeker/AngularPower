import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AppSessionService{

  constructor(private http: HttpClient) { }

  saveChildIdAndExperimenter(url, data, func){
    return this.http.post(url, data)
      .pipe(
        map((response: Response) => {
          return response;
        })
      )
  }
}