package com.resumebuilder.resumes;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.resumebuilder.resumetemplates.ResumeTemplatesServiceImplementation;
import com.syncfusion.docio.FormatType;
import com.syncfusion.docio.IWParagraph;
import com.syncfusion.docio.IWSection;
import com.syncfusion.docio.WordDocument;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ResumeGeneratorServiceImpl implements ResumeGeneratorService {

	 private static final Logger logger =  LoggerFactory.getLogger(ResumeTemplatesServiceImplementation.class); 
	 
	 public boolean addFile(String path,String html) throws IOException {
		 File file=new File(path);
		 File folder=file.getParentFile();
		 boolean res=false;
		/*
		 * First checks parent folder available or not, if present new file in that
		 * folder, or else first creates folder then creates a file
		 */
		 if(folder.exists()) {
			 //System.out.println("folder--"+folder.getName()+"-exists");
			 res=file.createNewFile();
			 
		 }else {
			 //System.out.println("folder--"+folder.getName()+"-Not exists");
		      String parentPath=folder.getAbsolutePath();
		      new File(parentPath).mkdirs();
			 res=file.createNewFile();
			 }
		 byte[] contentBytes=addContent(html);
		 //byte[] contentBytes="This is the content of the document.".getBytes();
		new FileWriter(file).write(new String(contentBytes));
		FileOutputStream fos = new FileOutputStream(path);
	fos.write(contentBytes);
	fos.close();
		 return res;
	 }
	 
	 public boolean deleteFile(File f) {
		 
		 if(f.exists() && f.canRead()) {
			 boolean res=f.setReadable(false);
			 System.out.println("response-->"+res);
			 return res;
 		 }else {
			 return false;
		 }
	 }
	 
     FileFilter readOnlyFilter=(file)->{
	    	return file.canRead();  
	      };
	 
	 public List<?> getAllFiles(String path){
		 //returns all files which are readable->true
		 File f=new File(path);
		 List<String> list=new ArrayList<String>();
		if(f.exists() && f.isDirectory()) {
			 File[] fileList= f.listFiles(readOnlyFilter);
			
				for(File file:fileList) {
					list.add(file.getName());
				}
		}
		return list;
	 }
	 
	 public List<?> getAllDeletedFiles(String path){
		 //returns all files which are readable->false
		 File f=new File(path);
		 List<String> list=new ArrayList<String>();
		 if(f.exists() && f.isDirectory()) {
			 	 File[] fileList=new File(path).listFiles((file)->{
				 return !file.canRead();
			 });
			 	 for(File e:fileList) {
			 		 list.add(e.getName());
			 		 }
			
		 }
		 return list;
	 }
	 
	 public boolean updateFile(String path,File f) throws IOException {
		    File oldFile=new File(path);

		 if(oldFile.exists()) {
			 FileWriter writerObj = new FileWriter(path, false);
			
//			 writerObj.
//	            writerObj.write(content);
	            writerObj.close();

		 }
		 return false;
	 }
	 
	 
	 public static  byte[] addContent(String htmlContent)  {
	     
		 	try {
				log.info("Converting html to word uing synfusion docio");
				WordDocument doc = new WordDocument("upload/ResumeOutline/QuadwaveReseumeTemplateOutline.docx", FormatType.Docx);
				// Get the first section of the document
				IWSection section = doc.getSections().get(0);

				// Get the header and footer of the first section
				section.getHeadersFooters().getHeader();
				section.getHeadersFooters().getFooter();
				
				// Add the HTML content to the body of the section
				InputStream inputStream = new ByteArrayInputStream(htmlContent.getBytes());
				IWParagraph contentPara = section.addParagraph();
				contentPara.appendHTML(htmlContent);
				// Save the document as a byte array
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				doc.save(outputStream, FormatType.Docx);
				doc.close();
				//System.out.println("genrated =========>"+new String(outputStream.toByteArray()));
				log.info("Converted html to word uing synfusion docio");
				//System.out.println("generated byte array----->"+outputStream);
				return outputStream.toByteArray();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				log.error("error converting html to word uing synfusion docio");
				e.printStackTrace();
			}
			return null;
	        
	 }

	@Override
	public File updateFile(File oldFile, Object content) throws IOException {
		String path=oldFile.getCanonicalPath();
		 try (FileWriter fw = new FileWriter(oldFile, false)) {
			fw.write(content.toString());
		}
		 return oldFile;
	}

	@Override
	public int countOfFilesInFolder(String path) {
		int count=0;
		File f=new File(path);
		File[] files=f.getParentFile().listFiles();
		count=files.length;
		return count;
	} 
		 

	
}