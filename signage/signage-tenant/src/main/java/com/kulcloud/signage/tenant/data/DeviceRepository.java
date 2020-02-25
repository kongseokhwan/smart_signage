package com.kulcloud.signage.tenant.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.kulcloud.signage.tenant.data.entity.Device;

public interface DeviceRepository extends JpaRepository<Device, Long> {

	Optional<Device> findByTypeAndIpAddressAndSerialNumber(String type, String ipAddress, String serialNumber);
	List<Device> findByType(String type);
	
	@Transactional
	@Modifying
	@Query(nativeQuery = true, value="UPDATE tb_device set health=?1 where type=?2")
	void updateHealth(boolean health, String type);
	
}
