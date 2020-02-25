package com.kulcloud.signage.cms.html.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kulcloud.signage.cms.data.ContentPK;

public interface HtmlContentRepository extends JpaRepository<HtmlContent, ContentPK> {
	List<HtmlContent> findByIdTenantId(String tenantId);
	List<HtmlContent> findByIdTenantId(String tenantId, Pageable pageable);
}
