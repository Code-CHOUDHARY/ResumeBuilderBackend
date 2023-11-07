package com.resumebuilder.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class Licenseconfig {

	@Value("${syncfusion.licenseKey}")
	private String syncfusionLicenseKey;
	
	
}
