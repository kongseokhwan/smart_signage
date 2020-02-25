package com.kulcloud.signage.cms.streaming.data;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kulcloud.signage.cms.data.ContentPK;

public interface LiveContentRepository extends JpaRepository<LiveContent, ContentPK> {

	List<LiveContent> findByIdTenantId(String tenantId);
	List<LiveContent> findByIdTenantId(String tenantId, Pageable pageable);
}
