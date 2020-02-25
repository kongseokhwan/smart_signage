package com.kulcloud.signage.commons.utils;

import java.util.List;

import javax.persistence.Converter;

import com.fasterxml.jackson.core.type.TypeReference;

@Converter
public class ListConverterJson extends JpaConverterJson<List<Object>>{

	public ListConverterJson() {
		super(new TypeReference<List<Object>>() {});
	}
	
}
