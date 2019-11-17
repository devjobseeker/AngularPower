package org.asu.chilll.power.enums;

public enum SessionAttributeName {
	Child_Id("childId"),
	Experimenter("experimenter"),
	Grade("grade");
	
	private final String id;
	
	SessionAttributeName(final String id){
		this.id = id;
	}
	
	@Override
	public String toString() {
		return id;
	}
}
