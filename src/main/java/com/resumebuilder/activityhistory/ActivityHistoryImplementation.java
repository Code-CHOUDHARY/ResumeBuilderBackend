package com.resumebuilder.activityhistory;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.exception.ActivityException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;


@Service
public class ActivityHistoryImplementation implements ActivityHistoryService {
	
	@Autowired
	private ActivityHistoryRepository activityHistoryRepo;
	
	@Autowired
	private UserRepository userRepository;
	
	/**
	 *  @return The List of Activities record.
	 */
	
	@Override
	public List<ActivityHistory> getAllAcitivities() {
		// TODO Auto-generated method stub
		return activityHistoryRepo.findAll();
	}

	/**
     * Add a new Activity record.
     *
     * @param activityHistory The technology record to be added.
     * @throws ActivityException if there is an issue adding the Activity.
     * 
     */
	
	
	@Override
	public Void addActivity(String activityType, String Description, String newData, String oldData, String modifiedBy ) throws ActivityException {
		
		
		ActivityHistory activityHistory = new ActivityHistory();
		
		activityHistory.setActivity_type(activityType);
		activityHistory.setDescription(Description);
		activityHistory.setNew_data(newData);
	
		activityHistoryRepo.save(activityHistory);
		
		return null;
	}

}
