package com.resumebuilder.resumes;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.resumebuilder.DTO.ProjectDto;
import com.resumebuilder.resumetemplates.ResumeTemplates;
import com.resumebuilder.user.User;

public interface ResumeGeneratorService {
	 public boolean addFile(String path,String html)throws IOException ;
	 public boolean deleteFile(File f);
	 public  List<?>  getAllFiles(String path);
	 public List<?> getAllDeletedFiles(String path);
	 public File updateFile(File oldFile,Object content)throws IOException ;
	 public int countOfFilesInFolder(String path);
	 
	 
	 

}
