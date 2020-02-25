package com.kulcloud.signage.tenant.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulcloud.signage.tenant.data.entity.Camera;

/**
 * CameraRepository
 */
public interface CameraRepository extends JpaRepository<Camera, Long> {
	
	List<Camera> findByDeviceDeviceId(Long deviceId);
}