package com.kulcloud.signage.cms.wowza.dto;

import java.util.ArrayList;
import java.util.List;

public class StreamConfigurationConfig {

	private String streamType = null;
	private Boolean storageDirExists = null;
	private String keyDir = null;
	private Boolean createStorageDir = null;
	private List<String> liveStreamPacketizer = new ArrayList<String>();
	private String serverName = null;
	private String storageDir = null;
	private List<String> saveFieldList = null;
	private String version = null;
	private Boolean httpRandomizeMediaName = null;

	public String getStreamType() {
		return streamType;
	}

	public void setStreamType(String streamType) {
		this.streamType = streamType;
	}

	public Boolean getStorageDirExists() {
		return storageDirExists;
	}

	public void setStorageDirExists(Boolean storageDirExists) {
		this.storageDirExists = storageDirExists;
	}

	public String getKeyDir() {
		return keyDir;
	}

	public void setKeyDir(String keyDir) {
		this.keyDir = keyDir;
	}

	public Boolean getCreateStorageDir() {
		return createStorageDir;
	}

	public void setCreateStorageDir(Boolean createStorageDir) {
		this.createStorageDir = createStorageDir;
	}

	public List<String> getLiveStreamPacketizer() {
		return liveStreamPacketizer;
	}

	public void setLiveStreamPacketizer(List<String> liveStreamPacketizer) {
		this.liveStreamPacketizer = liveStreamPacketizer;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Boolean getHttpRandomizeMediaName() {
		return httpRandomizeMediaName;
	}

	public void setHttpRandomizeMediaName(Boolean httpRandomizeMediaName) {
		this.httpRandomizeMediaName = httpRandomizeMediaName;
	}

}
