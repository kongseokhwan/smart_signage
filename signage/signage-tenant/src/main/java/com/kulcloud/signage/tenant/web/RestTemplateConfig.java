package com.kulcloud.signage.tenant.web;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	    HttpClientBuilder builder = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier());
		requestFactory.setHttpClient(builder.build());
        return new RestTemplate(requestFactory);
	}
}
