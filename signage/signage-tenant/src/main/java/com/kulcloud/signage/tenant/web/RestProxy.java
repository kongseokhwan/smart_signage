package com.kulcloud.signage.tenant.web;

import java.net.URL;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.kulcloud.signage.commons.utils.CommonConstants;

@RequestMapping("/proxy")
@Controller
public class RestProxy {
	private static final Logger logger = LoggerFactory.getLogger(RestProxy.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping
	@ResponseBody
	public String proxy(@RequestParam("url") String urlString, @RequestBody(required=false) String body, HttpMethod method, 
			HttpServletRequest request, HttpServletResponse response)
	{
		HttpHeaders header = getHttpHeaders(request);
		String responseBody;
		try {
    		ResponseEntity<String> responseEntity =
    	        restTemplate.exchange(urlString, method, new HttpEntity<String>(body, header), String.class);
    		
    		responseBody = responseEntity.getBody();
    		StringBuffer sb = new StringBuffer();
    		try {
    			URL url = new URL(urlString);
    			sb.append("<base href=\"").append(url.getProtocol()).append(CommonConstants.SCHEME);
    			sb.append(url.getHost()).append(url.getPort() == -1 ? "" : ":" + url.getPort());
    			sb.append("\">");
    		} catch (Exception ex) {
    			logger.warn("Cannot parse a url: " + urlString);
    		}
    		
    		sb.append(responseBody);
    		responseBody = sb.toString();
    		logger.info("Load " + urlString + " in " + request.getRemoteAddr());
    	} catch (HttpClientErrorException ex) {
    		logger.error("Cannot load " + urlString + " in " + request.getRemoteAddr());
		    if(ex.getStatusCode() == HttpStatus.NOT_ACCEPTABLE ||
		            ex.getStatusCode() == HttpStatus.BAD_REQUEST) {
		        responseBody = ex.getResponseBodyAsString();
		    } else {
		        throw ex;
		    }
		}
	    
	    return responseBody;
	}
	
	private HttpHeaders getHttpHeaders(HttpServletRequest request) {
	    return this.getHttpHeaders(request, null);
	}
	
	private HttpHeaders getHttpHeaders(HttpServletRequest request, HttpHeaders headers) {
	    if(headers == null) {
	        headers = new HttpHeaders();
	    }
	    
	    Enumeration<String> requestHeaders = request.getHeaderNames();
	    String headerName;
	    while(requestHeaders.hasMoreElements()) {
	        headerName = requestHeaders.nextElement();
	        if(StringUtils.equalsAny(headerName, "host", "referer")) {
	        	continue;
	        }
	        
	        headers.add(headerName, request.getHeader(headerName));
	    }
	    
	    return headers;
	}
}
