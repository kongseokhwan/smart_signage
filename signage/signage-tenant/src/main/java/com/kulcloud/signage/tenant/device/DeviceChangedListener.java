package com.kulcloud.signage.tenant.device;

import com.kulcloud.signage.tenant.data.entity.Device;

public interface DeviceChangedListener {

	public void changeDeviceStatus(Device device);
}
