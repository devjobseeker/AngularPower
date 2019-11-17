package org.asu.chilll.power.dataview.digitspan;

import java.util.List;

import org.asu.chilll.power.dataview.GameUserData;

public class DigitSpanDataView {
	private String childId;
	private String grade;
	private List<GameUserData> userData;
	private List<AudioDigit> audioDigits;
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
	public List<GameUserData> getUserData() {
		return userData;
	}
	public void setUserData(List<GameUserData> userData) {
		this.userData = userData;
	}
	public List<AudioDigit> getAudioDigits() {
		return audioDigits;
	}
	public void setAudioDigits(List<AudioDigit> audioDigits) {
		this.audioDigits = audioDigits;
	}
}
