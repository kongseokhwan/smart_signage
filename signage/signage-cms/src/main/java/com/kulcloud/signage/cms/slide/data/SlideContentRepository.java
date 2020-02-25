package com.kulcloud.signage.cms.slide.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kulcloud.signage.cms.data.ContentPK;

public interface SlideContentRepository extends JpaRepository<SlideContent, ContentPK> {

	List<SlideContent> findByIdTenantId(String tenantId);
	List<SlideContent> findByIdTenantId(String tenantId, Pageable pageable);
}
