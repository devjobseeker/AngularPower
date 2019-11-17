package org.asu.chilll.power.dataview;

import java.util.List;

public class GameProgressDataView {
	private String childId;
	private String gameId;
	private String grade;
	private String gameStatus;
	private Integer currentListIndex;
	private Integer currentGroupIndex;
	private Integer rocks;
	private Integer coins;
	private Integer totalTrialCount;
	private Integer totalCorrectCount;
	private List<Integer> consecutiveTrialResult;
	private Integer repetitionCount;	//for repetition detection games
	private Integer numOfBoxes;	//for number update games
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	public String getGameId() {
		return gameId;
	}
	public void setGameId(String gameId) {
		this.gameId = gameId;
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
	public Integer getRocks() {
		return rocks;
	}
	public void setRocks(Integer rocks) {
		this.rocks = rocks;
	}
	public Integer getCoins() {
		return coins;
	}
	public void setCoins(Integer coins) {
		this.coins = coins;
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
	public List<Integer> getConsecutiveTrialResult() {
		return consecutiveTrialResult;
	}
	public void setConsecutiveTrialResult(List<Integer> consecutiveTrialResult) {
		this.consecutiveTrialResult = consecutiveTrialResult;
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
}