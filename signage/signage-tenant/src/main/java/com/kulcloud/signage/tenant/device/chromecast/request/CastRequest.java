package com.kulcloud.signage.tenant.device.chromecast.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import su.litvak.chromecast.api.v2.Request;

public abstract class CastRequest implements Request {
	
	private long requestId;
	
	@Override
	public Long getRequestId() {
		return requestId;
	}

	@Override
	public void setRequestId(Long requestId) {
		this.requestId = requestId;
	}
	
	@JsonProperty("request_type")
	public abstract String getRequestType();
}
