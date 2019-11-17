import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { StoreService } from '../../services/store/store.service';
import { AppSessionService } from '../../services/session/app-session.service';
import { ErrorService } from '../../services/errors/error.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  constructor(
    private router: Router,
    private sessionService: AppSessionService,
    private errorService: ErrorService,
    private storeService: StoreService,
  ) { }

  ngOnInit() {
    this.initStore();
    if(sessionStorage.getItem("childId") != undefined){
      this.studentName = sessionStorage.getItem("childId");
    }

    if(sessionStorage.getItem("currentUser") != undefined){
      this.experimenter = sessionStorage.getItem("currentUser");
    }

    if(sessionStorage.getItem("grade") != undefined){
      this.grade = sessionStorage.getItem("grade");
    }
  }

  studentName: String;
  experimenter: String;
  grade: String;

  //after enter student id and experimenter name
  login():void {
    if(sessionStorage.length > 0){
      sessionStorage.clear();
    }
    sessionStorage.setItem("childId", this.studentName.toString());
    sessionStorage.setItem("experimenter", this.experimenter.toString());
    sessionStorage.setItem("grade", this.grade.toString());
    //http
    var data = {
      "studentId" : this.studentName,
      "experimenter": this.experimenter,
      "grade": this.grade
    }
    
    let result;
    this.sessionService.saveChildIdAndExperimenter("./api/childid/save", data, () => {})
      .subscribe(
        (data) => {
          result = data;
          if(result == true){
            this.router.navigate(["app/dashboard"]);
          }else{
            this.errorService.internalError();
          }
        },
        (err) => {
          this.errorService.networkError();
        }
      )
  }

  private initStore(){
    this.storeService.initStore("./api/home/storeinit", null, () => {})
      .subscribe(
        (data) => {
          if(data == undefined || !data){
            this.errorService.internalError();
          }
        },
        (err) => {
          this.errorService.networkError();
        }
      )
  }
}