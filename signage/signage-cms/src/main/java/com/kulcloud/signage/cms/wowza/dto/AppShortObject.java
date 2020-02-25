package com.kulcloud.signage.cms.wowza.dto;

public class AppShortObject {

	private Boolean drmEnabled = null;
	private Boolean streamTargetsEnabled = null;
	private String appType = null;
	private Boolean transcoderEnabled = null;
	private Boolean dvrEnabled = null;
	private String id = null;
	private String href = null;

	public Boolean getDrmEnabled() {
		return drmEnabled;
	}

	public void setDrmEnabled(Boolean drmEnabled) {
		this.drmEnabled = drmEnabled;
	}

	public Boolean getStreamTargetsEnabled() {
		return streamTargetsEnabled;
	}

	public void setStreamTargetsEnabled(Boolean streamTargetsEnabled) {
		this.streamTargetsEnabled = streamTargetsEnabled;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public Boolean getTranscoderEnabled() {
		return transcoderEnabled;
	}

	public void setTranscoderEnabled(Boolean transcoderEnabled) {
		this.transcoderEnabled = transcoderEnabled;
	}

	public Boolean getDvrEnabled() {
		return dvrEnabled;
	}

	public void setDvrEnabled(Boolean dvrEnabled) {
		this.dvrEnabled = dvrEnabled;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

}
