package com.resumebuilder.resumes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.downloadhistory.DownloadHistoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/Resumes")
public class ResumeController {
	
	private static final Logger log = LoggerFactory.getLogger(ResumeController.class);

     @Autowired
	private ResumeGeneratorService resumeService;
     
     @Autowired
     private DownloadHistoryService historyService;
     
     @PostMapping("/convertToDocs")
     public ResponseEntity<byte[]> convertHtmlToDocx(@RequestBody ResumeRequest htmlRequest) {
    	 String htmlContent = htmlRequest.getHtmlContent();
     	log.info("Html content : "+htmlContent);
    	 return null; 
     }
     
     @PostMapping("/convertToPdf")
     public ResponseEntity<byte[]> convertHtmlToPdf(@RequestBody ResumeRequest htmlRequest) {
    	 String htmlContent = htmlRequest.getHtmlContent();
     	log.info("Html content : "+htmlContent);
    	 return null; 
     }
     
     @PostMapping("/saveToOneDrive")
     public ResponseEntity<byte[]> saveToOmeDrive(@RequestBody ResumeRequest htmlRequest) {
    	 String htmlContent = htmlRequest.getHtmlContent();
     	log.info("Html content : "+htmlContent);
    	 return null; 
     }
     
}
