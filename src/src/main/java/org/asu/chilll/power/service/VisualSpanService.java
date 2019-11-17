package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.GameUserData;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.data.DetailVisualSpan;
import org.asu.chilll.power.entity.data.SummaryVisualSpan;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.repository.GameRepository;
import org.asu.chilll.power.repository.VisualSpanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisualSpanService {
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private VisualSpanRepository repo;
	
	public GameProgressDataView saveData(GameUserData userData) {
		DetailVisualSpan detailData = createDetailData(userData);
		updateSummaryData(userData, detailData);
		return updateCoinsAndRocksInGameProgress(userData, detailData);
	}

	private DetailVisualSpan createDetailData(GameUserData userData) {
		DetailVisualSpan detail = new DetailVisualSpan();
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

	private void updateSummaryData(GameUserData userData, DetailVisualSpan detailData) {
		String childId_grade = userData.getChildId() + "_" + userData.getGrade();
		SummaryVisualSpan summary = repo.fecthSummaryData(childId_grade);
		int listLength = userData.getStimuliInput().size();
		int indexOfTrial = userData.getIndexOfTrial();
		if(summary == null) {
			//create
			summary = new SummaryVisualSpan();
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
	
	private SummaryVisualSpan getSummaryData(int listLength, int indexOfTrial, int score, int numOfDigitsCorrect, String trialType, SummaryVisualSpan summary) {
		if(trialType != null && trialType.equals("practice")) {
			if(indexOfTrial == 0) {
				summary.setVspi1(numOfDigitsCorrect + "");
				summary.setVspt1(score + "");
			}else if(indexOfTrial == 1) {
				summary.setVspi2(numOfDigitsCorrect + "");
				summary.setVspt2(score + "");
			}else if(indexOfTrial == 2) {
				summary.setVspi3(numOfDigitsCorrect + "");
				summary.setVspt3(score + "");
			}
		}else if(listLength == 2) {
			switch (indexOfTrial){
				case 1: {
					summary.setVsi21(numOfDigitsCorrect + "");
					summary.setVst21(score + "");
					break;
				}
				case 2: {
					summary.setVsi22(numOfDigitsCorrect + "");
					summary.setVst22(score + "");
					break;
				}
				case 3: {
					summary.setVsi23(numOfDigitsCorrect + "");
					summary.setVst23(score + "");
					break;
				}
				case 4: {
					summary.setVsi24(numOfDigitsCorrect + "");
					summary.setVst24(score + "");
					break;
				}
			}
		}else if(listLength == 3) {
			switch (indexOfTrial){
				case 1: {
					summary.setVsi31(numOfDigitsCorrect + "");
					summary.setVst31(score + "");
					break;
				}
				case 2: {
					summary.setVsi32(numOfDigitsCorrect + "");
					summary.setVst32(score + "");
					break;
				}
				case 3: {
					summary.setVsi33(numOfDigitsCorrect + "");
					summary.setVst33(score + "");
					break;
				}
				case 4: {
					summary.setVsi34(numOfDigitsCorrect + "");
					summary.setVst34(score + "");
					break;
				}
			}
		}else if(listLength == 4) {
			switch (indexOfTrial){
				case 1: {
					summary.setVsi41(numOfDigitsCorrect + "");
					summary.setVst41(score + "");
					break;
				}
				case 2: {
					summary.setVsi42(numOfDigitsCorrect + "");
					summary.setVst42(score + "");
					break;
				}
				case 3: {
					summary.setVsi43(numOfDigitsCorrect + "");
					summary.setVst43(score + "");
					break;
				}
				case 4: {
					summary.setVsi44(numOfDigitsCorrect + "");
					summary.setVst44(score + "");
					break;
				}
			}
		}else if(listLength == 5) {
			switch (indexOfTrial){
				case 1: {
					summary.setVsi51(numOfDigitsCorrect + "");
					summary.setVst51(score + "");
					break;
				}
				case 2: {
					summary.setVsi52(numOfDigitsCorrect + "");
					summary.setVst52(score + "");
					break;
				}
				case 3: {
					summary.setVsi53(numOfDigitsCorrect + "");
					summary.setVst53(score + "");
					break;
				}
				case 4: {
					summary.setVsi54(numOfDigitsCorrect + "");
					summary.setVst54(score + "");
					break;
				}
			}
		}else if(listLength == 6) {
				switch (indexOfTrial){
					case 1: {
						summary.setVsi61(numOfDigitsCorrect + "");
						summary.setVst61(score + "");
						break;
					}
					case 2: {
						summary.setVsi62(numOfDigitsCorrect + "");
						summary.setVst62(score + "");
						break;
					}
					case 3: {
						summary.setVsi63(numOfDigitsCorrect + "");
						summary.setVst63(score + "");
						break;
					}
					case 4: {
						summary.setVsi64(numOfDigitsCorrect + "");
						summary.setVst64(score + "");
						break;
					}
				}
		}
		return summary;
	}

	private GameProgressDataView updateCoinsAndRocksInGameProgress(GameUserData userData, DetailVisualSpan detailData) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(userData.getChildId(), userData.getGrade(), GameIdType.Visual_Span.toString());
		if(dbProgress != null) {
			if(userData.getTrialType() == null || !userData.getTrialType().equals("practice")) {
				dbProgress.setNextListIndex(userData.getCurrentListIndex() + 1);
				
				if(dbProgress.getRocks() != null) {
					dbProgress.setRocks(detailData.getScore() == 0 ? dbProgress.getRocks() + 1 : dbProgress.getRocks());
				}else {
					dbProgress.setRocks(detailData.getScore() == 0 ? 1 : 0);
				}
				
				int listLength = userData.getStimuliInput().size();
				if(dbProgress.getCoins() != null) {
					dbProgress.setCoins(detailData.getScore() > 0 ? dbProgress.getCoins() + listLength : dbProgress.getCoins());
				}else {
					dbProgress.setCoins(detailData.getScore() > 0 ? listLength : 0);
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
