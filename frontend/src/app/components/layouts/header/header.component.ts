import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AppConfig } from '../../../app.config';
import { AuthService } from '../../../services/auth/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router: Router, private authService: AuthService) { }

  ngOnInit() {
    this.displayLogOut = AppConfig.ENABLE_LOGIN;
    this.userRole = sessionStorage.getItem("currentRole");
  }
  displayLogOut;
  userRole: String;

  logout(){
    this.authService.logOut()
      .subscribe(
        data => {
          this.router.navigate(["/login"]);
        },
      );
  }
}