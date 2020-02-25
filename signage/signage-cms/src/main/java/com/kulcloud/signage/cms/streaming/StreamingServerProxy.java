package com.kulcloud.signage.cms.streaming;

import org.springframework.web.multipart.MultipartFile;

import com.kulcloud.signage.cms.streaming.data.LiveContent;
import com.kulcloud.signage.cms.streaming.data.VodContent;

public interface StreamingServerProxy {
	
	public boolean createStorage(String tenantId);
	public boolean deleteStorage(String tenantId);

	public boolean addVod(VodContent content, MultipartFile file);
	public boolean deleteVod(VodContent content);
	
	public boolean addLive(LiveContent content);
	public boolean deleteLive(LiveContent content);
	
}
