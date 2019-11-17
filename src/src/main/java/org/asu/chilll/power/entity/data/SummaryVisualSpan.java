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
        name = "SummaryVisualSpan.fetchDistributedListVisualSpanSummaryByLimit",
        query = "SELECT d FROM SummaryVisualSpan d where d.lastUpdateTime > :lastUpdateTime order by d.lastUpdateTime"
    )
})

@Entity
@Table(name = "summary_visual_span")
public class SummaryVisualSpan {
	@Id
	@Column(name = "child_id_grade")
	private String childId_grade;
	private String experimenter;
	
	@Column(length = 10)
	private String vspt1;
	@Column(length = 10)
	private String vspt2;
	@Column(length = 10)
	private String vspt3;
	@Column(length = 10)
	private String vst21;
	@Column(length = 10)
	private String vst22;
	@Column(length = 10)
	private String vst23;
	@Column(length = 10)
	private String vst24;
	@Column(length = 10)
	private String vst31;
	@Column(length = 10)
	private String vst32;
	@Column(length = 10)
	private String vst33;
	@Column(length = 10)
	private String vst34;
	@Column(length = 10)
	private String vst41;
	@Column(length = 10)
	private String vst42;
	@Column(length = 10)
	private String vst43;
	@Column(length = 10)
	private String vst44;
	@Column(length = 10)
	private String vst51;
	@Column(length = 10)
	private String vst52;
	@Column(length = 10)
	private String vst53;
	@Column(length = 10)
	private String vst54;
	@Column(length = 10)
	private String vst61;
	@Column(length = 10)
	private String vst62;
	@Column(length = 10)
	private String vst63;
	@Column(length = 10)
	private String vst64;
	
