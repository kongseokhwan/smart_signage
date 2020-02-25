package com.kulcloud.signage.tenant.rest;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulcloud.signage.commons.dto.RestResult;
import com.kulcloud.signage.tenant.content.ContentService;
import com.kulcloud.signage.tenant.data.entity.Content;
import com.kulcloud.signage.tenant.data.entity.Device;
import com.kulcloud.signage.tenant.device.DeviceServiceManager;

@RestController
@RequestMapping(path = "/content")
public class ContentRestController {

	@Autowired
	private ContentService contentService;
	@Autowired
	private DeviceServiceManager deviceService;
	
	@GetMapping(path = "/list")
	public List<Content> getList() {
		return contentService.getContentList();
	}
	
	@GetMapping(path = "/{contentId}")
	public Content getContent(@PathVariable String contentId) {
		return contentService.getContent(contentId);
	}
	
	@PostMapping(path = "/{contentId}/send/{deviceId}")
	public RestResult send(@PathVariable String contentId, @PathVariable Long deviceId) {
		Content content = contentService.getContent(contentId);
		if(content == null || StringUtils.isEmpty(content.getUrl())) {
			return new RestResult(new IllegalArgumentException());
		} else {
			Device device = deviceService.findDevice(deviceId);
			if(device != null) {
				deviceService.sendMedia(device, content.getUrl());
				return new RestResult();
			} else {
				return new RestResult(false, "Cannot find a device: " + deviceId, null);
			}
		}
	}
}
