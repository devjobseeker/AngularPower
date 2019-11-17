package org.asu.chilll.power.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "store_purchase_history")
public class StorePurchaseHistory {
	@Id
	private String uid;
	@Column(name = "child_id")
	private String childId;
	private String grade;
	@Column(name = "purchased_item_id")
	private Integer purchasedItemId;
	@Column(name = "purchased_price")
	private Double purchasedPrice;
	@Column(name = "total_coins_before_purchase")
	private Integer totalCoinsBeforePurchase;
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
	public Integer getPurchasedItemId() {
		return purchasedItemId;
	}
	public void setPurchasedItemId(Integer purchasedItemId) {
		this.purchasedItemId = purchasedItemId;
	}
	public Double getPurchasedPrice() {
		return purchasedPrice;
	}
	public void setPurchasedPrice(Double purchasedPrice) {
		this.purchasedPrice = purchasedPrice;
	}
	public Integer getTotalCoinsBeforePurchase() {
		return totalCoinsBeforePurchase;
	}
	public void setTotalCoinsBeforePurchase(Integer totalCoinsBeforePurchase) {
		this.totalCoinsBeforePurchase = totalCoinsBeforePurchase;
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