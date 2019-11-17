package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.nonword.NonWordUserData;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.data.DetailNonWord;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.repository.GameRepository;
import org.asu.chilll.power.repository.NonWordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NonWordService {
	
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private NonWordRepository repo;
	
	private static final int attemptReward = 3;
	
	public GameProgressDataView saveData(NonWordUserData userData) {
		DetailNonWord detailData = createDetailData(userData);
		return updateCoinsAndRocksInGameProgress(userData, detailData);
	}
	
	private DetailNonWord createDetailData(NonWordUserData userData) {
		DetailNonWord detail = new DetailNonWord();
		detail.setChildId(userData.getChildId());
		detail.setGrade(userData.getGrade());
		detail.setExperimenter(userData.getExperimenter());
		detail.setTrialNo(userData.getCurrentListIndex() + 1);
		detail.setStimuliNonWordInput(userData.getStimuliInput());
		detail.setUserInputFileName(userData.getUserInputFileName());
		detail.setUserInputFileUid(userData.getUserInputFileUid());
		if(userData.getEndTime() != null && userData.getStartTime() != null) {
			detail.setResponseTime(userData.getEndTime() - userData.getStartTime());
		}
		if(userData.getUserAnswerCorrect() != null && userData.getUserAnswerCorrect()) {
			detail.setScore(1);
		}else {
			detail.setScore(0);
		}
		if(userData.getNeedCheck() != null && userData.getNeedCheck()) {
			detail.setComment("*");
		}
		detail.setTrialType(userData.getTrialType());
		return repo.createDetailData(detail);
	}
	
	private GameProgressDataView updateCoinsAndRocksInGameProgress(NonWordUserData userData, DetailNonWord detailData) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(userData.getChildId(), userData.getGrade(), GameIdType.Nonword_Repetition.toString());
		if(dbProgress != null) {
			if(userData.getTrialType() == null || !userData.getTrialType().equals("practice")) {
				dbProgress.setNextListIndex(userData.getCurrentListIndex() + 1);
				
				if(dbProgress.getRocks() != null) {
					dbProgress.setRocks(detailData.getScore() == 0 ? dbProgress.getRocks() + 1 : dbProgress.getRocks());
				}else {
					dbProgress.setRocks(detailData.getScore() == 0 ? 1 : 0);
				}
				
				if(dbProgress.getCoins() != null) {
					dbProgress.setCoins(detailData.getScore() > 0 ? dbProgress.getCoins() + attemptReward : dbProgress.getCoins());
				}else {
					dbProgress.setCoins(detailData.getScore() > 0 ? attemptReward : 0);
				}
			}
			
			if(detailData.getScore() == 1) {
				dbProgress.setTotalCorrectCount(dbProgress.getTotalCorrectCount() == null ? 1 : dbProgress.getTotalCorrectCount() + 1);
			}
			dbProgress.setTotalTrialCount(dbProgress.getTotalTrialCount() == null ? 1 : dbProgress.getTotalTrialCount() + 1);
			gameRepo.updateGameProgress(dbProgress);
			GameProgressDataView dv = new GameProgressDataView();
			dv.setChildId(dbProgress.getChildId());
			dv.setCoins(dbProgress.getCoins());
			dv.setRocks(dbProgress.getRocks());
			dv.setTotalCorrectCount(dbProgress.getTotalCorrectCount());
			dv.setTotalTrialCount(dbProgress.getTotalTrialCount());
			return dv;
		}
		return null;
	}
}