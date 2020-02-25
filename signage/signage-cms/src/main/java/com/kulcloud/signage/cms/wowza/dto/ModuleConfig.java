package com.kulcloud.signage.cms.wowza.dto;

import java.util.List;

public class ModuleConfig {

	private String name = null;
	private String serverName = null;
	private String description = null;
	private List<String> saveFieldList = null;
	private String version = null;
	private String propertyClass = null;
	private Integer order = null;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<String> getSaveFieldList() {
		return saveFieldList;
	}

	public void setSaveFieldList(List<String> saveFieldList) {
		this.saveFieldList = saveFieldList;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPropertyClass() {
		return propertyClass;
	}

	public void setPropertyClass(String propertyClass) {
		this.propertyClass = propertyClass;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

}
