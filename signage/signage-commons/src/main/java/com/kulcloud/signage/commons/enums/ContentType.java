package com.kulcloud.signage.commons.enums;

public enum ContentType {
	link, 
	html, 
	slide, 
	live, 
	vod;
	
	public static String[] names() {
//		ContentType[] values = ContentType.values();
//		String[] names = new String[values.length];
//		for (int i = 0; i < values.length; i++) {
//			names[i] = values[i].name();
//		}
//		
//		return names;
		
		return new String[] {"link", "live", "vod"};
	}
}
