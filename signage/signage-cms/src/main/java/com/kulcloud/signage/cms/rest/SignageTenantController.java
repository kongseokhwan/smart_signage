package com.kulcloud.signage.cms.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kulcloud.signage.cms.StorageService;
import com.kulcloud.signage.cms.security.ApiKeyService;
import com.kulcloud.signage.cms.user.SignageTenant;
import com.kulcloud.signage.cms.user.SignageTenantRepository;
import com.kulcloud.signage.commons.dto.RestResult;
import com.kulcloud.signage.commons.dto.SignageTenantDto;
import com.kulcloud.signage.commons.utils.CommonConstants;

@RestController
@RequestMapping(path = "${com.kulcloud.signage.publicApiPath}/tenant")
public class SignageTenantController {
	
	@Autowired
	private SignageTenantRepository repo;
	@Autowired
	private ApiKeyService keyService;
	@Autowired
	private List<StorageService> storages;
	
	@PostMapping
	public RestResult create(@RequestBody SignageTenantDto tenant) {
		if(StringUtils.isAnyEmpty(tenant.getTenantId(), tenant.getPassword())) {
			return new RestResult(new IllegalArgumentException());
		}
		
		SignageTenant user = new SignageTenant(tenant);
		user.setApiKey(keyService.createApiKey(user));
		repo.save(user);
		
		return new RestResult(user.getApiKey());
	}
	
	@PutMapping(path="/{tenantId}/info")
	public RestResult update(@PathVariable String tenantId, @RequestBody SignageTenantDto newTenant,
			@RequestHeader(CommonConstants.HEADER_KEY) String apiKey) {
		if(StringUtils.isEmpty(apiKey) || !StringUtils.equals(tenantId, newTenant.getTenantId())) {
			return new RestResult(new IllegalArgumentException());
		}
		
		Optional<SignageTenant> optional = repo.findById(newTenant.getTenantId());
		if(optional.isPresent()) {
			SignageTenant user = optional.get();
			if(!StringUtils.equals(keyService.getTenantId(user, apiKey), newTenant.getTenantId())) {
				return new RestResult(new IllegalArgumentException("Invalid api key"));
			}
			
			if(newTenant.getIpAddress() != null) {
				user.setIpAddress(newTenant.getIpAddress());
			}
			
			if(newTenant.getPassword() != null) {
				user.setPassword(newTenant.getPassword());
				user.setApiKey(keyService.createApiKey(user));
			}
			
			repo.save(user);
			return new RestResult(user.getApiKey());
		} else {
			return new RestResult(new UsernameNotFoundException("Cannot find " + newTenant.getTenantId()));
		}
	}
	
	@PutMapping(path="/{tenantId}/storage")
	public RestResult storage(@PathVariable String tenantId, @RequestHeader(CommonConstants.HEADER_KEY) String apiKey) {
		if(StringUtils.isEmpty(apiKey)) {
			return new RestResult(new IllegalArgumentException());
		}
		
		Optional<SignageTenant> optional = repo.findById(tenantId);
		if(optional.isPresent()) {
			SignageTenant user = optional.get();
			if(!StringUtils.equals(keyService.getTenantId(user, apiKey), tenantId)) {
				return new RestResult(new IllegalArgumentException("Invalid api key"));
			}
			
			if(createStorage(tenantId)) {
				return new RestResult();
			} else {
				return new RestResult(new IllegalArgumentException("Cannot create a storage of " + tenantId));
			}
		} else {
			return new RestResult(new UsernameNotFoundException("Cannot find " + tenantId));
		}
	}
	
	@DeleteMapping(path="/{tenantId}")
	public RestResult delete(@PathVariable String tenantId, @RequestHeader(CommonConstants.HEADER_KEY) String apiKey) {
		if(StringUtils.isEmpty(apiKey)) {
			return new RestResult(new IllegalArgumentException());
		}
		
		Optional<SignageTenant> optional = repo.findById(tenantId);
		if(optional.isPresent()) {
			SignageTenant user = optional.get();
			if(!StringUtils.equals(keyService.getTenantId(user, apiKey), tenantId)) {
				return new RestResult(new IllegalArgumentException("Invalid api key"));
			}
			
			repo.delete(user);
			deleteStorage(tenantId);
			return new RestResult();
		} else {
			return new RestResult(new UsernameNotFoundException("Cannot find " + tenantId));
		}
	}
	
	@GetMapping(path="/{tenantId}")
	public RestResult exist(@PathVariable String tenantId) {
		Optional<SignageTenant> optional = repo.findById(tenantId);
		if(optional.isPresent()) {
			return new RestResult("Exist " + tenantId);
		} else {
			return new RestResult(new UsernameNotFoundException("Cannot find " + tenantId));
		}
	}
	
	private boolean createStorage(String tenantId) {
		List<StorageService> success = new ArrayList<>(storages.size());
		boolean result = true;
		for (StorageService storage : storages) {
			if(storage.createStorage(tenantId)) {
				success.add(storage);
			} else {
				result = false;
			}
		}
		
		if(!result && success.size() > 0) {
			for (StorageService storage : success) {
				storage.deleteStorage(tenantId);
			}
		}
		
		return result;
	}
	
	private void deleteStorage(String tenantId) {
		for (StorageService storage : storages) {
			storage.deleteStorage(tenantId);
		}
	}

}
