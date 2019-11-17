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
        name = "SummaryVisualSpanRunning.fetchDistributedListVisualSpanRunningSummaryByLimit",
        query = "SELECT d FROM SummaryVisualSpanRunning d where d.lastUpdateTime > :lastUpdateTime order by d.lastUpdateTime"
    )
})

@Entity
@Table(name = "summary_visual_span_running")
public class SummaryVisualSpanRunning {
	@Id
	@Column(name = "child_id_grade")
	private String childId_grade;
	private String experimenter;
	
	@Column(length = 10)
	private String vsrp1;
	@Column(length = 10)
	private String vsrp2;
	@Column(length = 10)
	private String vsrp3;
	@Column(length = 10)
	private String vsr31;
	@Column(length = 10)
	private String vsr32;
	@Column(length = 10)
	private String vsr33;
	@Column(length = 10)
	private String vsr34;
	@Column(length = 10)
	private String vsr41;
	@Column(length = 10)
	private String vsr42;
	@Column(length = 10)
	private String vsr43;
	@Column(length = 10)
	private String vsr44;
	@Column(length = 10)
	private String vsr51;
	@Column(length = 10)
	private String vsr52;
	@Column(length = 10)
	private String vsr53;
	@Column(length = 10)
	private String vsr54;
	@Column(length = 10)
	private String vsr61;
	@Column(length = 10)
	private String vsr62;
	@Column(length = 10)
	private String vsr63;
	@Column(length = 10)
	private String vsr64;
	
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "create_time")
	private Long createTime;
	@Column(name = "last_update_date")
	private Date lastUpdateDate;
	@Column(name = "last_update_time")
	private Long lastUpdateTime;
	public String getChildId_grade() {
		return childId_grade;
	}
	public void setChildId_grade(String childId_grade) {
		this.childId_grade = childId_grade;
	}
	public String getExperimenter() {
		return experimenter;
	}
	public void setExperimenter(String experimenter) {
		this.experimenter = experimenter;
	}
	public String getVsr31() {
		return vsr31;
	}
	public void setVsr31(String vsr31) {
		this.vsr31 = vsr31;
	}
	public String getVsr32() {
		return vsr32;
	}
	public void setVsr32(String vsr32) {
		this.vsr32 = vsr32;
	}
	public String getVsr33() {
		return vsr33;
	}
	public void setVsr33(String vsr33) {
		this.vsr33 = vsr33;
	}
	public String getVsr34() {
		return vsr34;
	}
	public void setVsr34(String vsr34) {
		this.vsr34 = vsr34;
	}
	public String getVsr41() {
		return vsr41;
	}
	public void setVsr41(String vsr41) {
		this.vsr41 = vsr41;
	}
	public String getVsr42() {
		return vsr42;
	}
	public void setVsr42(String vsr42) {
		this.vsr42 = vsr42;
	}
	public String getVsr43() {
		return vsr43;
	}
	public void setVsr43(String vsr43) {
		this.vsr43 = vsr43;
	}
	public String getVsr44() {
		return vsr44;
	}
	public void setVsr44(String vsr44) {
		this.vsr44 = vsr44;
	}
	public String getVsr51() {
		return vsr51;
	}
	public void setVsr51(String vsr51) {
		this.vsr51 = vsr51;
	}
	public String getVsr52() {
		return vsr52;
	}
	public void setVsr52(String vsr52) {
		this.vsr52 = vsr52;
	}
	public String getVsr53() {
		return vsr53;
	}
	public void setVsr53(String vsr53) {
		this.vsr53 = vsr53;
	}
	public String getVsr54() {
		return vsr54;
	}
	public void setVsr54(String vsr54) {
		this.vsr54 = vsr54;
	}
	public String getVsr61() {
		return vsr61;
	}
	public void setVsr61(String vsr61) {
		this.vsr61 = vsr61;
	}
	public String getVsr62() {
		return vsr62;
	}
	public void setVsr62(String vsr62) {
		this.vsr62 = vsr62;
	}
	public String getVsr63() {
		return vsr63;
	}
	public void setVsr63(String vsr63) {
		this.vsr63 = vsr63;
	}
	public String getVsr64() {
		return vsr64;
	}
	public void setVsr64(String vsr64) {
		this.vsr64 = vsr64;
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
	public String getVsrp1() {
		return vsrp1;
	}
	public void setVsrp1(String vsrp1) {
		this.vsrp1 = vsrp1;
	}
	public String getVsrp2() {
		return vsrp2;
	}
	public void setVsrp2(String vsrp2) {
		this.vsrp2 = vsrp2;
	}
	public String getVsrp3() {
		return vsrp3;
	}
	public void setVsrp3(String vsrp3) {
		this.vsrp3 = vsrp3;
	}
}
