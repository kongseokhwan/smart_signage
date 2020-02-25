package com.kulcloud.signage.commons.utils;

import java.util.Map;

import javax.persistence.Converter;

import com.fasterxml.jackson.core.type.TypeReference;

@Converter
public class MapConverterJson extends JpaConverterJson<Map<String, Object>>{

	public MapConverterJson() {
		super(new TypeReference<Map<String, Object>>() {});
	}

}
