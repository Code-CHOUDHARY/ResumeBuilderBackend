package com.resumebuilder.downloadhistory;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DownloadHistoryServiceImplementation implements DownloadHistoryService {

	
	@Autowired
	private DownloadHistoryRepository downloadHistoryRepository;
	
	/**
     * Saves a new download history record.
     *
     * @param downloadHistory The download history to be saved.
     * @return The saved download history record.
     */
	
	@Override
	public DownloadHistory saveDownloadHistory(DownloadHistory downloadHistory, Principal principal) {
		// TODO Auto-generated method stub
		return downloadHistoryRepository.save(downloadHistory);
	}

    /**
     * Retrieves a list of all download history records.
     *
     * @return A list of download history records.
     */

	
	@Override
	public List<DownloadHistory> getAllDownloadHistories() {
		// TODO Auto-generated method stub
		return downloadHistoryRepository.findAll();
	}

}
