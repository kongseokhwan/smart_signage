package com.kulcloud.signage.tenant.device.rpi;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kulcloud.signage.commons.enums.DeviceType;
import com.kulcloud.signage.commons.utils.CommonConstants;
import com.kulcloud.signage.tenant.data.DeviceRepository;
import com.kulcloud.signage.tenant.data.entity.Content;
import com.kulcloud.signage.tenant.data.entity.Desktop;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.data.entity.Playlist;
import com.kulcloud.signage.tenant.data.entity.Schedule;
import com.kulcloud.signage.tenant.device.DeviceChangedListener;
import com.kulcloud.signage.tenant.device.DeviceController;
import com.kulcloud.signage.tenant.device.DeviceDiscoveryListener;
import com.kulcloud.signage.tenant.device.DeviceMeta;
import com.kulcloud.signage.tenant.device.DeviceService;

@Service
public class RpiDeviceService implements DeviceService {
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private boolean running;
    private List<DeviceDiscoveryListener> discoveryListeners = new ArrayList<>();
    
    @Autowired
    private DeviceRepository deviceRepo;
    @Autowired
	@Qualifier("asyncExecutor")
	private Executor async;
    private ExecutorService thread;
    @Autowired
    private RpiStompProperties stompProperties;
	
	@Value("${com.kulcloud.signage.rpi.broadcast.port: 4200}")
	private int broadcastPort;
    
	@Override
	public DeviceType getSupportedDeviceType() {
		return DeviceType.rpi;
	}

	@Override
	public void addDeviceDiscoveryListener(DeviceDiscoveryListener listener) {
		if(listener != null && !discoveryListeners.contains(listener)) {
			discoveryListeners.add(listener);
		}
	}
	
	@Override
	public void removeDeviceDiscoveryListener(DeviceDiscoveryListener listener) {
		if(listener != null && discoveryListeners.contains(listener)) {
			discoveryListeners.remove(listener);
		}
	}

	@Override
	public void startDiscovery() {
		// if(thread == null) {
		// 	thread =  Executors.newSingleThreadExecutor();
		// 	thread.execute(() -> {
		// 		DatagramSocket socket;
		// 		try {
		// 			socket = new DatagramSocket(broadcastPort);
		// 		} catch (SocketException e) {
		// 			logger.error("Cannot execute a discovery socket.", e);
		// 			return;
		// 		}
				
		// 		try {
		// 			byte[] buf = new byte[2* 1024 * 1024];
		// 			running = true;
		// 			while (running) {
		// 				DatagramPacket packet 
		// 	              = new DatagramPacket(buf, buf.length);
		// 	            try {
		// 					socket.receive(packet);
		// 				} catch (IOException e) {
		// 					logger.error(e.getMessage(), e);
		// 					continue;
		// 				}
			            
		// 	            InetAddress address = packet.getAddress();
		// 	            int port = packet.getPort();
		// 	            String received = new String(packet.getData(), 0, packet.getLength());
		// 	            async.execute(() -> {
		// 		            DatagramPacket reply;
		// 		            try {
		// 		            	Device device = parseDevice(address, received);
		// 		            	iam(device);
		// 		            	Map<String, Object> ok = new HashMap<>();
		// 		            	ok.put("deviceId", device.getDeviceId());
		// 		            	ok.put("stomp", stompProperties);
		// 		            	byte[] okBytes = CommonConstants.mapper.writeValueAsBytes(ok);
		// 		            	reply = new DatagramPacket(okBytes, okBytes.length, address, port);
		// 		            } catch (Throwable e) {
		// 		            	logger.error("Cannot parse a dicovery message.", e);
		// 		            	reply = new DatagramPacket(CommonConstants.FALSE_BYTES, CommonConstants.FALSE_BYTES.length, address, port);
		// 			        }
				            
		// 		            try {
		// 						socket.send(reply);
		// 					} catch (IOException e) {
		// 						logger.error("Cannot send a ok to " + address, e);
		// 					}
		// 	            });
		// 	        }
		// 		} finally {
		// 			socket.close();
		// 		}
		// 	});
		// }
	}

	@Override
	public void stopDiscovery() {
		// running = false;
		// if(thread != null) {
		// 	thread.shutdownNow();
		// 	try {
		// 		thread.awaitTermination(5, TimeUnit.SECONDS);
		// 	} catch (InterruptedException ignored) {}
		// 	thread = null;
		// }
	}

	private Device parseDevice(InetAddress address, String receive) {
		Device device = CommonConstants.mapper.convertValue(receive, Device.class);
		if(StringUtils.isEmpty(device.getIpAddress())) {
			device.setIpAddress(address.getHostAddress());
		}
		
		if(StringUtils.isEmpty(device.getType())) {
			device.setType(DeviceType.rpi.name());
		}
		
		if(device.getDeviceId() != null) {
			Optional<Device> optional = deviceRepo.findById(device.getDeviceId());
			if(optional.isPresent()) {
				return optional.get();
			}
		}
		
		Optional<Device> devices = deviceRepo.findByTypeAndIpAddressAndSerialNumber(device.getType(), device.getIpAddress(), device.getSerialNumber());
		if(devices.isPresent()) {
			return devices.get();
		} else {
			device = deviceRepo.save(device);
			return device;
		}
	}
	
	private void iam(Device device) {
		for (DeviceDiscoveryListener listener : discoveryListeners) {
			listener.iam(device);
		}
	}

	@Override
	public boolean sendMedia(Device device, String url) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean send(Device device, Content content) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean send(Device device, Playlist playlist) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean send(Schedule schedule) {
		// TODO Auto-generated method stub
		return false;
	}

	@PreDestroy
	public void distroy() {
		stopDiscovery();
	}

	@Override
	public boolean send(Device device, Desktop desktop) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DeviceController getDeviceController(Device device) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addDeviceChangedListener(DeviceChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDeviceChangedListener(DeviceChangedListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<DeviceMeta> getDeviceMetaForProperty(String property) {
		// TODO Auto-generated method stub
		return null;
	}
}
