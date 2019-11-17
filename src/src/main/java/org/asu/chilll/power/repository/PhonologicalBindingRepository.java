package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.data.DetailPhonologicalBinding;
import org.asu.chilll.power.entity.data.SummaryPhonologicalBinding;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class PhonologicalBindingRepository {
	@PersistenceContext
	private EntityManager em;
	
	public DetailPhonologicalBinding createDetailData(DetailPhonologicalBinding data) {
		data.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryPhonologicalBinding fecthSummaryData(String childId_grade) {
		return em.find(SummaryPhonologicalBinding.class, childId_grade);
	}
	
	public SummaryPhonologicalBinding createSummaryData(SummaryPhonologicalBinding data) {
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		data.setLastUpdateDate(date);
		data.setLastUpdateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryPhonologicalBinding updateSummaryData(SummaryPhonologicalBinding data) {
		SummaryPhonologicalBinding dbData = em.find(SummaryPhonologicalBinding.class, data.getChildId_grade());
		if(dbData != null) {
			dbData.setPbspi1(data.getPbspi1());
			dbData.setPbspi2(data.getPbspi2());
			dbData.setPbspi3(data.getPbspi3());
			dbData.setPbsi21(data.getPbsi21());
			dbData.setPbsi22(data.getPbsi22());
			dbData.setPbsi23(data.getPbsi23());
			dbData.setPbsi24(data.getPbsi24());
			dbData.setPbsi31(data.getPbsi31());
			dbData.setPbsi32(data.getPbsi32());
			dbData.setPbsi33(data.getPbsi33());
			dbData.setPbsi34(data.getPbsi34());
			dbData.setPbsi41(data.getPbsi41());
			dbData.setPbsi42(data.getPbsi42());
			dbData.setPbsi43(data.getPbsi43());
			dbData.setPbsi44(data.getPbsi44());
			
			dbData.setPbspt1(data.getPbspt1());
			dbData.setPbspt2(data.getPbspt2());
			dbData.setPbspt3(data.getPbspt3());
			dbData.setPbst21(data.getPbst21());
			dbData.setPbst22(data.getPbst22());
			dbData.setPbst23(data.getPbst23());
			dbData.setPbst24(data.getPbst24());
			dbData.setPbst31(data.getPbst31());
			dbData.setPbst32(data.getPbst32());
			dbData.setPbst33(data.getPbst33());
			dbData.setPbst34(data.getPbst34());
			dbData.setPbst41(data.getPbst41());
			dbData.setPbst42(data.getPbst42());
			dbData.setPbst43(data.getPbst43());
			dbData.setPbst44(data.getPbst44());
			
			Date date = new Date();
			dbData.setLastUpdateDate(date);
			dbData.setLastUpdateTime(date.getTime());
			em.merge(dbData);
		}
		
		return data;
	}
}