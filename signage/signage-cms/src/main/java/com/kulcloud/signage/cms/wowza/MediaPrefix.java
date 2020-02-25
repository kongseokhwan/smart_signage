package com.kulcloud.signage.cms.wowza;

import org.apache.commons.lang3.StringUtils;

import com.kulcloud.signage.commons.utils.CommonConstants;

public enum MediaPrefix {

	MP4("mp4", new String[]{".mp4", ".f4v", ".mov", ".m4a", ".m4v", ".mp4a", ".mp4v", ".3gp", ".3g2"}),
	MP3("mp3", new String[] {".mp3"}),
	SMIL("smil", new String[] {".smil"}),
	FLV("flv", new String[] {".flv"})
	;
	
	private String[] extentions;
	private String prefix;
	
	MediaPrefix(String prefix, String[] extentions) {
		this.prefix = prefix;
		this.extentions = extentions;
	}

	public String[] getExtentions() {
		return extentions;
	}

	public String getPrefix() {
		return prefix;
	}
	
	public static String getStreamName(String fileName) {
		int dotIndex = fileName.indexOf(CommonConstants.DOT);
		if(StringUtils.isEmpty(fileName) || 
				dotIndex == -1 || (dotIndex + 1) == fileName.length()) {
			return MP4.prefix + CommonConstants.COLON + fileName;
		}
		
		String extention = fileName.substring(dotIndex);
		for (MediaPrefix mediaPrefix : MediaPrefix.values()) {
			for (String ext : mediaPrefix.extentions) {
				if(StringUtils.equals(extention, ext)) {
					return mediaPrefix.prefix + CommonConstants.COLON + fileName;
				}
			}
		}
		
		return MP4.prefix + CommonConstants.COLON + fileName;
	}
}
