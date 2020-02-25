package com.kulcloud.signage.tenant.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulcloud.signage.tenant.data.entity.DeviceGroup;

public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long> {

}