	@Column(length = 10)
	private String vspi1;
	@Column(length = 10)
	private String vspi2;
	@Column(length = 10)
	private String vspi3;
	@Column(length = 10)
	private String vsi21;
	@Column(length = 10)
	private String vsi22;
	@Column(length = 10)
	private String vsi23;
	@Column(length = 10)
	private String vsi24;
	@Column(length = 10)
	private String vsi31;
	@Column(length = 10)
	private String vsi32;
	@Column(length = 10)
	private String vsi33;
	@Column(length = 10)
	private String vsi34;
	@Column(length = 10)
	private String vsi41;
	@Column(length = 10)
	private String vsi42;
	@Column(length = 10)
	private String vsi43;
	@Column(length = 10)
	private String vsi44;
	@Column(length = 10)
	private String vsi51;
	@Column(length = 10)
	private String vsi52;
	@Column(length = 10)
	private String vsi53;
	@Column(length = 10)
	private String vsi54;
	@Column(length = 10)
	private String vsi61;
	@Column(length = 10)
	private String vsi62;
	@Column(length = 10)
	private String vsi63;
	@Column(length = 10)
	private String vsi64;
	
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
	public String getVst21() {
		return vst21;
	}
	public void setVst21(String vst21) {
		this.vst21 = vst21;
	}
	public String getVst22() {
		return vst22;
	}
	public void setVst22(String vst22) {
		this.vst22 = vst22;
	}
	public String getVst23() {
		return vst23;
	}
	public void setVst23(String vst23) {
		this.vst23 = vst23;
	}
	public String getVst24() {
		return vst24;
	}
	public void setVst24(String vst24) {
		this.vst24 = vst24;
	}
	public String getVst31() {
		return vst31;
	}
	public void setVst31(String vst31) {
		this.vst31 = vst31;
	}
	public String getVst32() {
		return vst32;
	}
	public void setVst32(String vst32) {
		this.vst32 = vst32;
	}
	public String getVst33() {
		return vst33;
	}
	public void setVst33(String vst33) {
		this.vst33 = vst33;
	}
	public String getVst34() {
		return vst34;
	}
	public void setVst34(String vst34) {
		this.vst34 = vst34;
	}
	public String getVst41() {
		return vst41;
	}
	public void setVst41(String vst41) {
		this.vst41 = vst41;
	}
	public String getVst42() {
		return vst42;
	}
	public void setVst42(String vst42) {
		this.vst42 = vst42;
	}
	public String getVst43() {
		return vst43;
	}
	public void setVst43(String vst43) {
		this.vst43 = vst43;
	}
	public String getVst44() {
		return vst44;
	}
	public void setVst44(String vst44) {
		this.vst44 = vst44;
	}
	public String getVst51() {
		return vst51;
	}
	public void setVst51(String vst51) {
		this.vst51 = vst51;
	}
	public String getVst52() {
		return vst52;
	}
	public void setVst52(String vst52) {
		this.vst52 = vst52;
	}
	public String getVst53() {
		return vst53;
	}
	public void setVst53(String vst53) {
		this.vst53 = vst53;
	}
	public String getVst54() {
		return vst54;
	}
	public void setVst54(String vst54) {
		this.vst54 = vst54;
	}
	public String getVst61() {
		return vst61;
	}
	public void setVst61(String vst61) {
		this.vst61 = vst61;
	}
	public String getVst62() {
		return vst62;
	}
	public void setVst62(String vst62) {
		this.vst62 = vst62;
	}
	public String getVst63() {
		return vst63;
	}
	public void setVst63(String vst63) {
		this.vst63 = vst63;
	}
	public String getVst64() {
		return vst64;
	}
	public void setVst64(String vst64) {
		this.vst64 = vst64;
	}
	public String getVsi21() {
		return vsi21;
	}
	public void setVsi21(String vsi21) {
		this.vsi21 = vsi21;
	}
	public String getVsi22() {
		return vsi22;
	}
	public void setVsi22(String vsi22) {
		this.vsi22 = vsi22;
	}
	public String getVsi23() {
		return vsi23;
	}
	public void setVsi23(String vsi23) {
		this.vsi23 = vsi23;
	}
	public String getVsi24() {
		return vsi24;
	}
	public void setVsi24(String vsi24) {
		this.vsi24 = vsi24;
	}
	public String getVsi31() {
		return vsi31;
	}
	public void setVsi31(String vsi31) {
		this.vsi31 = vsi31;
	}
	public String getVsi32() {
		return vsi32;
	}
	public void setVsi32(String vsi32) {
		this.vsi32 = vsi32;
	}
	public String getVsi33() {
		return vsi33;
	}
	public void setVsi33(String vsi33) {
		this.vsi33 = vsi33;
	}
	public String getVsi34() {
		return vsi34;
	}
	public void setVsi34(String vsi34) {
		this.vsi34 = vsi34;
	}
	public String getVsi41() {
		return vsi41;
	}
	public void setVsi41(String vsi41) {
		this.vsi41 = vsi41;
	}
	public String getVsi42() {
		return vsi42;
	}
	public void setVsi42(String vsi42) {
		this.vsi42 = vsi42;
	}
	public String getVsi43() {
		return vsi43;
	}
	public void setVsi43(String vsi43) {
		this.vsi43 = vsi43;
	}
	public String getVsi44() {
		return vsi44;
	}
	public void setVsi44(String vsi44) {
		this.vsi44 = vsi44;
	}
	public String getVsi51() {
		return vsi51;
	}
	public void setVsi51(String vsi51) {
		this.vsi51 = vsi51;
	}
	public String getVsi52() {
		return vsi52;
	}
	public void setVsi52(String vsi52) {
		this.vsi52 = vsi52;
	}
	public String getVsi53() {
		return vsi53;
	}
	public void setVsi53(String vsi53) {
		this.vsi53 = vsi53;
	}
	public String getVsi54() {
		return vsi54;
	}
	public void setVsi54(String vsi54) {
		this.vsi54 = vsi54;
	}
	public String getVsi61() {
		return vsi61;
	}
	public void setVsi61(String vsi61) {
		this.vsi61 = vsi61;
	}
	public String getVsi62() {
		return vsi62;
	}
	public void setVsi62(String vsi62) {
		this.vsi62 = vsi62;
	}
	public String getVsi63() {
		return vsi63;
	}
	public void setVsi63(String vsi63) {
		this.vsi63 = vsi63;
	}
	public String getVsi64() {
		return vsi64;
	}
	public void setVsi64(String vsi64) {
		this.vsi64 = vsi64;
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
	public String getVspt1() {
		return vspt1;
	}
	public void setVspt1(String vspt1) {
		this.vspt1 = vspt1;
	}
	public String getVspt2() {
		return vspt2;
	}
	public void setVspt2(String vspt2) {
		this.vspt2 = vspt2;
	}
	public String getVspt3() {
		return vspt3;
	}
	public void setVspt3(String vspt3) {
		this.vspt3 = vspt3;
	}
	public String getVspi1() {
		return vspi1;
	}
	public void setVspi1(String vspi1) {
		this.vspi1 = vspi1;
	}
	public String getVspi2() {
		return vspi2;
	}
	public void setVspi2(String vspi2) {
		this.vspi2 = vspi2;
	}
	public String getVspi3() {
		return vspi3;
	}
	public void setVspi3(String vspi3) {
		this.vspi3 = vspi3;
	}
}
