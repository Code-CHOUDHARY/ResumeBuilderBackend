package com.resumebuilder.downloadhistory;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

@Service
public class DownloadHistoryServiceImplementation implements DownloadHistoryService {

	
	@Autowired
	private DownloadHistoryRepository downloadHistoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
     * Saves a new download history record.
     *
     * @param downloadHistory The download history to be saved.
     * @return The saved download history record.
     */
	
	@Override
	public DownloadHistory saveDownloadHistory(DownloadHistory downloadHistory, Principal principal) {
		User user = userRepository.findByEmailId(principal.getName());
		DownloadHistory history = new DownloadHistory();
    	history.setVersion(downloadHistory.getVersion());
    	history.setDoc_link(downloadHistory.getDoc_link());
    	history.setPdf_link(downloadHistory.getPdf_link());
    	history.setModified_by(user.getUser_id());
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
