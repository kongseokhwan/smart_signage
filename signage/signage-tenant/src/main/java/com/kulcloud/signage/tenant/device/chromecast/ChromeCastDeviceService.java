package com.kulcloud.signage.tenant.device.chromecast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kulcloud.signage.commons.enums.ContentType;
import com.kulcloud.signage.commons.enums.DeviceType;
import com.kulcloud.signage.commons.enums.ValueType;
import com.kulcloud.signage.commons.utils.CommonConstants;
import com.kulcloud.signage.commons.utils.CommonUtils;
import com.kulcloud.signage.tenant.data.entity.Content;
import com.kulcloud.signage.tenant.data.entity.Desktop;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.data.entity.Playlist;
import com.kulcloud.signage.tenant.data.entity.Schedule;
import com.kulcloud.signage.tenant.device.DeviceChangedListener;
import com.kulcloud.signage.tenant.device.DeviceController;
import com.kulcloud.signage.tenant.device.DeviceDiscoveryListener;
import com.kulcloud.signage.tenant.device.DeviceMeta;
import com.kulcloud.signage.tenant.device.DeviceService;
import com.kulcloud.signage.tenant.device.chromecast.request.ContentCastRequest;

import su.litvak.chromecast.api.v2.ChromeCast;
import su.litvak.chromecast.api.v2.ChromeCasts;

@Service
public class ChromeCastDeviceService implements DeviceService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	private boolean discovery = false;
	private InetAddress discoveryIp = null;
	private final Map<String, List<DeviceMeta>> deviceMetaForProperties = new HashMap<>();;
    
	@Value("${com.kulcloud.signage.chromecast.ip:}")
    private String chromecastIp;
	
    @Autowired
    private ChromeCastRepository castRepo;
	
	@PostConstruct
	private void init() {
		try {
			if(StringUtils.isEmpty(chromecastIp)) {
				chromecastIp = CommonUtils.getOutboundIp();
			}
			
			discoveryIp = InetAddress.getByName(chromecastIp);
		} catch (UnknownHostException e) {
			discoveryIp = null;
		}
		ChromeCasts.registerListener(castRepo);
		initDeviceMetaForProperties();
	}

	@Override
	public DeviceType getSupportedDeviceType() {
		return DeviceType.chromecast;
	}

	@Override
	public void addDeviceDiscoveryListener(DeviceDiscoveryListener listener) {
		castRepo.addDeviceDiscoveryListener(listener);
	}

	@Override
	public void removeDeviceDiscoveryListener(DeviceDiscoveryListener listener) {
		castRepo.removeDeviceDiscoveryListener(listener);
	}

	@Override
	public void addDeviceChangedListener(DeviceChangedListener listener) {
		castRepo.addDeviceChangedListener(listener);
	}

	@Override
	public void removeDeviceChangedListener(DeviceChangedListener listener) {
		castRepo.removeDeviceChangedListener(listener);
	}

	@Override
	public void startDiscovery() {
		try {
			if(discovery) {
				ChromeCasts.restartDiscovery(discoveryIp);
			} else {
				ChromeCasts.startDiscovery(discoveryIp);
				discovery = true;
			}
		} catch (IOException e) {
			logger.error("Cannot start the discovery.", e);
		}
	}

	@Override
	public void stopDiscovery() {
		if(discovery) {
			try {
				ChromeCasts.stopDiscovery();
				discovery = false;
			} catch (IOException e) {
				logger.error("Failure! Stop the discovery", e);
			}
		}
	}

	@Override
	public boolean sendMedia(Device device, String url) {
		ChromeCast chromeCast;
		try {
			chromeCast = castRepo.getChromeCast(device);
		} catch (IOException e1) {
			logger.error("Cannot launch a receiver app in chromecast: " + device.getDeviceId());
			return false;
		}
		
		return sendMedia(chromeCast, url);
	}
	
	public boolean sendMedia(ChromeCast chromeCast, String url) {
		try {
			if(url.indexOf(CommonConstants.SCHEME) > 0) {
				chromeCast.load(url);
			} else {
				String wowzaUrl = castRepo.getWowzaUrlPrefix() + 
						(url.startsWith(CommonConstants.SLASH) ? 
								url : CommonConstants.SLASH + url);
				chromeCast.load(wowzaUrl);
			}
			
			return true;
		} catch (IOException e) {
			logger.error("Cannot send a content to chromecast: " + chromeCast.getTitle());
			return false;
		}
	}

	@Override
	public boolean send(Device device, Content content) {
		if(content == null) {
			return false;
		}

		ChromeCast chromeCast;
		try {
			chromeCast = castRepo.getChromeCast(device);
		} catch (IOException e1) {
			logger.error("Cannot launch a receiver app in chromecast: " + device.getDeviceId());
			return false;
		}
		
		switch(ContentType.valueOf(content.getType())) {
		case live:
		case vod:
			if(!sendMedia(chromeCast, content.getUrl())) {
				return false;
			}
			break;
		case html:
		case link:
		case slide:
			try {
				chromeCast.send(castRepo.getNamespace(), new ContentCastRequest(content.getUrl()));
			} catch (IOException e) {
				logger.error("Cannot send a content to chromecast: " + device.getDeviceId());
				return false;
			}
			break;
		default:
			return false;
		
		}
		
		castRepo.setSendedContentToChromeCastProxy(device, content);
		return true;
	}

	@Override
	public boolean send(Device device, Playlist playlist) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean send(Schedule schedule) {
		// TODO Auto-generated method stub
		return false;
	}

	@PreDestroy
	private void destroy() {
		stopDiscovery();
	}

	@Override
	public boolean send(Device device, Desktop desktop) {
		try {
			ChromeCast chromeCast = castRepo.getChromeCast(device);
			chromeCast.send(castRepo.getNamespace(), new ContentCastRequest(castRepo.getDesktopUrl() + "?desktopId=" + desktop.getDesktopId()));
			return true;
		} catch (IOException e1) {
			logger.error("Cannot send a desktop to chromecast: " + device.getSerialNumber());
			return false;
		}
	}

	@Override
	public DeviceController getDeviceController(Device device) {
		return castRepo.getChromeCastProxy(device);
	}

	@Override
	public List<DeviceMeta> getDeviceMetaForProperty(String property) {
		return deviceMetaForProperties.get(property);
	}
	
    private void initDeviceMetaForProperties() {
		List<DeviceMeta> config = new ArrayList<>();
		List<DeviceMeta> control = new ArrayList<>();
		deviceMetaForProperties.put("config", config);
		deviceMetaForProperties.put("control", control);
		
		control.add(new DeviceMeta("play", ValueType.BOOLEAN.name()));
		control.add(new DeviceMeta("pause", ValueType.BOOLEAN.name()));
		control.add(new DeviceMeta("volume", ValueType.NUMBER.name()));
		control.add(new DeviceMeta("mute", ValueType.BOOLEAN.name()));
	}
}
