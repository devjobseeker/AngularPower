package org.asu.chilll.power.dataview;

public class StorePurchaseDataView {
	private String childId;
	private String grade;
	private Integer purchasedItemId;
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
}
