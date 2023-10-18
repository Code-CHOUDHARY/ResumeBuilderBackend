package com.resumebuilder.activityhistory;

import java.security.Principal;
import java.util.List;

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
	
	public Void addActivity(String activityType, String Description,String newData, String oldData, String modifiedBy);
	
	/**
	 * 
	 * @return list of activities
	 */
	public List<ActivityHistory> getAllAcitivities();
}
