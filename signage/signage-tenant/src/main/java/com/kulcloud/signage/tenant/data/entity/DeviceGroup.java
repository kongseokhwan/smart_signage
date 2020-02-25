package com.kulcloud.signage.tenant.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tb_device_group")
public class DeviceGroup extends EditedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "device_group_id")
	@JsonProperty("device_group_id")
	private Long deviceGroupId;
	@Column(name = "title")
	@JsonProperty("title")
	private Long title;
	@OneToMany(targetEntity = Device.class, fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private List<Device> devices;

	public Long getDeviceGroupId() {
		return deviceGroupId;
	}

	public void setDeviceGroupId(Long deviceGroupId) {
		this.deviceGroupId = deviceGroupId;
	}

	public Long getTitle() {
		return title;
	}

	public void setTitle(Long title) {
		this.title = title;
	}

	public List<Device> getDevices() {
		return devices;
	}

	public void setDevices(List<Device> devices) {
		this.devices = devices;
	}
	
	public void addDevice(Device device) {
		List<Device> devices;
		if(this.devices == null) {
			if(this.deviceGroupId == null) {
				devices = new ArrayList<> ();
				this.devices = devices;
			} else {
				devices = getDevices();
			}
		} else {
			devices = this.devices;
		}
		
		boolean contain  = false;
		for (Device deviceItem : devices) {
			if(deviceItem.getDeviceId().equals(device.getDeviceId())) {
				contain = true;
			}
		}
		
		if(!contain) {
			this.devices.add(device);
		}
	}
	
	public boolean removeDevice(Long deviceId) {
		List<Device> devices = this.getDevices();
		for (Device device : devices) {
			if(device.getDeviceId().equals(deviceId)) {
				return devices.remove(device);
			}
		}
		
		return false;
	}

}
