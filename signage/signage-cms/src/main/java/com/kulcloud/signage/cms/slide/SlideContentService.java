package com.kulcloud.signage.cms.slide;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
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
import com.kulcloud.signage.cms.slide.data.SlideContent;
import com.kulcloud.signage.cms.slide.data.SlideContentRepository;
import com.kulcloud.signage.commons.enums.ContentType;
import com.kulcloud.signage.commons.utils.CommonConstants;

@Service
public class SlideContentService implements StorageService, ContentService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	private String slide = ContentType.slide.name();
	
	@Value("${com.kulcloud.signage.webPath:/web}")
	private String webPath;
	@Value("${com.kulcloud.signage.webDir:./web}")
	private String webDir;
	private String slideDir;
	@Autowired
	private SlideContentRepository slideRepo;
	
	private Tika tika = new Tika();
	
	@PostConstruct
	private void init() {
		this.slideDir = webDir + File.separator + slide;
		File slideDir = new File(webDir, this.slideDir);
		try {
			FileUtils.forceMkdir(slideDir);
		} catch (IOException e) {
			try {
				FileUtils.forceDelete(slideDir);
				FileUtils.forceMkdir(slideDir);
			} catch (IOException ignored) {}
		}
	}
	
	@Override
	public boolean createStorage(String tenantId) {
		File tenantDir = new File(slideDir, tenantId);
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
		File tenantDir = new File(slideDir, tenantId);
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

	public List<SlideContent> findAllByTenantId(String tenantId) {
		return slideRepo.findByIdTenantId(tenantId);
	}
	
	public List<SlideContent> findAllByTenantId(String tenantId, Pageable pageable) {
		return slideRepo.findByIdTenantId(tenantId, pageable);
	}
	
	public void save(Map<String, Object> content, MultipartFile file) {
		ContentPK id = new ContentPK(content);
		SlideContent slide = slideRepo.findById(id).orElse(null);
		if(slide == null) {
			slide = new SlideContent(content, file.getOriginalFilename());
		}
		
		if(file != null) {
			String odfFilePath = convertSlideToOdf(slide.getId(), file);
			slide.setOdfFileName(odfFilePath.substring(odfFilePath.lastIndexOf(CommonConstants.SLASH) + 1));
			slide.setUrl(odfFilePath);
		}
		slideRepo.save(slide);
	}
	
	public void delete(String tenantId, String contentId) {
		ContentPK id = new ContentPK(tenantId, contentId);
		SlideContent content = slideRepo.findById(id).orElse(null);
		if(content != null) {
			deleteSlideFile(tenantId, content.getUrl());
			slideRepo.delete(content);
		}
	}
	
	private String convertSlideToOdf(ContentPK id, MultipartFile htmlFile) {
		try {
			File fileDir = new File(new File(slideDir, id.getTenantId()), id.getContentId());
			File file = new File(fileDir, htmlFile.getOriginalFilename());
			if(file.exists()) {
				FileUtils.forceDelete(file);
			}
			
			htmlFile.transferTo(file);
			String mediaType = tika.detect(file);
			if(mediaType != null && mediaType.equals("application/vnd.ms-powerpoint")) {
				String odfFileName = file.getName() + "odf";
				// TODO ppt to odf
				return webPath + CommonConstants.SLASH + slide + CommonConstants.SLASH + id.getTenantId() + CommonConstants.SLASH + id.getContentId() + CommonConstants.SLASH + file.getName() + CommonConstants.SLASH + odfFileName;
			} else {
				return webPath + CommonConstants.SLASH + slide + CommonConstants.SLASH + id.getTenantId() + CommonConstants.SLASH + id.getContentId() + CommonConstants.SLASH + htmlFile.getOriginalFilename();
			}
		} catch (IOException e) {
			logger.error("Cannot upload a file of web: " + htmlFile.getOriginalFilename() + ", " + id.getTenantId());
			return null;
		}
	}
	
	private boolean deleteSlideFile(String tenantId, String slideFilePath) {
		String slideFileName = webDir + slideFilePath.substring(slideFilePath.indexOf(webPath) + webPath.length());
		File slideFile = new File(slideFileName);
		if(slideFile.exists()) {
			try {
				FileUtils.forceDelete(slideFile);
			} catch (IOException e) {
				logger.error("Cannot delete a file of web: " + slideFilePath + ", " + tenantId);
				return false;
			}
		}
		
		return true;
	}

	@Override
	public String supportType() {
		return slide;
	}
}
