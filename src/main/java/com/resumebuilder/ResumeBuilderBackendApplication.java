package com.resumebuilder;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan	
public class ResumeBuilderBackendApplication {
	
	public static final Logger logger = LoggerFactory.getLogger(ResumeBuilderBackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ResumeBuilderBackendApplication.class, args);
		logger.info("Project started...");
	}
	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
