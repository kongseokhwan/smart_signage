package com.kulcloud.signage.cms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.kulcloud.signage.commons.SignageCommonsApplication;

/**
 * The entry point of the Spring Boot application.
 */
@SpringBootApplication(scanBasePackageClasses = {Application.class, SignageCommonsApplication.class})
public class Application extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
