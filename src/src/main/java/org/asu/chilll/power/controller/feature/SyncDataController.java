package org.asu.chilll.power.controller.feature;

import java.util.List;

import org.asu.chilll.power.dataview.DataToSyncCount;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.service.feature.SyncDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

//@Controller
public class SyncDataController {
	
//	@Autowired
//	private SyncDataService dataService;
	
//	@RequestMapping(value = "/api/upload/testconnection", method = RequestMethod.GET)
//	public @ResponseBody boolean ping() {
//		try{
//			return dataService.ping();
//		}catch(Exception e) {
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/total", method = RequestMethod.GET)
//	public @ResponseBody List<DataToSyncCount> fetchDataToSyncCount(){
//		try {
//			return dataService.fetchDataToSyncCount();
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return null;
//		}
//	}
//	
//	//game progress
//	@RequestMapping(value = "/api/upload/gameprogress", method = RequestMethod.POST)
//	public @ResponseBody boolean syncGameProgress() {
//		try {
//			return dataService.syncGameProgress(GameIdType.Game_Progress.toString());
//		}catch(Exception e) {
//			return false;
//		}
//	}
//	
//	//student profile
//	@RequestMapping(value = "/api/upload/studentprofile", method = RequestMethod.POST)
//	public @ResponseBody boolean syncStudentProfile() {
//		try {
//			return dataService.syncStudentProfile(GameIdType.Student_Profile.toString());
//		}catch(Exception e) {
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/crossmodal", method = RequestMethod.POST)
//	public @ResponseBody boolean syncCrossModal() {
//		try {
//			return dataService.syncData(GameIdType.Cross_Modal_Binding.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/digitspan", method = RequestMethod.POST)
//	public @ResponseBody boolean syncDigitSpan() {
//		try {
//			return dataService.syncData(GameIdType.Digit_Span.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/digitspanrunning", method = RequestMethod.POST)
//	public @ResponseBody boolean syncDigitSpanRunning() {
//		try {
//			return dataService.syncData(GameIdType.Digit_Span_Running.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/locationspan", method = RequestMethod.POST)
//	public @ResponseBody boolean syncLocationSpan() {
//		try {
//			return dataService.syncData(GameIdType.Location_Span.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/locationspanrunning", method = RequestMethod.POST)
//	public @ResponseBody boolean syncLocationSpanRunning() {
//		try {
//			return dataService.syncData(GameIdType.Location_Span_Running.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/numberupdatevisual", method = RequestMethod.POST)
//	public @ResponseBody boolean syncNumberUpdateVisual() {
//		try {
//			return dataService.syncData(GameIdType.Number_Update_Visual.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/phonological", method = RequestMethod.POST)
//	public @ResponseBody boolean syncPhonological() {
//		try {
//			return dataService.syncData(GameIdType.Phonological_Binding.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/visualbinding", method = RequestMethod.POST)
//	public @ResponseBody boolean syncVisualBinding() {
//		try {
//			return dataService.syncData(GameIdType.Visual_Binding_Span.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/visualspan", method = RequestMethod.POST)
//	public @ResponseBody boolean syncVisualSpan() {
//		try {
//			return dataService.syncData(GameIdType.Visual_Span.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/visualspanrunning", method = RequestMethod.POST)
//	public @ResponseBody boolean syncVisualSpanRunning() {
//		try {
//			return dataService.syncData(GameIdType.Visual_Span_Running.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/nonword", method = RequestMethod.POST)
//	public @ResponseBody boolean syncNonWord() {
//		try {
//			return dataService.syncData(GameIdType.Nonword_Repetition.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/repetitionauditory", method = RequestMethod.POST)
//	public @ResponseBody boolean syncRepetitionAuditory() {
//		try {
//			return dataService.syncData(GameIdType.Repetition_Detection_Auditory.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
//	
//	@RequestMapping(value = "/api/upload/repetitionvisual", method = RequestMethod.POST)
//	public @ResponseBody boolean syncRepetitionVisual() {
//		try {
//			return dataService.syncData(GameIdType.Repetition_Detection_Visual.toString());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return false;
//		}
//	}
}