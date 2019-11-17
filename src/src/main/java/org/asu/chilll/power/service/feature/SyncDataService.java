package org.asu.chilll.power.service.feature;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.asu.chilll.power.dataview.DataToSyncCount;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.StudentProfile;
import org.asu.chilll.power.entity.data.DetailCrossModalBinding;
import org.asu.chilll.power.entity.data.DetailDigitSpan;
import org.asu.chilll.power.entity.data.DetailDigitSpanRunning;
import org.asu.chilll.power.entity.data.DetailLocationSpan;
import org.asu.chilll.power.entity.data.DetailLocationSpanRunning;
import org.asu.chilll.power.entity.data.DetailNonWord;
import org.asu.chilll.power.entity.data.DetailNumberUpdateVisual;
import org.asu.chilll.power.entity.data.DetailPhonologicalBinding;
import org.asu.chilll.power.entity.data.DetailRepetitionAuditory;
import org.asu.chilll.power.entity.data.DetailRepetitionVisual;
import org.asu.chilll.power.entity.data.DetailVisualBindingSpan;
import org.asu.chilll.power.entity.data.DetailVisualSpan;
import org.asu.chilll.power.entity.data.DetailVisualSpanRunning;
import org.asu.chilll.power.entity.data.SummaryCrossModalBinding;
import org.asu.chilll.power.entity.data.SummaryDigitSpan;
import org.asu.chilll.power.entity.data.SummaryDigitSpanRunning;
import org.asu.chilll.power.entity.data.SummaryLocationSpan;
import org.asu.chilll.power.entity.data.SummaryLocationSpanRunning;
import org.asu.chilll.power.entity.data.SummaryPhonologicalBinding;
import org.asu.chilll.power.entity.data.SummaryVisualBindingSpan;
import org.asu.chilll.power.entity.data.SummaryVisualSpan;
import org.asu.chilll.power.entity.data.SummaryVisualSpanRunning;
import org.asu.chilll.power.entity.feature.SyncDataResult;
import org.asu.chilll.power.entity.feature.SyncDataRecord;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.enums.SyncDataErrorType;
import org.asu.chilll.power.repository.feature.SyncDataCenterRepository;
import org.asu.chilll.power.repository.feature.SyncDataDistributedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SyncDataService {
	
//	@Autowired
//	private SyncDataDistributedRepository distributedRepo;
//	
//	@Autowired
//	private SyncDataCenterRepository centerRepo;
//	
//	private final int insertLimit = 30;	//max batch size is 50, setting in application.properties
//	private final int maxLoop = 10;
//	
//	public List<DataToSyncCount> fetchDataToSyncCount(){
//		List<DataToSyncCount> result = new ArrayList<DataToSyncCount>();
//		
//		SyncDataRecord timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Cross_Modal_Binding.toString());
//		Long cmbTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long cmbCount = distributedRepo.fetchDistributedCrossModalBindingDetailTotalCount(cmbTime);
//		result.add(new DataToSyncCount(GameIdType.Cross_Modal_Binding.toString(), "Cross Modal Binding", cmbCount, "./api/upload/crossmodal"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Digit_Span.toString());
//		Long dsTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long dsCount = distributedRepo.fetchDistributedDigitSpanDetailTotalCount(dsTime);
//		result.add(new DataToSyncCount(GameIdType.Digit_Span.toString(), "Digit Span", dsCount, "./api/upload/digitspan"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Digit_Span_Running.toString());
//		Long dsrTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long dsrCount = distributedRepo.fetchDistributedDigitSpanRunningDetailTotalCount(dsrTime);
//		result.add(new DataToSyncCount(GameIdType.Digit_Span_Running.toString(), "Digit Span Running", dsrCount, "./api/upload/digitspanrunning"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Location_Span.toString());
//		Long lsTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long lsCount = distributedRepo.fetchDistributedLocationSpanDetailTotalCount(lsTime);
//		result.add(new DataToSyncCount(GameIdType.Location_Span.toString(), "Location Span", lsCount, "./api/upload/locationspan"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Location_Span_Running.toString());
//		Long lsrTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long lsrCount = distributedRepo.fetchDistributedLocationSpanRunningDetailTotalCount(lsrTime);
//		result.add(new DataToSyncCount(GameIdType.Location_Span_Running.toString(), "Location Span Running", lsrCount, "./api/upload/locationspanrunning"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Number_Update_Visual.toString());
//		Long nuvTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long nuvCount = distributedRepo.fetchDistributedNumberUpdateVisualDetailTotalCount(nuvTime);
//		result.add(new DataToSyncCount(GameIdType.Number_Update_Visual.toString(), "Number Update - Visual", nuvCount, "./api/upload/numberupdatevisual"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Phonological_Binding.toString());
//		Long pbTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long pbCount = distributedRepo.fetchDistributedPhonologicalBindingDetailTotalCount(pbTime);
//		result.add(new DataToSyncCount(GameIdType.Phonological_Binding.toString(), "Phonological Binding", pbCount, "./api/upload/phonological"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Visual_Binding_Span.toString());
//		Long vbsTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long vbsCount = distributedRepo.fetchDistributedVisualBindingSpanDetailTotalCount(vbsTime);
//		result.add(new DataToSyncCount(GameIdType.Visual_Binding_Span.toString(), "Visual Binding Span", vbsCount, "./api/upload/visualbinding"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Visual_Span.toString());
//		Long vsTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long vsCount = distributedRepo.fetchDistributedVisualSpanDetailTotalCount(vsTime);
//		result.add(new DataToSyncCount(GameIdType.Visual_Span.toString(), "Visual Span", vsCount, "./api/upload/visualspan"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Visual_Span_Running.toString());
//		Long vsrTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long vsrCount = distributedRepo.fetchDistributedVisualSpanRunningDetailTotalCount(vsrTime);
//		result.add(new DataToSyncCount(GameIdType.Visual_Span_Running.toString(), "Visual Span Running", vsrCount, "./api/upload/visualspanrunning"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Nonword_Repetition.toString());
//		Long nrTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long nrCount = distributedRepo.fetchDistributedNonWordDetailTotalCount(nrTime);
//		result.add(new DataToSyncCount(GameIdType.Nonword_Repetition.toString(), "Nonword Repetition", nrCount, "./api/upload/nonword"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Repetition_Detection_Auditory.toString());
//		Long rdaTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long rdaCount = distributedRepo.fetchDistributedRepetitionAuditoryDetailTotalCount(rdaTime);
//		result.add(new DataToSyncCount(GameIdType.Repetition_Detection_Auditory.toString(), "Repetition Detection - Auditory", rdaCount, "./api/upload/repetitionauditory"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Repetition_Detection_Visual.toString());
//		Long rdvTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long rdvCount = distributedRepo.fetchDistributedRepetitionVisualDetailTotalCount(rdvTime);
//		result.add(new DataToSyncCount(GameIdType.Repetition_Detection_Visual.toString(), "Repetition Detection - Visual", rdvCount, "./api/upload/repetitionvisual"));
//		
////		timestamp = distributedRepo.fetchTimeStampByTypeId("Detail_" + GameIdType.Number_Update_Auditory.toString());
////		Long nuaTime = timestamp == null ? 0L : timestamp.getTimeStamp();
////		long nuaCount = distributedRepo.fetchDistributedNumberUpdateAuditoryDetailTotalCount(nuaTime);
////		result.add(new DataToSyncCount(GameIdType.Number_Update_Auditory.toString(), "Number Update - Auditory", nuaCount, "./api/upload/numberupdateauditory"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId(GameIdType.Game_Progress.toString());
//		Long pTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long pCount = distributedRepo.fetchDistributedGameProgressTotalCount(pTime);
//		result.add(new DataToSyncCount(GameIdType.Game_Progress.toString(), "Game Progress", pCount, "./api/upload/gameprogress"));
//		
//		timestamp = distributedRepo.fetchTimeStampByTypeId(GameIdType.Student_Profile.toString());
//		Long sTime = timestamp == null ? 0L : timestamp.getTimeStamp();
//		long sCount = distributedRepo.fetchDistributedStudentProfileTotalCount(sTime);
//		result.add(new DataToSyncCount(GameIdType.Student_Profile.toString(), "Student Progress Summary", sCount, "./api/upload/studentprofile"));
//		
//		return result;
//	}
//	
//	public boolean syncData(String gameId) {
//		SyncDataResult result = new SyncDataResult();
//		if(gameId.equals(GameIdType.Digit_Span.toString())) {
//			result = syncDigitSpanDetailData();
//			if(result != null && result.getErrorType() == null) {
//				result = syncDigitSpanSummaryData();
//			}
//		}else if(gameId.equals(GameIdType.Digit_Span_Running.toString())) {
//			result = syncDigitSpanRunningDetailData();
//			if(result != null && result.getErrorType() == null) {
//				result = syncDigitSpanRunningSummaryData();
//			}
//		}else if(gameId.equals(GameIdType.Location_Span.toString())) {
//			result = syncLocationSpanDetailData();
//			if(result != null && result.getErrorType() == null) {
//				result = syncLocationSpanSummaryData();
//			}
//		}else if(gameId.equals(GameIdType.Location_Span_Running.toString())) {
//			result = syncLocationSpanRunningDetailData();
//			if(result != null && result.getErrorType() == null) {
//				result = syncLocationSpanRunningSummaryData();
//			}
//		}else if(gameId.equals(GameIdType.Number_Update_Visual.toString())) {
//			result = syncNumberUpdateVisualDetailData();
//			
//		}else if(gameId.equals(GameIdType.Phonological_Binding.toString())) {
//			result = syncPhonologicalBindingDetailData();
//			if(result != null && result.getErrorType() == null) {
//				result = syncPhonologicalBindingSummaryData();
//			}
//		}else if(gameId.equals(GameIdType.Visual_Binding_Span.toString())) {
//			result = syncVisualBindingSpanDetailData();
//			if(result != null && result.getErrorType() == null) {
//				result = syncVisualBindingSpanSummaryData();
//			}
//		}else if(gameId.equals(GameIdType.Visual_Span.toString())) {
//			result = syncVisualSpanDetailData();
//			if(result != null && result.getErrorType() == null) {
//				result = syncVisualSpanSummaryData();
//			}
//		}else if(gameId.equals(GameIdType.Visual_Span_Running.toString())) {
//			result = syncVisualSpanRunningDetailData();
//			if(result != null && result.getErrorType() == null) {
//				result = syncVisualSpanRunningSummaryData();
//			}
//		}else if(gameId.equals(GameIdType.Cross_Modal_Binding.toString())) {
//			result = syncCrossModalBindingDetailData();
//			if(result != null && result.getErrorType() == null) {
//				result = syncCrossModalBindingSummaryData();
//			}
//		}else if(gameId.equals(GameIdType.Nonword_Repetition.toString())) {
//			result = syncNonWordDetailData();
//		}else if(gameId.equals(GameIdType.Repetition_Detection_Auditory.toString())) {
//			result = syncRepetitionAuditoryDetailData();
//		}else if(gameId.equals(GameIdType.Repetition_Detection_Visual.toString())) {
//			result = syncRepetitionVisualDetailData();
//		}else {
//			result = new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}
//		
//		if(result != null && result.getErrorType() == null) {
//			return true;
//		}else {
//			return false;
//		}
//	}
//	
//	public boolean ping() {
//		return centerRepo.fetchTestConnection("test_connection");
//	}
//	
//	public boolean syncGameProgress(String dataTypeId) {
//		SyncDataResult result = new SyncDataResult();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(dataTypeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<GameProgress> distributedGameProgress = distributedRepo.fetchDistributedListGameProgressByLimit(timestamp, insertLimit);
//			if(distributedGameProgress == null || distributedGameProgress.size() == 0) {
//				break;
//			}
//			for(GameProgress p: distributedGameProgress) {
//				result = centerRepo.updateGameProgress(p);
//				if(result != null && result.getErrorType() == null) {
//					distributedRepo.updateTimeStampByTypeId(dataTypeId, result.getTimestamp());
//				}else {
//					break;
//				}
//			}
//		}
//		return result != null && result.getErrorType() == null;
//	}
//	
//	public boolean syncStudentProfile(String dataTypeId) {
//		SyncDataResult result = new SyncDataResult();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(dataTypeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<StudentProfile> distributedStudentProfile = distributedRepo.fetchDistributedListStudentProfileByLimit(timestamp, insertLimit);
//			if(distributedStudentProfile == null || distributedStudentProfile.size() == 0) {
//				break;
//			}
//			for(StudentProfile p: distributedStudentProfile) {
//				result = centerRepo.updateStudentProfile(p);
//				if(result != null && result.getErrorType() == null) {
//					distributedRepo.updateTimeStampByTypeId(dataTypeId, result.getTimestamp());
//				}else {
//					break;
//				}
//			}
//		}
//		return result != null && result.getErrorType() == null;
//	}
//	
//	private SyncDataResult syncCrossModalBindingDetailData() {
//		String typeId = "Detail_" + GameIdType.Cross_Modal_Binding.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailCrossModalBinding> distributedDigitSpan = distributedRepo.fetchDistributedListCrossModalBindingDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailCrossModalBinding ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailCrossModalBinding> centerDigitSpan = centerRepo.fetchListCrossModalBindingByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailCrossModalBinding ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailCrossModalBinding> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createCrossModalBindingDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncDigitSpanDetailData() {
//		String typeId = "Detail_" + GameIdType.Digit_Span.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailDigitSpan> distributedDigitSpan = distributedRepo.fetchDistributedListDigitSpanDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailDigitSpan ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailDigitSpan> centerDigitSpan = centerRepo.fetchListDigitSpanByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailDigitSpan ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailDigitSpan> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createDigitSpanDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncDigitSpanRunningDetailData() {
//		String typeId = "Detail_" + GameIdType.Digit_Span_Running.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailDigitSpanRunning> distributedDigitSpan = distributedRepo.fetchDistributedListDigitSpanRunningDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailDigitSpanRunning ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailDigitSpanRunning> centerDigitSpan = centerRepo.fetchListDigitSpanRunningByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailDigitSpanRunning ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailDigitSpanRunning> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createDigitSpanRunningDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncLocationSpanDetailData() {
//		String typeId = "Detail_" + GameIdType.Location_Span.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailLocationSpan> distributedDigitSpan = distributedRepo.fetchDistributedListLocationSpanDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailLocationSpan ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailLocationSpan> centerDigitSpan = centerRepo.fetchListLocationSpanByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailLocationSpan ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailLocationSpan> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createLocationSpanDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncLocationSpanRunningDetailData() {
//		String typeId = "Detail_" + GameIdType.Location_Span_Running.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailLocationSpanRunning> distributedDigitSpan = distributedRepo.fetchDistributedListLocationSpanRunningDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailLocationSpanRunning ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailLocationSpanRunning> centerDigitSpan = centerRepo.fetchListLocationSpanRunningByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailLocationSpanRunning ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailLocationSpanRunning> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createLocationSpanRunningDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncNumberUpdateVisualDetailData() {
//		String typeId = "Detail_" + GameIdType.Number_Update_Visual.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailNumberUpdateVisual> distributedDigitSpan = distributedRepo.fetchDistributedListNumberUpdateVisualDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailNumberUpdateVisual ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailNumberUpdateVisual> centerDigitSpan = centerRepo.fetchListNumberUpdateVisualByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailNumberUpdateVisual ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailNumberUpdateVisual> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createNumberUpdateVisualDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncPhonologicalBindingDetailData() {
//		String typeId = "Detail_" + GameIdType.Phonological_Binding.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailPhonologicalBinding> distributedDigitSpan = distributedRepo.fetchDistributedListPhonologicalBindingDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailPhonologicalBinding ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailPhonologicalBinding> centerDigitSpan = centerRepo.fetchListPhonologicalBindingByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailPhonologicalBinding ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailPhonologicalBinding> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createPhonologicalBindingDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncVisualBindingSpanDetailData() {
//		String typeId = "Detail_" + GameIdType.Visual_Binding_Span.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailVisualBindingSpan> distributedDigitSpan = distributedRepo.fetchDistributedListVisualBindingSpanDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailVisualBindingSpan ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailVisualBindingSpan> centerDigitSpan = centerRepo.fetchListVisualBindingSpanByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailVisualBindingSpan ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailVisualBindingSpan> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createVisualBindingSpanDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncVisualSpanDetailData() {
//		String typeId = "Detail_" + GameIdType.Visual_Span.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailVisualSpan> distributedDigitSpan = distributedRepo.fetchDistributedListVisualSpanDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailVisualSpan ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailVisualSpan> centerDigitSpan = centerRepo.fetchListVisualSpanByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailVisualSpan ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailVisualSpan> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createVisualSpanDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncVisualSpanRunningDetailData() {
//		String typeId = "Detail_" + GameIdType.Visual_Span_Running.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailVisualSpanRunning> distributedDigitSpan = distributedRepo.fetchDistributedListVisualSpanRunningDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailVisualSpanRunning ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailVisualSpanRunning> centerDigitSpan = centerRepo.fetchListVisualSpanRunningByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailVisualSpanRunning ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailVisualSpanRunning> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createVisualSpanRunningDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncNonWordDetailData() {
//		String typeId = "Detail_" + GameIdType.Nonword_Repetition.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailNonWord> distributedDigitSpan = distributedRepo.fetchDistributedListNonWordDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailNonWord ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailNonWord> centerDigitSpan = centerRepo.fetchListNonWordByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailNonWord ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailNonWord> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createNonWordDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncRepetitionAuditoryDetailData() {
//		String typeId = "Detail_" + GameIdType.Repetition_Detection_Auditory.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailRepetitionAuditory> distributedDigitSpan = distributedRepo.fetchDistributedListRepetitionAuditoryDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailRepetitionAuditory ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailRepetitionAuditory> centerDigitSpan = centerRepo.fetchListRepetitionAuditoryByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailRepetitionAuditory ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailRepetitionAuditory> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createRepetitionAuditoryDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncRepetitionVisualDetailData() {
//		String typeId = "Detail_" + GameIdType.Repetition_Detection_Visual.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		//keep fetching insertLimit records from distributed db and insert into center db
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<DetailRepetitionVisual> distributedDigitSpan = distributedRepo.fetchDistributedListRepetitionVisualDetailByLimit(timestamp, insertLimit);
//			if(distributedDigitSpan == null || distributedDigitSpan.size() == 0) {
//				break;
//			}
//			//check whether uids exist in center db
//			List<String> distributedUids = new ArrayList<String>();
//			for(DetailRepetitionVisual ds: distributedDigitSpan) {
//				distributedUids.add(ds.getUid());
//			}
//			List<DetailRepetitionVisual> centerDigitSpan = centerRepo.fetchListRepetitionVisualByUids(distributedUids);
//			if(centerDigitSpan != null && centerDigitSpan.size() > 0) {
//				//update timestamp and remove duplicate records from distributedDigitSpan
//				List<String> existingUids = new ArrayList<String>();
//				for(DetailRepetitionVisual ds: centerDigitSpan) {
//					existingUids.add(ds.getUid());
//				}
//				//List<DetailDigitSpan> removeList = new ArrayList<DetailDigitSpan>();
//				Iterator<DetailRepetitionVisual> iter = distributedDigitSpan.iterator();
//				while(iter.hasNext()) {
//					if(existingUids.contains(iter.next().getUid())) {
//						iter.remove();
//					}
//				}
//			}
//			//insert records
//			if(distributedDigitSpan != null && distributedDigitSpan.size() > 0) {
//				result = centerRepo.createRepetitionVisualDetailData(distributedDigitSpan);
//				if(result != null && result.getErrorType() == null) {
//					timestamp = result.getTimestamp();
//				}
//			}else {
//				timestamp = centerDigitSpan.get(centerDigitSpan.size() - 1).getCreateTime();
//			}
//			//update timestamp in distributed db
//			distributedRepo.updateTimeStampByTypeId(typeId, timestamp);
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncCrossModalBindingSummaryData() {
//		String typeId = "Summary_" + GameIdType.Cross_Modal_Binding.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<SummaryCrossModalBinding> summaryList = distributedRepo.fetchDistributedListCrossModalBindingSummaryByLimit(timestamp, insertLimit);
//			if(summaryList == null || summaryList.size() == 0) {
//				break;
//			}
//			for(SummaryCrossModalBinding ds: summaryList) {
//				result = centerRepo.updateCrossModalBindingSummaryData(ds);
//				if(result != null && result.getErrorType() == null) {
//					distributedRepo.updateTimeStampByTypeId(typeId, result.getTimestamp());
//				}else {
//					break;
//				}
//			}
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncDigitSpanSummaryData() {
//		String typeId = "Summary_" + GameIdType.Digit_Span.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<SummaryDigitSpan> summaryList = distributedRepo.fetchDistributedListDigitSpanSummaryByLimit(timestamp, insertLimit);
//			if(summaryList == null || summaryList.size() == 0) {
//				break;
//			}
//			for(SummaryDigitSpan ds: summaryList) {
//				result = centerRepo.updateDigitSpanSummaryData(ds);
//				if(result != null && result.getErrorType() == null) {
//					distributedRepo.updateTimeStampByTypeId(typeId, result.getTimestamp());
//				}else {
//					break;
//				}
//			}
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncDigitSpanRunningSummaryData() {
//		String typeId = "Summary_" + GameIdType.Digit_Span_Running.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<SummaryDigitSpanRunning> summaryList = distributedRepo.fetchDistributedListDigitSpanRunningSummaryByLimit(timestamp, insertLimit);
//			if(summaryList == null || summaryList.size() == 0) {
//				break;
//			}
//			for(SummaryDigitSpanRunning ds: summaryList) {
//				result = centerRepo.updateDigitSpanRunningSummaryData(ds);
//				if(result != null && result.getErrorType() == null) {
//					distributedRepo.updateTimeStampByTypeId(typeId, result.getTimestamp());
//				}else {
//					break;
//				}
//			}
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncLocationSpanSummaryData() {
//		String typeId = "Summary_" + GameIdType.Location_Span.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<SummaryLocationSpan> summaryList = distributedRepo.fetchDistributedListLocationSpanSummaryByLimit(timestamp, insertLimit);
//			if(summaryList == null || summaryList.size() == 0) {
//				break;
//			}
//			for(SummaryLocationSpan ds: summaryList) {
//				result = centerRepo.updateLocationSpanSummaryData(ds);
//				if(result != null && result.getErrorType() == null) {
//					distributedRepo.updateTimeStampByTypeId(typeId, result.getTimestamp());
//				}else {
//					break;
//				}
//			}
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncLocationSpanRunningSummaryData() {
//		String typeId = "Summary_" + GameIdType.Location_Span_Running.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<SummaryLocationSpanRunning> summaryList = distributedRepo.fetchDistributedListLocationSpanRunningSummaryByLimit(timestamp, insertLimit);
//			if(summaryList == null || summaryList.size() == 0) {
//				break;
//			}
//			for(SummaryLocationSpanRunning ds: summaryList) {
//				result = centerRepo.updateLocationSpanRunningSummaryData(ds);
//				if(result != null && result.getErrorType() == null) {
//					distributedRepo.updateTimeStampByTypeId(typeId, result.getTimestamp());
//				}else {
//					break;
//				}
//			}
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncPhonologicalBindingSummaryData() {
//		String typeId = "Summary_" + GameIdType.Phonological_Binding.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<SummaryPhonologicalBinding> summaryList = distributedRepo.fetchDistributedListPhonologicalBindingSummaryByLimit(timestamp, insertLimit);
//			if(summaryList == null || summaryList.size() == 0) {
//				break;
//			}
//			for(SummaryPhonologicalBinding ds: summaryList) {
//				result = centerRepo.updatePhonologicalBindingSummaryData(ds);
//				if(result != null && result.getErrorType() == null) {
//					distributedRepo.updateTimeStampByTypeId(typeId, result.getTimestamp());
//				}else {
//					break;
//				}
//			}
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncVisualBindingSpanSummaryData() {
//		String typeId = "Summary_" + GameIdType.Visual_Binding_Span.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<SummaryVisualBindingSpan> summaryList = distributedRepo.fetchDistributedListVisualBindingSpanSummaryByLimit(timestamp, insertLimit);
//			if(summaryList == null || summaryList.size() == 0) {
//				break;
//			}
//			for(SummaryVisualBindingSpan ds: summaryList) {
//				result = centerRepo.updateVisualBindingSpanSummaryData(ds);
//				if(result != null && result.getErrorType() == null) {
//					distributedRepo.updateTimeStampByTypeId(typeId, result.getTimestamp());
//				}else {
//					break;
//				}
//			}
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncVisualSpanSummaryData() {
//		String typeId = "Summary_" + GameIdType.Visual_Span.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<SummaryVisualSpan> summaryList = distributedRepo.fetchDistributedListVisualSpanSummaryByLimit(timestamp, insertLimit);
//			if(summaryList == null || summaryList.size() == 0) {
//				break;
//			}
//			for(SummaryVisualSpan ds: summaryList) {
//				result = centerRepo.updateVisualSpanSummaryData(ds);
//				if(result != null && result.getErrorType() == null) {
//					distributedRepo.updateTimeStampByTypeId(typeId, result.getTimestamp());
//				}else {
//					break;
//				}
//			}
//		}
//		return result;
//	}
//	
//	private SyncDataResult syncVisualSpanRunningSummaryData() {
//		String typeId = "Summary_" + GameIdType.Visual_Span_Running.toString();
//		SyncDataRecord dataTimestamp = distributedRepo.fetchTimeStampByTypeId(typeId);
//		Long timestamp = 0L;
//		if(dataTimestamp != null) {
//			timestamp = dataTimestamp.getTimeStamp();
//		}
//		SyncDataResult result = new SyncDataResult();
//		int loop = 0;
//		while(loop < maxLoop) {
//			loop++;
//			List<SummaryVisualSpanRunning> summaryList = distributedRepo.fetchDistributedListVisualSpanRunningSummaryByLimit(timestamp, insertLimit);
//			if(summaryList == null || summaryList.size() == 0) {
//				break;
//			}
//			for(SummaryVisualSpanRunning ds: summaryList) {
//				result = centerRepo.updateVisualSpanRunningSummaryData(ds);
//				if(result != null && result.getErrorType() == null) {
//					distributedRepo.updateTimeStampByTypeId(typeId, result.getTimestamp());
//				}else {
//					break;
//				}
//			}
//		}
//		return result;
//	}
}