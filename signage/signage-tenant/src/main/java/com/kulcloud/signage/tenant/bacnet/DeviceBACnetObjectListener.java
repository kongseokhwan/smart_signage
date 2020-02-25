package com.kulcloud.signage.tenant.bacnet;

public interface DeviceBACnetObjectListener {

	void propertyChange(DeviceBACnetObject object, Object oldValue, Object newValue);
	
}
