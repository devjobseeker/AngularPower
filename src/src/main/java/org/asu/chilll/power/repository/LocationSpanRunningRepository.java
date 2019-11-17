package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.data.DetailLocationSpanRunning;
import org.asu.chilll.power.entity.data.SummaryLocationSpanRunning;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LocationSpanRunningRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public DetailLocationSpanRunning createDetailData(DetailLocationSpanRunning data) {
		data.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryLocationSpanRunning fecthSummaryData(String childId_grade) {
		return em.find(SummaryLocationSpanRunning.class, childId_grade);
	}
	
	public SummaryLocationSpanRunning createSummaryData(SummaryLocationSpanRunning data) {
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		data.setLastUpdateDate(date);
		data.setLastUpdateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryLocationSpanRunning updateSummaryData(SummaryLocationSpanRunning data) {
		SummaryLocationSpanRunning dbData = em.find(SummaryLocationSpanRunning.class, data.getChildId_grade());
		if(dbData != null) {
			dbData.setLsrp1(data.getLsrp1());
			dbData.setLsrp2(data.getLsrp2());
			dbData.setLsrp3(data.getLsrp3());
			dbData.setLsr51(data.getLsr51());
			dbData.setLsr52(data.getLsr52());
			dbData.setLsr53(data.getLsr53());
			dbData.setLsr54(data.getLsr54());
			dbData.setLsr61(data.getLsr61());
			dbData.setLsr62(data.getLsr62());
			dbData.setLsr63(data.getLsr63());
			dbData.setLsr64(data.getLsr64());
			dbData.setLsr71(data.getLsr71());
			dbData.setLsr72(data.getLsr72());
			dbData.setLsr73(data.getLsr73());
			dbData.setLsr74(data.getLsr74());
			dbData.setLsr81(data.getLsr81());
			dbData.setLsr82(data.getLsr82());
			dbData.setLsr83(data.getLsr83());
			dbData.setLsr84(data.getLsr84());
			
			Date date = new Date();
			dbData.setLastUpdateDate(date);
			dbData.setLastUpdateTime(date.getTime());
			em.merge(dbData);
		}
		
		return data;
	}
}
