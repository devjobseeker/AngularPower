package org.asu.chilll.power.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.asu.chilll.power.entity.pk.StudentProfilePK;

@NamedQueries({
    @NamedQuery(
        name = "StudentProfile.fetchStudentProfileByTimestamp",
        query = "SELECT p FROM StudentProfile p where p.lastUpdateTime > :lastUpdateTime and p.childId = :childId and p.grade = :grade order by p.lastUpdateTime"
    )
})

@Entity
@Table(name = "student_profile")
@IdClass(value = StudentProfilePK.class)
public class StudentProfile {
	private String uid;
	@Id
	@Column(name = "child_id")
	private String childId;
	@Id
	private String grade;
	@Column(name = "pirate_character")
	private String pirateCharacter;
	@Column(name = "intro1_completed")
	private Boolean intro1Completed;
	@Column(name = "intro2_completed")
	private Boolean intro2Completed;
	@Column(name = "day1completed")
	private Boolean day1Completed;
	@Column(name = "day2completed")
	private Boolean day2Completed;
	@Column(name = "total_coins")
	private Integer totalCoins;
	@Column(name = "total_rocks")
	private Integer totalRocks;
	
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
	public String getPirateCharacter() {
		return pirateCharacter;
	}
	public void setPirateCharacter(String pirateCharacter) {
		this.pirateCharacter = pirateCharacter;
	}
	public Boolean getIntro1Completed() {
		return intro1Completed;
	}
	public void setIntro1Completed(Boolean intro1Completed) {
		this.intro1Completed = intro1Completed;
	}
	public Boolean getIntro2Completed() {
		return intro2Completed;
	}
	public void setIntro2Completed(Boolean intro2Completed) {
		this.intro2Completed = intro2Completed;
	}
	public Boolean getDay1Completed() {
		return day1Completed;
	}
	public void setDay1Completed(Boolean day1Completed) {
		this.day1Completed = day1Completed;
	}
	public Boolean getDay2Completed() {
		return day2Completed;
	}
	public void setDay2Completed(Boolean day2Completed) {
		this.day2Completed = day2Completed;
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
