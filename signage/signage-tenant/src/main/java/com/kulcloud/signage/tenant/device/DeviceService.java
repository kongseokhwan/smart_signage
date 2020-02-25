package com.kulcloud.signage.tenant.device;

import java.util.List;

import com.kulcloud.signage.commons.enums.DeviceType;
import com.kulcloud.signage.tenant.data.entity.Content;
import com.kulcloud.signage.tenant.data.entity.Desktop;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.data.entity.Playlist;
import com.kulcloud.signage.tenant.data.entity.Schedule;

public interface DeviceService {

	public DeviceType getSupportedDeviceType();
	public void addDeviceDiscoveryListener(DeviceDiscoveryListener listener);
	public void removeDeviceDiscoveryListener(DeviceDiscoveryListener listener);
	public void addDeviceChangedListener(DeviceChangedListener listener);
	public void removeDeviceChangedListener(DeviceChangedListener listener);
	public void startDiscovery();
	public void stopDiscovery();
	public boolean sendMedia(Device device, String url);
	public boolean send(Device device, Content content);
	public boolean send(Device device, Playlist playlist);
	public boolean send(Schedule schedule);
	public boolean send(Device device, Desktop desktop);
	public DeviceController getDeviceController(Device device);
	public List<DeviceMeta> getDeviceMetaForProperty(String property);
	
}
