package com.kulcloud.signage.tenant.content;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;

import com.kulcloud.signage.commons.SignageRestTemplate;
import com.kulcloud.signage.commons.dto.RestResult;
import com.kulcloud.signage.commons.dto.SignageTenantDto;
import com.kulcloud.signage.commons.enums.ContentType;
import com.kulcloud.signage.commons.security.CryptService;
import com.kulcloud.signage.commons.security.SecurityUtils;
import com.kulcloud.signage.commons.utils.CommonConstants;
import com.kulcloud.signage.commons.utils.CommonUtils;
import com.kulcloud.signage.tenant.data.ContentRepository;
import com.kulcloud.signage.tenant.data.TenantRepository;
import com.kulcloud.signage.tenant.data.entity.Content;
import com.kulcloud.signage.tenant.data.entity.Tenant;
import com.vaadin.flow.component.upload.receivers.FileBuffer;

@Service
public class CmsProxyService {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	private CryptService crypt = new CryptService();
	private String apiKey;
	private String publicServerUrl;
	private String publicApiPath;
	private SignageRestTemplate restTemplate;
	
	private TenantRepository tenantRepo;
	private AsyncTaskExecutor executor;
	private String tenantId;
	
	public CmsProxyService(
			@Value("${com.kulcloud.signage.tenant}") String tenantId,
			@Value("${com.kulcloud.signage.password:}") String password,
			@Value("${com.kulcloud.signage.userdata}") String userdata,
			@Value("${com.kulcloud.signage.publicServerUrl:http://localhost:9500}") String publicServerUrl,
			@Value("${com.kulcloud.signage.publicApiPath:/signage/public/api}") String publicApiPath,
			@Value("${com.kulcloud.signage.configDir:./config}") String configDir,
			@Autowired TenantRepository tenantRepo,
			@Autowired ContentRepository contentRepo,
			@Autowired AsyncTaskExecutor executor
	) {
		this.tenantRepo = tenantRepo;
		this.executor = executor;
		this.tenantId = tenantId;
		
		Tenant tenant = tenantRepo.findById(tenantId).orElse(null);
		if(tenant == null) {
			String pwd = StringUtils.isEmpty(password) ? userdata : password;
			tenant = new Tenant(tenantId, SecurityUtils.pwdEncoder.encode(pwd), CommonUtils.getOutboundIp());
			tenantRepo.save(tenant);
		}
		
		this.publicServerUrl = publicServerUrl;
		this.publicApiPath = publicApiPath;
		this.restTemplate = new SignageRestTemplate(publicServerUrl, executor);
	}

	public String getApiKey() {
		Tenant tenant = tenantRepo.findById(tenantId).orElse(null);
		if(tenant == null) {
			return null;
		}
		
		if(apiKey == null || !crypt.isValidApiKey(tenant.getPassword(), apiKey)) {
			loadApiKey();
		}
		
		return apiKey;
	}

	public SignageTenantDto getTenantDto() {
		Tenant tenant = tenantRepo.findById(tenantId).orElse(null);
		return convertTenantDto(tenant);
	}
	
	private SignageTenantDto convertTenantDto(Tenant tenant) {
		if(tenant == null) {
			return null;
		} else {
			SignageTenantDto tenantDto = new SignageTenantDto(tenant.getTenantId(), tenant.getPassword(), tenant.getIpAddress());
			return tenantDto;
		}
	}
	
	@PostConstruct
	@Async
	private void init() {
		loadApiKey();
		executor.execute(() -> createStorage());
	}
	
	private boolean loadApiKey() {
		Tenant tenant = tenantRepo.findById(tenantId).orElse(null);
		if(tenant == null) {
			return false;
		}
		
		String otp = crypt.createOtp();
		HttpHeaders headers = new HttpHeaders();
		headers.set(CommonConstants.HEADER_OTP, otp);
		String apiUrl = getApiPath("/tenant");
		this.apiKey = null;
		try {
			RestResult result = restTemplate.request(apiUrl, HttpMethod.POST, convertTenantDto(tenant), headers, RestResult.class);
			if(result.isResult()) {
				this.apiKey = result.getData().toString();
				tenant.setApiKey(this.apiKey);
				tenantRepo.save(tenant);
				return true;
			} else {
				logger.error(result.getException());
				return false;
			}
		} catch (RestClientException e) {
			logger.error("Cannot load a api key: " + e.getMessage());
			return false;
		}
	}
	
	private boolean createStorage() {
		HttpHeaders headers = getApiHeaders();
		if(headers != null) {
			String apiUrl = getApiPath("/tenant/" + tenantId + "/storage");
			RestResult result = restTemplate.request(apiUrl, HttpMethod.PUT, null, headers, RestResult.class);
			return result.isResult();
		} else {
			return false;
		}
	}
	
