package com.kulcloud.signage.cms.wowza.dto;

import java.util.List;

public class DRMConfig {

	private Boolean buyDRMProtectMpegDashStreaming = null;
	private String serverName = null;
	private Boolean buyDRMProtectCupertinoStreaming = null;
	private String version = null;
	private Integer verimatrixCupertinoKeyServerPort = null;
	private Integer verimatrixSmoothKeyServerPort = null;
	private VerimatrixStreamMapsConfig verimatrixStreamMaps = null;
	private String verimatrixSmoothKeyServerIpAddress = null;
	private String licenseType = null;
	private String verimatrixCupertinoKeyServerIpAddress = null;
	private Boolean buyDRMProtectSmoothStreaming = null;
	private String buyDRMUserKey = null;
	private Boolean inUse = null;
	private String ezDRMUsername = null;
	private Boolean verimatrixProtectSmoothStreaming = null;
	private BuyDRMStreamMapsConfig buyDRMStreamMaps = null;
	private Boolean verimatrixCupertinoVODPerSessionKeys = null;
	private List<String> saveFieldList = null;
	private String ezDRMPassword = null;
	private Boolean verimatrixProtectCupertinoStreaming = null;
	private Boolean cupertinoEncryptionAPIBased = null;

	public Boolean getBuyDRMProtectMpegDashStreaming() {
		return buyDRMProtectMpegDashStreaming;
	}

	public void setBuyDRMProtectMpegDashStreaming(Boolean buyDRMProtectMpegDashStreaming) {
		this.buyDRMProtectMpegDashStreaming = buyDRMProtectMpegDashStreaming;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public Boolean getBuyDRMProtectCupertinoStreaming() {
		return buyDRMProtectCupertinoStreaming;
	}

	public void setBuyDRMProtectCupertinoStreaming(Boolean buyDRMProtectCupertinoStreaming) {
		this.buyDRMProtectCupertinoStreaming = buyDRMProtectCupertinoStreaming;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getVerimatrixCupertinoKeyServerPort() {
		return verimatrixCupertinoKeyServerPort;
	}

	public void setVerimatrixCupertinoKeyServerPort(Integer verimatrixCupertinoKeyServerPort) {
		this.verimatrixCupertinoKeyServerPort = verimatrixCupertinoKeyServerPort;
	}

	public Integer getVerimatrixSmoothKeyServerPort() {
		return verimatrixSmoothKeyServerPort;
	}

	public void setVerimatrixSmoothKeyServerPort(Integer verimatrixSmoothKeyServerPort) {
		this.verimatrixSmoothKeyServerPort = verimatrixSmoothKeyServerPort;
	}

	public VerimatrixStreamMapsConfig getVerimatrixStreamMaps() {
		return verimatrixStreamMaps;
	}

	public void setVerimatrixStreamMaps(VerimatrixStreamMapsConfig verimatrixStreamMaps) {
		this.verimatrixStreamMaps = verimatrixStreamMaps;
	}

	public String getVerimatrixSmoothKeyServerIpAddress() {
		return verimatrixSmoothKeyServerIpAddress;
	}

	public void setVerimatrixSmoothKeyServerIpAddress(String verimatrixSmoothKeyServerIpAddress) {
		this.verimatrixSmoothKeyServerIpAddress = verimatrixSmoothKeyServerIpAddress;
	}

	public String getLicenseType() {
		return licenseType;
	}

	public void setLicenseType(String licenseType) {
		this.licenseType = licenseType;
	}

	public String getVerimatrixCupertinoKeyServerIpAddress() {
		return verimatrixCupertinoKeyServerIpAddress;
	}

	public void setVerimatrixCupertinoKeyServerIpAddress(String verimatrixCupertinoKeyServerIpAddress) {
		this.verimatrixCupertinoKeyServerIpAddress = verimatrixCupertinoKeyServerIpAddress;
	}

	public Boolean getBuyDRMProtectSmoothStreaming() {
		return buyDRMProtectSmoothStreaming;
	}

	public void setBuyDRMProtectSmoothStreaming(Boolean buyDRMProtectSmoothStreaming) {
		this.buyDRMProtectSmoothStreaming = buyDRMProtectSmoothStreaming;
	}

	public String getBuyDRMUserKey() {
		return buyDRMUserKey;
	}

	public void setBuyDRMUserKey(String buyDRMUserKey) {
		this.buyDRMUserKey = buyDRMUserKey;
	}

	public Boolean getInUse() {
		return inUse;
	}

	public void setInUse(Boolean inUse) {
		this.inUse = inUse;
	}

	public String getEzDRMUsername() {
		return ezDRMUsername;
	}

	public void setEzDRMUsername(String ezDRMUsername) {
		this.ezDRMUsername = ezDRMUsername;
	}

	public Boolean getVerimatrixProtectSmoothStreaming() {
		return verimatrixProtectSmoothStreaming;
	}

	public void setVerimatrixProtectSmoothStreaming(Boolean verimatrixProtectSmoothStreaming) {
		this.verimatrixProtectSmoothStreaming = verimatrixProtectSmoothStreaming;
	}

	public BuyDRMStreamMapsConfig getBuyDRMStreamMaps() {
		return buyDRMStreamMaps;
	}

	public void setBuyDRMStreamMaps(BuyDRMStreamMapsConfig buyDRMStreamMaps) {
		this.buyDRMStreamMaps = buyDRMStreamMaps;
	}

	public Boolean getVerimatrixCupertinoVODPerSessionKeys() {
		return verimatrixCupertinoVODPerSessionKeys;
	}

	public void setVerimatrixCupertinoVODPerSessionKeys(Boolean verimatrixCupertinoVODPerSessionKeys) {
		this.verimatrixCupertinoVODPerSessionKeys = verimatrixCupertinoVODPerSessionKeys;
	}

	public List<String> getSaveFieldList() {
		return saveFieldList;
	}

	public void setSaveFieldList(List<String> saveFieldList) {
		this.saveFieldList = saveFieldList;
	}

	public String getEzDRMPassword() {
		return ezDRMPassword;
	}

	public void setEzDRMPassword(String ezDRMPassword) {
		this.ezDRMPassword = ezDRMPassword;
	}

	public Boolean getVerimatrixProtectCupertinoStreaming() {
		return verimatrixProtectCupertinoStreaming;
	}

	public void setVerimatrixProtectCupertinoStreaming(Boolean verimatrixProtectCupertinoStreaming) {
		this.verimatrixProtectCupertinoStreaming = verimatrixProtectCupertinoStreaming;
	}

	public Boolean getCupertinoEncryptionAPIBased() {
		return cupertinoEncryptionAPIBased;
	}

	public void setCupertinoEncryptionAPIBased(Boolean cupertinoEncryptionAPIBased) {
		this.cupertinoEncryptionAPIBased = cupertinoEncryptionAPIBased;
	}

}
