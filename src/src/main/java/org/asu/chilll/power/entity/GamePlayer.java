package org.asu.chilll.power.entity;

public class GamePlayer {
	private String childId;
	private Integer year;
	private Integer totalCoins;
	private Integer totalRocks;
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	public Integer getTotalCoins() {
		return totalCoins;
	}
	public void setTotalCoins(Integer totalCoins) {
		this.totalCoins = totalCoins;
	}
	public Integer getTotalRocks() {
		return totalRocks;
	}
	public void setTotalRocks(Integer totalRocks) {
		this.totalRocks = totalRocks;
	}
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
	}
}
