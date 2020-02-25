package com.kulcloud.signage.cms.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kulcloud.signage.commons.dto.SignageTenantDto;

@Entity
@Table(name = "tb_signage_tenant")
public class SignageTenant {
	
	@Id
	@Column(name = "tenant_id")
	@JsonProperty("tenant_id")
	private String tenantId;
	private String password;
	@Column(name = "ip_address")
	@JsonProperty("ip_address")
	private String ipAddress;
	@Column(name = "api_key")
	@JsonProperty("api_key")
	private String apiKey;

	public SignageTenant() {}
	
	public SignageTenant(SignageTenantDto dto) {
		this.tenantId = dto.getTenantId();
		this.password = dto.getPassword();
		this.ipAddress = dto.getIpAddress();
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

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

}
