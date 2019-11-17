package org.asu.chilll.power.dataview.redcap;

import org.asu.chilll.power.entity.feature.SyncDataResult;

public class SyncRecordDataView {
	private String childId;
	private String cohort;
	private String grade;
	private String gameId;
	private String category;
	private Long totalRecordsCount;
	private String syncStatus;
	private SyncDataResult syncDataResult;
	private APIConfig apiConfig;
	public SyncRecordDataView() {
		
	}
	public SyncRecordDataView(String childId, String grade, String gameId, String category, String syncStatus) {
		this.childId = childId;
		this.grade = grade;
		this.gameId = gameId;
		this.category = category;
		this.syncStatus = syncStatus;
	}
	public String getChildId() {
		return childId;
	}
	public void setChildId(String childId) {
		this.childId = childId;
	}
	public String getCohort() {
		return cohort;
	}
	public void setCohort(String cohort) {
		this.cohort = cohort;
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
	public Long getTotalRecordsCount() {
		return totalRecordsCount;
	}
	public void setTotalRecordsCount(Long totalRecordsCount) {
		this.totalRecordsCount = totalRecordsCount;
	}
	public String getSyncStatus() {
		return syncStatus;
	}
	public void setSyncStatus(String syncStatus) {
		this.syncStatus = syncStatus;
	}
	public SyncDataResult getSyncDataResult() {
		return syncDataResult;
	}
	public void setSyncDataResult(SyncDataResult syncDataResult) {
		this.syncDataResult = syncDataResult;
	}
	public APIConfig getApiConfig() {
		return apiConfig;
	}
	public void setApiConfig(APIConfig apiConfig) {
		this.apiConfig = apiConfig;
	}
}