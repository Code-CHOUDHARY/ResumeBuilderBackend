package com.resumebuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResumeBuilderBackendApplication {
	
	public static final Logger logger = LoggerFactory.getLogger(ResumeBuilderBackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ResumeBuilderBackendApplication.class, args);
		logger.info("Project start...");
	}

}
