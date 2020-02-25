package com.kulcloud.signage.tenant.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulcloud.signage.tenant.data.entity.Desktop;

public interface DesktopRepository extends JpaRepository<Desktop, String> {

}
