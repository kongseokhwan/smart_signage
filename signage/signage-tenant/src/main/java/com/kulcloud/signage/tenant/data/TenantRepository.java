package com.kulcloud.signage.tenant.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulcloud.signage.tenant.data.entity.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, String> {

}
