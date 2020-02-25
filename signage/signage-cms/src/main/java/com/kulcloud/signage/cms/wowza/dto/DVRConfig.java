package com.kulcloud.signage.cms.wowza.dto;

import java.util.List;

public class DVRConfig {

	private Integer windowDuration = null;
	private Boolean dvrMediaCacheEnabled = null;
	private Boolean dvrEnable = null;
	private Boolean startRecordingOnStartup = null;
	private String serverName = null;
	private String store = null;
	private String version = null;
	private String recorders = null;
	private Boolean dvrOnlyStreaming = null;
	private Boolean httpRandomizeMediaName = null;
	private String licenseType = null;
	private String dvrEncryptionSharedSecret = null;
	private Boolean inUse = null;
	private String archiveStrategy = null;
	private String storageDir = null;
	private List<String> saveFieldList = null;

	public Integer getWindowDuration() {
		return windowDuration;
	}

	public void setWindowDuration(Integer windowDuration) {
		this.windowDuration = windowDuration;
	}

	public Boolean getDvrMediaCacheEnabled() {
		return dvrMediaCacheEnabled;
	}

	public void setDvrMediaCacheEnabled(Boolean dvrMediaCacheEnabled) {
		this.dvrMediaCacheEnabled = dvrMediaCacheEnabled;
	}

	public Boolean getDvrEnable() {
		return dvrEnable;
	}

	public void setDvrEnable(Boolean dvrEnable) {
		this.dvrEnable = dvrEnable;
	}

	public Boolean getStartRecordingOnStartup() {
		return startRecordingOnStartup;
	}

	public void setStartRecordingOnStartup(Boolean startRecordingOnStartup) {
		this.startRecordingOnStartup = startRecordingOnStartup;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getStore() {
		return store;
	}

	public void setStore(String store) {
		this.store = store;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getRecorders() {
		return recorders;
	}

	public void setRecorders(String recorders) {
		this.recorders = recorders;
	}

	public Boolean getDvrOnlyStreaming() {
		return dvrOnlyStreaming;
	}

	public void setDvrOnlyStreaming(Boolean dvrOnlyStreaming) {
		this.dvrOnlyStreaming = dvrOnlyStreaming;
	}

	public Boolean getHttpRandomizeMediaName() {
		return httpRandomizeMediaName;
	}

	public void setHttpRandomizeMediaName(Boolean httpRandomizeMediaName) {
		this.httpRandomizeMediaName = httpRandomizeMediaName;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getDvrEncryptionSharedSecret() {
		return dvrEncryptionSharedSecret;
	}

	public void setDvrEncryptionSharedSecret(String dvrEncryptionSharedSecret) {
		this.dvrEncryptionSharedSecret = dvrEncryptionSharedSecret;
	}

	public Boolean getInUse() {
		return inUse;
	}

	public void setInUse(Boolean inUse) {
		this.inUse = inUse;
	}

	public String getArchiveStrategy() {
		return archiveStrategy;
	}

	public void setArchiveStrategy(String archiveStrategy) {
		this.archiveStrategy = archiveStrategy;
	}

	public String getStorageDir() {
		return storageDir;
	}

	public void setStorageDir(String storageDir) {
		this.storageDir = storageDir;
	}

	public List<String> getSaveFieldList() {
		return saveFieldList;
	}

	public void setSaveFieldList(List<String> saveFieldList) {
		this.saveFieldList = saveFieldList;
	}

}
