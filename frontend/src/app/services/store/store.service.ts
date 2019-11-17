import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StoreService {

  constructor(private http: HttpClient) { }

  fetchStoreItem(url, data, func){
    return this.http.get(url, {
      params: data
    }).pipe(
        map((response) => {
          return response;
        })
      )
  }

  purcharseItem(url, data, func){
    return this.http.post(url, data)
      .pipe(
        map((response) => {
          return response;
        })
      )
  }

  fetchPurchasedItems(url, data, func){
    return this.http.get(url, {
      params: data
    }).pipe(
      map((response) => {
        return response;
      })
    )
  }

  initStore(url, data, func){
    return this.http.post(url, data)
      .pipe(
        map((response) => {
          return response;
        })
      )
  }
}
