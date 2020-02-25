package com.kulcloud.signage.tenant.device.rpi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import com.kulcloud.signage.commons.utils.CommonUtils;

public class RpiStompConfig implements WebSocketMessageBrokerConfigurer {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private RpiStompProperties stompProperties;
	
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint(stompProperties.getEndPoint());
        registry.addEndpoint(stompProperties.getEndPoint()).withSockJS();
        logger.info("Enabled a STOMP Broker on SockJS: " + CommonUtils.getOutboundIp() + ", " + stompProperties.getEndPoint());
	}

	@Override
	public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
		registry.setMessageSizeLimit(2* 1024 * 1024);
        registry.setSendBufferSizeLimit(2* 1024 * 1024);
        registry.setSendTimeLimit(20000);
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		registry.setApplicationDestinationPrefixes(stompProperties.getAppPrefix());
		registry.enableSimpleBroker(stompProperties.getTopicPrefix());
		registry.setApplicationDestinationPrefixes(stompProperties.getTopicPrefix());
	}
}
