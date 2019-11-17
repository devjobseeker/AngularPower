package org.asu.chilll.power.dataview;

public class UserDataView {
	private String uid;
	private String username;
	private String password;
	private String role;
	private Boolean enabled;
	
	private AppErrorMessage errorMsg;
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public AppErrorMessage getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(AppErrorMessage errorMsg) {
		this.errorMsg = errorMsg;
	}
}