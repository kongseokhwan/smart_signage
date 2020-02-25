package com.kulcloud.signage.tenant.device;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.kulcloud.signage.commons.enums.DeviceType;
import com.kulcloud.signage.tenant.data.CameraRepository;
import com.kulcloud.signage.tenant.data.DeviceRepository;
import com.kulcloud.signage.tenant.data.entity.Camera;
import com.kulcloud.signage.tenant.data.entity.Content;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.data.entity.Playlist;
import com.kulcloud.signage.tenant.data.entity.Schedule;

@Component
public class DeviceServiceManager {

	@Autowired
	private List<DeviceService> services;
	@Autowired
	private DeviceRepository deviceRepo;
	@Autowired
	private CameraRepository cameraRepo;
	@Autowired
	private AsyncTaskExecutor executor;
	
	private Map<String, DeviceService> indexDeviceServices = Collections.synchronizedMap(new HashMap<>());
	
	@PostConstruct
	private void init() {
		for (DeviceService service : services) {
			indexDeviceServices.put(service.getSupportedDeviceType().name(), service);
		}
	}
	
	public DeviceService getDeviceService(DeviceType type) {
		for (DeviceService service : services) {
			if(service.getSupportedDeviceType().equals(type)) {
				return service;
			}
		}
		
		return null;
	}
	
	public void addDeviceDiscoveryListener(DeviceDiscoveryListener listener) {
		for (DeviceService service : services) {
			service.addDeviceDiscoveryListener(listener);
		}
	}
	
	public void removeDeviceDiscoveryListener(DeviceDiscoveryListener listener) {
		for (DeviceService deviceService : services) {
			deviceService.removeDeviceDiscoveryListener(listener);
		}
	}
	
	public void addDeviceChangedListener(DeviceChangedListener listener) {
		for (DeviceService service : services) {
			service.addDeviceChangedListener(listener);
		}
	}
	
	public void removeDeviceChangedListener(DeviceChangedListener listener) {
		for (DeviceService deviceService : services) {
			deviceService.removeDeviceChangedListener(listener);
		}
	}
	
	public void startDiscovery() {
		for (DeviceService deviceService : services) {
			deviceService.startDiscovery();
		}
	}
	
	public void stopDiscoery() {
		for (DeviceService deviceService : services) {
			deviceService.stopDiscovery();
		}
	}
	
	public void sendMedia(Device device, String url) {
		if(!StringUtils.isEmpty(device.getType())) {
			DeviceService deviceService = indexDeviceServices.get(device.getType());
			if(deviceService != null) {
				deviceService.sendMedia(device, url);
			}
		}
	}
	
	public void send(Device device, Content content) {
		if(!StringUtils.isEmpty(device.getType())) {
			DeviceService deviceService = indexDeviceServices.get(device.getType());
			if(deviceService != null) {
				deviceService.send(device, content);
			}
		}
	}
	
	@Async
	public void asyncSend(Device device, Content content) {
		send(device, content);
	}
	
	@Async
	public void asyncSendMedia(Device device, String url) {
		sendMedia(device, url);
	}
	
	public void send(Device device, Playlist playlist) {
		if(!StringUtils.isEmpty(device.getType())) {
			DeviceService deviceService = indexDeviceServices.get(device.getType());
			if(deviceService != null) {
				deviceService.send(device, playlist);
			}
		}
	}
	
	@Async
	public void asyncSend(Device device, Playlist playlist) {
		send(device, playlist);
	}
	
	public void send(Schedule schedule) {
		for (DeviceService deviceService : services) {
			deviceService.send(schedule);
		}
	}
	
	@Async
	public void asyncSend(Schedule schedule) {
		for (DeviceService deviceService : services) {
			executor.execute(() -> deviceService.send(schedule));
		}
	}
	
	public Device findDevice(Long deviceId) {
		return deviceRepo.findById(deviceId).orElse(null);
	}
	
	public void saveDevice(Device device) {
		deviceRepo.save(device);
	}
	
	public void deleteDevice(Device device) {
		List<Camera> cameras = cameraRepo.findByDeviceDeviceId(device.getDeviceId());
		for (Camera camera : cameras) {
			camera.setDevice(null);
		}
		cameraRepo.saveAll(cameras);
		deviceRepo.delete(device);
	}
	
	public void deleteDevice(Long deviceId) {
		Optional<Device> deviceOpt = deviceRepo.findById(deviceId);
		if(deviceOpt.isPresent()) {
			deleteDevice(deviceOpt.get());
		}
	}
	
	public List<Device> findDevices() {
		return deviceRepo.findAll();
	}
	
	public List<DeviceMeta> getDeviceMeta(DeviceType type, String property) {
		return getDeviceMeta(type.name(), property);
	}
	
	public List<DeviceMeta> getDeviceMeta(String type, String property) {
		DeviceService deviceService = indexDeviceServices.get(type);
		if(deviceService == null) {
			return Collections.emptyList();
		} else {
			return deviceService.getDeviceMetaForProperty(property);
		}
	}
	
	public Camera findCamera(Long cameraId) {
		return cameraRepo.findById(cameraId).orElse(null);
	}
	
	public void saveCamera(Camera camera) {
		cameraRepo.save(camera);
	}
	
	public void deleteCamera(Camera camera) {
		cameraRepo.delete(camera);
	}
	
	public void deleteCamera(Long cameraId) {
		cameraRepo.deleteById(cameraId);
	}
	
	public List<Camera> findCameras() {
		return cameraRepo.findAll();
	}
}
