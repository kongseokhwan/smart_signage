package com.kulcloud.signage.tenant.data.entity;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Camera
 */
@Entity
@Table(name = "tb_camera")
public class Camera {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "camera_id")
	@JsonProperty("camera_id")
	private Long cameraId;
	private String title;
	@Column(name = "mac_address")
	@JsonProperty("mac_address")
	private String macAddress;
	@Column(name = "ip_address")
	@JsonProperty("ip_address")
    private String ipAddress;
	@Column(name = "live_link")
	@JsonProperty("live_link")
    private String liveLink;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "device_id", nullable = true, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Device device;

    public Long getCameraId() {
        return cameraId;
    }

    public void setCameraId(Long cameraId) {
        this.cameraId = cameraId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getLiveLink() {
		return liveLink;
	}

	public void setLiveLink(String liveLink) {
		this.liveLink = liveLink;
	}

	public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
    
}