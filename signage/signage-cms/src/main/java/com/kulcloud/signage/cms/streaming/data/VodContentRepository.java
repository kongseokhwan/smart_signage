package com.kulcloud.signage.cms.streaming.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kulcloud.signage.cms.data.ContentPK;

public interface VodContentRepository extends JpaRepository<VodContent, ContentPK> {

	List<VodContent> findByIdTenantId(String tenantId);
	List<VodContent> findByIdTenantId(String tenantId, Pageable pageable);
}
