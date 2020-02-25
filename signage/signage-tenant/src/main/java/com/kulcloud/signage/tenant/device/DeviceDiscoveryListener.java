package com.kulcloud.signage.tenant.device;

import com.kulcloud.signage.tenant.data.entity.Device;

public interface DeviceDiscoveryListener {

	public void iam(Device device);
	public void bye(Device device);
}
