package com.kulcloud.signage.cms.wowza.dto;

import java.util.List;

public class VerimatrixStreamMapConfig {

	private Integer posCount = null;
	private Integer keyInterval = null;
	private String serverName = null;
	private List<String> saveFieldList = null;
	private String version = null;
	private String streamName = null;
	private String resID = null;
	private Integer order = null;

	public Integer getPosCount() {
		return posCount;
	}

	public void setPosCount(Integer posCount) {
		this.posCount = posCount;
	}

	public Integer getKeyInterval() {
		return keyInterval;
	}

	public void setKeyInterval(Integer keyInterval) {
		this.keyInterval = keyInterval;
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

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public String getResID() {
		return resID;
	}

	public void setResID(String resID) {
		this.resID = resID;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

}
