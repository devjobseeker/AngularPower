package org.asu.chilll.power.dataview.redcap;

public class APIConfig {
	private String token;
	private String url;
	private String deviceId;
	public APIConfig() {
		
	}
	public APIConfig(String token, String url) {
		this.token = token;
		this.url = url;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
}
