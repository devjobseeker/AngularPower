package org.asu.chilll.power.repository.feature;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import org.asu.chilll.power.entity.data.SummaryCrossModalBinding;
import org.asu.chilll.power.entity.data.SummaryDigitSpan;
import org.asu.chilll.power.entity.data.SummaryDigitSpanRunning;
import org.asu.chilll.power.entity.data.SummaryLocationSpan;
import org.asu.chilll.power.entity.data.SummaryLocationSpanRunning;
import org.asu.chilll.power.entity.data.SummaryPhonologicalBinding;
import org.asu.chilll.power.entity.data.SummaryVisualBindingSpan;
import org.asu.chilll.power.entity.data.SummaryVisualSpan;
import org.asu.chilll.power.entity.data.SummaryVisualSpanRunning;
import org.asu.chilll.power.entity.feature.SyncDataRecord;
import org.asu.chilll.power.entity.feature.SyncDataResult;
import org.asu.chilll.power.entity.pk.SyncDataPK;
import org.asu.chilll.power.enums.SyncDataErrorType;
import org.asu.chilll.power.enums.SyncDataStatusType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SyncDataREDCapRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public SyncDataRecord fetchSyncDataRecordByPK(String childId, String grade, String gameId, String category) {
		SyncDataPK pk = new SyncDataPK();
		pk.setChildId(childId);
		pk.setGrade(grade);
		pk.setGameId(gameId);
		pk.setCategory(category);
		return em.find(SyncDataRecord.class, pk);
	}
	
	public SyncDataRecord updateSyncDataRecord(SyncDataRecord record) {
		SyncDataPK pk = new SyncDataPK();
		pk.setChildId(record.getChildId());
		pk.setGrade(record.getGrade());
		pk.setGameId(record.getGameId());
		pk.setCategory(record.getCategory());
		SyncDataRecord db_record = em.find(SyncDataRecord.class, pk);
		if(db_record != null) {
			db_record.setRedcapTimestamp(record.getRedcapTimestamp());
			db_record.setRedcapSyncStatus(record.getRedcapSyncStatus());
			em.merge(db_record);
		}
		return record;
	}
	
	public List<SyncDataRecord> fetchSyncDataRecordList(String childId, String grade, String gameId){
		return em.createNamedQuery("SyncDataRecord.fetchSyncDataRecordList", SyncDataRecord.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("gameId", gameId)
				.getResultList();
	}
	
	public SyncDataResult createSyncDataRecord(SyncDataRecord record) {
		record.setRedcapTimestamp(new Date().getTime());
		em.persist(record);
		return new SyncDataResult(1L);
	}
	
	public SyncDataResult createSyncDataRecordList(List<SyncDataRecord> syncDataRecords){
		try {
			long affectedRowsCount = 0L;
			for(SyncDataRecord record: syncDataRecords) {
				record.setRedcapTimestamp(new Date().getTime());
				em.persist(record);
				affectedRowsCount++;
			}
			return new SyncDataResult(affectedRowsCount);
		}catch(Exception e) {
			e.printStackTrace(System.out);
			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
		}finally {
			em.close();
		}
	}
	
	public SyncDataResult updateSyncDataRecordStatusList(List<SyncDataRecord> syncDataRecords) {
		if(syncDataRecords == null || syncDataRecords.size() == 0) {
			return new SyncDataResult(0L);
		}
		long affectedRowsCount = 0L;
		for(SyncDataRecord record: syncDataRecords) {
			SyncDataPK pk = new SyncDataPK();
			pk.setCategory(record.getCategory());
			pk.setChildId(record.getChildId());
			pk.setGameId(record.getGameId());
			pk.setGrade(record.getGrade());
			SyncDataRecord db_record = em.find(SyncDataRecord.class, pk);
			if(db_record != null) {
				db_record.setRedcapSyncStatus(SyncDataStatusType.Pending.toString());
				em.merge(db_record);
				affectedRowsCount++;
			}
		}
		return new SyncDataResult(affectedRowsCount);
	}
	
	public List<StudentProfile> fetchStudentProfileByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("StudentProfile.fetchStudentProfileByTimestamp", StudentProfile.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("lastUpdateTime", timestamp)
				.getResultList();
	}
	
	public List<SyncDataRecord> fetchSyncDataRecordListByREDCapStatus(String redcapStatus){
		return em.createNamedQuery("SyncDataRecord.fetchSyncDataRecordListByREDCapStatus", SyncDataRecord.class)
				.setParameter("redcapSyncStatus", redcapStatus)
				.getResultList();
	}
	
	//cross modal binding
	public Long fetchPendingDetailCrossModalBindingRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailCrossModalBinding.fetchPendingDetailCrossModalBindingRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailCrossModalBinding> fetchDetailCrossModalBindingListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailCrossModalBinding.fetchDetailCrossModalBindingListByTimestamp", DetailCrossModalBinding.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	public SummaryCrossModalBinding fecthCrossModalBindingSummaryData(String childId_grade) {
		return em.find(SummaryCrossModalBinding.class, childId_grade);
	}
	
	//digit span
	public Long fetchPendingDetailDigitSpanRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailDigitSpan.fetchPendingDetailDigitSpanRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailDigitSpan> fetchDetailDigitSpanListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailDigitSpan.fetchDetailDigitSpanListByTimestamp", DetailDigitSpan.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	public SummaryDigitSpan fecthDigitSpanSummaryData(String childId_grade) {
		return em.find(SummaryDigitSpan.class, childId_grade);
	}
	
	//digit span running
	public Long fetchPendingDetailDigitSpanRunningRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailDigitSpanRunning.fetchPendingDetailDigitSpanRunningRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailDigitSpanRunning> fetchDetailDigitSpanRunningListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailDigitSpanRunning.fetchDetailDigitSpanRunningListByTimestamp", DetailDigitSpanRunning.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	public SummaryDigitSpanRunning fecthDigitSpanRunningSummaryData(String childId_grade) {
		return em.find(SummaryDigitSpanRunning.class, childId_grade);
	}
	
	//location span
	public Long fetchPendingDetailLocationSpanRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailLocationSpan.fetchPendingDetailLocationSpanRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailLocationSpan> fetchDetailLocationSpanListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailLocationSpan.fetchDetailLocationSpanListByTimestamp", DetailLocationSpan.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	public SummaryLocationSpan fecthLocationSpanSummaryData(String childId_grade) {
		return em.find(SummaryLocationSpan.class, childId_grade);
	}
	
	//location span running
	public Long fetchPendingDetailLocationSpanRunningRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailLocationSpanRunning.fetchPendingDetailLocationSpanRunningRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailLocationSpanRunning> fetchDetailLocationSpanRunningListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailLocationSpanRunning.fetchDetailLocationSpanRunningListByTimestamp", DetailLocationSpanRunning.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	public SummaryLocationSpanRunning fecthLocactionSpanRunningSummaryData(String childId_grade) {
		return em.find(SummaryLocationSpanRunning.class, childId_grade);
	}
	
	//non word
	public Long fetchPendingDetailNonWordRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailNonWord.fetchPendingDetailNonWordRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailNonWord> fetchDetailNonWordListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailNonWord.fetchDetailNonWordListByTimestamp", DetailNonWord.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	
	//number update auditory
	public Long fetchPendingDetailNumberUpdateAuditoryRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailNumberUpdateAuditory.fetchPendingDetailNumberUpdateAuditoryRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailNumberUpdateAuditory> fetchDetailNumberUpdateAuditoryListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailNumberUpdateAuditory.fetchDetailNumberUpdateAuditoryListByTimestamp", DetailNumberUpdateAuditory.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	
	//number udpdate visual
	public Long fetchPendingDetailNumberUpdateVisualRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailNumberUpdateVisual.fetchPendingDetailNumberUpdateVisualRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailNumberUpdateVisual> fetchDetailNumberUpdateVisualListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailNumberUpdateVisual.fetchDetailNumberUpdateVisualListByTimestamp", DetailNumberUpdateVisual.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	
	//phonological binding
	public Long fetchPendingDetailPhonologicalBindingRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailPhonologicalBinding.fetchPendingDetailPhonologicalBindingRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailPhonologicalBinding> fetchDetailPhonologicalBindingListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailPhonologicalBinding.fetchDetailPhonologicalBindingListByTimestamp", DetailPhonologicalBinding.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	public SummaryPhonologicalBinding fecthPhonologicalBindingSummaryData(String childId_grade) {
		return em.find(SummaryPhonologicalBinding.class, childId_grade);
	}
	
	//repetition auditory
	public Long fetchPendingDetailRepetitionAuditoryRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailRepetitionAuditory.fetchPendingDetailRepetitionAuditoryRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailRepetitionAuditory> fetchDetailRepetitionAuditoryListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailRepetitionAuditory.fetchDetailRepetitionAuditoryListByTimestamp", DetailRepetitionAuditory.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	
	//repetition visual
	public Long fetchPendingDetailRepetitionVisualRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailRepetitionVisual.fetchPendingDetailRepetitionVisualRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailRepetitionVisual> fetchDetailRepetitionVisualListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailRepetitionVisual.fetchDetailRepetitionVisualListByTimestamp", DetailRepetitionVisual.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	
	//visual binding
	public Long fetchPendingDetailVisualBindingRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailVisualBindingSpan.fetchPendingDetailVisualBindingRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailVisualBindingSpan> fetchDetailVisualBindingListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailVisualBindingSpan.fetchDetailVisualBindingListByTimestamp", DetailVisualBindingSpan.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	public SummaryVisualBindingSpan fecthVisualBindingSummaryData(String childId_grade) {
		return em.find(SummaryVisualBindingSpan.class, childId_grade);
	}
	
	//visual span
	public Long fetchPendingDetailVisualSpanRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailVisualSpan.fetchPendingDetailVisualSpanRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailVisualSpan> fetchDetailVisualSpanListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailVisualSpan.fetchDetailVisualSpanListByTimestamp", DetailVisualSpan.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	public SummaryVisualSpan fecthVisualSpanSummaryData(String childId_grade) {
		return em.find(SummaryVisualSpan.class, childId_grade);
	}
	
	//visual span running
	public Long fetchPendingDetailVisualSpanRunningRecordTotalCount(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailVisualSpanRunning.fetchPendingDetailVisualSpanRunningRecordTotalCount", Long.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getSingleResult();
	}
	public List<DetailVisualSpanRunning> fetchDetailVisualSpanRunningListByTimestamp(String childId, String grade, Long timestamp){
		return em.createNamedQuery("DetailVisualSpanRunning.fetchDetailVisualSpanRunningListByTimestamp", DetailVisualSpanRunning.class)
				.setParameter("childId", childId)
				.setParameter("grade", grade)
				.setParameter("createTime", timestamp)
				.getResultList();
	}
	public SummaryVisualSpanRunning fecthVisualSpanRunningSummaryData(String childId_grade) {
		return em.find(SummaryVisualSpanRunning.class, childId_grade);
	}
}
