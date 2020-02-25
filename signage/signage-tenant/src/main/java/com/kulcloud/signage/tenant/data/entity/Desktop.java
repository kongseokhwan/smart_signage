package com.kulcloud.signage.tenant.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tb_desktop")
public class Desktop {

	@Id
	@Column(name = "desktop_id")
	@JsonProperty("desktop_id")
	private String desktopId;

	private String protocol;
	private String hostname;
	private String port;
	private String username;
	private String password;

	public String getDesktopId() {
		return desktopId;
	}

	public void setDesktopId(String desktopId) {
		this.desktopId = desktopId;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
