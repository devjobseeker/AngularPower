package org.asu.chilll.power.dataview.phonologicalbinding;

import java.util.List;

public class PhonologicalBindingUserData{
	private List<Integer> userSoundInput;
	private List<Integer> userWordInput;
	private List<Integer> stimuliSoundInput;
	private List<Integer> stimuliWordInput;
	private Integer indexOfTrial;	//value could be 1, 2, 3, 4
//	private Long responseTime;
	private Long startTime;
	private Long endTime;
	private Boolean needCheck;
	private String trialType;
	private Integer currentListIndex;
	private List<Integer> consecutiveTrialResult;
	private String childId;
	private String grade;
	private String experimenter;
	public List<Integer> getUserSoundInput() {
		return userSoundInput;
	}
	public void setUserSoundInput(List<Integer> userSoundInput) {
		this.userSoundInput = userSoundInput;
	}
	public List<Integer> getUserWordInput() {
		return userWordInput;
	}
	public void setUserWordInput(List<Integer> userWordInput) {
		this.userWordInput = userWordInput;
	}
	public List<Integer> getStimuliSoundInput() {
		return stimuliSoundInput;
	}
	public void setStimuliSoundInput(List<Integer> stimuliSoundInput) {
		this.stimuliSoundInput = stimuliSoundInput;
	}
	public List<Integer> getStimuliWordInput() {
		return stimuliWordInput;
	}
	public void setStimuliWordInput(List<Integer> stimuliWordInput) {
		this.stimuliWordInput = stimuliWordInput;
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
