package com.resumebuilder.resumetemplates;

import java.security.Principal;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.exception.ResumeTemplateExceptions;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/templates")
public class ResumeTemplatesController {

	@Autowired
	private ResumeTemplatesService service;
	
	 private static final Logger logger = LogManager.getLogger(ResumeTemplatesController.class); 

	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	 
	@PostMapping("/addTemplate")
	public ResponseEntity<?> addTemplate(@RequestBody  ResumeTemplates request,Principal p){
		//System.out.println("identifiled------->"+p.getName());
		ResumeTemplates template=service.addTemplate(request,p);
		if(template!=null) {
//			logger.info("Requested user info: {} "+"add template request ",authentication.getName());
			return ResponseEntity.status(HttpStatus.OK).body("template added Succesfull");
		}else {
			//ResponseEntity.ok(new ResumeTemplateExceptions("Unable to Add Resume Template"))
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResumeTemplateExceptions("Unable to Add Resume Template"));
		}
		     
	}
	
	 @PutMapping("/updateTemplate/{templateId}")
	public ResponseEntity<?> editTemplate(@PathVariable("templateId")String templateId,@RequestBody ResumeTemplates request,Principal p){
		 ResumeTemplates updatedTemplate=service.updateTemplate(templateId, request, p);
		 if(updatedTemplate!=null) {
			 return  ResponseEntity.status(HttpStatus.OK).body("template updated Succesfull");
		 }else {
			 return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResumeTemplateExceptions("Unable to update Resume Template") );
		 }

	}
	
	 @DeleteMapping("/deleteTemplate/{templateId}")
	 public ResponseEntity<?> delteTemplate(@PathVariable("templateId")String templateId,Principal p){
		boolean flag=service.deleteTemplate(templateId,p);	
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
			 return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResumeTemplateExceptions("At Present No Any Templates Present"));
		 }else {
			 
			 return  ResponseEntity.ok(list);
		 }
	 }
	 
	 @GetMapping("/replaceTemplate/{templateId}/{userId}")
	 public ResponseEntity<?> replaceTemplate(@PathVariable String templateId,@PathVariable String userId){
		 String genratedHtmString=service.replaceTemplateData(templateId, userId);
		 return ResponseEntity.ok(genratedHtmString);
	 }
	 
}
