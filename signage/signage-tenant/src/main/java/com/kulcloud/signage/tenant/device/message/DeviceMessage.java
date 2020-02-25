package com.kulcloud.signage.tenant.device.message;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceMessage {

	private String type;
	@JsonProperty("device_id")
	private Long deviceId;
	private Object message;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

}
