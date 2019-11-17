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
        name = "DetailNumberUpdateAuditory.fetchPendingDetailNumberUpdateAuditoryRecordTotalCount",
        query = "SELECT count(d.childId) FROM DetailNumberUpdateAuditory d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade"
    ),
    @NamedQuery(
        name = "DetailNumberUpdateAuditory.fetchDetailNumberUpdateAuditoryListByTimestamp",
        query = "SELECT d FROM DetailNumberUpdateAuditory d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade order by d.createTime"
    ),
    @NamedQuery(
        name = "DetailNumberUpdateAuditory.fetchDistributedNumberUpdateAuditoryDetailTotalCount",
        query = "SELECT count(d.uid) FROM DetailNumberUpdateAuditory d where d.createTime > :createTime"
    )
})

@Entity
@Table(name = "detail_number_update_auditory")
public class DetailNumberUpdateAuditory {
	@Id
	private String uid;
	@Column(name = "child_id")
	private String childId;
	private String grade;
	private String experimenter;
	@Column(name = "num_of_boxes")
	private Integer numOfBoxes;
	@Column(name = "block_index")
	private Integer blockIndex;
	@Column(name = "trial_index")
	private Integer trialIndex;
	
	@Column(name = "stimuli_left_initial", length = 10)
	private String stimuliLeftInitial;
	@Column(name = "stimuli_left_val_before", length = 10)
	private String stimuliLeftValBefore;
	@Column(name = "stimuli_left_val_after", length = 10)
	private String stimuliLeftValAfter;
	@Column(name = "stimuli_left_increment", length = 10)
	private String stimuliLeftIncrement;
	@Column(name = "stimuli_right_initial", length = 10)
	private String stimuliRightInitial;
	@Column(name = "stimuli_right_val_before", length = 10)
	private String stimuliRightValBefore;
	@Column(name = "stimuli_right_val_after", length = 10)
	private String stimuliRightValAfter;
	@Column(name = "stimuli_right_increment", length = 10)
	private String stimuliRightIncrement;
	@Column(name = "stimuli_middle_initial", length = 10)
	private String stimuliMiddleInitial;
	@Column(name = "stimuli_middle_val_before", length = 10)
	private String stimuliMiddleValBefore;
	@Column(name = "stimuli_middle_val_after", length = 10)
	private String stimuliMiddleValAfter;
	@Column(name = "stimuli_middle_increment", length = 10)
	private String stimuliMiddleIncrement;
	
	@Column(name = "user_left_val_before", length = 10)
	private String userLeftValBefore;
	@Column(name = "user_right_val_before", length = 10)
	private String userRightValBefore;
	@Column(name = "user_middle_val_before", length = 10)
	private String userMiddleValBefore;
	@Column(name = "user_left_val", length = 10)
	private String userLeftVal;
	@Column(name = "user_right_val", length = 10)
	private String userRightVal;
	@Column(name = "user_middle_val", length = 10)
	private String userMiddleVal;
	
