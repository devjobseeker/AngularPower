package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.phonologicalbinding.PhonologicalBindingUserData;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.data.DetailPhonologicalBinding;
import org.asu.chilll.power.entity.data.SummaryPhonologicalBinding;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.repository.GameRepository;
import org.asu.chilll.power.repository.PhonologicalBindingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhonologicalBindingService {
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private PhonologicalBindingRepository repo;
	
	private static final int attemptReward = 3;
	
	public GameProgressDataView saveData(PhonologicalBindingUserData userData) {
		DetailPhonologicalBinding detailData = createDetailData(userData);
		updateSummaryData(userData, detailData);
		return updateCoinsAndRocksInGameProgress(userData, detailData);
	}

	private DetailPhonologicalBinding createDetailData(PhonologicalBindingUserData userData) {
		DetailPhonologicalBinding detail = new DetailPhonologicalBinding();
		detail.setChildId(userData.getChildId());
		detail.setGrade(userData.getGrade());
		detail.setExperimenter(userData.getExperimenter());
		detail.setTrialNo(userData.getCurrentListIndex() + 1);
		//fill stimuliInput digit, userInput digit
		String stimuliWordInput = "";
		String stimuliSoundInput = "";
		String userWordInput = "";
		String userSoundInput = "";
		int numOfDigitsCorrect = 0;
		int maxOrdinal = 4;
		for(int i = 0; i < maxOrdinal; i++) {
			String userWordInputDigit = "X";
			String userSoundInputDigit = "X";
			String stimuliWordInputDigit = "X";
			String stimuliSoundInputDigit = "X";
			
			if(i < userData.getUserWordInput().size() && i < userData.getStimuliWordInput().size()) {
				userWordInputDigit = userData.getUserWordInput().get(i) + "";
				userWordInput += userWordInputDigit + ",";
				userSoundInputDigit = userData.getUserSoundInput().get(i) + "";
				userSoundInput += userSoundInputDigit + ",";
			}else if(i < userData.getStimuliWordInput().size()) {
				userSoundInputDigit = "NR";
				userWordInputDigit = "NR";
			}
			
			if(i < userData.getStimuliWordInput().size()) {
				stimuliWordInputDigit = userData.getStimuliWordInput().get(i) + "";
				stimuliWordInput += stimuliWordInputDigit + ",";
				stimuliSoundInputDigit = userData.getStimuliSoundInput().get(i) + "";
				stimuliSoundInput += stimuliSoundInputDigit + ",";
			}
			
			if(i == 0) {
				detail.setSwi1(stimuliWordInputDigit);
				detail.setUwi1(userWordInputDigit);
				detail.setSsi1(stimuliSoundInputDigit);
				detail.setUsi1(userSoundInputDigit);
			}else if(i == 1) {
				detail.setSwi2(stimuliWordInputDigit);
				detail.setUwi2(userWordInputDigit);
				detail.setSsi2(stimuliSoundInputDigit);
				detail.setUsi2(userSoundInputDigit);
			}else if(i == 2) {
				detail.setSwi3(stimuliWordInputDigit);
				detail.setUwi3(userWordInputDigit);
				detail.setSsi3(stimuliSoundInputDigit);
				detail.setUsi3(userSoundInputDigit);
			}else if(i == 3) {
				detail.setSwi4(stimuliWordInputDigit);
				detail.setUwi4(userWordInputDigit);
				detail.setSsi4(stimuliSoundInputDigit);
				detail.setUsi4(userSoundInputDigit);
			}
		}
		
		detail.setStimuliWordInput(stimuliWordInput.substring(0, stimuliWordInput.length() - 1));
		detail.setStimuliSoundInput(stimuliSoundInput.substring(0, stimuliSoundInput.length() - 1));
		if(userWordInput != null && !userWordInput.equals("")) {
			detail.setUserWordInput(userWordInput.substring(0, userWordInput.length() - 1));
		}
		if(userSoundInput != null && !userSoundInput.equals("")) {
			detail.setUserSoundInput(userSoundInput.substring(0, userSoundInput.length() - 1));
		}
		
		//fill extra user input
		if(userData.getUserWordInput().size() > userData.getStimuliWordInput().size()) {
			String extraWordInput = "";
			String extraSoundInput = "";
			for(int i = userData.getStimuliWordInput().size(); i < userData.getUserWordInput().size(); i++) {
				extraWordInput += userData.getUserWordInput().get(i) + ",";
				extraSoundInput += userData.getUserSoundInput().get(i) + ",";
			}
			
			detail.setExtraUserWordInput(extraWordInput.substring(0, extraWordInput.length() - 1));
			detail.setExtraUserSoundInput(extraSoundInput.substring(0, extraSoundInput.length() - 1));
			detail.setUserWordInput(detail.getUserWordInput() + "," + detail.getExtraUserWordInput());
			detail.setUserSoundInput(detail.getUserSoundInput() + "," + detail.getExtraUserSoundInput());
		}
		//fill * if data need to be check
		if(userData.getNeedCheck()) {
			detail.setComment("*");
		}
		//fill score and numOfDigitsCorrect
		int[] stimuliInputArray = new int[41];
		int[] userInputArray = new int[41];
		for(int i = 0; i < userData.getStimuliSoundInput().size(); i++) {
			stimuliInputArray[userData.getStimuliSoundInput().get(i) - 1] = userData.getStimuliWordInput().get(i);
		}
		for(int i = 0; i < userData.getUserSoundInput().size(); i++) {
			userInputArray[userData.getUserSoundInput().get(i) - 1] = userData.getUserWordInput().get(i);
		}
		for(int i = 0; i < stimuliInputArray.length; i++) {
			if(stimuliInputArray[i] != 0 && stimuliInputArray[i] == userInputArray[i]) {
				numOfDigitsCorrect++;
			}
		}
		detail.setScore(numOfDigitsCorrect == userData.getStimuliSoundInput().size() && detail.getExtraUserSoundInput() == null ? 1 : 0);
		detail.setNumOfDigitsCorrect(numOfDigitsCorrect);
		detail.setTrialType(userData.getTrialType());
		if(userData.getStartTime() != null && userData.getEndTime() != null) {
			detail.setResponseTime(userData.getEndTime() - userData.getStartTime());
		}
		return repo.createDetailData(detail);
	}

	private void updateSummaryData(PhonologicalBindingUserData userData, DetailPhonologicalBinding detailData) {
		String childId_grade = userData.getChildId() + "_" + userData.getGrade();
		SummaryPhonologicalBinding summary = repo.fecthSummaryData(childId_grade);
		int listLength = userData.getStimuliWordInput().size();
		int indexOfTrial = userData.getIndexOfTrial();
		if(summary == null) {
			//create
			summary = new SummaryPhonologicalBinding();
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
	
	private SummaryPhonologicalBinding getSummaryData(int listLength, int indexOfTrial, int score, int numOfDigitsCorrect, String trialType, SummaryPhonologicalBinding summary) {
		if(trialType != null && trialType.equals("practice")) {
			if(indexOfTrial == 0) {
				summary.setPbspi1(numOfDigitsCorrect + "");
				summary.setPbspt1(score + "");
			}else if(indexOfTrial == 1) {
				summary.setPbspi2(numOfDigitsCorrect + "");
				summary.setPbspt2(score + "");
			}else if(indexOfTrial == 2) {
				summary.setPbspi3(numOfDigitsCorrect + "");
				summary.setPbspt3(score + "");
			}
		}else if(listLength == 2) {
			switch (indexOfTrial){
				case 1: {
					summary.setPbsi21(numOfDigitsCorrect + "");
					summary.setPbst21(score + "");
					break;
				}
				case 2: {
					summary.setPbsi22(numOfDigitsCorrect + "");
					summary.setPbst22(score + "");
					break;
				}
				case 3: {
					summary.setPbsi23(numOfDigitsCorrect + "");
					summary.setPbst23(score + "");
					break;
				}
				case 4: {
					summary.setPbsi24(numOfDigitsCorrect + "");
					summary.setPbst24(score + "");
					break;
				}
			}
		}else if(listLength == 3) {
			switch (indexOfTrial){
				case 1: {
					summary.setPbsi31(numOfDigitsCorrect + "");
					summary.setPbst31(score + "");
					break;
				}
				case 2: {
					summary.setPbsi32(numOfDigitsCorrect + "");
					summary.setPbst32(score + "");
					break;
				}
				case 3: {
					summary.setPbsi33(numOfDigitsCorrect + "");
					summary.setPbst33(score + "");
					break;
				}
				case 4: {
					summary.setPbsi34(numOfDigitsCorrect + "");
					summary.setPbst34(score + "");
					break;
				}
			}
		}else if(listLength == 4) {
			switch (indexOfTrial){
			case 1: {
				summary.setPbsi41(numOfDigitsCorrect + "");
				summary.setPbst41(score + "");
				break;
			}
			case 2: {
				summary.setPbsi42(numOfDigitsCorrect + "");
				summary.setPbst42(score + "");
				break;
			}
			case 3: {
				summary.setPbsi43(numOfDigitsCorrect + "");
				summary.setPbst43(score + "");
				break;
			}
			case 4: {
				summary.setPbsi44(numOfDigitsCorrect + "");
				summary.setPbst44(score + "");
				break;
			}
		}
	}
		return summary;
	}

	private GameProgressDataView updateCoinsAndRocksInGameProgress(PhonologicalBindingUserData userData, DetailPhonologicalBinding detailData) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(userData.getChildId(), userData.getGrade(), GameIdType.Phonological_Binding.toString());
		if(dbProgress != null) {
			if(userData.getTrialType() == null || !userData.getTrialType().equals("practice")) {
				dbProgress.setNextListIndex(userData.getCurrentListIndex() + 1);
				
				if(dbProgress.getRocks() != null) {
					dbProgress.setRocks(detailData.getNumOfDigitsCorrect() == 0 ? dbProgress.getRocks() + 1 : dbProgress.getRocks());
				}else {
					dbProgress.setRocks(detailData.getNumOfDigitsCorrect() == 0 ? 1 : 0);
				}
				
				if(dbProgress.getCoins() != null) {
					dbProgress.setCoins(detailData.getNumOfDigitsCorrect() > 0 ? dbProgress.getCoins() + attemptReward : dbProgress.getCoins());
				}else {
					dbProgress.setCoins(detailData.getNumOfDigitsCorrect() > 0 ? attemptReward : 0);
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
