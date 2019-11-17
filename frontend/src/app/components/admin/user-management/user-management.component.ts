import { Component, OnInit } from '@angular/core';

import { Router } from '@angular/router';

import { UserService } from '../../../services/admin/user.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-user-management',
  templateUrl: './user-management.component.html',
  styleUrls: ['./user-management.component.css']
})
export class UserManagementComponent implements OnInit {

  constructor(
    private userService: UserService
  ) { }

  ngOnInit() {
    //this.onChangeTable(this.config);
    //call server to get list of user
    this.fetchUsers();
  }

  //add user variable
  aUsername: String;
  aPassword: String;
  addUserMessage;
  addMessageStyle;
  
  dUsername: String;
  deUserMessage;
  deMessageStyle;

  userData: Array<any>;


  addUser():void {
    let result;
    this.userService.addUser(this.aUsername, this.aPassword)
      .subscribe(
        (data) => {
          result = data;
          if(result === "User_Exist"){
            this.addUserMessage = "Username exists, please add another user.";
            this.addMessageStyle = {"color" : "red"};
          }else if(result === "Success"){
            this.addUserMessage = "Add user success!";
            this.addMessageStyle = {"color": "green"};
            this.fetchUsers();
          }
        },
        (err) => console.log(err),
        //() => console.log("observable complete")
      )
  }

  deactivateUser():void{
    let result;
    this.userService.deactivateUser(this.dUsername)
      .subscribe(
        (data) => {
          result = data;
          if(result === "User_Not_Exist"){
            this.deUserMessage = "Username does not exist, please add another user.";
            this.deMessageStyle = {"color" : "red"};
          }else if(result === "Success"){
            this.deUserMessage = "Deactivate user success!";
            this.deMessageStyle = {"color": "green"};
            this.fetchUsers();
          }
        },
        (err) => console.log(err),
        //() => console.log("observable complete")
      )
  }

  private fetchUsers(){
    let result: any;
    this.userService.fetchUsers()
      .subscribe(
        (data) => {
          result = data;
          //console.log(result);
          if(result != undefined){
            this.userData = result.users;
          }
        }
      )
  }


}
