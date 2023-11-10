package com.resumebuilder.resumes;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
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

import com.resumebuilder.downloadhistory.DownloadHistoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/Resumes")
public class ResumeController {
	
	private static final Logger log = LoggerFactory.getLogger(ResumeController.class);
	 String fileSeprator=System.getProperty("file.separator");
	 String baseFolder="upload";

     @Autowired
	private ResumeGeneratorService resumeService;
     
     @Autowired
     private DownloadHistoryService historyService;
     
     @PostMapping("/convertToDocs")
     public ResponseEntity<?> convertHtmlToDocx(@RequestBody ResumeRequest htmlRequest) {
    	 try {
    	 String htmlContent = htmlRequest.getHtmlContent();
     	log.info("Html content : "+htmlRequest.getFileName());
     	String userFolder=baseFolder+fileSeprator+htmlRequest.getUserId();
     	if(new File(userFolder).exists()) {   		
     		String path=userFolder+"/Resume/"+htmlRequest.getFileName()+".docx";
     		File f = new File(path);
     		if (f.exists()) {
     			// content add to new generated file
     			if(resumeService.addFile(path,htmlContent)) {
     				return  ResponseEntity.ok(new ResumeResponse(HttpStatus.OK,"true","Resume Genrated SuccessFully",null));
     			}
     			
     		}
     		else {
     			boolean res = resumeService.addFile(path,htmlContent);
     			if (res) {
     				return  ResponseEntity.ok(new ResumeResponse(HttpStatus.OK,"true","Resume Genrated SuccessFully",null));
     			} 
     		}
     	}
	
    	 } catch (IOException e) {
    		 
//			e.printStackTrace();
    		 return ResponseEntity
    				 .ok(new ResumeResponse(HttpStatus.INTERNAL_SERVER_ERROR, "false", "Unable to Generate Resume", null));
    	 }
    	 return ResponseEntity
				 .ok(new ResumeResponse(HttpStatus.INTERNAL_SERVER_ERROR, "false", "Unable to Generate Resume", null));
	}

     @PostMapping("/convertToPdf")
     public ResponseEntity<byte[]> convertHtmlToPdf(@RequestBody ResumeRequest htmlRequest) {
 		String path=baseFolder+fileSeprator+htmlRequest.getUserId()+"/Resume/"+htmlRequest.getFileName()+".pdf";

    	 String htmlContent = htmlRequest.getHtmlContent();
     	log.info("Html content : "+htmlContent);
     	
    	 return null; 
     }
     
     @PostMapping("/saveToOneDrive")
     public ResponseEntity<byte[]> saveToOmeDrive(@RequestBody ResumeRequest htmlRequest) {
 		String path=baseFolder+fileSeprator+htmlRequest.getUserId()+"/Resume/"+htmlRequest.getFileName()+".oneDrive";

    	 String htmlContent = htmlRequest.getHtmlContent();
     	log.info("Html content : "+htmlContent);
    	 return null; 
     }
     
     
     
     @GetMapping("/getAll/{empId}")
     public ResponseEntity<?> getAll(@PathVariable("empId")String empId, Principal p) {
    	 
     	log.info("getALL Resumes of ->"+empId+"- by:"+p.getName());
     	String path=baseFolder+fileSeprator+empId+fileSeprator+"Resume";
     	System.out.println("exists-->"+new File(path+"/Vinit_Resume.docx").exists());
     	 List<?>  list=resumeService.getAllFiles(path);
        if( !list.isEmpty()) {
			//System.out.println("list--->"+list.toString());
        	return  ResponseEntity.ok(new ResumeResponse(HttpStatus.OK,"true","All Resumes Fetched",list));
            
        }else {
        	return  ResponseEntity.ok(new ResumeResponse(HttpStatus.NO_CONTENT,"false","Unable to fetch list of Resumes",list));
        }
      
     }
     
     @DeleteMapping("/deleteResume/{empId}/{path}")
		public ResponseEntity<?> deleteFile(@PathVariable("empId") String empId, @PathVariable("path") String path,
				Principal p) {

			String filePath = baseFolder + fileSeprator + empId + fileSeprator + "Resume" + fileSeprator + path;

			File f = new File(filePath);
			if (f.exists()) {
				boolean res = resumeService.deleteFile(f);
				if (res) {

					return ResponseEntity
							.ok(new ResumeResponse(HttpStatus.OK, "true", "Resume Deleted SuccessFull", null));
				}
			}
			return ResponseEntity
					.ok(new ResumeResponse(HttpStatus.NO_CONTENT, "false", "Unable to Delete Resume", null));

		}
     
     
     @PutMapping("updateTask/{empId}/{path}")
		public ResponseEntity<?> updateFile(@PathVariable("empId") String empId, @PathVariable("path") String path,
				Principal p,@RequestBody ResumeRequest req) {

			String filePath = baseFolder + fileSeprator + empId + fileSeprator + "Resume" + fileSeprator + path;

			File f = new File(filePath);
			try {
			if (f.exists() && f.canRead()) {
					File newFile=resumeService.updateFile(f, req.getHtmlContent());
			   if(newFile.exists()) {
				   return ResponseEntity
							.ok(new ResumeResponse(HttpStatus.OK, "true", "Resume updated SuccessFully", null));  
			   }
			}else {
				if(resumeService.addFile(filePath,req.getHtmlContent())){
					return ResponseEntity
							.ok(new ResumeResponse(HttpStatus.NO_CONTENT, "false", "Resume updated SuccessFully", null));
				}
			}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return ResponseEntity
						.ok(new ResumeResponse(HttpStatus.NO_CONTENT, "false", "Resume updated SuccessFully", null));
			}
			return ResponseEntity
					.ok(new ResumeResponse(HttpStatus.INTERNAL_SERVER_ERROR, "false", "Unable to Update Resume", null));

		}
     
   
}

