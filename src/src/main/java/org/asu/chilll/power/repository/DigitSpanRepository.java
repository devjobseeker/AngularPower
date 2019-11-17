package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.data.DetailDigitSpan;
import org.asu.chilll.power.entity.data.SummaryDigitSpan;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class DigitSpanRepository {
	
	@PersistenceContext
	private EntityManager em;
	
	public SummaryDigitSpan createDigitSpanSummaryData(SummaryDigitSpan data) {
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		data.setLastUpdateDate(date);
		data.setLastUpdateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryDigitSpan updateDigitSpanSummaryData(SummaryDigitSpan data) {
		SummaryDigitSpan dbData = em.find(SummaryDigitSpan.class, data.getChildId_grade());
		if(dbData != null) {
			dbData.setDspi1(data.getDspi1());
			dbData.setDspi2(data.getDspi2());
			dbData.setDspi3(data.getDspi3());
			dbData.setDsi21(data.getDsi21());
			dbData.setDsi22(data.getDsi22());
			dbData.setDsi23(data.getDsi23());
			dbData.setDsi24(data.getDsi24());
			dbData.setDsi31(data.getDsi31());
			dbData.setDsi32(data.getDsi32());
			dbData.setDsi33(data.getDsi33());
			dbData.setDsi34(data.getDsi34());
			dbData.setDsi41(data.getDsi41());
			dbData.setDsi42(data.getDsi42());
			dbData.setDsi43(data.getDsi43());
			dbData.setDsi44(data.getDsi44());
			dbData.setDsi51(data.getDsi51());
			dbData.setDsi52(data.getDsi52());
			dbData.setDsi53(data.getDsi53());
			dbData.setDsi54(data.getDsi54());
			dbData.setDsi61(data.getDsi61());
			dbData.setDsi62(data.getDsi62());
			dbData.setDsi63(data.getDsi63());
			dbData.setDsi64(data.getDsi64());
			dbData.setDsi71(data.getDsi71());
			dbData.setDsi72(data.getDsi72());
			dbData.setDsi73(data.getDsi73());
			dbData.setDsi74(data.getDsi74());
			dbData.setDsi81(data.getDsi81());
			dbData.setDsi82(data.getDsi82());
			dbData.setDsi83(data.getDsi83());
			dbData.setDsi84(data.getDsi84());
			
			dbData.setDspt1(data.getDspt1());
			dbData.setDspt2(data.getDspt2());
			dbData.setDspt3(data.getDspt3());
			dbData.setDst21(data.getDst21());
			dbData.setDst22(data.getDst22());
			dbData.setDst23(data.getDst23());
			dbData.setDst24(data.getDst24());
			dbData.setDst31(data.getDst31());
			dbData.setDst32(data.getDst32());
			dbData.setDst33(data.getDst33());
			dbData.setDst34(data.getDst34());
			dbData.setDst41(data.getDst41());
			dbData.setDst42(data.getDst42());
			dbData.setDst43(data.getDst43());
			dbData.setDst44(data.getDst44());
			dbData.setDst51(data.getDst51());
			dbData.setDst52(data.getDst52());
			dbData.setDst53(data.getDst53());
			dbData.setDst54(data.getDst54());
			dbData.setDst61(data.getDst61());
			dbData.setDst62(data.getDst62());
			dbData.setDst63(data.getDst63());
			dbData.setDst64(data.getDst64());
			dbData.setDst71(data.getDst71());
			dbData.setDst72(data.getDst72());
			dbData.setDst73(data.getDst73());
			dbData.setDst74(data.getDst74());
			dbData.setDst81(data.getDst81());
			dbData.setDst82(data.getDst82());
			dbData.setDst83(data.getDst83());
			dbData.setDst84(data.getDst84());
			
			Date date = new Date();
			dbData.setLastUpdateDate(date);
			dbData.setLastUpdateTime(date.getTime());
			em.merge(dbData);
		}
		
		return data;
	}
	
	public SummaryDigitSpan fecthDigitSpanSummaryData(String childId_grade) {
		return em.find(SummaryDigitSpan.class, childId_grade);
	}
	
	public DetailDigitSpan createDigitSpanDetailData(DetailDigitSpan data) {
		data.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		em.persist(data);
		return data;
	}
}
