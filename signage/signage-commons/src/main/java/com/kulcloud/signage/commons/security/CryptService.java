package com.kulcloud.signage.commons.security;

import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang3.StringUtils;

import com.kulcloud.signage.commons.utils.CommonConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class CryptService {

	private static final long OTP_DISTANCE = 30000;
	private static final String OTP_ALGORITHM = "HmacSHA1";
	
	public boolean isValidApiKey(String secretKey, String apiKey) {
		if(StringUtils.isAnyEmpty(secretKey, apiKey)) {
			return false;
		} else {
			try {
				Claims claims = Jwts.parser()
						.setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
						.parseClaimsJws(apiKey).getBody();
				
				Date expire = claims.getExpiration();
				if(expire.after(new Date())) {
					return true;
				} else {
					return false;
				}
			} catch(Throwable ex) {
				return false;
			}
		}
	}
	
	public String createOtp() {
		try {
			return String.format("%06d", createOtp(System.currentTimeMillis() / OTP_DISTANCE));
		} catch (Exception e) {
			return "";
		}
	}
	
	public boolean verifyOtp(String code) {
		if(StringUtils.isEmpty(code)) {
			return false;
		}
		
		String otp = createOtp();
		if(StringUtils.isEmpty(otp)) {
			return false;
		} else {
			return createOtp().equals(code);
		}
	}
	
	private long createOtp(long time) throws Exception {
		byte[] data = new byte[8];
		long value = time;
		for (int i = data.length; i > 0; i--, value >>>= data.length ) {
			data[i] = (byte) value;
		}
		
		Mac mac = Mac.getInstance(OTP_ALGORITHM);
		mac.init(new SecretKeySpec(CommonConstants.code.getBytes(), OTP_ALGORITHM));
		byte[] hash = mac.doFinal(data);
		int offset = hash[20-1] & 0xF;
		
		long truncatedHash = 0;
		for(int i = 0; i < data.length / 2; i++) {
			truncatedHash <<= data.length;
			truncatedHash |= hash[offset + i] & 0xFF;
		}
		
		truncatedHash &= 0x7FFFFFFF;
		truncatedHash %= 1000000;
		
		return truncatedHash;
	}
}
