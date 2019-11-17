package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.repetitiondetection.RepetitionDetectionUserData;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.data.DetailRepetitionVisual;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.repository.GameRepository;
import org.asu.chilll.power.repository.RepetitionVisualRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RepetitionVisualService {
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private RepetitionVisualRepository repo;
	
	public GameProgressDataView saveData(RepetitionDetectionUserData userData) {
		DetailRepetitionVisual detailData = createDetailData(userData);
		return updateCoinsAndRocksInGameProgress(userData, detailData);
	}
	
	private DetailRepetitionVisual createDetailData(RepetitionDetectionUserData userData) {
		DetailRepetitionVisual detail = new DetailRepetitionVisual();
		detail.setChildId(userData.getChildId());
		detail.setGrade(userData.getGrade());
		detail.setExperimenter(userData.getExperimenter());
		detail.setStimuliInput1(userData.getStimuliInput1());
		detail.setStimuliInput2(userData.getStimuliInput2());
		detail.setStimuliInput3(userData.getStimuliInput3());
		detail.setRepetitionCount(userData.getRepetitionCount());
		detail.setUserInput(userData.getUserInput());
		detail.setBlockIndex(userData.getBlockIndex() + 1);
		if(detail.getRepetitionCount() == 2) {
			detail.setTrialIndex(userData.getTrialIndex());
		}else {
			detail.setTrialIndex(userData.getTrialIndex() - 1);
		}
		
		if(userData.getStartTime() != null && userData.getEndTime() != null) {
			detail.setResponseTime(userData.getEndTime() - userData.getStartTime());
		}
		detail.setTrialType(userData.getTrialType());
		detail.setCorrectRepsonse("different");
		if(detail.getRepetitionCount() != null && detail.getRepetitionCount() == 2) {
			detail.setStimuliInput3("X");
			if(detail.getStimuliInput1() != null && detail.getStimuliInput2() != null && detail.getStimuliInput1().equals(detail.getStimuliInput2())) {
				detail.setCorrectRepsonse("same");
			}
		}else if(detail.getRepetitionCount() != null && detail.getRepetitionCount() == 3) {
			if(detail.getStimuliInput1() != null && detail.getStimuliInput2() != null && detail.getStimuliInput3() != null 
					&& detail.getStimuliInput1().equals(detail.getStimuliInput2()) && detail.getStimuliInput1().equals(detail.getStimuliInput3())) {
				detail.setCorrectRepsonse("same");
			}
		}
		
		detail.setScore(detail.getUserInput() != null && detail.getCorrectRepsonse().equals(detail.getUserInput()) ? 1 : 0);
		if(userData.getNeedCheck() != null && userData.getNeedCheck()) {
			detail.setComment("*");
		}
		return repo.createDetailData(detail);
	}

	private GameProgressDataView updateCoinsAndRocksInGameProgress(RepetitionDetectionUserData userData, DetailRepetitionVisual detailData) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(userData.getChildId(), userData.getGrade(), GameIdType.Repetition_Detection_Visual.toString());
		if(dbProgress != null) {
			//rock will not increase in practice trial. if user answer all digit correct in one list length, coin + 1.
			if(userData.getTrialType() == null || !userData.getTrialType().equals("practice")) {
				dbProgress.setNextListIndex(userData.getTrialIndex() + 1);
				dbProgress.setCurrentGroupIndex(userData.getBlockIndex());
				
				if(dbProgress.getRocks() != null) {
					dbProgress.setRocks(detailData.getScore() == 0 ? dbProgress.getRocks() + 1 : dbProgress.getRocks());
				}else {
					dbProgress.setRocks(detailData.getScore() == 0 ? 1 : 0);
				}
				//if it's real trial, save total correct count and total trial count
				if(detailData.getScore() == 1) {
					dbProgress.setTotalCorrectCount(dbProgress.getTotalCorrectCount() == null ? 1 : dbProgress.getTotalCorrectCount() + 1);
				}
				dbProgress.setTotalTrialCount(dbProgress.getTotalTrialCount() == null ? 1 : dbProgress.getTotalTrialCount() + 1);
				
				if(dbProgress.getCoins() != null) {
					dbProgress.setCoins(detailData.getScore() > 0 ? dbProgress.getCoins() + 1 : dbProgress.getCoins());
				}else {
					dbProgress.setCoins(detailData.getScore() > 0 ? 1 : 0);
				}
			}
			
			dbProgress.setRepetitionCount(userData.getRepetitionCount());
			gameRepo.updateGameProgress(dbProgress);
			GameProgressDataView dv = new GameProgressDataView();
			dv.setChildId(dbProgress.getChildId());
			dv.setCoins(dbProgress.getCoins());
			dv.setRocks(dbProgress.getRocks());
			return dv;
		}
		return null;
	}
}
