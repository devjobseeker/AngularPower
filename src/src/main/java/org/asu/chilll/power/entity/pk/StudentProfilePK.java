package org.asu.chilll.power.entity.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class StudentProfilePK implements Serializable{
	private String childId;
	private String grade;
	
	public StudentProfilePK() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof StudentProfilePK) {
			StudentProfilePK pk = (StudentProfilePK) obj;
			return pk.getChildId().equals(childId) && pk.getGrade().equals(grade);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return childId.hashCode() + grade.hashCode();
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
}
