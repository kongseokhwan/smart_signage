package com.kulcloud.signage.cms.wowza.dto;

import java.util.ArrayList;
import java.util.List;

public class TranscoderTemplatesConfig {

	private String vhostName = null;
	private List<ShortObject> templates = new ArrayList<ShortObject>();
	private String serverName = null;
	private List<String> saveFieldList = null;
	private String version = null;

	public String getVhostName() {
		return vhostName;
	}

	public void setVhostName(String vhostName) {
		this.vhostName = vhostName;
	}

	public List<ShortObject> getTemplates() {
		return templates;
	}

	public void setTemplates(List<ShortObject> templates) {
		this.templates = templates;
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
