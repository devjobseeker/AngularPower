package org.asu.chilll.power.entity.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class SyncDataPK implements Serializable{
	private String childId;
	private String grade;
	private String gameId;
	private String category;
	
	public SyncDataPK() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof SyncDataPK) {
			SyncDataPK pk = (SyncDataPK) obj;
			return pk.getChildId().equals(childId) && pk.getGrade().equals(grade) && pk.getGameId().equals(gameId) && pk.getCategory().equals(category);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return childId.hashCode() + gameId.hashCode() + grade.hashCode() + category.hashCode();
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

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
