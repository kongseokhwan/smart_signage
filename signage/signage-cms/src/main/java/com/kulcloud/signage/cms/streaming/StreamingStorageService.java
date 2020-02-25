package com.kulcloud.signage.cms.streaming;

import org.springframework.beans.factory.annotation.Autowired;

import com.kulcloud.signage.cms.StorageService;

//@Service
public class StreamingStorageService implements StorageService{

	@Autowired
	private StreamingServerProxy server;
	
	@Override
	public boolean createStorage(String tenantId) {
		return server.createStorage(tenantId);
	}
	
	@Override
	public boolean deleteStorage(String tenantId) {
		return server.deleteStorage(tenantId);
	}
}
