package org.asu.chilll.power.entity.data;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "summary_digit_span")
public class SummaryDigitSpan {
	@Id
	@Column(name = "child_id_grade")
	private String childId_grade;
	private String experimenter;
	@Column(length = 10)
	private String dspt1;
	@Column(length = 10)
	private String dspt2;
	@Column(length = 10)
	private String dspt3;
	@Column(length = 10)
	private String dst21;
	@Column(length = 10)
	private String dst22;
	@Column(length = 10)
	private String dst23;
	@Column(length = 10)
	private String dst24;
	@Column(length = 10)
	private String dst31;
	@Column(length = 10)
	private String dst32;
	@Column(length = 10)
	private String dst33;
	@Column(length = 10)
	private String dst34;
	@Column(length = 10)
	private String dst41;
	@Column(length = 10)
	private String dst42;
	@Column(length = 10)
	private String dst43;
	@Column(length = 10)
	private String dst44;
	@Column(length = 10)
	private String dst51;
	@Column(length = 10)
	private String dst52;
	@Column(length = 10)
	private String dst53;
	@Column(length = 10)
	private String dst54;
	@Column(length = 10)
	private String dst61;
	@Column(length = 10)
	private String dst62;
	@Column(length = 10)
	private String dst63;
	@Column(length = 10)
	private String dst64;
	@Column(length = 10)
	private String dst71;
	@Column(length = 10)
	private String dst72;
	@Column(length = 10)
	private String dst73;
	@Column(length = 10)
	private String dst74;
	@Column(length = 10)
	private String dst81;
	@Column(length = 10)
	private String dst82;
	@Column(length = 10)
	private String dst83;
	@Column(length = 10)
	private String dst84;
	
