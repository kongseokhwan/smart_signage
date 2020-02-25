package com.kulcloud.signage.cms.streaming.data;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kulcloud.signage.cms.data.Content;

@Entity
@Table(name="tb_vod_content")
public class VodContent extends Content {

	@Column(name = "file_name")
	@JsonProperty("file_name")
	private String fileName;

	public VodContent() {
		super();
	}
	
	public VodContent(Map<String, Object> content) {
		super(content);
		this.fileName = content.get("file_name") != null ? content.get("file_name").toString() : null;
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void copyFromMap(Map<String, Object> content) {
		super.copyFromMap(content);
		
		if(content.get("file_name") != null) {
			this.fileName = content.get("file_name").toString();
		}
	}
	
}
