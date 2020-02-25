package com.kulcloud.signage.tenant.device.rpi;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.kulcloud.signage.commons.utils.CommonUtils;

@Configuration
@ConfigurationProperties(prefix = "com.kulcloud.signage.tenant.device.rpi.stomp")
public class RpiStompProperties {
	
	private String ip;
	@Value("${server.port:8080}")
	private int port;
	
	private String endPoint;
	private String appPrefix;
	private String topicPrefix;
	
	private String brokerUri;
	private String clientTimeout;
	private int reconnTimeToBroker;

	@PostConstruct
	public void init() {
		if(ip == null) {
			ip = CommonUtils.getOutboundIp();
		}
		
		if(endPoint == null) {
			endPoint = "/stomp";
		}
		
		if(appPrefix == null) {
			appPrefix = "/app";
		}
		
		if(topicPrefix == null) {
			topicPrefix = "/topic";
		}
		
		if(brokerUri == null) {
			brokerUri = "";
		}
		
		if(clientTimeout == null) {
			clientTimeout = "60000";
		}
		
		if(reconnTimeToBroker < 1) {
			reconnTimeToBroker = 6;
		}
	}
	
	public String getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(String endPoint) {
		this.endPoint = endPoint;
	}

	public String getAppPrefix() {
		return appPrefix;
	}

	public void setAppPrefix(String appPrefix) {
		this.appPrefix = appPrefix;
	}

	public String getTopicPrefix() {
		return topicPrefix;
	}

	public void setTopicPrefix(String topicPrefix) {
		this.topicPrefix = topicPrefix;
	}

	public String getBrokerUri() {
		return brokerUri;
	}

	public void setBrokerUri(String brokerUri) {
		this.brokerUri = brokerUri;
	}

	public String getClientTimeout() {
		return clientTimeout;
	}

	public void setClientTimeout(String clientTimeout) {
		this.clientTimeout = clientTimeout;
	}

	public int getReconnTimeToBroker() {
		return reconnTimeToBroker;
	}

	public void setReconnTimeToBroker(int reconnTimeToBroker) {
		this.reconnTimeToBroker = reconnTimeToBroker;
	}

}
