package com.kulcloud.signage.cms.data;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kulcloud.signage.commons.utils.CommonUtils;

@Embeddable
public class ContentPK implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "tenant_id")
	@JsonProperty("tenant_id")
	private String tenantId;
	@Column(name = "content_id")
	@JsonProperty("content_id")
	private String contentId;

	public ContentPK() {
	}
	
	public ContentPK(String tenantId, String contentId) {
		this.tenantId = tenantId;
		this.contentId = contentId;
	}
	
	public ContentPK(Map<String, Object> map) {
		setIdFromMap(map);
		
		if(this.tenantId == null && map.get("id") != null) {
			Map<?, ?> idMap = CommonUtils.convertToMap(map.get("id"));
			setIdFromMap(idMap);
		} 
		
		if(this.tenantId == null) {
			throw new IllegalArgumentException();
		}
	}
	
	private void setIdFromMap(Map<? ,?> map) {
		if (map.get("tenant_id") != null && StringUtils.isEmpty(map.get("tenant_id").toString())) {
			this.tenantId = map.get("tenant_id").toString();
			if(map.get("content_id") != null && StringUtils.isEmpty(map.get("content_id").toString())) {
				this.contentId = map.get("content_id").toString();
			} else {
				this.contentId = CommonUtils.getUUID();
			}
		}
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String contentId) {
		this.contentId = contentId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((contentId == null) ? 0 : contentId.hashCode());
		result = prime * result + ((tenantId == null) ? 0 : tenantId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ContentPK other = (ContentPK) obj;
		if (contentId == null) {
			if (other.contentId != null)
				return false;
		} else if (!contentId.equals(other.contentId))
			return false;
		if (tenantId == null) {
			if (other.tenantId != null)
				return false;
		} else if (!tenantId.equals(other.tenantId))
			return false;
		return true;
	}

}
