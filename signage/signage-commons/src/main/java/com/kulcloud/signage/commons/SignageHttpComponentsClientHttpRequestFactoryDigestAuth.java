package com.kulcloud.signage.commons;

import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.client.AuthCache;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.auth.DigestScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

public class SignageHttpComponentsClientHttpRequestFactoryDigestAuth 
	extends HttpComponentsClientHttpRequestFactory {

	private HttpHost host;
	private String realm;
	
	public SignageHttpComponentsClientHttpRequestFactoryDigestAuth(HttpClient httpClient, HttpHost host, String realm) {
		super(httpClient);
		this.host = host;
		this.realm = realm;
	}

	@Override
	protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
		return createHttpContext();
	}
	
	private HttpContext createHttpContext() {
        // Create AuthCache instance
        AuthCache authCache = new BasicAuthCache();
        // Generate DIGEST scheme object, initialize it and add it to the local auth cache
        DigestScheme digestAuth = new DigestScheme();
        // If we already know the realm name
        digestAuth.overrideParamter("realm", realm);
        authCache.put(host, digestAuth);
 
        // Add AuthCache to the execution context
        BasicHttpContext localcontext = new BasicHttpContext();
        localcontext.setAttribute(HttpClientContext.AUTH_CACHE, authCache);
        return localcontext;
    }
}
