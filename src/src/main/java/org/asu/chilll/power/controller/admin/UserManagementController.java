package org.asu.chilll.power.controller.admin;

import javax.servlet.http.HttpSession;

import org.asu.chilll.power.dataview.UserDataView;
import org.asu.chilll.power.dataview.search.PageResult;
import org.asu.chilll.power.dataview.search.SearchCriteria;
import org.asu.chilll.power.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class UserManagementController {

	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/api/admin/user/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String addUser(@RequestBody String formData, HttpSession session) {
		UserDataView user = new Gson().fromJson(formData, UserDataView.class);
		UserDataView result = userService.addUser(user);
		//first check user exist or not
		//if not, then add user. if exist, return user exist message
		return result.getErrorMsg().getErrorMsg();
	}
	
	@RequestMapping(value = "/api/admin/user/deactivate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String deactivateUser(@RequestBody String formData, HttpSession session) {
		UserDataView user = new Gson().fromJson(formData, UserDataView.class);
		UserDataView result = userService.deactivateUser(user.getUsername());
		return result.getErrorMsg().getErrorMsg();
	}
	
	@RequestMapping(value = "/api/admin/users", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody PageResult getUsers(@RequestBody String formData, HttpSession session){
		SearchCriteria criteria = new Gson().fromJson(formData, SearchCriteria.class);
		return userService.findUsers(criteria);
	}
}