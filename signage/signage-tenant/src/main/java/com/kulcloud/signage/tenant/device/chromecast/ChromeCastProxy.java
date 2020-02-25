package com.kulcloud.signage.tenant.device.chromecast;

import java.io.IOException;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.WebSocketSession;

import com.kulcloud.signage.commons.utils.CommonConstants;
import com.kulcloud.signage.commons.utils.CommonUtils;
import com.kulcloud.signage.tenant.data.entity.Content;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.data.entity.Playlist;
import com.kulcloud.signage.tenant.device.DeviceController;
import com.kulcloud.signage.tenant.device.chromecast.request.PlayerStopCastRequest;

import su.litvak.chromecast.api.v2.ChromeCast;

public class ChromeCastProxy implements DeviceController {
	private static final Logger logger = LoggerFactory.getLogger(DeviceController.class);

	private Device device;
	private WebSocketSession session;
	private ChromeCast chromeCast;
	private LocalDateTime heartbeatTime;
	private Object content;
	
	public ChromeCastProxy(Device device) {
		this(device, null, null);
	}
	
	public ChromeCastProxy(Device device, ChromeCast chromeCast) {
		this(device, chromeCast, null);
	}

	public ChromeCastProxy(Device device, ChromeCast chromeCast, WebSocketSession session) {
		super();
		this.device = device;
		this.session = session;
		this.chromeCast = chromeCast;
	}

	public Long getDeviceId() {
		return device.getDeviceId();
	}

	public WebSocketSession getSession() {
		return session;
	}

	public void setSession(WebSocketSession session) {
		this.session = session;
	}

	public boolean isHealth() {
		return session != null;
	}
	
	public ChromeCast getChromeCast() {
		return chromeCast;
	}

	public void setChromeCast(ChromeCast chromeCast) {
		this.chromeCast = chromeCast;
	}

	public void setHeartbeatTime(LocalDateTime heartbeatTime) {
		this.heartbeatTime = heartbeatTime;
	}

	public LocalDateTime getHeartbeatTime() {
		return heartbeatTime;
	}
	
	public void setDevice(Device device) {
		this.device = device;
	}
	
	@Override
	public Device getDevice() {
		return device;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}
	
	public boolean isContent() {
		return content != null && content instanceof Content;
	}
	
	public boolean isPlaylist() {
		return content != null && content instanceof Playlist;
	}

	@Override
	public void control(String key, Object value) {
		if(chromeCast != null) {
			try{
			switch(key) {
				case "play":
					boolean play = CommonUtils.convertToBoolean(value);
					if(play) {
						chromeCast.play();
					} else {
						PlayerStopCastRequest request = new PlayerStopCastRequest(device.getDeviceId());
						chromeCast.send(CommonConstants.namespace, request);
					}
				break;
				case "pause":
					boolean pause = CommonUtils.convertToBoolean(value);
					if(pause) {
						chromeCast.pause();
					} else {
						chromeCast.play();
					}
				break;
				case "volume":
					float volume = CommonUtils.convertToFloat(value);
					chromeCast.setVolume(volume);
				break;
				case "mute":
					boolean mute = CommonUtils.convertToBoolean(value);
					chromeCast.setMuted(mute);
				break;
			}
			} catch (IOException ex) {
				logger.error("Cannot control a command: " + key + ", " + value);
			}
		}
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((device.getDeviceId() == null) ? 0 : device.getDeviceId().hashCode());
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
		ChromeCastProxy other = (ChromeCastProxy) obj;
		if (device.getDeviceId() == null) {
			if (other.device.getDeviceId() != null)
				return false;
		} else if (!device.getDeviceId().equals(other.device.getDeviceId()))
			return false;
		return true;
	}

}