	@Column(name = "response_time")
	private Long responseTime;
	@Column(name = "lenient_correct")
	private Integer lenientCorrect;
	@Column(name = "num_of_boxes_lenient_correct")
	private Integer numOfBoxesLenientCorrect;
	@Column(name = "absolute_correct")
	private Integer absoluteCorrect;
	@Column(name = "num_of_boxes_absolute_correct")
	private Integer numOfBoxesAbsoluteCorrect;
	private Integer score;
	private String comment;
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
	public Integer getNumOfBoxes() {
		return numOfBoxes;
	}
	public void setNumOfBoxes(Integer numOfBoxes) {
		this.numOfBoxes = numOfBoxes;
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
	public String getStimuliLeftInitial() {
		return stimuliLeftInitial;
	}
	public void setStimuliLeftInitial(String stimuliLeftInitial) {
		this.stimuliLeftInitial = stimuliLeftInitial;
	}
	public String getStimuliLeftValBefore() {
		return stimuliLeftValBefore;
	}
	public void setStimuliLeftValBefore(String stimuliLeftValBefore) {
		this.stimuliLeftValBefore = stimuliLeftValBefore;
	}
	public String getStimuliLeftValAfter() {
		return stimuliLeftValAfter;
	}
	public void setStimuliLeftValAfter(String stimuliLeftValAfter) {
		this.stimuliLeftValAfter = stimuliLeftValAfter;
	}
	public String getStimuliLeftIncrement() {
		return stimuliLeftIncrement;
	}
	public void setStimuliLeftIncrement(String stimuliLeftIncrement) {
		this.stimuliLeftIncrement = stimuliLeftIncrement;
	}
	public String getStimuliRightInitial() {
		return stimuliRightInitial;
	}
	public void setStimuliRightInitial(String stimuliRightInitial) {
		this.stimuliRightInitial = stimuliRightInitial;
	}
	public String getStimuliRightValBefore() {
		return stimuliRightValBefore;
	}
	public void setStimuliRightValBefore(String stimuliRightValBefore) {
		this.stimuliRightValBefore = stimuliRightValBefore;
	}
	public String getStimuliRightValAfter() {
		return stimuliRightValAfter;
	}
	public void setStimuliRightValAfter(String stimuliRightValAfter) {
		this.stimuliRightValAfter = stimuliRightValAfter;
	}
	public String getStimuliRightIncrement() {
		return stimuliRightIncrement;
	}
	public void setStimuliRightIncrement(String stimuliRightIncrement) {
		this.stimuliRightIncrement = stimuliRightIncrement;
	}
	public String getStimuliMiddleInitial() {
		return stimuliMiddleInitial;
	}
	public void setStimuliMiddleInitial(String stimuliMiddleInitial) {
		this.stimuliMiddleInitial = stimuliMiddleInitial;
	}
	public String getStimuliMiddleValBefore() {
		return stimuliMiddleValBefore;
	}
	public void setStimuliMiddleValBefore(String stimuliMiddleValBefore) {
		this.stimuliMiddleValBefore = stimuliMiddleValBefore;
	}
	public String getStimuliMiddleValAfter() {
		return stimuliMiddleValAfter;
	}
	public void setStimuliMiddleValAfter(String stimuliMiddleValAfter) {
		this.stimuliMiddleValAfter = stimuliMiddleValAfter;
	}
	public String getStimuliMiddleIncrement() {
		return stimuliMiddleIncrement;
	}
	public void setStimuliMiddleIncrement(String stimuliMiddleIncrement) {
		this.stimuliMiddleIncrement = stimuliMiddleIncrement;
	}
	public String getUserLeftValBefore() {
		return userLeftValBefore;
	}
	public void setUserLeftValBefore(String userLeftValBefore) {
		this.userLeftValBefore = userLeftValBefore;
	}
	public String getUserRightValBefore() {
		return userRightValBefore;
	}
	public void setUserRightValBefore(String userRightValBefore) {
		this.userRightValBefore = userRightValBefore;
	}
	public String getUserMiddleValBefore() {
		return userMiddleValBefore;
	}
	public void setUserMiddleValBefore(String userMiddleValBefore) {
		this.userMiddleValBefore = userMiddleValBefore;
	}
	public String getUserLeftVal() {
		return userLeftVal;
	}
	public void setUserLeftVal(String userLeftVal) {
		this.userLeftVal = userLeftVal;
	}
	public String getUserRightVal() {
		return userRightVal;
	}
	public void setUserRightVal(String userRightVal) {
		this.userRightVal = userRightVal;
	}
	public String getUserMiddleVal() {
		return userMiddleVal;
	}
	public void setUserMiddleVal(String userMiddleVal) {
		this.userMiddleVal = userMiddleVal;
	}
	public Integer getLenientCorrect() {
		return lenientCorrect;
	}
	public void setLenientCorrect(Integer lenientCorrect) {
		this.lenientCorrect = lenientCorrect;
	}
	public Integer getNumOfBoxesLenientCorrect() {
		return numOfBoxesLenientCorrect;
	}
	public void setNumOfBoxesLenientCorrect(Integer numOfBoxesLenientCorrect) {
		this.numOfBoxesLenientCorrect = numOfBoxesLenientCorrect;
	}
	public Integer getNumOfBoxesAbsoluteCorrect() {
		return numOfBoxesAbsoluteCorrect;
	}
	public void setNumOfBoxesAbsoluteCorrect(Integer numOfBoxesAbsoluteCorrect) {
		this.numOfBoxesAbsoluteCorrect = numOfBoxesAbsoluteCorrect;
	}
	public Integer getAbsoluteCorrect() {
		return absoluteCorrect;
	}
	public void setAbsoluteCorrect(Integer absoluteCorrect) {
		this.absoluteCorrect = absoluteCorrect;
	}
}
