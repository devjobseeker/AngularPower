package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.data.DetailLocationSpan;
import org.asu.chilll.power.entity.data.SummaryLocationSpan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class LocationSpanRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public DetailLocationSpan createDetailData(DetailLocationSpan data) {
		data.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryLocationSpan fecthSummaryData(String childId_grade) {
		return em.find(SummaryLocationSpan.class, childId_grade);
	}
	
	public SummaryLocationSpan createSummaryData(SummaryLocationSpan data) {
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		data.setLastUpdateDate(date);
		data.setLastUpdateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryLocationSpan updateSummaryData(SummaryLocationSpan data) {
		SummaryLocationSpan dbData = em.find(SummaryLocationSpan.class, data.getChildId_grade());
		if(dbData != null) {
			dbData.setLspi1(data.getLspi1());
			dbData.setLspi2(data.getLspi2());
			dbData.setLspi3(data.getLspi3());
			dbData.setLsi21(data.getLsi21());
			dbData.setLsi22(data.getLsi22());
			dbData.setLsi23(data.getLsi23());
			dbData.setLsi24(data.getLsi24());
			dbData.setLsi31(data.getLsi31());
			dbData.setLsi32(data.getLsi32());
			dbData.setLsi33(data.getLsi33());
			dbData.setLsi34(data.getLsi34());
			dbData.setLsi41(data.getLsi41());
			dbData.setLsi42(data.getLsi42());
			dbData.setLsi43(data.getLsi43());
			dbData.setLsi44(data.getLsi44());
			dbData.setLsi51(data.getLsi51());
			dbData.setLsi52(data.getLsi52());
			dbData.setLsi53(data.getLsi53());
			dbData.setLsi54(data.getLsi54());
			dbData.setLsi61(data.getLsi61());
			dbData.setLsi62(data.getLsi62());
			dbData.setLsi63(data.getLsi63());
			dbData.setLsi64(data.getLsi64());
			
			dbData.setLspt1(data.getLspt1());
			dbData.setLspt2(data.getLspt2());
			dbData.setLspt3(data.getLspt3());
			dbData.setLst21(data.getLst21());
			dbData.setLst22(data.getLst22());
			dbData.setLst23(data.getLst23());
			dbData.setLst24(data.getLst24());
			dbData.setLst31(data.getLst31());
			dbData.setLst32(data.getLst32());
			dbData.setLst33(data.getLst33());
			dbData.setLst34(data.getLst34());
			dbData.setLst41(data.getLst41());
			dbData.setLst42(data.getLst42());
			dbData.setLst43(data.getLst43());
			dbData.setLst44(data.getLst44());
			dbData.setLst51(data.getLst51());
			dbData.setLst52(data.getLst52());
			dbData.setLst53(data.getLst53());
			dbData.setLst54(data.getLst54());
			dbData.setLst61(data.getLst61());
			dbData.setLst62(data.getLst62());
			dbData.setLst63(data.getLst63());
			dbData.setLst64(data.getLst64());
			
			Date date = new Date();
			dbData.setLastUpdateDate(date);
			dbData.setLastUpdateTime(date.getTime());
			em.merge(dbData);
		}
		
		return data;
	}
}