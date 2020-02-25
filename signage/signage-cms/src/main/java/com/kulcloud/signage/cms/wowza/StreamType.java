package com.kulcloud.signage.cms.wowza;

public enum StreamType {
	
	file, live, live_lowlatency("live-lowlatency"), live_record("live-record"), live_record_lowlatency("live-record-lowlatency"),
	liverepeater_edge("liverepeater-edge "), liverepeater_edge_lowlatency("liverepeater-edge-lowlatency"), liverepeater_edge_origin("liverepeater-edge-origin"),
	record, rtp_live("rtp-live"), rtp_live_lowlatency("rtp-live-lowlatency"), rtp_live_record("rtp-live-record"), rtp_live_record_lowlatency("rtp-live-record-lowlatency"),
	shoutcast, shoutcast_record("shoutcast-record")
	;
	
	private String name;
	
	StreamType() {
		this.name = name();
	}
	
	StreamType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
