package com.kulcloud.signage.cms.html;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kulcloud.signage.cms.ContentService;
import com.kulcloud.signage.cms.StorageService;
import com.kulcloud.signage.cms.data.ContentPK;
import com.kulcloud.signage.cms.html.data.HtmlContent;
import com.kulcloud.signage.cms.html.data.HtmlContentRepository;
import com.kulcloud.signage.commons.enums.ContentType;
import com.kulcloud.signage.commons.utils.CommonConstants;
import com.kulcloud.signage.commons.utils.CommonUtils;

@Service
public class HtmlContentService implements StorageService, ContentService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private String html = ContentType.html.name();
	
	@Value("${com.kulcloud.signage.webPath:/web}")
	private String webPath;
	@Value("${com.kulcloud.signage.webDir:./web}")
	private String webDir;
	private String htmlDir;
	@Autowired
	private HtmlContentRepository htmlRepo;
	
	private Tika tika = new Tika();
	
	@PostConstruct
	private void init() {
		this.htmlDir = webDir + File.separator + html;
		File htmlDir = new File(webDir, this.htmlDir);
		try {
			FileUtils.forceMkdir(htmlDir);
		} catch (IOException e) {
			try {
				FileUtils.forceDelete(htmlDir);
				FileUtils.forceMkdir(htmlDir);
			} catch (IOException ignored) {}
		}
	}
	@Override
	public boolean createStorage(String tenantId) {
		File tenantDir = new File(htmlDir, tenantId);
		if(!tenantDir.exists()) {
			try {
				FileUtils.forceMkdir(tenantDir);
			} catch (IOException e) {
				logger.error("Cannot create a storage of html: " + tenantId, e);
				return false;
			}
		}
		
		return true;
	}

	@Override
	public boolean deleteStorage(String tenantId) {
		File tenantDir = new File(htmlDir, tenantId);
		if(tenantDir.exists()) {
			try {
				FileUtils.forceDelete(tenantDir);
			} catch (IOException e) {
				logger.error("Cannot delete a storage of web: " + tenantId, e);
				return false;
			}
		}
		
		return true;
	}
	
	public List<HtmlContent> findAllByTenantId(String tenantId) {
		return htmlRepo.findByIdTenantId(tenantId);
	}
	
	public List<HtmlContent> findAllByTenantId(String tenantId, Pageable pageable) {
		return htmlRepo.findByIdTenantId(tenantId, pageable);
	}
	
	public void save(Map<String, Object> content, MultipartFile file) {
		ContentPK id = new ContentPK(content);
		HtmlContent html = htmlRepo.findById(id).orElse(null);
		if(html == null) {
			html = new HtmlContent(content);
		}
		
		if(file != null) {
			html.setUrl(uploadWebContent(html.getId(), file));
		}
		htmlRepo.save(html);
	}
	
	public void delete(String tenantId, String contentId) {
		ContentPK id = new ContentPK(tenantId, contentId);
		HtmlContent content = htmlRepo.findById(id).orElse(null);
		if(content != null) {
			deleteWebContent(tenantId, content.getUrl());
			htmlRepo.delete(content);
		}
	}
	
	private String uploadWebContent(ContentPK id, MultipartFile htmlFile) {
		try {
			File fileDir = new File(new File(htmlDir, id.getTenantId()), id.getContentId());
			File file = new File(fileDir, htmlFile.getOriginalFilename());
			if(file.exists()) {
				FileUtils.forceDelete(file);
			}
			
			htmlFile.transferTo(file);
			String mediaType = tika.detect(file);
			if(mediaType != null && mediaType.equals("application/zip")) {
				File dest = new File(file.getParentFile(), file.getName());
				CommonUtils.unzip(file, dest);
				String indexFileName = null;
				for (String fileName : dest.list()) {
					if(StringUtils.equalsAny(fileName, "index.html", "index.htm")) {
						break;
					}
					
					if(StringUtils.endsWithAny(fileName, ".html", ".htm")) {
						indexFileName = fileName;
					}
				}
				
				if(indexFileName == null) {
					return webPath + CommonConstants.SLASH + html + CommonConstants.SLASH + id.getTenantId() + CommonConstants.SLASH + id.getContentId() + CommonConstants.SLASH + htmlFile.getOriginalFilename();
				} else {
					return webPath + CommonConstants.SLASH + html + CommonConstants.SLASH + id.getTenantId() + CommonConstants.SLASH + id.getContentId() + CommonConstants.SLASH + file.getName() + CommonConstants.SLASH + indexFileName;
				}
			} else {
				return webPath + CommonConstants.SLASH + html + CommonConstants.SLASH + id.getTenantId() + CommonConstants.SLASH + id.getContentId() + CommonConstants.SLASH + htmlFile.getOriginalFilename();
			}
		} catch (IOException e) {
			logger.error("Cannot upload a file of web: " + htmlFile.getOriginalFilename() + ", " + id.getTenantId());
			return null;
		}
	}
	
	private boolean deleteWebContent(String tenantId, String htmlFilePath) {
		String htmlFileName = webDir + htmlFilePath.substring(htmlFilePath.indexOf(webPath) + webPath.length());
		File htmlFile = new File(htmlFileName);
		if(htmlFile.exists()) {
			try {
				FileUtils.forceDelete(htmlFile);
			} catch (IOException e) {
				logger.error("Cannot delete a file of web: " + htmlFilePath + ", " + tenantId);
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	public String supportType() {
		return html;
	}
}
