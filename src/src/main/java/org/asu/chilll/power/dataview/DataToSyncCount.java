package org.asu.chilll.power.dataview;

public class DataToSyncCount {
	private String dataType;
	private String name;
	private Long count;
	private String link;
	private String status;
	
	public DataToSyncCount() {
		
	}
	
	public DataToSyncCount(String dataType, String name, Long count, String link) {
		this.dataType = dataType;
		this.count = count;
		this.name = name;
		this.link = link;
		this.status = "start";
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}