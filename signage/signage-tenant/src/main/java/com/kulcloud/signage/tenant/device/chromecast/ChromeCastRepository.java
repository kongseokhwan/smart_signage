package com.kulcloud.signage.tenant.device.chromecast;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.kulcloud.signage.commons.enums.DeviceType;
import com.kulcloud.signage.commons.utils.CommonConstants;
import com.kulcloud.signage.commons.utils.CommonUtils;
import com.kulcloud.signage.tenant.data.DeviceRepository;
import com.kulcloud.signage.tenant.data.entity.Content;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.data.entity.Playlist;
import com.kulcloud.signage.tenant.device.DeviceChangedListener;
import com.kulcloud.signage.tenant.device.DeviceDiscoveryListener;
import com.kulcloud.signage.tenant.device.chromecast.request.WebSocketCastRequest;
import com.kulcloud.signage.tenant.device.message.DeviceMessage;
import com.kulcloud.signage.tenant.device.message.DeviceMessageType;

import su.litvak.chromecast.api.v2.ChromeCast;
import su.litvak.chromecast.api.v2.ChromeCastsListener;
import su.litvak.chromecast.api.v2.Status;

@Component
public class ChromeCastRepository extends TextWebSocketHandler implements ChromeCastsListener {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private Map<Long, ChromeCastProxy> chromecasts = Collections.synchronizedMap(new HashMap<>());
	private Map<WebSocketSession, ChromeCastProxy> sessions = Collections.synchronizedMap(new HashMap<>());
	
	private List<DeviceDiscoveryListener> discoveryListeners = new ArrayList<>();
	private List<DeviceChangedListener> changedListeners = new ArrayList<>();
	
	@Autowired
    private DeviceRepository deviceRepo;
	@Autowired
	private AsyncTaskExecutor thread;
	
	@Value("${wowza.stream.ssl:false}")
    private boolean wowzaSsl;
    @Value("${wowza.host:localhost}")
    private String wowzaHost;
    @Value("${wowza.stream.port:1935}")
    private int wowzaPort;
    
    private String wowzaUrlPrefix;
    
    @Value("${com.kulcloud.signage.chromecast.ip:}")
    private String chromecastIp;
	@Value("${com.kulcloud.signage.chromecast.appId:5AF7AE49}")
    private String appId;
	@Value("${server.port:8080}")
    private String serverPort;
    @Value("${server.ssl.enabled:false}")
    private boolean serverSsl;
    private String wsUrl;
    private String desktopUrl;
    
	@PostConstruct
    private void init() {
		if(StringUtils.isEmpty(chromecastIp)) {
			chromecastIp = CommonUtils.getOutboundIp();
		}
		
		wowzaUrlPrefix = (wowzaSsl ? CommonConstants.HTTPS : CommonConstants.HTTP) + 
    			CommonConstants.SCHEME + wowzaHost + CommonConstants.COLON + wowzaPort;
    	wsUrl = (serverSsl? CommonConstants.HTTPS : CommonConstants.HTTP) + CommonConstants.SCHEME + chromecastIp + CommonConstants.COLON + serverPort + ChromeCastWebConfig.castHandlerPath;
    	desktopUrl = (serverSsl? CommonConstants.HTTPS : CommonConstants.HTTP) + CommonConstants.SCHEME + chromecastIp + CommonConstants.COLON + serverPort + "/desktop/index.html";
		
		deviceRepo.updateHealth(false, DeviceType.chromecast.name());
		connectDevices();
    }
	
	private void connectDevices() {
		List<Device> devices = deviceRepo.findByType(DeviceType.chromecast.name());
		for (Device device : devices) {
			if(!StringUtils.isEmpty(device.getIpAddress())) {
				thread.execute(() -> connectDevice(device));
			}
		}
	}
	
	private void connectDevice(Device device) {
		try {
			ChromeCast chromeCast = getChromeCast(device);
			sendWebSocketCastRequest(chromeCast, device.getDeviceId());
		} catch (IOException e) {
			logger.warn("Cannot connect a ChromeCast: " + device.getIpAddress());
		}
	}
    
