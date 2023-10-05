package com.resumebuilder.bulkupload;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.resumebuilder.user.User;

@RestController
public class BulkUploadController {
	
	@Autowired
    private EmployeeExcelService excelService;
	
	@Autowired
	private RoleExcelService roleExcelService;
	
	@Autowired
	private TechnologyExcelService technologyExcelService;
	
	 //bulk upload API for employees
    @PostMapping("/employees/upload")
	public ResponseEntity<?> uploadEmployeeExcel(@RequestParam("file")MultipartFile file) throws IOException{
		
		if(EmployeeExcelHelper.checkExcelFormat(file)) {
			//true
			this.excelService.save(file);
			return ResponseEntity.ok(Map.of("message","File is uploaded and data saved in database."));
			
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload excel file only.");
	}
    
//    @GetMapping("/employees")
//    public List<User> getAllEmployees() {
//        return excelService.getAllUsers();
//    }
    
  //bulk upload API for roles
    @PostMapping("/roles/upload")
    public ResponseEntity<?> uploadRolesExcel(@RequestParam("file") MultipartFile file) throws IOException {
        if (RoleExcelHelper.checkExcelFormat(file)) {
        	roleExcelService.save(file);
            return ResponseEntity.ok(Map.of("message", "File is uploaded, and roles are saved in the database."));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload an Excel file only.");
    }
    
    
  //bulk upload API for technology
    @PostMapping("/technology/upload")
    public ResponseEntity<?> uploadTechnologyExcel(@RequestParam("file") MultipartFile file) throws IOException {
        if (TechnologyExcelHelper.checkExcelFormat(file)) {
        	technologyExcelService.save(file);
            return ResponseEntity.ok(Map.of("message", "File is uploaded, and technologies are saved in the database."));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload an Excel file only.");
    }

}