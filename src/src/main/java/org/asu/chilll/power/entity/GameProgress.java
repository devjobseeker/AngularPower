package org.asu.chilll.power.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.asu.chilll.power.entity.pk.GameProgressPK;

@NamedQueries({
    @NamedQuery(
        name = "GameProgress.fetchListGameProgress",
        query = "SELECT p FROM GameProgress p where p.childId = :childId and p.grade = :grade and p.gameId in (:gameIds) order by p.createTime"
    ),
    @NamedQuery(
        name = "GameProgress.fetchDistributedGameProgressTotalCount",
        query = "SELECT count(p.uid) FROM GameProgress p where p.lastUpdateTime > :lastUpdateTime"
    ),
    @NamedQuery(
        name = "GameProgress.fetchGameProgressByChildIdAndGrade",
        query = "SELECT p FROM GameProgress p where p.childId = :childId and p.grade = :grade order by p.createTime"
    )
})

@Entity
@IdClass(value = GameProgressPK.class)
@Table(name = "game_progress")
public class GameProgress {
	private String uid;
	@Column(name = "game_id")
	@Id
	private String gameId;
	@Column(name = "child_id")
	@Id
	private String childId;
	@Id
	private String grade;
	//private Integer year;
	@Column(name = "game_status")
	private String gameStatus;
	@Column(name = "next_list_index")
	private Integer nextListIndex;
	@Column(name = "current_group_index")
	private Integer currentGroupIndex;
	@Column(name = "consecutive_trial_result")
	private String consecutiveTrialResult;	//save 4 consecutive trial result, eg. 1111, 0000
	private Integer coins;
	private Integer rocks;
	@Column(name = "total_trial_count")
	private Integer totalTrialCount;
	@Column(name = "total_correct_count")
	private Integer totalCorrectCount;
	@Column(name = "repetition_count")
	private Integer repetitionCount;	//used in repetition detection game
	@Column(name = "num_of_boxes")	
	private Integer numOfBoxes;	//used in number update games
	
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "create_time")
	private Long createTime;
	@Column(name = "last_update_date")
	private Date lastUpdateDate;
	@Column(name = "last_update_time")
	private Long lastUpdateTime;
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
