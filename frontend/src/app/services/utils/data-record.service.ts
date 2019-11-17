import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DataRecordService {

  constructor(private http: HttpClient) { }

  sendUserData(url, data, func){
    return this.http.post(url, data)
      .pipe(
        map((response: Response) => {
          return response;
        })
      );
  }

  // sendUserData1(url, data, func): Observable<Response>{
  //   return this.http.post(url, data)
  //     .pipe(
  //       map((response: Response) => {
  //         return response;
  //       })
  //     );
  // }
}