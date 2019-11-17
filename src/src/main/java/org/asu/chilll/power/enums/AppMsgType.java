package org.asu.chilll.power.enums;

public enum AppMsgType {
	User_Exist("User_Exist"),
	User_Not_Exist("User_Not_Exist"),
	Success("Success");
	
	private final String id;
	
	AppMsgType(final String id){
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
