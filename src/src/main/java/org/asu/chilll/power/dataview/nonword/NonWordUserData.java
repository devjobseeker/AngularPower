package org.asu.chilll.power.dataview.nonword;

public class NonWordUserData {
	private String stimuliInput;
	private String userInputFileName;
	private String userInputFileUid;
	private Boolean userAnswerCorrect;
	private Integer indexOfTrial;	//value could be 1, 2, 3, 4
	private Long startTime;
	private Long endTime;
	private Boolean needCheck;
	private String trialType;
	private Integer currentListIndex;
	private String childId;
	private String grade;
	private String experimenter;
	public String getStimuliInput() {
		return stimuliInput;
	}
	public void setStimuliInput(String stimuliInput) {
		this.stimuliInput = stimuliInput;
	}
	public String getUserInputFileName() {
		return userInputFileName;
	}
	public void setUserInputFileName(String userInputFileName) {
		this.userInputFileName = userInputFileName;
	}
	public String getUserInputFileUid() {
		return userInputFileUid;
	}
	public void setUserInputFileUid(String userInputFileUid) {
		this.userInputFileUid = userInputFileUid;
	}
	public Boolean getUserAnswerCorrect() {
		return userAnswerCorrect;
	}
	public void setUserAnswerCorrect(Boolean userAnswerCorrect) {
		this.userAnswerCorrect = userAnswerCorrect;
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
