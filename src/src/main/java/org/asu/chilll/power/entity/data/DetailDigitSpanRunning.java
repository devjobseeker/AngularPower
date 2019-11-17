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
        name = "DetailDigitSpanRunning.fetchPendingDetailDigitSpanRunningRecordTotalCount",
        query = "SELECT count(d.childId) FROM DetailDigitSpanRunning d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade"
    ),
    @NamedQuery(
        name = "DetailDigitSpanRunning.fetchDetailDigitSpanRunningListByTimestamp",
        query = "SELECT d FROM DetailDigitSpanRunning d where d.createTime > :createTime and d.childId = :childId and d.grade = :grade order by d.createTime"
    ),
    @NamedQuery(
        name = "DetailDigitSpanRunning.fetchDistributedDigitSpanRunningDetailTotalCount",
        query = "SELECT count(d.uid) FROM DetailDigitSpanRunning d where d.createTime > :createTime"
    )
})

@Entity
@Table(name = "detail_digit_span_running")
public class DetailDigitSpanRunning {
	@Id
	private String uid;
	@Column(name = "child_id")
	private String childId;
	private String grade;
	private String experimenter;
	@Column(name = "stimuli_input")
	private String stimuliInput;
	@Column(name = "user_input")
	private String userInput;
	@Column(name = "extra_user_input")
	private String extraUserInput;
	
	@Column(length = 10)
	private String si1;
	@Column(length = 10)
	private String si2;
	@Column(length = 10)
	private String si3;
	@Column(length = 10)
	private String si4;
	@Column(length = 10)
	private String si5;
	@Column(length = 10)
	private String si6;
	@Column(length = 10)
	private String si7;
	@Column(length = 10)
	private String si8;
	@Column(length = 10)
	private String si9;
	@Column(length = 10)
	private String si10;

	@Column(length = 10)
	private String ui1;
	@Column(length = 10)
	private String ui2;
	@Column(length = 10)
	private String ui3;
	@Column(length = 10)
	private String ui4;
	@Column(length = 10)
	private String ui5;
	@Column(length = 10)
	private String ui6;
	@Column(length = 10)
	private String ui7;
	@Column(length = 10)
	private String ui8;
	@Column(length = 10)
	private String ui9;
	@Column(length = 10)
	private String ui10;
	
	private Integer score;
	@Column(name = "num_of_digits_correct")
	private Integer numOfDigitsCorrect;	//how many digits are correct
	@Column(name = "num_of_digits_correct_from_end")
	private Integer numOfDigitsCorrectFromEnd;	// count from end, how many digits are correct, if one digit is wrong, stop counting
	private String comment;
	@Column(name = "trial_no")
	private Integer trialNo;
	@Column(name = "trial_type")
	private String trialType;	//practice
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
	public String getStimuliInput() {
		return stimuliInput;
	}
	public void setStimuliInput(String stimuliInput) {
		this.stimuliInput = stimuliInput;
	}
	public String getUserInput() {
		return userInput;
	}
	public void setUserInput(String userInput) {
		this.userInput = userInput;
	}
	public String getExtraUserInput() {
		return extraUserInput;
	}
	public void setExtraUserInput(String extraUserInput) {
		this.extraUserInput = extraUserInput;
	}
	public String getSi1() {
		return si1;
	}
	public void setSi1(String si1) {
		this.si1 = si1;
	}
	public String getSi2() {
		return si2;
	}
	public void setSi2(String si2) {
		this.si2 = si2;
	}
	public String getSi3() {
		return si3;
	}
	public void setSi3(String si3) {
		this.si3 = si3;
	}
	public String getSi4() {
		return si4;
	}
	public void setSi4(String si4) {
		this.si4 = si4;
	}
	public String getSi5() {
		return si5;
	}
	public void setSi5(String si5) {
		this.si5 = si5;
	}
	public String getSi6() {
		return si6;
	}
	public void setSi6(String si6) {
		this.si6 = si6;
	}
	public String getSi7() {
		return si7;
	}
	public void setSi7(String si7) {
		this.si7 = si7;
	}
	public String getSi8() {
		return si8;
	}
	public void setSi8(String si8) {
		this.si8 = si8;
	}
	public String getSi9() {
		return si9;
	}
	public void setSi9(String si9) {
		this.si9 = si9;
	}
	public String getSi10() {
		return si10;
	}
	public void setSi10(String si10) {
		this.si10 = si10;
	}
	public String getUi1() {
		return ui1;
	}
	public void setUi1(String ui1) {
		this.ui1 = ui1;
	}
	public String getUi2() {
		return ui2;
	}
	public void setUi2(String ui2) {
		this.ui2 = ui2;
	}
	public String getUi3() {
		return ui3;
	}
	public void setUi3(String ui3) {
		this.ui3 = ui3;
	}
	public String getUi4() {
		return ui4;
	}
	public void setUi4(String ui4) {
		this.ui4 = ui4;
	}
	public String getUi5() {
		return ui5;
	}
	public void setUi5(String ui5) {
		this.ui5 = ui5;
	}
	public String getUi6() {
		return ui6;
	}
	public void setUi6(String ui6) {
		this.ui6 = ui6;
	}
	public String getUi7() {
		return ui7;
	}
	public void setUi7(String ui7) {
		this.ui7 = ui7;
	}
	public String getUi8() {
		return ui8;
	}
	public void setUi8(String ui8) {
		this.ui8 = ui8;
	}
	public String getUi9() {
		return ui9;
	}
	public void setUi9(String ui9) {
		this.ui9 = ui9;
	}
	public String getUi10() {
		return ui10;
	}
	public void setUi10(String ui10) {
		this.ui10 = ui10;
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
	public Integer getNumOfDigitsCorrectFromEnd() {
		return numOfDigitsCorrectFromEnd;
	}
	public void setNumOfDigitsCorrectFromEnd(Integer numOfDigitsCorrectFromEnd) {
		this.numOfDigitsCorrectFromEnd = numOfDigitsCorrectFromEnd;
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
