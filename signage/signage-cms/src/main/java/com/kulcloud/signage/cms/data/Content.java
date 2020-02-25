package com.kulcloud.signage.cms.data;

import java.util.Map;

import javax.persistence.EmbeddedId;
import javax.persistence.MappedSuperclass;

import org.apache.commons.lang3.StringUtils;

import com.kulcloud.signage.commons.utils.CommonUtils;

@MappedSuperclass
public class Content {
	@EmbeddedId
	private ContentPK id;
	private String name;
	private String title;
	private String description;
	private String url;
	private boolean builtin = false;

	public Content() {}
	public Content(Map<String, Object> content) {
		this.id = new ContentPK(content);
		copyFromMap(content);	
	}

	public ContentPK getId() {
		return id;
	}

	public void setId(ContentPK id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isBuiltin() {
		return builtin;
	}

	public void setBuiltin(boolean builtin) {
		this.builtin = builtin;
	}
	
	public void copyFromMap(Map<String, Object> content) {
		if(content.get("name") != null) {
			this.name = content.get("name").toString();
		}
		
		if(content.get("title") != null) {
			this.title = content.get("title").toString();
		}
		
		if(content.get("description") != null) {
			this.description = content.get("description").toString();
		}
		
		if(content.get("url") != null) {
			this.url = content.get("url").toString();
		}
		
		if(content.get("builtin") != null) {
			this.builtin = Boolean.parseBoolean(content.get("builtin").toString());
		}
	}

}
