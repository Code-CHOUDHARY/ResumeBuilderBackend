package com.resumebuilder.bulkupload;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExcelMappingConfig {

	 @Bean
	    public Map<Integer, String> employeeColumnMapping() {
	        Map<Integer, String> mapping = new HashMap<>();
	        mapping.put(1, "employee_Id");
	        mapping.put(2, "full_name");
	        mapping.put(3, "date_of_joining");
	        mapping.put(4, "date_of_birth");
	        mapping.put(5, "current_role");
	        mapping.put(6, "email");
	        mapping.put(7, "gender");
	        mapping.put(8, "mobile_number");
	        mapping.put(9, "location");
	        //mapping.put(10, "manager_employee_id");
	        return mapping;
	    }
	 
	   @Bean
	    public Map<Integer, String> technologyColumnMapping() {
	        Map<Integer, String> mapping = new HashMap<>();
	        mapping.put(1, "technology_name");
	        return mapping;
	    }

	    @Bean
	    public Map<Integer, String> rolesColumnMapping() {
	        Map<Integer, String> mapping = new HashMap<>();
	        mapping.put(1, "role_name");
	        return mapping;
	    }
	 
}
