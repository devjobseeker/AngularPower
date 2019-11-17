package org.asu.chilll.power.dataview;

public class ResponseDataView {
	private Integer httpCode;
	private String responseMsg;
	private boolean hasError;
	private Long count;
	public ResponseDataView() {
		
	}
	public ResponseDataView(Integer httpCode) {
		this.httpCode = httpCode;
	}
	public Integer getHttpCode() {
		return httpCode;
	}
	public void setHttpCode(Integer httpCode) {
		this.httpCode = httpCode;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public boolean getHasError() {
		return hasError;
	}
	public void setHasError(boolean hasError) {
		this.hasError = hasError;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
}