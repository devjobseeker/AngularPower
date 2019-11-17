package org.asu.chilll.power.entity.lookup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries({
    @NamedQuery(
        name = "LookupStoreItem.fetchAllItems",
        query = "SELECT p FROM LookupStoreItem p order by price_level"
    ),
    @NamedQuery(
        name = "LookupStoreItem.fetchTotalCount",
        query = "SELECT count(p) FROM LookupStoreItem p"
    )
})

@Entity
@Table(name = "lookup_store_item")
public class LookupStoreItem {
	@Id
	@Column(name = "item_id")
	private Integer itemId;
	@Column(name = "item_name")
	private String itemName;
	@Column(name = "item_src")
	private String itemSrc;
	@Column(name = "price_level")
	private Integer priceLevel;
	@Column(name = "price_grade_k")
	private Double priceGradeK;
	@Column(name = "price_grade_1")
	private Double priceGrade1;
	@Column(name = "price_grade_2")
	private Double priceGrade2;
	@Column(name = "price_grade_3")
	private Double priceGrade3;
	@Column(name = "price_grade_4")
	private Double priceGrade4;
	@Column(name = "price_grade_5")
	private Double priceGrade5;
	@Column(name = "price_grade_6")
	private Double priceGrade6;
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemSrc() {
		return itemSrc;
	}
	public void setItemSrc(String itemSrc) {
		this.itemSrc = itemSrc;
	}
	public Integer getPriceLevel() {
		return priceLevel;
	}
	public void setPriceLevel(Integer priceLevel) {
		this.priceLevel = priceLevel;
	}
	public Double getPriceGradeK() {
		return priceGradeK;
	}
	public void setPriceGradeK(Double priceGradeK) {
		this.priceGradeK = priceGradeK;
	}
	public Double getPriceGrade1() {
		return priceGrade1;
	}
	public void setPriceGrade1(Double priceGrade1) {
		this.priceGrade1 = priceGrade1;
	}
	public Double getPriceGrade2() {
		return priceGrade2;
	}
	public void setPriceGrade2(Double priceGrade2) {
		this.priceGrade2 = priceGrade2;
	}
	public Double getPriceGrade3() {
		return priceGrade3;
	}
	public void setPriceGrade3(Double priceGrade3) {
		this.priceGrade3 = priceGrade3;
	}
	public Double getPriceGrade4() {
		return priceGrade4;
	}
	public void setPriceGrade4(Double priceGrade4) {
		this.priceGrade4 = priceGrade4;
	}
	public Double getPriceGrade5() {
		return priceGrade5;
	}
	public void setPriceGrade5(Double priceGrade5) {
		this.priceGrade5 = priceGrade5;
	}
	public Double getPriceGrade6() {
		return priceGrade6;
	}
	public void setPriceGrade6(Double priceGrade6) {
		this.priceGrade6 = priceGrade6;
	}
}