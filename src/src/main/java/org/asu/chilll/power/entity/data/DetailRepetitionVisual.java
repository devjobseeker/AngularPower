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
        name = "DetailRepetitionVisual.fetchPendingDetailRepetitionVisualRecordTotalCount",
        query = "SELECT count(d.childId) FROM DetailRepetitionVisual d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade"
    ),
    @NamedQuery(
        name = "DetailRepetitionVisual.fetchDetailRepetitionVisualListByTimestamp",
        query = "SELECT d FROM DetailRepetitionVisual d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade order by d.createTime"
    ),
    @NamedQuery(
        name = "DetailRepetitionVisual.fetchDistributedRepetitionVisualDetailTotalCount",
        query = "SELECT count(d.uid) FROM DetailRepetitionVisual d where d.createTime > :createTime"
    )
})

@Entity
@Table(name = "detail_repetition_visual")
public class DetailRepetitionVisual {
	@Id
	private String uid;
	@Column(name = "child_id")
	private String childId;
	private String grade;
	private String experimenter;
	@Column(name = "stimuli_input1", length = 10)
	private String stimuliInput1;
	@Column(name = "stimuli_input2", length = 10)
	private String stimuliInput2;
	@Column(name = "stimuli_input3", length = 10)
	private String stimuliInput3;
	@Column(name = "user_input")
	private String userInput;	//press, none
	@Column(name = "correct_response")
	private String correctRepsonse;
	@Column(name = "block_index")
	private Integer blockIndex;
	@Column(name = "trial_index")
	private Integer trialIndex;
	
	private Integer score;
	private String comment;
	@Column(name = "trial_type")
	private String trialType;	//practice
	@Column(name = "repetition_count")
	private Integer repetitionCount;
	@Column(name = "response_time")
	private Long responseTime;
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
	public String getCorrectRepsonse() {
		return correctRepsonse;
	}
	public void setCorrectRepsonse(String correctRepsonse) {
		this.correctRepsonse = correctRepsonse;
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
	public Integer getRepetitionCount() {
		return repetitionCount;
	}
	public void setRepetitionCount(Integer repetitionCount) {
		this.repetitionCount = repetitionCount;
	}
	public Long getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
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
