package com.kulcloud.signage.commons.utils;

import java.io.IOException;

import javax.persistence.AttributeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JpaConverterJson <T> implements AttributeConverter<T, String>{

	private final static Logger LOGGER = LoggerFactory.getLogger(JpaConverterJson.class);
	private final static ObjectMapper objectMapper = new ObjectMapper();

	private TypeReference<T> type;
	
	public JpaConverterJson(TypeReference<T> type) {
		this.type = type;
	}
	
	public String convertToDatabaseColumn(T meta) {
		if(meta == null) {
			return null;
		} else {
			try {
				return objectMapper.writeValueAsString(meta);
			} catch (JsonProcessingException ex) {
				 LOGGER.error("Could not convert map to json string.");
		         return null;
			}
		}
	}

	public T convertToEntityAttribute(String dbData) {
		if (dbData == null) {
           return null;
        } else {
			try {
				return objectMapper.readValue(dbData, type);
			} catch (IOException ex) {
				LOGGER.error("Convert error while trying to convert string(JSON) to map data structure.");
				return null;
			}
        }
	}

}
