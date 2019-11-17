package org.asu.chilll.power.dataview.repetitiondetection;

public class RepetitionDetectionUserData {
	private String childId;
	private String grade;
	private String experimenter;
	private String stimuliInput1;
	private String stimuliInput2;
	private String stimuliInput3;
	private String userInput;
	private Integer blockIndex;
	private Integer trialIndex;
	private Long startTime;
	private Long endTime;
	private Boolean needCheck;
	private String trialType;
	private Integer repetitionCount;
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
	public String getStimuliInput1() {
		return stimuliInput1;
	}
	public void setStimuliInput1(String stimuliInput1) {
		this.stimuliInput1 = stimuliInput1;
	}
	public String getStimuliInput2() {
		return stimuliInput2;
	}
	public void setStimuliInput2(String stimuliInput2) {
		this.stimuliInput2 = stimuliInput2;
	}
	public String getStimuliInput3() {
		return stimuliInput3;
	}
	public void setStimuliInput3(String stimuliInput3) {
		this.stimuliInput3 = stimuliInput3;
	}
	public String getUserInput() {
		return userInput;
	}
	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
	public Integer getBlockIndex() {
		return blockIndex;
	}
	public void setBlockIndex(Integer blockIndex) {
		this.blockIndex = blockIndex;
	}
	public Integer getTrialIndex() {
		return trialIndex;
	}
	public void setTrialIndex(Integer trialIndex) {
		this.trialIndex = trialIndex;
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
	public Integer getRepetitionCount() {
		return repetitionCount;
	}
	public void setRepetitionCount(Integer repetitionCount) {
		this.repetitionCount = repetitionCount;
	}
}
