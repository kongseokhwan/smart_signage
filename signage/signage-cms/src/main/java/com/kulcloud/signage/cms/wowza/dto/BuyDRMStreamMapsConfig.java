package com.kulcloud.signage.cms.wowza.dto;

import java.util.List;

public class BuyDRMStreamMapsConfig {

	private String buyDRMStreamNameMapFile = null;
	private String serverName = null;
	private List<String> saveFieldList = null;
	private String version = null;

	public String getBuyDRMStreamNameMapFile() {
		return buyDRMStreamNameMapFile;
	}

	public void setBuyDRMStreamNameMapFile(String buyDRMStreamNameMapFile) {
		this.buyDRMStreamNameMapFile = buyDRMStreamNameMapFile;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
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

}
