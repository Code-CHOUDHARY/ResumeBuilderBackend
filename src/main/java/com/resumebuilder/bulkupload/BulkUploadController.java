package com.resumebuilder.bulkupload;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.resumebuilder.ResumeBuilderBackendApplication;
import com.resumebuilder.DTO.RolesDto;
import com.resumebuilder.DTO.TechnologyDto;
import com.resumebuilder.exception.DuplicateDataEntryException;


@RestController
//@RequestMapping("/auth") //temporary 
public class BulkUploadController {
	
	public static final Logger logger = LoggerFactory.getLogger(ResumeBuilderBackendApplication.class);
	
	@Autowired
	private BulkUploadRoleService bulkUploadRoleService;
	
	@Autowired
	private BulkUploadEmployeeService bulkUploadEmployeeService;
	
	@Autowired
	private BulkUploadTechnologyService bulkUploadTechnologyService;
	
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/upload/EmployeeExcel")
    public ResponseEntity<List<EmployeeBulkUploadDto>> uploadEmployeeExcelFile(@RequestParam("file") MultipartFile file, Principal principal) {
			try {
				var employeeBulkUploadDtos = bulkUploadEmployeeService.processEmployeeExcelFile(file, principal);
				return ResponseEntity.ok(employeeBulkUploadDtos);
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
			}
    }
	
	@PreAuthorize("hasRole('ADMIN')")
	 @PostMapping("/upload/RoleExcel")
	    public ResponseEntity<List<RolesDto>> uploadRoleExcel(@RequestParam("file") MultipartFile file, Principal principal) throws Exception {		 
		 try {
			 var roleBulkUploadDtos = bulkUploadRoleService.processRoleExcelFile(file, principal);
			 return ResponseEntity.ok(roleBulkUploadDtos);
	        } catch (Exception e) {
	        	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
	        } 		
	    }
	 
				
	 
	@PreAuthorize("hasRole('ADMIN')")
	 @PostMapping("/upload/TechnologyExcel")
	    public ResponseEntity<List<TechnologyDto>> uploadTechnologyExcel(@RequestParam("file") MultipartFile file, Principal principal) throws IOException {
		 
		 try {
			 var technologyBulkUploadDtos = bulkUploadTechnologyService.processTechnologyExcelFile(file, principal);
			 return ResponseEntity.ok(technologyBulkUploadDtos);
	        } catch (Exception e) {
	        	return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(null);
	        } 
		
	    }
	
	
	 
}