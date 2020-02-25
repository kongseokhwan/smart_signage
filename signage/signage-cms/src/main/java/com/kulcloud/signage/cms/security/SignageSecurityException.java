package com.kulcloud.signage.cms.security;

public class SignageSecurityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public SignageSecurityException(String id) {
		super("Could not find tenant of signage: " + id);
	}
	
}
