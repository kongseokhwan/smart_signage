package com.kulcloud.signage.cms.streaming.data;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kulcloud.signage.cms.data.Content;

@Entity
@Table(name = "tb_live_content")
public class LiveContent extends Content {

	@Column(name="live_type")
	@JsonProperty("live_type")
	private String liveType;

	public LiveContent() {
		super();
	}
	
	public LiveContent(Map<String, Object> content) {
		super(content);
		this.liveType = content.get("live_type") != null ? content.get("live_type").toString() : null;
	}
	
	public String getLiveType() {
		return liveType;
	}

	public void setLiveType(String liveType) {
		this.liveType = liveType;
	}

}
