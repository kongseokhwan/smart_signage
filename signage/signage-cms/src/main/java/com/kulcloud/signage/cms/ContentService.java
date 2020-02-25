package com.kulcloud.signage.cms;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kulcloud.signage.cms.data.Content;

public interface ContentService {
	public String supportType();
	public List<? extends Content> findAllByTenantId(String tenantId);
	public List<? extends Content> findAllByTenantId(String tenantId, Pageable pageable);
	public void save(Map<String, Object> content, MultipartFile file);
	public void delete(String tenantId, String contentId);
}
