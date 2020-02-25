package com.kulcloud.signage.commons;

import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kulcloud.signage.commons.utils.CommonConstants;

public class SignageRestTemplate {

	private AsyncTaskExecutor executor;
	private RestTemplate restTemplate;
    private String hostUrl;
    
    public SignageRestTemplate(
    		String hostUrl,
    		AsyncTaskExecutor executor) {
    	this(hostUrl, null, null, null, executor);
    }
    
    public SignageRestTemplate(
    		String hostUrl,
    		String realm,
    		String user,
    		String password,
    		AsyncTaskExecutor executor) {
    	boolean ssl = hostUrl.startsWith("https") ? true : false;
    	String host = hostUrl.substring(hostUrl.indexOf("://") + 3);
    	int port = 80;
    	if(host.indexOf( ":") > 0) {
    		port = Integer.parseInt(host.substring(host.indexOf(":") + 1));
    		host = host.substring(0, host.indexOf(":"));
    	}
    	
    	init(ssl, host, port, realm, user, password, executor);
    }
    
    public SignageRestTemplate(
    		boolean ssl,
    		String host,
    		int port,
    		String realm,
    		String user,
    		String password,
    		AsyncTaskExecutor executor) {
    	init(ssl, host, port, realm, user, password, executor);
    }
    
    private void init(
    		boolean ssl,
    		String host,
    		int port,
    		String realm,
    		String user,
    		String password,
    		AsyncTaskExecutor executor) {
    	String scheme = ssl ? CommonConstants.HTTPS : CommonConstants.HTTP;
    	if(StringUtils.isAnyBlank(realm, user, password)) {
    		restTemplate = createRestTemplate(scheme, host, port);
    	} else {
    		restTemplate = createRestTemplate(scheme, host, port, realm, user, password);
    	}
    	
    	this.executor = executor;
    	this.hostUrl = scheme + CommonConstants.SCHEME + host + CommonConstants.COLON + port;
    }
    
    private RestTemplate createRestTemplate(String scheme, String host, int port) {
    	HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	    HttpClientBuilder builder = HttpClients.custom();
        if(scheme.equals("https")) {
			builder = builder.setSSLHostnameVerifier(new NoopHostnameVerifier());
		}
	    requestFactory.setHttpClient(builder.build());
        return new RestTemplate(requestFactory);
	}
    
    private RestTemplate createRestTemplate(String scheme, String host, int port, String realm, String user, String password) {
    	HttpHost httpHost = new HttpHost(host, port, scheme);
    	
    	CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials = 
          new UsernamePasswordCredentials(user, password);
        provider.setCredentials(AuthScope.ANY, credentials);
        
        HttpClientBuilder builder = HttpClients.custom().setDefaultCredentialsProvider(provider);
        if(scheme.equals("https")) {
			builder = builder.setSSLHostnameVerifier(new NoopHostnameVerifier());
		}
        
	    return new RestTemplate(new SignageHttpComponentsClientHttpRequestFactoryDigestAuth(builder.build(), httpHost, realm));
	}
    
    public <T> Future<T> asyncRequest(String pathToResource, HttpMethod method, String body, Class<T> responseClass) {
    	return executor.submit(() -> request(pathToResource, method, body, responseClass));
    }
    
    public <T> T request(String pathToResource, HttpMethod method, Object body, Class<T> responseClass) throws RestClientException{
    	try {
			return request(pathToResource, method, CommonConstants.mapper.writeValueAsString(body), null, responseClass);
		} catch (JsonProcessingException e) {
			throw new RestClientException(e.getMessage(), e);
		}
    }
    
    public <T> T request(String pathToResource, HttpMethod method, String body, Class<T> responseClass) throws RestClientException{
    	
    	return request(pathToResource, method, body, null, responseClass);
    }
    
    public <T> T request(String pathToResource, HttpMethod method, Object body, HttpHeaders headers, Class<T> responseClass) throws RestClientException{
    	try {
			return request(pathToResource, method, CommonConstants.mapper.writeValueAsString(body), headers, responseClass);
		} catch (JsonProcessingException e) {
			throw new RestClientException(e.getMessage(), e);
		}
    }
    
    public <T> Future<T> asyncRequest(String pathToResource, HttpMethod method, String body, HttpHeaders headers, Class<T> responseClass) {
    	return executor.submit(() -> request(pathToResource, method, body, headers, responseClass));
    }
    
    public <T> T request(String pathToResource, HttpMethod method, String body, HttpHeaders headers, Class<T> responseClass) throws RestClientException{
    	if(headers == null) {
    		headers = new HttpHeaders();
    	}
    	
    	headers.add("Accept", "application/json");
    	headers.add("Content-Type", "application/json");
    	headers.add("charset", "utf-8");
    	
    	String uri;
    	if(pathToResource.startsWith(hostUrl)) {
    		uri = pathToResource;
    	} else {
    		uri = hostUrl + (pathToResource.startsWith("/") ? pathToResource : "/" + pathToResource);
    	}
    	
    	return restTemplate.exchange(uri, method, new HttpEntity<String>(body, headers), responseClass).getBody();
    }
    
    
}