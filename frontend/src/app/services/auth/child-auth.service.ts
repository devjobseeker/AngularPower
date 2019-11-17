import { Injectable } from '@angular/core';
import { CanActivate, Router} from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class ChildAuthService implements CanActivate{

  constructor(
    private router: Router
  ) { }

  canActivate(): boolean{
    let childId = sessionStorage.getItem("childId");
    let experimenter = sessionStorage.getItem("experimenter");

    if(childId != undefined && experimenter != undefined && childId != null){
      return true;
    }else{
      this.router.navigate([""]);
      return false;
    }
  }
}