package com.kulcloud.signage.cms.security;

import java.security.Key;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.kulcloud.signage.cms.user.SignageTenant;
import com.kulcloud.signage.commons.security.CryptService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class ApiKeyService extends CryptService {
	
	private int oneday = 1000 * 60 * 60 * 24;
	
	public String createApiKey(SignageTenant tenant) {
		if(tenant.getPassword() == null) {
			throw new IllegalArgumentException();
		}
		
		byte[] secretKeyBytes = DatatypeConverter.parseBase64Binary(tenant.getPassword());
		Key signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS256.getJcaName());
		
		Date expireTime = new Date();
        expireTime.setTime(expireTime.getTime() + oneday);
		
		return Jwts.builder()
				.setHeaderParam("typ", "JWT")
				.setHeaderParam("alg", SignatureAlgorithm.HS256.name())
				.setSubject(tenant.getTenantId())
				.claim("ipAddress", tenant.getIpAddress())
				.setExpiration(expireTime)
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
	}
	
	public Claims getClaims(SignageTenant tenant) {
		if(StringUtils.isAnyEmpty(tenant.getPassword(), tenant.getApiKey())) {
			return null;
		} else {
			try {
				return Jwts.parser()
						.setSigningKey(DatatypeConverter.parseBase64Binary(tenant.getPassword()))
						.parseClaimsJws(tenant.getApiKey()).getBody();
			} catch(ExpiredJwtException ex) {
				return ex.getClaims();
			} catch (Throwable ex) {
				return null;
			}
		}
	}
	
	public String getTenantId(SignageTenant tenant, String apiKey) {
		if(tenant.getPassword() == null) {
			return null;
		} else {
			try {
				Claims claims = Jwts.parser()
						.setSigningKey(DatatypeConverter.parseBase64Binary(tenant.getPassword()))
						.parseClaimsJws(apiKey).getBody();
				
				return claims.getSubject();
			} catch(Throwable ex) {
				return null;
			}
		}
	}
}
