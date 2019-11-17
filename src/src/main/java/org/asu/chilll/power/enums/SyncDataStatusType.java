package org.asu.chilll.power.enums;

public enum SyncDataStatusType {
	Pending("Pending"),
	Processing("Processing"),
	Finished("Finished"),
	Fail("Fail"),
	NoData("NoData");
	
	private final String id;
	
	SyncDataStatusType(final String id){
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
