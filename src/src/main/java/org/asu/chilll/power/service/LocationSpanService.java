package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.GameUserData;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.data.DetailLocationSpan;
import org.asu.chilll.power.entity.data.SummaryLocationSpan;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.repository.GameRepository;
import org.asu.chilll.power.repository.LocationSpanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationSpanService {
	
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private LocationSpanRepository repo;
	
	public GameProgressDataView saveData(GameUserData userData) {
		DetailLocationSpan detailData = createDetailData(userData);
		updateSummaryData(userData, detailData);
		return updateCoinsAndRocksInGameProgress(userData, detailData);
		
	}

	private DetailLocationSpan createDetailData(GameUserData userData) {
		DetailLocationSpan detail = new DetailLocationSpan();
		detail.setChildId(userData.getChildId());
		detail.setGrade(userData.getGrade());
		detail.setExperimenter(userData.getExperimenter());
		detail.setTrialNo(userData.getCurrentListIndex() + 1);
		//fill stimuliInput digit, userInput digit
		String stimuliInput = "";
		String userInput = "";
		Integer score = null;
		int numOfDigitsCorrect = 0;
		int maxOrdinal = 6;
		for(int i = 0; i < maxOrdinal; i++) {
			String userInputDigit = "X";
			String stimuliInputDigit = "X";
			
			if(i < userData.getUserInput().size() && i < userData.getStimuliInput().size()) {
				userInputDigit = userData.getUserInput().get(i) + "";
				userInput += userInputDigit;
			}else if(i < userData.getStimuliInput().size()) {
				userInputDigit = "NR";
			}
			
			if(i < userData.getStimuliInput().size()) {
				stimuliInputDigit = userData.getStimuliInput().get(i) + "";
				stimuliInput += stimuliInputDigit;	
				if(score == null && !stimuliInputDigit.equals(userInputDigit)) {
					score = 0;
				}else if(stimuliInputDigit.equals(userInputDigit)) {
					numOfDigitsCorrect++;
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
			detail.setUserInput(userInput + detail.getExtraUserInput());
		}
		//fill * if data need to be check
		if(userData.getNeedCheck()) {
			detail.setComment("*");
		}
		//fill score and numOfDigitsCorrect
		detail.setScore(score == null && detail.getExtraUserInput() == null ? 1 : 0);
		detail.setNumOfDigitsCorrect(numOfDigitsCorrect);
		detail.setTrialType(userData.getTrialType());
		if(userData.getEndTime() != null && userData.getStartTime() != null) {
			detail.setResponseTime(userData.getEndTime() - userData.getStartTime());
		}
		return repo.createDetailData(detail);
	}

	private void updateSummaryData(GameUserData userData, DetailLocationSpan detailData) {
		String childId_grade = userData.getChildId() + "_" + userData.getGrade();
		SummaryLocationSpan summary = repo.fecthSummaryData(childId_grade);

		int listLength = userData.getStimuliInput().size();
		int indexOfTrial = userData.getIndexOfTrial();
		if(summary == null) {
			//create
			summary = new SummaryLocationSpan();
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
	
	private SummaryLocationSpan getSummaryData(int listLength, int indexOfTrial, int score, int numOfDigitsCorrect, String trialType, SummaryLocationSpan summary) {
		if(trialType != null && trialType.equals("practice")) {
			if(indexOfTrial == 0) {
				summary.setLspi1(numOfDigitsCorrect + "");
				summary.setLspt1(score + "");
			}else if(indexOfTrial == 1) {
				summary.setLspi2(numOfDigitsCorrect + "");
				summary.setLspt2(score + "");
			}else if(indexOfTrial == 2) {
				summary.setLspi3(numOfDigitsCorrect + "");
				summary.setLspt3(score + "");
			}
		}else if(listLength == 2) {
			switch (indexOfTrial){
				case 1: {
					summary.setLsi21(numOfDigitsCorrect + "");
					summary.setLst21(score + "");
					break;
				}
				case 2: {
					summary.setLsi22(numOfDigitsCorrect + "");
					summary.setLst22(score + "");
					break;
				}
				case 3: {
					summary.setLsi23(numOfDigitsCorrect + "");
					summary.setLst23(score + "");
					break;
				}
				case 4: {
					summary.setLsi24(numOfDigitsCorrect + "");
					summary.setLst24(score + "");
					break;
				}
			}
		}else if(listLength == 3) {
			switch (indexOfTrial){
				case 1: {
					summary.setLsi31(numOfDigitsCorrect + "");
					summary.setLst31(score + "");
					break;
				}
				case 2: {
					summary.setLsi32(numOfDigitsCorrect + "");
					summary.setLst32(score + "");
					break;
				}
				case 3: {
					summary.setLsi33(numOfDigitsCorrect + "");
					summary.setLst33(score + "");
					break;
				}
				case 4: {
					summary.setLsi34(numOfDigitsCorrect + "");
					summary.setLst34(score + "");
					break;
				}
			}
		}else if(listLength == 4) {
			switch (indexOfTrial){
				case 1: {
					summary.setLsi41(numOfDigitsCorrect + "");
					summary.setLst41(score + "");
					break;
				}
				case 2: {
					summary.setLsi42(numOfDigitsCorrect + "");
					summary.setLst42(score + "");
					break;
				}
				case 3: {
					summary.setLsi43(numOfDigitsCorrect + "");
					summary.setLst43(score + "");
					break;
				}
				case 4: {
					summary.setLsi44(numOfDigitsCorrect + "");
					summary.setLst44(score + "");
					break;
				}
			}
		}else if(listLength == 5) {
			switch (indexOfTrial){
				case 1: {
					summary.setLsi51(numOfDigitsCorrect + "");
					summary.setLst51(score + "");
					break;
				}
				case 2: {
					summary.setLsi52(numOfDigitsCorrect + "");
					summary.setLst52(score + "");
					break;
				}
				case 3: {
					summary.setLsi53(numOfDigitsCorrect + "");
					summary.setLst53(score + "");
					break;
				}
				case 4: {
					summary.setLsi54(numOfDigitsCorrect + "");
					summary.setLst54(score + "");
					break;
				}
			}
		}else if(listLength == 6) {
				switch (indexOfTrial){
					case 1: {
						summary.setLsi61(numOfDigitsCorrect + "");
						summary.setLst61(score + "");
						break;
					}
					case 2: {
						summary.setLsi62(numOfDigitsCorrect + "");
						summary.setLst62(score + "");
						break;
					}
					case 3: {
						summary.setLsi63(numOfDigitsCorrect + "");
						summary.setLst63(score + "");
						break;
					}
					case 4: {
						summary.setLsi64(numOfDigitsCorrect + "");
						summary.setLst64(score + "");
						break;
					}
				}
		}
		return summary;
	}

	private GameProgressDataView updateCoinsAndRocksInGameProgress(GameUserData userData, DetailLocationSpan detailData) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(userData.getChildId(), userData.getGrade(), GameIdType.Location_Span.toString());
		if(dbProgress != null) {
			//rock will not increase in practice trial. if user answer all digit correct in one list length, coin + 1.
			if(userData.getTrialType() == null || !userData.getTrialType().equals("practice")) {
				dbProgress.setNextListIndex(userData.getCurrentListIndex() + 1);
				
				if(dbProgress.getRocks() != null) {
					dbProgress.setRocks(detailData.getScore() == 0 ? dbProgress.getRocks() + 1 : dbProgress.getRocks());
				}else {
					dbProgress.setRocks(detailData.getScore() == 0 ? 1 : 0);
				}
				
				int listLength = userData.getStimuliInput().size();
				if(dbProgress.getCoins() != null) {
					dbProgress.setCoins(detailData.getScore() == 1 ? dbProgress.getCoins() + listLength : dbProgress.getCoins());
				}else {
					dbProgress.setCoins(detailData.getScore() == 1 ? listLength : 0);
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
