package org.asu.chilll.power.controller;

import java.util.Calendar;
import java.util.List;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.StudentIdentity;
import org.asu.chilll.power.dataview.StudentProfileDataView;
import org.asu.chilll.power.entity.feature.SyncDataResult;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.enums.SyncDataErrorType;
import org.asu.chilll.power.service.GameProgressService;
import org.asu.chilll.power.service.StudentProfileService;
import org.asu.chilll.power.service.feature.REDCapSyncDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class DashboardController {
	@Autowired
	private StudentProfileService profileService;
	@Autowired
	private GameProgressService progressService;
	@Autowired
	private REDCapSyncDataService redcapService;
	
	@RequestMapping(value = "/api/dashboard/syncdata", method = RequestMethod.POST)
	public @ResponseBody SyncDataResult initSyncDataRecord(@RequestBody String formData) {
		try {
			StudentIdentity student = new Gson().fromJson(formData, StudentIdentity.class);
			return redcapService.initProfileSyncDataRecord(student.getStudentId(), student.getGrade(), GameIdType.AllGames.toString());
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
		}
	}
	
	@RequestMapping(value = "/api/dashboard/fetch", method = RequestMethod.GET)
	public @ResponseBody StudentProfileDataView fetchStudentProfile(@RequestParam("studentId") String studentId, @RequestParam("grade") String grade) {
		if(studentId != null) {
			StudentProfileDataView dv = profileService.fetchStudentProfile(studentId, grade);
			return dv;
		}else {
			return null;
		}
	}
	
	@RequestMapping(value = "/api/dashboard/intro1", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String updateIntro1(@RequestBody String formData) {
		try{
			StudentProfileDataView profile = new Gson().fromJson(formData, StudentProfileDataView.class);
			profileService.updateIntro1(profile);
			return "true";
		}catch(Exception e) {
			return "false";
		}
	}
	
	@RequestMapping(value = "/api/dashboard/intro2", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String updateIntro2(@RequestBody String formData) {
		try{
			StudentProfileDataView profile = new Gson().fromJson(formData, StudentProfileDataView.class);
			profileService.updateIntro2(profile);
			return "true";
		}catch(Exception e) {
			return "false";
		}
	}
	
	@RequestMapping(value = "/api/dashboard/day", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody String updateDayComplete(@RequestBody String formData) {
		try {
			StudentProfileDataView profile = new Gson().fromJson(formData, StudentProfileDataView.class);
			int year = Calendar.getInstance().get(Calendar.YEAR);
			profile.setYear(year);
			profileService.updateDayComplete(profile);
			return "true";
		}catch(Exception e) {
			return "false";
		}
	}
	
	@RequestMapping(value = "/api/dashboard/gamelist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody List<GameProgressDataView> fetchGamesProgress(@RequestParam("studentId") String studentId, @RequestParam("grade") String grade,
			@RequestBody List<String> gameIds){
		try {
			List<GameProgressDataView> result = progressService.fetchListGamesProgress(studentId, grade, gameIds);
			return result;
		}catch(Exception e) {
			return null;
		}
	}
}