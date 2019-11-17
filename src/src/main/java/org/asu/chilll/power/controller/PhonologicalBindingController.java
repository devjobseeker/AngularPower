package org.asu.chilll.power.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.asu.chilll.power.dataview.DocumentDataView;
import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.StudentIdentity;
import org.asu.chilll.power.dataview.phonologicalbinding.PhonologicalBindingUserData;
import org.asu.chilll.power.entity.feature.SyncDataResult;
import org.asu.chilll.power.enums.FileFolderName;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.enums.SyncDataErrorType;
import org.asu.chilll.power.service.FileService;
import org.asu.chilll.power.service.GameProgressService;
import org.asu.chilll.power.service.PhonologicalBindingService;
import org.asu.chilll.power.service.feature.REDCapSyncDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.Gson;

@Controller
public class PhonologicalBindingController {
	@Autowired
	private GameProgressService progressService;
	
	@Autowired
	private PhonologicalBindingService gameService;
	
	@Autowired
	private FileService fileService;
	
	@Autowired
	private REDCapSyncDataService redcapService;
	
	@RequestMapping(value = "/api/phonological/syncdata", method = RequestMethod.POST)
	public @ResponseBody SyncDataResult initSyncDataRecord(@RequestBody String formData) {
		try {
			StudentIdentity student = new Gson().fromJson(formData, StudentIdentity.class);
			return redcapService.initSyncDataRecords(student.getStudentId(), student.getGrade(), GameIdType.Phonological_Binding.toString());
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
		}
	}
	
	//fetch game progress for recovering
	@RequestMapping(value = "/api/phonological/progress", method = RequestMethod.GET)
	public @ResponseBody GameProgressDataView fetchGameProgress(@RequestParam("studentId") String studentId, @RequestParam("grade") String grade) {
		try {
			GameProgressDataView progress = progressService.fetchGameProgress(studentId, grade, GameIdType.Phonological_Binding.toString());
			return progress;
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	//update game progress when the game enter into a different status
	@RequestMapping(value = "/api/phonological/progress", method = RequestMethod.POST)
	public @ResponseBody String updateGameProgress(@RequestBody String formData) {
		try {
			GameProgressDataView progress = new Gson().fromJson(formData, GameProgressDataView.class);
			progress.setGameId(GameIdType.Phonological_Binding.toString());
			progressService.updateGameProgress(progress, Optional.ofNullable(progress.getRepetitionCount()));
			return "true";
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return "false";
		}
	}
	
	//update data after user press keyboard
	@RequestMapping(value = "/api/phonological", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public @ResponseBody GameProgressDataView sendKeyBoardData(@RequestBody String formData) {
		try {
			PhonologicalBindingUserData userData = new Gson().fromJson(formData, PhonologicalBindingUserData.class);
			return gameService.saveData(userData);
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	//update game progress when game is over
	@RequestMapping(value = "/api/phonological/gameover", method = RequestMethod.POST)
	public @ResponseBody GameProgressDataView gameOver(@RequestBody String formData) {
		try {
			GameProgressDataView progress = new Gson().fromJson(formData, GameProgressDataView.class);
			return progressService.updateGameProgressWhenGameOver(progress.getChildId(), progress.getGrade(), GameIdType.Phonological_Binding.toString(), null);
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	//save audio files into database
	@RequestMapping(value = "/api/phonological/audio", method = RequestMethod.POST)
	public @ResponseBody String sendAudioData(MultipartHttpServletRequest request, @RequestParam("childId") String childId,
			@RequestParam("experimenter") String experimenter, @RequestParam("grade") String grade) {
		//
		try {
			List<DocumentDataView> dvs = new ArrayList<DocumentDataView>();
			Iterator<String> itr = request.getFileNames();
			while(itr.hasNext()) {
				MultipartFile file = request.getFile(itr.next());
				DocumentDataView dv = new DocumentDataView();
				dv.setDocumentId(UUID.randomUUID().toString());
				dv.setChildId(childId);
				dv.setExperimenter(experimenter);
				dv.setGrade(grade);
				dv.setGameId(GameIdType.Phonological_Binding.toString());
				dv.setFileLength(file.getSize());
				dv.setFileContent(file.getBytes());
				dv.setFileType(file.getContentType());
				dv.setFileName(GameIdType.Phonological_Binding.toString() + "_" + file.getOriginalFilename());
				dv.setFolderName(FileFolderName.Phonological_Binding.toString());
				dvs.add(dv);
				
				//save file to local first
				dv = fileService.createFileOnLocal(dv);
				if(dv == null) {
					return "false";
				}
				fileService.createFile(dv);
			}
			
			return "true";
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return "false";
		}
	}
}
