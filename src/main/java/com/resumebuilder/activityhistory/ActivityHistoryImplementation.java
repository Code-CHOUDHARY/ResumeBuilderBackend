package com.resumebuilder.activityhistory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ActivityHistoryImplementation implements ActivityHistoryService {
	
	@Autowired
	private ActivityHistoryRepository activityHistoryRepo;
	
	/**
     * Add a new Activity record.
     *
     * @param activityHistory The technology record to be added.
     * @throws ActivityException if there is an issue adding the Activity.
     * @return The added technology record.
     */
	
	@Override
	public ActivityHistory addActivity(ActivityHistory activityHistory) {
		
		return activityHistoryRepo.save(activityHistory);
	}

	/**
	 *  @return The List of Activities record.
	 */
	
	@Override
	public List<ActivityHistory> getAllAcitivities() {
		// TODO Auto-generated method stub
		return activityHistoryRepo.findAll();
	}

}
