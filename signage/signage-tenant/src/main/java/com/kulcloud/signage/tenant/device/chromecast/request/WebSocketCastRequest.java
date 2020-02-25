package com.kulcloud.signage.tenant.device.chromecast.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WebSocketCastRequest extends CastRequest {

	@JsonProperty
	private String url;
	@JsonProperty("device_id")
	private Long deviceId;
	
	public WebSocketCastRequest() {}
	
	public WebSocketCastRequest(Long deviceId, String url) {
		this.deviceId = deviceId;
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	@Override
	public String getRequestType() {
		return "websocket";
	}

}
