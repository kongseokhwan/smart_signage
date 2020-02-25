package com.kulcloud.signage.cms.html.data;

import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.kulcloud.signage.cms.data.Content;

@Entity
@Table(name = "tb_html_content")
public class HtmlContent extends Content {
	
	private boolean link;

	public HtmlContent() {
		super();
	}
	
	public HtmlContent(Map<String, Object> content) {
		super(content);
		this.link = content.get("link") != null ? Boolean.parseBoolean(content.get("link").toString()) : false;
	}

	public boolean isLink() {
		return link;
	}

	public void setLink(boolean link) {
		this.link = link;
	}

	@Override
	public void copyFromMap(Map<String, Object> content) {
		super.copyFromMap(content);
		
		if(content.get("link") != null) {
			this.link = Boolean.parseBoolean(content.get("link").toString());
		}
	}
	
}
