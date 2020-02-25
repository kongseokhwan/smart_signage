package com.kulcloud.signage.cms.influxdb;

import org.apache.commons.lang3.StringUtils;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import com.kulcloud.signage.cms.StorageService;

//@Service
public class InfluxDBStorage implements StorageService {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private InfluxDB influxDB;
	
	public InfluxDBStorage(
			@Value("${spring.influxdb.url: http://localhost:8086}") String url,
			@Value("${spring.influxdb.username:}") String username,
			@Value("${spring.influxdb.password:}") String password) {
		if(!StringUtils.isAnyEmpty(username, password)) {
			this.influxDB = InfluxDBFactory.connect(url, username, password);
		} else {
			this.influxDB = InfluxDBFactory.connect(url);
		}
	}
	
	@Override
	public boolean createStorage(String tenantId) {
		try {
			String createDatabaseQueryString = String.format("CREATE DATABASE \"%s\"", tenantId);
			influxDB.query(new Query(createDatabaseQueryString, null));
			return true;
		}catch(Throwable ex) {
			logger.error("Cannot create a database for tenant: " + tenantId, ex);
			return false;
		}
	}

	@Override
	public boolean deleteStorage(String tenantId) {
		try {
			String dropDatabaseQueryString = String.format("DROP DATABASE \"%s\"", tenantId);
			influxDB.query(new Query(dropDatabaseQueryString, null));
			return true;
		}catch(Throwable ex) {
			logger.error("Cannot drop a database for tenant: " + tenantId, ex);
			return false;
		}
	}

}
