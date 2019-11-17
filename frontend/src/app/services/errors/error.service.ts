import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ErrorService {

  constructor() { }

  internalError(){
    alert("Internal Error. Please try to log in first.");
  }

  networkError(){
    alert("Please check your internet connection or start your local server, then try it again.");
  }
}
