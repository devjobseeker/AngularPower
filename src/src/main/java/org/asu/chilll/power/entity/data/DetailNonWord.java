package org.asu.chilll.power.entity.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
	@NamedQuery(
        name = "DetailNonWord.fetchPendingDetailNonWordRecordTotalCount",
        query = "SELECT count(d.childId) FROM DetailNonWord d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade"
    ),
    @NamedQuery(
        name = "DetailNonWord.fetchDetailNonWordListByTimestamp",
        query = "SELECT d FROM DetailNonWord d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade order by d.createTime"
    ),
    @NamedQuery(
        name = "DetailNonWord.fetchDistributedNonWordDetailTotalCount",
        query = "SELECT count(d.uid) FROM DetailNonWord d where d.createTime > :createTime"
    )
})

@Entity
@Table(name = "detail_non_word")
public class DetailNonWord {
	@Id
	private String uid;
	@Column(name = "child_id")
	private String childId;
	private String grade;
	private String experimenter;
	@Column(name = "stimuli_non_word_input")
	private String stimuliNonWordInput;
	@Column(name = "user_input_file_uid")
	private String userInputFileUid;
	@Column(name = "user_input_file_name")
	private String userInputFileName;
	
	@Column(name = "response_time")
	private Long responseTime;
	private Integer score;
	private String comment;
	@Column(name = "trial_no")
	private Integer trialNo;
	@Column(name = "trial_type")
	private String trialType;	//practice
	
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
	public String getStimuliNonWordInput() {
		return stimuliNonWordInput;
	}
	public void setStimuliNonWordInput(String stimuliNonWordInput) {
		this.stimuliNonWordInput = stimuliNonWordInput;
	}
	public String getUserInputFileUid() {
		return userInputFileUid;
	}
	public void setUserInputFileUid(String userInputFileUid) {
		this.userInputFileUid = userInputFileUid;
	}
	public String getUserInputFileName() {
		return userInputFileName;
	}
	public void setUserInputFileName(String userInputFileName) {
		this.userInputFileName = userInputFileName;
	}
	public Long getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Integer getTrialNo() {
		return trialNo;
	}
	public void setTrialNo(Integer trialNo) {
		this.trialNo = trialNo;
	}
	public String getTrialType() {
		return trialType;
	}
	public void setTrialType(String trialType) {
		this.trialType = trialType;
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