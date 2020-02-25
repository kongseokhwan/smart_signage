package com.kulcloud.signage.cms.user;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

	private final SignageTenantRepository tenantRepo;
	private final SignageCmsAdminRepository adminRepo;

	@Autowired
	public UserDetailsServiceImpl(SignageTenantRepository userRepo, SignageCmsAdminRepository adminRepo) {
		this.tenantRepo = userRepo;
		this.adminRepo = adminRepo;
	}
	
	@Override
	public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
		SignageTenant tenant = tenantRepo.findById(userId).orElse(null);
		if (tenant != null) {
			return new org.springframework.security.core.userdetails.User(tenant.getTenantId(), tenant.getPassword(), 
					Collections.singletonList(new SimpleGrantedAuthority("tenant")));
		} else {
			SignageCmsAdmin admin = adminRepo.findById(userId).orElse(null);
			if(admin != null) {
				return new org.springframework.security.core.userdetails.User(admin.getAdminId(), admin.getPassword(), 
						Collections.singletonList(new SimpleGrantedAuthority("admin")));
			} else {
				return null;
			}
		}
	}

}
