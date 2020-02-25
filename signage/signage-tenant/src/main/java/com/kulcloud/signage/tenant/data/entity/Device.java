package com.kulcloud.signage.tenant.data.entity;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kulcloud.signage.commons.enums.DeviceType;
import com.kulcloud.signage.commons.utils.MapConverterJson;

@Entity
@Table(name = "tb_device", indexes = {
		@Index(name="IDX_TYPE_IP", columnList = "type, ip_address")
})
public class Device {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "device_id")
	@JsonProperty("device_id")
	private Long deviceId;
	private String title;
	@Column(name = "serial_number")
	@JsonProperty("serial_number")
	private String serialNumber;
	@Column(name = "ip_address")
	@JsonProperty("ip_address")
	private String ipAddress;
	private String type = DeviceType.chromecast.name();
	@Column(name = "group_id")
	@JsonProperty("group_id")
	private Long groupId;
	private boolean health = false;
	@Convert(converter = MapConverterJson.class)
	private Map<String, Object> controls = new HashMap<>();;
	@Convert(converter = MapConverterJson.class)
	private Map<String, Object> config = new HashMap<>();
	
	public Long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
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

	public boolean isHealth() {
		return health;
	}

	public void setHealth(boolean health) {
		this.health = health;
	}

	public Map<String, Object> getControls() {
		return controls;
	}

	public void setControls(Map<String, Object> controls) {
		this.controls = controls;
	}

	public Map<String, Object> getConfig() {
		return config;
	}

	public void setConfig(Map<String, Object> config) {
		this.config = config;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((deviceId == null) ? 0 : deviceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		if (deviceId == null) {
			if (other.deviceId != null)
				return false;
		} else if (!deviceId.equals(other.deviceId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return title;
	}

}
