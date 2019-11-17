import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class FileService {

  constructor(private http: HttpClient) { }

  downloadFile(url, data, func){
    return this.http.get(url, {
      params: data
    }).pipe(
      map((response) => {
        return response;
      })
    )
  }
}