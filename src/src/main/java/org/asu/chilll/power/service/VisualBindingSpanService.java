package org.asu.chilll.power.service;

import org.asu.chilll.power.dataview.GameProgressDataView;
import org.asu.chilll.power.dataview.visualbinding.VisualBindingUserData;
import org.asu.chilll.power.entity.GameProgress;
import org.asu.chilll.power.entity.data.DetailVisualBindingSpan;
import org.asu.chilll.power.entity.data.SummaryVisualBindingSpan;
import org.asu.chilll.power.enums.GameIdType;
import org.asu.chilll.power.repository.GameRepository;
import org.asu.chilll.power.repository.VisualBindingSpanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisualBindingSpanService {
	@Autowired
	private GameRepository gameRepo;
	
	@Autowired
	private VisualBindingSpanRepository repo;
	
	private final int totalCount = 16;
	
	public GameProgressDataView saveData(VisualBindingUserData userData) {
		DetailVisualBindingSpan detailData = createDetailData(userData);
		updateSummaryData(userData, detailData);
		return updateCoinsAndRocksInGameProgress(userData, detailData);
	}

	private DetailVisualBindingSpan createDetailData(VisualBindingUserData userData) {
		DetailVisualBindingSpan detail = new DetailVisualBindingSpan();
		detail.setChildId(userData.getChildId());
		detail.setGrade(userData.getGrade());
		detail.setExperimenter(userData.getExperimenter());
		detail.setTrialNo(userData.getCurrentListIndex() + 1);
		//fill stimuliInput digit, userInput digit
		String stimuliPolygonInput = "";
		String stimuliLocationInput = "";
		String userPolygonInput = "";
		String userLocationInput = "";
		//Integer score = null;
		int numOfDigitsCorrect = 0;
		int maxOrdinal = 4;
		for(int i = 0; i < maxOrdinal; i++) {
			String userPolygonInputDigit = "X";
			String userLocationInputDigit = "X";
			String stimuliPolygonInputDigit = "X";
			String stimuliLocationInputDigit = "X";
			
			if(i < userData.getUserPolygonInput().size() && i < userData.getStimuliPolygonInput().size()) {
				userPolygonInputDigit = userData.getUserPolygonInput().get(i) + "";
				userPolygonInput += userPolygonInputDigit + ",";
				userLocationInputDigit = userData.getUserLocationInput().get(i) + "";
				userLocationInput += userLocationInputDigit + ",";
			}else if(i < userData.getStimuliPolygonInput().size()) {
				userLocationInputDigit = "NR";
				userPolygonInputDigit = "NR";
			}
			
			if(i < userData.getStimuliPolygonInput().size()) {
				stimuliPolygonInputDigit = userData.getStimuliPolygonInput().get(i) + "";
				stimuliPolygonInput += stimuliPolygonInputDigit + ",";
				stimuliLocationInputDigit = userData.getStimuliLocationInput().get(i) + "";
				stimuliLocationInput += stimuliLocationInputDigit + ",";
			}
			
			if(i == 0) {
				detail.setSpi1(stimuliPolygonInputDigit);
				detail.setUpi1(userPolygonInputDigit);
				detail.setSli1(stimuliLocationInputDigit);
				detail.setUli1(userLocationInputDigit);
			}else if(i == 1) {
				detail.setSpi2(stimuliPolygonInputDigit);
				detail.setUpi2(userPolygonInputDigit);
				detail.setSli2(stimuliLocationInputDigit);
				detail.setUli2(userLocationInputDigit);
			}else if(i == 2) {
				detail.setSpi3(stimuliPolygonInputDigit);
				detail.setUpi3(userPolygonInputDigit);
				detail.setSli3(stimuliLocationInputDigit);
				detail.setUli3(userLocationInputDigit);
			}else if(i == 3) {
				detail.setSpi4(stimuliPolygonInputDigit);
				detail.setUpi4(userPolygonInputDigit);
				detail.setSli4(stimuliLocationInputDigit);
				detail.setUli4(userLocationInputDigit);
			}
		}
		
		detail.setStimuliPolygonInput(stimuliPolygonInput.substring(0, stimuliPolygonInput.length() - 1));
		detail.setStimuliLocationInput(stimuliLocationInput.substring(0, stimuliLocationInput.length() - 1));
		if(userPolygonInput != null && !userPolygonInput.equals("")) {
			detail.setUserPolygonInput(userPolygonInput.substring(0, userPolygonInput.length() - 1));
		}
		if(userLocationInput != null && !userLocationInput.equals("")) {
			detail.setUserLocationInput(userLocationInput.substring(0, userLocationInput.length() - 1));
		}
		
		//fill extra user input
		if(userData.getUserPolygonInput().size() > userData.getStimuliPolygonInput().size()) {
			String extraPolygonInput = "";
			String extraLocationInput = "";
			for(int i = userData.getStimuliPolygonInput().size(); i < userData.getUserPolygonInput().size(); i++) {
				extraPolygonInput += userData.getUserPolygonInput().get(i) + ",";
				extraLocationInput += userData.getUserLocationInput().get(i) + ",";
			}
			
			detail.setExtraUserPolygonInput(extraPolygonInput.substring(0, extraPolygonInput.length() - 1));
			detail.setExtraUserLocationInput(extraLocationInput.substring(0, extraLocationInput.length() - 1));
			detail.setUserPolygonInput(detail.getUserPolygonInput() + "," + detail.getExtraUserPolygonInput());
			detail.setUserLocationInput(detail.getUserLocationInput() + "," + detail.getExtraUserLocationInput());
		}
		//fill * if data need to be check
		if(userData.getNeedCheck()) {
			detail.setComment("*");
		}
		//fill score and numOfDigitsCorrect
		int[] stimuliInputArray = new int[totalCount];
		int[] userInputArray = new int[totalCount];
		for(int i = 0; i < userData.getStimuliLocationInput().size(); i++) {
			stimuliInputArray[userData.getStimuliLocationInput().get(i) - 1] = userData.getStimuliPolygonInput().get(i);
		}
		for(int i = 0; i < userData.getUserLocationInput().size(); i++) {
			userInputArray[userData.getUserLocationInput().get(i) - 1] = userData.getUserPolygonInput().get(i);
		}
		for(int i = 0; i < stimuliInputArray.length; i++) {
			if(stimuliInputArray[i] != 0 && stimuliInputArray[i] == userInputArray[i]) {
				numOfDigitsCorrect++;
			}
		}
		detail.setScore(numOfDigitsCorrect == userData.getStimuliLocationInput().size() && detail.getExtraUserLocationInput() == null ? 1 : 0);
		detail.setNumOfDigitsCorrect(numOfDigitsCorrect);
		detail.setTrialType(userData.getTrialType());
		if(userData.getEndTime() != null && userData.getStartTime() != null) {
			detail.setResponseTime(userData.getEndTime() - userData.getStartTime());
		}
		return repo.createDetailData(detail);
	}

	private void updateSummaryData(VisualBindingUserData userData, DetailVisualBindingSpan detailData) {
		String childId_grade = userData.getChildId() + "_" + userData.getGrade();
		SummaryVisualBindingSpan summary = repo.fecthSummaryData(childId_grade);
		int listLength = userData.getStimuliPolygonInput().size();
		int indexOfTrial = userData.getIndexOfTrial();
		if(summary == null) {
			//create
			summary = new SummaryVisualBindingSpan();
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
	
	private SummaryVisualBindingSpan getSummaryData(int listLength, int indexOfTrial, int score, int numOfDigitsCorrect, String trialType, SummaryVisualBindingSpan summary) {
		if(trialType != null && trialType.equals("practice")) {
			if(indexOfTrial == 0) {
				summary.setVbspi1(numOfDigitsCorrect + "");
				summary.setVbspt1(score + "");
			}else if(indexOfTrial == 1) {
				summary.setVbspi2(numOfDigitsCorrect + "");
				summary.setVbspt2(score + "");
			}else if(indexOfTrial == 2) {
				summary.setVbspi3(numOfDigitsCorrect + "");
				summary.setVbspt3(score + "");
			}
		}else if(listLength == 2) {
			switch (indexOfTrial){
				case 1: {
					summary.setVbsi21(numOfDigitsCorrect + "");
					summary.setVbst21(score + "");
					break;
				}
				case 2: {
					summary.setVbsi22(numOfDigitsCorrect + "");
					summary.setVbst22(score + "");
					break;
				}
				case 3: {
					summary.setVbsi23(numOfDigitsCorrect + "");
					summary.setVbst23(score + "");
					break;
				}
				case 4: {
					summary.setVbsi24(numOfDigitsCorrect + "");
					summary.setVbst24(score + "");
					break;
				}
			}
		}else if(listLength == 3) {
			switch (indexOfTrial){
				case 1: {
					summary.setVbsi31(numOfDigitsCorrect + "");
					summary.setVbst31(score + "");
					break;
				}
				case 2: {
					summary.setVbsi32(numOfDigitsCorrect + "");
					summary.setVbst32(score + "");
					break;
				}
				case 3: {
					summary.setVbsi33(numOfDigitsCorrect + "");
					summary.setVbst33(score + "");
					break;
				}
				case 4: {
					summary.setVbsi34(numOfDigitsCorrect + "");
					summary.setVbst34(score + "");
					break;
				}
			}
		}else if(listLength == 4) {
			switch (indexOfTrial){
			case 1: {
				summary.setVbsi41(numOfDigitsCorrect + "");
				summary.setVbst41(score + "");
				break;
			}
			case 2: {
				summary.setVbsi42(numOfDigitsCorrect + "");
				summary.setVbst42(score + "");
				break;
			}
			case 3: {
				summary.setVbsi43(numOfDigitsCorrect + "");
				summary.setVbst43(score + "");
				break;
			}
			case 4: {
				summary.setVbsi44(numOfDigitsCorrect + "");
				summary.setVbst44(score + "");
				break;
			}
		}
	}
		return summary;
	}

	private GameProgressDataView updateCoinsAndRocksInGameProgress(VisualBindingUserData userData, DetailVisualBindingSpan detailData) {
		GameProgress dbProgress = gameRepo.fetchGameProgress(userData.getChildId(), userData.getGrade(), GameIdType.Visual_Binding_Span.toString());
		if(dbProgress != null) {
			if(userData.getTrialType() == null || !userData.getTrialType().equals("practice")) {
				dbProgress.setNextListIndex(userData.getCurrentListIndex() + 1);
				
				if(dbProgress.getRocks() != null) {
					dbProgress.setRocks(detailData.getScore() == 0 ? dbProgress.getRocks() + 1 : dbProgress.getRocks());
				}else {
					dbProgress.setRocks(detailData.getScore() == 0 ? 1 : 0);
				}
				
				int listLength = userData.getStimuliLocationInput().size();
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
