package com.kulcloud.signage.tenant.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kulcloud.signage.tenant.data.entity.Content;

public interface UserRepository extends JpaRepository<Content, String> {

}
