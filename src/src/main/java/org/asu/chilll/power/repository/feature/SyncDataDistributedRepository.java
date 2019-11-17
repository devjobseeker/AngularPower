package org.asu.chilll.power.repository.feature;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
import org.asu.chilll.power.entity.feature.SyncDataRecord;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class SyncDataDistributedRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public SyncDataRecord fetchTimeStampByTypeId(String typeId) {
		return em.find(SyncDataRecord.class, typeId);
	}
	
//	public SyncDataRecord updateTimeStampByTypeId(String typeId, Long time) {
//		SyncDataRecord timestamp = em.find(SyncDataRecord.class, typeId);
//		if(timestamp != null) {
//			timestamp.setTimeStamp(time);
//			em.merge(timestamp);
//		}else {
//			timestamp = new SyncDataRecord();
//			timestamp.setCategory(typeId);
//			timestamp.setTimeStamp(time);
//			em.persist(timestamp);
//		}
//		return timestamp;
//	}
	
	public List<GameProgress> fetchDistributedListGameProgressByLimit(Long lastUpdateTime, int limit){
		return em.createNamedQuery("GameProgress.fetchDistributedListGameProgressByLimit", GameProgress.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.setMaxResults(limit)
				.getResultList();
	}
	
	public List<StudentProfile> fetchDistributedListStudentProfileByLimit(Long lastUpdateTime, int limit){
		return em.createNamedQuery("StudentProfile.fetchDistributedListStudentProfileByLimit", StudentProfile.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.setMaxResults(limit)
				.getResultList();
	}
	
	public List<DetailCrossModalBinding> fetchDistributedListCrossModalBindingDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailCrossModalBinding.fetchDistributedListCrossModalBindingDetailByLimit", DetailCrossModalBinding.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<DetailDigitSpan> fetchDistributedListDigitSpanDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailDigitSpan.fetchDistributedListDigitSpanDetailByLimit", DetailDigitSpan.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<DetailDigitSpanRunning> fetchDistributedListDigitSpanRunningDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailDigitSpanRunning.fetchDistributedListDigitSpanRunningDetailByLimit", DetailDigitSpanRunning.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<DetailLocationSpan> fetchDistributedListLocationSpanDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailLocationSpan.fetchDistributedListLocationSpanDetailByLimit", DetailLocationSpan.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<DetailLocationSpanRunning> fetchDistributedListLocationSpanRunningDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailLocationSpanRunning.fetchDistributedListLocationSpanRunningDetailByLimit", DetailLocationSpanRunning.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<DetailNumberUpdateVisual> fetchDistributedListNumberUpdateVisualDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailNumberUpdateVisual.fetchDistributedListNumberUpdateVisualDetailByLimit", DetailNumberUpdateVisual.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<DetailPhonologicalBinding> fetchDistributedListPhonologicalBindingDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailPhonologicalBinding.fetchDistributedListPhonologicalBindingDetailByLimit", DetailPhonologicalBinding.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<DetailVisualBindingSpan> fetchDistributedListVisualBindingSpanDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailVisualBindingSpan.fetchDistributedListVisualBindingSpanDetailByLimit", DetailVisualBindingSpan.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<DetailVisualSpan> fetchDistributedListVisualSpanDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailVisualSpan.fetchDistributedListVisualSpanDetailByLimit", DetailVisualSpan.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<DetailVisualSpanRunning> fetchDistributedListVisualSpanRunningDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailVisualSpanRunning.fetchDistributedListVisualSpanRunningDetailByLimit", DetailVisualSpanRunning.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<DetailNonWord> fetchDistributedListNonWordDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailNonWord.fetchDistributedListNonWordDetailByLimit", DetailNonWord.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<DetailRepetitionAuditory> fetchDistributedListRepetitionAuditoryDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailRepetitionAuditory.fetchDistributedListRepetitionAuditoryDetailByLimit", DetailRepetitionAuditory.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<DetailRepetitionVisual> fetchDistributedListRepetitionVisualDetailByLimit(Long createTime, int limit) {
		return em.createNamedQuery("DetailRepetitionVisual.fetchDistributedListRepetitionVisualDetailByLimit", DetailRepetitionVisual.class)
			.setParameter("createTime", createTime)
			.setMaxResults(limit)
			.getResultList();
	}
	
	public List<SummaryCrossModalBinding> fetchDistributedListCrossModalBindingSummaryByLimit(Long lastUpdateTime, int limit){
		return em.createNamedQuery("SummaryCrossModalBinding.fetchDistributedListCrossModalBindingSummaryByLimit", SummaryCrossModalBinding.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.setMaxResults(limit)
				.getResultList();
	}
	
	public List<SummaryDigitSpan> fetchDistributedListDigitSpanSummaryByLimit(Long lastUpdateTime, int limit){
		return em.createNamedQuery("SummaryDigitSpan.fetchDistributedListDigitSpanSummaryByLimit", SummaryDigitSpan.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.setMaxResults(limit)
				.getResultList();
	}
	
	public List<SummaryDigitSpanRunning> fetchDistributedListDigitSpanRunningSummaryByLimit(Long lastUpdateTime, int limit){
		return em.createNamedQuery("SummaryDigitSpanRunning.fetchDistributedListDigitSpanRunningSummaryByLimit", SummaryDigitSpanRunning.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.setMaxResults(limit)
				.getResultList();
	}
	
	public List<SummaryLocationSpan> fetchDistributedListLocationSpanSummaryByLimit(Long lastUpdateTime, int limit){
		return em.createNamedQuery("SummaryLocationSpan.fetchDistributedListLocationSpanSummaryByLimit", SummaryLocationSpan.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.setMaxResults(limit)
				.getResultList();
	}
	
	public List<SummaryLocationSpanRunning> fetchDistributedListLocationSpanRunningSummaryByLimit(Long lastUpdateTime, int limit){
		return em.createNamedQuery("SummaryLocationSpanRunning.fetchDistributedListLocationSpanRunningSummaryByLimit", SummaryLocationSpanRunning.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.setMaxResults(limit)
				.getResultList();
	}
	
	public List<SummaryPhonologicalBinding> fetchDistributedListPhonologicalBindingSummaryByLimit(Long lastUpdateTime, int limit){
		return em.createNamedQuery("SummaryPhonologicalBinding.fetchDistributedListPhonologicalBindingSummaryByLimit", SummaryPhonologicalBinding.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.setMaxResults(limit)
				.getResultList();
	}
	
	public List<SummaryVisualBindingSpan> fetchDistributedListVisualBindingSpanSummaryByLimit(Long lastUpdateTime, int limit){
		return em.createNamedQuery("SummaryVisualBindingSpan.fetchDistributedListVisualBindingSpanSummaryByLimit", SummaryVisualBindingSpan.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.setMaxResults(limit)
				.getResultList();
	}
	
	public List<SummaryVisualSpan> fetchDistributedListVisualSpanSummaryByLimit(Long lastUpdateTime, int limit){
		return em.createNamedQuery("SummaryVisualSpan.fetchDistributedListVisualSpanSummaryByLimit", SummaryVisualSpan.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.setMaxResults(limit)
				.getResultList();
	}
	
	public List<SummaryVisualSpanRunning> fetchDistributedListVisualSpanRunningSummaryByLimit(Long lastUpdateTime, int limit){
		return em.createNamedQuery("SummaryVisualSpanRunning.fetchDistributedListVisualSpanRunningSummaryByLimit", SummaryVisualSpanRunning.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.setMaxResults(limit)
				.getResultList();
	}
	
	public Long fetchDistributedCrossModalBindingDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailCrossModalBinding.fetchDistributedCrossModalBindingDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedDigitSpanDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailDigitSpan.fetchDistributedDigitSpanDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedDigitSpanRunningDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailDigitSpanRunning.fetchDistributedDigitSpanRunningDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedLocationSpanDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailLocationSpan.fetchDistributedLocationSpanDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedLocationSpanRunningDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailLocationSpanRunning.fetchDistributedLocationSpanRunningDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedNumberUpdateVisualDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailNumberUpdateVisual.fetchDistributedNumberUpdateVisualDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedPhonologicalBindingDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailPhonologicalBinding.fetchDistributedPhonologicalBindingDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedVisualBindingSpanDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailVisualBindingSpan.fetchDistributedVisualBindingSpanDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedVisualSpanDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailVisualSpan.fetchDistributedVisualSpanDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedVisualSpanRunningDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailVisualSpanRunning.fetchDistributedVisualSpanRunningDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedNonWordDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailNonWord.fetchDistributedNonWordDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedRepetitionAuditoryDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailRepetitionAuditory.fetchDistributedRepetitionAuditoryDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedRepetitionVisualDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailRepetitionVisual.fetchDistributedRepetitionVisualDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedNumberUpdateAuditoryDetailTotalCount(Long createTime) {
		return em.createNamedQuery("DetailNumberUpdateAuditory.fetchDistributedNumberUpdateAuditoryDetailTotalCount", Long.class)
				.setParameter("createTime", createTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedGameProgressTotalCount(Long lastUpdateTime) {
		return em.createNamedQuery("GameProgress.fetchDistributedGameProgressTotalCount", Long.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.getSingleResult();
	}
	
	public Long fetchDistributedStudentProfileTotalCount(Long lastUpdateTime) {
		return em.createNamedQuery("StudentProfile.fetchDistributedStudentProfileTotalCount", Long.class)
				.setParameter("lastUpdateTime", lastUpdateTime)
				.getSingleResult();
	}
}