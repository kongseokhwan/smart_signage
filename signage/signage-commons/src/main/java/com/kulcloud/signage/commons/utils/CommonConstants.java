package com.kulcloud.signage.commons.utils;

import java.text.NumberFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

public interface CommonConstants {

	String PAGE_ROOT = "";
	String VIEWPORT = "width=device-width, minimum-scale=1, initial-scale=1, user-scalable=yes";
	
	String HTTPS = "https";
	String HTTP = "http";
	String SCHEME = "://";
	String SLASH = "/";
	String UNDER = "_";
	String COLON = ":";
	String DOT = ".";
	String ALL = "all";
	String CLASS_SCHEME = "classpath:";
	
	String PAGE_LOGIN = "login";
	String PAGE_SIGNUP = "signup";
	
	String LOGIN_PROCESSING_URL = SLASH + PAGE_LOGIN;
	String LOGIN_FAILURE_URL = SLASH + PAGE_LOGIN + "?error";
	String LOGIN_URL = SLASH + PAGE_LOGIN;
	String LOGOUT_SUCCESS_URL = SLASH + PAGE_ROOT;
	
	ObjectMapper mapper = new ObjectMapper();
	
	NumberFormat nf = NumberFormat.getInstance();
	String dtf = "yyyy-MM-dd HH:mm:ss.SSSSSS";
	
	String broadcastTopic = "/broadcast";
	String code = "v7B9jGbEBoFCwCH1DWNfbROZPjUkz8W2xKiHv0kwr55yXV2xnt8OuHhRA9mIrSbe";
	
	String HEADER_OTP = "X-AUTH-OTP";
	String HEADER_KEY = "X-AUTH-KEY";
	
	byte[] TRUE_BYTES = Boolean.TRUE.toString().getBytes();
	byte[] FALSE_BYTES = Boolean.FALSE.toString().getBytes();
	
	String POST_FILE_KEY = "file";
	String editorMaxWidth = "400px";
	String gridHeight = "86vh";
	String innerGridHeight = "82vh";
	String namespace = "urn:x-cast:com.kulcloud.signage";
	
}
