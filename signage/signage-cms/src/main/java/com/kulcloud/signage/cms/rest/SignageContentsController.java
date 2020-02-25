package com.kulcloud.signage.cms.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kulcloud.signage.cms.data.Content;
import com.kulcloud.signage.cms.html.HtmlContentService;
import com.kulcloud.signage.cms.html.data.HtmlContentRepository;
import com.kulcloud.signage.cms.slide.data.SlideContentRepository;
import com.kulcloud.signage.cms.streaming.data.LiveContentRepository;
import com.kulcloud.signage.cms.streaming.data.VodContentRepository;
import com.kulcloud.signage.commons.dto.RestResult;
import com.kulcloud.signage.commons.utils.CommonConstants;

// com.kulcloud.signage.publicApiPath = /signage/public/api
@RestController
@RequestMapping(path = "${com.kulcloud.signage.publicApiPath}/tenant/{tenantId}/contents")
public class SignageContentsController {

	@Autowired
	private HtmlContentRepository webRepo;
	@Autowired
	private HtmlContentService webContentService;
	@Autowired
	private VodContentRepository vodRepo;
	@Autowired
	private LiveContentRepository liveRepo;
	@Autowired
	private SlideContentRepository slideRepo;
	
	@GetMapping
	public RestResult getContents(
			@PathVariable("tenantId") String tenantId,
			@RequestParam(name = "type") String type,
			@RequestHeader(CommonConstants.HEADER_KEY) String apiKey) {
		
		List<Content> result = new ArrayList<>();
		
		switch(type) {
		case "web":
			result.addAll(webRepo.findByIdTenantId(tenantId));
			break;
		case "vod":
			result.addAll(vodRepo.findByIdTenantId(tenantId));
			break;
		case "live":
			result.addAll(liveRepo.findByIdTenantId(tenantId));
			break;
		case "slide":
			result.addAll(slideRepo.findByIdTenantId(tenantId));
			break;
		}
		
		return new RestResult(result);
	}
	
	@GetMapping(params = { "page", "size" })
	public RestResult getContents(
			@PathVariable("tenantId") String tenantId,
			@RequestParam(name = "type") String type,
			@RequestHeader(CommonConstants.HEADER_KEY) String apiKey,
			Pageable pageable) {
		
		List<Content> result = new ArrayList<>();
		
		switch(type) {
		case "web":
			result.addAll(webRepo.findByIdTenantId(tenantId, pageable));
			break;
		case "vod":
			result.addAll(vodRepo.findByIdTenantId(tenantId, pageable));
			break;
		case "live":
			result.addAll(liveRepo.findByIdTenantId(tenantId, pageable));
			break;
		case "slide":
			result.addAll(slideRepo.findByIdTenantId(tenantId, pageable));
			break;
		}
		
		return new RestResult(result);
	}
	
	@PostMapping(consumes = {"application/json"})
	public RestResult postContent(
			@RequestHeader(CommonConstants.HEADER_KEY) String apiKey,
			@RequestParam(name = "type") String type,
			@PathVariable("tenantId") String tenantId,
			@RequestBody Map<String, Object> content
			) {
		System.out.println(content);
		return new RestResult();
	}
	
	@PostMapping(consumes = {"multipart/form-data"})
	public RestResult postContentWithFile(
			@RequestHeader(CommonConstants.HEADER_KEY) String apiKey,
			@RequestParam(name = "type") String type,
			@PathVariable("tenantId") String tenantId,
			@ModelAttribute Map<String, Object> content,
			MultipartFile file
			) {
		System.out.println(content);
		return new RestResult();
	}
	
	@PutMapping(path="/{contentId}", consumes = {"application/json"})
	public RestResult putContent(
			@RequestHeader(CommonConstants.HEADER_KEY) String apiKey,
			@RequestParam(name = "type") String type,
			@PathVariable("tenantId") String tenantId,
			@PathVariable("contentId") String contentId,
			@RequestBody Map<String, Object> content
			) {
		System.out.println(content);
		return new RestResult();
	}
	
	@PutMapping(path="/{contentId}", consumes = {"multipart/form-data"})
	public RestResult putContentWithFile(
			@RequestHeader(CommonConstants.HEADER_KEY) String apiKey,
			@RequestParam(name = "type") String type,
			@PathVariable("tenantId") String tenantId,
			@PathVariable("contentId") String contentId,
			@ModelAttribute Map<String, Object> content,
			MultipartFile file
			) {
		System.out.println(content);
		return new RestResult();
	}
	
	@DeleteMapping(path="/{contentId}")
	public RestResult deleteContent(
			@RequestHeader(CommonConstants.HEADER_KEY) String apiKey,
			@PathVariable("tenantId") String tenantId,
			@PathVariable("contentId") String contentId
			) {
		System.out.println(contentId);
		return new RestResult();
	}
}
