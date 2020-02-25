package com.kulcloud.signage.cms.wowza.dto;

import java.util.ArrayList;
import java.util.List;

public class ModulesConfig {

	private List<ModuleConfig> moduleList = new ArrayList<ModuleConfig>();
	private String serverName = null;
	private List<String> saveFieldList = null;
	private String version = null;

	public List<ModuleConfig> getModuleList() {
		return moduleList;
	}

	public void setModuleList(List<ModuleConfig> moduleList) {
		this.moduleList = moduleList;
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
