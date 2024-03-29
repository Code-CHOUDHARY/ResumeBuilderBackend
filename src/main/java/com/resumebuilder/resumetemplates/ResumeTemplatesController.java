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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.resumebuilder.DTO.TemplateDto;
import com.resumebuilder.exception.ResumeTemplateExceptions;
import com.resumebuilder.resumes.ResumeResponse;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@RestController
@RequestMapping("/templates")
public class ResumeTemplatesController {

	@Autowired
	private ResumeTemplatesService service;
	
	@Autowired
    private UserService userService;
	
	 private static final Logger logger = LogManager.getLogger(ResumeTemplatesController.class); 

	 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	 
	@PostMapping("/addTemplate")
	public ResponseEntity<?> addTemplate(@RequestBody  ResumeTemplates request,Principal principle){
		ResumeTemplates template=service.addTemplate(request,principle);
		if(template!=null) {
//			logger.info("Requested user info: {} "+"add template request ",authentication.getName());
			return ResponseEntity.status(HttpStatus.OK).body("template added Succesfull");
		}else {
			//ResponseEntity.ok(new ResumeTemplateExceptions("Unable to Add Resume Template"))
			return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResumeTemplateExceptions("Unable to Add Resume Template"));
		}
		     
	}
	
	 @PutMapping("/updateTemplate/{templateId}")
	public ResponseEntity<?> editTemplate(@PathVariable("templateId")String templateId,@RequestBody ResumeTemplates request,Principal principle){
		 ResumeTemplates updatedTemplate=service.updateTemplate(templateId, request,principle);
		 if(updatedTemplate!=null) {
			 return  ResponseEntity.status(HttpStatus.OK).body("template updated Succesfull");
		 }else {
			 return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResumeTemplateExceptions("Unable to update Resume Template") );
		 }

	}
	
	 @DeleteMapping("/deleteTemplate/{templateId}")
	 public ResponseEntity<?> delteTemplate(@PathVariable("templateId")String templateId,Principal principle){
		boolean flag=service.deleteTemplate(templateId,principle);	
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
		 List<TemplateDto> list=service.getAllTemplates();
		
		 if(list.isEmpty()) {
			 return null;
		 }else {
			 
			 return  ResponseEntity.ok(list);
		 }
	 }
	 
	 
	 
     @GetMapping("/replace/{templateId}/{userId}")
     public ResponseEntity<?> replaceEntireTemplate(@PathVariable("templateId") String tempId,@PathVariable("userId") String UserId){
    	 try {
    	    ResumeTemplates template=service.getTemplateById(tempId);
    	   ObjectMapper parser=new ObjectMapper();
    	   parser.enableDefaultTyping();
    	   User u= userService.findUserByIdUser(Long.parseLong(UserId));
    	    ResumeTemplates res;
				res = service.getGeneratedPreview(template, u);
    	 return ResponseEntity
					.ok(new ResumeResponse(HttpStatus.OK, "true", "Template updated SuccessFully", res));
    	 } catch (JsonProcessingException e) {
    		 // TODO Auto-generated catch block
    		 e.printStackTrace();
    		 
    	 }
    	 return ResponseEntity
					.ok(new ResumeResponse(HttpStatus.CONFLICT, "false", "Unable to Generate Preview", null));
     }
	 
}
