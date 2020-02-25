package com.kulcloud.signage.cms.wowza.dto;

import java.util.ArrayList;
import java.util.List;

public class ApplicationsConfig {

	private String serverName = null;
	private List<String> saveFieldList = null;
	private String version = null;
	private List<AppShortObject> applications = new ArrayList<AppShortObject>();

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

	public List<AppShortObject> getApplications() {
		return applications;
	}

	public void setApplications(List<AppShortObject> applications) {
		this.applications = applications;
	}

}