	@Column(length = 10)
	private String dspi1;
	@Column(length = 10)
	private String dspi2;
	@Column(length = 10)
	private String dspi3;
	@Column(length = 10)
	private String dsi21;
	@Column(length = 10)
	private String dsi22;
	@Column(length = 10)
	private String dsi23;
	@Column(length = 10)
	private String dsi24;
	@Column(length = 10)
	private String dsi31;
	@Column(length = 10)
	private String dsi32;
	@Column(length = 10)
	private String dsi33;
	@Column(length = 10)
	private String dsi34;
	@Column(length = 10)
	private String dsi41;
	@Column(length = 10)
	private String dsi42;
	@Column(length = 10)
	private String dsi43;
	@Column(length = 10)
	private String dsi44;
	@Column(length = 10)
	private String dsi51;
	@Column(length = 10)
	private String dsi52;
	@Column(length = 10)
	private String dsi53;
	@Column(length = 10)
	private String dsi54;
	@Column(length = 10)
	private String dsi61;
	@Column(length = 10)
	private String dsi62;
	@Column(length = 10)
	private String dsi63;
	@Column(length = 10)
	private String dsi64;
	@Column(length = 10)
	private String dsi71;
	@Column(length = 10)
	private String dsi72;
	@Column(length = 10)
	private String dsi73;
	@Column(length = 10)
	private String dsi74;
	@Column(length = 10)
	private String dsi81;
	@Column(length = 10)
	private String dsi82;
	@Column(length = 10)
	private String dsi83;
	@Column(length = 10)
	private String dsi84;
	
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "create_time")
	private Long createTime;
	@Column(name = "last_update_date")
	private Date lastUpdateDate;
	@Column(name = "last_update_time")
	private Long lastUpdateTime;
	public String getExperimenter() {
		return experimenter;
	}
	public void setExperimenter(String experimenter) {
		this.experimenter = experimenter;
	}
	public String getChildId_grade() {
		return childId_grade;
	}
	public void setChildId_grade(String childId_grade) {
		this.childId_grade = childId_grade;
	}
	public String getDspt1() {
		return dspt1;
	}
	public void setDspt1(String dspt1) {
		this.dspt1 = dspt1;
	}
	public String getDspt2() {
		return dspt2;
	}
	public void setDspt2(String dspt2) {
		this.dspt2 = dspt2;
	}
	public String getDspt3() {
		return dspt3;
	}
	public void setDspt3(String dspt3) {
		this.dspt3 = dspt3;
	}
	public String getDst21() {
		return dst21;
	}
	public void setDst21(String dst21) {
		this.dst21 = dst21;
	}
	public String getDst22() {
		return dst22;
	}
	public void setDst22(String dst22) {
		this.dst22 = dst22;
	}
	public String getDst23() {
		return dst23;
	}
	public void setDst23(String dst23) {
		this.dst23 = dst23;
	}
	public String getDst24() {
		return dst24;
	}
	public void setDst24(String dst24) {
		this.dst24 = dst24;
	}
	public String getDst31() {
		return dst31;
	}
	public void setDst31(String dst31) {
		this.dst31 = dst31;
	}
	public String getDst32() {
		return dst32;
	}
	public void setDst32(String dst32) {
		this.dst32 = dst32;
	}
	public String getDst33() {
		return dst33;
	}
	public void setDst33(String dst33) {
		this.dst33 = dst33;
	}
	public String getDst34() {
		return dst34;
	}
	public void setDst34(String dst34) {
		this.dst34 = dst34;
	}
	public String getDst41() {
		return dst41;
	}
	public void setDst41(String dst41) {
		this.dst41 = dst41;
	}
	public String getDst42() {
		return dst42;
	}
	public void setDst42(String dst42) {
		this.dst42 = dst42;
	}
	public String getDst43() {
		return dst43;
	}
	public void setDst43(String dst43) {
		this.dst43 = dst43;
	}
	public String getDst44() {
		return dst44;
	}
	public void setDst44(String dst44) {
		this.dst44 = dst44;
	}
	public String getDst51() {
		return dst51;
	}
	public void setDst51(String dst51) {
		this.dst51 = dst51;
	}
	public String getDst52() {
		return dst52;
	}
	public void setDst52(String dst52) {
		this.dst52 = dst52;
	}
	public String getDst53() {
		return dst53;
	}
	public void setDst53(String dst53) {
		this.dst53 = dst53;
	}
	public String getDst54() {
		return dst54;
	}
	public void setDst54(String dst54) {
		this.dst54 = dst54;
	}
	public String getDst61() {
		return dst61;
	}
	public void setDst61(String dst61) {
		this.dst61 = dst61;
	}
	public String getDst62() {
		return dst62;
	}
	public void setDst62(String dst62) {
		this.dst62 = dst62;
	}
	public String getDst63() {
		return dst63;
	}
	public void setDst63(String dst63) {
		this.dst63 = dst63;
	}
	public String getDst64() {
		return dst64;
	}
	public void setDst64(String dst64) {
		this.dst64 = dst64;
	}
	public String getDst71() {
		return dst71;
	}
	public void setDst71(String dst71) {
		this.dst71 = dst71;
	}
	public String getDst72() {
		return dst72;
	}
	public void setDst72(String dst72) {
		this.dst72 = dst72;
	}
	public String getDst73() {
		return dst73;
	}
	public void setDst73(String dst73) {
		this.dst73 = dst73;
	}
	public String getDst74() {
		return dst74;
	}
	public void setDst74(String dst74) {
		this.dst74 = dst74;
	}
	public String getDst81() {
		return dst81;
	}
	public void setDst81(String dst81) {
		this.dst81 = dst81;
	}
	public String getDst82() {
		return dst82;
	}
	public void setDst82(String dst82) {
		this.dst82 = dst82;
	}
	public String getDst83() {
		return dst83;
	}
	public void setDst83(String dst83) {
		this.dst83 = dst83;
	}
	public String getDst84() {
		return dst84;
	}
	public void setDst84(String dst84) {
		this.dst84 = dst84;
	}
	public String getDspi1() {
		return dspi1;
	}
	public void setDspi1(String dspi1) {
		this.dspi1 = dspi1;
	}
	public String getDspi2() {
		return dspi2;
	}
	public void setDspi2(String dspi2) {
		this.dspi2 = dspi2;
	}
	public String getDspi3() {
		return dspi3;
	}
	public void setDspi3(String dspi3) {
		this.dspi3 = dspi3;
	}
	public String getDsi21() {
		return dsi21;
	}
	public void setDsi21(String dsi21) {
		this.dsi21 = dsi21;
	}
	public String getDsi22() {
		return dsi22;
	}
	public void setDsi22(String dsi22) {
		this.dsi22 = dsi22;
	}
	public String getDsi23() {
		return dsi23;
	}
	public void setDsi23(String dsi23) {
		this.dsi23 = dsi23;
	}
	public String getDsi24() {
		return dsi24;
	}
	public void setDsi24(String dsi24) {
		this.dsi24 = dsi24;
	}
	public String getDsi31() {
		return dsi31;
	}
	public void setDsi31(String dsi31) {
		this.dsi31 = dsi31;
	}
	public String getDsi32() {
		return dsi32;
	}
	public void setDsi32(String dsi32) {
		this.dsi32 = dsi32;
	}
	public String getDsi33() {
		return dsi33;
	}
	public void setDsi33(String dsi33) {
		this.dsi33 = dsi33;
	}
	public String getDsi34() {
		return dsi34;
	}
	public void setDsi34(String dsi34) {
		this.dsi34 = dsi34;
	}
	public String getDsi41() {
		return dsi41;
	}
	public void setDsi41(String dsi41) {
		this.dsi41 = dsi41;
	}
	public String getDsi42() {
		return dsi42;
	}
	public void setDsi42(String dsi42) {
		this.dsi42 = dsi42;
	}
	public String getDsi43() {
		return dsi43;
	}
	public void setDsi43(String dsi43) {
		this.dsi43 = dsi43;
	}
	public String getDsi44() {
		return dsi44;
	}
	public void setDsi44(String dsi44) {
		this.dsi44 = dsi44;
	}
	public String getDsi51() {
		return dsi51;
	}
	public void setDsi51(String dsi51) {
		this.dsi51 = dsi51;
	}
	public String getDsi52() {
		return dsi52;
	}
	public void setDsi52(String dsi52) {
		this.dsi52 = dsi52;
	}
	public String getDsi53() {
		return dsi53;
	}
	public void setDsi53(String dsi53) {
		this.dsi53 = dsi53;
	}
	public String getDsi54() {
		return dsi54;
	}
	public void setDsi54(String dsi54) {
		this.dsi54 = dsi54;
	}
	public String getDsi61() {
		return dsi61;
	}
	public void setDsi61(String dsi61) {
		this.dsi61 = dsi61;
	}
	public String getDsi62() {
		return dsi62;
	}
	public void setDsi62(String dsi62) {
		this.dsi62 = dsi62;
	}
	public String getDsi63() {
		return dsi63;
	}
	public void setDsi63(String dsi63) {
		this.dsi63 = dsi63;
	}
	public String getDsi64() {
		return dsi64;
	}
	public void setDsi64(String dsi64) {
		this.dsi64 = dsi64;
	}
	public String getDsi71() {
		return dsi71;
	}
	public void setDsi71(String dsi71) {
		this.dsi71 = dsi71;
	}
	public String getDsi72() {
		return dsi72;
	}
	public void setDsi72(String dsi72) {
		this.dsi72 = dsi72;
	}
	public String getDsi73() {
		return dsi73;
	}
	public void setDsi73(String dsi73) {
		this.dsi73 = dsi73;
	}
	public String getDsi74() {
		return dsi74;
	}
	public void setDsi74(String dsi74) {
		this.dsi74 = dsi74;
	}
	public String getDsi81() {
		return dsi81;
	}
	public void setDsi81(String dsi81) {
		this.dsi81 = dsi81;
	}
	public String getDsi82() {
		return dsi82;
	}
	public void setDsi82(String dsi82) {
		this.dsi82 = dsi82;
	}
	public String getDsi83() {
		return dsi83;
	}
	public void setDsi83(String dsi83) {
		this.dsi83 = dsi83;
	}
	public String getDsi84() {
		return dsi84;
	}
	public void setDsi84(String dsi84) {
		this.dsi84 = dsi84;
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
