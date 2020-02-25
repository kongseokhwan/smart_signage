package com.kulcloud.signage.cms.wowza.dto;

import java.util.List;

public class WebRTCConfig {

	private String udpBindAddress = null;
	private Boolean enablePlay = null;
	private String preferredCodecsAudio = null;
	private String preferredCodecsVideo = null;
	private Boolean enableQuery = null;
	private Boolean debugLog = null;
	private String serverName = null;
	private List<String> saveFieldList = null;
	private String iceCandidateIpAddresses = null;
	private String version = null;
	private Boolean enablePublish = null;

	public String getUdpBindAddress() {
		return udpBindAddress;
	}

	public void setUdpBindAddress(String udpBindAddress) {
		this.udpBindAddress = udpBindAddress;
	}

	public Boolean getEnablePlay() {
		return enablePlay;
	}

	public void setEnablePlay(Boolean enablePlay) {
		this.enablePlay = enablePlay;
	}

	public String getPreferredCodecsAudio() {
		return preferredCodecsAudio;
	}

	public void setPreferredCodecsAudio(String preferredCodecsAudio) {
		this.preferredCodecsAudio = preferredCodecsAudio;
	}

	public String getPreferredCodecsVideo() {
		return preferredCodecsVideo;
	}

	public void setPreferredCodecsVideo(String preferredCodecsVideo) {
		this.preferredCodecsVideo = preferredCodecsVideo;
	}

	public Boolean getEnableQuery() {
		return enableQuery;
	}

	public void setEnableQuery(Boolean enableQuery) {
		this.enableQuery = enableQuery;
	}

	public Boolean getDebugLog() {
		return debugLog;
	}

	public void setDebugLog(Boolean debugLog) {
		this.debugLog = debugLog;
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

	public String getIceCandidateIpAddresses() {
		return iceCandidateIpAddresses;
	}

	public void setIceCandidateIpAddresses(String iceCandidateIpAddresses) {
		this.iceCandidateIpAddresses = iceCandidateIpAddresses;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Boolean getEnablePublish() {
		return enablePublish;
	}

	public void setEnablePublish(Boolean enablePublish) {
		this.enablePublish = enablePublish;
	}

}
