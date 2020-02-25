package com.kulcloud.signage.tenant;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DataSourceConfig {

    @Value("${spring.datasource.url:}")
    private String url;
    @Value("${spring.datasource.password:}")
    private String password;
    @Value("${spring.datasource.userdata:}")
    private String userdata;
    @Value("${spring.datasource.username:}")
    private String username;

    @Value("${com.kulcloud.signage.db.url:}")
    private String signageDbUrl;
    @Value("${com.kulcloud.signage.tenant:}")
    private String signageTenant;
    @Value("${com.kulcloud.signage.userdata:}")
    private String signageUserdata;
    
    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");
        
        if(StringUtils.isAnyEmpty(signageDbUrl, signageTenant, signageUserdata)) {
        	config.setJdbcUrl(url);
        	config.setUsername(username);
        	if(StringUtils.isEmpty(userdata)) {
        		config.setPassword(password);
        	} else {
        		config.setPassword(userdata);
        	}
        } else {
        	config.setJdbcUrl(signageDbUrl);
            config.setUsername(signageTenant);
            config.setPassword(signageUserdata);
        }
        
        return new HikariDataSource(config);
    }
}
