package com.resumebuilder.projects;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.DTO.APIResponse;
import com.resumebuilder.DTO.EmployeProjectRequestEntity;
import com.resumebuilder.DTO.EmployeeProjectResponceEntity;
@RestController
@RequestMapping("/employeeProject")
public class EmployeeProjectController {
     @Autowired
	private EmployProjectService empservice;
     @PostMapping("/addEmployeeProjectadd/user/{empid}")
	public ResponseEntity<EmployeeProject> addEmployeeProject(@RequestBody EmployeProjectRequestEntity empproje,@PathVariable("empid")Long id,Principal principal){
		EmployeeProject emppro=this.empservice.addEmployeeProject(empproje, id,principal);
		
		
		return new ResponseEntity<EmployeeProject>(emppro,HttpStatus.CREATED);
	}
     @PutMapping("/updateEmployeeProjectadd/proje/{projeid}")
 	public ResponseEntity<EmployeeProject> updateEmployeeProject(@RequestBody EmployeProjectRequestEntity empproje,@PathVariable("projeid")Long id,Principal principal){
 		EmployeeProject emppro=this.empservice.updateEmployeeProject(empproje, id, principal);
 		
 		
 		return new ResponseEntity<EmployeeProject>(emppro,HttpStatus.CREATED);
 	}
     @GetMapping("getAllProjects")
   
	public ResponseEntity<List<EmployeeProjectResponceEntity>>getProjects(){
          
    	 List<EmployeeProjectResponceEntity>emp=this.empservice.getAllEmplyeeProjects();
		return new ResponseEntity<List<EmployeeProjectResponceEntity>>(emp,HttpStatus.ACCEPTED );
	
	
}
     @GetMapping("/getempProjectbyid/{proid}")
public ResponseEntity<EmployeeProjectResponceEntity> get(@PathVariable("proid")Long id) {
    	 EmployeeProjectResponceEntity emppro=this.empservice.getbyid(id);
	
	return new ResponseEntity<EmployeeProjectResponceEntity>(emppro,HttpStatus.ACCEPTED);
}     
     @PostMapping("/DeleteempProjectbyid/{proid}")
     public APIResponse dekletepro(@PathVariable("proid")Long id) {
String emppro=this.empservice.deleteEmployeeProject(id);
     	if(emppro.equals("deleted")) {
     	return new APIResponse(HttpStatus.ACCEPTED,"Employee Projects","Project deleted Succesffuly");
     } 
     	return new APIResponse(HttpStatus.ACCEPTED,"Employee Projects"," Not Found or Not Deleted!!!!!! ");

     }
}