	@Override
	public void newChromeCastDiscovered(ChromeCast chromeCast) {
		Device device = saveDeviceForChromeCast(chromeCast, true);
		registerChromeCastProxy(device, chromeCast);
		
		device = deviceRepo.save(device);
		long deviceId = device.getDeviceId();
		chromeCast.setAutoReconnect(true);
		chromeCast.registerConnectionListener(e -> {
			Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
			if(deviceOpt.isPresent()) {
				Device srcDevice = deviceOpt.get();
				srcDevice.setHealth(e.isConnected());
				deviceRepo.save(srcDevice);
			}
		});
		
		sendWebSocketCastRequest(chromeCast, deviceId);
		
		for (DeviceDiscoveryListener listener : discoveryListeners) {
			listener.iam(device);
		}
	}
	
	private void sendWebSocketCastRequest(ChromeCast chromeCast, long deviceId) {
		try {
			chromeCast.send(CommonConstants.namespace, new WebSocketCastRequest(deviceId, wsUrl));
		} catch (IOException e) {
			logger.error("Cannot send a websocket url to ChromeCast: " + chromeCast.getAddress());
		}
	}

	@Override
	public void chromeCastRemoved(ChromeCast chromeCast) {
		Device device = saveDeviceForChromeCast(chromeCast, false);
		registerChromeCastProxy(device, chromeCast);
		
		for (DeviceDiscoveryListener listener : discoveryListeners) {
			listener.bye(device);
		}
	}
	
	public void registerChromeCastProxy(Device device, ChromeCast chromeCast) {
		ChromeCastProxy proxy = chromecasts.get(device.getDeviceId());
		if(proxy == null) {
			proxy = new ChromeCastProxy(device, chromeCast);
			chromecasts.put(device.getDeviceId(), proxy);
		} else {
			proxy.setChromeCast(chromeCast);
		}
	}
	
	public ChromeCast getChromeCast(Device device) throws IOException {
		ChromeCastProxy chromeCastProxy = getChromeCastProxy(device);
		ChromeCast chromeCast = chromeCastProxy.getChromeCast();
		Status status = chromeCast.getStatus();
		if (chromeCast.isAppAvailable(appId) && !status.isAppRunning(appId)) {
		  chromeCast.launchApp(appId);
		  sendWebSocketCastRequest(chromeCast, device.getDeviceId());
		}
		
		return chromeCast;
	}
	
	public ChromeCastProxy getChromeCastProxy(Device device) {
		ChromeCastProxy chromeCastProxy = chromecasts.get(device.getDeviceId());
		if(chromeCastProxy == null) {
			chromeCastProxy = new ChromeCastProxy(device, new ChromeCast(device.getIpAddress()));
			chromecasts.put(device.getDeviceId(), chromeCastProxy);
		}
		
		return chromeCastProxy;
	}
	
	public void setSendedContentToChromeCastProxy(Device device, Content content) {
		ChromeCastProxy chromeCastProxy = getChromeCastProxy(device);
		chromeCastProxy.setContent(content);
	}
	
	public void setSendedPlaylistToChromeCastProxy(Device device, Playlist playlist) {
		ChromeCastProxy chromeCastProxy = getChromeCastProxy(device);
		chromeCastProxy.setContent(playlist);
	}
	
	public Device saveDeviceForChromeCast(ChromeCast chromeCast, boolean health) {
		Optional<Device> optional = deviceRepo.findByTypeAndIpAddressAndSerialNumber(DeviceType.chromecast.name(), chromeCast.getAddress(), chromeCast.getName());
		Device device;
		if(optional.isPresent()) {
			device = optional.get();
			device.setHealth(health);
		} else {
			device = new Device();
			device.setHealth(health);
			device.setIpAddress(chromeCast.getAddress());
			device.setSerialNumber(chromeCast.getName());
			device.setTitle(chromeCast.getTitle());
			device.setType(DeviceType.chromecast.name());
		}
		
		return deviceRepo.save(device);
	}
	
	public void addDeviceDiscoveryListener(DeviceDiscoveryListener listener) {
		if(listener != null && !discoveryListeners.contains(listener)) {
			this.discoveryListeners.add(listener);
		}
	}

