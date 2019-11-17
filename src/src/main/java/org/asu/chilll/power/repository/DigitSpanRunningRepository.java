package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.data.DetailDigitSpanRunning;
import org.asu.chilll.power.entity.data.SummaryDigitSpanRunning;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DigitSpanRunningRepository {
	@PersistenceContext
	private EntityManager em;
	
	public SummaryDigitSpanRunning createSummaryData(SummaryDigitSpanRunning data) {
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		data.setLastUpdateDate(date);
		data.setLastUpdateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryDigitSpanRunning updateSummaryData(SummaryDigitSpanRunning data) {
		SummaryDigitSpanRunning dbData = em.find(SummaryDigitSpanRunning.class, data.getChildId_grade());
		if(dbData != null) {
			dbData.setDsrp1(data.getDsrp1());
			dbData.setDsrp2(data.getDsrp2());
			dbData.setDsrp3(data.getDsrp3());
			dbData.setDsr71(data.getDsr71());
			dbData.setDsr72(data.getDsr72());
			dbData.setDsr73(data.getDsr73());
			dbData.setDsr74(data.getDsr74());
			
			dbData.setDsr81(data.getDsr81());
			dbData.setDsr82(data.getDsr82());
			dbData.setDsr83(data.getDsr83());
			dbData.setDsr84(data.getDsr84());
			
			dbData.setDsr91(data.getDsr91());
			dbData.setDsr92(data.getDsr92());
			dbData.setDsr93(data.getDsr93());
			dbData.setDsr94(data.getDsr94());
			
			dbData.setDsr101(data.getDsr101());
			dbData.setDsr102(data.getDsr102());
			dbData.setDsr103(data.getDsr103());
			dbData.setDsr104(data.getDsr104());
			
			Date date = new Date();
			dbData.setLastUpdateDate(date);
			dbData.setLastUpdateTime(date.getTime());
			em.merge(dbData);
		}
		
		return data;
	}
	
	public SummaryDigitSpanRunning fecthSummaryData(String childId_grade) {
		return em.find(SummaryDigitSpanRunning.class, childId_grade);
	}
	
	public DetailDigitSpanRunning createDetailData(DetailDigitSpanRunning data) {
		data.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		em.persist(data);
		return data;
	}
}
