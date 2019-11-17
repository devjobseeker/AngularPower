package org.asu.chilll.power.controller.feature;

import java.util.List;

import org.asu.chilll.power.dataview.redcap.SyncRecordDataView;
import org.asu.chilll.power.service.feature.REDCapSyncDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class REDCapController {
	
	@Autowired
	private REDCapSyncDataService redcapService;
	
	@RequestMapping(value = "/api/redcap/testconnection", method = RequestMethod.POST)
	public @ResponseBody SyncRecordDataView ping(@RequestBody String formData) {
		try{
			SyncRecordDataView dv = new Gson().fromJson(formData, SyncRecordDataView.class);
			return redcapService.ping(dv);
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@RequestMapping(value = "/api/redcap/total", method = RequestMethod.GET)
	public @ResponseBody List<SyncRecordDataView> fetchRecordsToSync(){
		try {
			return redcapService.fetchPendingSyncDataRecordsCount();
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return null;
		}
	}
	
	@RequestMapping(value = "/api/redcap/processing", method = RequestMethod.POST)
	public synchronized @ResponseBody SyncRecordDataView processData(@RequestBody String formData) {
		try {
			SyncRecordDataView dv = new Gson().fromJson(formData, SyncRecordDataView.class);
			return redcapService.processData(dv);
		}catch(Exception e) {
			System.out.println("========================================== POWER PROGRAM ERROR ==========================================");
			e.printStackTrace(System.out);
			return null;
		}
	}
}