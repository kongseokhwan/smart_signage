package com.kulcloud.signage.cms.slide.data;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kulcloud.signage.cms.data.Content;

@Entity
@Table(name = "tb_slide_content")
public class SlideContent extends Content {

	@Column(name = "original_file_name")
	@JsonProperty("original_file_name")
	private String originalFileName;
	@Column(name = "odf_file_name")
	@JsonProperty("odf_file_name")
	private String odfFileName;

	public SlideContent() {
		super();
	}
	
	public SlideContent(Map<String, Object> content, String origianalFileName) {
		super(content);
		this.originalFileName = origianalFileName;
	}
	
	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getOdfFileName() {
		return odfFileName;
	}

	public void setOdfFileName(String odfFileName) {
		this.odfFileName = odfFileName;
	}

	@Override
	public void copyFromMap(Map<String, Object> content) {
		super.copyFromMap(content);
		
		if(content.get("original_file_name") != null) {
			this.originalFileName = content.get("original_file_name").toString();
		}
		
		if(content.get("odf_file_name") != null) {
			this.odfFileName = content.get("odf_file_name").toString();
		}
	}

}
