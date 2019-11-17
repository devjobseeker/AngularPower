package org.asu.chilll.power.repository.feature;

import java.util.ArrayList;
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
import org.asu.chilll.power.entity.feature.DummyTestConnection;
import org.asu.chilll.power.entity.feature.SyncDataResult;
import org.asu.chilll.power.entity.pk.GameProgressPK;
import org.asu.chilll.power.entity.pk.StudentProfilePK;
import org.asu.chilll.power.enums.SyncDataErrorType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

//@Repository
//@Transactional("centerTransactionManager")
//@Transactional
public class SyncDataCenterRepository {
	
//	@PersistenceContext(unitName = "centerEntityManagerFactory")
	//@PersistenceContext
//	private EntityManager em;
	
//	public boolean fetchTestConnection(String testId) {
//		em.find(DummyTestConnection.class, testId);
//		return true;
//	}
	
//	public SyncDataResult updateGameProgress(GameProgress progress) {
//		try{
//			GameProgressPK pk = new GameProgressPK();
//			pk.setChildId(progress.getChildId());
//			pk.setGrade(progress.getGrade());
////			pk.setYear(progress.getYear());
//			pk.setGameId(progress.getGameId());
//			GameProgress dbProgress = em.find(GameProgress.class, pk);
//			if(dbProgress != null) {
//				//map
//				dbProgress.setGameStatus(progress.getGameStatus());
//				dbProgress.setNextListIndex(progress.getNextListIndex());
//				dbProgress.setCurrentGroupIndex(progress.getCurrentGroupIndex());
//				dbProgress.setConsecutiveTrialResult(progress.getConsecutiveTrialResult());
//				dbProgress.setCoins(progress.getCoins());
//				dbProgress.setRocks(progress.getRocks());
//				dbProgress.setTotalCorrectCount(progress.getTotalCorrectCount());
//				dbProgress.setTotalTrialCount(progress.getTotalTrialCount());
//				dbProgress.setRepetitionCount(progress.getRepetitionCount());
//				dbProgress.setNumOfBoxes(progress.getNumOfBoxes());
//				dbProgress.setCreateDate(progress.getCreateDate());
//				dbProgress.setCreateTime(progress.getCreateTime());
//				dbProgress.setLastUpdateDate(progress.getLastUpdateDate());
//				dbProgress.setLastUpdateTime(progress.getLastUpdateTime());
//				
//				em.merge(dbProgress);
//			}else {
//				em.persist(progress);
//			}
//			return new SyncDataResult(progress.getLastUpdateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult updateStudentProfile(StudentProfile profile) {
//		try{
//			StudentProfilePK pk = new StudentProfilePK();
//			pk.setChildId(profile.getChildId());
//			pk.setGrade(profile.getGrade());
////			pk.setYear(profile.getYear());
//			StudentProfile dbProfile = em.find(StudentProfile.class, pk);
//			if(dbProfile != null) {
//				//map
//				dbProfile.setPirateCharacter(profile.getPirateCharacter());
//				dbProfile.setIntroCompleted(profile.getIntroCompleted());
//				dbProfile.setDay1Completed(profile.getDay1Completed());
//				dbProfile.setDay2Completed(profile.getDay2Completed());
//				dbProfile.setCreateDate(profile.getCreateDate());
//				dbProfile.setCreateTime(profile.getCreateTime());
//				dbProfile.setLastUpdateDate(profile.getLastUpdateDate());
//				dbProfile.setLastUpdateTime(profile.getLastUpdateTime());
//				em.merge(dbProfile);
//			}else {
//				em.persist(profile);
//			}
//			return new SyncDataResult(profile.getLastUpdateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createCrossModalBindingDetailData(List<DetailCrossModalBinding> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailCrossModalBinding ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createDigitSpanDetailData(List<DetailDigitSpan> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailDigitSpan ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createDigitSpanRunningDetailData(List<DetailDigitSpanRunning> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailDigitSpanRunning ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createLocationSpanDetailData(List<DetailLocationSpan> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailLocationSpan ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createLocationSpanRunningDetailData(List<DetailLocationSpanRunning> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailLocationSpanRunning ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createNumberUpdateVisualDetailData(List<DetailNumberUpdateVisual> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailNumberUpdateVisual ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createPhonologicalBindingDetailData(List<DetailPhonologicalBinding> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailPhonologicalBinding ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createVisualBindingSpanDetailData(List<DetailVisualBindingSpan> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailVisualBindingSpan ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createVisualSpanDetailData(List<DetailVisualSpan> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailVisualSpan ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createVisualSpanRunningDetailData(List<DetailVisualSpanRunning> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailVisualSpanRunning ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createNonWordDetailData(List<DetailNonWord> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailNonWord ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createRepetitionAuditoryDetailData(List<DetailRepetitionAuditory> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailRepetitionAuditory ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult createRepetitionVisualDetailData(List<DetailRepetitionVisual> data) {
//		if(data == null || data.size() == 0) {
//			return new SyncDataResult(SyncDataErrorType.No_Record.name(), SyncDataErrorType.No_Record.toString());
//		}
//		
//		try {
//			for(DetailRepetitionVisual ds: data) {
//				em.persist(ds);			
//			}
//			return new SyncDataResult(data.get(data.size() - 1).getCreateTime());
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult updateCrossModalBindingSummaryData(SummaryCrossModalBinding data) {
//		try {
//			SummaryCrossModalBinding summary = em.find(SummaryCrossModalBinding.class, data.getChildId_grade());
//			if(summary != null) {
//				summary.setExperimenter(data.getExperimenter());
//				summary.setCmbpi1(data.getCmbpi1());
//				summary.setCmbpi2(data.getCmbpi2());
//				summary.setCmbpi3(data.getCmbpi3());
//				summary.setCmbi21(data.getCmbi21());
//				summary.setCmbi22(data.getCmbi22());
//				summary.setCmbi23(data.getCmbi23());
//				summary.setCmbi24(data.getCmbi24());
//				summary.setCmbi31(data.getCmbi31());
//				summary.setCmbi32(data.getCmbi32());
//				summary.setCmbi33(data.getCmbi33());
//				summary.setCmbi34(data.getCmbi34());
//				summary.setCmbi41(data.getCmbi41());
//				summary.setCmbi42(data.getCmbi42());
//				summary.setCmbi43(data.getCmbi43());
//				summary.setCmbi44(data.getCmbi44());
//				
//				summary.setCmbpt1(data.getCmbpt1());
//				summary.setCmbpt2(data.getCmbpt2());
//				summary.setCmbpt3(data.getCmbpt3());
//				summary.setCmbt21(data.getCmbt21());
//				summary.setCmbt22(data.getCmbt22());
//				summary.setCmbt23(data.getCmbt23());
//				summary.setCmbt24(data.getCmbt24());
//				summary.setCmbt31(data.getCmbt31());
//				summary.setCmbt32(data.getCmbt32());
//				summary.setCmbt33(data.getCmbt33());
//				summary.setCmbt34(data.getCmbt34());
//				summary.setCmbt41(data.getCmbt41());
//				summary.setCmbt42(data.getCmbt42());
//				summary.setCmbt43(data.getCmbt43());
//				summary.setCmbt44(data.getCmbt44());
//				
//				summary.setCreateDate(data.getCreateDate());
//				summary.setCreateTime(data.getCreateTime());
//				summary.setLastUpdateDate(data.getLastUpdateDate());
//				summary.setLastUpdateTime(data.getLastUpdateTime());
//				em.merge(summary);
//			}else {
//				em.persist(data);
//			}
//			return new SyncDataResult(data.getLastUpdateTime()); 
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult updateDigitSpanSummaryData(SummaryDigitSpan data) {
//		try {
//			SummaryDigitSpan summary = em.find(SummaryDigitSpan.class, data.getChildId_grade());
//			if(summary != null) {
//				summary.setExperimenter(data.getExperimenter());
//				summary.setDspi1(data.getDspi1());
//				summary.setDspi2(data.getDspi2());
//				summary.setDspi3(data.getDspi3());
//				summary.setDsi21(data.getDsi21());
//				summary.setDsi22(data.getDsi22());
//				summary.setDsi23(data.getDsi23());
//				summary.setDsi24(data.getDsi24());
//				summary.setDsi31(data.getDsi31());
//				summary.setDsi32(data.getDsi32());
//				summary.setDsi33(data.getDsi33());
//				summary.setDsi34(data.getDsi34());
//				summary.setDsi41(data.getDsi41());
//				summary.setDsi42(data.getDsi42());
//				summary.setDsi43(data.getDsi43());
//				summary.setDsi44(data.getDsi44());
//				summary.setDsi51(data.getDsi51());
//				summary.setDsi52(data.getDsi52());
//				summary.setDsi53(data.getDsi53());
//				summary.setDsi54(data.getDsi54());
//				summary.setDsi61(data.getDsi61());
//				summary.setDsi62(data.getDsi62());
//				summary.setDsi63(data.getDsi63());
//				summary.setDsi64(data.getDsi64());
//				summary.setDsi71(data.getDsi71());
//				summary.setDsi72(data.getDsi72());
//				summary.setDsi73(data.getDsi73());
//				summary.setDsi74(data.getDsi74());
//				summary.setDsi81(data.getDsi81());
//				summary.setDsi82(data.getDsi82());
//				summary.setDsi83(data.getDsi83());
//				summary.setDsi84(data.getDsi84());
//				
//				summary.setDspt1(data.getDspt1());
//				summary.setDspt2(data.getDspt2());
//				summary.setDspt3(data.getDspt3());
//				summary.setDst21(data.getDst21());
//				summary.setDst22(data.getDst22());
//				summary.setDst23(data.getDst23());
//				summary.setDst24(data.getDst24());
//				summary.setDst31(data.getDst31());
//				summary.setDst32(data.getDst32());
//				summary.setDst33(data.getDst33());
//				summary.setDst34(data.getDst34());
//				summary.setDst41(data.getDst41());
//				summary.setDst42(data.getDst42());
//				summary.setDst43(data.getDst43());
//				summary.setDst44(data.getDst44());
//				summary.setDst51(data.getDst51());
//				summary.setDst52(data.getDst52());
//				summary.setDst53(data.getDst53());
//				summary.setDst54(data.getDst54());
//				summary.setDst61(data.getDst61());
//				summary.setDst62(data.getDst62());
//				summary.setDst63(data.getDst63());
//				summary.setDst64(data.getDst64());
//				summary.setDst71(data.getDst71());
//				summary.setDst72(data.getDst72());
//				summary.setDst73(data.getDst73());
//				summary.setDst74(data.getDst74());
//				summary.setDst81(data.getDst81());
//				summary.setDst82(data.getDst82());
//				summary.setDst83(data.getDst83());
//				summary.setDst84(data.getDst84());
//				
//				summary.setCreateDate(data.getCreateDate());
//				summary.setCreateTime(data.getCreateTime());
//				summary.setLastUpdateDate(data.getLastUpdateDate());
//				summary.setLastUpdateTime(data.getLastUpdateTime());
//				em.merge(summary);
//			}else {
//				em.persist(data);
//			}
//			return new SyncDataResult(data.getLastUpdateTime()); 
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult updateDigitSpanRunningSummaryData(SummaryDigitSpanRunning data) {
//		try {
//			SummaryDigitSpanRunning summary = em.find(SummaryDigitSpanRunning.class, data.getChildId_grade());
//			if(summary != null) {
//				summary.setExperimenter(data.getExperimenter());
//				summary.setDsrp1(data.getDsrp1());
//				summary.setDsrp2(data.getDsrp2());
//				summary.setDsrp3(data.getDsrp3());
//				summary.setDsr71(data.getDsr71());
//				summary.setDsr72(data.getDsr72());
//				summary.setDsr73(data.getDsr73());
//				summary.setDsr74(data.getDsr74());
//				
//				summary.setDsr81(data.getDsr81());
//				summary.setDsr82(data.getDsr82());
//				summary.setDsr83(data.getDsr83());
//				summary.setDsr84(data.getDsr84());
//				
//				summary.setDsr91(data.getDsr91());
//				summary.setDsr92(data.getDsr92());
//				summary.setDsr93(data.getDsr93());
//				summary.setDsr94(data.getDsr94());
//				
//				summary.setDsr101(data.getDsr101());
//				summary.setDsr102(data.getDsr102());
//				summary.setDsr103(data.getDsr103());
//				summary.setDsr104(data.getDsr104());
//				
//				summary.setCreateDate(data.getCreateDate());
//				summary.setCreateTime(data.getCreateTime());
//				summary.setLastUpdateDate(data.getLastUpdateDate());
//				summary.setLastUpdateTime(data.getLastUpdateTime());
//				em.merge(summary);
//			}else {
//				em.persist(data);
//			}
//			return new SyncDataResult(data.getLastUpdateTime()); 
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult updateLocationSpanSummaryData(SummaryLocationSpan data) {
//		try {
//			SummaryLocationSpan summary = em.find(SummaryLocationSpan.class, data.getChildId_grade());
//			if(summary != null) {
//				summary.setExperimenter(data.getExperimenter());
//				summary.setLspi1(data.getLspi1());
//				summary.setLspi2(data.getLspi2());
//				summary.setLspi3(data.getLspi3());
//				summary.setLsi21(data.getLsi21());
//				summary.setLsi22(data.getLsi22());
//				summary.setLsi23(data.getLsi23());
//				summary.setLsi24(data.getLsi24());
//				summary.setLsi31(data.getLsi31());
//				summary.setLsi32(data.getLsi32());
//				summary.setLsi33(data.getLsi33());
//				summary.setLsi34(data.getLsi34());
//				summary.setLsi41(data.getLsi41());
//				summary.setLsi42(data.getLsi42());
//				summary.setLsi43(data.getLsi43());
//				summary.setLsi44(data.getLsi44());
//				summary.setLsi51(data.getLsi51());
//				summary.setLsi52(data.getLsi52());
//				summary.setLsi53(data.getLsi53());
//				summary.setLsi54(data.getLsi54());
//				summary.setLsi61(data.getLsi61());
//				summary.setLsi62(data.getLsi62());
//				summary.setLsi63(data.getLsi63());
//				summary.setLsi64(data.getLsi64());
//				
//				summary.setLspt1(data.getLspt1());
//				summary.setLspt2(data.getLspt2());
//				summary.setLspt3(data.getLspt3());
//				summary.setLst21(data.getLst21());
//				summary.setLst22(data.getLst22());
//				summary.setLst23(data.getLst23());
//				summary.setLst24(data.getLst24());
//				summary.setLst31(data.getLst31());
//				summary.setLst32(data.getLst32());
//				summary.setLst33(data.getLst33());
//				summary.setLst34(data.getLst34());
//				summary.setLst41(data.getLst41());
//				summary.setLst42(data.getLst42());
//				summary.setLst43(data.getLst43());
//				summary.setLst44(data.getLst44());
//				summary.setLst51(data.getLst51());
//				summary.setLst52(data.getLst52());
//				summary.setLst53(data.getLst53());
//				summary.setLst54(data.getLst54());
//				summary.setLst61(data.getLst61());
//				summary.setLst62(data.getLst62());
//				summary.setLst63(data.getLst63());
//				summary.setLst64(data.getLst64());
//				
//				summary.setCreateDate(data.getCreateDate());
//				summary.setCreateTime(data.getCreateTime());
//				summary.setLastUpdateDate(data.getLastUpdateDate());
//				summary.setLastUpdateTime(data.getLastUpdateTime());
//				em.merge(summary);
//			}else {
//				em.persist(data);
//			}
//			return new SyncDataResult(data.getLastUpdateTime()); 
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult updateLocationSpanRunningSummaryData(SummaryLocationSpanRunning data) {
//		try {
//			SummaryLocationSpanRunning summary = em.find(SummaryLocationSpanRunning.class, data.getChildId_grade());
//			if(summary != null) {
//				summary.setExperimenter(data.getExperimenter());
//				summary.setLsrp1(data.getLsrp1());
//				summary.setLsrp2(data.getLsrp2());
//				summary.setLsrp3(data.getLsrp3());
//				summary.setLsr51(data.getLsr51());
//				summary.setLsr52(data.getLsr52());
//				summary.setLsr53(data.getLsr53());
//				summary.setLsr54(data.getLsr54());
//				summary.setLsr61(data.getLsr61());
//				summary.setLsr62(data.getLsr62());
//				summary.setLsr63(data.getLsr63());
//				summary.setLsr64(data.getLsr64());
//				summary.setLsr71(data.getLsr71());
//				summary.setLsr72(data.getLsr72());
//				summary.setLsr73(data.getLsr73());
//				summary.setLsr74(data.getLsr74());
//				summary.setLsr81(data.getLsr81());
//				summary.setLsr82(data.getLsr82());
//				summary.setLsr83(data.getLsr83());
//				summary.setLsr84(data.getLsr84());
//				
//				summary.setCreateDate(data.getCreateDate());
//				summary.setCreateTime(data.getCreateTime());
//				summary.setLastUpdateDate(data.getLastUpdateDate());
//				summary.setLastUpdateTime(data.getLastUpdateTime());
//				em.merge(summary);
//			}else {
//				em.persist(data);
//			}
//			return new SyncDataResult(data.getLastUpdateTime()); 
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult updatePhonologicalBindingSummaryData(SummaryPhonologicalBinding data) {
//		try {
//			SummaryPhonologicalBinding summary = em.find(SummaryPhonologicalBinding.class, data.getChildId_grade());
//			if(summary != null) {
//				summary.setExperimenter(data.getExperimenter());
//				summary.setPbspi1(data.getPbspi1());
//				summary.setPbspi2(data.getPbspi2());
//				summary.setPbspi3(data.getPbspi3());
//				summary.setPbsi21(data.getPbsi21());
//				summary.setPbsi22(data.getPbsi22());
//				summary.setPbsi23(data.getPbsi23());
//				summary.setPbsi24(data.getPbsi24());
//				summary.setPbsi31(data.getPbsi31());
//				summary.setPbsi32(data.getPbsi32());
//				summary.setPbsi33(data.getPbsi33());
//				summary.setPbsi34(data.getPbsi34());
//				summary.setPbsi41(data.getPbsi41());
//				summary.setPbsi42(data.getPbsi42());
//				summary.setPbsi43(data.getPbsi43());
//				summary.setPbsi44(data.getPbsi44());
//				
//				summary.setPbspt1(data.getPbspt1());
//				summary.setPbspt2(data.getPbspt2());
//				summary.setPbspt3(data.getPbspt3());
//				summary.setPbst21(data.getPbst21());
//				summary.setPbst22(data.getPbst22());
//				summary.setPbst23(data.getPbst23());
//				summary.setPbst24(data.getPbst24());
//				summary.setPbst31(data.getPbst31());
//				summary.setPbst32(data.getPbst32());
//				summary.setPbst33(data.getPbst33());
//				summary.setPbst34(data.getPbst34());
//				summary.setPbst41(data.getPbst41());
//				summary.setPbst42(data.getPbst42());
//				summary.setPbst43(data.getPbst43());
//				summary.setPbst44(data.getPbst44());
//				
//				summary.setCreateDate(data.getCreateDate());
//				summary.setCreateTime(data.getCreateTime());
//				summary.setLastUpdateDate(data.getLastUpdateDate());
//				summary.setLastUpdateTime(data.getLastUpdateTime());
//				em.merge(summary);
//			}else {
//				em.persist(data);
//			}
//			return new SyncDataResult(data.getLastUpdateTime()); 
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult updateVisualBindingSpanSummaryData(SummaryVisualBindingSpan data) {
//		try {
//			SummaryVisualBindingSpan summary = em.find(SummaryVisualBindingSpan.class, data.getChildId_grade());
//			if(summary != null) {
//				summary.setExperimenter(data.getExperimenter());
//				summary.setVbspi1(data.getVbspi1());
//				summary.setVbspi2(data.getVbspi2());
//				summary.setVbspi3(data.getVbspi3());
//				summary.setVbsi21(data.getVbsi21());
//				summary.setVbsi22(data.getVbsi22());
//				summary.setVbsi23(data.getVbsi23());
//				summary.setVbsi24(data.getVbsi24());
//				summary.setVbsi31(data.getVbsi31());
//				summary.setVbsi32(data.getVbsi32());
//				summary.setVbsi33(data.getVbsi33());
//				summary.setVbsi34(data.getVbsi34());
//				summary.setVbsi41(data.getVbsi41());
//				summary.setVbsi42(data.getVbsi42());
//				summary.setVbsi43(data.getVbsi43());
//				summary.setVbsi44(data.getVbsi44());
//				
//				summary.setVbspt1(data.getVbspt1());
//				summary.setVbspt2(data.getVbspt2());
//				summary.setVbspt3(data.getVbspt3());
//				summary.setVbst21(data.getVbst21());
//				summary.setVbst22(data.getVbst22());
//				summary.setVbst23(data.getVbst23());
//				summary.setVbst24(data.getVbst24());
//				summary.setVbst31(data.getVbst31());
//				summary.setVbst32(data.getVbst32());
//				summary.setVbst33(data.getVbst33());
//				summary.setVbst34(data.getVbst34());
//				summary.setVbst41(data.getVbst41());
//				summary.setVbst42(data.getVbst42());
//				summary.setVbst43(data.getVbst43());
//				summary.setVbst44(data.getVbst44());
//				
//				summary.setCreateDate(data.getCreateDate());
//				summary.setCreateTime(data.getCreateTime());
//				summary.setLastUpdateDate(data.getLastUpdateDate());
//				summary.setLastUpdateTime(data.getLastUpdateTime());
//				em.merge(summary);
//			}else {
//				em.persist(data);
//			}
//			return new SyncDataResult(data.getLastUpdateTime()); 
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult updateVisualSpanSummaryData(SummaryVisualSpan data) {
//		try {
//			SummaryVisualSpan summary = em.find(SummaryVisualSpan.class, data.getChildId_grade());
//			if(summary != null) {
//				summary.setExperimenter(data.getExperimenter());
//				summary.setVspi1(data.getVspi1());
//				summary.setVspi2(data.getVspi2());
//				summary.setVspi3(data.getVspi3());
//				summary.setVsi21(data.getVsi21());
//				summary.setVsi22(data.getVsi22());
//				summary.setVsi23(data.getVsi23());
//				summary.setVsi24(data.getVsi24());
//				summary.setVsi31(data.getVsi31());
//				summary.setVsi32(data.getVsi32());
//				summary.setVsi33(data.getVsi33());
//				summary.setVsi34(data.getVsi34());
//				summary.setVsi41(data.getVsi41());
//				summary.setVsi42(data.getVsi42());
//				summary.setVsi43(data.getVsi43());
//				summary.setVsi44(data.getVsi44());
//				summary.setVsi51(data.getVsi51());
//				summary.setVsi52(data.getVsi52());
//				summary.setVsi53(data.getVsi53());
//				summary.setVsi54(data.getVsi54());
//				summary.setVsi61(data.getVsi61());
//				summary.setVsi62(data.getVsi62());
//				summary.setVsi63(data.getVsi63());
//				summary.setVsi64(data.getVsi64());
//				
//				summary.setVspt1(data.getVspt1());
//				summary.setVspt2(data.getVspt2());
//				summary.setVspt3(data.getVspt3());
//				summary.setVst21(data.getVst21());
//				summary.setVst22(data.getVst22());
//				summary.setVst23(data.getVst23());
//				summary.setVst24(data.getVst24());
//				summary.setVst31(data.getVst31());
//				summary.setVst32(data.getVst32());
//				summary.setVst33(data.getVst33());
//				summary.setVst34(data.getVst34());
//				summary.setVst41(data.getVst41());
//				summary.setVst42(data.getVst42());
//				summary.setVst43(data.getVst43());
//				summary.setVst44(data.getVst44());
//				summary.setVst51(data.getVst51());
//				summary.setVst52(data.getVst52());
//				summary.setVst53(data.getVst53());
//				summary.setVst54(data.getVst54());
//				summary.setVst61(data.getVst61());
//				summary.setVst62(data.getVst62());
//				summary.setVst63(data.getVst63());
//				summary.setVst64(data.getVst64());
//				
//				summary.setCreateDate(data.getCreateDate());
//				summary.setCreateTime(data.getCreateTime());
//				summary.setLastUpdateDate(data.getLastUpdateDate());
//				summary.setLastUpdateTime(data.getLastUpdateTime());
//				em.merge(summary);
//			}else {
//				em.persist(data);
//			}
//			return new SyncDataResult(data.getLastUpdateTime()); 
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public SyncDataResult updateVisualSpanRunningSummaryData(SummaryVisualSpanRunning data) {
//		try {
//			SummaryVisualSpanRunning summary = em.find(SummaryVisualSpanRunning.class, data.getChildId_grade());
//			if(summary != null) {
//				summary.setExperimenter(data.getExperimenter());
//				summary.setVsrp1(data.getVsrp1());
//				summary.setVsrp2(data.getVsrp2());
//				summary.setVsrp3(data.getVsrp3());
//				
//				summary.setVsr31(data.getVsr31());
//				summary.setVsr32(data.getVsr32());
//				summary.setVsr33(data.getVsr33());
//				summary.setVsr34(data.getVsr34());
//				summary.setVsr41(data.getVsr41());
//				summary.setVsr42(data.getVsr42());
//				summary.setVsr43(data.getVsr43());
//				summary.setVsr44(data.getVsr44());
//				summary.setVsr51(data.getVsr51());
//				summary.setVsr52(data.getVsr52());
//				summary.setVsr53(data.getVsr53());
//				summary.setVsr54(data.getVsr54());
//				summary.setVsr61(data.getVsr61());
//				summary.setVsr62(data.getVsr62());
//				summary.setVsr63(data.getVsr63());
//				summary.setVsr64(data.getVsr64());
//				
//				summary.setCreateDate(data.getCreateDate());
//				summary.setCreateTime(data.getCreateTime());
//				summary.setLastUpdateDate(data.getLastUpdateDate());
//				summary.setLastUpdateTime(data.getLastUpdateTime());
//				em.merge(summary);
//			}else {
//				em.persist(data);
//			}
//			return new SyncDataResult(data.getLastUpdateTime()); 
//		}catch(Exception e) {
//			e.printStackTrace(System.out);
//			return new SyncDataResult(SyncDataErrorType.Other.name(), SyncDataErrorType.Other.toString());
//		}finally {
//			em.close();
//		}
//	}
//	
//	public List<DetailCrossModalBinding> fetchListCrossModalBindingByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailCrossModalBinding>();
//		}
//		return em.createNamedQuery("DetailCrossModalBinding.fetchListCrossModalBindingByUids", DetailCrossModalBinding.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
//	
//	public List<DetailDigitSpan> fetchListDigitSpanByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailDigitSpan>();
//		}
//		return em.createNamedQuery("DetailDigitSpan.fetchListDigitSpanByUids", DetailDigitSpan.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
//	
//	public List<DetailDigitSpanRunning> fetchListDigitSpanRunningByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailDigitSpanRunning>();
//		}
//		return em.createNamedQuery("DetailDigitSpanRunning.fetchListDigitSpanRunningByUids", DetailDigitSpanRunning.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
//	
//	public List<DetailLocationSpan> fetchListLocationSpanByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailLocationSpan>();
//		}
//		return em.createNamedQuery("DetailLocationSpan.fetchListLocationSpanByUids", DetailLocationSpan.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
//	
//	public List<DetailLocationSpanRunning> fetchListLocationSpanRunningByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailLocationSpanRunning>();
//		}
//		return em.createNamedQuery("DetailLocationSpanRunning.fetchListLocationSpanRunningByUids", DetailLocationSpanRunning.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
//	
//	public List<DetailNumberUpdateVisual> fetchListNumberUpdateVisualByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailNumberUpdateVisual>();
//		}
//		return em.createNamedQuery("DetailNumberUpdateVisual.fetchListNumberUpdateVisualByUids", DetailNumberUpdateVisual.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
//	
//	public List<DetailPhonologicalBinding> fetchListPhonologicalBindingByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailPhonologicalBinding>();
//		}
//		return em.createNamedQuery("DetailPhonologicalBinding.fetchListPhonologicalBindingByUids", DetailPhonologicalBinding.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
//	
//	public List<DetailVisualBindingSpan> fetchListVisualBindingSpanByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailVisualBindingSpan>();
//		}
//		return em.createNamedQuery("DetailVisualBindingSpan.fetchListVisualBindingSpanByUids", DetailVisualBindingSpan.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
//	
//	public List<DetailVisualSpan> fetchListVisualSpanByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailVisualSpan>();
//		}
//		return em.createNamedQuery("DetailVisualSpan.fetchListVisualSpanByUids", DetailVisualSpan.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
//	
//	public List<DetailVisualSpanRunning> fetchListVisualSpanRunningByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailVisualSpanRunning>();
//		}
//		return em.createNamedQuery("DetailVisualSpanRunning.fetchListVisualSpanRunningByUids", DetailVisualSpanRunning.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
//	
//	public List<DetailNonWord> fetchListNonWordByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailNonWord>();
//		}
//		return em.createNamedQuery("DetailNonWord.fetchListNonWordByUids", DetailNonWord.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
//	
//	public List<DetailRepetitionAuditory> fetchListRepetitionAuditoryByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailRepetitionAuditory>();
//		}
//		return em.createNamedQuery("DetailRepetitionAuditory.fetchListRepetitionAuditoryByUids", DetailRepetitionAuditory.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
//	
//	public List<DetailRepetitionVisual> fetchListRepetitionVisualByUids(List<String> uids){
//		if(uids == null || uids.size() == 0) {
//			return new ArrayList<DetailRepetitionVisual>();
//		}
//		return em.createNamedQuery("DetailRepetitionVisual.fetchListRepetitionVisualByUids", DetailRepetitionVisual.class)
//				.setParameter("uids", uids)
//				.getResultList();
//	}
}