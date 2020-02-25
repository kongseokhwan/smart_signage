package com.kulcloud.signage.tenant.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tb_tenant")
public class Tenant {

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

	public Tenant() {}
	
	public Tenant(String tenantId, String password, String ipAddress) {
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

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

}
