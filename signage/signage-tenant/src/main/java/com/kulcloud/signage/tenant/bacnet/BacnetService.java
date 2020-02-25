package com.kulcloud.signage.tenant.bacnet;

import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kulcloud.signage.commons.enums.DeviceType;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.device.DeviceController;
import com.kulcloud.signage.tenant.device.DeviceMeta;
import com.kulcloud.signage.tenant.device.DeviceService;
import com.kulcloud.signage.tenant.device.DeviceServiceManager;
import com.serotonin.bacnet4j.LocalDevice;
import com.serotonin.bacnet4j.exception.BACnetServiceException;
import com.serotonin.bacnet4j.npdu.ip.IpNetwork;
import com.serotonin.bacnet4j.npdu.ip.IpNetworkBuilder;
import com.serotonin.bacnet4j.transport.DefaultTransport;
import com.serotonin.bacnet4j.transport.Transport;

@Service
public class BacnetService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Value("${bacnet.deviceNumber:-1}")
	private int deviceNumber;
	@Value("${bacnet.subnet.ip:}")
	private String subnet;
	@Value("${bacnet.subnet.mask:24}")
	private short mask;
	@Value("${bacnet.broadcast:255.255.255.0}")
	private String broadcast;
	
	@Autowired
	private DeviceServiceManager deviceService;
	
	private LocalDevice localDevice;
	
	@PostConstruct
	private void init() {
		if(deviceNumber < 1) {
			deviceNumber = 10000 + (int) ( Math.random() * 10000);
		}
		
		IpNetwork network;
		if(!StringUtils.isEmpty(subnet)) {
			network = new IpNetworkBuilder().withSubnet(subnet, mask).build();
		} else if (!StringUtils.isEmpty(broadcast)) {
			network = new IpNetworkBuilder().withBroadcast(broadcast, mask).build();
		} else {
			network = null;
			logger.warn("For BACnet service, \"bacnet.subnet.ip\" or \"bacnet.broadcast\" should be set.");
			return;
		}
		
		Transport transport = new DefaultTransport(network);
		localDevice = new LocalDevice(deviceNumber, transport);
		initBACnetObject();
		logger.info("BACnetService started on port: " + network.getPort());
	}
	
	private void initBACnetObject() {
		List<Device> devices = deviceService.findDevices();
		
		List<DeviceMeta> metaList;
		for (Device device : devices) {
			metaList = deviceService.getDeviceMeta(device.getType(), DeviceMeta.control);
			for (DeviceMeta meta : metaList) {
				DeviceBACnetObject object = new DeviceBACnetObject(localDevice, device, meta);
				try {
					localDevice.addObject(object);
					object.addListener((DeviceBACnetObject deviceObject, Object oldValue, Object newValue) -> {
						DeviceController controller = getDeviceController(deviceObject.getDeviceId());
						if(controller != null) {
							controller.control(deviceObject.getKey(), newValue);
						}	
					});
					deviceService.addDeviceChangedListener(object);
				} catch (BACnetServiceException e) {
					logger.warn("Cannot add a bacnet object in local device: " + object.getObjectName());
				}
			}
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
