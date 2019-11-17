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
        name = "DetailVisualBindingSpan.fetchPendingDetailVisualBindingRecordTotalCount",
        query = "SELECT count(d.childId) FROM DetailVisualBindingSpan d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade"
    ),
    @NamedQuery(
        name = "DetailVisualBindingSpan.fetchDetailVisualBindingListByTimestamp",
        query = "SELECT d FROM DetailVisualBindingSpan d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade order by d.createTime"
    ),
    @NamedQuery(
        name = "DetailVisualBindingSpan.fetchDistributedVisualBindingSpanDetailTotalCount",
        query = "SELECT count(d.uid) FROM DetailVisualBindingSpan d where d.createTime > :createTime"
    )
})

@Entity
@Table(name = "detail_visual_binding_span")
public class DetailVisualBindingSpan {
	@Id
	private String uid;
	@Column(name = "child_id")
	private String childId;
	private String grade;
	private String experimenter;
	@Column(name = "stimuli_location_input")
	private String stimuliLocationInput;
	@Column(name = "stimuli_polygon_input")
	private String stimuliPolygonInput;
	@Column(name = "user_location_input")
	private String userLocationInput;
	@Column(name = "user_polygon_input")
	private String userPolygonInput;
	@Column(name = "extra_user_polygon_input")
	private String extraUserPolygonInput;
	@Column(name = "extra_user_location_input")
	private String extraUserLocationInput;
	
	@Column(length = 10)
	private String sli1;
	@Column(length = 10)
	private String sli2;
	@Column(length = 10)
	private String sli3;
	@Column(length = 10)
	private String sli4;
	
	@Column(length = 10)
	private String spi1;
	@Column(length = 10)
	private String spi2;
	@Column(length = 10)
	private String spi3;
	@Column(length = 10)
	private String spi4;
	
	@Column(length = 10)
	private String uli1;
	@Column(length = 10)
	private String uli2;
	@Column(length = 10)
	private String uli3;
	@Column(length = 10)
	private String uli4;
	
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
	public String getStimuliLocationInput() {
		return stimuliLocationInput;
	}
	public void setStimuliLocationInput(String stimuliLocationInput) {
		this.stimuliLocationInput = stimuliLocationInput;
	}
	public String getStimuliPolygonInput() {
		return stimuliPolygonInput;
	}
	public void setStimuliPolygonInput(String stimuliPolygonInput) {
		this.stimuliPolygonInput = stimuliPolygonInput;
	}
	public String getUserLocationInput() {
		return userLocationInput;
	}
	public void setUserLocationInput(String userLocationInput) {
		this.userLocationInput = userLocationInput;
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
	public String getExtraUserLocationInput() {
		return extraUserLocationInput;
	}
	public void setExtraUserLocationInput(String extraUserLocationInput) {
		this.extraUserLocationInput = extraUserLocationInput;
	}
	public String getSli1() {
		return sli1;
	}
	public void setSli1(String sli1) {
		this.sli1 = sli1;
	}
	public String getSli2() {
		return sli2;
	}
	public void setSli2(String sli2) {
		this.sli2 = sli2;
	}
	public String getSli3() {
		return sli3;
	}
	public void setSli3(String sli3) {
		this.sli3 = sli3;
	}
	public String getSli4() {
		return sli4;
	}
	public void setSli4(String sli4) {
		this.sli4 = sli4;
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
	public String getUli1() {
		return uli1;
	}
	public void setUli1(String uli1) {
		this.uli1 = uli1;
	}
	public String getUli2() {
		return uli2;
	}
	public void setUli2(String uli2) {
		this.uli2 = uli2;
	}
	public String getUli3() {
		return uli3;
	}
	public void setUli3(String uli3) {
		this.uli3 = uli3;
	}
	public String getUli4() {
		return uli4;
	}
	public void setUli4(String uli4) {
		this.uli4 = uli4;
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
