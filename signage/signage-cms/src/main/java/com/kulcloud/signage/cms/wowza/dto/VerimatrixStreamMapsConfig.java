package com.kulcloud.signage.cms.wowza.dto;

import java.util.ArrayList;
import java.util.List;

public class VerimatrixStreamMapsConfig {

	private String filename = null;
	private String serverName = null;
	private List<String> saveFieldList = null;
	private String version = null;
	private List<VerimatrixStreamMapConfig> verimatrixStreamMaps = new ArrayList<VerimatrixStreamMapConfig>();

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
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

	public List<VerimatrixStreamMapConfig> getVerimatrixStreamMaps() {
		return verimatrixStreamMaps;
	}

	public void setVerimatrixStreamMaps(List<VerimatrixStreamMapConfig> verimatrixStreamMaps) {
		this.verimatrixStreamMaps = verimatrixStreamMaps;
	}

}