	public void removeDeviceDiscoveryListener(DeviceDiscoveryListener listener) {
		if(listener != null && discoveryListeners.contains(listener)) {
			discoveryListeners.remove(listener);
		}
	}
	
	public void addDeviceChangedListener(DeviceChangedListener listener) {
		if(listener != null && !changedListeners.contains(listener)) {
			this.changedListeners.add(listener);
		}
	}

	public void removeDeviceChangedListener(DeviceChangedListener listener) {
		if(listener != null && changedListeners.contains(listener)) {
			changedListeners.remove(listener);
		}
	}

	public String getNamespace() {
		return CommonConstants.namespace;
	}

	public String getWowzaUrlPrefix() {
		return wowzaUrlPrefix;
	}

	public String getAppId() {
		return appId;
	}

	public String getWsUrl() {
		return wsUrl;
	}

	public String getDesktopUrl() {
		return desktopUrl;
	}
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		super.handleTextMessage(session, message);
		
		String payload = message.getPayload();
		DeviceMessage castMessage = CommonConstants.mapper.readValue(payload, DeviceMessage.class);
		ChromeCastProxy castProxy = chromecasts.get(castMessage.getDeviceId());
		Device device = null;
		if(castProxy == null) {
			Optional<Device> deviceOpt = deviceRepo.findById(castMessage.getDeviceId());
			if(deviceOpt.isPresent()) {
				device = deviceOpt.get();
				castProxy = new ChromeCastProxy(device, new ChromeCast(device.getIpAddress()));
				chromecasts.put(device.getDeviceId(), castProxy);
			}
		}
		
		if(castProxy == null) {
			logger.warn("Cannot find a device for " + castMessage.getDeviceId());
			session.close();
			return;
		}
		
		if(device == null) {
			device = deviceRepo.findById(castProxy.getDeviceId()).orElse(null);
			if(device == null) {
				logger.warn("Cannot find a device for " + castMessage.getDeviceId());
				chromecasts.remove(castMessage.getDeviceId());
				castProxy.setSession(null);
				sessions.remove(session);
				session.close();
				return;
			}
		}
		
		ChromeCastProxy castProxyForSession = sessions.get(session);
		if(castProxyForSession == null || !castProxyForSession.equals(castProxy)) {
			castProxy.setSession(session);
			sessions.put(session, castProxy);
			device.setHealth(castProxy.isHealth());
			deviceRepo.save(device);
			if(castProxyForSession != null) {
				castProxyForSession.setSession(null);
			} else {
				castProxyForSession = castProxy;
			}
		}
		
		castProxyForSession.setHeartbeatTime(LocalDateTime.now());
		handleMessage(castProxy, castMessage);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		ChromeCastProxy cast = sessions.get(session);
		if(cast != null) {
			cast.setSession(null);
			sessions.remove(session);
			Optional<Device> deviceOpt = deviceRepo.findById(cast.getDeviceId());
			if(deviceOpt.isPresent()) {
				Device device = deviceOpt.get();
				device.setHealth(cast.isHealth());
				device.setControls(new HashMap<>());
				deviceRepo.save(device);
				cast.setDevice(device);
				fireDeviceChangedEvent(device);
			}
		}
		
		super.afterConnectionClosed(session, status);
	}
	
	private void handleMessage(ChromeCastProxy cast, DeviceMessage message) {
		if(!StringUtils.isEmpty(message.getType())) {
			DeviceMessageType type = DeviceMessageType.valueOf(message.getType());
			switch(type) {
			case STATUS:
				@SuppressWarnings("unchecked")
				Map<String, Object> status = CommonConstants.mapper.convertValue(message.getMessage(), Map.class);
				Device device = deviceRepo.findById(cast.getDeviceId()).orElse(null);
				if(device != null) {
					device.getControls().putAll(status);
					deviceRepo.save(device);
					cast.setDevice(device);
					fireDeviceChangedEvent(device);
				}
				break;
			}
		}
	}

	private void fireDeviceChangedEvent(Device device) {
		for (DeviceChangedListener listener : changedListeners) {
			thread.execute(() -> listener.changeDeviceStatus(device));
		}
	}
}
