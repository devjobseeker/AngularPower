package org.asu.chilll.power.dataview;

public class StoreItemDataView {
	private Integer itemId;
	private String name;
	private String itemSrc;
	private Double price;
	private Integer priceLevel;
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getItemSrc() {
		return itemSrc;
	}
	public void setItemSrc(String itemSrc) {
		this.itemSrc = itemSrc;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Integer getPriceLevel() {
		return priceLevel;
	}
	public void setPriceLevel(Integer priceLevel) {
		this.priceLevel = priceLevel;
	}
}