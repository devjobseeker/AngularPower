package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.GameUserData;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.data.DetailLocationSpanRunning;
import org.asu.chilll.power.entity.data.SummaryLocationSpanRunning;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.repository.GameRepository;
import org.asu.chilll.power.repository.LocationSpanRunningRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationSpanRunningService {
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private LocationSpanRunningRepository repo;
	
	public GameProgressDataView saveData(GameUserData userData) {
		DetailLocationSpanRunning detailData = createDetailData(userData);
		updateSummaryData(userData, detailData);
		return updateCoinsAndRocksInGameProgress(userData, detailData);
	}

	private DetailLocationSpanRunning createDetailData(GameUserData userData) {
		DetailLocationSpanRunning detail = new DetailLocationSpanRunning();
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
		int maxOrdinal = 8;
		for(int i = 0; i < maxOrdinal; i++) {
			String userInputDigit = "X";
			String stimuliInputDigit = "X";
			
			if(i < userInputSize && i < stimuliInputSize) {
				userInputDigit = userData.getUserInput().get(userInputSize - 1 - i) + "";
				userInput = userInputDigit + userInput;	//append new digit before userInput
			}else if(i < stimuliInputSize) {
				userInputDigit = "NR";
			}
			
			if(i < stimuliInputSize) {
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
			}else if(i == 6) {
				detail.setSi7(stimuliInputDigit);
				detail.setUi7(userInputDigit);
			}else if( i == 7) {
				detail.setSi8(stimuliInputDigit);
				detail.setUi8(userInputDigit);
			}
		}
		
		detail.setStimuliInput(stimuliInput);
		detail.setUserInput(userInput);
		//fill extra user input
		if(userData.getUserInput().size() > userData.getStimuliInput().size()) {
			String extraInput = "";
			for(int i = 0; i < userInputSize - stimuliInputSize; i++) {
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

	private void updateSummaryData(GameUserData userData, DetailLocationSpanRunning detailData) {
		String childId_grade = userData.getChildId() + "_" + userData.getGrade();
		SummaryLocationSpanRunning summary = repo.fecthSummaryData(childId_grade);
		int listLength = userData.getStimuliInput().size();
		int indexOfTrial = userData.getIndexOfTrial();
		if(summary == null) {
			//create
			summary = new SummaryLocationSpanRunning();
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
	
	private SummaryLocationSpanRunning getSummaryData(int listLength, int indexOfTrial, int score, int numOfDigitsCorrect, String trialType, SummaryLocationSpanRunning summary) {
		if(trialType != null && trialType.equals("practice")) {
			if(indexOfTrial == 0) {
				summary.setLsrp1(numOfDigitsCorrect + "");
			}else if(indexOfTrial == 1) {
				summary.setLsrp2(numOfDigitsCorrect + "");
			}else if(indexOfTrial == 2) {
				summary.setLsrp3(numOfDigitsCorrect + "");
			}
		}else if(listLength == 5) {
			switch(indexOfTrial) {
			case 1:
				summary.setLsr51(numOfDigitsCorrect + "");
				break;
			case 2: 
				summary.setLsr52(numOfDigitsCorrect + "");
				break;
			case 3:
				summary.setLsr53(numOfDigitsCorrect + "");
				break;
			case 4:
				summary.setLsr54(numOfDigitsCorrect + "");
				break;
			}
		}else if(listLength == 6) {
			switch(indexOfTrial) {
			case 1:
				summary.setLsr61(numOfDigitsCorrect + "");
				break;
			case 2: 
				summary.setLsr62(numOfDigitsCorrect + "");
				break;
			case 3:
				summary.setLsr63(numOfDigitsCorrect + "");
				break;
			case 4:
				summary.setLsr64(numOfDigitsCorrect + "");
				break;
			}
		}else if(listLength == 7) {
			switch(indexOfTrial) {
			case 1:
				summary.setLsr71(numOfDigitsCorrect + "");
				break;
			case 2: 
				summary.setLsr72(numOfDigitsCorrect + "");
				break;
			case 3:
				summary.setLsr73(numOfDigitsCorrect + "");
				break;
			case 4:
				summary.setLsr74(numOfDigitsCorrect + "");
				break;
			}
		}else if(listLength == 8) {
			switch(indexOfTrial) {
			case 1:
				summary.setLsr81(numOfDigitsCorrect + "");
				break;
			case 2: 
				summary.setLsr82(numOfDigitsCorrect + "");
				break;
			case 3:
				summary.setLsr83(numOfDigitsCorrect + "");
				break;
			case 4:
				summary.setLsr84(numOfDigitsCorrect + "");
				break;
			}
		}
		
		return summary;
	}

	private GameProgressDataView updateCoinsAndRocksInGameProgress(GameUserData userData, DetailLocationSpanRunning detailData) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(userData.getChildId(), userData.getGrade(), GameIdType.Location_Span_Running.toString());
		if(dbProgress != null) {
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
