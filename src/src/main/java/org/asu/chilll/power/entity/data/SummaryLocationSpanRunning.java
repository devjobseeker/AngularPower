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
        name = "SummaryLocationSpanRunning.fetchDistributedListLocationSpanRunningSummaryByLimit",
        query = "SELECT d FROM SummaryLocationSpanRunning d where d.lastUpdateTime > :lastUpdateTime order by d.lastUpdateTime"
    )
})

@Entity
@Table(name = "summary_location_span_running")
public class SummaryLocationSpanRunning {
	@Id
	@Column(name = "child_id_grade")
	private String childId_grade;
	private String experimenter;
	
	@Column(length = 10)
	private String lsrp1;
	@Column(length = 10)
	private String lsrp2;
	@Column(length = 10)
	private String lsrp3;
	@Column(length = 10)
	private String lsr51;
	@Column(length = 10)
	private String lsr52;
	@Column(length = 10)
	private String lsr53;
	@Column(length = 10)
	private String lsr54;
	@Column(length = 10)
	private String lsr61;
	@Column(length = 10)
	private String lsr62;
	@Column(length = 10)
	private String lsr63;
	@Column(length = 10)
	private String lsr64;
	@Column(length = 10)
	private String lsr71;
	@Column(length = 10)
	private String lsr72;
	@Column(length = 10)
	private String lsr73;
	@Column(length = 10)
	private String lsr74;
	@Column(length = 10)
	private String lsr81;
	@Column(length = 10)
	private String lsr82;
	@Column(length = 10)
	private String lsr83;
	@Column(length = 10)
	private String lsr84;
	
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
	public String getLsr51() {
		return lsr51;
	}
	public void setLsr51(String lsr51) {
		this.lsr51 = lsr51;
	}
	public String getLsr52() {
		return lsr52;
	}
	public void setLsr52(String lsr52) {
		this.lsr52 = lsr52;
	}
	public String getLsr53() {
		return lsr53;
	}
	public void setLsr53(String lsr53) {
		this.lsr53 = lsr53;
	}
	public String getLsr54() {
		return lsr54;
	}
	public void setLsr54(String lsr54) {
		this.lsr54 = lsr54;
	}
	public String getLsr61() {
		return lsr61;
	}
	public void setLsr61(String lsr61) {
		this.lsr61 = lsr61;
	}
	public String getLsr62() {
		return lsr62;
	}
	public void setLsr62(String lsr62) {
		this.lsr62 = lsr62;
	}
	public String getLsr63() {
		return lsr63;
	}
	public void setLsr63(String lsr63) {
		this.lsr63 = lsr63;
	}
	public String getLsr64() {
		return lsr64;
	}
	public void setLsr64(String lsr64) {
		this.lsr64 = lsr64;
	}
	public String getLsr71() {
		return lsr71;
	}
	public void setLsr71(String lsr71) {
		this.lsr71 = lsr71;
	}
	public String getLsr72() {
		return lsr72;
	}
	public void setLsr72(String lsr72) {
		this.lsr72 = lsr72;
	}
	public String getLsr73() {
		return lsr73;
	}
	public void setLsr73(String lsr73) {
		this.lsr73 = lsr73;
	}
	public String getLsr74() {
		return lsr74;
	}
	public void setLsr74(String lsr74) {
		this.lsr74 = lsr74;
	}
	public String getLsr81() {
		return lsr81;
	}
	public void setLsr81(String lsr81) {
		this.lsr81 = lsr81;
	}
	public String getLsr82() {
		return lsr82;
	}
	public void setLsr82(String lsr82) {
		this.lsr82 = lsr82;
	}
	public String getLsr83() {
		return lsr83;
	}
	public void setLsr83(String lsr83) {
		this.lsr83 = lsr83;
	}
	public String getLsr84() {
		return lsr84;
	}
	public void setLsr84(String lsr84) {
		this.lsr84 = lsr84;
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
	public String getLsrp1() {
		return lsrp1;
	}
	public void setLsrp1(String lsrp1) {
		this.lsrp1 = lsrp1;
	}
	public String getLsrp2() {
		return lsrp2;
	}
	public void setLsrp2(String lsrp2) {
		this.lsrp2 = lsrp2;
	}
	public String getLsrp3() {
		return lsrp3;
	}
	public void setLsrp3(String lsrp3) {
		this.lsrp3 = lsrp3;
	}
}