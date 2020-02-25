package com.kulcloud.signage.commons.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SignageTenantDto {
	
	@JsonProperty("tenant_id")
	private String tenantId;
	private String password;
	@JsonProperty("ip_address")
	private String ipAddress;

	public SignageTenantDto() {}

	public SignageTenantDto(String tenantId, String password, String ipAddress) {
		super();
		this.tenantId = tenantId;
		this.password = password;
		this.ipAddress = ipAddress;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
