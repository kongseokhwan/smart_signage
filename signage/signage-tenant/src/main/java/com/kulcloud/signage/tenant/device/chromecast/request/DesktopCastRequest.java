package com.kulcloud.signage.tenant.device.chromecast.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DesktopCastRequest extends CastRequest {

	@JsonProperty
	private String url;
	
	public DesktopCastRequest() {}

	public DesktopCastRequest(String url) {
		this.url = url;
	}

	@Override
	public String getRequestType() {
		return "desktop";
	}

}
