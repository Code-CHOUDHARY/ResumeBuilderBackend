package com.resumebuilder.bulkupload;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.resumebuilder.ResumeBuilderBackendApplication;
import com.resumebuilder.exception.DataMissingException;
import com.resumebuilder.exception.DuplicateDataEntryException;
import com.resumebuilder.user.User;

@RestController
//@RequestMapping("/auth") //temporary 
public class BulkUploadController {
	
	public static final Logger logger = LoggerFactory.getLogger(ResumeBuilderBackendApplication.class);
	
	@Autowired
    private BulkExcelService bulkexcelService;
	
	@Autowired
	private RoleBulkService roleBulkService;
	
	@Autowired
	private BulkUploadRoleService bulkUploadRoleService;
	
	@Autowired
	private BulkUploadTechnologyService bulkUploadTechnologyService;
	
	@PostMapping("/upload/EmployeeExcel")
    public ResponseEntity<String> uploadEmployeeExcelFile(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
        try {
        	bulkexcelService.processExcelFile(file, principal);
            return ResponseEntity.ok("File uploaded successfully.");
        } catch (DuplicateDataEntryException e) {
        	 String errorMessage = e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorMessage);
        } 
    }
	
	 @PostMapping("/upload/RoleExcel")
	    public ResponseEntity<?> uploadRoleExcel(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
		 
		 try {
			 bulkUploadRoleService.processRoleExcelFile(file, principal);
	            return ResponseEntity.ok("File uploaded successfully.");
	        } catch (DuplicateDataEntryException e) {
	            String errorMessage = e.getMessage();
	            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorMessage);
	        } 
		
	    }
	 
	 @PostMapping("/upload/TechnologyExcel")
	    public ResponseEntity<?> uploadTechnologyExcel(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
		 
		 try {
			 bulkUploadTechnologyService.processTechnologyExcelFile(file, principal);
	            return ResponseEntity.ok("File uploaded successfully.");
	        } catch (DuplicateDataEntryException e) {
	            String errorMessage = e.getMessage();
	            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(errorMessage);
	        } 
		
	    }
	
	
	 
}