	public List<Content> getContentList() {
		checkApiKey();
		HttpHeaders headers = getApiHeaders();
		if(headers == null) {
			return null;
		}
		
		String apiUrl = getApiPath("/tenant/" + tenantId + "/contents");
		List<Content> contents = new ArrayList<>();
		Map<String, Future<RestResult>> futures = new HashMap<>();
		for(String type: ContentType.names()) {
			if(!type.equals(ContentType.link.name())) {
				Future<RestResult> result = restTemplate.asyncRequest(apiUrl + "?type=" + type, HttpMethod.GET, null, headers, RestResult.class);
				futures.put(type, result);
			}
		}
		
		for (String type : futures.keySet()) {
			try {
				RestResult result = futures.get(type).get();
				if(result.isResult()) {
					@SuppressWarnings("unchecked")
					List<Map<?, ?>> publicContents = (List<Map<?, ?>>) CommonUtils.convertTo(List.class, result.getData());
					Content content;
					for (Map<?, ?> publicContent : publicContents) {
						content = new Content();
						content.setContentId(((Map<?, ?>)publicContent.get("id")).get("content_id").toString());
						content.setName(CommonUtils.convertToString(publicContent.get("name")));
						content.setTitle(CommonUtils.convertToString(publicContent.get("title")));
						content.setDescription(CommonUtils.convertToString(publicContent.get("description")));
						content.setUrl(CommonUtils.convertToString(publicContent.get("url")));
						content.setBuiltin(CommonUtils.convertToBoolean(publicContent.get("builtin")));
						content.setType(type);
						contents.add(content);
					}
				}
			} catch (InterruptedException | ExecutionException e) {
				logger.error("Cannot get contents: " + type);
			}
		}
		
		return contents; 
	}
	
	public boolean save(Content content, FileBuffer buffer) {
		boolean fileBasedContent = isFileBasedContent(content);
		if(fileBasedContent && buffer == null) {
			return false;
		}
		
		if(StringUtils.isEmpty(content.getContentId()) && 
				fileBasedContent && 
				StringUtils.isEmpty(buffer.getFileName())) {
			logger.error("It is not a file");
			return false;
		}
		
		checkApiKey();
		HttpHeaders headers = getApiHeaders();
		if(headers == null) {
			return false;
		}
		
		Object body;
		if(buffer != null && !StringUtils.isEmpty(buffer.getFileName())) {
			MultiValueMap <String, Object> bodyMap = new LinkedMultiValueMap<String, Object> ();
			Map<?, ?> map = CommonUtils.convertToMap(content);
			for (Object key: map.keySet()) {
				bodyMap.add(key.toString(), map.get(key));
			}
			bodyMap.add("file", new InputStreamResource(buffer.getInputStream()) {

				@Override
				public String getFilename() {
					return buffer.getFileName();
				}
				
			});
			
			body = bodyMap;
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		} else {
			body = content;
			headers.setContentType(MediaType.APPLICATION_JSON);
		}
		
		String apiUrl = getApiPath("/tenant/" + tenantId + "/contents");
		HttpMethod method;
		if(StringUtils.isEmpty(content.getContentId())) {
			method = HttpMethod.POST;
		} else {
			method = HttpMethod.PUT;
			apiUrl += "/" + content.getContentId();
		}
		
		apiUrl += "?type=" + content.getType();
		try {
			RestResult result = restTemplate.request(apiUrl, method, body, headers, RestResult.class);
			if(result.isResult()) {
				Map<?, ?> map = CommonUtils.convertToMap(result.getData());
				if(fileBasedContent && map.get("url") != null) {
					content.setUrl(map.get("url").toString());
				}
			}
			return result.isResult();
		} catch (RestClientException e) {
			logger.error("Cannot save a content: " + content);
			return false;
		}
	}
	
	private boolean isFileBasedContent(Content content) {
		return StringUtils.equalsAny(content.getType(), ContentType.vod.name(), ContentType.html.name(), ContentType.slide.name());
	}
	
	public boolean deleteContent(String contentId) {
		if(StringUtils.isEmpty(contentId)) {
			return false;
		}
		
		String apiUrl = getApiPath("/tenant/" + tenantId + "/content/" + contentId);
		try {
			checkApiKey();
			RestResult result = restTemplate.request(apiUrl, HttpMethod.DELETE, null, getApiHeaders(), RestResult.class);
			return result.isResult();
		} catch (RestClientException e) {
			logger.error("Cannot delete a content: " + contentId);
			return false;
		}
	}
	
	private String getApiPath(String apiPath) {
		StringBuffer sb = new StringBuffer();
		return sb.append(publicServerUrl).append(publicApiPath).append(apiPath).toString();
	}
	
	private HttpHeaders getApiHeaders() {
		if(StringUtils.isEmpty(apiKey)) {
			return null;
		} else {
			HttpHeaders headers = new HttpHeaders();
			headers.set(CommonConstants.HEADER_KEY, apiKey);
			return headers;
		}
	}
	
	private boolean checkApiKey() {
		Tenant tenant = tenantRepo.findById(tenantId).orElse(null);
		if(tenant == null) {
			return false;
		}
		
		return checkApiKey(tenant);
	}
	
	private boolean checkApiKey(Tenant tenant) {
		if(apiKey != null && crypt.isValidApiKey(tenant.getPassword(), apiKey)) {
			return true;
		}
		
		return loadApiKey();
	}
}
