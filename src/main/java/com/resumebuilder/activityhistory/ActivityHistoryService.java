package com.resumebuilder.activityhistory;

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
	
	public ActivityHistory addActivity(ActivityHistory activityHistory);
	
	/**
	 * 
	 * @return list of activities
	 */
	public List<ActivityHistory> getAllAcitivities();
}
