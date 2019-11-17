package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.GameUserData;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.data.DetailVisualSpanRunning;
import org.asu.chilll.power.entity.data.SummaryVisualSpanRunning;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.repository.GameRepository;
import org.asu.chilll.power.repository.VisualSpanRunningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisualSpanRunningService {
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private VisualSpanRunningRepository repo;
	
	public GameProgressDataView saveData(GameUserData userData) {
		DetailVisualSpanRunning detailData = createDetailData(userData);
		updateSummaryData(userData, detailData);
		return updateCoinsAndRocksInGameProgress(userData, detailData);
	}

	private DetailVisualSpanRunning createDetailData(GameUserData userData) {
		DetailVisualSpanRunning detail = new DetailVisualSpanRunning();
		detail.setChildId(userData.getChildId());
		detail.setGrade(userData.getGrade());
		detail.setExperimenter(userData.getExperimenter());
		detail.setTrialNo(userData.getCurrentListIndex() + 1);
		//fill stimuliInput digit, userInput digit
		String stimuliInput = "";
		String userInput = "";
		Integer score = null;
		int numOfDigitsCorrect = 0;
		int numOfDigitsCorrectFromEnd = 0;
		boolean countCorrectFlag = true;
		int userInputSize = userData.getUserInput().size();
		int stimuliInputSize = userData.getStimuliInput().size();
		int maxOrdinal = 6;
		for(int i = 0; i < maxOrdinal; i++) {
			String userInputDigit = "X";
			String stimuliInputDigit = "X";
			
			if(i < userData.getUserInput().size() && i < userData.getStimuliInput().size()) {
				userInputDigit = userData.getUserInput().get(userInputSize - 1 - i) + "";
				userInput = userInputDigit + userInput;	//append new digit before userInput
			}else if(i < userData.getStimuliInput().size()) {
				userInputDigit = "NR";
			}
			
			if(i < userData.getStimuliInput().size()) {
				stimuliInputDigit = userData.getStimuliInput().get(stimuliInputSize - 1 - i) + "";
				stimuliInput = stimuliInputDigit + stimuliInput;	//append new digit before stimuliInput
				if(score == null && !stimuliInputDigit.equals(userInputDigit)) {
					score = 0;
				}else if(stimuliInputDigit.equals(userInputDigit)) {
					numOfDigitsCorrect++;
					if(countCorrectFlag) {
						numOfDigitsCorrectFromEnd++;
					}
				}else if(countCorrectFlag) {
					countCorrectFlag = false;
				}
			}
			
			if(i == 0) {
				detail.setSi1(stimuliInputDigit);
				detail.setUi1(userInputDigit);
			}else if(i == 1) {
				detail.setSi2(stimuliInputDigit);
				detail.setUi2(userInputDigit);
			}else if(i == 2) {
				detail.setSi3(stimuliInputDigit);
				detail.setUi3(userInputDigit);
			}else if(i == 3) {
				detail.setSi4(stimuliInputDigit);
				detail.setUi4(userInputDigit);
			}else if(i == 4) {
				detail.setSi5(stimuliInputDigit);
				detail.setUi5(userInputDigit);
			}else if(i == 5) {
				detail.setSi6(stimuliInputDigit);
				detail.setUi6(userInputDigit);
			}
		}
		
		detail.setStimuliInput(stimuliInput);
		detail.setUserInput(userInput);
		//fill extra user input
		if(userData.getUserInput().size() > userData.getStimuliInput().size()) {
			String extraInput = "";
			for(int i = userData.getStimuliInput().size(); i < userData.getUserInput().size(); i++) {
				extraInput += userData.getUserInput().get(i);
			}
			detail.setExtraUserInput(extraInput);
			detail.setUserInput(detail.getExtraUserInput() + userInput);
		}
		//fill * if data need to be check
		if(userData.getNeedCheck()) {
			detail.setComment("*");
		}
		//fill score and numOfDigitsCorrect
		detail.setScore(score == null && detail.getExtraUserInput() == null ? 1 : 0);
		detail.setNumOfDigitsCorrect(numOfDigitsCorrect);
		detail.setNumOfDigitsCorrectFromEnd(numOfDigitsCorrectFromEnd);
		detail.setTrialType(userData.getTrialType());
		if(userData.getEndTime() != null && userData.getStartTime() != null) {
			detail.setResponseTime(userData.getEndTime() - userData.getStartTime());
		}
		return repo.createDetailData(detail);
	}

	private void updateSummaryData(GameUserData userData, DetailVisualSpanRunning detailData) {
		String childId_grade = userData.getChildId() + "_" + userData.getGrade();
		SummaryVisualSpanRunning summary = repo.fecthSummaryData(childId_grade);
		int listLength = userData.getStimuliInput().size();
		int indexOfTrial = userData.getIndexOfTrial();
		if(summary == null) {
			//create
			summary = new SummaryVisualSpanRunning();
			summary.setChildId_grade(childId_grade);
			summary.setExperimenter(userData.getExperimenter());
			summary = getSummaryData(listLength, indexOfTrial, detailData.getScore(), detailData.getNumOfDigitsCorrect(), userData.getTrialType(), summary);
			repo.createSummaryData(summary);
		}else {
			//update
			summary = getSummaryData(listLength, indexOfTrial, detailData.getScore(), detailData.getNumOfDigitsCorrect(), userData.getTrialType(), summary);
			repo.updateSummaryData(summary);
		}
	}
	
	private SummaryVisualSpanRunning getSummaryData(int listLength, int indexOfTrial, int score, int numOfDigitsCorrect, String trialType, SummaryVisualSpanRunning summary) {
		if(trialType != null && trialType.equals("practice")) {
			if(indexOfTrial == 0) {
				summary.setVsrp1(numOfDigitsCorrect + "");
			}else if(indexOfTrial == 1) {
				summary.setVsrp2(numOfDigitsCorrect + "");
			}else if(indexOfTrial == 2) {
				summary.setVsrp3(numOfDigitsCorrect + "");
			}
		}else if(listLength == 3) {
			switch (indexOfTrial){
				case 1: {
					summary.setVsr31(numOfDigitsCorrect + "");
					break;
				}
				case 2: {
					summary.setVsr32(numOfDigitsCorrect + "");
					break;
				}
				case 3: {
					summary.setVsr33(numOfDigitsCorrect + "");
					break;
				}
				case 4: {
					summary.setVsr34(numOfDigitsCorrect + "");
					break;
				}
			}
		}else if(listLength == 4) {
			switch (indexOfTrial){
				case 1: {
					summary.setVsr41(numOfDigitsCorrect + "");
					break;
				}
				case 2: {
					summary.setVsr42(numOfDigitsCorrect + "");
					break;
				}
				case 3: {
					summary.setVsr43(numOfDigitsCorrect + "");
					break;
				}
				case 4: {
					summary.setVsr44(numOfDigitsCorrect + "");
					break;
				}
			}
		}else if(listLength == 5) {
			switch (indexOfTrial){
				case 1: {
					summary.setVsr51(numOfDigitsCorrect + "");
					break;
				}
				case 2: {
					summary.setVsr52(numOfDigitsCorrect + "");
					break;
				}
				case 3: {
					summary.setVsr53(numOfDigitsCorrect + "");
					break;
				}
				case 4: {
					summary.setVsr54(numOfDigitsCorrect + "");
					break;
				}
			}
		}else if(listLength == 6) {
			switch (indexOfTrial){
				case 1: {
					summary.setVsr61(numOfDigitsCorrect + "");
					break;
				}
				case 2: {
					summary.setVsr62(numOfDigitsCorrect + "");
					break;
				}
				case 3: {
					summary.setVsr63(numOfDigitsCorrect + "");
					break;
				}
				case 4: {
					summary.setVsr64(numOfDigitsCorrect + "");
					break;
				}
			}
		}
		return summary;
	}

	private GameProgressDataView updateCoinsAndRocksInGameProgress(GameUserData userData, DetailVisualSpanRunning detailData) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(userData.getChildId(), userData.getGrade(), GameIdType.Visual_Span_Running.toString());
		if(dbProgress != null) {
			//rock will not increase in practice trial. if user answer all correct in one list length, coin + 1.
			if(userData.getTrialType() == null || !userData.getTrialType().equals("practice")) {
				dbProgress.setNextListIndex(userData.getCurrentListIndex() + 1);
				
				if(dbProgress.getRocks() != null) {
					dbProgress.setRocks(detailData.getNumOfDigitsCorrect() == 0 ? dbProgress.getRocks() + 1 : dbProgress.getRocks());
				}else {
					dbProgress.setRocks(detailData.getNumOfDigitsCorrect() == 0 ? 1 : 0);
				}
				
				if(dbProgress.getCoins() != null) {
					dbProgress.setCoins(detailData.getNumOfDigitsCorrect() > 0 ? dbProgress.getCoins() + detailData.getNumOfDigitsCorrect() : dbProgress.getCoins());
				}else {
					dbProgress.setCoins(detailData.getNumOfDigitsCorrect() > 0 ? detailData.getNumOfDigitsCorrect() : 0);
				}
			}
			
			if(userData.getConsecutiveTrialResult() != null && userData.getConsecutiveTrialResult().size() > 0) {
				StringBuilder sb = new StringBuilder();
				for(Integer i: userData.getConsecutiveTrialResult()) {
					sb.append(i + "");
				}
				dbProgress.setConsecutiveTrialResult(sb.toString());
			}
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
