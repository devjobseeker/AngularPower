package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.data.DetailVisualSpan;
import org.asu.chilll.power.entity.data.SummaryVisualSpan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class VisualSpanRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public DetailVisualSpan createDetailData(DetailVisualSpan data) {
		data.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryVisualSpan fecthSummaryData(String childId_grade) {
		return em.find(SummaryVisualSpan.class, childId_grade);
	}
	
	public SummaryVisualSpan createSummaryData(SummaryVisualSpan data) {
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		data.setLastUpdateDate(date);
		data.setLastUpdateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryVisualSpan updateSummaryData(SummaryVisualSpan data) {
		SummaryVisualSpan dbData = em.find(SummaryVisualSpan.class, data.getChildId_grade());
		if(dbData != null) {
			dbData.setVspi1(data.getVspi1());
			dbData.setVspi2(data.getVspi2());
			dbData.setVspi3(data.getVspi3());
			dbData.setVsi21(data.getVsi21());
			dbData.setVsi22(data.getVsi22());
			dbData.setVsi23(data.getVsi23());
			dbData.setVsi24(data.getVsi24());
			dbData.setVsi31(data.getVsi31());
			dbData.setVsi32(data.getVsi32());
			dbData.setVsi33(data.getVsi33());
			dbData.setVsi34(data.getVsi34());
			dbData.setVsi41(data.getVsi41());
			dbData.setVsi42(data.getVsi42());
			dbData.setVsi43(data.getVsi43());
			dbData.setVsi44(data.getVsi44());
			dbData.setVsi51(data.getVsi51());
			dbData.setVsi52(data.getVsi52());
			dbData.setVsi53(data.getVsi53());
			dbData.setVsi54(data.getVsi54());
			dbData.setVsi61(data.getVsi61());
			dbData.setVsi62(data.getVsi62());
			dbData.setVsi63(data.getVsi63());
			dbData.setVsi64(data.getVsi64());
			
			dbData.setVspt1(data.getVspt1());
			dbData.setVspt2(data.getVspt2());
			dbData.setVspt3(data.getVspt3());
			dbData.setVst21(data.getVst21());
			dbData.setVst22(data.getVst22());
			dbData.setVst23(data.getVst23());
			dbData.setVst24(data.getVst24());
			dbData.setVst31(data.getVst31());
			dbData.setVst32(data.getVst32());
			dbData.setVst33(data.getVst33());
			dbData.setVst34(data.getVst34());
			dbData.setVst41(data.getVst41());
			dbData.setVst42(data.getVst42());
			dbData.setVst43(data.getVst43());
			dbData.setVst44(data.getVst44());
			dbData.setVst51(data.getVst51());
			dbData.setVst52(data.getVst52());
			dbData.setVst53(data.getVst53());
			dbData.setVst54(data.getVst54());
			dbData.setVst61(data.getVst61());
			dbData.setVst62(data.getVst62());
			dbData.setVst63(data.getVst63());
			dbData.setVst64(data.getVst64());
			
			Date date = new Date();
			dbData.setLastUpdateDate(date);
			dbData.setLastUpdateTime(date.getTime());
			em.merge(dbData);
		}
		
		return data;
	}
}
