package com.kulcloud.signage.tenant.device;

import com.kulcloud.signage.tenant.data.entity.Device;

public interface DeviceController {

	public Device getDevice();
	public void control(String key, Object value);
	
}
