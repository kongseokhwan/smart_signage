package com.kulcloud.signage.tenant.device.chromecast.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContentCastRequest extends CastRequest {
	
	@JsonProperty
    final String url;
    @JsonProperty
    final boolean force;
    @JsonProperty
    final boolean reload;
    @JsonProperty("reload_time")
    final int reloadMilliseconds;

    public ContentCastRequest(String url) {
		this(url, true, false, 0);
	}
    public ContentCastRequest(String url,
                           boolean force,
                           boolean reload,
                           int reloadMilliseconds) {
    	this.url = url;
        this.force = force;
        this.reload = reload;
        this.reloadMilliseconds = reloadMilliseconds;
    }

    @Override
	public String getRequestType() {
		return "content";
	}
}