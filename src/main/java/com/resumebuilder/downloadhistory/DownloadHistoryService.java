package com.resumebuilder.downloadhistory;

import java.security.Principal;
import java.util.List;

public interface DownloadHistoryService {
	
	public DownloadHistory saveDownloadHistory(DownloadHistory downloadHistory, Principal principal);
	public List<DownloadHistory> getAllDownloadHistories();
}
