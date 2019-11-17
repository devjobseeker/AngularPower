package org.asu.chilll.power.dataview.redcap.records;

import java.util.Date;

public class GameProgressRecord {
	private String record_id;
	private String uid;
	private String gameId;
	private String childId;
	private String grade;
	private String gameStatus;
	private Integer nextListIndex;
	private Integer currentGroupIndex;
	private String consecutiveTrialResult;	//save 4 consecutive trial result, eg. 1111, 0000
	private Integer coins;
	private Integer rocks;
	private Integer totalTrialCount;
	private Integer totalCorrectCount;
	private Integer repetitionCount;	//used in repetition detection game
	private Integer numOfBoxes;	//used in number update games
	
	private Date createDate;
	private Long createTime;
	private Date lastUpdateDate;
	private Long lastUpdateTime;
	public String getRecord_id() {
		return record_id;
	}
	public void setRecord_id(String record_id) {
		this.record_id = record_id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
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
	public String getGameStatus() {
		return gameStatus;
	}
	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}
	public Integer getNextListIndex() {
		return nextListIndex;
	}
	public void setNextListIndex(Integer nextListIndex) {
		this.nextListIndex = nextListIndex;
	}
	public Integer getCurrentGroupIndex() {
		return currentGroupIndex;
	}
	public void setCurrentGroupIndex(Integer currentGroupIndex) {
		this.currentGroupIndex = currentGroupIndex;
	}
	public String getConsecutiveTrialResult() {
		return consecutiveTrialResult;
	}
	public void setConsecutiveTrialResult(String consecutiveTrialResult) {
		this.consecutiveTrialResult = consecutiveTrialResult;
	}
	public Integer getCoins() {
		return coins;
	}
	public void setCoins(Integer coins) {
		this.coins = coins;
	}
	public Integer getRocks() {
		return rocks;
	}
	public void setRocks(Integer rocks) {
		this.rocks = rocks;
	}
	public Integer getTotalTrialCount() {
		return totalTrialCount;
	}
	public void setTotalTrialCount(Integer totalTrialCount) {
		this.totalTrialCount = totalTrialCount;
	}
	public Integer getTotalCorrectCount() {
		return totalCorrectCount;
	}
	public void setTotalCorrectCount(Integer totalCorrectCount) {
		this.totalCorrectCount = totalCorrectCount;
	}
	public Integer getRepetitionCount() {
		return repetitionCount;
	}
	public void setRepetitionCount(Integer repetitionCount) {
		this.repetitionCount = repetitionCount;
	}
	public Integer getNumOfBoxes() {
		return numOfBoxes;
	}
	public void setNumOfBoxes(Integer numOfBoxes) {
		this.numOfBoxes = numOfBoxes;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}
	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	public Long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(Long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
}
