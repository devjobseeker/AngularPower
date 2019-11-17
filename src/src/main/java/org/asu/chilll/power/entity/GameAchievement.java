package org.asu.chilll.power.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//@Entity	//not used
//@Table(name = "game_achievement")
public class GameAchievement {
	@Id
	private String uid;
	@Column(name = "game_id")
	private String gameId;
	@Column(name = "child_id")
	private String childId;
	private Integer year;
	private Integer rocks;
	private Integer coins;

	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "create_time")
	private Long createTime;
	
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
	public Integer getYear() {
		return year;
	}
	public void setYear(Integer year) {
		this.year = year;
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
}
