package com.kulcloud.signage.tenant.device.chromecast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration  
@EnableWebMvc
@EnableWebSocket
public class ChromeCastWebConfig implements WebMvcConfigurer, WebSocketConfigurer {
	public static final String castHandlerPath = "/ws/cast";
	@Autowired
	private ChromeCastRepository castRepo;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/cast", "/cast/index.html");
    }

	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(castRepo, castHandlerPath).setAllowedOrigins("*").withSockJS();
	}

}