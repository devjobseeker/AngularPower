package org.asu.chilll.power.controller;

import javax.servlet.http.HttpSession;

import org.asu.chilll.power.dataview.StudentIdentity;
import org.asu.chilll.power.enums.SessionAttributeName;
import org.asu.chilll.power.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

//@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@Controller
public class HomeController {
	
	@Autowired
	private StoreService storeService;
	
	@RequestMapping(value = "/api/childid/save", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String saveChildIdAndExperimeterInSession(@RequestBody String formData, HttpSession session) {
		try{
			StudentIdentity currentChild = new Gson().fromJson(formData, StudentIdentity.class);
			session.setAttribute(SessionAttributeName.Child_Id.toString(), currentChild.getStudentId());
			session.setAttribute(SessionAttributeName.Experimenter.toString(), currentChild.getExperimenter());
			session.setAttribute(SessionAttributeName.Grade.toString(), currentChild.getGrade());
			return "true";
		}catch(Exception e) {
			return "false";
		}
	}
	
	@RequestMapping(value = "/api/home/storeinit", method = RequestMethod.POST)
	public @ResponseBody String initStoreItems() {
		try {
			if(storeService.initStoreItems()) {
				return "true";
			}
			return "false";
		}catch(Exception e) {
			return "false";
		}
	}
}