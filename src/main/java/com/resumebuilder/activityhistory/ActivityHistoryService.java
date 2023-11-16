package com.resumebuilder.activityhistory;

import java.security.Principal;
import java.util.List;

import com.resumebuilder.DTO.ActivityHistoryDto;
import com.resumebuilder.DTO.TeamActivityDto;

/**
 * Service interface for managing technology records.
 */

public interface ActivityHistoryService {
	
	/**
     * Add a new activity record.
     *
     * @param activityHistory The Activity record to be added.
     * @return The added Activity record.
     */
	
	public ActivityHistoryDto addActivity(ActivityHistory activityHistory, Principal principal);
	
	/**
	 * 
	 * @return list of activities
	 */
	public List<ActivityHistoryDto> getAllAcitivities();
	
	public List<ActivityHistoryDto> getActivitiesByUser(Long userId);
	
	public List<TeamActivityDto> getActivitiesByReportingManager(Long managerId);
}
