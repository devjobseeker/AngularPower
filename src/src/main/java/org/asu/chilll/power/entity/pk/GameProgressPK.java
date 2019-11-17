package org.asu.chilll.power.entity.pk;

import java.io.Serializable;

import javax.persistence.Embeddable;

@Embeddable
public class GameProgressPK implements Serializable{
	private String childId;
	private String grade;
	//private Integer year;
	private String gameId;
	
	public GameProgressPK() {
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof GameProgressPK) {
			GameProgressPK pk = (GameProgressPK) obj;
			return pk.getChildId().equals(childId) && pk.getGrade().equals(grade) && pk.getGameId().equals(gameId);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return childId.hashCode() + gameId.hashCode() + grade.hashCode();
	}

	public String getChildId() {
		return childId;
	}

	public void setChildId(String childId) {
		this.childId = childId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
}
