package com.resumebuilder.resumetemplates;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.exception.ResumeTemplateExceptions;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/templates")
public class ResumeTemplatesController {

	@Autowired
	private ResumeTemplatesService service;
	
	
	 
	 
	@PostMapping("/addTemplate")
	public ResponseEntity<?> addTemplate(@RequestBody  ResumeTemplates request){
		ResumeTemplates template=service.addTemplate(request);
		if(template!=null) {
			return ResponseEntity.status(HttpStatus.OK).body("template added Succesfull");
		}else {
			//ResponseEntity.ok(new ResumeTemplateExceptions("Unable to Add Resume Template"))
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResumeTemplateExceptions("Unable to Add Resume Template"));
		}
		     
	}
	
	 @PutMapping("/updateTemplate/{templateId}")
	public ResponseEntity<?> editTemplate(@PathVariable("templateId")String templateId,@RequestBody ResumeTemplates request){
		 ResumeTemplates updatedTemplate=service.updateTemplate(templateId, request);
		 if(updatedTemplate!=null) {
			 return  ResponseEntity.status(HttpStatus.OK).body("template updated Succesfull");
		 }else {
			 return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResumeTemplateExceptions("Unable to update Resume Template") );
		 }

	}
	
	 @DeleteMapping("/deleteTemplate/{templateId}")
	 public ResponseEntity<?> delteTemplate(@PathVariable("templateId")String templateId){
		boolean flag=service.deleteTemplate(templateId);	
		if(flag) {
			return  ResponseEntity.status(HttpStatus.OK).body("template Deleted Succesfully");
		}else {
			return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResumeTemplateExceptions("Unable to Delete Resume Template") );
		}

		}
	 
	 @GetMapping("/getTemplate/{templateId}")
	   public ResponseEntity<?> getTemplateById(@PathVariable("templateId")String templateId){
		 ResumeTemplates template=service.getTemplateById(templateId);
		 if(template!=null) {
			 return  ResponseEntity.status(HttpStatus.OK).body(template);
		 }else {
			 return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResumeTemplateExceptions("Unable to find Resume Template"));
		 }
		 
	 }
	 
	 @GetMapping("/getallTemplates")
	   public ResponseEntity<?> getAllTemplates(){
		 List<ResumeTemplates> list=service.getAllTemplates();
		 if(list.isEmpty()) {
			 return null;
		 }else {
			 
			 return  ResponseEntity.ok(list);
		 }
	 }
	 
}
