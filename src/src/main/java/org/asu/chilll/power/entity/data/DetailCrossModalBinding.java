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
		name = "DetailCrossModalBinding.fetchPendingDetailCrossModalBindingRecordTotalCount",
        query = "SELECT count(d.childId) FROM DetailCrossModalBinding d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade"
    ),
    @NamedQuery(
        name = "DetailCrossModalBinding.fetchDetailCrossModalBindingListByTimestamp",
        query = "SELECT d FROM DetailCrossModalBinding d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade order by d.createTime"
    ),
    @NamedQuery(
        name = "DetailCrossModalBinding.fetchDistributedCrossModalBindingDetailTotalCount",
        query = "SELECT count(d.uid) FROM DetailCrossModalBinding d where d.createTime > :createTime"
    )
})

@Entity
@Table(name = "detail_cross_modal_binding")
public class DetailCrossModalBinding {
	@Id
	private String uid;
	@Column(name = "child_id")
	private String childId;
	private String grade;
	private String experimenter;
	@Column(name = "stimuli_word_input")
	private String stimuliWordInput;
	@Column(name = "stimuli_polygon_input")
	private String stimuliPolygonInput;
	@Column(name = "user_word_input")
	private String userWordInput;
	@Column(name = "user_polygon_input")
	private String userPolygonInput;
	@Column(name = "extra_user_polygon_input")
	private String extraUserPolygonInput;
	@Column(name = "extra_user_word_input")
	private String extraUserWordInput;
	
	@Column(length = 10)
	private String swi1;
	@Column(length = 10)
	private String swi2;
	@Column(length = 10)
	private String swi3;
	@Column(length = 10)
	private String swi4;
	
	@Column(length = 10)
	private String spi1;
	@Column(length = 10)
	private String spi2;
	@Column(length = 10)
	private String spi3;
	@Column(length = 10)
	private String spi4;
	
	@Column(length = 10)
	private String uwi1;
	@Column(length = 10)
	private String uwi2;
	@Column(length = 10)
	private String uwi3;
	@Column(length = 10)
	private String uwi4;
	
	@Column(length = 10)
	private String upi1;
	@Column(length = 10)
	private String upi2;
	@Column(length = 10)
	private String upi3;
	@Column(length = 10)
	private String upi4;
	
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
	public String getStimuliWordInput() {
		return stimuliWordInput;
	}
	public void setStimuliWordInput(String stimuliWordInput) {
		this.stimuliWordInput = stimuliWordInput;
	}
	public String getStimuliPolygonInput() {
		return stimuliPolygonInput;
	}
	public void setStimuliPolygonInput(String stimuliPolygonInput) {
		this.stimuliPolygonInput = stimuliPolygonInput;
	}
	public String getUserWordInput() {
		return userWordInput;
	}
	public void setUserWordInput(String userWordInput) {
		this.userWordInput = userWordInput;
	}
	public String getUserPolygonInput() {
		return userPolygonInput;
	}
	public void setUserPolygonInput(String userPolygonInput) {
		this.userPolygonInput = userPolygonInput;
	}
	public String getExtraUserPolygonInput() {
		return extraUserPolygonInput;
	}
	public void setExtraUserPolygonInput(String extraUserPolygonInput) {
		this.extraUserPolygonInput = extraUserPolygonInput;
	}
	public String getExtraUserWordInput() {
		return extraUserWordInput;
	}
	public void setExtraUserWordInput(String extraUserWordInput) {
		this.extraUserWordInput = extraUserWordInput;
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
	public String getSpi1() {
		return spi1;
	}
	public void setSpi1(String spi1) {
		this.spi1 = spi1;
	}
	public String getSpi2() {
		return spi2;
	}
	public void setSpi2(String spi2) {
		this.spi2 = spi2;
	}
	public String getSpi3() {
		return spi3;
	}
	public void setSpi3(String spi3) {
		this.spi3 = spi3;
	}
	public String getSpi4() {
		return spi4;
	}
	public void setSpi4(String spi4) {
		this.spi4 = spi4;
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
	public String getUpi1() {
		return upi1;
	}
	public void setUpi1(String upi1) {
		this.upi1 = upi1;
	}
	public String getUpi2() {
		return upi2;
	}
	public void setUpi2(String upi2) {
		this.upi2 = upi2;
	}
	public String getUpi3() {
		return upi3;
	}
	public void setUpi3(String upi3) {
		this.upi3 = upi3;
	}
	public String getUpi4() {
		return upi4;
	}
	public void setUpi4(String upi4) {
		this.upi4 = upi4;
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
