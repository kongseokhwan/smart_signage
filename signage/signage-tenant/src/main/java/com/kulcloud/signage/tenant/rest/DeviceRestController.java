package com.kulcloud.signage.tenant.rest;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulcloud.signage.commons.dto.RestResult;
import com.kulcloud.signage.commons.enums.DeviceType;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.device.DeviceController;
import com.kulcloud.signage.tenant.device.DeviceMeta;
import com.kulcloud.signage.tenant.device.DeviceService;
import com.kulcloud.signage.tenant.device.DeviceServiceManager;

@RestController
@RequestMapping(path = "/device")
public class DeviceRestController {

	@Autowired
	private DeviceServiceManager deviceService;
	
	@GetMapping(path = "/list")
	public List<Device> getList() {
		return deviceService.findDevices();
	}
	
	@GetMapping(path = "/meta/{type}/{property}")
	public List<DeviceMeta> getDeviceMeta(@PathVariable("type") String deviceType, @PathVariable("property") String property) {
		return deviceService.getDeviceMeta(deviceType, property);
	}
	
	@PostMapping(path = "/{deviceId}/sendUrl")
	public RestResult sendUrl(@PathVariable Long deviceId, @RequestBody Map<String, Object> payload) {
		if(payload.containsKey("url")) {
			Device device = deviceService.findDevice(deviceId);
			if(device != null) {
				deviceService.sendMedia(device, payload.get("url").toString());
				return new RestResult();
			} else {
				return new RestResult(false, "Cannot find a device: " + deviceId, null);
			}
		} else {
			return new RestResult(new IllegalArgumentException());
		}
	}
	
	@PostMapping(path = "/{deviceId}/control")
	public RestResult control(@PathVariable Long deviceId, @RequestBody Map<String, Object> payload) {
		if(payload.containsKey("key")) {
			DeviceController controller = getDeviceController(deviceId);
			if(controller != null) {
				controller.control(payload.get("key").toString(), payload.get("value"));
				return new RestResult();
			} else {
				return new RestResult(false, "Cannot find a device: " + deviceId, null);
			}
		} else {
			return new RestResult(new IllegalArgumentException());
		}
	}
	
	private DeviceController getDeviceController(Long deviceId) {
		Device device = deviceService.findDevice(deviceId);
		if(device == null || device.getDeviceId() != null) {
			DeviceType type = DeviceType.valueOf(device.getType());
			if(type != null) {
				DeviceService service = deviceService.getDeviceService(type);
				return service.getDeviceController(device);
			}
		}

		return null;
	}
}
