package com.kulcloud.signage.cms.streaming;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kulcloud.signage.cms.ContentService;
import com.kulcloud.signage.cms.data.ContentPK;
import com.kulcloud.signage.cms.streaming.data.LiveContent;
import com.kulcloud.signage.cms.streaming.data.LiveContentRepository;

//@Service
public class LiveContentService implements ContentService{

	@Autowired
	private StreamingServerProxy server;
	@Autowired
	private LiveContentRepository liveRepo;
	
	@Override
	public void save(Map<String, Object> content, MultipartFile file) {
		// ignored file
		
		LiveContent live = new LiveContent(content);
		save(live);
	}

	@Override
	public void delete(String tenantId, String contentId) {
		LiveContent content = liveRepo.findById(new ContentPK(tenantId, contentId)).orElse(null);
		if(content != null) {
			if(server.deleteLive(content)) {
				liveRepo.delete(content);
			}
		}
	}
	
	public boolean save(LiveContent content) {
		if(liveRepo.findById(content.getId()).isPresent()) {
			server.deleteLive(content);
		}
		
		if(server.addLive(content)) {
			try {
				liveRepo.save(content);
				return true;
			} catch (Exception ex) {
				if(!liveRepo.findById(content.getId()).isPresent()) {
					server.deleteLive(content);
				}
				
				return false;
			}
		} else {
			return false;
		}
	}
	
	public List<LiveContent> findAllByTenantId(String tenantId) {
		return liveRepo.findByIdTenantId(tenantId);
	}
	
	public List<LiveContent> findAllByTenantId(String tenantId, Pageable pageable) {
		return liveRepo.findByIdTenantId(tenantId, pageable);
	}

	@Override
	public String supportType() {
		// TODO Auto-generated method stub
		return null;
	}
}
