package com.kulcloud.signage.tenant.device;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceMeta {
	@JsonIgnore
	public static final String config = "config";
	@JsonIgnore
	public static final String control = "control";

	@JsonProperty
	private String name;
	@JsonProperty(required = false)
	private String title;
	@JsonProperty
	private String type;
	@JsonProperty(value = "default_value", required = false)
	private String defaultValue;
	@JsonProperty(value = "enumeration", required = false)
	private List<String> enumeration;

	public DeviceMeta() {}

	public DeviceMeta(String name, String type) {
		this(name, null, type, null, null);
	}

	public DeviceMeta(String name, String title, String type, String defaultValue, List<String> enumeration) {
		super();
		this.name = name;
		this.title = title;
		this.type = type;
		this.defaultValue = defaultValue;
		this.enumeration = enumeration;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public List<String> getEnumeration() {
		return enumeration;
	}

	public void setEnumeration(List<String> enumeration) {
		this.enumeration = enumeration;
	}
	
}
