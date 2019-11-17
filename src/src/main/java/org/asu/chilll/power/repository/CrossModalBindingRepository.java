package org.asu.chilll.power.repository;

import java.util.Date;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.asu.chilll.power.entity.data.DetailCrossModalBinding;
import org.asu.chilll.power.entity.data.SummaryCrossModalBinding;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class CrossModalBindingRepository {
	@PersistenceContext
	private EntityManager em;
	
	public DetailCrossModalBinding createDetailData(DetailCrossModalBinding data) {
		data.setUid(UUID.randomUUID().toString());
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryCrossModalBinding fecthSummaryData(String childId_grade) {
		return em.find(SummaryCrossModalBinding.class, childId_grade);
	}
	
	public SummaryCrossModalBinding createSummaryData(SummaryCrossModalBinding data) {
		Date date = new Date();
		data.setCreateDate(date);
		data.setCreateTime(date.getTime());
		data.setLastUpdateDate(date);
		data.setLastUpdateTime(date.getTime());
		em.persist(data);
		return data;
	}
	
	public SummaryCrossModalBinding updateSummaryData(SummaryCrossModalBinding data) {
		SummaryCrossModalBinding dbData = em.find(SummaryCrossModalBinding.class, data.getChildId_grade());
		if(dbData != null) {
			dbData.setCmbpi1(data.getCmbpi1());
			dbData.setCmbpi2(data.getCmbpi2());
			dbData.setCmbpi3(data.getCmbpi3());
			dbData.setCmbi21(data.getCmbi21());
			dbData.setCmbi22(data.getCmbi22());
			dbData.setCmbi23(data.getCmbi23());
			dbData.setCmbi24(data.getCmbi24());
			dbData.setCmbi31(data.getCmbi31());
			dbData.setCmbi32(data.getCmbi32());
			dbData.setCmbi33(data.getCmbi33());
			dbData.setCmbi34(data.getCmbi34());
			dbData.setCmbi41(data.getCmbi41());
			dbData.setCmbi42(data.getCmbi42());
			dbData.setCmbi43(data.getCmbi43());
			dbData.setCmbi44(data.getCmbi44());
			
			dbData.setCmbpt1(data.getCmbpt1());
			dbData.setCmbpt2(data.getCmbpt2());
			dbData.setCmbpt3(data.getCmbpt3());
			dbData.setCmbt21(data.getCmbt21());
			dbData.setCmbt22(data.getCmbt22());
			dbData.setCmbt23(data.getCmbt23());
			dbData.setCmbt24(data.getCmbt24());
			dbData.setCmbt31(data.getCmbt31());
			dbData.setCmbt32(data.getCmbt32());
			dbData.setCmbt33(data.getCmbt33());
			dbData.setCmbt34(data.getCmbt34());
			dbData.setCmbt41(data.getCmbt41());
			dbData.setCmbt42(data.getCmbt42());
			dbData.setCmbt43(data.getCmbt43());
			dbData.setCmbt44(data.getCmbt44());
			
			Date date = new Date();
			dbData.setLastUpdateDate(date);
			dbData.setLastUpdateTime(date.getTime());
			em.merge(dbData);
		}
		
		return data;
	}
}
