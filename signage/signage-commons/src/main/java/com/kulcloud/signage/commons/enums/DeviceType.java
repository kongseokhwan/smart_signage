package com.kulcloud.signage.commons.enums;

public enum DeviceType {
	chromecast, rpi;
	
	public static String[] names() {
		return new String[] {chromecast.name(), rpi.name()};
	}
}
