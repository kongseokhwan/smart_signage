package com.kulcloud.signage.tenant.data.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tb_schedule")
public class Schedule extends EditedEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "schedule_id")
	private Long scheduleId;
	private String title;
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "start_time")
	@JsonProperty("start_time")
	private LocalDateTime startTime;
	@Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
	@Column(name = "end_time")
	@JsonProperty("end_time")
	private LocalDateTime endTime;
	
	@OneToMany(fetch = FetchType.LAZY, targetEntity = ScheduleContent.class)
	@JoinColumn(name = "schedule_id")
	private List<ScheduleContent> contents;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "tbl_schedule_device",
               joinColumns = @JoinColumn(name = "schedule_id"),
               inverseJoinColumns = @JoinColumn(name = "device_id"))
	private List<Device> devices;

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public List<ScheduleContent> getContents() {
		return contents;
	}

	public void setContents(List<ScheduleContent> contents) {
		this.contents = contents;
	}

	public void addContent(ScheduleContent content) {
		if(content.getId() != null && content.getId().getScheduleId() != null
				&& content.getId().getScheduleId().equals(scheduleId)) {
			List<ScheduleContent> contents;
			if(this.contents == null) {
				if(this.scheduleId == null) {
					contents = new ArrayList<> ();
					this.contents = contents;
				} else {
					contents = getContents();
				}
			} else {
				contents = this.contents;
			}
			
			boolean contain  = false;
			for (ScheduleContent contentItem : contents) {
				if(contentItem.getId().equals(content.getId())) {
					contain = true;
				}
			}
			
			if(!contain) {
				contents.add(content);
			}
		}
	}
	
	public boolean removeContent(ScheduleContentPK contentId) {
		List<ScheduleContent> contents = this.getContents();
		for (ScheduleContent content : contents) {
			if(content.getId().equals(contentId)) {
				return contents.remove(content);
			}
		}
		
		return false;
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
			if(this.scheduleId == null) {
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
