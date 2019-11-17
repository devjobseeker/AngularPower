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
        name = "SummaryLocationSpan.fetchDistributedListLocationSpanSummaryByLimit",
        query = "SELECT d FROM SummaryLocationSpan d where d.lastUpdateTime > :lastUpdateTime order by d.lastUpdateTime"
    )
})

@Entity
@Table(name = "summary_location_span")
public class SummaryLocationSpan {
	@Id
	@Column(name = "child_id_grade")
	private String childId_grade;
	private String experimenter;
	
	@Column(length = 10)
	private String lspt1;
	@Column(length = 10)
	private String lspt2;
	@Column(length = 10)
	private String lspt3;
	@Column(length = 10)
	private String lst21;
	@Column(length = 10)
	private String lst22;
	@Column(length = 10)
	private String lst23;
	@Column(length = 10)
	private String lst24;
	@Column(length = 10)
	private String lst31;
	@Column(length = 10)
	private String lst32;
	@Column(length = 10)
	private String lst33;
	@Column(length = 10)
	private String lst34;
	@Column(length = 10)
	private String lst41;
	@Column(length = 10)
	private String lst42;
	@Column(length = 10)
	private String lst43;
	@Column(length = 10)
	private String lst44;
	@Column(length = 10)
	private String lst51;
	@Column(length = 10)
	private String lst52;
	@Column(length = 10)
	private String lst53;
	@Column(length = 10)
	private String lst54;
	@Column(length = 10)
	private String lst61;
	@Column(length = 10)
	private String lst62;
	@Column(length = 10)
	private String lst63;
	@Column(length = 10)
	private String lst64;
	
