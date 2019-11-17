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
        name = "SummaryVisualBindingSpan.fetchDistributedListVisualBindingSpanSummaryByLimit",
        query = "SELECT d FROM SummaryVisualBindingSpan d where d.lastUpdateTime > :lastUpdateTime order by d.lastUpdateTime"
    )
})

@Entity
@Table(name = "summary_visual_binding_span")
public class SummaryVisualBindingSpan {
	@Id
	@Column(name = "child_id_grade")
	private String childId_grade;
	private String experimenter;
	
	@Column(length = 10)
	private String vbspt1;
	@Column(length = 10)
	private String vbspt2;
	@Column(length = 10)
	private String vbspt3;
	@Column(length = 10)
	private String vbst21;
	@Column(length = 10)
	private String vbst22;
	@Column(length = 10)
	private String vbst23;
	@Column(length = 10)
	private String vbst24;
	@Column(length = 10)
	private String vbst31;
	@Column(length = 10)
	private String vbst32;
	@Column(length = 10)
	private String vbst33;
	@Column(length = 10)
	private String vbst34;
	@Column(length = 10)
	private String vbst41;
	@Column(length = 10)
	private String vbst42;
	@Column(length = 10)
	private String vbst43;
	@Column(length = 10)
	private String vbst44;
	
	@Column(length = 10)
	private String vbspi1;
	@Column(length = 10)
	private String vbspi2;
	@Column(length = 10)
	private String vbspi3;
	@Column(length = 10)
	private String vbsi21;
	@Column(length = 10)
	private String vbsi22;
	@Column(length = 10)
	private String vbsi23;
	@Column(length = 10)
	private String vbsi24;
	@Column(length = 10)
	private String vbsi31;
	@Column(length = 10)
	private String vbsi32;
	@Column(length = 10)
	private String vbsi33;
	@Column(length = 10)
	private String vbsi34;
	@Column(length = 10)
	private String vbsi41;
	@Column(length = 10)
	private String vbsi42;
	@Column(length = 10)
	private String vbsi43;
	@Column(length = 10)
	private String vbsi44;
	
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
	public String getVbst21() {
		return vbst21;
	}
	public void setVbst21(String vbst21) {
		this.vbst21 = vbst21;
	}
	public String getVbst22() {
		return vbst22;
	}
	public void setVbst22(String vbst22) {
		this.vbst22 = vbst22;
	}
	public String getVbst23() {
		return vbst23;
	}
	public void setVbst23(String vbst23) {
		this.vbst23 = vbst23;
	}
	public String getVbst24() {
		return vbst24;
	}
	public void setVbst24(String vbst24) {
		this.vbst24 = vbst24;
	}
	public String getVbst31() {
		return vbst31;
	}
	public void setVbst31(String vbst31) {
		this.vbst31 = vbst31;
	}
	public String getVbst32() {
		return vbst32;
	}
	public void setVbst32(String vbst32) {
		this.vbst32 = vbst32;
	}
	public String getVbst33() {
		return vbst33;
	}
	public void setVbst33(String vbst33) {
		this.vbst33 = vbst33;
	}
	public String getVbst34() {
		return vbst34;
	}
	public void setVbst34(String vbst34) {
		this.vbst34 = vbst34;
	}
	public String getVbst41() {
		return vbst41;
	}
	public void setVbst41(String vbst41) {
		this.vbst41 = vbst41;
	}
	public String getVbst42() {
		return vbst42;
	}
	public void setVbst42(String vbst42) {
		this.vbst42 = vbst42;
	}
	public String getVbst43() {
		return vbst43;
	}
	public void setVbst43(String vbst43) {
		this.vbst43 = vbst43;
	}
	public String getVbst44() {
		return vbst44;
	}
	public void setVbst44(String vbst44) {
		this.vbst44 = vbst44;
	}
	public String getVbsi21() {
		return vbsi21;
	}
	public void setVbsi21(String vbsi21) {
		this.vbsi21 = vbsi21;
	}
	public String getVbsi22() {
		return vbsi22;
	}
	public void setVbsi22(String vbsi22) {
		this.vbsi22 = vbsi22;
	}
	public String getVbsi23() {
		return vbsi23;
	}
	public void setVbsi23(String vbsi23) {
		this.vbsi23 = vbsi23;
	}
	public String getVbsi24() {
		return vbsi24;
	}
	public void setVbsi24(String vbsi24) {
		this.vbsi24 = vbsi24;
	}
	public String getVbsi31() {
		return vbsi31;
	}
	public void setVbsi31(String vbsi31) {
		this.vbsi31 = vbsi31;
	}
	public String getVbsi32() {
		return vbsi32;
	}
	public void setVbsi32(String vbsi32) {
		this.vbsi32 = vbsi32;
	}
	public String getVbsi33() {
		return vbsi33;
	}
	public void setVbsi33(String vbsi33) {
		this.vbsi33 = vbsi33;
	}
	public String getVbsi34() {
		return vbsi34;
	}
	public void setVbsi34(String vbsi34) {
		this.vbsi34 = vbsi34;
	}
	public String getVbsi41() {
		return vbsi41;
	}
	public void setVbsi41(String vbsi41) {
		this.vbsi41 = vbsi41;
	}
	public String getVbsi42() {
		return vbsi42;
	}
	public void setVbsi42(String vbsi42) {
		this.vbsi42 = vbsi42;
	}
	public String getVbsi43() {
		return vbsi43;
	}
	public void setVbsi43(String vbsi43) {
		this.vbsi43 = vbsi43;
	}
	public String getVbsi44() {
		return vbsi44;
	}
	public void setVbsi44(String vbsi44) {
		this.vbsi44 = vbsi44;
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
	public String getVbspt1() {
		return vbspt1;
	}
	public void setVbspt1(String vbspt1) {
		this.vbspt1 = vbspt1;
	}
	public String getVbspt2() {
		return vbspt2;
	}
	public void setVbspt2(String vbspt2) {
		this.vbspt2 = vbspt2;
	}
	public String getVbspt3() {
		return vbspt3;
	}
	public void setVbspt3(String vbspt3) {
		this.vbspt3 = vbspt3;
	}
	public String getVbspi1() {
		return vbspi1;
	}
	public void setVbspi1(String vbspi1) {
		this.vbspi1 = vbspi1;
	}
	public String getVbspi2() {
		return vbspi2;
	}
	public void setVbspi2(String vbspi2) {
		this.vbspi2 = vbspi2;
	}
	public String getVbspi3() {
		return vbspi3;
	}
	public void setVbspi3(String vbspi3) {
		this.vbspi3 = vbspi3;
	}
}
