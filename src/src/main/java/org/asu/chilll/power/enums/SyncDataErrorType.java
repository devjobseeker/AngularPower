package org.asu.chilll.power.enums;

public enum SyncDataErrorType {
	No_Record("No record need to be insert or update."),
	Duplicate_Record("Duplicate record exists"),
	REDCap_Error("REDCap error"),
	Other("Other Error");
	
	private final String id;
	
	SyncDataErrorType(final String id){
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}