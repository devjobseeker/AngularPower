package org.asu.chilll.power.enums;

public enum UserType {
	ADMIN("ADMIN"),
	USER("USER");
	
	private final String id;
	
	UserType(final String id){
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}