package org.asu.chilll.power.controller;

import java.util.Optional;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.StudentIdentity;
import org.asu.chilll.power.dataview.repetitiondetection.RepetitionDetectionUserData;
import org.asu.chilll.power.entity.feature.SyncDataResult;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.enums.SyncDataErrorType;
import org.asu.chilll.power.service.GameProgressService;
import org.asu.chilll.power.service.RepetitionAuditoryService;
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
public class RepetitionAuditoryController {
	@Autowired
	private GameProgressService progressService;
	
	@Autowired
	private RepetitionAuditoryService gameService;
	
	@Autowired
	private REDCapSyncDataService redcapService;
	
	@RequestMapping(value = "/api/repetitionauditory/syncdata", method = RequestMethod.POST)
	public @ResponseBody SyncDataResult initSyncDataRecord(@RequestBody String formData) {
		try {
			StudentIdentity student = new Gson().fromJson(formData, StudentIdentity.class);
			return redcapService.initSyncDataRecords(student.getStudentId(), student.getGrade(), GameIdType.Repetition_Detection_Auditory.toString());
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
		}
	}
	
	//fetch game progress for recovering
	@RequestMapping(value = "/api/repetitionauditory/progress", method = RequestMethod.GET)
	public @ResponseBody GameProgressDataView fetchGameProgress(@RequestParam("studentId") String studentId, @RequestParam("grade") String grade) {
		try {
			GameProgressDataView progress = progressService.fetchGameProgress(studentId, grade, GameIdType.Repetition_Detection_Auditory.toString());
			return progress;
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	//update game progress when the game enter into a different status
	@RequestMapping(value = "/api/repetitionauditory/progress", method = RequestMethod.POST)
	public @ResponseBody String updateGameProgress(@RequestBody String formData) {
		try {
			GameProgressDataView progress = new Gson().fromJson(formData, GameProgressDataView.class);
			progress.setGameId(GameIdType.Repetition_Detection_Auditory.toString());
			progressService.updateGameProgress(progress, Optional.ofNullable(progress.getRepetitionCount()));
			return "true";
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return "false";
		}
	}
	
	//update data after user press keyboard
	@RequestMapping(value = "/api/repetitionauditory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody GameProgressDataView sendKeyBoardData(@RequestBody String formData) {
		try {
			RepetitionDetectionUserData userData = new Gson().fromJson(formData, RepetitionDetectionUserData.class);
			return gameService.saveData(userData);
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	//update game progress when game is over
	@RequestMapping(value = "/api/repetitionauditory/gameover", method = RequestMethod.POST)
	public @ResponseBody GameProgressDataView gameOver(@RequestBody String formData) {
		try {
			GameProgressDataView progress = new Gson().fromJson(formData, GameProgressDataView.class);
			return progressService.updateGameProgressWhenGameOver(progress.getChildId(), progress.getGrade(), GameIdType.Repetition_Detection_Auditory.toString(), null);
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return null;
		}
	}
}