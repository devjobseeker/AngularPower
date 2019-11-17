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
        name = "DetailPhonologicalBinding.fetchPendingDetailPhonologicalBindingRecordTotalCount",
        query = "SELECT count(d.childId) FROM DetailPhonologicalBinding d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade"
    ),
    @NamedQuery(
        name = "DetailPhonologicalBinding.fetchDetailPhonologicalBindingListByTimestamp",
        query = "SELECT d FROM DetailPhonologicalBinding d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade order by d.createTime"
    ),
    @NamedQuery(
        name = "DetailPhonologicalBinding.fetchDistributedPhonologicalBindingDetailTotalCount",
        query = "SELECT count(d.uid) FROM DetailPhonologicalBinding d where d.createTime > :createTime"
    )
})

@Entity
@Table(name = "detail_phonological_binding")
public class DetailPhonologicalBinding {
	@Id
	private String uid;
	@Column(name = "child_id")
	private String childId;
	private String grade;
	private String experimenter;
	@Column(name = "stimuli_sound_input")
	private String stimuliSoundInput;
	@Column(name = "stimuli_word_input")
	private String stimuliWordInput;
	@Column(name = "user_sound_input")
	private String userSoundInput;
	@Column(name = "user_word_input")
	private String userWordInput;
	@Column(name = "extra_user_sound_input")
	private String extraUserSoundInput;
	@Column(name = "extra_user_word_input")
	private String extraUserWordInput;
	
	@Column(length = 10)
	private String ssi1;
	@Column(length = 10)
	private String ssi2;
	@Column(length = 10)
	private String ssi3;
	@Column(length = 10)
	private String ssi4;
	
	@Column(length = 10)
	private String swi1;
	@Column(length = 10)
	private String swi2;
	@Column(length = 10)
	private String swi3;
	@Column(length = 10)
	private String swi4;
	
	@Column(length = 10)
	private String usi1;
	@Column(length = 10)
	private String usi2;
	@Column(length = 10)
	private String usi3;
	@Column(length = 10)
	private String usi4;
	
	@Column(length = 10)
	private String uwi1;
	@Column(length = 10)
	private String uwi2;
	@Column(length = 10)
	private String uwi3;
	@Column(length = 10)
	private String uwi4;
	
	@Column(name = "response_time")
	private Long responseTime;
	
	private Integer score;
	@Column(name = "num_of_digits_correct")
	private Integer numOfDigitsCorrect;
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
	public String getStimuliSoundInput() {
		return stimuliSoundInput;
	}
	public void setStimuliSoundInput(String stimuliSoundInput) {
		this.stimuliSoundInput = stimuliSoundInput;
	}
	public String getStimuliWordInput() {
		return stimuliWordInput;
	}
	public void setStimuliWordInput(String stimuliWordInput) {
		this.stimuliWordInput = stimuliWordInput;
	}
	public String getUserSoundInput() {
		return userSoundInput;
	}
	public void setUserSoundInput(String userSoundInput) {
		this.userSoundInput = userSoundInput;
	}
	public String getUserWordInput() {
		return userWordInput;
	}
	public void setUserWordInput(String userWordInput) {
		this.userWordInput = userWordInput;
	}
	public String getExtraUserSoundInput() {
		return extraUserSoundInput;
	}
	public void setExtraUserSoundInput(String extraUserSoundInput) {
		this.extraUserSoundInput = extraUserSoundInput;
	}
	public String getExtraUserWordInput() {
		return extraUserWordInput;
	}
	public void setExtraUserWordInput(String extraUserWordInput) {
		this.extraUserWordInput = extraUserWordInput;
	}
	public String getSsi1() {
		return ssi1;
	}
	public void setSsi1(String ssi1) {
		this.ssi1 = ssi1;
	}
	public String getSsi2() {
		return ssi2;
	}
	public void setSsi2(String ssi2) {
		this.ssi2 = ssi2;
	}
	public String getSsi3() {
		return ssi3;
	}
	public void setSsi3(String ssi3) {
		this.ssi3 = ssi3;
	}
	public String getSsi4() {
		return ssi4;
	}
	public void setSsi4(String ssi4) {
		this.ssi4 = ssi4;
	}
	public String getSwi1() {
		return swi1;
	}
	public void setSwi1(String swi1) {
		this.swi1 = swi1;
	}
	public String getSwi2() {
		return swi2;
	}
	public void setSwi2(String swi2) {
		this.swi2 = swi2;
	}
	public String getSwi3() {
		return swi3;
	}
	public void setSwi3(String swi3) {
		this.swi3 = swi3;
	}
	public String getSwi4() {
		return swi4;
	}
	public void setSwi4(String swi4) {
		this.swi4 = swi4;
	}
	public String getUsi1() {
		return usi1;
	}
	public void setUsi1(String usi1) {
		this.usi1 = usi1;
	}
	public String getUsi2() {
		return usi2;
	}
	public void setUsi2(String usi2) {
		this.usi2 = usi2;
	}
	public String getUsi3() {
		return usi3;
	}
	public void setUsi3(String usi3) {
		this.usi3 = usi3;
	}
	public String getUsi4() {
		return usi4;
	}
	public void setUsi4(String usi4) {
		this.usi4 = usi4;
	}
	public String getUwi1() {
		return uwi1;
	}
	public void setUwi1(String uwi1) {
		this.uwi1 = uwi1;
	}
	public String getUwi2() {
		return uwi2;
	}
	public void setUwi2(String uwi2) {
		this.uwi2 = uwi2;
	}
	public String getUwi3() {
		return uwi3;
	}
	public void setUwi3(String uwi3) {
		this.uwi3 = uwi3;
	}
	public String getUwi4() {
		return uwi4;
	}
	public void setUwi4(String uwi4) {
		this.uwi4 = uwi4;
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
	public Integer getNumOfDigitsCorrect() {
		return numOfDigitsCorrect;
	}
	public void setNumOfDigitsCorrect(Integer numOfDigitsCorrect) {
		this.numOfDigitsCorrect = numOfDigitsCorrect;
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
