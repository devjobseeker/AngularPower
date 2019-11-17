package org.asu.chilll.power.enums;

public enum SyncDataRecordType {
	Detail("Detail"),
	Summary("Summary"),
	GameProgress("Game Progress"),
	StudentProfile("Student Profile");
	
	private final String id;
	
	SyncDataRecordType(final String id){
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}