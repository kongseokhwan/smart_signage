package com.kulcloud.signage.tenant.content;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kulcloud.signage.commons.enums.ContentType;
import com.kulcloud.signage.commons.utils.CommonConstants;
import com.kulcloud.signage.commons.utils.CommonUtils;
import com.kulcloud.signage.tenant.data.ContentRepository;
import com.kulcloud.signage.tenant.data.TenantRepository;
import com.kulcloud.signage.tenant.data.entity.Content;
import com.vaadin.flow.component.upload.receivers.FileBuffer;

@Service
public class ContentService {

	private CmsProxyService cmsService;
	private ContentRepository contentRepo;
	@Value("${wowza.stream.ssl:false}")
	private boolean wowzaSsl;
	@Value("${wowza.host:localhost}")
	private String wowzaHost;
	@Value("${wowza.stream.port:1935}")
	private String wowzaPort;
	
	public ContentService(
			@Autowired CmsProxyService cmsService,
			@Autowired TenantRepository tenantRepo,
			@Autowired ContentRepository contentRepo) {
		this.cmsService = cmsService;
		this.contentRepo = contentRepo;
	}
	
	@PostConstruct
	public void init() {
		if(contentRepo.count() == 0) {
			Content testContent = new Content();
			testContent.setBuiltin(true);
			testContent.setContentId(CommonUtils.getUUID());
			testContent.setDescription("Vod Sample Content (mpeg dash)");
			testContent.setName("sample.mp4");
			testContent.setTitle("VOD Sample");
			testContent.setType(ContentType.vod.name());
			StringBuffer sb = new StringBuffer();
			sb.append(wowzaSsl ? CommonConstants.HTTPS : CommonConstants.HTTP)
			  .append(CommonConstants.SCHEME)
			  .append(wowzaHost).append(CommonConstants.COLON).append(wowzaPort)
			  .append("/vod/mp4:sample.mp4/manifest.mpd");
			testContent.setUrl(sb.toString());
			contentRepo.save(testContent);
		}
	}
	
	public List<Content> getContentList() {
		return contentRepo.findAll();
	}
	
	public Content getContent(String contentId) {
		return contentRepo.findById(contentId).orElse(null);
	}
	
	public boolean syncContentList() {
		try {
			List<Content> contents = cmsService.getContentList();
			List<String> contentIds = new ArrayList<>();
			for (Content content : contents) {
				contentRepo.save(content);
				contentIds.add(content.getContentId());
			}
			
			if(contentIds.size() > 0) {
				contentRepo.deleteAllByIdNotInQuery(contentIds);
			} else {
				contentRepo.deleteAllNotLink();
			}
			return true;
		} catch(Throwable ex) {
			return false;
		}
	}
	
	public boolean save(Content content, FileBuffer buffer) {
		if(StringUtils.isBlank(content.getContentId())) {
			content.setContentId(CommonUtils.getUUID());
		}
		
		if(StringUtils.equals(content.getType(), ContentType.link.name())) {
			contentRepo.save(content);
			return true;
		} else {
			if(cmsService.save(content, buffer)) {
				contentRepo.save(content);
				return true;
			} else {
				return false;
			}
		}
	}
	
	public boolean deleteContent(Content content) {
		if(StringUtils.equals(content.getType(), ContentType.link.name())) {
			contentRepo.delete(content);
			return true;
		} else {
			return deleteContent(content.getContentId());
		}
	}
	
	public boolean deleteContent(String contentId) {
		if(cmsService.deleteContent(contentId)) {
			contentRepo.deleteById(contentId);
			return true;
		} else {
			return false;
		}
	}
}
