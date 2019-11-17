package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.GameUserData;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.data.DetailDigitSpan;
import org.asu.chilll.power.entity.data.SummaryDigitSpan;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.repository.DigitSpanRepository;
import org.asu.chilll.power.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DigitSpanService{
	
	@Autowired
	private DigitSpanRepository repo;
	
	@Autowired
	private GameRepository gameRepo;
	
	public GameProgressDataView saveData(GameUserData userData) {
		DetailDigitSpan detailData = createDetailData(userData);
		updateSummaryData(userData, detailData);
		return updateCoinsAndRocksInGameProgress(userData, detailData);
	}
	
	private DetailDigitSpan createDetailData(GameUserData userData) {
		int maxDigitOrdinal = 8;
		DetailDigitSpan detail = new DetailDigitSpan();
		detail.setChildId(userData.getChildId());
		detail.setGrade(userData.getGrade());
		detail.setExperimenter(userData.getExperimenter());
		detail.setTrialNo(userData.getCurrentListIndex() + 1);
		//fill stimuliInput digit, userInput digit
		String stimuliInput = "";
		String userInput = "";
		Integer score = null;
		int numOfDigitsCorrect = 0;
		for(int i = 0; i < maxDigitOrdinal; i++) {
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
			}else if(i == 6) {
				detail.setSi7(stimuliInputDigit);
				detail.setUi7(userInputDigit);
			}else if(i == 7) {
				detail.setSi8(stimuliInputDigit);
				detail.setUi8(userInputDigit);
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
		return repo.createDigitSpanDetailData(detail);
	}
	
	private void updateSummaryData(GameUserData userData, DetailDigitSpan detailData) {
		String childId_grade = userData.getChildId() + "_" + userData.getGrade();
		SummaryDigitSpan summary = repo.fecthDigitSpanSummaryData(childId_grade);
		int listLength = userData.getStimuliInput().size();
		int indexOfTrial = userData.getIndexOfTrial();
		if(summary == null) {
			//create
			summary = new SummaryDigitSpan();
			summary.setChildId_grade(childId_grade);
			summary.setExperimenter(userData.getExperimenter());
			summary = getSummaryData(listLength, indexOfTrial, detailData.getScore(), detailData.getNumOfDigitsCorrect(), userData.getTrialType(), summary);
			repo.createDigitSpanSummaryData(summary);
		}else {
			//update
			summary = getSummaryData(listLength, indexOfTrial, detailData.getScore(), detailData.getNumOfDigitsCorrect(), userData.getTrialType(), summary);
			repo.updateDigitSpanSummaryData(summary);
		}
	}
	
	private SummaryDigitSpan getSummaryData(int listLength, int indexOfTrial, int score, int numOfDigitsCorrect, String trialType, SummaryDigitSpan summary) {
		if(trialType != null && trialType.equals("practice")) {
			if(indexOfTrial == 0) {
				summary.setDspi1(numOfDigitsCorrect + "");
				summary.setDspt1(score + "");
			}else if(indexOfTrial == 1) {
				summary.setDspi2(numOfDigitsCorrect + "");
				summary.setDspt2(score + "");
			}else if(indexOfTrial == 2) {
				summary.setDspi3(numOfDigitsCorrect + "");
				summary.setDspt3(score + "");
			}
		}else if(listLength == 2) {
			switch (indexOfTrial){
				case 1: {
					summary.setDsi21(numOfDigitsCorrect + "");
					summary.setDst21(score + "");
					break;
				}
				case 2: {
					summary.setDsi22(numOfDigitsCorrect + "");
					summary.setDst22(score + "");
					break;
				}
				case 3: {
					summary.setDsi23(numOfDigitsCorrect + "");
					summary.setDst23(score + "");
					break;
				}
				case 4: {
					summary.setDsi24(numOfDigitsCorrect + "");
					summary.setDst24(score + "");
					break;
				}
			}
		}else if(listLength == 3) {
			switch (indexOfTrial){
				case 1: {
					summary.setDsi31(numOfDigitsCorrect + "");
					summary.setDst31(score + "");
					break;
				}
				case 2: {
					summary.setDsi32(numOfDigitsCorrect + "");
					summary.setDst32(score + "");
					break;
				}
				case 3: {
					summary.setDsi33(numOfDigitsCorrect + "");
					summary.setDst33(score + "");
					break;
				}
				case 4: {
					summary.setDsi34(numOfDigitsCorrect + "");
					summary.setDst34(score + "");
					break;
				}
			}
		}else if(listLength == 4) {
			switch (indexOfTrial){
				case 1: {
					summary.setDsi41(numOfDigitsCorrect + "");
					summary.setDst41(score + "");
					break;
				}
				case 2: {
					summary.setDsi42(numOfDigitsCorrect + "");
					summary.setDst42(score + "");
					break;
				}
				case 3: {
					summary.setDsi43(numOfDigitsCorrect + "");
					summary.setDst43(score + "");
					break;
				}
				case 4: {
					summary.setDsi44(numOfDigitsCorrect + "");
					summary.setDst44(score + "");
					break;
				}
			}
		}else if(listLength == 5) {
			switch (indexOfTrial){
				case 1: {
					summary.setDsi51(numOfDigitsCorrect + "");
					summary.setDst51(score + "");
					break;
				}
				case 2: {
					summary.setDsi52(numOfDigitsCorrect + "");
					summary.setDst52(score + "");
					break;
				}
				case 3: {
					summary.setDsi53(numOfDigitsCorrect + "");
					summary.setDst53(score + "");
					break;
				}
				case 4: {
					summary.setDsi54(numOfDigitsCorrect + "");
					summary.setDst54(score + "");
					break;
				}
			}
		}else if(listLength == 6) {
				switch (indexOfTrial){
					case 1: {
						summary.setDsi61(numOfDigitsCorrect + "");
						summary.setDst61(score + "");
						break;
					}
					case 2: {
						summary.setDsi62(numOfDigitsCorrect + "");
						summary.setDst62(score + "");
						break;
					}
					case 3: {
						summary.setDsi63(numOfDigitsCorrect + "");
						summary.setDst63(score + "");
						break;
					}
					case 4: {
						summary.setDsi64(numOfDigitsCorrect + "");
						summary.setDst64(score + "");
						break;
					}
				}
		}else if(listLength == 7) {
			switch (indexOfTrial){
				case 1: {
					summary.setDsi71(numOfDigitsCorrect + "");
					summary.setDst71(score + "");
					break;
				}
				case 2: {
					summary.setDsi72(numOfDigitsCorrect + "");
					summary.setDst72(score + "");
					break;
				}
				case 3: {
					summary.setDsi73(numOfDigitsCorrect + "");
					summary.setDst73(score + "");
					break;
				}
				case 4: {
					summary.setDsi74(numOfDigitsCorrect + "");
					summary.setDst74(score + "");
					break;
				}
			}
		}else if(listLength == 8) {
			switch (indexOfTrial){
				case 1: {
					summary.setDsi81(numOfDigitsCorrect + "");
					summary.setDst81(score + "");
					break;
				}
				case 2: {
					summary.setDsi82(numOfDigitsCorrect + "");
					summary.setDst82(score + "");
					break;
				}
				case 3: {
					summary.setDsi83(numOfDigitsCorrect + "");
					summary.setDst83(score + "");
					break;
				}
				case 4: {
					summary.setDsi84(numOfDigitsCorrect + "");
					summary.setDst84(score + "");
					break;
				}
			}
		}
		return summary;
	}	
	
	private GameProgressDataView updateCoinsAndRocksInGameProgress(GameUserData userData, DetailDigitSpan detailData) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(userData.getChildId(), userData.getGrade(), GameIdType.Digit_Span.toString());
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
