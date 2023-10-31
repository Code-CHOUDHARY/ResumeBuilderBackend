package com.resumebuilder.downloadhistory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
	

@Service
public class DownloadHistoryServiceImplementation implements DownloadHistoryService {

	private final Path root = Paths.get("Upload/E620/Resume");
	
	@Override
	public boolean delete(String filename) {
		try {
		      Path file = root.resolve(filename);
		      return Files.deleteIfExists(file);
		    } catch (IOException e) {
		      throw new RuntimeException("Error: " + e.getMessage());
		    }
	}

}
