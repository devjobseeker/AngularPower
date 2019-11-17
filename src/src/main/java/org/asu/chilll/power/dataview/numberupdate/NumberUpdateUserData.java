package org.asu.chilll.power.dataview.numberupdate;

import java.util.List;

public class NumberUpdateUserData {
	private List<Integer> userInput;	//left, right
	private List<Integer> lastUserInput;
	private List<Integer> stimuliInputBefore;	//input before increment
	private List<Integer> stimuliIncrementInput;	//left, right increment input
	private List<Integer> stimuliInitialVal;
	private Integer indexOfTrial;	//value could be 1, 2, 3, 4
	private Long startTime;
	private Long endTime;
	private Boolean needCheck;
	private String trialType;
	private Integer currentListIndex;
	private Integer currentGroupIndex;
	private Integer numOfBoxes;
	private String childId;
	private String grade;
	private String experimenter;
	public List<Integer> getUserInput() {
		return userInput;
	}
	public void setUserInput(List<Integer> userInput) {
		this.userInput = userInput;
	}
	public List<Integer> getLastUserInput() {
		return lastUserInput;
	}
	public void setLastUserInput(List<Integer> lastUserInput) {
		this.lastUserInput = lastUserInput;
	}
	public List<Integer> getStimuliInputBefore() {
		return stimuliInputBefore;
	}
	public void setStimuliInputBefore(List<Integer> stimuliInputBefore) {
		this.stimuliInputBefore = stimuliInputBefore;
	}
	public List<Integer> getStimuliIncrementInput() {
		return stimuliIncrementInput;
	}
	public void setStimuliIncrementInput(List<Integer> stimuliIncrementInput) {
		this.stimuliIncrementInput = stimuliIncrementInput;
	}
	public List<Integer> getStimuliInitialVal() {
		return stimuliInitialVal;
	}
	public void setStimuliInitialVal(List<Integer> stimuliInitialVal) {
		this.stimuliInitialVal = stimuliInitialVal;
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
	public Integer getCurrentGroupIndex() {
		return currentGroupIndex;
	}
	public void setCurrentGroupIndex(Integer currentGroupIndex) {
		this.currentGroupIndex = currentGroupIndex;
	}
	public Integer getNumOfBoxes() {
		return numOfBoxes;
	}
	public void setNumOfBoxes(Integer numOfBoxes) {
		this.numOfBoxes = numOfBoxes;
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
