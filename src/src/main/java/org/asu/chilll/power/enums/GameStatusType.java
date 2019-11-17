package org.asu.chilll.power.enums;

public enum GameStatusType {
	Start("Start"),	//in intro part
	InPractice("In Practice"),	//in practice trial
	InProgress("In Progress"),	//in real trial
	Complete("Complete");
	
	private final String id;
	
	GameStatusType(final String id){
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}