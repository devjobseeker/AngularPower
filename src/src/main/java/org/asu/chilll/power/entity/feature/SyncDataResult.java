package org.asu.chilll.power.entity.feature;

public class SyncDataResult {
	private Long affectedRowsCount;
	private String errorType;
	private String errorDetail;
	private Integer respCode;
	
	public SyncDataResult(Long affectedRowsCount) {
		this.affectedRowsCount = affectedRowsCount;
	}
	public SyncDataResult(String errorType, String errorDetail) {
		this.errorType = errorType;
		this.errorDetail = errorDetail;
	}
	public SyncDataResult() {
		
	}
	public Long getAffectedRowsCount() {
		return affectedRowsCount;
	}
	public void setAffectedRowsCount(Long affectedRowsCount) {
		this.affectedRowsCount = affectedRowsCount;
	}
	public String getErrorType() {
		return errorType;
	}
	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
	public String getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
	public Integer getRespCode() {
		return respCode;
	}
	public void setRespCode(Integer respCode) {
		this.respCode = respCode;
	}
}
