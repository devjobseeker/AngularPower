import { Component, OnInit } from '@angular/core';
import { FormGroup} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(private router: Router, private authService: AuthService) { }

  ngOnInit() {
  }

  username: String;
  password: String;
  errorMessage: String;

  login():void {
    let result;
    this.authService.logIn(this.username, this.password)
      .subscribe(
        (data) => {
          result = data;
          if(result == true){
            this.router.navigate(["/"]);
          }else{
            this.errorMessage = "Please check you internet. If your internet is fine, there might be a internal error. Please try again later.";
          }
          
        },
        err => {
          this.errorMessage = "Username or Password is not correct";
        }
      )
  }
}
