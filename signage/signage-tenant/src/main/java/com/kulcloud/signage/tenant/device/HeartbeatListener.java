package com.kulcloud.signage.tenant.device;

import com.kulcloud.signage.tenant.data.entity.Device;

public interface HeartbeatListener {

	public void health(Device device, boolean health);
	
}
