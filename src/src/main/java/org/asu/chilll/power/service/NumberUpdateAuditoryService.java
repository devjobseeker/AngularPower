package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.numberupdate.NumberUpdateUserData;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.data.DetailNumberUpdateAuditory;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.repository.GameRepository;
import org.asu.chilll.power.repository.NumberUpdateAuditoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NumberUpdateAuditoryService {
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private NumberUpdateAuditoryRepository repo;
	
	private final static int leftIndex = 0;
	private final static int middleIndex = 1;
	
	public GameProgressDataView saveData(NumberUpdateUserData userData) {
		DetailNumberUpdateAuditory detailData = createDetailData(userData);
		return updateCoinsAndRocksInGameProgress(userData, detailData);
	}

	private DetailNumberUpdateAuditory createDetailData(NumberUpdateUserData userData) {
		DetailNumberUpdateAuditory detail = new DetailNumberUpdateAuditory();
		detail.setChildId(userData.getChildId());
		detail.setGrade(userData.getGrade());
		detail.setExperimenter(userData.getExperimenter());
		detail.setNumOfBoxes(userData.getNumOfBoxes());
		detail.setBlockIndex(userData.getCurrentGroupIndex() + 1);
		detail.setTrialIndex(userData.getCurrentListIndex());
		
		int rightIndex = 1;
		if(detail.getNumOfBoxes() == 3) {
			rightIndex = 2;
		}
		if(detail.getNumOfBoxes() == 2) {
			detail.setStimuliMiddleIncrement("X");
			detail.setStimuliMiddleInitial("X");
			detail.setStimuliMiddleValAfter("X");
			detail.setStimuliMiddleValBefore("X");
			detail.setUserMiddleVal("X");
			detail.setUserMiddleValBefore("X");
		}
		//stimuli initial value
		if(userData.getStimuliInitialVal() != null && userData.getStimuliInitialVal().size() > 0) {
			detail.setStimuliLeftInitial(userData.getStimuliInitialVal().get(leftIndex) + "");
			if(detail.getNumOfBoxes() == 3 && userData.getStimuliInitialVal().size() > middleIndex) {
				detail.setStimuliMiddleInitial(userData.getStimuliInitialVal().get(middleIndex) + "");
			}
			if(userData.getStimuliInitialVal().size() > rightIndex) {
				detail.setStimuliRightInitial(userData.getStimuliInitialVal().get(rightIndex) + "");
			}
		}
		//stimuli input before increment
		if(userData.getStimuliInputBefore() != null && userData.getStimuliInputBefore().size() > 0) {
			detail.setStimuliLeftValBefore(userData.getStimuliInputBefore().get(leftIndex) + "");
			if(detail.getNumOfBoxes() == 3 && userData.getStimuliInputBefore().size() > middleIndex) {
				detail.setStimuliMiddleValBefore(userData.getStimuliInputBefore().get(middleIndex) + "");
			}
			if(userData.getStimuliInputBefore().size() > rightIndex) {
				detail.setStimuliRightValBefore(userData.getStimuliInputBefore().get(rightIndex) + "");
			}
		}
		//stimuli increment
		if(userData.getStimuliIncrementInput() != null && userData.getStimuliIncrementInput().size() > 0) {
			detail.setStimuliLeftIncrement(userData.getStimuliIncrementInput().get(leftIndex) + "");
			if(detail.getNumOfBoxes() == 3 && userData.getStimuliIncrementInput().size() > middleIndex) {
				detail.setStimuliMiddleIncrement(userData.getStimuliIncrementInput().get(middleIndex) + "");
			}
			if(userData.getStimuliIncrementInput().size() > rightIndex) {
				detail.setStimuliRightIncrement(userData.getStimuliIncrementInput().get(rightIndex) + "");
			}
		}
		
		//stimuli input after increment
		if(userData.getStimuliIncrementInput().size() > 0 && userData.getStimuliInputBefore().size() > 0) {
			int left = userData.getStimuliInputBefore().get(0) + userData.getStimuliIncrementInput().get(0);
			detail.setStimuliLeftValAfter(left + "");
		}
		if(detail.getNumOfBoxes() == 3 && userData.getStimuliIncrementInput().size() > middleIndex && userData.getStimuliInputBefore().size() > middleIndex) {
			int middle = userData.getStimuliInputBefore().get(middleIndex) + userData.getStimuliIncrementInput().get(middleIndex);
			detail.setStimuliMiddleValAfter(middle + "");
		}
		if(userData.getStimuliIncrementInput().size() > rightIndex && userData.getStimuliInputBefore().size() > rightIndex) {
			int right = userData.getStimuliInputBefore().get(rightIndex) + userData.getStimuliIncrementInput().get(rightIndex);
			detail.setStimuliRightValAfter(right + "");
		}
		
		//user input
		if(userData.getUserInput() != null && userData.getUserInput().size() > 0) {
			detail.setUserLeftVal(userData.getUserInput().get(0) + "");
			if(detail.getNumOfBoxes() == 3 && userData.getUserInput().size() > middleIndex) {
				detail.setUserMiddleVal(userData.getUserInput().get(middleIndex) + "");
			}
			if(userData.getUserInput().size() > rightIndex) {
				detail.setUserRightVal(userData.getUserInput().get(rightIndex) + "");
			}
		}
		
		//user input before
		detail.setUserLeftValBefore("X");
		detail.setUserRightValBefore("X");
		if(userData.getLastUserInput() != null && userData.getLastUserInput().size() > 0) {
			detail.setUserLeftValBefore(userData.getLastUserInput().get(0) + "");
			if(detail.getNumOfBoxes() == 3 && userData.getLastUserInput().size() > middleIndex) {
				detail.setUserMiddleValBefore(userData.getLastUserInput().get(middleIndex) + "");
			}
			if(userData.getLastUserInput().size() > rightIndex) {
				detail.setUserRightValBefore(userData.getLastUserInput().get(rightIndex) + "");
			}
		}
		//fill * if data need to be check
		if(userData.getNeedCheck() != null && userData.getNeedCheck()) {
			detail.setComment("*");
		}
		detail.setScore(0);
		if(detail.getNumOfBoxes() < 3) {
			if(detail.getUserLeftVal() != null && detail.getUserRightVal() != null && detail.getStimuliLeftValAfter() != null && detail.getStimuliRightValAfter() != null) {
				detail.setScore(detail.getUserLeftVal().equals(detail.getStimuliLeftValAfter()) 
						&& detail.getUserRightVal().equals(detail.getStimuliRightValAfter()) 
						? 1: 0);
				int numOfBoxesCorrect = 0;
				if(detail.getUserLeftVal().equals(detail.getStimuliLeftValAfter())) {
					numOfBoxesCorrect++;
				}
				if(detail.getUserRightVal().equals(detail.getStimuliRightValAfter())) {
					numOfBoxesCorrect++;
				}
				detail.setNumOfBoxesAbsoluteCorrect(numOfBoxesCorrect);
			}
		}else {
			if(detail.getUserLeftVal() != null && detail.getUserRightVal() != null && detail.getStimuliLeftValAfter() != null && detail.getStimuliRightValAfter() != null
					&& detail.getUserMiddleVal() != null && detail.getStimuliMiddleValAfter() != null) {
				detail.setScore(detail.getUserLeftVal().equals(detail.getStimuliLeftValAfter()) 
						&& detail.getUserRightVal().equals(detail.getStimuliRightValAfter()) 
						&& detail.getUserMiddleVal().equals(detail.getStimuliMiddleValAfter())
						? 1: 0);
				int numOfBoxesCorrect = 0;
				if(detail.getUserLeftVal().equals(detail.getStimuliLeftValAfter())) {
					numOfBoxesCorrect++;
				}
				if(detail.getUserRightVal().equals(detail.getStimuliRightValAfter())) {
					numOfBoxesCorrect++;
				}
				if(detail.getUserMiddleVal().equals(detail.getStimuliMiddleValAfter())) {
					numOfBoxesCorrect++;
				}
				detail.setNumOfBoxesAbsoluteCorrect(numOfBoxesCorrect);
			}
		}
		//lenient / absolute correct
		detail.setAbsoluteCorrect(detail.getScore());
		if(detail.getUserLeftValBefore() != null && detail.getUserLeftValBefore().equals("X")) {
			detail.setLenientCorrect(detail.getAbsoluteCorrect());
			detail.setNumOfBoxesLenientCorrect(detail.getNumOfBoxesAbsoluteCorrect());
		}else {
			detail.setLenientCorrect(0);
			int left = userData.getLastUserInput().get(0) + userData.getStimuliIncrementInput().get(0);
			Integer mid = detail.getNumOfBoxes() == 3 && userData.getLastUserInput().size() > middleIndex 
					? userData.getLastUserInput().get(middleIndex) + userData.getStimuliIncrementInput().get(middleIndex)
					: null;
			Integer right = userData.getLastUserInput().size() > rightIndex 
					? userData.getLastUserInput().get(rightIndex) + userData.getStimuliIncrementInput().get(rightIndex)
					: null;	
			if(detail.getNumOfBoxes() == 3 && userData.getUserInput().size() > rightIndex && right != null && mid != null) {
				int numOfBoxesCorrect = 0;
				if(left == userData.getUserInput().get(0)) {
					numOfBoxesCorrect++;
				}
				if(mid == userData.getUserInput().get(middleIndex)) {
					numOfBoxesCorrect++;
				}
				if(right == userData.getUserInput().get(rightIndex)) {
					numOfBoxesCorrect++;
				}
				detail.setNumOfBoxesLenientCorrect(numOfBoxesCorrect);
				detail.setLenientCorrect(numOfBoxesCorrect == 3 ? 1 : 0);
			}else if(detail.getNumOfBoxes() == 2 && userData.getUserInput().size() > rightIndex && right != null) {
				int numOfBoxesCorrect = 0;
				if(left == userData.getUserInput().get(0)) {
					numOfBoxesCorrect++;
				}
				if(right == userData.getUserInput().get(rightIndex)) {
					numOfBoxesCorrect++;
				}
				detail.setNumOfBoxesLenientCorrect(numOfBoxesCorrect);
				detail.setLenientCorrect(numOfBoxesCorrect == 2 ? 1 : 0);
			}
//			if(detail.getNumOfBoxes() == 3 && userData.getUserInput().size() > rightIndex && right != null && mid != null && left == userData.getUserInput().get(0) 
//					&& mid == userData.getUserInput().get(middleIndex) && right == userData.getUserInput().get(rightIndex)) {
//				detail.setLenientCorrect(1);
//			}else if(detail.getNumOfBoxes() == 2 && userData.getUserInput().size() > rightIndex && right != null && left == userData.getUserInput().get(0) 
//					&& right == userData.getUserInput().get(rightIndex)) {
//				detail.setLenientCorrect(1);
//			}
		}
		detail.setTrialType(userData.getTrialType());
		if(userData.getEndTime() != null && userData.getStartTime() != null) {
			detail.setResponseTime(userData.getEndTime() - userData.getStartTime());
		}
		return repo.createDetailData(detail);
	}

	private GameProgressDataView updateCoinsAndRocksInGameProgress(NumberUpdateUserData userData, DetailNumberUpdateAuditory detailData) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(userData.getChildId(), userData.getGrade(), GameIdType.Number_Update_Auditory.toString());
		if(dbProgress != null) {
			//one trial lenient correct, coin +1, exclude practice trial
			if(userData.getTrialType() == null || !userData.getTrialType().equals("practice")) {
				dbProgress.setNextListIndex(userData.getCurrentListIndex() + 1);
				dbProgress.setCurrentGroupIndex(userData.getCurrentGroupIndex());
				
				if(dbProgress.getRocks() != null) {
					dbProgress.setRocks(detailData.getLenientCorrect() == 0 ? dbProgress.getRocks() + 1 : dbProgress.getRocks());
				}else {
					dbProgress.setRocks(detailData.getLenientCorrect() == 0 ? 1 : 0);
				}
				
				if(dbProgress.getCoins() != null) {
					dbProgress.setCoins(detailData.getLenientCorrect() > 0 ? dbProgress.getCoins() + detailData.getNumOfBoxes() : dbProgress.getCoins());
				}else {
					dbProgress.setCoins(detailData.getLenientCorrect() > 0 ? detailData.getNumOfBoxes() : 0);
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
