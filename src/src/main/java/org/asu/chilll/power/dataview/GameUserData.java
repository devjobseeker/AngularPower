package org.asu.chilll.power.dataview;

import java.util.List;

public class GameUserData {
	private List<Integer> userInput;
	private List<Integer> stimuliInput;
	private Integer indexOfTrial;	//value could be 1, 2, 3, 4
	private Long startTime;
	private Long endTime;
	private Boolean needCheck;
	private String trialType;
	private Integer currentListIndex;
	private List<Integer> consecutiveTrialResult;
	private String childId;
	private String grade;
	private String experimenter;
	public List<Integer> getUserInput() {
		return userInput;
	}
	public void setUserInput(List<Integer> userInput) {
		this.userInput = userInput;
	}
	public List<Integer> getStimuliInput() {
		return stimuliInput;
	}
	public void setStimuliInput(List<Integer> stimuliInput) {
		this.stimuliInput = stimuliInput;
	}
	public Integer getIndexOfTrial() {
		return indexOfTrial;
	}
	public void setIndexOfTrial(Integer indexOfTrial) {
		this.indexOfTrial = indexOfTrial;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getEndTime() {
		return endTime;
	}
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	public Boolean getNeedCheck() {
		return needCheck;
	}
	public void setNeedCheck(Boolean needCheck) {
		this.needCheck = needCheck;
	}
	public String getTrialType() {
		return trialType;
	}
	public void setTrialType(String trialType) {
		this.trialType = trialType;
	}
	public Integer getCurrentListIndex() {
		return currentListIndex;
	}
	public void setCurrentListIndex(Integer currentListIndex) {
		this.currentListIndex = currentListIndex;
	}
	public List<Integer> getConsecutiveTrialResult() {
		return consecutiveTrialResult;
	}
	public void setConsecutiveTrialResult(List<Integer> consecutiveTrialResult) {
		this.consecutiveTrialResult = consecutiveTrialResult;
	}
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getExperimenter() {
		return experimenter;
	}
	public void setExperimenter(String experimenter) {
		this.experimenter = experimenter;
	}
}
