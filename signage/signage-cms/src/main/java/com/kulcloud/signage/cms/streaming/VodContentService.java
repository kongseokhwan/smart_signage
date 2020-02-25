package com.kulcloud.signage.cms.streaming;

import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kulcloud.signage.cms.ContentService;
import com.kulcloud.signage.cms.data.ContentPK;
import com.kulcloud.signage.cms.streaming.data.VodContent;
import com.kulcloud.signage.cms.streaming.data.VodContentRepository;
import com.kulcloud.signage.commons.enums.ContentType;

//@Service
public class VodContentService implements ContentService{

	@Autowired
	private StreamingServerProxy server;
	@Autowired
	private VodContentRepository vodRepo;
	
	@Override
	public void save(Map<String, Object> content, MultipartFile file) {
		VodContent vod = new VodContent(content);
		save(vod, file);
	}
	
	public boolean save(VodContent content, MultipartFile file) {
		if(vodRepo.findById(content.getId()).isPresent()) {
			server.deleteVod(content);
		}
		
		if(!StringUtils.equals(content.getFileName(), file.getOriginalFilename())) {
			content.setFileName(file.getOriginalFilename());
		}
		
		if(server.addVod(content, file)) {
			try {
				vodRepo.save(content);
				return true;
			} catch (Exception ex) {
				if(!vodRepo.findById(content.getId()).isPresent()) {
					server.deleteVod(content);
				}
				
				return false;
			}
		} else {
			return false;
		}
	}
	
	public void delete(String tenantId, String contentId) {
		VodContent content = vodRepo.findById(new ContentPK(tenantId, contentId)).orElse(null);
		if(content != null) {
			if(server.deleteVod(content)) {
				vodRepo.delete(content);
			}
		}
	}
	
	public List<VodContent> findAllByTenantId(String tenantId) {
		return vodRepo.findByIdTenantId(tenantId);
	}
	
	public List<VodContent> findAllByTenantId(String tenantId, Pageable pageable) {
		return vodRepo.findByIdTenantId(tenantId, pageable);
	}

	@Override
	public String supportType() {
		return ContentType.vod.name();
	}
}
