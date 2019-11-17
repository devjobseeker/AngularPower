package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.data.DetailVisualSpanRunning;
import org.asu.chilll.power.entity.data.SummaryVisualSpanRunning;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class VisualSpanRunningRepository {
	@PersistenceContext
	private EntityManager em;
	
	public DetailVisualSpanRunning createDetailData(DetailVisualSpanRunning data) {
		data.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryVisualSpanRunning fecthSummaryData(String childId_grade) {
		return em.find(SummaryVisualSpanRunning.class, childId_grade);
	}
	
	public SummaryVisualSpanRunning createSummaryData(SummaryVisualSpanRunning data) {
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		data.setLastUpdateDate(date);
		data.setLastUpdateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryVisualSpanRunning updateSummaryData(SummaryVisualSpanRunning data) {
		SummaryVisualSpanRunning dbData = em.find(SummaryVisualSpanRunning.class, data.getChildId_grade());
		if(dbData != null) {
			dbData.setVsrp1(data.getVsrp1());
			dbData.setVsrp2(data.getVsrp2());
			dbData.setVsrp3(data.getVsrp3());
			dbData.setVsr31(data.getVsr31());
			dbData.setVsr32(data.getVsr32());
			dbData.setVsr33(data.getVsr33());
			dbData.setVsr34(data.getVsr34());
			dbData.setVsr41(data.getVsr41());
			dbData.setVsr42(data.getVsr42());
			dbData.setVsr43(data.getVsr43());
			dbData.setVsr44(data.getVsr44());
			dbData.setVsr51(data.getVsr51());
			dbData.setVsr52(data.getVsr52());
			dbData.setVsr53(data.getVsr53());
			dbData.setVsr54(data.getVsr54());
			dbData.setVsr61(data.getVsr61());
			dbData.setVsr62(data.getVsr62());
			dbData.setVsr63(data.getVsr63());
			dbData.setVsr64(data.getVsr64());
			
			Date date = new Date();
			dbData.setLastUpdateDate(date);
			dbData.setLastUpdateTime(date.getTime());
			em.merge(dbData);
		}
		
		return data;
	}
}
