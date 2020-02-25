package com.kulcloud.signage.cms.wowza;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.stereotype.Component;

import com.kulcloud.signage.commons.SignageRestTemplate;

@Component
public class WowzaRestTemplate extends SignageRestTemplate{

	@Autowired
    public WowzaRestTemplate(
    		@Value("${wowza.api.ssl:false}") boolean ssl,
    		@Value("${wowza.host:localhost}") String host,
    		@Value("${wowza.api.port:8087}") int apiPort,
    		@Value("${wowza.api.user:}") String user,
    		@Value("${wowza.api.password:}") String password,
    		@Autowired AsyncTaskExecutor executor) {
    	super(ssl, host, apiPort, "Wowza", user, password, executor);
    }
}