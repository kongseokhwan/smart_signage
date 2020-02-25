package com.kulcloud.signage.tenant.ui.page;

import com.kulcloud.signage.tenant.device.DeviceMeta;

public interface DeviceMetaChangeListener {

	public void changeValue(DeviceMeta meta, String key, Object oldValue, Object newValue);
}
