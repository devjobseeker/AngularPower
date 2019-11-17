package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.data.DetailVisualBindingSpan;
import org.asu.chilll.power.entity.data.SummaryVisualBindingSpan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class VisualBindingSpanRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public DetailVisualBindingSpan createDetailData(DetailVisualBindingSpan data) {
		data.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryVisualBindingSpan fecthSummaryData(String childId_grade) {
		return em.find(SummaryVisualBindingSpan.class, childId_grade);
	}
	
	public SummaryVisualBindingSpan createSummaryData(SummaryVisualBindingSpan data) {
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		data.setLastUpdateDate(date);
		data.setLastUpdateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryVisualBindingSpan updateSummaryData(SummaryVisualBindingSpan data) {
		SummaryVisualBindingSpan dbData = em.find(SummaryVisualBindingSpan.class, data.getChildId_grade());
		if(dbData != null) {
			dbData.setVbspi1(data.getVbspi1());
			dbData.setVbspi2(data.getVbspi2());
			dbData.setVbspi3(data.getVbspi3());
			dbData.setVbsi21(data.getVbsi21());
			dbData.setVbsi22(data.getVbsi22());
			dbData.setVbsi23(data.getVbsi23());
			dbData.setVbsi24(data.getVbsi24());
			dbData.setVbsi31(data.getVbsi31());
			dbData.setVbsi32(data.getVbsi32());
			dbData.setVbsi33(data.getVbsi33());
			dbData.setVbsi34(data.getVbsi34());
			dbData.setVbsi41(data.getVbsi41());
			dbData.setVbsi42(data.getVbsi42());
			dbData.setVbsi43(data.getVbsi43());
			dbData.setVbsi44(data.getVbsi44());
			
			dbData.setVbspt1(data.getVbspt1());
			dbData.setVbspt2(data.getVbspt2());
			dbData.setVbspt3(data.getVbspt3());
			dbData.setVbst21(data.getVbst21());
			dbData.setVbst22(data.getVbst22());
			dbData.setVbst23(data.getVbst23());
			dbData.setVbst24(data.getVbst24());
			dbData.setVbst31(data.getVbst31());
			dbData.setVbst32(data.getVbst32());
			dbData.setVbst33(data.getVbst33());
			dbData.setVbst34(data.getVbst34());
			dbData.setVbst41(data.getVbst41());
			dbData.setVbst42(data.getVbst42());
			dbData.setVbst43(data.getVbst43());
			dbData.setVbst44(data.getVbst44());
			
			Date date = new Date();
			dbData.setLastUpdateDate(date);
			dbData.setLastUpdateTime(date.getTime());
			em.merge(dbData);
		}
		
		return data;
	}
}
