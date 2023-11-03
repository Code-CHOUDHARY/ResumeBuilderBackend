package com.resumebuilder;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan	 
public class ResumeBuilderBackendApplication  {
	
	public static final Logger logger = LoggerFactory.getLogger(ResumeBuilderBackendApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ResumeBuilderBackendApplication.class, args);
		logger.info("Project started...");
	}

//	@PostConstruct
//    public void registerSyncfusionLicense() {
//        // Register Syncfusion license using the property from SyncfusionConfig
//        SyncfusionLicenseProvider.registerLicense(licenseCOnfig.getSyncfusionLicenseKey());
//		log.info("Successfully registered the license key : "+licenseCOnfig.getSyncfusionLicenseKey());
//	}	
}
