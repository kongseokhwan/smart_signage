package com.kulcloud.signage.tenant.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulcloud.signage.tenant.data.entity.Camera;
import com.kulcloud.signage.tenant.device.DeviceServiceManager;

@RestController
@RequestMapping(path = "/camera")
public class CameraRestController {

	@Autowired
	private DeviceServiceManager deviceService;
	
	@GetMapping(path = "/list")
	public List<Camera> getList() {
		return deviceService.findCameras();
	}
}
