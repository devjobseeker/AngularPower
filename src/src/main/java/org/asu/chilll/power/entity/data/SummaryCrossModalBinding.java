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
        name = "SummaryCrossModalBinding.fetchDistributedListCrossModalBindingSummaryByLimit",
        query = "SELECT d FROM SummaryCrossModalBinding d where d.lastUpdateTime > :lastUpdateTime order by d.lastUpdateTime"
    )
})

@Entity
@Table(name = "summary_cross_modal_binding")
public class SummaryCrossModalBinding {
	@Id
	@Column(name = "child_id_grade")
	private String childId_grade;
	private String experimenter;
	
	@Column(length = 10)
	private String cmbpt1;
	@Column(length = 10)
	private String cmbpt2;
	@Column(length = 10)
	private String cmbpt3;
	@Column(length = 10)
	private String cmbt21;
	@Column(length = 10)
	private String cmbt22;
	@Column(length = 10)
	private String cmbt23;
	@Column(length = 10)
	private String cmbt24;
	@Column(length = 10)
	private String cmbt31;
	@Column(length = 10)
	private String cmbt32;
	@Column(length = 10)
	private String cmbt33;
	@Column(length = 10)
	private String cmbt34;
	@Column(length = 10)
	private String cmbt41;
	@Column(length = 10)
	private String cmbt42;
	@Column(length = 10)
	private String cmbt43;
	@Column(length = 10)
	private String cmbt44;
	
	@Column(length = 10)
	private String cmbpi1;
	@Column(length = 10)
	private String cmbpi2;
	@Column(length = 10)
	private String cmbpi3;
	@Column(length = 10)
	private String cmbi21;
	@Column(length = 10)
	private String cmbi22;
	@Column(length = 10)
	private String cmbi23;
	@Column(length = 10)
	private String cmbi24;
	@Column(length = 10)
	private String cmbi31;
	@Column(length = 10)
	private String cmbi32;
	@Column(length = 10)
	private String cmbi33;
	@Column(length = 10)
	private String cmbi34;
	@Column(length = 10)
	private String cmbi41;
	@Column(length = 10)
	private String cmbi42;
	@Column(length = 10)
	private String cmbi43;
	@Column(length = 10)
	private String cmbi44;
	
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
	public String getCmbt21() {
		return cmbt21;
	}
	public void setCmbt21(String cmbt21) {
		this.cmbt21 = cmbt21;
	}
	public String getCmbt22() {
		return cmbt22;
	}
	public void setCmbt22(String cmbt22) {
		this.cmbt22 = cmbt22;
	}
	public String getCmbt23() {
		return cmbt23;
	}
	public void setCmbt23(String cmbt23) {
		this.cmbt23 = cmbt23;
	}
	public String getCmbt24() {
		return cmbt24;
	}
	public void setCmbt24(String cmbt24) {
		this.cmbt24 = cmbt24;
	}
	public String getCmbt31() {
		return cmbt31;
	}
	public void setCmbt31(String cmbt31) {
		this.cmbt31 = cmbt31;
	}
	public String getCmbt32() {
		return cmbt32;
	}
	public void setCmbt32(String cmbt32) {
		this.cmbt32 = cmbt32;
	}
	public String getCmbt33() {
		return cmbt33;
	}
	public void setCmbt33(String cmbt33) {
		this.cmbt33 = cmbt33;
	}
	public String getCmbt34() {
		return cmbt34;
	}
	public void setCmbt34(String cmbt34) {
		this.cmbt34 = cmbt34;
	}
	public String getCmbt41() {
		return cmbt41;
	}
	public void setCmbt41(String cmbt41) {
		this.cmbt41 = cmbt41;
	}
	public String getCmbt42() {
		return cmbt42;
	}
	public void setCmbt42(String cmbt42) {
		this.cmbt42 = cmbt42;
	}
	public String getCmbt43() {
		return cmbt43;
	}
	public void setCmbt43(String cmbt43) {
		this.cmbt43 = cmbt43;
	}
	public String getCmbt44() {
		return cmbt44;
	}
	public void setCmbt44(String cmbt44) {
		this.cmbt44 = cmbt44;
	}
	public String getCmbi21() {
		return cmbi21;
	}
	public void setCmbi21(String cmbi21) {
		this.cmbi21 = cmbi21;
	}
	public String getCmbi22() {
		return cmbi22;
	}
	public void setCmbi22(String cmbi22) {
		this.cmbi22 = cmbi22;
	}
	public String getCmbi23() {
		return cmbi23;
	}
	public void setCmbi23(String cmbi23) {
		this.cmbi23 = cmbi23;
	}
	public String getCmbi24() {
		return cmbi24;
	}
	public void setCmbi24(String cmbi24) {
		this.cmbi24 = cmbi24;
	}
	public String getCmbi31() {
		return cmbi31;
	}
	public void setCmbi31(String cmbi31) {
		this.cmbi31 = cmbi31;
	}
	public String getCmbi32() {
		return cmbi32;
	}
	public void setCmbi32(String cmbi32) {
		this.cmbi32 = cmbi32;
	}
	public String getCmbi33() {
		return cmbi33;
	}
	public void setCmbi33(String cmbi33) {
		this.cmbi33 = cmbi33;
	}
	public String getCmbi34() {
		return cmbi34;
	}
	public void setCmbi34(String cmbi34) {
		this.cmbi34 = cmbi34;
	}
	public String getCmbi41() {
		return cmbi41;
	}
	public void setCmbi41(String cmbi41) {
		this.cmbi41 = cmbi41;
	}
	public String getCmbi42() {
		return cmbi42;
	}
	public void setCmbi42(String cmbi42) {
		this.cmbi42 = cmbi42;
	}
	public String getCmbi43() {
		return cmbi43;
	}
	public void setCmbi43(String cmbi43) {
		this.cmbi43 = cmbi43;
	}
	public String getCmbi44() {
		return cmbi44;
	}
	public void setCmbi44(String cmbi44) {
		this.cmbi44 = cmbi44;
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
	public String getCmbpt1() {
		return cmbpt1;
	}
	public void setCmbpt1(String cmbpt1) {
		this.cmbpt1 = cmbpt1;
	}
	public String getCmbpt2() {
		return cmbpt2;
	}
	public void setCmbpt2(String cmbpt2) {
		this.cmbpt2 = cmbpt2;
	}
	public String getCmbpt3() {
		return cmbpt3;
	}
	public void setCmbpt3(String cmbpt3) {
		this.cmbpt3 = cmbpt3;
	}
	public String getCmbpi1() {
		return cmbpi1;
	}
	public void setCmbpi1(String cmbpi1) {
		this.cmbpi1 = cmbpi1;
	}
	public String getCmbpi2() {
		return cmbpi2;
	}
	public void setCmbpi2(String cmbpi2) {
		this.cmbpi2 = cmbpi2;
	}
	public String getCmbpi3() {
		return cmbpi3;
	}
	public void setCmbpi3(String cmbpi3) {
		this.cmbpi3 = cmbpi3;
	}
}
