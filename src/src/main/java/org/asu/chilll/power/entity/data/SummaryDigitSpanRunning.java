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
        name = "SummaryDigitSpanRunning.fetchDistributedListDigitSpanRunningSummaryByLimit",
        query = "SELECT d FROM SummaryDigitSpanRunning d where d.lastUpdateTime > :lastUpdateTime order by d.lastUpdateTime"
    )
})

@Entity
@Table(name = "summary_digit_span_running")
public class SummaryDigitSpanRunning {
	@Id
	@Column(name = "child_id_grade")
	private String childId_grade;
	private String experimenter;
	@Column(length = 10)
	private String dsrp1;
	@Column(length = 10)
	private String dsrp2;
	@Column(length = 10)
	private String dsrp3;
	@Column(length = 10)
	private String dsr71;
	@Column(length = 10)
	private String dsr72;
	@Column(length = 10)
	private String dsr73;
	@Column(length = 10)
	private String dsr74;
	@Column(length = 10)
	private String dsr81;
	@Column(length = 10)
	private String dsr82;
	@Column(length = 10)
	private String dsr83;
	@Column(length = 10)
	private String dsr84;
	@Column(length = 10)
	private String dsr91;
	@Column(length = 10)
	private String dsr92;
	@Column(length = 10)
	private String dsr93;
	@Column(length = 10)
	private String dsr94;
	@Column(length = 10)
	private String dsr101;
	@Column(length = 10)
	private String dsr102;
	@Column(length = 10)
	private String dsr103;
	@Column(length = 10)
	private String dsr104;
	
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
	public String getDsrp1() {
		return dsrp1;
	}
	public void setDsrp1(String dsrp1) {
		this.dsrp1 = dsrp1;
	}
	public String getDsrp2() {
		return dsrp2;
	}
	public void setDsrp2(String dsrp2) {
		this.dsrp2 = dsrp2;
	}
	public String getDsrp3() {
		return dsrp3;
	}
	public void setDsrp3(String dsrp3) {
		this.dsrp3 = dsrp3;
	}
	public String getDsr71() {
		return dsr71;
	}
	public void setDsr71(String dsr71) {
		this.dsr71 = dsr71;
	}
	public String getDsr72() {
		return dsr72;
	}
	public void setDsr72(String dsr72) {
		this.dsr72 = dsr72;
	}
	public String getDsr73() {
		return dsr73;
	}
	public void setDsr73(String dsr73) {
		this.dsr73 = dsr73;
	}
	public String getDsr74() {
		return dsr74;
	}
	public void setDsr74(String dsr74) {
		this.dsr74 = dsr74;
	}
	public String getDsr81() {
		return dsr81;
	}
	public void setDsr81(String dsr81) {
		this.dsr81 = dsr81;
	}
	public String getDsr82() {
		return dsr82;
	}
	public void setDsr82(String dsr82) {
		this.dsr82 = dsr82;
	}
	public String getDsr83() {
		return dsr83;
	}
	public void setDsr83(String dsr83) {
		this.dsr83 = dsr83;
	}
	public String getDsr84() {
		return dsr84;
	}
	public void setDsr84(String dsr84) {
		this.dsr84 = dsr84;
	}
	public String getDsr91() {
		return dsr91;
	}
	public void setDsr91(String dsr91) {
		this.dsr91 = dsr91;
	}
	public String getDsr92() {
		return dsr92;
	}
	public void setDsr92(String dsr92) {
		this.dsr92 = dsr92;
	}
	public String getDsr93() {
		return dsr93;
	}
	public void setDsr93(String dsr93) {
		this.dsr93 = dsr93;
	}
	public String getDsr94() {
		return dsr94;
	}
	public void setDsr94(String dsr94) {
		this.dsr94 = dsr94;
	}
	public String getDsr101() {
		return dsr101;
	}
	public void setDsr101(String dsr101) {
		this.dsr101 = dsr101;
	}
	public String getDsr102() {
		return dsr102;
	}
	public void setDsr102(String dsr102) {
		this.dsr102 = dsr102;
	}
	public String getDsr103() {
		return dsr103;
	}
	public void setDsr103(String dsr103) {
		this.dsr103 = dsr103;
	}
	public String getDsr104() {
		return dsr104;
	}
	public void setDsr104(String dsr104) {
		this.dsr104 = dsr104;
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
