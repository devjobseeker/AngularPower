package org.asu.chilll.power.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AngularRouteController {
	
	@RequestMapping(value = {"/app/**", "/login", "/admin/**"})
	public String forward() {
		return "forward:/";
	}
}
