package org.asu.chilll.power.service.feature;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.asu.chilll.power.dataview.ResponseDataView;
import org.asu.chilll.power.dataview.redcap.SyncRecordDataView;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.StudentProfile;
import org.asu.chilll.power.entity.data.DetailCrossModalBinding;
import org.asu.chilll.power.entity.data.DetailDigitSpan;
import org.asu.chilll.power.entity.data.DetailDigitSpanRunning;
import org.asu.chilll.power.entity.data.DetailLocationSpan;
import org.asu.chilll.power.entity.data.DetailLocationSpanRunning;
import org.asu.chilll.power.entity.data.DetailNonWord;
import org.asu.chilll.power.entity.data.DetailNumberUpdateAuditory;
import org.asu.chilll.power.entity.data.DetailNumberUpdateVisual;
import org.asu.chilll.power.entity.data.DetailPhonologicalBinding;
import org.asu.chilll.power.entity.data.DetailRepetitionAuditory;
import org.asu.chilll.power.entity.data.DetailRepetitionVisual;
import org.asu.chilll.power.entity.data.DetailVisualBindingSpan;
import org.asu.chilll.power.entity.data.DetailVisualSpan;
import org.asu.chilll.power.entity.data.DetailVisualSpanRunning;
import org.asu.chilll.power.entity.data.SummaryDigitSpan;
import org.asu.chilll.power.entity.feature.SyncDataRecord;
import org.asu.chilll.power.entity.feature.SyncDataResult;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.enums.SyncDataErrorType;
import org.asu.chilll.power.enums.SyncDataRecordType;
import org.asu.chilll.power.enums.SyncDataStatusType;
import org.asu.chilll.power.repository.GameRepository;
import org.asu.chilll.power.repository.feature.SyncDataREDCapRepository;
import org.asu.chilll.power.vendor.REDCapVendor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class REDCapSyncDataService {
	
	@Autowired
	private SyncDataREDCapRepository syncRepo;
	@Autowired
	private GameRepository gameRepo;
	
	private final static String dateFormat = "yyyy-MM-dd HH:mm:ss";
	
	@SuppressWarnings("unchecked")
	public SyncRecordDataView ping(SyncRecordDataView dv) {
		REDCapVendor vendor = new REDCapVendor();
		JSONArray jsonArray = new JSONArray();
		JSONObject record = new JSONObject();
		record.put("record", UUID.randomUUID().toString());
		record.put("redcap_event_name", "dummy_event_arm_5");
		record.put("field_name", "dummy_create_date");
		DateFormat dateF = new SimpleDateFormat(dateFormat);
		record.put("value", dateF.format(new Date()));
		jsonArray.add(record);
		ResponseDataView response = vendor.ping(dv.getApiConfig(), jsonArray);
		if(response != null && response.getHttpCode() == 200 && !response.getHasError()) {
			dv.setSyncDataResult(new SyncDataResult(response.getCount()));
			dv.getSyncDataResult().setRespCode(response.getHttpCode());
			return dv;
		}else if(response != null){
			dv.setSyncDataResult(new SyncDataResult(SyncDataErrorType.REDCap_Error.toString(), response.getResponseMsg()));
			dv.getSyncDataResult().setRespCode(response.getHttpCode());
			return dv;
		}
		dv.setSyncDataResult(new SyncDataResult(SyncDataErrorType.REDCap_Error.toString(), "REDCap Response: null"));
		return dv;
	}
	
	//find records, if no records, create; else update records with status pending
	public SyncDataResult initSyncDataRecords(String childId, String grade, String gameId){
		//fetch list of records by gameId, grade, childId
		List<SyncDataRecord> syncRecords = syncRepo.fetchSyncDataRecordList(childId, grade, gameId);
		if(syncRecords == null || syncRecords.size() == 0) {
			//create new records
			syncRecords = new ArrayList<SyncDataRecord>();
			SyncDataRecord detail = new SyncDataRecord(childId, grade, gameId, SyncDataRecordType.Detail.toString(), UUID.randomUUID().toString());
			detail.setRedcapSyncStatus(SyncDataStatusType.Pending.toString());
			syncRecords.add(detail);
			SyncDataRecord summary = new SyncDataRecord(childId, grade, gameId, SyncDataRecordType.Summary.toString(), UUID.randomUUID().toString());
			summary.setRedcapSyncStatus(SyncDataStatusType.Pending.toString());
			syncRecords.add(summary);
			SyncDataRecord progress = new SyncDataRecord(childId, grade, gameId, SyncDataRecordType.GameProgress.toString(), UUID.randomUUID().toString());
			progress.setRedcapSyncStatus(SyncDataStatusType.Pending.toString());
			syncRecords.add(progress);
			SyncDataResult createResult = syncRepo.createSyncDataRecordList(syncRecords);
			return createResult;
		}else {
			//update records (redcap status not pending) redcap status to pending
			List<SyncDataRecord> updateRecords = new ArrayList<SyncDataRecord>();
			for(SyncDataRecord r: syncRecords) {
				if(r.getRedcapSyncStatus() == null || !r.getRedcapSyncStatus().equals(SyncDataStatusType.Pending.toString())) {
					updateRecords.add(r);
				}
			}
			return syncRepo.updateSyncDataRecordStatusList(updateRecords);
		}
	}
	
	public SyncDataResult initProfileSyncDataRecord(String childId, String grade, String gameId) {
		SyncDataRecord syncRecord = syncRepo.fetchSyncDataRecordByPK(childId, grade, gameId, SyncDataRecordType.StudentProfile.toString());
		if(syncRecord == null) {
			//create new record
			syncRecord = new SyncDataRecord(childId, grade, gameId, SyncDataRecordType.StudentProfile.toString(), UUID.randomUUID().toString());
			syncRecord.setRedcapSyncStatus(SyncDataStatusType.Pending.toString());
			return syncRepo.createSyncDataRecord(syncRecord);
		}else {
			if(syncRecord.getRedcapSyncStatus() == null || ! syncRecord.getRedcapSyncStatus().equals(SyncDataStatusType.Pending.toString())) {
				syncRecord.setRedcapSyncStatus(SyncDataStatusType.Pending.toString());
				syncRepo.updateSyncDataRecord(syncRecord);
				return new SyncDataResult(1L);
			}
			return new SyncDataResult(0L);
		}
	}
	
	public List<SyncRecordDataView> fetchPendingSyncDataRecordsCount(){
		List<SyncRecordDataView> result = new ArrayList<SyncRecordDataView>();
		List<SyncDataRecord> records = syncRepo.fetchSyncDataRecordListByREDCapStatus(SyncDataStatusType.Pending.toString());
		for(SyncDataRecord r: records) {
			SyncRecordDataView dv = new SyncRecordDataView(r.getChildId(), r.getGrade(), r.getGameId(), r.getCategory(), r.getRedcapSyncStatus());
			dv.setCohort("1");	//by default
			//fetch total count for each table by timestamp
			Long count = null;
			if(r.getCategory().equals(SyncDataRecordType.GameProgress.toString())
					|| r.getCategory().equals(SyncDataRecordType.StudentProfile.toString())
					|| r.getCategory().equals(SyncDataRecordType.Summary.toString())) {
				//count = 1L;
			}else if(r.getCategory().equals(SyncDataRecordType.Detail.toString())){
				//fetch detail game total count
				if(r.getGameId().equals(GameIdType.Cross_Modal_Binding.toString())) {
					count = syncRepo.fetchPendingDetailCrossModalBindingRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Digit_Span.toString())) {
					count = syncRepo.fetchPendingDetailDigitSpanRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Digit_Span_Running.toString())) {
					count = syncRepo.fetchPendingDetailDigitSpanRunningRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Location_Span.toString())) {
					count = syncRepo.fetchPendingDetailLocationSpanRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Location_Span_Running.toString())) {
					count = syncRepo.fetchPendingDetailLocationSpanRunningRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Nonword_Repetition.toString())) {
					count = syncRepo.fetchPendingDetailNonWordRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Number_Update_Auditory.toString())) {
					count = syncRepo.fetchPendingDetailNumberUpdateAuditoryRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Number_Update_Visual.toString())) {
					count = syncRepo.fetchPendingDetailNumberUpdateVisualRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Phonological_Binding.toString())) {
					count = syncRepo.fetchPendingDetailPhonologicalBindingRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Repetition_Detection_Auditory.toString())) {
					count = syncRepo.fetchPendingDetailRepetitionAuditoryRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Repetition_Detection_Visual.toString())) {
					count = syncRepo.fetchPendingDetailRepetitionVisualRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Visual_Binding_Span.toString())) {
					count = syncRepo.fetchPendingDetailVisualBindingRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Visual_Span.toString())) {
					count = syncRepo.fetchPendingDetailVisualSpanRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}else if(r.getGameId().equals(GameIdType.Visual_Span_Running.toString())) {
					count = syncRepo.fetchPendingDetailVisualSpanRunningRecordTotalCount(r.getChildId(), r.getGrade(), r.getRedcapTimestamp());
				}
				dv.setTotalRecordsCount(count);
				result.add(dv);
			}
		}
		return result;
	}
	
	public SyncRecordDataView processData(SyncRecordDataView dv) {
		SyncDataRecord record = syncRepo.fetchSyncDataRecordByPK(dv.getChildId(), dv.getGrade(), dv.getGameId(), dv.getCategory());
		if(record == null) {
			dv.setSyncStatus(SyncDataStatusType.Pending.toString());
			return dv;
		}
		JSONArray jsonArray = new JSONArray();
		String deviceId = dv.getApiConfig() != null ? dv.getApiConfig().getDeviceId() : null;
		if(dv.getCategory().equals(SyncDataRecordType.Detail.toString())) {
			if(dv.getGameId().equals(GameIdType.Cross_Modal_Binding.toString())){
				List<DetailCrossModalBinding> details = syncRepo.fetchDetailCrossModalBindingListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailCrossModalBindingJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Digit_Span.toString())) {
				List<DetailDigitSpan> details = syncRepo.fetchDetailDigitSpanListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailDigitSpanJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Digit_Span_Running.toString())) {
				List<DetailDigitSpanRunning> details = syncRepo.fetchDetailDigitSpanRunningListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailDigitSpanRunningJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Location_Span.toString())) {
				List<DetailLocationSpan> details = syncRepo.fetchDetailLocationSpanListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailLocationSpanJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Location_Span_Running.toString())) {
				List<DetailLocationSpanRunning> details = syncRepo.fetchDetailLocationSpanRunningListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailLocationSpanRunningJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Visual_Span.toString())) {
				List<DetailVisualSpan> details = syncRepo.fetchDetailVisualSpanListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailVisualSpanJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Visual_Span_Running.toString())) {
				List<DetailVisualSpanRunning> details = syncRepo.fetchDetailVisualSpanRunningListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailVisualSpanRunningJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Visual_Binding_Span.toString())) {
				List<DetailVisualBindingSpan> details = syncRepo.fetchDetailVisualBindingListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailVisualBindingJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Nonword_Repetition.toString())) {
				List<DetailNonWord> details = syncRepo.fetchDetailNonWordListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailNonWordJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Phonological_Binding.toString())) {
				List<DetailPhonologicalBinding> details = syncRepo.fetchDetailPhonologicalBindingListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailPhonologicalBindingJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Number_Update_Auditory.toString())) {
				List<DetailNumberUpdateAuditory> details = syncRepo.fetchDetailNumberUpdateAuditoryListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailNumberUpdateAuditoryJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Number_Update_Visual.toString())) {
				List<DetailNumberUpdateVisual> details = syncRepo.fetchDetailNumberUpdateVisualListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailNumberUpdateVisualJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Repetition_Detection_Auditory.toString())) {
				List<DetailRepetitionAuditory> details = syncRepo.fetchDetailRepetitionAuditoryListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailRepetitionAuditoryJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}else if(dv.getGameId().equals(GameIdType.Repetition_Detection_Visual.toString())) {
				List<DetailRepetitionVisual> details = syncRepo.fetchDetailRepetitionVisualListByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
				jsonArray = generateDetailRepetionVisualJson(details, deviceId, dv);
				if(details != null && details.size() > 0) {
					record.setRedcapTimestamp(details.get(details.size() - 1).getCreateTime());
				}
			}
		}else if(dv.getCategory().equals(SyncDataRecordType.Summary.toString())) {
//			if(dv.getGameId().equals(GameIdType.Digit_Span.toString())) {
//				SummaryDigitSpan summary = syncRepo.fecthDigitSpanSummaryData(dv.getChildId() + "_" + dv.getGrade());
//				if(summary != null) {
//					//jsonArray = generateSummaryDigitSpan(summary, deviceId);
//					record.setRedcapTimestamp(summary.getLastUpdateTime());
//				}
//			}
		}else if(dv.getCategory().equals(SyncDataRecordType.StudentProfile.toString())) {
//			List<StudentProfile> profiles = syncRepo.fetchStudentProfileByTimestamp(dv.getChildId(), dv.getGrade(), record.getRedcapTimestamp());
//			//jsonArray = generateStudentProfileJson(profiles, deviceId);
//			if(profiles != null && profiles.size() > 0) {
//				record.setRedcapTimestamp(profiles.get(profiles.size() - 1).getLastUpdateTime());
//			}
		}else if(dv.getCategory().equals(SyncDataRecordType.GameProgress.toString())) {
//			GameProgress p = gameRepo.fetchGameProgress(dv.getChildId(), dv.getGrade(), dv.getGameId());
//			//jsonArray = generateGameProgressJson(p, deviceId);
//			record.setRedcapTimestamp(p.getLastUpdateTime());
		}
		
		if(jsonArray == null || jsonArray.isEmpty()) {
			dv.setSyncDataResult(new SyncDataResult());
			dv.setSyncStatus(SyncDataStatusType.Finished.toString());
			dv.setSyncDataResult(new SyncDataResult(0L));
			dv.getSyncDataResult().setRespCode(200);
			return dv;
		}
		if(dv.getApiConfig() == null || dv.getApiConfig().getUrl() == null || dv.getApiConfig().getToken() == null) {
			dv.setSyncDataResult(new SyncDataResult(SyncDataErrorType.REDCap_Error.toString(), "URL or API token is Empty!"));
			return dv;
		}
		
		REDCapVendor vendor = new REDCapVendor();
		ResponseDataView response = vendor.importLongitudinalRecords(dv.getApiConfig(), jsonArray);
		if(response != null && response.getHttpCode() == 200 && !response.getHasError()) {
			dv.setSyncDataResult(new SyncDataResult(response.getCount()));
			dv.getSyncDataResult().setRespCode(response.getHttpCode());
			dv.setSyncStatus(SyncDataStatusType.Finished.toString());
			//update SyncDataRecord
			record.setRedcapSyncStatus(SyncDataStatusType.Finished.toString());
			syncRepo.updateSyncDataRecord(record);
			return dv;
		}else if(response != null){
			dv.setSyncDataResult(new SyncDataResult(SyncDataErrorType.REDCap_Error.toString(), response.getResponseMsg()));
			dv.getSyncDataResult().setRespCode(response.getHttpCode());
			return dv;
		}
		dv.setSyncDataResult(new SyncDataResult(SyncDataErrorType.REDCap_Error.toString(), "REDCap Response: null"));
		return dv;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailVisualSpanRunningJson(List<DetailVisualSpanRunning> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "vsr_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "vsr_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
			
		}
		
		for(DetailVisualSpanRunning d: details) {
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//each stimuli digit
				JSONObject sp_1 = new JSONObject();
				sp_1.put("record", recordId);
				sp_1.put("redcap_event_name", eventId);
				String fieldName1 = "vsr_sp" + d.getTrialNo() + "_1";
				sp_1.put("field_name", fieldName1);
				sp_1.put("value", d.getSi1());
				result.add(sp_1);
				JSONObject sp_2 = new JSONObject();
				sp_2.put("record", recordId);
				sp_2.put("redcap_event_name", eventId);
				String fieldName2 = "vsr_sp" + d.getTrialNo() + "_2";
				sp_2.put("field_name", fieldName2);
				sp_2.put("value", d.getSi2());
				result.add(sp_2);
				JSONObject sp_3 = new JSONObject();
				sp_3.put("record", recordId);
				sp_3.put("redcap_event_name", eventId);
				String fieldName3 = "vsr_sp" + d.getTrialNo() + "_3";
				sp_3.put("field_name", fieldName3);
				sp_3.put("value", d.getSi3());
				result.add(sp_3);
				JSONObject sp_4 = new JSONObject();
				sp_4.put("record", recordId);
				sp_4.put("redcap_event_name", eventId);
				String fieldName4 = "vsr_sp" + d.getTrialNo() + "_4";
				sp_4.put("field_name", fieldName4);
				sp_4.put("value", d.getSi4());
				result.add(sp_4);
				JSONObject sp_5 = new JSONObject();
				sp_5.put("record", recordId);
				sp_5.put("redcap_event_name", eventId);
				String fieldName5 = "vsr_sp" + d.getTrialNo() + "_5";
				sp_5.put("field_name", fieldName5);
				sp_5.put("value", d.getSi5());
				result.add(sp_5);
				JSONObject sp_6 = new JSONObject();
				sp_6.put("record", recordId);
				sp_6.put("redcap_event_name", eventId);
				String fieldName6 = "vsr_sp" + d.getTrialNo() + "_6";
				sp_6.put("field_name", fieldName6);
				sp_6.put("value", d.getSi6());
				result.add(sp_6);
				//stimuli input
				JSONObject sp_list = new JSONObject();
				sp_list.put("record", recordId);
				sp_list.put("redcap_event_name", eventId);
				String spFieldName = "vsr_sp" + d.getTrialNo() + "_list";
				sp_list.put("field_name", spFieldName);
				sp_list.put("value", d.getStimuliInput());
				result.add(sp_list);
				//each user digit
				JSONObject pip_1 = new JSONObject();
				pip_1.put("record", recordId);
				pip_1.put("redcap_event_name", eventId);
				String pipFieldName1 = "vsr_pip" + d.getTrialNo() + "_1";
				pip_1.put("field_name", pipFieldName1);
				pip_1.put("value", d.getUi1());
				result.add(pip_1);
				JSONObject pip_2 = new JSONObject();
				pip_2.put("record", recordId);
				pip_2.put("redcap_event_name", eventId);
				String pipFieldName2 = "vsr_pip" + d.getTrialNo() + "_2";
				pip_2.put("field_name", pipFieldName2);
				pip_2.put("value", d.getUi2());
				result.add(pip_2);
				JSONObject pip_3 = new JSONObject();
				pip_3.put("record", recordId);
				pip_3.put("redcap_event_name", eventId);
				String pipFieldName3 = "vsr_pip" + d.getTrialNo() + "_3";
				pip_3.put("field_name", pipFieldName3);
				pip_3.put("value", d.getUi3());
				result.add(pip_3);
				JSONObject pip_4 = new JSONObject();
				pip_4.put("record", recordId);
				pip_4.put("redcap_event_name", eventId);
				String pipFieldName4 = "vsr_pip" + d.getTrialNo() + "_4";
				pip_4.put("field_name", pipFieldName4);
				pip_4.put("value", d.getUi4());
				result.add(pip_4);
				JSONObject pip_5 = new JSONObject();
				pip_5.put("record", recordId);
				pip_5.put("redcap_event_name", eventId);
				String pipFieldName5 = "vsr_pip" + d.getTrialNo() + "_5";
				pip_5.put("field_name", pipFieldName5);
				pip_5.put("value", d.getUi5());
				result.add(pip_5);
				JSONObject pip_6 = new JSONObject();
				pip_6.put("record", recordId);
				pip_6.put("redcap_event_name", eventId);
				String pipFieldName6 = "vsr_pip" + d.getTrialNo() + "_6";
				pip_6.put("field_name", pipFieldName6);
				pip_6.put("value", d.getUi6());
				result.add(pip_6);
				//user input
				JSONObject pip_list = new JSONObject();
				pip_list.put("record", recordId);
				pip_list.put("redcap_event_name", eventId);
				String pipListFieldName = "vsr_pip" + d.getTrialNo() + "_list";
				pip_list.put("field_name", pipListFieldName);
				pip_list.put("value", d.getUserInput());
				result.add(pip_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "vsr_pip" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject sp_tot = new JSONObject();
				sp_tot.put("record", recordId);
				sp_tot.put("redcap_event_name", eventId);
				String piTotFieldName = "vsr_sp" + d.getTrialNo() + "_tot";
				sp_tot.put("field_name", piTotFieldName);
				sp_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(sp_tot);
				//span
				JSONObject sp_span = new JSONObject();
				sp_span.put("record", recordId);
				sp_span.put("redcap_event_name", eventId);
				String piSpanFieldName = "vsr_sp" + d.getTrialNo() + "_span";
				sp_span.put("field_name", piSpanFieldName);
				sp_span.put("value", d.getNumOfDigitsCorrectFromEnd());
				result.add(sp_span);
				//accuracy
				JSONObject sp_acc = new JSONObject();
				sp_acc.put("record", recordId);
				sp_acc.put("redcap_event_name", eventId);
				String piAccFieldName = "vsr_sp" + d.getTrialNo() + "_acc";
				sp_acc.put("field_name", piAccFieldName);
				sp_acc.put("value", d.getScore());
				result.add(sp_acc);
				//comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "vsr_sp" + d.getTrialNo() + "_comment";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
			}else {
				//each stimuli digit
				JSONObject st_1 = new JSONObject();
				st_1.put("record", recordId);
				st_1.put("redcap_event_name", eventId);
				String stFieldName1 = "vsr_st" + d.getTrialNo() + "_1";
				st_1.put("field_name", stFieldName1);
				st_1.put("value", d.getSi1());
				result.add(st_1);
				JSONObject st_2 = new JSONObject();
				st_2.put("record", recordId);
				st_2.put("redcap_event_name", eventId);
				String stFieldName2 = "vsr_st" + d.getTrialNo() + "_2";
				st_2.put("field_name", stFieldName2);
				st_2.put("value", d.getSi2());
				result.add(st_2);
				JSONObject st_3 = new JSONObject();
				st_3.put("record", recordId);
				st_3.put("redcap_event_name", eventId);
				String stFieldName3 = "vsr_st" + d.getTrialNo() + "_3";
				st_3.put("field_name", stFieldName3);
				st_3.put("value", d.getSi3());
				result.add(st_3);
				JSONObject st_4 = new JSONObject();
				st_4.put("record", recordId);
				st_4.put("redcap_event_name", eventId);
				String stFieldName4 = "vsr_st" + d.getTrialNo() + "_4";
				st_4.put("field_name", stFieldName4);
				st_4.put("value", d.getSi4());
				result.add(st_4);
				JSONObject st_5 = new JSONObject();
				st_5.put("record", recordId);
				st_5.put("redcap_event_name", eventId);
				String stFieldName5 = "vsr_st" + d.getTrialNo() + "_5";
				st_5.put("field_name", stFieldName5);
				st_5.put("value", d.getSi5());
				result.add(st_5);
				JSONObject st_6 = new JSONObject();
				st_6.put("record", recordId);
				st_6.put("redcap_event_name", eventId);
				String stFieldName6 = "vsr_st" + d.getTrialNo() + "_6";
				st_6.put("field_name", stFieldName6);
				st_6.put("value", d.getSi6());
				result.add(st_6);
				//stimuli input
				JSONObject st_list = new JSONObject();
				st_list.put("record", recordId);
				st_list.put("redcap_event_name", eventId);
				String stListFieldName = "vsr_st" + d.getTrialNo() + "_list";
				st_list.put("field_name", stListFieldName);
				st_list.put("value", d.getStimuliInput());
				result.add(st_list);
				//each user digit
				JSONObject pit_1 = new JSONObject();
				pit_1.put("record", recordId);
				pit_1.put("redcap_event_name", eventId);
				String pitFieldName1 = "vsr_pit" + d.getTrialNo() + "_1";
				pit_1.put("field_name", pitFieldName1);
				pit_1.put("value", d.getUi1());
				result.add(pit_1);
				JSONObject pit_2 = new JSONObject();
				pit_2.put("record", recordId);
				pit_2.put("redcap_event_name", eventId);
				String pitFieldName2 = "vsr_pit" + d.getTrialNo() + "_2";
				pit_2.put("field_name", pitFieldName2);
				pit_2.put("value", d.getUi2());
				result.add(pit_2);
				JSONObject pit_3 = new JSONObject();
				pit_3.put("record", recordId);
				pit_3.put("redcap_event_name", eventId);
				String pitFieldName3 = "vsr_pit" + d.getTrialNo() + "_3";
				pit_3.put("field_name", pitFieldName3);
				pit_3.put("value", d.getUi3());
				result.add(pit_3);
				JSONObject pit_4 = new JSONObject();
				pit_4.put("record", recordId);
				pit_4.put("redcap_event_name", eventId);
				String pitFieldName4 = "vsr_pit" + d.getTrialNo() + "_4";
				pit_4.put("field_name", pitFieldName4);
				pit_4.put("value", d.getUi4());
				result.add(pit_4);
				JSONObject pit_5 = new JSONObject();
				pit_5.put("record", recordId);
				pit_5.put("redcap_event_name", eventId);
				String pitFieldName5 = "vsr_pit" + d.getTrialNo() + "_5";
				pit_5.put("field_name", pitFieldName5);
				pit_5.put("value", d.getUi5());
				result.add(pit_5);
				JSONObject pit_6 = new JSONObject();
				pit_6.put("record", recordId);
				pit_6.put("redcap_event_name", eventId);
				String pitFieldName6 = "vsr_pit" + d.getTrialNo() + "_6";
				pit_6.put("field_name", pitFieldName6);
				pit_6.put("value", d.getUi6());
				result.add(pit_6);
				//user input
				JSONObject pit_list = new JSONObject();
				pit_list.put("record", recordId);
				pit_list.put("redcap_event_name", eventId);
				String pitListFieldName = "vsr_pit" + d.getTrialNo() + "_list";
				pit_list.put("field_name", pitListFieldName);
				pit_list.put("value", d.getUserInput());
				result.add(pit_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "vsr_pit" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject st_tot = new JSONObject();
				st_tot.put("record", recordId);
				st_tot.put("redcap_event_name", eventId);
				String tiTotFieldName = "vsr_st" + d.getTrialNo() + "_tot";
				st_tot.put("field_name", tiTotFieldName);
				st_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(st_tot);
				//span
				JSONObject st_span = new JSONObject();
				st_span.put("record", recordId);
				st_span.put("redcap_event_name", eventId);
				String tiSpanFieldName = "vsr_st" + d.getTrialNo() + "_span";
				st_span.put("field_name", tiSpanFieldName);
				st_span.put("value", d.getNumOfDigitsCorrectFromEnd());
				result.add(st_span);
				//accuracy
				JSONObject st_acc = new JSONObject();
				st_acc.put("record", recordId);
				st_acc.put("redcap_event_name", eventId);
				String siAccFieldName = "vsr_st" + d.getTrialNo() + "_acc";
				st_acc.put("field_name", siAccFieldName);
				st_acc.put("value", d.getScore());
				result.add(st_acc);
				//comment
				JSONObject st_comment = new JSONObject();
				st_comment.put("record", recordId);
				st_comment.put("redcap_event_name", eventId);
				String siCommentFieldName = "vsr_st" + d.getTrialNo() + "_comment";
				st_comment.put("field_name", siCommentFieldName);
				st_comment.put("value", d.getComment());
				result.add(st_comment);
			}	
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailVisualSpanJson(List<DetailVisualSpan> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "vs_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "vs_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
			
		}
		
		for(DetailVisualSpan d: details) {
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//each stimuli digit
				JSONObject sp_1 = new JSONObject();
				sp_1.put("record", recordId);
				sp_1.put("redcap_event_name", eventId);
				String fieldName1 = "vs_sp" + d.getTrialNo() + "_1";
				sp_1.put("field_name", fieldName1);
				sp_1.put("value", d.getSi1());
				result.add(sp_1);
				if(d.getTrialNo() > 1) {
					JSONObject sp_2 = new JSONObject();
					sp_2.put("record", recordId);
					sp_2.put("redcap_event_name", eventId);
					String fieldName2 = "vs_sp" + d.getTrialNo() + "_2";
					sp_2.put("field_name", fieldName2);
					sp_2.put("value", d.getSi2());
					result.add(sp_2);
				}
				//stimuli input
				JSONObject sp_list = new JSONObject();
				sp_list.put("record", recordId);
				sp_list.put("redcap_event_name", eventId);
				String spFieldName = "vs_sp" + d.getTrialNo() + "_list";
				sp_list.put("field_name", spFieldName);
				sp_list.put("value", d.getStimuliInput());
				result.add(sp_list);
				//each user digit
				JSONObject pip_1 = new JSONObject();
				pip_1.put("record", recordId);
				pip_1.put("redcap_event_name", eventId);
				String pipFieldName1 = "vs_pip" + d.getTrialNo() + "_1";
				pip_1.put("field_name", pipFieldName1);
				pip_1.put("value", d.getUi1());
				result.add(pip_1);
				if(d.getTrialNo() > 1) {
					JSONObject pip_2 = new JSONObject();
					pip_2.put("record", recordId);
					pip_2.put("redcap_event_name", eventId);
					String pipFieldName2 = "vs_pip" + d.getTrialNo() + "_2";
					pip_2.put("field_name", pipFieldName2);
					pip_2.put("value", d.getUi2());
					result.add(pip_2);
				}
				//user input
				JSONObject pip_list = new JSONObject();
				pip_list.put("record", recordId);
				pip_list.put("redcap_event_name", eventId);
				String pipListFieldName = "vs_pip" + d.getTrialNo() + "_list";
				pip_list.put("field_name", pipListFieldName);
				pip_list.put("value", d.getUserInput());
				result.add(pip_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "vs_pip" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject sp_tot = new JSONObject();
				sp_tot.put("record", recordId);
				sp_tot.put("redcap_event_name", eventId);
				String piTotFieldName = "vs_sp" + d.getTrialNo() + "_tot";
				sp_tot.put("field_name", piTotFieldName);
				sp_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(sp_tot);
				//accuracy
				JSONObject sp_acc = new JSONObject();
				sp_acc.put("record", recordId);
				sp_acc.put("redcap_event_name", eventId);
				String piAccFieldName = "vs_sp" + d.getTrialNo() + "_acc";
				sp_acc.put("field_name", piAccFieldName);
				sp_acc.put("value", d.getScore());
				result.add(sp_acc);
				//comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "vs_sp" + d.getTrialNo() + "_comment";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
			}else {
				//each stimuli digit
				JSONObject st_1 = new JSONObject();
				st_1.put("record", recordId);
				st_1.put("redcap_event_name", eventId);
				String stFieldName1 = "vs_st" + d.getTrialNo() + "_1";
				st_1.put("field_name", stFieldName1);
				st_1.put("value", d.getSi1());
				result.add(st_1);
				JSONObject st_2 = new JSONObject();
				st_2.put("record", recordId);
				st_2.put("redcap_event_name", eventId);
				String stFieldName2 = "vs_st" + d.getTrialNo() + "_2";
				st_2.put("field_name", stFieldName2);
				st_2.put("value", d.getSi2());
				result.add(st_2);
				JSONObject st_3 = new JSONObject();
				st_3.put("record", recordId);
				st_3.put("redcap_event_name", eventId);
				String stFieldName3 = "vs_st" + d.getTrialNo() + "_3";
				st_3.put("field_name", stFieldName3);
				st_3.put("value", d.getSi3());
				result.add(st_3);
				JSONObject st_4 = new JSONObject();
				st_4.put("record", recordId);
				st_4.put("redcap_event_name", eventId);
				String stFieldName4 = "vs_st" + d.getTrialNo() + "_4";
				st_4.put("field_name", stFieldName4);
				st_4.put("value", d.getSi4());
				result.add(st_4);
				JSONObject st_5 = new JSONObject();
				st_5.put("record", recordId);
				st_5.put("redcap_event_name", eventId);
				String stFieldName5 = "vs_st" + d.getTrialNo() + "_5";
				st_5.put("field_name", stFieldName5);
				st_5.put("value", d.getSi5());
				result.add(st_5);
				JSONObject st_6 = new JSONObject();
				st_6.put("record", recordId);
				st_6.put("redcap_event_name", eventId);
				String stFieldName6 = "vs_st" + d.getTrialNo() + "_6";
				st_6.put("field_name", stFieldName6);
				st_6.put("value", d.getSi6());
				result.add(st_6);
				//stimuli input
				JSONObject st_list = new JSONObject();
				st_list.put("record", recordId);
				st_list.put("redcap_event_name", eventId);
				String stListFieldName = "vs_st" + d.getTrialNo() + "_list";
				st_list.put("field_name", stListFieldName);
				st_list.put("value", d.getStimuliInput());
				result.add(st_list);
				//each user digit
				JSONObject pit_1 = new JSONObject();
				pit_1.put("record", recordId);
				pit_1.put("redcap_event_name", eventId);
				String pitFieldName1 = "vs_pit" + d.getTrialNo() + "_1";
				pit_1.put("field_name", pitFieldName1);
				pit_1.put("value", d.getUi1());
				result.add(pit_1);
				JSONObject pit_2 = new JSONObject();
				pit_2.put("record", recordId);
				pit_2.put("redcap_event_name", eventId);
				String pitFieldName2 = "vs_pit" + d.getTrialNo() + "_2";
				pit_2.put("field_name", pitFieldName2);
				pit_2.put("value", d.getUi2());
				result.add(pit_2);
				JSONObject pit_3 = new JSONObject();
				pit_3.put("record", recordId);
				pit_3.put("redcap_event_name", eventId);
				String pitFieldName3 = "vs_pit" + d.getTrialNo() + "_3";
				pit_3.put("field_name", pitFieldName3);
				pit_3.put("value", d.getUi3());
				result.add(pit_3);
				JSONObject pit_4 = new JSONObject();
				pit_4.put("record", recordId);
				pit_4.put("redcap_event_name", eventId);
				String pitFieldName4 = "vs_pit" + d.getTrialNo() + "_4";
				pit_4.put("field_name", pitFieldName4);
				pit_4.put("value", d.getUi4());
				result.add(pit_4);
				JSONObject pit_5 = new JSONObject();
				pit_5.put("record", recordId);
				pit_5.put("redcap_event_name", eventId);
				String pitFieldName5 = "vs_pit" + d.getTrialNo() + "_5";
				pit_5.put("field_name", pitFieldName5);
				pit_5.put("value", d.getUi5());
				result.add(pit_5);
				JSONObject pit_6 = new JSONObject();
				pit_6.put("record", recordId);
				pit_6.put("redcap_event_name", eventId);
				String pitFieldName6 = "vs_pit" + d.getTrialNo() + "_6";
				pit_6.put("field_name", pitFieldName6);
				pit_6.put("value", d.getUi6());
				result.add(pit_6);
				//user input
				JSONObject pit_list = new JSONObject();
				pit_list.put("record", recordId);
				pit_list.put("redcap_event_name", eventId);
				String pitListFieldName = "vs_pit" + d.getTrialNo() + "_list";
				pit_list.put("field_name", pitListFieldName);
				pit_list.put("value", d.getUserInput());
				result.add(pit_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "vs_pit" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject st_tot = new JSONObject();
				st_tot.put("record", recordId);
				st_tot.put("redcap_event_name", eventId);
				String tiTotFieldName = "vs_st" + d.getTrialNo() + "_tot";
				st_tot.put("field_name", tiTotFieldName);
				st_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(st_tot);
				//accuracy
				JSONObject st_acc = new JSONObject();
				st_acc.put("record", recordId);
				st_acc.put("redcap_event_name", eventId);
				String siAccFieldName = "vs_st" + d.getTrialNo() + "_acc";
				st_acc.put("field_name", siAccFieldName);
				st_acc.put("value", d.getScore());
				result.add(st_acc);
				//comment
				JSONObject st_comment = new JSONObject();
				st_comment.put("record", recordId);
				st_comment.put("redcap_event_name", eventId);
				String siCommentFieldName = "vs_st" + d.getTrialNo() + "_comment";
				st_comment.put("field_name", siCommentFieldName);
				st_comment.put("value", d.getComment());
				result.add(st_comment);
			}	
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailVisualBindingJson(List<DetailVisualBindingSpan> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "vbs_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "vbs_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
			
		}
		
		for(DetailVisualBindingSpan d: details) {
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//each stimuli digit
				JSONObject sp_1 = new JSONObject();
				sp_1.put("record", recordId);
				sp_1.put("redcap_event_name", eventId);
				String fieldName1 = "vbs_sppo" + d.getTrialNo() + "_1";
				sp_1.put("field_name", fieldName1);
				sp_1.put("value", d.getSpi1());
				result.add(sp_1);
				JSONObject sw_1 = new JSONObject();
				sw_1.put("record", recordId);
				sw_1.put("redcap_event_name", eventId);
				String sw1 = "vbs_splo" + d.getTrialNo() + "_1";
				sw_1.put("field_name", sw1);
				sw_1.put("value", d.getSli1());
				result.add(sw_1);
				if(d.getTrialNo() > 1) {
					JSONObject sp_2 = new JSONObject();
					sp_2.put("record", recordId);
					sp_2.put("redcap_event_name", eventId);
					String fieldName2 = "vbs_sppo" + d.getTrialNo() + "_2";
					sp_2.put("field_name", fieldName2);
					sp_2.put("value", d.getSpi2());
					result.add(sp_2);
					JSONObject sw_2 = new JSONObject();
					sw_2.put("record", recordId);
					sw_2.put("redcap_event_name", eventId);
					String sw2 = "vbs_splo" + d.getTrialNo() + "_2";
					sw_2.put("field_name", sw2);
					sw_2.put("value", d.getSli2());
					result.add(sw_2);
				}
				//stimuli input
				JSONObject sp_list = new JSONObject();
				sp_list.put("record", recordId);
				sp_list.put("redcap_event_name", eventId);
				String spFieldName = "vbs_sppo" + d.getTrialNo() + "_list";
				sp_list.put("field_name", spFieldName);
				sp_list.put("value", d.getStimuliPolygonInput());
				result.add(sp_list);
				JSONObject sw_list = new JSONObject();
				sw_list.put("record", recordId);
				sw_list.put("redcap_event_name", eventId);
				String swFieldName = "vbs_splo" + d.getTrialNo() + "_list";
				sw_list.put("field_name", swFieldName);
				sw_list.put("value", d.getStimuliLocationInput());
				result.add(sw_list);
				//each user digit
				JSONObject pip_1 = new JSONObject();
				pip_1.put("record", recordId);
				pip_1.put("redcap_event_name", eventId);
				String pipFieldName1 = "vbs_pippo" + d.getTrialNo() + "_1";
				pip_1.put("field_name", pipFieldName1);
				pip_1.put("value", d.getUpi1());
				result.add(pip_1);
				JSONObject piw_1 = new JSONObject();
				piw_1.put("record", recordId);
				piw_1.put("redcap_event_name", eventId);
				String piwFieldName1 = "vbs_piplo" + d.getTrialNo() + "_1";
				piw_1.put("field_name", piwFieldName1);
				piw_1.put("value", d.getUli1());
				result.add(piw_1);
				if(d.getTrialNo() > 1) {
					JSONObject pip_2 = new JSONObject();
					pip_2.put("record", recordId);
					pip_2.put("redcap_event_name", eventId);
					String pipFieldName2 = "vbs_pippo" + d.getTrialNo() + "_2";
					pip_2.put("field_name", pipFieldName2);
					pip_2.put("value", d.getUpi2());
					result.add(pip_2);
					JSONObject piw_2 = new JSONObject();
					piw_2.put("record", recordId);
					piw_2.put("redcap_event_name", eventId);
					String piwFieldName2 = "vbs_piplo" + d.getTrialNo() + "_2";
					piw_2.put("field_name", piwFieldName2);
					piw_2.put("value", d.getUli2());
					result.add(piw_2);
				}
				//user input
				JSONObject pip_list = new JSONObject();
				pip_list.put("record", recordId);
				pip_list.put("redcap_event_name", eventId);
				String pipListFieldName = "vbs_pippo" + d.getTrialNo() + "_list";
				pip_list.put("field_name", pipListFieldName);
				pip_list.put("value", d.getUserPolygonInput());
				result.add(pip_list);
				JSONObject piw_list = new JSONObject();
				piw_list.put("record", recordId);
				piw_list.put("redcap_event_name", eventId);
				String piwListFieldName = "vbs_piplo" + d.getTrialNo() + "_list";
				piw_list.put("field_name", piwListFieldName);
				piw_list.put("value", d.getUserLocationInput());
				result.add(piw_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "vbs_pip" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject sp_tot = new JSONObject();
				sp_tot.put("record", recordId);
				sp_tot.put("redcap_event_name", eventId);
				String piTotFieldName = "vbs_sp" + d.getTrialNo() + "_tot";
				sp_tot.put("field_name", piTotFieldName);
				sp_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(sp_tot);
				//accuracy
				JSONObject sp_acc = new JSONObject();
				sp_acc.put("record", recordId);
				sp_acc.put("redcap_event_name", eventId);
				String piAccFieldName = "vbs_sp" + d.getTrialNo() + "_acc";
				sp_acc.put("field_name", piAccFieldName);
				sp_acc.put("value", d.getScore());
				result.add(sp_acc);
				//comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "vbs_sp" + d.getTrialNo() + "_comment";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
			}else {
				//each stimuli digit
				JSONObject st_1 = new JSONObject();
				st_1.put("record", recordId);
				st_1.put("redcap_event_name", eventId);
				String stFieldName1 = "vbs_stpo" + d.getTrialNo() + "_1";
				st_1.put("field_name", stFieldName1);
				st_1.put("value", d.getSpi1());
				result.add(st_1);
				JSONObject st_2 = new JSONObject();
				st_2.put("record", recordId);
				st_2.put("redcap_event_name", eventId);
				String stFieldName2 = "vbs_stpo" + d.getTrialNo() + "_2";
				st_2.put("field_name", stFieldName2);
				st_2.put("value", d.getSpi2());
				result.add(st_2);
				JSONObject st_3 = new JSONObject();
				st_3.put("record", recordId);
				st_3.put("redcap_event_name", eventId);
				String stFieldName3 = "vbs_stpo" + d.getTrialNo() + "_3";
				st_3.put("field_name", stFieldName3);
				st_3.put("value", d.getSpi3());
				result.add(st_3);
				JSONObject st_4 = new JSONObject();
				st_4.put("record", recordId);
				st_4.put("redcap_event_name", eventId);
				String stFieldName4 = "vbs_stpo" + d.getTrialNo() + "_4";
				st_4.put("field_name", stFieldName4);
				st_4.put("value", d.getSpi4());
				result.add(st_4);
				JSONObject stw_1 = new JSONObject();
				stw_1.put("record", recordId);
				stw_1.put("redcap_event_name", eventId);
				String swFieldName1 = "vbs_stlo" + d.getTrialNo() + "_1";
				stw_1.put("field_name", swFieldName1);
				stw_1.put("value", d.getSli1());
				result.add(stw_1);
				JSONObject sw_2 = new JSONObject();
				sw_2.put("record", recordId);
				sw_2.put("redcap_event_name", eventId);
				String swFieldName2 = "vbs_stlo" + d.getTrialNo() + "_2";
				sw_2.put("field_name", swFieldName2);
				sw_2.put("value", d.getSli2());
				result.add(sw_2);
				JSONObject sw_3 = new JSONObject();
				sw_3.put("record", recordId);
				sw_3.put("redcap_event_name", eventId);
				String swFieldName3 = "vbs_stlo" + d.getTrialNo() + "_3";
				sw_3.put("field_name", swFieldName3);
				sw_3.put("value", d.getSli3());
				result.add(sw_3);
				JSONObject sw_4 = new JSONObject();
				sw_4.put("record", recordId);
				sw_4.put("redcap_event_name", eventId);
				String swFieldName4 = "vbs_stlo" + d.getTrialNo() + "_4";
				sw_4.put("field_name", swFieldName4);
				sw_4.put("value", d.getSli4());
				result.add(sw_4);
				//stimuli input
				JSONObject st_list = new JSONObject();
				st_list.put("record", recordId);
				st_list.put("redcap_event_name", eventId);
				String stListFieldName = "vbs_stpo" + d.getTrialNo() + "_list";
				st_list.put("field_name", stListFieldName);
				st_list.put("value", d.getStimuliPolygonInput());
				result.add(st_list);
				JSONObject sw_list = new JSONObject();
				sw_list.put("record", recordId);
				sw_list.put("redcap_event_name", eventId);
				String swListFieldName = "vbs_stlo" + d.getTrialNo() + "_list";
				sw_list.put("field_name", swListFieldName);
				sw_list.put("value", d.getStimuliLocationInput());
				result.add(sw_list);
				//each user digit
				JSONObject pit_1 = new JSONObject();
				pit_1.put("record", recordId);
				pit_1.put("redcap_event_name", eventId);
				String pitFieldName1 = "vbs_pitpo" + d.getTrialNo() + "_1";
				pit_1.put("field_name", pitFieldName1);
				pit_1.put("value", d.getUpi1());
				result.add(pit_1);
				JSONObject pit_2 = new JSONObject();
				pit_2.put("record", recordId);
				pit_2.put("redcap_event_name", eventId);
				String pitFieldName2 = "vbs_pitpo" + d.getTrialNo() + "_2";
				pit_2.put("field_name", pitFieldName2);
				pit_2.put("value", d.getUpi2());
				result.add(pit_2);
				JSONObject pit_3 = new JSONObject();
				pit_3.put("record", recordId);
				pit_3.put("redcap_event_name", eventId);
				String pitFieldName3 = "vbs_pitpo" + d.getTrialNo() + "_3";
				pit_3.put("field_name", pitFieldName3);
				pit_3.put("value", d.getUpi3());
				result.add(pit_3);
				JSONObject pit_4 = new JSONObject();
				pit_4.put("record", recordId);
				pit_4.put("redcap_event_name", eventId);
				String pitFieldName4 = "vbs_pitpo" + d.getTrialNo() + "_4";
				pit_4.put("field_name", pitFieldName4);
				pit_4.put("value", d.getUpi4());
				result.add(pit_4);
				JSONObject piw_1 = new JSONObject();
				piw_1.put("record", recordId);
				piw_1.put("redcap_event_name", eventId);
				String piwFieldName1 = "vbs_pitlo" + d.getTrialNo() + "_1";
				piw_1.put("field_name", piwFieldName1);
				piw_1.put("value", d.getUli1());
				result.add(piw_1);
				JSONObject piw_2 = new JSONObject();
				piw_2.put("record", recordId);
				piw_2.put("redcap_event_name", eventId);
				String piwFieldName2 = "vbs_pitlo" + d.getTrialNo() + "_2";
				piw_2.put("field_name", piwFieldName2);
				piw_2.put("value", d.getUli2());
				result.add(piw_2);
				JSONObject piw_3 = new JSONObject();
				piw_3.put("record", recordId);
				piw_3.put("redcap_event_name", eventId);
				String piwFieldName3 = "vbs_pitlo" + d.getTrialNo() + "_3";
				piw_3.put("field_name", piwFieldName3);
				piw_3.put("value", d.getUli3());
				result.add(piw_3);
				JSONObject piw_4 = new JSONObject();
				piw_4.put("record", recordId);
				piw_4.put("redcap_event_name", eventId);
				String piwFieldName4 = "vbs_pitlo" + d.getTrialNo() + "_4";
				piw_4.put("field_name", piwFieldName4);
				piw_4.put("value", d.getUli4());
				result.add(piw_4);
				//user input
				JSONObject pit_list = new JSONObject();
				pit_list.put("record", recordId);
				pit_list.put("redcap_event_name", eventId);
				String pitListFieldName = "vbs_pitpo" + d.getTrialNo() + "_list";
				pit_list.put("field_name", pitListFieldName);
				pit_list.put("value", d.getUserPolygonInput());
				result.add(pit_list);
				JSONObject piw_list = new JSONObject();
				piw_list.put("record", recordId);
				piw_list.put("redcap_event_name", eventId);
				String piwListFieldName = "vbs_pitlo" + d.getTrialNo() + "_list";
				piw_list.put("field_name", piwListFieldName);
				piw_list.put("value", d.getUserLocationInput());
				result.add(piw_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "vbs_pit" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject st_tot = new JSONObject();
				st_tot.put("record", recordId);
				st_tot.put("redcap_event_name", eventId);
				String tiTotFieldName = "vbs_st" + d.getTrialNo() + "_tot";
				st_tot.put("field_name", tiTotFieldName);
				st_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(st_tot);
				//accuracy
				JSONObject st_acc = new JSONObject();
				st_acc.put("record", recordId);
				st_acc.put("redcap_event_name", eventId);
				String siAccFieldName = "vbs_st" + d.getTrialNo() + "_acc";
				st_acc.put("field_name", siAccFieldName);
				st_acc.put("value", d.getScore());
				result.add(st_acc);
				//comment
				JSONObject st_comment = new JSONObject();
				st_comment.put("record", recordId);
				st_comment.put("redcap_event_name", eventId);
				String siCommentFieldName = "vbs_st" + d.getTrialNo() + "_comment";
				st_comment.put("field_name", siCommentFieldName);
				st_comment.put("value", d.getComment());
				result.add(st_comment);
			}	
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailRepetionVisualJson(List<DetailRepetitionVisual> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "rdv_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "rdv_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
		}
		
		for(DetailRepetitionVisual d: details) {
			String repetitionCount = d.getRepetitionCount() == 2 ? "double" : "triple";
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//sitmuli result - rdv_sp1_1_double
				JSONObject lval = new JSONObject();
				lval.put("record", recordId);
				lval.put("redcap_event_name", eventId);
				String lvalFieldName = "rdv_sp" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_" + repetitionCount;
				lval.put("field_name", lvalFieldName);
				lval.put("value", d.getCorrectRepsonse());
				result.add(lval);				
				//user result - rdv_pip1_1_double
				JSONObject ulval = new JSONObject();
				ulval.put("record", recordId);
				ulval.put("redcap_event_name", eventId);
				String ulvalFieldName = "rdv_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_" + repetitionCount;
				ulval.put("field_name", ulvalFieldName);
				ulval.put("value", d.getUserInput());
				result.add(ulval);
				//comment - rdv_sp1_1_comment_double
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "rdv_sp" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_comment_" + repetitionCount;
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
				//response time - rdv_pip1_1_rt_double
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "rdv_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rt_" + repetitionCount;
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
			}else {
				//sitmuli result - rdv_st1_1_double
				JSONObject lval = new JSONObject();
				lval.put("record", recordId);
				lval.put("redcap_event_name", eventId);
				String lvalFieldName = "rdv_st" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_" + repetitionCount;
				lval.put("field_name", lvalFieldName);
				lval.put("value", d.getCorrectRepsonse());
				result.add(lval);				
				//user result - rdv_pit1_1_double
				JSONObject ulval = new JSONObject();
				ulval.put("record", recordId);
				ulval.put("redcap_event_name", eventId);
				String ulvalFieldName = "rdv_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_" + repetitionCount;
				ulval.put("field_name", ulvalFieldName);
				ulval.put("value", d.getUserInput());
				result.add(ulval);
				//comment - rdv_st1_1_comment_double
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "rdv_st" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_comment_" + repetitionCount;
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
				//response time - rdv_pit1_1_rt_double
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "rdv_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rt_" + repetitionCount;
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
			}	
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailRepetitionAuditoryJson(List<DetailRepetitionAuditory> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "rda_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "rda_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
		}
		
		for(DetailRepetitionAuditory d: details) {
			String repetitionCount = d.getRepetitionCount() == 2 ? "double" : "triple";
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//sitmuli result - rda_sp1_1_double
				JSONObject lval = new JSONObject();
				lval.put("record", recordId);
				lval.put("redcap_event_name", eventId);
				String lvalFieldName = "rda_sp" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_" + repetitionCount;
				lval.put("field_name", lvalFieldName);
				lval.put("value", d.getCorrectRepsonse());
				result.add(lval);				
				//user result - rda_pip1_1_double
				JSONObject ulval = new JSONObject();
				ulval.put("record", recordId);
				ulval.put("redcap_event_name", eventId);
				String ulvalFieldName = "rda_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_" + repetitionCount;
				ulval.put("field_name", ulvalFieldName);
				ulval.put("value", d.getUserInput());
				result.add(ulval);
				//comment - rda_sp1_1_comment_double
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "rda_sp" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_comment_" + repetitionCount;
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
				//response time - rda_pip1_1_rt_double
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "rda_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rt_" + repetitionCount;
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
			}else {
				//sitmuli result - rda_st1_1_double
				JSONObject lval = new JSONObject();
				lval.put("record", recordId);
				lval.put("redcap_event_name", eventId);
				String lvalFieldName = "rda_st" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_" + repetitionCount;
				lval.put("field_name", lvalFieldName);
				lval.put("value", d.getCorrectRepsonse());
				result.add(lval);				
				//user result - rda_pit1_1_double
				JSONObject ulval = new JSONObject();
				ulval.put("record", recordId);
				ulval.put("redcap_event_name", eventId);
				String ulvalFieldName = "rda_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_" + repetitionCount;
				ulval.put("field_name", ulvalFieldName);
				ulval.put("value", d.getUserInput());
				result.add(ulval);
				//comment - rda_st1_1_comment_double
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "rda_st" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_comment_" + repetitionCount;
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
				//response time - rda_pit1_1_rt_double
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "rda_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rt_" + repetitionCount;
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
			}	
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailPhonologicalBindingJson(List<DetailPhonologicalBinding> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "pbs_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "pbs_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
		}
		
		for(DetailPhonologicalBinding d: details) {
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//each stimuli digit
				JSONObject sp_1 = new JSONObject();
				sp_1.put("record", recordId);
				sp_1.put("redcap_event_name", eventId);
				String fieldName1 = "pbs_spso" + d.getTrialNo() + "_1";
				sp_1.put("field_name", fieldName1);
				sp_1.put("value", d.getSsi1());
				result.add(sp_1);
				JSONObject sw_1 = new JSONObject();
				sw_1.put("record", recordId);
				sw_1.put("redcap_event_name", eventId);
				String sw1 = "pbs_spwo" + d.getTrialNo() + "_1";
				sw_1.put("field_name", sw1);
				sw_1.put("value", d.getSwi1());
				result.add(sw_1);
				if(d.getTrialNo() > 1) {
					JSONObject sp_2 = new JSONObject();
					sp_2.put("record", recordId);
					sp_2.put("redcap_event_name", eventId);
					String fieldName2 = "pbs_spso" + d.getTrialNo() + "_2";
					sp_2.put("field_name", fieldName2);
					sp_2.put("value", d.getSsi2());
					result.add(sp_2);
					JSONObject sw_2 = new JSONObject();
					sw_2.put("record", recordId);
					sw_2.put("redcap_event_name", eventId);
					String sw2 = "pbs_spwo" + d.getTrialNo() + "_2";
					sw_2.put("field_name", sw2);
					sw_2.put("value", d.getSwi2());
					result.add(sw_2);
				}
				//stimuli input
				JSONObject sp_list = new JSONObject();
				sp_list.put("record", recordId);
				sp_list.put("redcap_event_name", eventId);
				String spFieldName = "pbs_spso" + d.getTrialNo() + "_list";
				sp_list.put("field_name", spFieldName);
				sp_list.put("value", d.getStimuliSoundInput());
				result.add(sp_list);
				JSONObject sw_list = new JSONObject();
				sw_list.put("record", recordId);
				sw_list.put("redcap_event_name", eventId);
				String swFieldName = "pbs_spwo" + d.getTrialNo() + "_list";
				sw_list.put("field_name", swFieldName);
				sw_list.put("value", d.getStimuliWordInput());
				result.add(sw_list);
				//each user digit
				JSONObject pip_1 = new JSONObject();
				pip_1.put("record", recordId);
				pip_1.put("redcap_event_name", eventId);
				String pipFieldName1 = "pbs_pipso" + d.getTrialNo() + "_1";
				pip_1.put("field_name", pipFieldName1);
				pip_1.put("value", d.getUsi1());
				result.add(pip_1);
				JSONObject piw_1 = new JSONObject();
				piw_1.put("record", recordId);
				piw_1.put("redcap_event_name", eventId);
				String piwFieldName1 = "pbs_pipwo" + d.getTrialNo() + "_1";
				piw_1.put("field_name", piwFieldName1);
				piw_1.put("value", d.getUwi1());
				result.add(piw_1);
				if(d.getTrialNo() > 1) {
					JSONObject pip_2 = new JSONObject();
					pip_2.put("record", recordId);
					pip_2.put("redcap_event_name", eventId);
					String pipFieldName2 = "pbs_pipso" + d.getTrialNo() + "_2";
					pip_2.put("field_name", pipFieldName2);
					pip_2.put("value", d.getUsi2());
					result.add(pip_2);
					JSONObject piw_2 = new JSONObject();
					piw_2.put("record", recordId);
					piw_2.put("redcap_event_name", eventId);
					String piwFieldName2 = "pbs_pipwo" + d.getTrialNo() + "_2";
					piw_2.put("field_name", piwFieldName2);
					piw_2.put("value", d.getUwi2());
					result.add(piw_2);
				}
				//user input
				JSONObject pip_list = new JSONObject();
				pip_list.put("record", recordId);
				pip_list.put("redcap_event_name", eventId);
				String pipListFieldName = "pbs_pipso" + d.getTrialNo() + "_list";
				pip_list.put("field_name", pipListFieldName);
				pip_list.put("value", d.getUserSoundInput());
				result.add(pip_list);
				JSONObject piw_list = new JSONObject();
				piw_list.put("record", recordId);
				piw_list.put("redcap_event_name", eventId);
				String piwListFieldName = "pbs_pipwo" + d.getTrialNo() + "_list";
				piw_list.put("field_name", piwListFieldName);
				piw_list.put("value", d.getUserWordInput());
				result.add(piw_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "pbs_pip" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject sp_tot = new JSONObject();
				sp_tot.put("record", recordId);
				sp_tot.put("redcap_event_name", eventId);
				String piTotFieldName = "pbs_sp" + d.getTrialNo() + "_tot";
				sp_tot.put("field_name", piTotFieldName);
				sp_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(sp_tot);
				//accuracy
				JSONObject sp_acc = new JSONObject();
				sp_acc.put("record", recordId);
				sp_acc.put("redcap_event_name", eventId);
				String piAccFieldName = "pbs_sp" + d.getTrialNo() + "_acc";
				sp_acc.put("field_name", piAccFieldName);
				sp_acc.put("value", d.getScore());
				result.add(sp_acc);
				//comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "pbs_sp" + d.getTrialNo() + "_comment";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
			}else {
				//each stimuli digit
				JSONObject st_1 = new JSONObject();
				st_1.put("record", recordId);
				st_1.put("redcap_event_name", eventId);
				String stFieldName1 = "pbs_stso" + d.getTrialNo() + "_1";
				st_1.put("field_name", stFieldName1);
				st_1.put("value", d.getSsi1());
				result.add(st_1);
				JSONObject st_2 = new JSONObject();
				st_2.put("record", recordId);
				st_2.put("redcap_event_name", eventId);
				String stFieldName2 = "pbs_stso" + d.getTrialNo() + "_2";
				st_2.put("field_name", stFieldName2);
				st_2.put("value", d.getSsi2());
				result.add(st_2);
				JSONObject st_3 = new JSONObject();
				st_3.put("record", recordId);
				st_3.put("redcap_event_name", eventId);
				String stFieldName3 = "pbs_stso" + d.getTrialNo() + "_3";
				st_3.put("field_name", stFieldName3);
				st_3.put("value", d.getSsi3());
				result.add(st_3);
				JSONObject st_4 = new JSONObject();
				st_4.put("record", recordId);
				st_4.put("redcap_event_name", eventId);
				String stFieldName4 = "pbs_stso" + d.getTrialNo() + "_4";
				st_4.put("field_name", stFieldName4);
				st_4.put("value", d.getSsi4());
				result.add(st_4);
				JSONObject stw_1 = new JSONObject();
				stw_1.put("record", recordId);
				stw_1.put("redcap_event_name", eventId);
				String swFieldName1 = "pbs_stwo" + d.getTrialNo() + "_1";
				stw_1.put("field_name", swFieldName1);
				stw_1.put("value", d.getSwi1());
				result.add(stw_1);
				JSONObject sw_2 = new JSONObject();
				sw_2.put("record", recordId);
				sw_2.put("redcap_event_name", eventId);
				String swFieldName2 = "pbs_stwo" + d.getTrialNo() + "_2";
				sw_2.put("field_name", swFieldName2);
				sw_2.put("value", d.getSwi2());
				result.add(sw_2);
				JSONObject sw_3 = new JSONObject();
				sw_3.put("record", recordId);
				sw_3.put("redcap_event_name", eventId);
				String swFieldName3 = "pbs_stwo" + d.getTrialNo() + "_3";
				sw_3.put("field_name", swFieldName3);
				sw_3.put("value", d.getSwi3());
				result.add(sw_3);
				JSONObject sw_4 = new JSONObject();
				sw_4.put("record", recordId);
				sw_4.put("redcap_event_name", eventId);
				String swFieldName4 = "pbs_stwo" + d.getTrialNo() + "_4";
				sw_4.put("field_name", swFieldName4);
				sw_4.put("value", d.getSwi4());
				result.add(sw_4);
				//stimuli input
				JSONObject st_list = new JSONObject();
				st_list.put("record", recordId);
				st_list.put("redcap_event_name", eventId);
				String stListFieldName = "pbs_stso" + d.getTrialNo() + "_list";
				st_list.put("field_name", stListFieldName);
				st_list.put("value", d.getStimuliSoundInput());
				result.add(st_list);
				JSONObject sw_list = new JSONObject();
				sw_list.put("record", recordId);
				sw_list.put("redcap_event_name", eventId);
				String swListFieldName = "pbs_stwo" + d.getTrialNo() + "_list";
				sw_list.put("field_name", swListFieldName);
				sw_list.put("value", d.getStimuliWordInput());
				result.add(sw_list);
				//each user digit
				JSONObject pit_1 = new JSONObject();
				pit_1.put("record", recordId);
				pit_1.put("redcap_event_name", eventId);
				String pitFieldName1 = "pbs_pitso" + d.getTrialNo() + "_1";
				pit_1.put("field_name", pitFieldName1);
				pit_1.put("value", d.getUsi1());
				result.add(pit_1);
				JSONObject pit_2 = new JSONObject();
				pit_2.put("record", recordId);
				pit_2.put("redcap_event_name", eventId);
				String pitFieldName2 = "pbs_pitso" + d.getTrialNo() + "_2";
				pit_2.put("field_name", pitFieldName2);
				pit_2.put("value", d.getUsi2());
				result.add(pit_2);
				JSONObject pit_3 = new JSONObject();
				pit_3.put("record", recordId);
				pit_3.put("redcap_event_name", eventId);
				String pitFieldName3 = "pbs_pitso" + d.getTrialNo() + "_3";
				pit_3.put("field_name", pitFieldName3);
				pit_3.put("value", d.getUsi3());
				result.add(pit_3);
				JSONObject pit_4 = new JSONObject();
				pit_4.put("record", recordId);
				pit_4.put("redcap_event_name", eventId);
				String pitFieldName4 = "pbs_pitso" + d.getTrialNo() + "_4";
				pit_4.put("field_name", pitFieldName4);
				pit_4.put("value", d.getUsi4());
				result.add(pit_4);
				JSONObject piw_1 = new JSONObject();
				piw_1.put("record", recordId);
				piw_1.put("redcap_event_name", eventId);
				String piwFieldName1 = "pbs_pitwo" + d.getTrialNo() + "_1";
				piw_1.put("field_name", piwFieldName1);
				piw_1.put("value", d.getUwi1());
				result.add(piw_1);
				JSONObject piw_2 = new JSONObject();
				piw_2.put("record", recordId);
				piw_2.put("redcap_event_name", eventId);
				String piwFieldName2 = "pbs_pitwo" + d.getTrialNo() + "_2";
				piw_2.put("field_name", piwFieldName2);
				piw_2.put("value", d.getUwi2());
				result.add(piw_2);
				JSONObject piw_3 = new JSONObject();
				piw_3.put("record", recordId);
				piw_3.put("redcap_event_name", eventId);
				String piwFieldName3 = "pbs_pitwo" + d.getTrialNo() + "_3";
				piw_3.put("field_name", piwFieldName3);
				piw_3.put("value", d.getUwi3());
				result.add(piw_3);
				JSONObject piw_4 = new JSONObject();
				piw_4.put("record", recordId);
				piw_4.put("redcap_event_name", eventId);
				String piwFieldName4 = "pbs_pitwo" + d.getTrialNo() + "_4";
				piw_4.put("field_name", piwFieldName4);
				piw_4.put("value", d.getUwi4());
				result.add(piw_4);
				//user input
				JSONObject pit_list = new JSONObject();
				pit_list.put("record", recordId);
				pit_list.put("redcap_event_name", eventId);
				String pitListFieldName = "pbs_pitso" + d.getTrialNo() + "_list";
				pit_list.put("field_name", pitListFieldName);
				pit_list.put("value", d.getUserSoundInput());
				result.add(pit_list);
				JSONObject piw_list = new JSONObject();
				piw_list.put("record", recordId);
				piw_list.put("redcap_event_name", eventId);
				String piwListFieldName = "pbs_pitwo" + d.getTrialNo() + "_list";
				piw_list.put("field_name", piwListFieldName);
				piw_list.put("value", d.getUserWordInput());
				result.add(piw_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "pbs_pit" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject st_tot = new JSONObject();
				st_tot.put("record", recordId);
				st_tot.put("redcap_event_name", eventId);
				String tiTotFieldName = "pbs_st" + d.getTrialNo() + "_tot";
				st_tot.put("field_name", tiTotFieldName);
				st_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(st_tot);
				//accuracy
				JSONObject st_acc = new JSONObject();
				st_acc.put("record", recordId);
				st_acc.put("redcap_event_name", eventId);
				String siAccFieldName = "pbs_st" + d.getTrialNo() + "_acc";
				st_acc.put("field_name", siAccFieldName);
				st_acc.put("value", d.getScore());
				result.add(st_acc);
				//comment
				JSONObject st_comment = new JSONObject();
				st_comment.put("record", recordId);
				st_comment.put("redcap_event_name", eventId);
				String siCommentFieldName = "pbs_st" + d.getTrialNo() + "_comment";
				st_comment.put("field_name", siCommentFieldName);
				st_comment.put("value", d.getComment());
				result.add(st_comment);
			}	
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailNumberUpdateVisualJson(List<DetailNumberUpdateVisual> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "nuv_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "nuv_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
		}
		
		for(DetailNumberUpdateVisual d: details) {
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//stimuli left val - nuv_sp1_1_lval_2boxes
				JSONObject lval = new JSONObject();
				lval.put("record", recordId);
				lval.put("redcap_event_name", eventId);
				String lvalFieldName = "nuv_sp" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_lval_" + d.getNumOfBoxes() + "boxes";
				lval.put("field_name", lvalFieldName);
				lval.put("value", d.getStimuliLeftValAfter());
				result.add(lval);
				//stimuli middle val - nuv_sp1_1_mval_2boxes
				JSONObject mval = new JSONObject();
				mval.put("record", recordId);
				mval.put("redcap_event_name", eventId);
				String mvalFieldName = "nuv_sp" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_mval_" + d.getNumOfBoxes() + "boxes";
				mval.put("field_name", mvalFieldName);
				mval.put("value", d.getStimuliMiddleValAfter());
				result.add(mval);
				//stimuli right val - nuv_sp1_1_rval_2boxes
				JSONObject rval = new JSONObject();
				rval.put("record", recordId);
				rval.put("redcap_event_name", eventId);
				String rvalFieldName = "nuv_sp" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rval_" + d.getNumOfBoxes() + "boxes";
				rval.put("field_name", rvalFieldName);
				rval.put("value", d.getStimuliRightValAfter());
				result.add(rval);
				//user left val - nuv_pip1_1_lval_2boxes
				JSONObject ulval = new JSONObject();
				ulval.put("record", recordId);
				ulval.put("redcap_event_name", eventId);
				String ulvalFieldName = "nuv_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_lval_" + d.getNumOfBoxes() + "boxes";
				ulval.put("field_name", ulvalFieldName);
				ulval.put("value", d.getUserLeftVal());
				result.add(ulval);
				//user middle val - nuv_pip1_1_mval_2boxes
				JSONObject umval = new JSONObject();
				umval.put("record", recordId);
				umval.put("redcap_event_name", eventId);
				String umvalFieldName = "nuv_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_mval_" + d.getNumOfBoxes() + "boxes";
				umval.put("field_name", umvalFieldName);
				umval.put("value", d.getUserMiddleVal());
				result.add(umval);
				//user right val - nuv_pip1_1_rval_2boxes
				JSONObject urval = new JSONObject();
				urval.put("record", recordId);
				urval.put("redcap_event_name", eventId);
				String urvalFieldName = "nuv_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rval_" + d.getNumOfBoxes() + "boxes";
				urval.put("field_name", urvalFieldName);
				urval.put("value", d.getUserRightVal());
				result.add(urval);
				//comment - nuv_sp1_1_comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "nuv_sp" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_comment_" + d.getNumOfBoxes() + "boxes";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
				//response time - nuv_pip1_1_rt
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "nuv_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rt_" + d.getNumOfBoxes() + "boxes";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//nuv_pip1_1_acc_2boxes
				JSONObject acc = new JSONObject();
				acc.put("record", recordId);
				acc.put("redcap_event_name", eventId);
				String accFieldName = "nuv_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_acc_" + d.getNumOfBoxes() + "boxes";
				acc.put("field_name", accFieldName);
				acc.put("value", d.getLenientCorrect());
				result.add(acc);
			}else {
				//stimuli left val - nuv_st1_1_lval_2boxes
				JSONObject lval = new JSONObject();
				lval.put("record", recordId);
				lval.put("redcap_event_name", eventId);
				String lvalFieldName = "nuv_st" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_lval_" + d.getNumOfBoxes() + "boxes";
				lval.put("field_name", lvalFieldName);
				lval.put("value", d.getStimuliLeftValAfter());
				result.add(lval);
				//stimuli middle val - nuv_st1_1_mval_2boxes
				JSONObject mval = new JSONObject();
				mval.put("record", recordId);
				mval.put("redcap_event_name", eventId);
				String mvalFieldName = "nuv_st" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_mval_" + d.getNumOfBoxes() + "boxes";
				mval.put("field_name", mvalFieldName);
				mval.put("value", d.getStimuliMiddleValAfter());
				result.add(mval);
				//stimuli right val - nuv_st1_1_rval_2boxes
				JSONObject rval = new JSONObject();
				rval.put("record", recordId);
				rval.put("redcap_event_name", eventId);
				String rvalFieldName = "nuv_st" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rval_" + d.getNumOfBoxes() + "boxes";
				rval.put("field_name", rvalFieldName);
				rval.put("value", d.getStimuliRightValAfter());
				result.add(rval);
				//user left val - nuv_pit1_1_lval_2boxes
				JSONObject ulval = new JSONObject();
				ulval.put("record", recordId);
				ulval.put("redcap_event_name", eventId);
				String ulvalFieldName = "nuv_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_lval_" + d.getNumOfBoxes() + "boxes";
				ulval.put("field_name", ulvalFieldName);
				ulval.put("value", d.getUserLeftVal());
				result.add(ulval);
				//user middle val - nuv_pit1_1_mval_2boxes
				JSONObject umval = new JSONObject();
				umval.put("record", recordId);
				umval.put("redcap_event_name", eventId);
				String umvalFieldName = "nuv_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_mval_" + d.getNumOfBoxes() + "boxes";
				umval.put("field_name", umvalFieldName);
				umval.put("value", d.getUserMiddleVal());
				result.add(umval);
				//user right val - nuv_pit1_1_rval_2boxes
				JSONObject urval = new JSONObject();
				urval.put("record", recordId);
				urval.put("redcap_event_name", eventId);
				String urvalFieldName = "nuv_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rval_" + d.getNumOfBoxes() + "boxes";
				urval.put("field_name", urvalFieldName);
				urval.put("value", d.getUserRightVal());
				result.add(urval);
				//comment - nuv_st1_1_comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "nuv_st" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_comment_" + d.getNumOfBoxes() + "boxes";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
				//response time - nuv_pit1_1_rt
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "nuv_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rt_" + d.getNumOfBoxes() + "boxes";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//nuv_pit1_1_acc_2boxes
				JSONObject acc = new JSONObject();
				acc.put("record", recordId);
				acc.put("redcap_event_name", eventId);
				String accFieldName = "nuv_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_acc_" + d.getNumOfBoxes() + "boxes";
				acc.put("field_name", accFieldName);
				acc.put("value", d.getLenientCorrect());
				result.add(acc);
			}	
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailNumberUpdateAuditoryJson(List<DetailNumberUpdateAuditory> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "nua_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "nua_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
		}
		
		for(DetailNumberUpdateAuditory d: details) {
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//stimuli left val - nua_sp1_1_lval_2boxes
				JSONObject lval = new JSONObject();
				lval.put("record", recordId);
				lval.put("redcap_event_name", eventId);
				String lvalFieldName = "nua_sp" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_lval_" + d.getNumOfBoxes() + "boxes";
				lval.put("field_name", lvalFieldName);
				lval.put("value", d.getStimuliLeftValAfter());
				result.add(lval);
				//stimuli middle val - nua_sp1_1_mval_2boxes
				JSONObject mval = new JSONObject();
				mval.put("record", recordId);
				mval.put("redcap_event_name", eventId);
				String mvalFieldName = "nua_sp" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_mval_" + d.getNumOfBoxes() + "boxes";
				mval.put("field_name", mvalFieldName);
				mval.put("value", d.getStimuliMiddleValAfter());
				result.add(mval);
				//stimuli right val - nua_sp1_1_rval_2boxes
				JSONObject rval = new JSONObject();
				rval.put("record", recordId);
				rval.put("redcap_event_name", eventId);
				String rvalFieldName = "nua_sp" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rval_" + d.getNumOfBoxes() + "boxes";
				rval.put("field_name", rvalFieldName);
				rval.put("value", d.getStimuliRightValAfter());
				result.add(rval);
				//user left val - nua_pip1_1_lval_2boxes
				JSONObject ulval = new JSONObject();
				ulval.put("record", recordId);
				ulval.put("redcap_event_name", eventId);
				String ulvalFieldName = "nua_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_lval_" + d.getNumOfBoxes() + "boxes";
				ulval.put("field_name", ulvalFieldName);
				ulval.put("value", d.getUserLeftVal());
				result.add(ulval);
				//user middle val - nua_pip1_1_mval_2boxes
				JSONObject umval = new JSONObject();
				umval.put("record", recordId);
				umval.put("redcap_event_name", eventId);
				String umvalFieldName = "nua_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_mval_" + d.getNumOfBoxes() + "boxes";
				umval.put("field_name", umvalFieldName);
				umval.put("value", d.getUserMiddleVal());
				result.add(umval);
				//user right val - nua_pip1_1_rval_2boxes
				JSONObject urval = new JSONObject();
				urval.put("record", recordId);
				urval.put("redcap_event_name", eventId);
				String urvalFieldName = "nua_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rval_" + d.getNumOfBoxes() + "boxes";
				urval.put("field_name", urvalFieldName);
				urval.put("value", d.getUserRightVal());
				result.add(urval);
				//comment - nua_sp1_1_comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "nua_sp" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_comment_" + d.getNumOfBoxes() + "boxes";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
				//response time - nua_pip1_1_rt
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "nua_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rt_" + d.getNumOfBoxes() + "boxes";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//nua_pip1_1_acc_2boxes
				JSONObject acc = new JSONObject();
				acc.put("record", recordId);
				acc.put("redcap_event_name", eventId);
				String accFieldName = "nua_pip" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_acc_" + d.getNumOfBoxes() + "boxes";
				acc.put("field_name", accFieldName);
				acc.put("value", d.getLenientCorrect());
				result.add(acc);
			}else {
				//stimuli left val - nua_st1_1_lval_2boxes
				JSONObject lval = new JSONObject();
				lval.put("record", recordId);
				lval.put("redcap_event_name", eventId);
				String lvalFieldName = "nua_st" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_lval_" + d.getNumOfBoxes() + "boxes";
				lval.put("field_name", lvalFieldName);
				lval.put("value", d.getStimuliLeftValAfter());
				result.add(lval);
				//stimuli middle val - nua_st1_1_mval_2boxes
				JSONObject mval = new JSONObject();
				mval.put("record", recordId);
				mval.put("redcap_event_name", eventId);
				String mvalFieldName = "nua_st" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_mval_" + d.getNumOfBoxes() + "boxes";
				mval.put("field_name", mvalFieldName);
				mval.put("value", d.getStimuliMiddleValAfter());
				result.add(mval);
				//stimuli right val - nua_st1_1_rval_2boxes
				JSONObject rval = new JSONObject();
				rval.put("record", recordId);
				rval.put("redcap_event_name", eventId);
				String rvalFieldName = "nua_st" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rval_" + d.getNumOfBoxes() + "boxes";
				rval.put("field_name", rvalFieldName);
				rval.put("value", d.getStimuliRightValAfter());
				result.add(rval);
				//user left val - nua_pit1_1_lval_2boxes
				JSONObject ulval = new JSONObject();
				ulval.put("record", recordId);
				ulval.put("redcap_event_name", eventId);
				String ulvalFieldName = "nua_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_lval_" + d.getNumOfBoxes() + "boxes";
				ulval.put("field_name", ulvalFieldName);
				ulval.put("value", d.getUserLeftVal());
				result.add(ulval);
				//user middle val - nua_pit1_1_mval_2boxes
				JSONObject umval = new JSONObject();
				umval.put("record", recordId);
				umval.put("redcap_event_name", eventId);
				String umvalFieldName = "nua_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_mval_" + d.getNumOfBoxes() + "boxes";
				umval.put("field_name", umvalFieldName);
				umval.put("value", d.getUserMiddleVal());
				result.add(umval);
				//user right val - nua_pit1_1_rval_2boxes
				JSONObject urval = new JSONObject();
				urval.put("record", recordId);
				urval.put("redcap_event_name", eventId);
				String urvalFieldName = "nua_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rval_" + d.getNumOfBoxes() + "boxes";
				urval.put("field_name", urvalFieldName);
				urval.put("value", d.getUserRightVal());
				result.add(urval);
				//comment - nua_st1_1_comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "nua_st" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_comment_" + d.getNumOfBoxes() + "boxes";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
				//response time - nua_pit1_1_rt
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "nua_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_rt_" + d.getNumOfBoxes() + "boxes";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//nua_pit1_1_acc_2boxes
				JSONObject acc = new JSONObject();
				acc.put("record", recordId);
				acc.put("redcap_event_name", eventId);
				String accFieldName = "nua_pit" + d.getBlockIndex() + "_" + d.getTrialIndex() + "_acc_" + d.getNumOfBoxes() + "boxes";
				acc.put("field_name", accFieldName);
				acc.put("value", d.getLenientCorrect());
				result.add(acc);
			}	
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailNonWordJson(List<DetailNonWord> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "nr_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "nr_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
		}
		for(DetailNonWord d: details) {
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//stimuli input
				JSONObject sp_list = new JSONObject();
				sp_list.put("record", recordId);
				sp_list.put("redcap_event_name", eventId);
				String spFieldName = "nr_sp" + d.getTrialNo();
				sp_list.put("field_name", spFieldName);
				sp_list.put("value", d.getStimuliNonWordInput());
				result.add(sp_list);
				//user input file
				JSONObject pip_list = new JSONObject();
				pip_list.put("record", recordId);
				pip_list.put("redcap_event_name", eventId);
				String pipListFieldName = "nr_sp" + d.getTrialNo() + "_filename";
				pip_list.put("field_name", pipListFieldName);
				pip_list.put("value", d.getUserInputFileName());
				result.add(pip_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "nr_pip" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
			}else {
				//stimuli input
				JSONObject st_list = new JSONObject();
				st_list.put("record", recordId);
				st_list.put("redcap_event_name", eventId);
				String stListFieldName = "nr_st" + d.getTrialNo();
				st_list.put("field_name", stListFieldName);
				st_list.put("value", d.getStimuliNonWordInput());
				result.add(st_list);
				//user input
				JSONObject pit_list = new JSONObject();
				pit_list.put("record", recordId);
				pit_list.put("redcap_event_name", eventId);
				String pitListFieldName = "nr_st" + d.getTrialNo() + "_filename";
				pit_list.put("field_name", pitListFieldName);
				//TODO
				pit_list.put("value", d.getUserInputFileName());
				result.add(pit_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "nr_pit" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailLocationSpanRunningJson(List<DetailLocationSpanRunning> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "lsr_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "lsr_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
		}
		for(DetailLocationSpanRunning d: details) {
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//each stimuli digit
				JSONObject sp_1 = new JSONObject();
				sp_1.put("record", recordId);
				sp_1.put("redcap_event_name", eventId);
				String fieldName1 = "lsr_sp" + d.getTrialNo() + "_1";
				sp_1.put("field_name", fieldName1);
				sp_1.put("value", d.getSi1());
				result.add(sp_1);
				JSONObject sp_2 = new JSONObject();
				sp_2.put("record", recordId);
				sp_2.put("redcap_event_name", eventId);
				String fieldName2 = "lsr_sp" + d.getTrialNo() + "_2";
				sp_2.put("field_name", fieldName2);
				sp_2.put("value", d.getSi2());
				result.add(sp_2);
				JSONObject sp_3 = new JSONObject();
				sp_3.put("record", recordId);
				sp_3.put("redcap_event_name", eventId);
				String fieldName3 = "lsr_sp" + d.getTrialNo() + "_3";
				sp_3.put("field_name", fieldName3);
				sp_3.put("value", d.getSi3());
				result.add(sp_3);
				JSONObject sp_4 = new JSONObject();
				sp_4.put("record", recordId);
				sp_4.put("redcap_event_name", eventId);
				String fieldName4 = "lsr_sp" + d.getTrialNo() + "_4";
				sp_4.put("field_name", fieldName4);
				sp_4.put("value", d.getSi4());
				result.add(sp_4);
				JSONObject sp_5 = new JSONObject();
				sp_5.put("record", recordId);
				sp_5.put("redcap_event_name", eventId);
				String fieldName5 = "lsr_sp" + d.getTrialNo() + "_5";
				sp_5.put("field_name", fieldName5);
				sp_5.put("value", d.getSi5());
				result.add(sp_5);
				JSONObject sp_6 = new JSONObject();
				sp_6.put("record", recordId);
				sp_6.put("redcap_event_name", eventId);
				String fieldName6 = "lsr_sp" + d.getTrialNo() + "_6";
				sp_6.put("field_name", fieldName6);
				sp_6.put("value", d.getSi6());
				result.add(sp_6);
				JSONObject sp_7 = new JSONObject();
				sp_7.put("record", recordId);
				sp_7.put("redcap_event_name", eventId);
				String fieldName7 = "lsr_sp" + d.getTrialNo() + "_7";
				sp_7.put("field_name", fieldName7);
				sp_7.put("value", d.getSi7());
				result.add(sp_7);
				JSONObject sp_8 = new JSONObject();
				sp_8.put("record", recordId);
				sp_8.put("redcap_event_name", eventId);
				String fieldName8 = "lsr_sp" + d.getTrialNo() + "_8";
				sp_8.put("field_name", fieldName8);
				sp_8.put("value", d.getSi8());
				result.add(sp_8);
				//stimuli input
				JSONObject sp_list = new JSONObject();
				sp_list.put("record", recordId);
				sp_list.put("redcap_event_name", eventId);
				String spFieldName = "lsr_sp" + d.getTrialNo() + "_list";
				sp_list.put("field_name", spFieldName);
				sp_list.put("value", d.getStimuliInput());
				result.add(sp_list);
				//each user digit
				JSONObject pip_1 = new JSONObject();
				pip_1.put("record", recordId);
				pip_1.put("redcap_event_name", eventId);
				String pipFieldName1 = "lsr_pip" + d.getTrialNo() + "_1";
				pip_1.put("field_name", pipFieldName1);
				pip_1.put("value", d.getUi1());
				result.add(pip_1);
				JSONObject pip_2 = new JSONObject();
				pip_2.put("record", recordId);
				pip_2.put("redcap_event_name", eventId);
				String pipFieldName2 = "lsr_pip" + d.getTrialNo() + "_2";
				pip_2.put("field_name", pipFieldName2);
				pip_2.put("value", d.getUi2());
				result.add(pip_2);
				JSONObject pip_3 = new JSONObject();
				pip_3.put("record", recordId);
				pip_3.put("redcap_event_name", eventId);
				String pipFieldName3 = "lsr_pip" + d.getTrialNo() + "_3";
				pip_3.put("field_name", pipFieldName3);
				pip_3.put("value", d.getUi3());
				result.add(pip_3);
				JSONObject pip_4 = new JSONObject();
				pip_4.put("record", recordId);
				pip_4.put("redcap_event_name", eventId);
				String pipFieldName4 = "lsr_pip" + d.getTrialNo() + "_4";
				pip_4.put("field_name", pipFieldName4);
				pip_4.put("value", d.getUi4());
				result.add(pip_4);
				JSONObject pip_5 = new JSONObject();
				pip_5.put("record", recordId);
				pip_5.put("redcap_event_name", eventId);
				String pipFieldName5 = "lsr_pip" + d.getTrialNo() + "_5";
				pip_5.put("field_name", pipFieldName5);
				pip_5.put("value", d.getUi5());
				result.add(pip_5);
				JSONObject pip_6 = new JSONObject();
				pip_6.put("record", recordId);
				pip_6.put("redcap_event_name", eventId);
				String pipFieldName6 = "lsr_pip" + d.getTrialNo() + "_6";
				pip_6.put("field_name", pipFieldName6);
				pip_6.put("value", d.getUi6());
				result.add(pip_6);
				JSONObject pip_7 = new JSONObject();
				pip_7.put("record", recordId);
				pip_7.put("redcap_event_name", eventId);
				String pipFieldName7 = "lsr_pip" + d.getTrialNo() + "_7";
				pip_7.put("field_name", pipFieldName7);
				pip_7.put("value", d.getUi7());
				result.add(pip_7);
				JSONObject pip_8 = new JSONObject();
				pip_8.put("record", recordId);
				pip_8.put("redcap_event_name", eventId);
				String pipFieldName8 = "lsr_pip" + d.getTrialNo() + "_8";
				pip_8.put("field_name", pipFieldName8);
				pip_8.put("value", d.getUi8());
				result.add(pip_8);
				//user input
				JSONObject pip_list = new JSONObject();
				pip_list.put("record", recordId);
				pip_list.put("redcap_event_name", eventId);
				String pipListFieldName = "lsr_pip" + d.getTrialNo() + "_list";
				pip_list.put("field_name", pipListFieldName);
				pip_list.put("value", d.getUserInput());
				result.add(pip_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "lsr_pip" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject sp_tot = new JSONObject();
				sp_tot.put("record", recordId);
				sp_tot.put("redcap_event_name", eventId);
				String piTotFieldName = "lsr_sp" + d.getTrialNo() + "_tot";
				sp_tot.put("field_name", piTotFieldName);
				sp_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(sp_tot);
				//span
				JSONObject sp_span = new JSONObject();
				sp_span.put("record", recordId);
				sp_span.put("redcap_event_name", eventId);
				String piSpanFieldName = "lsr_sp" + d.getTrialNo() + "_span";
				sp_span.put("field_name", piSpanFieldName);
				sp_span.put("value", d.getNumOfDigitsCorrectFromEnd());
				result.add(sp_span);
				//accuracy
				JSONObject sp_acc = new JSONObject();
				sp_acc.put("record", recordId);
				sp_acc.put("redcap_event_name", eventId);
				String piAccFieldName = "lsr_sp" + d.getTrialNo() + "_acc";
				sp_acc.put("field_name", piAccFieldName);
				sp_acc.put("value", d.getScore());
				result.add(sp_acc);
				//comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "lsr_sp" + d.getTrialNo() + "_comment";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
			}else {
				//each stimuli digit
				JSONObject st_1 = new JSONObject();
				st_1.put("record", recordId);
				st_1.put("redcap_event_name", eventId);
				String stFieldName1 = "lsr_st" + d.getTrialNo() + "_1";
				st_1.put("field_name", stFieldName1);
				st_1.put("value", d.getSi1());
				result.add(st_1);
				JSONObject st_2 = new JSONObject();
				st_2.put("record", recordId);
				st_2.put("redcap_event_name", eventId);
				String stFieldName2 = "lsr_st" + d.getTrialNo() + "_2";
				st_2.put("field_name", stFieldName2);
				st_2.put("value", d.getSi2());
				result.add(st_2);
				JSONObject st_3 = new JSONObject();
				st_3.put("record", recordId);
				st_3.put("redcap_event_name", eventId);
				String stFieldName3 = "lsr_st" + d.getTrialNo() + "_3";
				st_3.put("field_name", stFieldName3);
				st_3.put("value", d.getSi3());
				result.add(st_3);
				JSONObject st_4 = new JSONObject();
				st_4.put("record", recordId);
				st_4.put("redcap_event_name", eventId);
				String stFieldName4 = "lsr_st" + d.getTrialNo() + "_4";
				st_4.put("field_name", stFieldName4);
				st_4.put("value", d.getSi4());
				result.add(st_4);
				JSONObject st_5 = new JSONObject();
				st_5.put("record", recordId);
				st_5.put("redcap_event_name", eventId);
				String stFieldName5 = "lsr_st" + d.getTrialNo() + "_5";
				st_5.put("field_name", stFieldName5);
				st_5.put("value", d.getSi5());
				result.add(st_5);
				JSONObject st_6 = new JSONObject();
				st_6.put("record", recordId);
				st_6.put("redcap_event_name", eventId);
				String stFieldName6 = "lsr_st" + d.getTrialNo() + "_6";
				st_6.put("field_name", stFieldName6);
				st_6.put("value", d.getSi6());
				result.add(st_6);
				JSONObject st_7 = new JSONObject();
				st_7.put("record", recordId);
				st_7.put("redcap_event_name", eventId);
				String stFieldName7 = "lsr_st" + d.getTrialNo() + "_7";
				st_7.put("field_name", stFieldName7);
				st_7.put("value", d.getSi7());
				result.add(st_7);
				JSONObject st_8 = new JSONObject();
				st_8.put("record", recordId);
				st_8.put("redcap_event_name", eventId);
				String stFieldName8 = "lsr_st" + d.getTrialNo() + "_8";
				st_8.put("field_name", stFieldName8);
				st_8.put("value", d.getSi8());
				result.add(st_8);
				//stimuli input
				JSONObject st_list = new JSONObject();
				st_list.put("record", recordId);
				st_list.put("redcap_event_name", eventId);
				String stListFieldName = "lsr_st" + d.getTrialNo() + "_list";
				st_list.put("field_name", stListFieldName);
				st_list.put("value", d.getStimuliInput());
				result.add(st_list);
				//each user digit
				JSONObject pit_1 = new JSONObject();
				pit_1.put("record", recordId);
				pit_1.put("redcap_event_name", eventId);
				String pitFieldName1 = "lsr_pit" + d.getTrialNo() + "_1";
				pit_1.put("field_name", pitFieldName1);
				pit_1.put("value", d.getUi1());
				result.add(pit_1);
				JSONObject pit_2 = new JSONObject();
				pit_2.put("record", recordId);
				pit_2.put("redcap_event_name", eventId);
				String pitFieldName2 = "lsr_pit" + d.getTrialNo() + "_2";
				pit_2.put("field_name", pitFieldName2);
				pit_2.put("value", d.getUi2());
				result.add(pit_2);
				JSONObject pit_3 = new JSONObject();
				pit_3.put("record", recordId);
				pit_3.put("redcap_event_name", eventId);
				String pitFieldName3 = "lsr_pit" + d.getTrialNo() + "_3";
				pit_3.put("field_name", pitFieldName3);
				pit_3.put("value", d.getUi3());
				result.add(pit_3);
				JSONObject pit_4 = new JSONObject();
				pit_4.put("record", recordId);
				pit_4.put("redcap_event_name", eventId);
				String pitFieldName4 = "lsr_pit" + d.getTrialNo() + "_4";
				pit_4.put("field_name", pitFieldName4);
				pit_4.put("value", d.getUi4());
				result.add(pit_4);
				JSONObject pit_5 = new JSONObject();
				pit_5.put("record", recordId);
				pit_5.put("redcap_event_name", eventId);
				String pitFieldName5 = "lsr_pit" + d.getTrialNo() + "_5";
				pit_5.put("field_name", pitFieldName5);
				pit_5.put("value", d.getUi5());
				result.add(pit_5);
				JSONObject pit_6 = new JSONObject();
				pit_6.put("record", recordId);
				pit_6.put("redcap_event_name", eventId);
				String pitFieldName6 = "lsr_pit" + d.getTrialNo() + "_6";
				pit_6.put("field_name", pitFieldName6);
				pit_6.put("value", d.getUi6());
				result.add(pit_6);
				JSONObject pit_7 = new JSONObject();
				pit_7.put("record", recordId);
				pit_7.put("redcap_event_name", eventId);
				String pitFieldName7 = "lsr_pit" + d.getTrialNo() + "_7";
				pit_7.put("field_name", pitFieldName7);
				pit_7.put("value", d.getUi7());
				result.add(pit_7);
				JSONObject pit_8 = new JSONObject();
				pit_8.put("record", recordId);
				pit_8.put("redcap_event_name", eventId);
				String pitFieldName8 = "lsr_pit" + d.getTrialNo() + "_8";
				pit_8.put("field_name", pitFieldName8);
				pit_8.put("value", d.getUi8());
				result.add(pit_8);
				//user input
				JSONObject pit_list = new JSONObject();
				pit_list.put("record", recordId);
				pit_list.put("redcap_event_name", eventId);
				String pitListFieldName = "lsr_pit" + d.getTrialNo() + "_list";
				pit_list.put("field_name", pitListFieldName);
				pit_list.put("value", d.getUserInput());
				result.add(pit_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "lsr_pit" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject st_tot = new JSONObject();
				st_tot.put("record", recordId);
				st_tot.put("redcap_event_name", eventId);
				String tiTotFieldName = "lsr_st" + d.getTrialNo() + "_tot";
				st_tot.put("field_name", tiTotFieldName);
				st_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(st_tot);
				//span
				JSONObject st_span = new JSONObject();
				st_span.put("record", recordId);
				st_span.put("redcap_event_name", eventId);
				String tiSpanFieldName = "lsr_st" + d.getTrialNo() + "_span";
				st_span.put("field_name", tiSpanFieldName);
				st_span.put("value", d.getNumOfDigitsCorrectFromEnd());
				result.add(st_span);
				//accuracy
				JSONObject st_acc = new JSONObject();
				st_acc.put("record", recordId);
				st_acc.put("redcap_event_name", eventId);
				String siAccFieldName = "lsr_st" + d.getTrialNo() + "_acc";
				st_acc.put("field_name", siAccFieldName);
				st_acc.put("value", d.getScore());
				result.add(st_acc);
				//comment
				JSONObject st_comment = new JSONObject();
				st_comment.put("record", recordId);
				st_comment.put("redcap_event_name", eventId);
				String siCommentFieldName = "lsr_st" + d.getTrialNo() + "_comment";
				st_comment.put("field_name", siCommentFieldName);
				st_comment.put("value", d.getComment());
				result.add(st_comment);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailLocationSpanJson(List<DetailLocationSpan> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "ls_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "ls_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
		}
		
		for(DetailLocationSpan d: details) {
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//each stimuli digit
				JSONObject sp_1 = new JSONObject();
				sp_1.put("record", recordId);
				sp_1.put("redcap_event_name", eventId);
				String fieldName1 = "ls_sp" + d.getTrialNo() + "_1";
				sp_1.put("field_name", fieldName1);
				sp_1.put("value", d.getSi1());
				result.add(sp_1);
				if(d.getTrialNo() > 1) {
					JSONObject sp_2 = new JSONObject();
					sp_2.put("record", recordId);
					sp_2.put("redcap_event_name", eventId);
					String fieldName2 = "ls_sp" + d.getTrialNo() + "_2";
					sp_2.put("field_name", fieldName2);
					sp_2.put("value", d.getSi2());
					result.add(sp_2);
				}
				//stimuli input
				JSONObject sp_list = new JSONObject();
				sp_list.put("record", recordId);
				sp_list.put("redcap_event_name", eventId);
				String spFieldName = "ls_sp" + d.getTrialNo() + "_list";
				sp_list.put("field_name", spFieldName);
				sp_list.put("value", d.getStimuliInput());
				result.add(sp_list);
				//each user digit
				JSONObject pip_1 = new JSONObject();
				pip_1.put("record", recordId);
				pip_1.put("redcap_event_name", eventId);
				String pipFieldName1 = "ls_pip" + d.getTrialNo() + "_1";
				pip_1.put("field_name", pipFieldName1);
				pip_1.put("value", d.getUi1());
				result.add(pip_1);
				if(d.getTrialNo() > 1) {
					JSONObject pip_2 = new JSONObject();
					pip_2.put("record", recordId);
					pip_2.put("redcap_event_name", eventId);
					String pipFieldName2 = "ls_pip" + d.getTrialNo() + "_2";
					pip_2.put("field_name", pipFieldName2);
					pip_2.put("value", d.getUi2());
					result.add(pip_2);
				}
				//user input
				JSONObject pip_list = new JSONObject();
				pip_list.put("record", recordId);
				pip_list.put("redcap_event_name", eventId);
				String pipListFieldName = "ls_pip" + d.getTrialNo() + "_list";
				pip_list.put("field_name", pipListFieldName);
				pip_list.put("value", d.getUserInput());
				result.add(pip_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "ls_pip" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject sp_tot = new JSONObject();
				sp_tot.put("record", recordId);
				sp_tot.put("redcap_event_name", eventId);
				String piTotFieldName = "ls_sp" + d.getTrialNo() + "_tot";
				sp_tot.put("field_name", piTotFieldName);
				sp_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(sp_tot);
				//accuracy
				JSONObject sp_acc = new JSONObject();
				sp_acc.put("record", recordId);
				sp_acc.put("redcap_event_name", eventId);
				String piAccFieldName = "ls_sp" + d.getTrialNo() + "_acc";
				sp_acc.put("field_name", piAccFieldName);
				sp_acc.put("value", d.getScore());
				result.add(sp_acc);
				//comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "ls_sp" + d.getTrialNo() + "_comment";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
			}else {
				//each stimuli digit
				JSONObject st_1 = new JSONObject();
				st_1.put("record", recordId);
				st_1.put("redcap_event_name", eventId);
				String stFieldName1 = "ls_st" + d.getTrialNo() + "_1";
				st_1.put("field_name", stFieldName1);
				st_1.put("value", d.getSi1());
				result.add(st_1);
				JSONObject st_2 = new JSONObject();
				st_2.put("record", recordId);
				st_2.put("redcap_event_name", eventId);
				String stFieldName2 = "ls_st" + d.getTrialNo() + "_2";
				st_2.put("field_name", stFieldName2);
				st_2.put("value", d.getSi2());
				result.add(st_2);
				JSONObject st_3 = new JSONObject();
				st_3.put("record", recordId);
				st_3.put("redcap_event_name", eventId);
				String stFieldName3 = "ls_st" + d.getTrialNo() + "_3";
				st_3.put("field_name", stFieldName3);
				st_3.put("value", d.getSi3());
				result.add(st_3);
				JSONObject st_4 = new JSONObject();
				st_4.put("record", recordId);
				st_4.put("redcap_event_name", eventId);
				String stFieldName4 = "ls_st" + d.getTrialNo() + "_4";
				st_4.put("field_name", stFieldName4);
				st_4.put("value", d.getSi4());
				result.add(st_4);
				JSONObject st_5 = new JSONObject();
				st_5.put("record", recordId);
				st_5.put("redcap_event_name", eventId);
				String stFieldName5 = "ls_st" + d.getTrialNo() + "_5";
				st_5.put("field_name", stFieldName5);
				st_5.put("value", d.getSi5());
				result.add(st_5);
				JSONObject st_6 = new JSONObject();
				st_6.put("record", recordId);
				st_6.put("redcap_event_name", eventId);
				String stFieldName6 = "ls_st" + d.getTrialNo() + "_6";
				st_6.put("field_name", stFieldName6);
				st_6.put("value", d.getSi6());
				result.add(st_6);
				//stimuli input
				JSONObject st_list = new JSONObject();
				st_list.put("record", recordId);
				st_list.put("redcap_event_name", eventId);
				String stListFieldName = "ls_st" + d.getTrialNo() + "_list";
				st_list.put("field_name", stListFieldName);
				st_list.put("value", d.getStimuliInput());
				result.add(st_list);
				//each user digit
				JSONObject pit_1 = new JSONObject();
				pit_1.put("record", recordId);
				pit_1.put("redcap_event_name", eventId);
				String pitFieldName1 = "ls_pit" + d.getTrialNo() + "_1";
				pit_1.put("field_name", pitFieldName1);
				pit_1.put("value", d.getUi1());
				result.add(pit_1);
				JSONObject pit_2 = new JSONObject();
				pit_2.put("record", recordId);
				pit_2.put("redcap_event_name", eventId);
				String pitFieldName2 = "ls_pit" + d.getTrialNo() + "_2";
				pit_2.put("field_name", pitFieldName2);
				pit_2.put("value", d.getUi2());
				result.add(pit_2);
				JSONObject pit_3 = new JSONObject();
				pit_3.put("record", recordId);
				pit_3.put("redcap_event_name", eventId);
				String pitFieldName3 = "ls_pit" + d.getTrialNo() + "_3";
				pit_3.put("field_name", pitFieldName3);
				pit_3.put("value", d.getUi3());
				result.add(pit_3);
				JSONObject pit_4 = new JSONObject();
				pit_4.put("record", recordId);
				pit_4.put("redcap_event_name", eventId);
				String pitFieldName4 = "ls_pit" + d.getTrialNo() + "_4";
				pit_4.put("field_name", pitFieldName4);
				pit_4.put("value", d.getUi4());
				result.add(pit_4);
				JSONObject pit_5 = new JSONObject();
				pit_5.put("record", recordId);
				pit_5.put("redcap_event_name", eventId);
				String pitFieldName5 = "ls_pit" + d.getTrialNo() + "_5";
				pit_5.put("field_name", pitFieldName5);
				pit_5.put("value", d.getUi5());
				result.add(pit_5);
				JSONObject pit_6 = new JSONObject();
				pit_6.put("record", recordId);
				pit_6.put("redcap_event_name", eventId);
				String pitFieldName6 = "ls_pit" + d.getTrialNo() + "_6";
				pit_6.put("field_name", pitFieldName6);
				pit_6.put("value", d.getUi6());
				result.add(pit_6);
				//user input
				JSONObject pit_list = new JSONObject();
				pit_list.put("record", recordId);
				pit_list.put("redcap_event_name", eventId);
				String pitListFieldName = "ls_pit" + d.getTrialNo() + "_list";
				pit_list.put("field_name", pitListFieldName);
				pit_list.put("value", d.getUserInput());
				result.add(pit_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "ls_pit" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject st_tot = new JSONObject();
				st_tot.put("record", recordId);
				st_tot.put("redcap_event_name", eventId);
				String tiTotFieldName = "ls_st" + d.getTrialNo() + "_tot";
				st_tot.put("field_name", tiTotFieldName);
				st_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(st_tot);
				//accuracy
				JSONObject st_acc = new JSONObject();
				st_acc.put("record", recordId);
				st_acc.put("redcap_event_name", eventId);
				String siAccFieldName = "ls_st" + d.getTrialNo() + "_acc";
				st_acc.put("field_name", siAccFieldName);
				st_acc.put("value", d.getScore());
				result.add(st_acc);
				//comment
				JSONObject st_comment = new JSONObject();
				st_comment.put("record", recordId);
				st_comment.put("redcap_event_name", eventId);
				String siCommentFieldName = "ls_st" + d.getTrialNo() + "_comment";
				st_comment.put("field_name", siCommentFieldName);
				st_comment.put("value", d.getComment());
				result.add(st_comment);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailDigitSpanRunningJson(List<DetailDigitSpanRunning> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "dsr_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "dsr_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
		}
		
		for(DetailDigitSpanRunning d: details) {
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//each stimuli digit
				JSONObject sp_1 = new JSONObject();
				sp_1.put("record", recordId);
				sp_1.put("redcap_event_name", eventId);
				String spFieldName1 = "dsr_sp" + d.getTrialNo() + "_1";
				sp_1.put("field_name", spFieldName1);
				sp_1.put("value", d.getSi1());
				result.add(sp_1);
				JSONObject sp_2 = new JSONObject();
				sp_2.put("record", recordId);
				sp_2.put("redcap_event_name", eventId);
				String spFieldName2 = "dsr_sp" + d.getTrialNo() + "_2";
				sp_2.put("field_name", spFieldName2);
				sp_2.put("value", d.getSi2());
				result.add(sp_2);
				JSONObject sp_3 = new JSONObject();
				sp_3.put("record", recordId);
				sp_3.put("redcap_event_name", eventId);
				String spFieldName3 = "dsr_sp" + d.getTrialNo() + "_3";
				sp_3.put("field_name", spFieldName3);
				sp_3.put("value", d.getSi3());
				result.add(sp_3);
				JSONObject sp_4 = new JSONObject();
				sp_4.put("record", recordId);
				sp_4.put("redcap_event_name", eventId);
				String spFieldName4 = "dsr_sp" + d.getTrialNo() + "_4";
				sp_4.put("field_name", spFieldName4);
				sp_4.put("value", d.getSi4());
				result.add(sp_4);
				JSONObject sp_5 = new JSONObject();
				sp_5.put("record", recordId);
				sp_5.put("redcap_event_name", eventId);
				String spFieldName5 = "dsr_sp" + d.getTrialNo() + "_5";
				sp_5.put("field_name", spFieldName5);
				sp_5.put("value", d.getSi5());
				result.add(sp_5);
				JSONObject sp_6 = new JSONObject();
				sp_6.put("record", recordId);
				sp_6.put("redcap_event_name", eventId);
				String spFieldName6 = "dsr_sp" + d.getTrialNo() + "_6";
				sp_6.put("field_name", spFieldName6);
				sp_6.put("value", d.getSi6());
				result.add(sp_6);
				JSONObject sp_7 = new JSONObject();
				sp_7.put("record", recordId);
				sp_7.put("redcap_event_name", eventId);
				String spFieldName7 = "dsr_sp" + d.getTrialNo() + "_7";
				sp_7.put("field_name", spFieldName7);
				sp_7.put("value", d.getSi7());
				result.add(sp_7);
				JSONObject sp_8 = new JSONObject();
				sp_8.put("record", recordId);
				sp_8.put("redcap_event_name", eventId);
				String spFieldName8 = "dsr_sp" + d.getTrialNo() + "_8";
				sp_8.put("field_name", spFieldName8);
				sp_8.put("value", d.getSi8());
				result.add(sp_8);
				JSONObject sp_9 = new JSONObject();
				sp_9.put("record", recordId);
				sp_9.put("redcap_event_name", eventId);
				String spFieldName9 = "dsr_sp" + d.getTrialNo() + "_9";
				sp_9.put("field_name", spFieldName9);
				sp_9.put("value", d.getSi9());
				result.add(sp_9);
				JSONObject sp_10 = new JSONObject();
				sp_10.put("record", recordId);
				sp_10.put("redcap_event_name", eventId);
				String spFieldName10 = "dsr_sp" + d.getTrialNo() + "_10";
				sp_10.put("field_name", spFieldName10);
				sp_10.put("value", d.getSi10());
				result.add(sp_10);
				//stimuli input
				JSONObject sp_list = new JSONObject();
				sp_list.put("record", recordId);
				sp_list.put("redcap_event_name", eventId);
				String spFieldName = "dsr_sp" + d.getTrialNo() + "_list";
				sp_list.put("field_name", spFieldName);
				sp_list.put("value", d.getStimuliInput());
				result.add(sp_list);
				//each user digit
				JSONObject pip_1 = new JSONObject();
				pip_1.put("record", recordId);
				pip_1.put("redcap_event_name", eventId);
				String pipFieldName1 = "dsr_pip" + d.getTrialNo() + "_1";
				pip_1.put("field_name", pipFieldName1);
				pip_1.put("value", d.getUi1());
				result.add(pip_1);
				JSONObject pip_2 = new JSONObject();
				pip_2.put("record", recordId);
				pip_2.put("redcap_event_name", eventId);
				String pipFieldName2 = "dsr_pip" + d.getTrialNo() + "_2";
				pip_2.put("field_name", pipFieldName2);
				pip_2.put("value", d.getUi2());
				result.add(pip_2);
				JSONObject pip_3 = new JSONObject();
				pip_3.put("record", recordId);
				pip_3.put("redcap_event_name", eventId);
				String pipFieldName3 = "dsr_pip" + d.getTrialNo() + "_3";
				pip_3.put("field_name", pipFieldName3);
				pip_3.put("value", d.getUi3());
				result.add(pip_3);
				JSONObject pip_4 = new JSONObject();
				pip_4.put("record", recordId);
				pip_4.put("redcap_event_name", eventId);
				String pipFieldName4 = "dsr_pip" + d.getTrialNo() + "_4";
				pip_4.put("field_name", pipFieldName4);
				pip_4.put("value", d.getUi4());
				result.add(pip_4);
				JSONObject pip_5 = new JSONObject();
				pip_5.put("record", recordId);
				pip_5.put("redcap_event_name", eventId);
				String pipFieldName5 = "dsr_pip" + d.getTrialNo() + "_5";
				pip_5.put("field_name", pipFieldName5);
				pip_5.put("value", d.getUi5());
				result.add(pip_5);
				JSONObject pip_6 = new JSONObject();
				pip_6.put("record", recordId);
				pip_6.put("redcap_event_name", eventId);
				String pipFieldName6 = "dsr_pip" + d.getTrialNo() + "_6";
				pip_6.put("field_name", pipFieldName6);
				pip_6.put("value", d.getUi6());
				result.add(pip_6);
				JSONObject pip_7 = new JSONObject();
				pip_7.put("record", recordId);
				pip_7.put("redcap_event_name", eventId);
				String pipFieldName7 = "dsr_pip" + d.getTrialNo() + "_7";
				pip_7.put("field_name", pipFieldName7);
				pip_7.put("value", d.getUi7());
				result.add(pip_7);
				JSONObject pip_8 = new JSONObject();
				pip_8.put("record", recordId);
				pip_8.put("redcap_event_name", eventId);
				String pipFieldName8 = "dsr_pip" + d.getTrialNo() + "_8";
				pip_8.put("field_name", pipFieldName8);
				pip_8.put("value", d.getUi8());
				result.add(pip_8);
				JSONObject pip_9 = new JSONObject();
				pip_9.put("record", recordId);
				pip_9.put("redcap_event_name", eventId);
				String pipFieldName9 = "dsr_pip" + d.getTrialNo() + "_9";
				pip_9.put("field_name", pipFieldName9);
				pip_9.put("value", d.getUi9());
				result.add(pip_9);
				JSONObject pip_10 = new JSONObject();
				pip_10.put("record", recordId);
				pip_10.put("redcap_event_name", eventId);
				String pipFieldName10 = "dsr_pip" + d.getTrialNo() + "_10";
				pip_10.put("field_name", pipFieldName10);
				pip_10.put("value", d.getUi10());
				result.add(pip_10);
				//user input
				JSONObject pip_list = new JSONObject();
				pip_list.put("record", recordId);
				pip_list.put("redcap_event_name", eventId);
				String pipListFieldName = "dsr_pip" + d.getTrialNo() + "_list";
				pip_list.put("field_name", pipListFieldName);
				pip_list.put("value", d.getUserInput());
				result.add(pip_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "dsr_pip" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject sp_tot = new JSONObject();
				sp_tot.put("record", recordId);
				sp_tot.put("redcap_event_name", eventId);
				String piTotFieldName = "dsr_sp" + d.getTrialNo() + "_tot";
				sp_tot.put("field_name", piTotFieldName);
				sp_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(sp_tot);
				//span
				JSONObject sp_span = new JSONObject();
				sp_span.put("record", recordId);
				sp_span.put("redcap_event_name", eventId);
				String piSpanFieldName = "dsr_sp" + d.getTrialNo() + "_span";
				sp_span.put("field_name", piSpanFieldName);
				sp_span.put("value", d.getNumOfDigitsCorrectFromEnd());
				result.add(sp_span);
				//accuracy
				JSONObject sp_acc = new JSONObject();
				sp_acc.put("record", recordId);
				sp_acc.put("redcap_event_name", eventId);
				String piAccFieldName = "dsr_sp" + d.getTrialNo() + "_acc";
				sp_acc.put("field_name", piAccFieldName);
				sp_acc.put("value", d.getScore());
				result.add(sp_acc);
				//comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "dsr_sp" + d.getTrialNo() + "_comment";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
			}else {
				//each stimuli digit
				JSONObject st_1 = new JSONObject();
				st_1.put("record", recordId);
				st_1.put("redcap_event_name", eventId);
				String stFieldName1 = "dsr_st" + d.getTrialNo() + "_1";
				st_1.put("field_name", stFieldName1);
				st_1.put("value", d.getSi1());
				result.add(st_1);
				JSONObject st_2 = new JSONObject();
				st_2.put("record", recordId);
				st_2.put("redcap_event_name", eventId);
				String stFieldName2 = "dsr_st" + d.getTrialNo() + "_2";
				st_2.put("field_name", stFieldName2);
				st_2.put("value", d.getSi2());
				result.add(st_2);
				JSONObject st_3 = new JSONObject();
				st_3.put("record", recordId);
				st_3.put("redcap_event_name", eventId);
				String stFieldName3 = "dsr_st" + d.getTrialNo() + "_3";
				st_3.put("field_name", stFieldName3);
				st_3.put("value", d.getSi3());
				result.add(st_3);
				JSONObject st_4 = new JSONObject();
				st_4.put("record", recordId);
				st_4.put("redcap_event_name", eventId);
				String stFieldName4 = "dsr_st" + d.getTrialNo() + "_4";
				st_4.put("field_name", stFieldName4);
				st_4.put("value", d.getSi4());
				result.add(st_4);
				JSONObject st_5 = new JSONObject();
				st_5.put("record", recordId);
				st_5.put("redcap_event_name", eventId);
				String stFieldName5 = "dsr_st" + d.getTrialNo() + "_5";
				st_5.put("field_name", stFieldName5);
				st_5.put("value", d.getSi2());
				result.add(st_5);
				JSONObject st_6 = new JSONObject();
				st_6.put("record", recordId);
				st_6.put("redcap_event_name", eventId);
				String stFieldName6 = "dsr_st" + d.getTrialNo() + "_6";
				st_6.put("field_name", stFieldName6);
				st_6.put("value", d.getSi6());
				result.add(st_6);
				JSONObject st_7 = new JSONObject();
				st_7.put("record", recordId);
				st_7.put("redcap_event_name", eventId);
				String stFieldName7 = "dsr_st" + d.getTrialNo() + "_7";
				st_7.put("field_name", stFieldName7);
				st_7.put("value", d.getSi7());
				result.add(st_7);
				JSONObject st_8 = new JSONObject();
				st_8.put("record", recordId);
				st_8.put("redcap_event_name", eventId);
				String stFieldName8 = "dsr_st" + d.getTrialNo() + "_8";
				st_8.put("field_name", stFieldName8);
				st_8.put("value", d.getSi8());
				result.add(st_8);
				JSONObject st_9 = new JSONObject();
				st_9.put("record", recordId);
				st_9.put("redcap_event_name", eventId);
				String stFieldName9 = "dsr_st" + d.getTrialNo() + "_9";
				st_9.put("field_name", stFieldName9);
				st_9.put("value", d.getSi9());
				result.add(st_9);
				JSONObject st_10 = new JSONObject();
				st_10.put("record", recordId);
				st_10.put("redcap_event_name", eventId);
				String stFieldName10 = "dsr_st" + d.getTrialNo() + "_10";
				st_10.put("field_name", stFieldName10);
				st_10.put("value", d.getSi10());
				result.add(st_10);
				//stimuli input
				JSONObject st_list = new JSONObject();
				st_list.put("record", recordId);
				st_list.put("redcap_event_name", eventId);
				String stListFieldName = "dsr_st" + d.getTrialNo() + "_list";
				st_list.put("field_name", stListFieldName);
				st_list.put("value", d.getStimuliInput());
				result.add(st_list);
				//each user digit
				JSONObject pit_1 = new JSONObject();
				pit_1.put("record", recordId);
				pit_1.put("redcap_event_name", eventId);
				String pitFieldName1 = "dsr_pit" + d.getTrialNo() + "_1";
				pit_1.put("field_name", pitFieldName1);
				pit_1.put("value", d.getUi1());
				result.add(pit_1);
				JSONObject pit_2 = new JSONObject();
				pit_2.put("record", recordId);
				pit_2.put("redcap_event_name", eventId);
				String pitFieldName2 = "dsr_pit" + d.getTrialNo() + "_2";
				pit_2.put("field_name", pitFieldName2);
				pit_2.put("value", d.getUi2());
				result.add(pit_2);
				JSONObject pit_3 = new JSONObject();
				pit_3.put("record", recordId);
				pit_3.put("redcap_event_name", eventId);
				String pitFieldName3 = "dsr_pit" + d.getTrialNo() + "_3";
				pit_3.put("field_name", pitFieldName3);
				pit_3.put("value", d.getUi3());
				result.add(pit_3);
				JSONObject pit_4 = new JSONObject();
				pit_4.put("record", recordId);
				pit_4.put("redcap_event_name", eventId);
				String pitFieldName4 = "dsr_pit" + d.getTrialNo() + "_4";
				pit_4.put("field_name", pitFieldName4);
				pit_4.put("value", d.getUi4());
				result.add(pit_4);
				JSONObject pit_5 = new JSONObject();
				pit_5.put("record", recordId);
				pit_5.put("redcap_event_name", eventId);
				String pitFieldName5 = "dsr_pit" + d.getTrialNo() + "_5";
				pit_5.put("field_name", pitFieldName5);
				pit_5.put("value", d.getUi5());
				result.add(pit_5);
				JSONObject pit_6 = new JSONObject();
				pit_6.put("record", recordId);
				pit_6.put("redcap_event_name", eventId);
				String pitFieldName6 = "dsr_pit" + d.getTrialNo() + "_6";
				pit_6.put("field_name", pitFieldName6);
				pit_6.put("value", d.getUi6());
				result.add(pit_6);
				JSONObject pit_7 = new JSONObject();
				pit_7.put("record", recordId);
				pit_7.put("redcap_event_name", eventId);
				String pitFieldName7 = "dsr_pit" + d.getTrialNo() + "_7";
				pit_7.put("field_name", pitFieldName7);
				pit_7.put("value", d.getUi7());
				result.add(pit_7);
				JSONObject pit_8 = new JSONObject();
				pit_8.put("record", recordId);
				pit_8.put("redcap_event_name", eventId);
				String pitFieldName8 = "dsr_pit" + d.getTrialNo() + "_8";
				pit_8.put("field_name", pitFieldName8);
				pit_8.put("value", d.getUi8());
				result.add(pit_8);
				JSONObject pit_9 = new JSONObject();
				pit_9.put("record", recordId);
				pit_9.put("redcap_event_name", eventId);
				String pitFieldName9 = "dsr_pit" + d.getTrialNo() + "_9";
				pit_9.put("field_name", pitFieldName9);
				pit_9.put("value", d.getUi9());
				result.add(pit_9);
				JSONObject pit_10 = new JSONObject();
				pit_10.put("record", recordId);
				pit_10.put("redcap_event_name", eventId);
				String pitFieldName10 = "dsr_pit" + d.getTrialNo() + "_10";
				pit_10.put("field_name", pitFieldName10);
				pit_10.put("value", d.getUi10());
				result.add(pit_10);
				//user input
				JSONObject pit_list = new JSONObject();
				pit_list.put("record", recordId);
				pit_list.put("redcap_event_name", eventId);
				String pitListFieldName = "dsr_pit" + d.getTrialNo() + "_list";
				pit_list.put("field_name", pitListFieldName);
				pit_list.put("value", d.getUserInput());
				result.add(pit_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "dsr_pit" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject st_tot = new JSONObject();
				st_tot.put("record", recordId);
				st_tot.put("redcap_event_name", eventId);
				String tiTotFieldName = "dsr_st" + d.getTrialNo() + "_tot";
				st_tot.put("field_name", tiTotFieldName);
				st_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(st_tot);
				//span
				JSONObject st_span = new JSONObject();
				st_span.put("record", recordId);
				st_span.put("redcap_event_name", eventId);
				String tiSpanFieldName = "dsr_st" + d.getTrialNo() + "_span";
				st_span.put("field_name", tiSpanFieldName);
				st_span.put("value", d.getNumOfDigitsCorrectFromEnd());
				result.add(st_span);
				//accuracy
				JSONObject st_acc = new JSONObject();
				st_acc.put("record", recordId);
				st_acc.put("redcap_event_name", eventId);
				String siAccFieldName = "dsr_st" + d.getTrialNo() + "_acc";
				st_acc.put("field_name", siAccFieldName);
				st_acc.put("value", d.getScore());
				result.add(st_acc);
				//comment
				JSONObject st_comment = new JSONObject();
				st_comment.put("record", recordId);
				st_comment.put("redcap_event_name", eventId);
				String siCommentFieldName = "dsr_st" + d.getTrialNo() + "_comment";
				st_comment.put("field_name", siCommentFieldName);
				st_comment.put("value", d.getComment());
				result.add(st_comment);
			}
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray generateDetailDigitSpanJson(List<DetailDigitSpan> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "ds_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "ds_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
			
		}
		
		for(DetailDigitSpan d: details) {
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//each stimuli digit
				JSONObject sp_1 = new JSONObject();
				sp_1.put("record", recordId);
				sp_1.put("redcap_event_name", eventId);
				String fieldName1 = "ds_sp" + d.getTrialNo() + "_1";
				sp_1.put("field_name", fieldName1);
				sp_1.put("value", d.getSi1());
				result.add(sp_1);
				if(d.getTrialNo() > 1) {
					JSONObject sp_2 = new JSONObject();
					sp_2.put("record", recordId);
					sp_2.put("redcap_event_name", eventId);
					String fieldName2 = "ds_sp" + d.getTrialNo() + "_2";
					sp_2.put("field_name", fieldName2);
					sp_2.put("value", d.getSi2());
					result.add(sp_2);
				}
				//stimuli input
				JSONObject sp_list = new JSONObject();
				sp_list.put("record", recordId);
				sp_list.put("redcap_event_name", eventId);
				String spFieldName = "ds_sp" + d.getTrialNo() + "_list";
				sp_list.put("field_name", spFieldName);
				sp_list.put("value", d.getStimuliInput());
				result.add(sp_list);
				//each user digit
				JSONObject pip_1 = new JSONObject();
				pip_1.put("record", recordId);
				pip_1.put("redcap_event_name", eventId);
				String pipFieldName1 = "ds_pip" + d.getTrialNo() + "_1";
				pip_1.put("field_name", pipFieldName1);
				pip_1.put("value", d.getUi1());
				result.add(pip_1);
				if(d.getTrialNo() > 1) {
					JSONObject pip_2 = new JSONObject();
					pip_2.put("record", recordId);
					pip_2.put("redcap_event_name", eventId);
					String pipFieldName2 = "ds_pip" + d.getTrialNo() + "_2";
					pip_2.put("field_name", pipFieldName2);
					pip_2.put("value", d.getUi2());
					result.add(pip_2);
				}
				//user input
				JSONObject pip_list = new JSONObject();
				pip_list.put("record", recordId);
				pip_list.put("redcap_event_name", eventId);
				String pipListFieldName = "ds_pip" + d.getTrialNo() + "_list";
				pip_list.put("field_name", pipListFieldName);
				pip_list.put("value", d.getUserInput());
				result.add(pip_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "ds_pip" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject sp_tot = new JSONObject();
				sp_tot.put("record", recordId);
				sp_tot.put("redcap_event_name", eventId);
				String piTotFieldName = "ds_sp" + d.getTrialNo() + "_tot";
				sp_tot.put("field_name", piTotFieldName);
				sp_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(sp_tot);
				//accuracy
				JSONObject sp_acc = new JSONObject();
				sp_acc.put("record", recordId);
				sp_acc.put("redcap_event_name", eventId);
				String piAccFieldName = "ds_sp" + d.getTrialNo() + "_acc";
				sp_acc.put("field_name", piAccFieldName);
				sp_acc.put("value", d.getScore());
				result.add(sp_acc);
				//comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "ds_sp" + d.getTrialNo() + "_comment";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
			}else {
				//each stimuli digit
				JSONObject st_1 = new JSONObject();
				st_1.put("record", recordId);
				st_1.put("redcap_event_name", eventId);
				String stFieldName1 = "ds_st" + d.getTrialNo() + "_1";
				st_1.put("field_name", stFieldName1);
				st_1.put("value", d.getSi1());
				result.add(st_1);
				JSONObject st_2 = new JSONObject();
				st_2.put("record", recordId);
				st_2.put("redcap_event_name", eventId);
				String stFieldName2 = "ds_st" + d.getTrialNo() + "_2";
				st_2.put("field_name", stFieldName2);
				st_2.put("value", d.getSi2());
				result.add(st_2);
				JSONObject st_3 = new JSONObject();
				st_3.put("record", recordId);
				st_3.put("redcap_event_name", eventId);
				String stFieldName3 = "ds_st" + d.getTrialNo() + "_3";
				st_3.put("field_name", stFieldName3);
				st_3.put("value", d.getSi3());
				result.add(st_3);
				JSONObject st_4 = new JSONObject();
				st_4.put("record", recordId);
				st_4.put("redcap_event_name", eventId);
				String stFieldName4 = "ds_st" + d.getTrialNo() + "_4";
				st_4.put("field_name", stFieldName4);
				st_4.put("value", d.getSi4());
				result.add(st_4);
				JSONObject st_5 = new JSONObject();
				st_5.put("record", recordId);
				st_5.put("redcap_event_name", eventId);
				String stFieldName5 = "ds_st" + d.getTrialNo() + "_5";
				st_5.put("field_name", stFieldName5);
				st_5.put("value", d.getSi5());
				result.add(st_5);
				JSONObject st_6 = new JSONObject();
				st_6.put("record", recordId);
				st_6.put("redcap_event_name", eventId);
				String stFieldName6 = "ds_st" + d.getTrialNo() + "_6";
				st_6.put("field_name", stFieldName6);
				st_6.put("value", d.getSi6());
				result.add(st_6);
				JSONObject st_7 = new JSONObject();
				st_7.put("record", recordId);
				st_7.put("redcap_event_name", eventId);
				String stFieldName7 = "ds_st" + d.getTrialNo() + "_7";
				st_7.put("field_name", stFieldName7);
				st_7.put("value", d.getSi7());
				result.add(st_7);
				JSONObject st_8 = new JSONObject();
				st_8.put("record", recordId);
				st_8.put("redcap_event_name", eventId);
				String stFieldName8 = "ds_st" + d.getTrialNo() + "_8";
				st_8.put("field_name", stFieldName8);
				st_8.put("value", d.getSi8());
				result.add(st_8);
				//stimuli input
				JSONObject st_list = new JSONObject();
				st_list.put("record", recordId);
				st_list.put("redcap_event_name", eventId);
				String stListFieldName = "ds_st" + d.getTrialNo() + "_list";
				st_list.put("field_name", stListFieldName);
				st_list.put("value", d.getStimuliInput());
				result.add(st_list);
				//each user digit
				JSONObject pit_1 = new JSONObject();
				pit_1.put("record", recordId);
				pit_1.put("redcap_event_name", eventId);
				String pitFieldName1 = "ds_pit" + d.getTrialNo() + "_1";
				pit_1.put("field_name", pitFieldName1);
				pit_1.put("value", d.getUi1());
				result.add(pit_1);
				JSONObject pit_2 = new JSONObject();
				pit_2.put("record", recordId);
				pit_2.put("redcap_event_name", eventId);
				String pitFieldName2 = "ds_pit" + d.getTrialNo() + "_2";
				pit_2.put("field_name", pitFieldName2);
				pit_2.put("value", d.getUi2());
				result.add(pit_2);
				JSONObject pit_3 = new JSONObject();
				pit_3.put("record", recordId);
				pit_3.put("redcap_event_name", eventId);
				String pitFieldName3 = "ds_pit" + d.getTrialNo() + "_3";
				pit_3.put("field_name", pitFieldName3);
				pit_3.put("value", d.getUi3());
				result.add(pit_3);
				JSONObject pit_4 = new JSONObject();
				pit_4.put("record", recordId);
				pit_4.put("redcap_event_name", eventId);
				String pitFieldName4 = "ds_pit" + d.getTrialNo() + "_4";
				pit_4.put("field_name", pitFieldName4);
				pit_4.put("value", d.getUi4());
				result.add(pit_4);
				JSONObject pit_5 = new JSONObject();
				pit_5.put("record", recordId);
				pit_5.put("redcap_event_name", eventId);
				String pitFieldName5 = "ds_pit" + d.getTrialNo() + "_5";
				pit_5.put("field_name", pitFieldName5);
				pit_5.put("value", d.getUi5());
				result.add(pit_5);
				JSONObject pit_6 = new JSONObject();
				pit_6.put("record", recordId);
				pit_6.put("redcap_event_name", eventId);
				String pitFieldName6 = "ds_pit" + d.getTrialNo() + "_6";
				pit_6.put("field_name", pitFieldName6);
				pit_6.put("value", d.getUi6());
				result.add(pit_6);
				JSONObject pit_7 = new JSONObject();
				pit_7.put("record", recordId);
				pit_7.put("redcap_event_name", eventId);
				String pitFieldName7 = "ds_pit" + d.getTrialNo() + "_7";
				pit_7.put("field_name", pitFieldName7);
				pit_7.put("value", d.getUi7());
				result.add(pit_7);
				JSONObject pit_8 = new JSONObject();
				pit_8.put("record", recordId);
				pit_8.put("redcap_event_name", eventId);
				String pitFieldName8 = "ds_pit" + d.getTrialNo() + "_8";
				pit_8.put("field_name", pitFieldName8);
				pit_8.put("value", d.getUi8());
				result.add(pit_8);
				//user input
				JSONObject pit_list = new JSONObject();
				pit_list.put("record", recordId);
				pit_list.put("redcap_event_name", eventId);
				String pitListFieldName = "ds_pit" + d.getTrialNo() + "_list";
				pit_list.put("field_name", pitListFieldName);
				pit_list.put("value", d.getUserInput());
				result.add(pit_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "ds_pit" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject st_tot = new JSONObject();
				st_tot.put("record", recordId);
				st_tot.put("redcap_event_name", eventId);
				String tiTotFieldName = "ds_st" + d.getTrialNo() + "_tot";
				st_tot.put("field_name", tiTotFieldName);
				st_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(st_tot);
				//accuracy
				JSONObject st_acc = new JSONObject();
				st_acc.put("record", recordId);
				st_acc.put("redcap_event_name", eventId);
				String siAccFieldName = "ds_st" + d.getTrialNo() + "_acc";
				st_acc.put("field_name", siAccFieldName);
				st_acc.put("value", d.getScore());
				result.add(st_acc);
				//comment
				JSONObject st_comment = new JSONObject();
				st_comment.put("record", recordId);
				st_comment.put("redcap_event_name", eventId);
				String siCommentFieldName = "ds_st" + d.getTrialNo() + "_comment";
				st_comment.put("field_name", siCommentFieldName);
				st_comment.put("value", d.getComment());
				result.add(st_comment);
			}	
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateDetailCrossModalBindingJson(List<DetailCrossModalBinding> details, String deviceId, SyncRecordDataView dv) {
		JSONArray result = new JSONArray();
		String recordId = dv.getChildId();
		String eventId = generateEventIdForDetails(dv.getGrade(), dv.getCohort());
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "cmb_device_id");
		device.put("value", deviceId);
		result.add(device);
		if(details != null && details.size() > 0) {
			JSONObject experimenter = new JSONObject();
			experimenter.put("record", recordId);
			experimenter.put("redcap_event_name", eventId);
			experimenter.put("field_name", "cmb_experimenter");
			experimenter.put("value", details.get(0).getExperimenter());
			result.add(experimenter);
			
		}
		
		for(DetailCrossModalBinding d: details) {
			if(d.getTrialType() != null && d.getTrialType().equals("practice")) {
				//each stimuli digit
				JSONObject sp_1 = new JSONObject();
				sp_1.put("record", recordId);
				sp_1.put("redcap_event_name", eventId);
				String fieldName1 = "cmb_sppo" + d.getTrialNo() + "_1";
				sp_1.put("field_name", fieldName1);
				sp_1.put("value", d.getSpi1());
				result.add(sp_1);
				JSONObject sw_1 = new JSONObject();
				sw_1.put("record", recordId);
				sw_1.put("redcap_event_name", eventId);
				String sw1 = "cmb_spwo" + d.getTrialNo() + "_1";
				sw_1.put("field_name", sw1);
				sw_1.put("value", d.getSwi1());
				result.add(sw_1);
				if(d.getTrialNo() > 1) {
					JSONObject sp_2 = new JSONObject();
					sp_2.put("record", recordId);
					sp_2.put("redcap_event_name", eventId);
					String fieldName2 = "cmb_sppo" + d.getTrialNo() + "_2";
					sp_2.put("field_name", fieldName2);
					sp_2.put("value", d.getSpi2());
					result.add(sp_2);
					JSONObject sw_2 = new JSONObject();
					sw_2.put("record", recordId);
					sw_2.put("redcap_event_name", eventId);
					String sw2 = "cmb_spwo" + d.getTrialNo() + "_2";
					sw_2.put("field_name", sw2);
					sw_2.put("value", d.getSwi2());
					result.add(sw_2);
				}
				//stimuli input
				JSONObject sp_list = new JSONObject();
				sp_list.put("record", recordId);
				sp_list.put("redcap_event_name", eventId);
				String spFieldName = "cmb_sppo" + d.getTrialNo() + "_list";
				sp_list.put("field_name", spFieldName);
				sp_list.put("value", d.getStimuliPolygonInput());
				result.add(sp_list);
				JSONObject sw_list = new JSONObject();
				sw_list.put("record", recordId);
				sw_list.put("redcap_event_name", eventId);
				String swFieldName = "cmb_spwo" + d.getTrialNo() + "_list";
				sw_list.put("field_name", swFieldName);
				sw_list.put("value", d.getStimuliWordInput());
				result.add(sw_list);
				//each user digit
				JSONObject pip_1 = new JSONObject();
				pip_1.put("record", recordId);
				pip_1.put("redcap_event_name", eventId);
				String pipFieldName1 = "cmb_pippo" + d.getTrialNo() + "_1";
				pip_1.put("field_name", pipFieldName1);
				pip_1.put("value", d.getUpi1());
				result.add(pip_1);
				JSONObject piw_1 = new JSONObject();
				piw_1.put("record", recordId);
				piw_1.put("redcap_event_name", eventId);
				String piwFieldName1 = "cmb_pipwo" + d.getTrialNo() + "_1";
				piw_1.put("field_name", piwFieldName1);
				piw_1.put("value", d.getUwi1());
				result.add(piw_1);
				if(d.getTrialNo() > 1) {
					JSONObject pip_2 = new JSONObject();
					pip_2.put("record", recordId);
					pip_2.put("redcap_event_name", eventId);
					String pipFieldName2 = "cmb_pippo" + d.getTrialNo() + "_2";
					pip_2.put("field_name", pipFieldName2);
					pip_2.put("value", d.getUpi2());
					result.add(pip_2);
					JSONObject piw_2 = new JSONObject();
					piw_2.put("record", recordId);
					piw_2.put("redcap_event_name", eventId);
					String piwFieldName2 = "cmb_pipwo" + d.getTrialNo() + "_2";
					piw_2.put("field_name", piwFieldName2);
					piw_2.put("value", d.getUwi2());
					result.add(piw_2);
				}
				//user input
				JSONObject pip_list = new JSONObject();
				pip_list.put("record", recordId);
				pip_list.put("redcap_event_name", eventId);
				String pipListFieldName = "cmb_pippo" + d.getTrialNo() + "_list";
				pip_list.put("field_name", pipListFieldName);
				pip_list.put("value", d.getUserPolygonInput());
				result.add(pip_list);
				JSONObject piw_list = new JSONObject();
				piw_list.put("record", recordId);
				piw_list.put("redcap_event_name", eventId);
				String piwListFieldName = "cmb_pipwo" + d.getTrialNo() + "_list";
				piw_list.put("field_name", piwListFieldName);
				piw_list.put("value", d.getUserWordInput());
				result.add(piw_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "cmb_pip" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject sp_tot = new JSONObject();
				sp_tot.put("record", recordId);
				sp_tot.put("redcap_event_name", eventId);
				String piTotFieldName = "cmb_sp" + d.getTrialNo() + "_tot";
				sp_tot.put("field_name", piTotFieldName);
				sp_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(sp_tot);
				//accuracy
				JSONObject sp_acc = new JSONObject();
				sp_acc.put("record", recordId);
				sp_acc.put("redcap_event_name", eventId);
				String piAccFieldName = "cmb_sp" + d.getTrialNo() + "_acc";
				sp_acc.put("field_name", piAccFieldName);
				sp_acc.put("value", d.getScore());
				result.add(sp_acc);
				//comment
				JSONObject sp_comment = new JSONObject();
				sp_comment.put("record", recordId);
				sp_comment.put("redcap_event_name", eventId);
				String piCommentFieldName = "cmb_sp" + d.getTrialNo() + "_comment";
				sp_comment.put("field_name", piCommentFieldName);
				sp_comment.put("value", d.getComment());
				result.add(sp_comment);
			}else {
				//each stimuli digit
				JSONObject st_1 = new JSONObject();
				st_1.put("record", recordId);
				st_1.put("redcap_event_name", eventId);
				String stFieldName1 = "cmb_stpo" + d.getTrialNo() + "_1";
				st_1.put("field_name", stFieldName1);
				st_1.put("value", d.getSpi1());
				result.add(st_1);
				JSONObject st_2 = new JSONObject();
				st_2.put("record", recordId);
				st_2.put("redcap_event_name", eventId);
				String stFieldName2 = "cmb_stpo" + d.getTrialNo() + "_2";
				st_2.put("field_name", stFieldName2);
				st_2.put("value", d.getSpi2());
				result.add(st_2);
				JSONObject st_3 = new JSONObject();
				st_3.put("record", recordId);
				st_3.put("redcap_event_name", eventId);
				String stFieldName3 = "cmb_stpo" + d.getTrialNo() + "_3";
				st_3.put("field_name", stFieldName3);
				st_3.put("value", d.getSpi3());
				result.add(st_3);
				JSONObject st_4 = new JSONObject();
				st_4.put("record", recordId);
				st_4.put("redcap_event_name", eventId);
				String stFieldName4 = "cmb_stpo" + d.getTrialNo() + "_4";
				st_4.put("field_name", stFieldName4);
				st_4.put("value", d.getSpi4());
				result.add(st_4);
				JSONObject stw_1 = new JSONObject();
				stw_1.put("record", recordId);
				stw_1.put("redcap_event_name", eventId);
				String swFieldName1 = "cmb_stwo" + d.getTrialNo() + "_1";
				stw_1.put("field_name", swFieldName1);
				stw_1.put("value", d.getSwi1());
				result.add(stw_1);
				JSONObject sw_2 = new JSONObject();
				sw_2.put("record", recordId);
				sw_2.put("redcap_event_name", eventId);
				String swFieldName2 = "cmb_stwo" + d.getTrialNo() + "_2";
				sw_2.put("field_name", swFieldName2);
				sw_2.put("value", d.getSwi2());
				result.add(sw_2);
				JSONObject sw_3 = new JSONObject();
				sw_3.put("record", recordId);
				sw_3.put("redcap_event_name", eventId);
				String swFieldName3 = "cmb_stwo" + d.getTrialNo() + "_3";
				sw_3.put("field_name", swFieldName3);
				sw_3.put("value", d.getSwi3());
				result.add(sw_3);
				JSONObject sw_4 = new JSONObject();
				sw_4.put("record", recordId);
				sw_4.put("redcap_event_name", eventId);
				String swFieldName4 = "cmb_stwo" + d.getTrialNo() + "_4";
				sw_4.put("field_name", swFieldName4);
				sw_4.put("value", d.getSwi4());
				result.add(sw_4);
				//stimuli input
				JSONObject st_list = new JSONObject();
				st_list.put("record", recordId);
				st_list.put("redcap_event_name", eventId);
				String stListFieldName = "cmb_stpo" + d.getTrialNo() + "_list";
				st_list.put("field_name", stListFieldName);
				st_list.put("value", d.getStimuliPolygonInput());
				result.add(st_list);
				JSONObject sw_list = new JSONObject();
				sw_list.put("record", recordId);
				sw_list.put("redcap_event_name", eventId);
				String swListFieldName = "cmb_stwo" + d.getTrialNo() + "_list";
				sw_list.put("field_name", swListFieldName);
				sw_list.put("value", d.getStimuliWordInput());
				result.add(sw_list);
				//each user digit
				JSONObject pit_1 = new JSONObject();
				pit_1.put("record", recordId);
				pit_1.put("redcap_event_name", eventId);
				String pitFieldName1 = "cmb_pitpo" + d.getTrialNo() + "_1";
				pit_1.put("field_name", pitFieldName1);
				pit_1.put("value", d.getUpi1());
				result.add(pit_1);
				JSONObject pit_2 = new JSONObject();
				pit_2.put("record", recordId);
				pit_2.put("redcap_event_name", eventId);
				String pitFieldName2 = "cmb_pitpo" + d.getTrialNo() + "_2";
				pit_2.put("field_name", pitFieldName2);
				pit_2.put("value", d.getUpi2());
				result.add(pit_2);
				JSONObject pit_3 = new JSONObject();
				pit_3.put("record", recordId);
				pit_3.put("redcap_event_name", eventId);
				String pitFieldName3 = "cmb_pitpo" + d.getTrialNo() + "_3";
				pit_3.put("field_name", pitFieldName3);
				pit_3.put("value", d.getUpi3());
				result.add(pit_3);
				JSONObject pit_4 = new JSONObject();
				pit_4.put("record", recordId);
				pit_4.put("redcap_event_name", eventId);
				String pitFieldName4 = "cmb_pitpo" + d.getTrialNo() + "_4";
				pit_4.put("field_name", pitFieldName4);
				pit_4.put("value", d.getUpi4());
				result.add(pit_4);
				JSONObject piw_1 = new JSONObject();
				piw_1.put("record", recordId);
				piw_1.put("redcap_event_name", eventId);
				String piwFieldName1 = "cmb_pitwo" + d.getTrialNo() + "_1";
				piw_1.put("field_name", piwFieldName1);
				piw_1.put("value", d.getUwi1());
				result.add(piw_1);
				JSONObject piw_2 = new JSONObject();
				piw_2.put("record", recordId);
				piw_2.put("redcap_event_name", eventId);
				String piwFieldName2 = "cmb_pitwo" + d.getTrialNo() + "_2";
				piw_2.put("field_name", piwFieldName2);
				piw_2.put("value", d.getUwi2());
				result.add(piw_2);
				JSONObject piw_3 = new JSONObject();
				piw_3.put("record", recordId);
				piw_3.put("redcap_event_name", eventId);
				String piwFieldName3 = "cmb_pitwo" + d.getTrialNo() + "_3";
				piw_3.put("field_name", piwFieldName3);
				piw_3.put("value", d.getUwi3());
				result.add(piw_3);
				JSONObject piw_4 = new JSONObject();
				piw_4.put("record", recordId);
				piw_4.put("redcap_event_name", eventId);
				String piwFieldName4 = "cmb_pitwo" + d.getTrialNo() + "_4";
				piw_4.put("field_name", piwFieldName4);
				piw_4.put("value", d.getUwi4());
				result.add(piw_4);
				//user input
				JSONObject pit_list = new JSONObject();
				pit_list.put("record", recordId);
				pit_list.put("redcap_event_name", eventId);
				String pitListFieldName = "cmb_pitpo" + d.getTrialNo() + "_list";
				pit_list.put("field_name", pitListFieldName);
				pit_list.put("value", d.getUserPolygonInput());
				result.add(pit_list);
				JSONObject piw_list = new JSONObject();
				piw_list.put("record", recordId);
				piw_list.put("redcap_event_name", eventId);
				String piwListFieldName = "cmb_pitwo" + d.getTrialNo() + "_list";
				piw_list.put("field_name", piwListFieldName);
				piw_list.put("value", d.getUserWordInput());
				result.add(piw_list);
				//response time
				if(d.getResponseTime() != null) {
					JSONObject rt = new JSONObject();
					rt.put("record", recordId);
					rt.put("redcap_event_name", eventId);
					String rtFieldName = "cmb_pit" + d.getTrialNo() + "_rt";
					rt.put("field_name", rtFieldName);
					rt.put("value", d.getResponseTime());
					result.add(rt);
				}
				//total correct numbers
				JSONObject st_tot = new JSONObject();
				st_tot.put("record", recordId);
				st_tot.put("redcap_event_name", eventId);
				String tiTotFieldName = "cmb_st" + d.getTrialNo() + "_tot";
				st_tot.put("field_name", tiTotFieldName);
				st_tot.put("value", d.getNumOfDigitsCorrect());
				result.add(st_tot);
				//accuracy
				JSONObject st_acc = new JSONObject();
				st_acc.put("record", recordId);
				st_acc.put("redcap_event_name", eventId);
				String siAccFieldName = "cmb_st" + d.getTrialNo() + "_acc";
				st_acc.put("field_name", siAccFieldName);
				st_acc.put("value", d.getScore());
				result.add(st_acc);
				//comment
				JSONObject st_comment = new JSONObject();
				st_comment.put("record", recordId);
				st_comment.put("redcap_event_name", eventId);
				String siCommentFieldName = "cmb_st" + d.getTrialNo() + "_comment";
				st_comment.put("field_name", siCommentFieldName);
				st_comment.put("value", d.getComment());
				result.add(st_comment);
			}	
		}
		
		return result;
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray generateSummaryDigitSpan(SummaryDigitSpan summary, String deviceId) {
		JSONArray result = new JSONArray();
		String childId_grade = summary.getChildId_grade();
		String[] array = childId_grade.split("_");
		String recordId = array[0];
		String eventId = generateEventId(array[1]);
		
//		JSONObject childId = new JSONObject();
//		childId.put("record", recordId);
//		childId.put("redcap_event_name", eventId);
//		childId.put("field_name", "summary_ds_subject_id");
//		childId.put("value", array[0]);
//		result.add(childId);
//		
//		JSONObject grade = new JSONObject();
//		grade.put("record", recordId);
//		grade.put("redcap_event_name", eventId);
//		grade.put("field_name", "summary_ds_grade");
//		grade.put("value", array[1]);
//		result.add(grade);
//		
//		JSONObject experimenter = new JSONObject();
//		experimenter.put("record", recordId);
//		experimenter.put("redcap_event_name", eventId);
//		experimenter.put("field_name", "summary_ds_experimenter");
//		experimenter.put("value", summary.getExperimenter());
//		result.add(experimenter);
//		
//		JSONObject pt1 = new JSONObject();
//		pt1.put("record", recordId);
//		pt1.put("redcap_event_name", eventId);
//		pt1.put("field_name", "summary_ds_pt1");
//		pt1.put("value", summary.getDspt1());
//		result.add(pt1);
//		
//		JSONObject pt2 = new JSONObject();
//		pt2.put("record", recordId);
//		pt2.put("redcap_event_name", eventId);
//		pt2.put("field_name", "summary_ds_pt2");
//		pt2.put("value", summary.getDspt2());
//		result.add(pt2);
//		
//		JSONObject pt3 = new JSONObject();
//		pt3.put("record", recordId);
//		pt3.put("redcap_event_name", eventId);
//		pt3.put("field_name", "summary_ds_pt3");
//		pt3.put("value", summary.getDspt3());
//		result.add(pt3);
//		
//		JSONObject t21 = new JSONObject();
//		t21.put("record", recordId);
//		t21.put("redcap_event_name", eventId);
//		t21.put("field_name", "summary_ds_t21");
//		t21.put("value", summary.getDst21());
//		result.add(t21);
//		
//		JSONObject t22 = new JSONObject();
//		t22.put("record", recordId);
//		t22.put("redcap_event_name", eventId);
//		t22.put("field_name", "summary_ds_t22");
//		t22.put("value", summary.getDst22());
//		result.add(t22);
//		
//		JSONObject t23 = new JSONObject();
//		t23.put("record", recordId);
//		t23.put("redcap_event_name", eventId);
//		t23.put("field_name", "summary_ds_t23");
//		t23.put("value", summary.getDst23());
//		result.add(t23);
//		
//		JSONObject t24 = new JSONObject();
//		t24.put("record", recordId);
//		t24.put("redcap_event_name", eventId);
//		t24.put("field_name", "summary_ds_t24");
//		t24.put("value", summary.getDst24());
//		result.add(t24);
//		
//		JSONObject t31 = new JSONObject();
//		t31.put("record", recordId);
//		t31.put("redcap_event_name", eventId);
//		t31.put("field_name", "summary_ds_t31");
//		t31.put("value", summary.getDst31());
//		result.add(t31);
//		
//		JSONObject t32 = new JSONObject();
//		t32.put("record", recordId);
//		t32.put("redcap_event_name", eventId);
//		t32.put("field_name", "summary_ds_t32");
//		t32.put("value", summary.getDst32());
//		result.add(t32);
//		
//		JSONObject t33 = new JSONObject();
//		t33.put("record", recordId);
//		t33.put("redcap_event_name", eventId);
//		t33.put("field_name", "summary_ds_t33");
//		t33.put("value", summary.getDst33());
//		result.add(t33);
//		
//		JSONObject t34 = new JSONObject();
//		t34.put("record", recordId);
//		t34.put("redcap_event_name", eventId);
//		t34.put("field_name", "summary_ds_t34");
//		t34.put("value", summary.getDst34());
//		result.add(t34);
//		
//		JSONObject t41 = new JSONObject();
//		t41.put("record", recordId);
//		t41.put("redcap_event_name", eventId);
//		t41.put("field_name", "summary_ds_t41");
//		t41.put("value", summary.getDst41());
//		result.add(t41);
//		
//		JSONObject t42 = new JSONObject();
//		t42.put("record", recordId);
//		t42.put("redcap_event_name", eventId);
//		t42.put("field_name", "summary_ds_t42");
//		t42.put("value", summary.getDst42());
//		result.add(t42);
//		
//		JSONObject t43 = new JSONObject();
//		t43.put("record", recordId);
//		t43.put("redcap_event_name", eventId);
//		t43.put("field_name", "summary_ds_t43");
//		t43.put("value", summary.getDst43());
//		result.add(t43);
//		
//		JSONObject t44 = new JSONObject();
//		t44.put("record", recordId);
//		t44.put("redcap_event_name", eventId);
//		t44.put("field_name", "summary_ds_t44");
//		t44.put("value", summary.getDst44());
//		result.add(t44);
//		
//		JSONObject t51 = new JSONObject();
//		t51.put("record", recordId);
//		t51.put("redcap_event_name", eventId);
//		t51.put("field_name", "summary_ds_t51");
//		t51.put("value", summary.getDst51());
//		result.add(t51);
//		
//		JSONObject t52 = new JSONObject();
//		t52.put("record", recordId);
//		t52.put("redcap_event_name", eventId);
//		t52.put("field_name", "summary_ds_t52");
//		t52.put("value", summary.getDst52());
//		result.add(t52);
//		
//		JSONObject t53 = new JSONObject();
//		t53.put("record", recordId);
//		t53.put("redcap_event_name", eventId);
//		t53.put("field_name", "summary_ds_t53");
//		t53.put("value", summary.getDst53());
//		result.add(t53);
//		
//		JSONObject t54 = new JSONObject();
//		t54.put("record", recordId);
//		t54.put("redcap_event_name", eventId);
//		t54.put("field_name", "summary_ds_t54");
//		t54.put("value", summary.getDst54());
//		result.add(t54);
//		
//		JSONObject t61 = new JSONObject();
//		t61.put("record", recordId);
//		t61.put("redcap_event_name", eventId);
//		t61.put("field_name", "summary_ds_t61");
//		t61.put("value", summary.getDst61());
//		result.add(t61);
//		
//		JSONObject t62 = new JSONObject();
//		t62.put("record", recordId);
//		t62.put("redcap_event_name", eventId);
//		t62.put("field_name", "summary_ds_t62");
//		t62.put("value", summary.getDst62());
//		result.add(t62);
//		
//		JSONObject t63 = new JSONObject();
//		t63.put("record", recordId);
//		t63.put("redcap_event_name", eventId);
//		t63.put("field_name", "summary_ds_t63");
//		t63.put("value", summary.getDst63());
//		result.add(t63);
//		
//		JSONObject t64 = new JSONObject();
//		t64.put("record", recordId);
//		t64.put("redcap_event_name", eventId);
//		t64.put("field_name", "summary_ds_t24");
//		t64.put("value", summary.getDst64());
//		result.add(t64);
//		
//		JSONObject t71 = new JSONObject();
//		t71.put("record", recordId);
//		t71.put("redcap_event_name", eventId);
//		t71.put("field_name", "summary_ds_t71");
//		t71.put("value", summary.getDst71());
//		result.add(t71);
//		
//		JSONObject t72 = new JSONObject();
//		t72.put("record", recordId);
//		t72.put("redcap_event_name", eventId);
//		t72.put("field_name", "summary_ds_t72");
//		t72.put("value", summary.getDst72());
//		result.add(t72);
//		
//		JSONObject t73 = new JSONObject();
//		t73.put("record", recordId);
//		t73.put("redcap_event_name", eventId);
//		t73.put("field_name", "summary_ds_t73");
//		t73.put("value", summary.getDst73());
//		result.add(t73);
//		
//		JSONObject t74 = new JSONObject();
//		t74.put("record", recordId);
//		t74.put("redcap_event_name", eventId);
//		t74.put("field_name", "summary_ds_t74");
//		t74.put("value", summary.getDst74());
//		result.add(t74);
//		
//		JSONObject t81 = new JSONObject();
//		t81.put("record", recordId);
//		t81.put("redcap_event_name", eventId);
//		t81.put("field_name", "summary_ds_t81");
//		t81.put("value", summary.getDst81());
//		result.add(t81);
//		
//		JSONObject t82 = new JSONObject();
//		t82.put("record", recordId);
//		t82.put("redcap_event_name", eventId);
//		t82.put("field_name", "summary_ds_t82");
//		t82.put("value", summary.getDst82());
//		result.add(t82);
//		
//		JSONObject t83 = new JSONObject();
//		t83.put("record", recordId);
//		t83.put("redcap_event_name", eventId);
//		t83.put("field_name", "summary_ds_t83");
//		t83.put("value", summary.getDst83());
//		result.add(t83);
//		
//		JSONObject t84 = new JSONObject();
//		t84.put("record", recordId);
//		t84.put("redcap_event_name", eventId);
//		t84.put("field_name", "summary_ds_t84");
//		t84.put("value", summary.getDst84());
//		result.add(t84);
//		
//		JSONObject pi1 = new JSONObject();
//		pi1.put("record", recordId);
//		pi1.put("redcap_event_name", eventId);
//		pi1.put("field_name", "summary_ds_pi1");
//		pi1.put("value", summary.getDspi1());
//		result.add(pi1);
//		
//		JSONObject pi2 = new JSONObject();
//		pi2.put("record", recordId);
//		pi2.put("redcap_event_name", eventId);
//		pi2.put("field_name", "summary_ds_pi2");
//		pi2.put("value", summary.getDspi2());
//		result.add(pi2);
//		
//		JSONObject pi3 = new JSONObject();
//		pi3.put("record", recordId);
//		pi3.put("redcap_event_name", eventId);
//		pi3.put("field_name", "summary_ds_pi3");
//		pi3.put("value", summary.getDspi3());
//		result.add(pi3);
//		
//		JSONObject i21 = new JSONObject();
//		i21.put("record", recordId);
//		i21.put("redcap_event_name", eventId);
//		i21.put("field_name", "summary_ds_i21");
//		i21.put("value", summary.getDsi21());
//		result.add(i21);
//		
//		JSONObject i22 = new JSONObject();
//		i22.put("record", recordId);
//		i22.put("redcap_event_name", eventId);
//		i22.put("field_name", "summary_ds_i22");
//		i22.put("value", summary.getDsi22());
//		result.add(i22);
//		
//		JSONObject i23 = new JSONObject();
//		i23.put("record", recordId);
//		i23.put("redcap_event_name", eventId);
//		i23.put("field_name", "summary_ds_i23");
//		i23.put("value", summary.getDsi23());
//		result.add(i23);
//		
//		JSONObject i24 = new JSONObject();
//		i24.put("record", recordId);
//		i24.put("redcap_event_name", eventId);
//		i24.put("field_name", "summary_ds_i24");
//		i24.put("value", summary.getDsi24());
//		result.add(i24);
//		
//		JSONObject i31 = new JSONObject();
//		i31.put("record", recordId);
//		i31.put("redcap_event_name", eventId);
//		i31.put("field_name", "summary_ds_i31");
//		i31.put("value", summary.getDsi31());
//		result.add(i31);
//		
//		JSONObject i32 = new JSONObject();
//		i32.put("record", recordId);
//		i32.put("redcap_event_name", eventId);
//		i32.put("field_name", "summary_ds_i32");
//		i32.put("value", summary.getDsi32());
//		result.add(i32);
//		
//		JSONObject i33 = new JSONObject();
//		i33.put("record", recordId);
//		i33.put("redcap_event_name", eventId);
//		i33.put("field_name", "summary_ds_i33");
//		i33.put("value", summary.getDsi33());
//		result.add(i33);
//		
//		JSONObject i34 = new JSONObject();
//		i34.put("record", recordId);
//		i34.put("redcap_event_name", eventId);
//		i34.put("field_name", "summary_ds_i34");
//		i34.put("value", summary.getDsi34());
//		result.add(i34);
//		
//		JSONObject i41 = new JSONObject();
//		i41.put("record", recordId);
//		i41.put("redcap_event_name", eventId);
//		i41.put("field_name", "summary_ds_i41");
//		i41.put("value", summary.getDsi41());
//		result.add(i41);
//		
//		JSONObject i42 = new JSONObject();
//		i42.put("record", recordId);
//		i42.put("redcap_event_name", eventId);
//		i42.put("field_name", "summary_ds_i42");
//		i42.put("value", summary.getDsi42());
//		result.add(i42);
//		
//		JSONObject i43 = new JSONObject();
//		i43.put("record", recordId);
//		i43.put("redcap_event_name", eventId);
//		i43.put("field_name", "summary_ds_i43");
//		i43.put("value", summary.getDsi43());
//		result.add(i43);
//		
//		JSONObject i44 = new JSONObject();
//		i44.put("record", recordId);
//		i44.put("redcap_event_name", eventId);
//		i44.put("field_name", "summary_ds_i44");
//		i44.put("value", summary.getDsi44());
//		result.add(i44);
//		
//		JSONObject i51 = new JSONObject();
//		i51.put("record", recordId);
//		i51.put("redcap_event_name", eventId);
//		i51.put("field_name", "summary_ds_i51");
//		i51.put("value", summary.getDsi51());
//		result.add(i51);
//		
//		JSONObject i52 = new JSONObject();
//		i52.put("record", recordId);
//		i52.put("redcap_event_name", eventId);
//		i52.put("field_name", "summary_ds_i52");
//		i52.put("value", summary.getDsi52());
//		result.add(i52);
//		
//		JSONObject i53 = new JSONObject();
//		i53.put("record", recordId);
//		i53.put("redcap_event_name", eventId);
//		i53.put("field_name", "summary_ds_i53");
//		i53.put("value", summary.getDsi53());
//		result.add(i53);
//		
//		JSONObject i54 = new JSONObject();
//		i54.put("record", recordId);
//		i54.put("redcap_event_name", eventId);
//		i54.put("field_name", "summary_ds_i54");
//		i54.put("value", summary.getDsi54());
//		result.add(i54);
//		
//		JSONObject i61 = new JSONObject();
//		i61.put("record", recordId);
//		i61.put("redcap_event_name", eventId);
//		i61.put("field_name", "summary_ds_i61");
//		i61.put("value", summary.getDsi61());
//		result.add(i61);
//		
//		JSONObject i62 = new JSONObject();
//		i62.put("record", recordId);
//		i62.put("redcap_event_name", eventId);
//		i62.put("field_name", "summary_ds_i62");
//		i62.put("value", summary.getDsi62());
//		result.add(i62);
//		
//		JSONObject i63 = new JSONObject();
//		i63.put("record", recordId);
//		i63.put("redcap_event_name", eventId);
//		i63.put("field_name", "summary_ds_i63");
//		i63.put("value", summary.getDsi63());
//		result.add(i63);
//		
//		JSONObject i64 = new JSONObject();
//		i64.put("record", recordId);
//		i64.put("redcap_event_name", eventId);
//		i64.put("field_name", "summary_ds_i24");
//		i64.put("value", summary.getDsi64());
//		result.add(i64);
//		
//		JSONObject i71 = new JSONObject();
//		i71.put("record", recordId);
//		i71.put("redcap_event_name", eventId);
//		i71.put("field_name", "summary_ds_i71");
//		i71.put("value", summary.getDsi71());
//		result.add(i71);
//		
//		JSONObject i72 = new JSONObject();
//		i72.put("record", recordId);
//		i72.put("redcap_event_name", eventId);
//		i72.put("field_name", "summary_ds_i72");
//		i72.put("value", summary.getDsi72());
//		result.add(i72);
//		
//		JSONObject i73 = new JSONObject();
//		i73.put("record", recordId);
//		i73.put("redcap_event_name", eventId);
//		i73.put("field_name", "summary_ds_i73");
//		i73.put("value", summary.getDsi73());
//		result.add(i73);
//		
//		JSONObject i74 = new JSONObject();
//		i74.put("record", recordId);
//		i74.put("redcap_event_name", eventId);
//		i74.put("field_name", "summary_ds_i74");
//		i74.put("value", summary.getDsi74());
//		result.add(i74);
//		
//		JSONObject i81 = new JSONObject();
//		i81.put("record", recordId);
//		i81.put("redcap_event_name", eventId);
//		i81.put("field_name", "summary_ds_i81");
//		i81.put("value", summary.getDsi81());
//		result.add(i81);
//		
//		JSONObject i82 = new JSONObject();
//		i82.put("record", recordId);
//		i82.put("redcap_event_name", eventId);
//		i82.put("field_name", "summary_ds_i82");
//		i82.put("value", summary.getDsi82());
//		result.add(i82);
//		
//		JSONObject i83 = new JSONObject();
//		i83.put("record", recordId);
//		i83.put("redcap_event_name", eventId);
//		i83.put("field_name", "summary_ds_i83");
//		i83.put("value", summary.getDsi83());
//		result.add(i83);
//		
//		JSONObject i84 = new JSONObject();
//		i84.put("record", recordId);
//		i84.put("redcap_event_name", eventId);
//		i84.put("field_name", "summary_ds_i84");
//		i84.put("value", summary.getDsi84());
//		result.add(i84);
//		
//		DateFormat dateF = new SimpleDateFormat(dateFormat);
//		JSONObject createDate = new JSONObject();
//		createDate.put("record", recordId);
//		createDate.put("redcap_event_name", eventId);
//		createDate.put("field_name", "summary_ds_create_date");
//		createDate.put("value", dateF.format(summary.getCreateDate()));
//		result.add(createDate);
//		
//		JSONObject createTime = new JSONObject();
//		createTime.put("record", recordId);
//		createTime.put("redcap_event_name", eventId);
//		createTime.put("field_name", "summary_ds_create_time");
//		createTime.put("value", summary.getCreateTime());
//		result.add(createTime);
//		
//		JSONObject lastUpdateDate = new JSONObject();
//		lastUpdateDate.put("record", recordId);
//		lastUpdateDate.put("redcap_event_name", eventId);
//		lastUpdateDate.put("field_name", "summary_ds_last_update_date");
//		lastUpdateDate.put("value", dateF.format(summary.getLastUpdateDate()));
//		result.add(lastUpdateDate);
//		
//		JSONObject lastUpdateTime = new JSONObject();
//		lastUpdateTime.put("record", recordId);
//		lastUpdateTime.put("redcap_event_name", eventId);
//		lastUpdateTime.put("field_name", "summary_ds_last_update_time");
//		lastUpdateTime.put("value", summary.getLastUpdateTime());
//		result.add(lastUpdateTime);
//		
//		JSONObject device = new JSONObject();
//		device.put("record", recordId);
//		device.put("redcap_event_name", eventId);
//		device.put("field_name", "summary_ds_device_id");
//		device.put("value", deviceId);
//		result.add(device);
		
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	private JSONArray generateStudentProfileJson(List<StudentProfile> profiles, String deviceId) {
		JSONArray result = new JSONArray();
		for(StudentProfile p: profiles) {
			String recordId = p.getChildId();
			String eventId = generateEventId(p.getGrade());
			
			JSONObject childId = new JSONObject();
			childId.put("record", recordId);
			childId.put("redcap_event_name", eventId);
			childId.put("field_name", "profile_subject_id");
			childId.put("value", p.getChildId());
			result.add(childId);
			
			JSONObject grade = new JSONObject();
			grade.put("record", recordId);
			grade.put("redcap_event_name", eventId);
			grade.put("field_name", "profile_grade");
			grade.put("value", p.getGrade());
			result.add(grade);
			
			JSONObject character = new JSONObject();
			character.put("record", recordId);
			character.put("redcap_event_name", eventId);
			character.put("field_name", "profile_pirate_character");
			character.put("value", p.getPirateCharacter());
			result.add(character);
			
			JSONObject intro1 = new JSONObject();
			intro1.put("record", recordId);
			intro1.put("redcap_event_name", eventId);
			intro1.put("field_name", "profile_intro1_completed");
			intro1.put("value", p.getIntro1Completed());
			result.add(intro1);
			
			JSONObject intro2 = new JSONObject();
			intro2.put("record", recordId);
			intro2.put("redcap_event_name", eventId);
			intro2.put("field_name", "profile_intro2_completed");
			intro2.put("value", p.getIntro2Completed());
			result.add(intro2);
			
			JSONObject day1 = new JSONObject();
			day1.put("record", recordId);
			day1.put("redcap_event_name", eventId);
			day1.put("field_name", "profile_day_1_completed");
			day1.put("value", p.getDay1Completed());
			result.add(day1);
			
			JSONObject day2 = new JSONObject();
			day2.put("record", recordId);
			day2.put("redcap_event_name", eventId);
			day2.put("field_name", "profile_day_2_completed");
			day2.put("value", p.getDay2Completed());
			result.add(day2);
			
			JSONObject totalCoins = new JSONObject();
			totalCoins.put("record", recordId);
			totalCoins.put("redcap_event_name", eventId);
			totalCoins.put("field_name", "profile_total_coins");
			totalCoins.put("value", p.getTotalCoins());
			result.add(totalCoins);
			
			JSONObject totalRocks = new JSONObject();
			totalRocks.put("record", recordId);
			totalRocks.put("redcap_event_name", eventId);
			totalRocks.put("field_name", "profile_total_rocks");
			totalRocks.put("value", p.getTotalRocks());
			result.add(totalRocks);
			
			DateFormat dateF = new SimpleDateFormat(dateFormat);
			JSONObject createDate = new JSONObject();
			createDate.put("record", recordId);
			createDate.put("redcap_event_name", eventId);
			createDate.put("field_name", "profile_create_date");
			createDate.put("value", dateF.format(p.getCreateDate()));
			result.add(createDate);
			
			JSONObject createTime = new JSONObject();
			createTime.put("record", recordId);
			createTime.put("redcap_event_name", eventId);
			createTime.put("field_name", "profile_create_time");
			createTime.put("value", p.getCreateTime());
			result.add(createTime);
			
			JSONObject lastUpdateDate = new JSONObject();
			lastUpdateDate.put("record", recordId);
			lastUpdateDate.put("redcap_event_name", eventId);
			lastUpdateDate.put("field_name", "profile_last_update_date");
			lastUpdateDate.put("value", dateF.format(p.getLastUpdateDate()));
			result.add(lastUpdateDate);
			
			JSONObject lastUpdateTime = new JSONObject();
			lastUpdateTime.put("record", recordId);
			lastUpdateTime.put("redcap_event_name", eventId);
			lastUpdateTime.put("field_name", "profile_last_update_time");
			lastUpdateTime.put("value", p.getLastUpdateTime());
			result.add(lastUpdateTime);
			
			JSONObject device = new JSONObject();
			device.put("record", recordId);
			device.put("redcap_event_name", eventId);
			device.put("field_name", "profile_device_id");
			device.put("value", deviceId);
			result.add(device);
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	private JSONArray generateGameProgressJson(GameProgress p, String deviceId) {
		JSONArray result = new JSONArray();
		String recordId = p.getChildId();
		String eventId = generateEventId(p.getGrade());
		
		JSONObject childId = new JSONObject();
		childId.put("record", recordId);
		childId.put("redcap_event_name", eventId);
		childId.put("field_name", "progress_subject_id");
		childId.put("value", p.getChildId());
		result.add(childId);
		
		JSONObject grade = new JSONObject();
		grade.put("record", recordId);
		grade.put("redcap_event_name", eventId);
		grade.put("field_name", "progress_grade");
		grade.put("value", p.getGrade());
		result.add(grade);
		
		JSONObject gameId = new JSONObject();
		gameId.put("record", recordId);
		gameId.put("redcap_event_name", eventId);
		gameId.put("field_name", "progress_game_id");
		gameId.put("value", p.getGameId());
		result.add(gameId);
		
		JSONObject gameStatus = new JSONObject();
		gameStatus.put("record", recordId);
		gameStatus.put("redcap_event_name", eventId);
		gameStatus.put("field_name", "progress_game_status");
		gameStatus.put("value", p.getGameStatus());
		result.add(gameStatus);
		
		JSONObject totalCoins = new JSONObject();
		totalCoins.put("record", recordId);
		totalCoins.put("redcap_event_name", eventId);
		totalCoins.put("field_name", "progress_coins");
		totalCoins.put("value", p.getCoins());
		result.add(totalCoins);
		
		JSONObject totalRocks = new JSONObject();
		totalRocks.put("record", recordId);
		totalRocks.put("redcap_event_name", eventId);
		totalRocks.put("field_name", "progress_rocks");
		totalRocks.put("value", p.getRocks());
		result.add(totalRocks);
		
		JSONObject repetitionCount = new JSONObject();
		repetitionCount.put("record", recordId);
		repetitionCount.put("redcap_event_name", eventId);
		repetitionCount.put("field_name", "progress_repetition_count");
		repetitionCount.put("value", p.getRepetitionCount());
		result.add(repetitionCount);
		
		JSONObject numOfBoxes = new JSONObject();
		numOfBoxes.put("record", recordId);
		numOfBoxes.put("redcap_event_name", eventId);
		numOfBoxes.put("field_name", "progress_num_of_boxes");
		numOfBoxes.put("value", p.getNumOfBoxes());
		result.add(numOfBoxes);
		
		DateFormat dateF = new SimpleDateFormat(dateFormat);
		JSONObject createDate = new JSONObject();
		createDate.put("record", recordId);
		createDate.put("redcap_event_name", eventId);
		createDate.put("field_name", "progress_create_date");
		createDate.put("value", dateF.format(p.getCreateDate()));
		result.add(createDate);
		
		JSONObject createTime = new JSONObject();
		createTime.put("record", recordId);
		createTime.put("redcap_event_name", eventId);
		createTime.put("field_name", "progress_create_time");
		createTime.put("value", p.getCreateTime());
		result.add(createTime);
		
		JSONObject lastUpdateDate = new JSONObject();
		lastUpdateDate.put("record", recordId);
		lastUpdateDate.put("redcap_event_name", eventId);
		lastUpdateDate.put("field_name", "progress_last_update_date");
		lastUpdateDate.put("value", dateF.format(p.getLastUpdateDate()));
		result.add(lastUpdateDate);
		
		JSONObject lastUpdateTime = new JSONObject();
		lastUpdateTime.put("record", recordId);
		lastUpdateTime.put("redcap_event_name", eventId);
		lastUpdateTime.put("field_name", "progress_last_update_time");
		lastUpdateTime.put("value", p.getLastUpdateTime());
		result.add(lastUpdateTime);
		
		JSONObject device = new JSONObject();
		device.put("record", recordId);
		device.put("redcap_event_name", eventId);
		device.put("field_name", "progress_device_id");
		device.put("value", deviceId);
		result.add(device);
		
		return result;
	}
	
	//NOT USED
	private String generateEventId(String grade) {
		if(grade == null || grade.equals("K")) {
			return "grade_k_arm_1";
		}else if(grade.equals("1")) {
			return "grade_1_arm_1";
		}else if(grade.equals("2")) {
			return "grade_2_arm_1";
		}else if(grade.equals("3")) {
			return "grade_3_arm_1";
		}else if(grade.equals("4")) {
			return "grade_4_arm_1";
		}else if(grade.equals("5")) {
			return "grade_5_arm_1";
		}else if(grade.equals("6")) {
			return "grade_6_arm_1";
		}
		return "grade_k_arm_1";
	}
	
	private String generateEventIdForDetails(String grade, String cohort) {
		String arm = "arm_1";
		if(cohort != null && cohort.equals("2")) {
			arm = "arm_2";
		}else if(cohort != null && cohort.equals("3")) {
			arm = "arm_3";
		}
		if(grade == null || grade.equals("K")) {
			return "kindergarten_" + arm;
		}else if(grade.equals("1")) {
			return "grade_1_" + arm;
		}else if(grade.equals("2")) {
			return "grade_2_" + arm;
		}else if(grade.equals("3")) {
			return "grade_3_" + arm;
		}else if(grade.equals("4")) {
			return "grade_4_" + arm;
		}else if(grade.equals("5")) {
			return "grade_5_" + arm;
		}else if(grade.equals("6")) {
			return "grade_6_" + arm;
		}
		return "kindergarten_" + arm;
	}
}
