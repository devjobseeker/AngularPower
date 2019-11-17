package org.asu.chilll.power.controller;

import java.security.Principal;

import org.asu.chilll.power.dataview.AppErrorMessage;
import org.asu.chilll.power.dataview.UserDataView;
import org.asu.chilll.power.entity.security.AppUser;
import org.asu.chilll.power.enums.UserType;
import org.asu.chilll.power.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {
	
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@RequestMapping(value = "/api/login", method = RequestMethod.GET)
	public UserDataView login(Principal principal) {
		//logger.info("User logged " + principal.getName());
		UserDataView dv = new UserDataView();
		dv.setUsername(principal.getName());
		AppUser user = userService.findByUsername(dv.getUsername());
		dv.setRole(user.getRole());
		return dv;
	}
	
	@RequestMapping(value = "/api/register", method = RequestMethod.POST)
	public ResponseEntity<?> register(@RequestBody AppUser newUser){
		if(userService.findByUsername(newUser.getUsername()) != null) {
			logger.error("Username already exist " + newUser.getUsername());
			
			return new ResponseEntity<AppErrorMessage>(
					new AppErrorMessage("Username already exist."),
					HttpStatus.CONFLICT
			);
		}
		newUser.setRole(UserType.USER.toString());
		newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
		newUser.setEnabled(true);
		return new ResponseEntity<AppUser>(userService.save(newUser), HttpStatus.CREATED);
	}
}
