package com.kulcloud.signage.cms.wowza.dto;

import java.util.List;

public class TranscoderAppConfig {

	private String profileDir = null;
	private Boolean licensed = null;
	private TranscoderTemplatesConfig templates = null;
	private Boolean available = null;
	private String serverName = null;
	private String templateDir = null;
	private String version = null;
	private Boolean createTemplateDir = null;
	private Integer licenses = null;
	private String liveStreamTranscoder = null;
	private String templatesInUse = null;
	private Integer licensesInUse = null;
	private List<String> saveFieldList = null;

	public String getProfileDir() {
		return profileDir;
	}

	public void setProfileDir(String profileDir) {
		this.profileDir = profileDir;
	}

	public Boolean getLicensed() {
		return licensed;
	}

	public void setLicensed(Boolean licensed) {
		this.licensed = licensed;
	}

	public TranscoderTemplatesConfig getTemplates() {
		return templates;
	}

	public void setTemplates(TranscoderTemplatesConfig templates) {
		this.templates = templates;
	}

	public Boolean getAvailable() {
		return available;
	}

	public void setAvailable(Boolean available) {
		this.available = available;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getTemplateDir() {
		return templateDir;
	}

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Boolean getCreateTemplateDir() {
		return createTemplateDir;
	}

	public void setCreateTemplateDir(Boolean createTemplateDir) {
		this.createTemplateDir = createTemplateDir;
	}

	public Integer getLicenses() {
		return licenses;
	}

	public void setLicenses(Integer licenses) {
		this.licenses = licenses;
	}

	public String getLiveStreamTranscoder() {
		return liveStreamTranscoder;
	}

	public void setLiveStreamTranscoder(String liveStreamTranscoder) {
		this.liveStreamTranscoder = liveStreamTranscoder;
	}

	public String getTemplatesInUse() {
		return templatesInUse;
	}

	public void setTemplatesInUse(String templatesInUse) {
		this.templatesInUse = templatesInUse;
	}

	public Integer getLicensesInUse() {
		return licensesInUse;
	}

	public void setLicensesInUse(Integer licensesInUse) {
		this.licensesInUse = licensesInUse;
	}

	public List<String> getSaveFieldList() {
		return saveFieldList;
	}

	public void setSaveFieldList(List<String> saveFieldList) {
		this.saveFieldList = saveFieldList;
	}

}
