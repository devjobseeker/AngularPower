package org.asu.chilll.power.dataview;

public class AppErrorMessage {
	public AppErrorMessage(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	private String errorMsg;
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}