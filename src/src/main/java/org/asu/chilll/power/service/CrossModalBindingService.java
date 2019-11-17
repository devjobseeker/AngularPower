package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.CrossModalBindingUserData;
import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.data.DetailCrossModalBinding;
import org.asu.chilll.power.entity.data.SummaryCrossModalBinding;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.repository.CrossModalBindingRepository;
import org.asu.chilll.power.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CrossModalBindingService {
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private CrossModalBindingRepository repo;
	
	private final int totalCount = 36;
	
	public GameProgressDataView saveData(CrossModalBindingUserData userData) {
		DetailCrossModalBinding detailData = createDetailData(userData);
		updateSummaryData(userData, detailData);
		return updateCoinsAndRocksInGameProgress(userData, detailData);
	}

	private DetailCrossModalBinding createDetailData(CrossModalBindingUserData userData) {
		DetailCrossModalBinding detail = new DetailCrossModalBinding();
		detail.setChildId(userData.getChildId());
		detail.setGrade(userData.getGrade());
		detail.setExperimenter(userData.getExperimenter());
		detail.setTrialNo(userData.getCurrentListIndex() + 1);
		//fill stimuliInput digit, userInput digit
		String stimuliPolygonInput = "";
		String stimuliWordInput = "";
		String userPolygonInput = "";
		String userWordInput = "";
		//Integer score = null;
		int numOfDigitsCorrect = 0;
		int maxOrdinal = 4;
		for(int i = 0; i < maxOrdinal; i++) {
			String userPolygonInputDigit = "X";
			String userWordInputDigit = "X";
			String stimuliPolygonInputDigit = "X";
			String stimuliWordInputDigit = "X";
			
			if(i < userData.getUserPolygonInput().size() && i < userData.getStimuliPolygonInput().size()) {
				userPolygonInputDigit = userData.getUserPolygonInput().get(i) + "";
				userPolygonInput += userPolygonInputDigit + ",";
				userWordInputDigit = userData.getResponseWordInput().get(i) + "";
				userWordInput += userWordInputDigit + ",";
			}else if(i < userData.getStimuliPolygonInput().size()) {
				userWordInputDigit = "NR";
				userPolygonInputDigit = "NR";
			}
			
			if(i < userData.getStimuliPolygonInput().size()) {
				stimuliPolygonInputDigit = userData.getStimuliPolygonInput().get(i) + "";
				stimuliPolygonInput += stimuliPolygonInputDigit + ",";
				stimuliWordInputDigit = userData.getStimuliWordInput().get(i) + "";
				stimuliWordInput += stimuliWordInputDigit + ",";
			}
			
			if(i == 0) {
				detail.setSpi1(stimuliPolygonInputDigit);
				detail.setUpi1(userPolygonInputDigit);
				detail.setSwi1(stimuliWordInputDigit);
				detail.setUwi1(userWordInputDigit);
			}else if(i == 1) {
				detail.setSpi2(stimuliPolygonInputDigit);
				detail.setUpi2(userPolygonInputDigit);
				detail.setSwi2(stimuliWordInputDigit);
				detail.setUwi2(userWordInputDigit);
			}else if(i == 2) {
				detail.setSpi3(stimuliPolygonInputDigit);
				detail.setUpi3(userPolygonInputDigit);
				detail.setSwi3(stimuliWordInputDigit);
				detail.setUwi3(userWordInputDigit);
			}else if(i == 3) {
				detail.setSpi4(stimuliPolygonInputDigit);
				detail.setUpi4(userPolygonInputDigit);
				detail.setSwi4(stimuliWordInputDigit);
				detail.setUwi4(userWordInputDigit);
			}
		}
		
		detail.setStimuliPolygonInput(stimuliPolygonInput.substring(0, stimuliPolygonInput.length() - 1));
		detail.setStimuliWordInput(stimuliWordInput.substring(0, stimuliWordInput.length() - 1));
		if(userPolygonInput != null && !userPolygonInput.equals("")) {
			detail.setUserPolygonInput(userPolygonInput.substring(0, userPolygonInput.length() - 1));
		}
		if(userWordInput != null && !userWordInput.equals("")) {
			detail.setUserWordInput(userWordInput.substring(0, userWordInput.length() - 1));
		}
		
		//fill extra user input
		if(userData.getUserPolygonInput().size() > userData.getStimuliPolygonInput().size()) {
			String extraPolygonInput = "";
			String extraWordInput = "";
			for(int i = userData.getStimuliPolygonInput().size(); i < userData.getUserPolygonInput().size(); i++) {
				extraPolygonInput += userData.getUserPolygonInput().get(i) + ",";
				extraWordInput += userData.getResponseWordInput().get(i) + ",";
			}
			
			detail.setExtraUserPolygonInput(extraPolygonInput.substring(0, extraPolygonInput.length() - 1));
			detail.setExtraUserWordInput(extraWordInput.substring(0, extraWordInput.length() - 1));
			detail.setUserPolygonInput(detail.getUserPolygonInput() + "," + detail.getExtraUserPolygonInput());
			detail.setUserWordInput(detail.getUserWordInput() + "," + detail.getExtraUserWordInput());
		}
		//fill * if data need to be check
		if(userData.getNeedCheck()) {
			detail.setComment("*");
		}
		//fill score and numOfDigitsCorrect
		int[] stimuliInputArray = new int[totalCount];
		int[] userInputArray = new int[totalCount];
		for(int i = 0; i < userData.getStimuliWordInput().size(); i++) {
			stimuliInputArray[userData.getStimuliWordInput().get(i) - 1] = userData.getStimuliPolygonInput().get(i);
		}
		for(int i = 0; i < userData.getResponseWordInput().size(); i++) {
			userInputArray[userData.getResponseWordInput().get(i) - 1] = userData.getUserPolygonInput().get(i);
		}
		for(int i = 0; i < stimuliInputArray.length; i++) {
			if(stimuliInputArray[i] != 0 && stimuliInputArray[i] == userInputArray[i]) {
				numOfDigitsCorrect++;
			}
		}
		detail.setScore(numOfDigitsCorrect == userData.getStimuliWordInput().size() && detail.getExtraUserWordInput() == null ? 1 : 0);
		detail.setNumOfDigitsCorrect(numOfDigitsCorrect);
		detail.setTrialType(userData.getTrialType());
		if(userData.getEndTime() != null && userData.getStartTime() != null) {
			detail.setResponseTime(userData.getEndTime() - userData.getStartTime());
		}
		return repo.createDetailData(detail);
	}

	private void updateSummaryData(CrossModalBindingUserData userData, DetailCrossModalBinding detailData) {
		String childId_grade = userData.getChildId() + "_" + userData.getGrade();
		SummaryCrossModalBinding summary = repo.fecthSummaryData(childId_grade);
		int listLength = userData.getStimuliPolygonInput().size();
		int indexOfTrial = userData.getIndexOfTrial();
		if(summary == null) {
			//create
			summary = new SummaryCrossModalBinding();
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
	
	private SummaryCrossModalBinding getSummaryData(int listLength, int indexOfTrial, int score, int numOfDigitsCorrect, String trialType, SummaryCrossModalBinding summary) {
		if(trialType != null && trialType.equals("practice")) {
			if(indexOfTrial == 0) {
				summary.setCmbpi1(numOfDigitsCorrect + "");
				summary.setCmbpt1(score + "");
			}else if(indexOfTrial == 1) {
				summary.setCmbpi2(numOfDigitsCorrect + "");
				summary.setCmbpt2(score + "");
			}else if(indexOfTrial == 2) {
				summary.setCmbpi3(numOfDigitsCorrect + "");
				summary.setCmbpt3(score + "");
			}
		}else if(listLength == 2) {
			switch (indexOfTrial){
				case 1: {
					summary.setCmbi21(numOfDigitsCorrect + "");
					summary.setCmbt21(score + "");
					break;
				}
				case 2: {
					summary.setCmbi22(numOfDigitsCorrect + "");
					summary.setCmbt22(score + "");
					break;
				}
				case 3: {
					summary.setCmbi23(numOfDigitsCorrect + "");
					summary.setCmbt23(score + "");
					break;
				}
				case 4: {
					summary.setCmbi24(numOfDigitsCorrect + "");
					summary.setCmbt24(score + "");
					break;
				}
			}
		}else if(listLength == 3) {
			switch (indexOfTrial){
				case 1: {
					summary.setCmbi31(numOfDigitsCorrect + "");
					summary.setCmbt31(score + "");
					break;
				}
				case 2: {
					summary.setCmbi32(numOfDigitsCorrect + "");
					summary.setCmbt32(score + "");
					break;
				}
				case 3: {
					summary.setCmbi33(numOfDigitsCorrect + "");
					summary.setCmbt33(score + "");
					break;
				}
				case 4: {
					summary.setCmbi34(numOfDigitsCorrect + "");
					summary.setCmbt34(score + "");
					break;
				}
			}
		}else if(listLength == 4) {
			switch (indexOfTrial){
			case 1: {
				summary.setCmbi41(numOfDigitsCorrect + "");
				summary.setCmbt41(score + "");
				break;
			}
			case 2: {
				summary.setCmbi42(numOfDigitsCorrect + "");
				summary.setCmbt42(score + "");
				break;
			}
			case 3: {
				summary.setCmbi43(numOfDigitsCorrect + "");
				summary.setCmbt43(score + "");
				break;
			}
			case 4: {
				summary.setCmbi44(numOfDigitsCorrect + "");
				summary.setCmbt44(score + "");
				break;
			}
		}
	}
		return summary;
	}

	private GameProgressDataView updateCoinsAndRocksInGameProgress(CrossModalBindingUserData userData, DetailCrossModalBinding detailData) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(userData.getChildId(), userData.getGrade(), GameIdType.Cross_Modal_Binding.toString());
		if(dbProgress != null) {
			if(userData.getTrialType() == null || !userData.getTrialType().equals("practice")) {
				dbProgress.setNextListIndex(userData.getCurrentListIndex() + 1);
				
				if(dbProgress.getRocks() != null) {
					dbProgress.setRocks(detailData.getNumOfDigitsCorrect() == 0 ? dbProgress.getRocks() + 1 : dbProgress.getRocks());
				}else {
					dbProgress.setRocks(detailData.getNumOfDigitsCorrect() == 0 ? 1 : 0);
				}
				int listLength = userData.getStimuliPolygonInput().size();
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