	@Column(length = 10)
	private String lspi1;
	@Column(length = 10)
	private String lspi2;
	@Column(length = 10)
	private String lspi3;
	@Column(length = 10)
	private String lsi21;
	@Column(length = 10)
	private String lsi22;
	@Column(length = 10)
	private String lsi23;
	@Column(length = 10)
	private String lsi24;
	@Column(length = 10)
	private String lsi31;
	@Column(length = 10)
	private String lsi32;
	@Column(length = 10)
	private String lsi33;
	@Column(length = 10)
	private String lsi34;
	@Column(length = 10)
	private String lsi41;
	@Column(length = 10)
	private String lsi42;
	@Column(length = 10)
	private String lsi43;
	@Column(length = 10)
	private String lsi44;
	@Column(length = 10)
	private String lsi51;
	@Column(length = 10)
	private String lsi52;
	@Column(length = 10)
	private String lsi53;
	@Column(length = 10)
	private String lsi54;
	@Column(length = 10)
	private String lsi61;
	@Column(length = 10)
	private String lsi62;
	@Column(length = 10)
	private String lsi63;
	@Column(length = 10)
	private String lsi64;
	
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
	public String getLst21() {
		return lst21;
	}
	public void setLst21(String lst21) {
		this.lst21 = lst21;
	}
	public String getLst22() {
		return lst22;
	}
	public void setLst22(String lst22) {
		this.lst22 = lst22;
	}
	public String getLst23() {
		return lst23;
	}
	public void setLst23(String lst23) {
		this.lst23 = lst23;
	}
	public String getLst24() {
		return lst24;
	}
	public void setLst24(String lst24) {
		this.lst24 = lst24;
	}
	public String getLst31() {
		return lst31;
	}
	public void setLst31(String lst31) {
		this.lst31 = lst31;
	}
	public String getLst32() {
		return lst32;
	}
	public void setLst32(String lst32) {
		this.lst32 = lst32;
	}
	public String getLst33() {
		return lst33;
	}
	public void setLst33(String lst33) {
		this.lst33 = lst33;
	}
	public String getLst34() {
		return lst34;
	}
	public void setLst34(String lst34) {
		this.lst34 = lst34;
	}
	public String getLst41() {
		return lst41;
	}
	public void setLst41(String lst41) {
		this.lst41 = lst41;
	}
	public String getLst42() {
		return lst42;
	}
	public void setLst42(String lst42) {
		this.lst42 = lst42;
	}
	public String getLst43() {
		return lst43;
	}
	public void setLst43(String lst43) {
		this.lst43 = lst43;
	}
	public String getLst44() {
		return lst44;
	}
	public void setLst44(String lst44) {
		this.lst44 = lst44;
	}
	public String getLst51() {
		return lst51;
	}
	public void setLst51(String lst51) {
		this.lst51 = lst51;
	}
	public String getLst52() {
		return lst52;
	}
	public void setLst52(String lst52) {
		this.lst52 = lst52;
	}
	public String getLst53() {
		return lst53;
	}
	public void setLst53(String lst53) {
		this.lst53 = lst53;
	}
	public String getLst54() {
		return lst54;
	}
	public void setLst54(String lst54) {
		this.lst54 = lst54;
	}
	public String getLst61() {
		return lst61;
	}
	public void setLst61(String lst61) {
		this.lst61 = lst61;
	}
	public String getLst62() {
		return lst62;
	}
	public void setLst62(String lst62) {
		this.lst62 = lst62;
	}
	public String getLst63() {
		return lst63;
	}
	public void setLst63(String lst63) {
		this.lst63 = lst63;
	}
	public String getLst64() {
		return lst64;
	}
	public void setLst64(String lst64) {
		this.lst64 = lst64;
	}
	public String getLsi21() {
		return lsi21;
	}
	public void setLsi21(String lsi21) {
		this.lsi21 = lsi21;
	}
	public String getLsi22() {
		return lsi22;
	}
	public void setLsi22(String lsi22) {
		this.lsi22 = lsi22;
	}
	public String getLsi23() {
		return lsi23;
	}
	public void setLsi23(String lsi23) {
		this.lsi23 = lsi23;
	}
	public String getLsi24() {
		return lsi24;
	}
	public void setLsi24(String lsi24) {
		this.lsi24 = lsi24;
	}
	public String getLsi31() {
		return lsi31;
	}
	public void setLsi31(String lsi31) {
		this.lsi31 = lsi31;
	}
	public String getLsi32() {
		return lsi32;
	}
	public void setLsi32(String lsi32) {
		this.lsi32 = lsi32;
	}
	public String getLsi33() {
		return lsi33;
	}
	public void setLsi33(String lsi33) {
		this.lsi33 = lsi33;
	}
	public String getLsi34() {
		return lsi34;
	}
	public void setLsi34(String lsi34) {
		this.lsi34 = lsi34;
	}
	public String getLsi41() {
		return lsi41;
	}
	public void setLsi41(String lsi41) {
		this.lsi41 = lsi41;
	}
	public String getLsi42() {
		return lsi42;
	}
	public void setLsi42(String lsi42) {
		this.lsi42 = lsi42;
	}
	public String getLsi43() {
		return lsi43;
	}
	public void setLsi43(String lsi43) {
		this.lsi43 = lsi43;
	}
	public String getLsi44() {
		return lsi44;
	}
	public void setLsi44(String lsi44) {
		this.lsi44 = lsi44;
	}
	public String getLsi51() {
		return lsi51;
	}
	public void setLsi51(String lsi51) {
		this.lsi51 = lsi51;
	}
	public String getLsi52() {
		return lsi52;
	}
	public void setLsi52(String lsi52) {
		this.lsi52 = lsi52;
	}
	public String getLsi53() {
		return lsi53;
	}
	public void setLsi53(String lsi53) {
		this.lsi53 = lsi53;
	}
	public String getLsi54() {
		return lsi54;
	}
	public void setLsi54(String lsi54) {
		this.lsi54 = lsi54;
	}
	public String getLsi61() {
		return lsi61;
	}
	public void setLsi61(String lsi61) {
		this.lsi61 = lsi61;
	}
	public String getLsi62() {
		return lsi62;
	}
	public void setLsi62(String lsi62) {
		this.lsi62 = lsi62;
	}
	public String getLsi63() {
		return lsi63;
	}
	public void setLsi63(String lsi63) {
		this.lsi63 = lsi63;
	}
	public String getLsi64() {
		return lsi64;
	}
	public void setLsi64(String lsi64) {
		this.lsi64 = lsi64;
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
	public String getLspt1() {
		return lspt1;
	}
	public void setLspt1(String lspt1) {
		this.lspt1 = lspt1;
	}
	public String getLspt2() {
		return lspt2;
	}
	public void setLspt2(String lspt2) {
		this.lspt2 = lspt2;
	}
	public String getLspt3() {
		return lspt3;
	}
	public void setLspt3(String lspt3) {
		this.lspt3 = lspt3;
	}
	public String getLspi1() {
		return lspi1;
	}
	public void setLspi1(String lspi1) {
		this.lspi1 = lspi1;
	}
	public String getLspi2() {
		return lspi2;
	}
	public void setLspi2(String lspi2) {
		this.lspi2 = lspi2;
	}
	public String getLspi3() {
		return lspi3;
	}
	public void setLspi3(String lspi3) {
		this.lspi3 = lspi3;
	}
}
