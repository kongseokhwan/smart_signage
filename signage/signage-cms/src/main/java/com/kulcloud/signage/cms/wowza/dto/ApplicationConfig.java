package com.kulcloud.signage.cms.wowza.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Applicaiton(Live, VOD) 운영을 위한 설정 정보 
 * @author sungil.yang
 *
 */
public class ApplicationConfig {

	private Boolean httpOptimizeFileReads = null;
	private String captionLiveIngestType = null;
	private StreamConfigurationConfig streamConfig = null;
	private String serverName = null;
	private String description = null;
	private WebRTCConfig webRTCConfig = null;
	private List<String> mediaCacheSourceList = new ArrayList<String>();
	private String repeaterOriginURL = null;
	private String clientStreamReadAccess = null;
	private String appType = null;
	private Integer pingTimeout = null;
	private List<String> vodTimedTextProviders = new ArrayList<String>();
	private List<String> saveFieldList = null;
	private String mediaReaderRandomAccessReaderClass = null;
	private Boolean mediaReaderBufferSeekIO = null;
	private List<String> httpStreamers = new ArrayList<String>();
	private Boolean httpCORSHeadersEnabled = null;
	private String avSyncMethod = null;
	private TranscoderAppConfig transcoderConfig = null;
	private String clientStreamWriteAccess = null;
	private String repeaterQueryString = null;
	private DRMConfig drmConfig = null;
	private String version = null;
	private ModulesConfig modules = null;
	private Integer maxRTCPWaitTime = null;
	private SecurityConfig securityConfig = null;
	private DVRConfig dvrConfig = null;
	private Integer applicationTimeout = null;
	private String name = null;

	public Boolean getHttpOptimizeFileReads() {
		return httpOptimizeFileReads;
	}

	public void setHttpOptimizeFileReads(Boolean httpOptimizeFileReads) {
		this.httpOptimizeFileReads = httpOptimizeFileReads;
	}

	public String getCaptionLiveIngestType() {
		return captionLiveIngestType;
	}

	public void setCaptionLiveIngestType(String captionLiveIngestType) {
		this.captionLiveIngestType = captionLiveIngestType;
	}

	public StreamConfigurationConfig getStreamConfig() {
		return streamConfig;
	}

	public void setStreamConfig(StreamConfigurationConfig streamConfig) {
		this.streamConfig = streamConfig;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public WebRTCConfig getWebRTCConfig() {
		return webRTCConfig;
	}

	public void setWebRTCConfig(WebRTCConfig webRTCConfig) {
		this.webRTCConfig = webRTCConfig;
	}

	public List<String> getMediaCacheSourceList() {
		return mediaCacheSourceList;
	}

	public void setMediaCacheSourceList(List<String> mediaCacheSourceList) {
		this.mediaCacheSourceList = mediaCacheSourceList;
	}

	public String getRepeaterOriginURL() {
		return repeaterOriginURL;
	}

	public void setRepeaterOriginURL(String repeaterOriginURL) {
		this.repeaterOriginURL = repeaterOriginURL;
	}

	public String getClientStreamReadAccess() {
		return clientStreamReadAccess;
	}

	public void setClientStreamReadAccess(String clientStreamReadAccess) {
		this.clientStreamReadAccess = clientStreamReadAccess;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public Integer getPingTimeout() {
		return pingTimeout;
	}

	public void setPingTimeout(Integer pingTimeout) {
		this.pingTimeout = pingTimeout;
	}

	public List<String> getVodTimedTextProviders() {
		return vodTimedTextProviders;
	}

	public void setVodTimedTextProviders(List<String> vodTimedTextProviders) {
		this.vodTimedTextProviders = vodTimedTextProviders;
	}

	public List<String> getSaveFieldList() {
		return saveFieldList;
	}

	public void setSaveFieldList(List<String> saveFieldList) {
		this.saveFieldList = saveFieldList;
	}

	public String getMediaReaderRandomAccessReaderClass() {
		return mediaReaderRandomAccessReaderClass;
	}

	public void setMediaReaderRandomAccessReaderClass(String mediaReaderRandomAccessReaderClass) {
		this.mediaReaderRandomAccessReaderClass = mediaReaderRandomAccessReaderClass;
	}

	public Boolean getMediaReaderBufferSeekIO() {
		return mediaReaderBufferSeekIO;
	}

	public void setMediaReaderBufferSeekIO(Boolean mediaReaderBufferSeekIO) {
		this.mediaReaderBufferSeekIO = mediaReaderBufferSeekIO;
	}

	public List<String> getHttpStreamers() {
		return httpStreamers;
	}

	public void setHttpStreamers(List<String> httpStreamers) {
		this.httpStreamers = httpStreamers;
	}

	public Boolean getHttpCORSHeadersEnabled() {
		return httpCORSHeadersEnabled;
	}

	public void setHttpCORSHeadersEnabled(Boolean httpCORSHeadersEnabled) {
		this.httpCORSHeadersEnabled = httpCORSHeadersEnabled;
	}

	public String getAvSyncMethod() {
		return avSyncMethod;
	}

	public void setAvSyncMethod(String avSyncMethod) {
		this.avSyncMethod = avSyncMethod;
	}

	public TranscoderAppConfig getTranscoderConfig() {
		return transcoderConfig;
	}

	public void setTranscoderConfig(TranscoderAppConfig transcoderConfig) {
		this.transcoderConfig = transcoderConfig;
	}

	public String getClientStreamWriteAccess() {
		return clientStreamWriteAccess;
	}

	public void setClientStreamWriteAccess(String clientStreamWriteAccess) {
		this.clientStreamWriteAccess = clientStreamWriteAccess;
	}

	public String getRepeaterQueryString() {
		return repeaterQueryString;
	}

	public void setRepeaterQueryString(String repeaterQueryString) {
		this.repeaterQueryString = repeaterQueryString;
	}

	public DRMConfig getDrmConfig() {
		return drmConfig;
	}

	public void setDrmConfig(DRMConfig drmConfig) {
		this.drmConfig = drmConfig;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public ModulesConfig getModules() {
		return modules;
	}

	public void setModules(ModulesConfig modules) {
		this.modules = modules;
	}

	public Integer getMaxRTCPWaitTime() {
		return maxRTCPWaitTime;
	}

	public void setMaxRTCPWaitTime(Integer maxRTCPWaitTime) {
		this.maxRTCPWaitTime = maxRTCPWaitTime;
	}

	public SecurityConfig getSecurityConfig() {
		return securityConfig;
	}

	public void setSecurityConfig(SecurityConfig securityConfig) {
		this.securityConfig = securityConfig;
	}

	public DVRConfig getDvrConfig() {
		return dvrConfig;
	}

	public void setDvrConfig(DVRConfig dvrConfig) {
		this.dvrConfig = dvrConfig;
	}

	public Integer getApplicationTimeout() {
		return applicationTimeout;
	}

	public void setApplicationTimeout(Integer applicationTimeout) {
		this.applicationTimeout = applicationTimeout;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
