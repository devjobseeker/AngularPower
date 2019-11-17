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
        name = "SummaryPhonologicalBinding.fetchDistributedListPhonologicalBindingSummaryByLimit",
        query = "SELECT d FROM SummaryPhonologicalBinding d where d.lastUpdateTime > :lastUpdateTime order by d.lastUpdateTime"
    )
})

@Entity
@Table(name = "summary_phonological_binding")
public class SummaryPhonologicalBinding {
	@Id
	@Column(name = "child_id_grade")
	private String childId_grade;
	private String experimenter;
	
	@Column(length = 10)
	private String pbspt1;
	@Column(length = 10)
	private String pbspt2;
	@Column(length = 10)
	private String pbspt3;
	@Column(length = 10)
	private String pbst21;
	@Column(length = 10)
	private String pbst22;
	@Column(length = 10)
	private String pbst23;
	@Column(length = 10)
	private String pbst24;
	@Column(length = 10)
	private String pbst31;
	@Column(length = 10)
	private String pbst32;
	@Column(length = 10)
	private String pbst33;
	@Column(length = 10)
	private String pbst34;
	@Column(length = 10)
	private String pbst41;
	@Column(length = 10)
	private String pbst42;
	@Column(length = 10)
	private String pbst43;
	@Column(length = 10)
	private String pbst44;
	
	@Column(length = 10)
	private String pbspi1;
	@Column(length = 10)
	private String pbspi2;
	@Column(length = 10)
	private String pbspi3;
	@Column(length = 10)
	private String pbsi21;
	@Column(length = 10)
	private String pbsi22;
	@Column(length = 10)
	private String pbsi23;
	@Column(length = 10)
	private String pbsi24;
	@Column(length = 10)
	private String pbsi31;
	@Column(length = 10)
	private String pbsi32;
	@Column(length = 10)
	private String pbsi33;
	@Column(length = 10)
	private String pbsi34;
	@Column(length = 10)
	private String pbsi41;
	@Column(length = 10)
	private String pbsi42;
	@Column(length = 10)
	private String pbsi43;
	@Column(length = 10)
	private String pbsi44;
	
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
	public String getPbspt1() {
		return pbspt1;
	}
	public void setPbspt1(String pbspt1) {
		this.pbspt1 = pbspt1;
	}
	public String getPbspt2() {
		return pbspt2;
	}
	public void setPbspt2(String pbspt2) {
		this.pbspt2 = pbspt2;
	}
	public String getPbspt3() {
		return pbspt3;
	}
	public void setPbspt3(String pbspt3) {
		this.pbspt3 = pbspt3;
	}
	public String getPbst21() {
		return pbst21;
	}
	public void setPbst21(String pbst21) {
		this.pbst21 = pbst21;
	}
	public String getPbst22() {
		return pbst22;
	}
	public void setPbst22(String pbst22) {
		this.pbst22 = pbst22;
	}
	public String getPbst23() {
		return pbst23;
	}
	public void setPbst23(String pbst23) {
		this.pbst23 = pbst23;
	}
	public String getPbst24() {
		return pbst24;
	}
	public void setPbst24(String pbst24) {
		this.pbst24 = pbst24;
	}
	public String getPbst31() {
		return pbst31;
	}
	public void setPbst31(String pbst31) {
		this.pbst31 = pbst31;
	}
	public String getPbst32() {
		return pbst32;
	}
	public void setPbst32(String pbst32) {
		this.pbst32 = pbst32;
	}
	public String getPbst33() {
		return pbst33;
	}
	public void setPbst33(String pbst33) {
		this.pbst33 = pbst33;
	}
	public String getPbst34() {
		return pbst34;
	}
	public void setPbst34(String pbst34) {
		this.pbst34 = pbst34;
	}
	public String getPbst41() {
		return pbst41;
	}
	public void setPbst41(String pbst41) {
		this.pbst41 = pbst41;
	}
	public String getPbst42() {
		return pbst42;
	}
	public void setPbst42(String pbst42) {
		this.pbst42 = pbst42;
	}
	public String getPbst43() {
		return pbst43;
	}
	public void setPbst43(String pbst43) {
		this.pbst43 = pbst43;
	}
	public String getPbst44() {
		return pbst44;
	}
	public void setPbst44(String pbst44) {
		this.pbst44 = pbst44;
	}
	public String getPbspi1() {
		return pbspi1;
	}
	public void setPbspi1(String pbspi1) {
		this.pbspi1 = pbspi1;
	}
	public String getPbspi2() {
		return pbspi2;
	}
	public void setPbspi2(String pbspi2) {
		this.pbspi2 = pbspi2;
	}
	public String getPbspi3() {
		return pbspi3;
	}
	public void setPbspi3(String pbspi3) {
		this.pbspi3 = pbspi3;
	}
	public String getPbsi21() {
		return pbsi21;
	}
	public void setPbsi21(String pbsi21) {
		this.pbsi21 = pbsi21;
	}
	public String getPbsi22() {
		return pbsi22;
	}
	public void setPbsi22(String pbsi22) {
		this.pbsi22 = pbsi22;
	}
	public String getPbsi23() {
		return pbsi23;
	}
	public void setPbsi23(String pbsi23) {
		this.pbsi23 = pbsi23;
	}
	public String getPbsi24() {
		return pbsi24;
	}
	public void setPbsi24(String pbsi24) {
		this.pbsi24 = pbsi24;
	}
	public String getPbsi31() {
		return pbsi31;
	}
	public void setPbsi31(String pbsi31) {
		this.pbsi31 = pbsi31;
	}
	public String getPbsi32() {
		return pbsi32;
	}
	public void setPbsi32(String pbsi32) {
		this.pbsi32 = pbsi32;
	}
	public String getPbsi33() {
		return pbsi33;
	}
	public void setPbsi33(String pbsi33) {
		this.pbsi33 = pbsi33;
	}
	public String getPbsi34() {
		return pbsi34;
	}
	public void setPbsi34(String pbsi34) {
		this.pbsi34 = pbsi34;
	}
	public String getPbsi41() {
		return pbsi41;
	}
	public void setPbsi41(String pbsi41) {
		this.pbsi41 = pbsi41;
	}
	public String getPbsi42() {
		return pbsi42;
	}
	public void setPbsi42(String pbsi42) {
		this.pbsi42 = pbsi42;
	}
	public String getPbsi43() {
		return pbsi43;
	}
	public void setPbsi43(String pbsi43) {
		this.pbsi43 = pbsi43;
	}
	public String getPbsi44() {
		return pbsi44;
	}
	public void setPbsi44(String pbsi44) {
		this.pbsi44 = pbsi44;
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
