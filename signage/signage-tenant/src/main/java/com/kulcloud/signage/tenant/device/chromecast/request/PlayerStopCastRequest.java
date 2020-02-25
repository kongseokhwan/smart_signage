package com.kulcloud.signage.tenant.device.chromecast.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PlayerStopCastRequest extends CastRequest {

	@JsonProperty("device_id")
	private Long deviceId;

	public PlayerStopCastRequest() {
	}

	public PlayerStopCastRequest(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	
	@Override
	public String getRequestType() {
		return "stopPlay";
	}

}
