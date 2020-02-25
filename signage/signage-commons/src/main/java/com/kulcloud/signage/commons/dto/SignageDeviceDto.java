package com.kulcloud.signage.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignageDeviceDto {

	@JsonProperty("device_id")
	private Long deviceId;
	@JsonProperty("title")
	private Long title;
	@JsonProperty("serial_number")
	private String serialNumber;
	@JsonProperty("ip_address")
	private String ipAddress;
	private boolean camera;
	private String type;
	@JsonProperty("group_id")
	private Long groupId;

	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getTitle() {
		return title;
	}

	public void setTitle(Long title) {
		this.title = title;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public boolean isCamera() {
		return camera;
	}

	public void setCamera(boolean camera) {
		this.camera = camera;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}
