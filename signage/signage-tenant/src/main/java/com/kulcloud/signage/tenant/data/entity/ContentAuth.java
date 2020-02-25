package com.kulcloud.signage.tenant.data.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.http.HttpMethod;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kulcloud.signage.commons.enums.LoginType;

@Entity
@Table(name = "tb_content_login")
public class ContentAuth {

	@Id
	@Column(name = "login_id")
	@JsonProperty("login_id")
	private String loginId;
	@Column(name = "url_prefix")
	@JsonProperty("url_prefix")
	private String urlPrefix;
	private String type = LoginType.form.name();
	private String method = HttpMethod.POST.name();
	@Column(name = "login_url")
	@JsonProperty("login_url")
	private String loginUrl;
	@Column(name = "user_id")
	@JsonProperty("user_id")
	private String userId;
	private String password;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
