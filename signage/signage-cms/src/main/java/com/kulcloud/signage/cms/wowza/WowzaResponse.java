package com.kulcloud.signage.cms.wowza;

public enum WowzaResponse {
	OK(200),
	Bad_Request(400),
	Payment_Required(402),
	Not_Found(404),
	Unsupported_Media_Type(415),
	Created(201),
	Conflict(409)
	;
	
	private int code;
	
	WowzaResponse(int code) {
		this.code = code;
	}
	
	public int getCode() {
		return code;
	}
	
}
