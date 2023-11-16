package com.resumebuilder.activityhistory;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.resumebuilder.DTO.ActivityHistoryDto;
import com.resumebuilder.DTO.TeamActivityDto;
import com.resumebuilder.exception.ActivityException;
import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;
import com.resumebuilder.user.UserService;



@Service
public class ActivityHistoryImplementation implements ActivityHistoryService {
	
	private final ActivityHistoryRepository activityHistoryRepo;
	
	private final UserRepository userRepository;
	
	private final UserService userService;
	
	@Autowired
	public ActivityHistoryImplementation(ActivityHistoryRepository activityHistoryRepo, UserRepository userRepository,
		@Lazy UserService userService) {
		super();
		this.activityHistoryRepo = activityHistoryRepo;
		this.userRepository = userRepository;
		this.userService = userService;
	}
	
	
	/**
     * Add a new Activity record.
     *
     * @param activityHistory The technology record to be added.
     * @throws ActivityException if there is an issue adding the Activity.
     * 
     */
	@Override
	public ActivityHistoryDto addActivity(ActivityHistory activityHistory, Principal principal) throws ActivityException {
		
		 User user = userRepository.findByEmailId(principal.getName());
		 activityHistory.setActivity_by(user.getUser_id());
		 activityHistoryRepo.save(activityHistory);
		
		ActivityHistoryDto activity = new ActivityHistoryDto();
		
		activity.setActivity_type(activityHistory.getActivity_type());
		activity.setDescription(activityHistory.getDescription());
		activity.setNew_data(activityHistory.getNew_data());
		activity.setActivity_by(user.getFull_name());
		activity.setOld_data(activityHistory.getOld_data());
		
		return activity;
	}
	
	/**
	 *  @return The List of Activities record.
	 */
	
	@Override
	public List<ActivityHistoryDto> getAllAcitivities() {
	
		List<ActivityHistory> activities = activityHistoryRepo.findAll();
        return activities.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
	 private ActivityHistoryDto convertToDto(ActivityHistory activity) {
		
	        ActivityHistoryDto dto = new ActivityHistoryDto();
	        dto.setActivity_by(userService.findUserByIdUser(activity.getActivity_by()).getFull_name());
	        dto.setActivity_type(activity.getActivity_type());
	        dto.setDescription(activity.getDescription());
	        dto.setOld_data(activity.getOld_data());
	        dto.setNew_data(activity.getNew_data());
	        return dto;
	 }

	@Override
	public List<ActivityHistoryDto> getActivitiesByUser(Long userId) {
		// TODO Auto-generated method stub
		 Optional<User> userOptional = userRepository.findById(userId);
		 
		 if (userOptional.isPresent()) {
	            User user = userOptional.get();
	            List<ActivityHistory> activities = activityHistoryRepo.findByUser(user);

	            return activities.stream()
	                    .map(this::convertToDto)
	                    .collect(Collectors.toList());
	        } else {
	            throw new UserNotFoundException("User not found with ID: " + userId);
	        }
	}

	@Override
	public List<TeamActivityDto> getActivitiesByReportingManager(Long managerId) {
		List<ActivityHistory> activities = activityHistoryRepo.findActivitiesByReportingManager(managerId);
		return activities.stream()
                .map(this::convertToTeamActivityDto)
                .collect(Collectors.toList());
	}
	private TeamActivityDto convertToTeamActivityDto(ActivityHistory activityHistory) {
        TeamActivityDto teamActivityDto = new TeamActivityDto();
        User user = activityHistory.getUser();
        teamActivityDto.setEmployee_name(user.getFull_name());
        teamActivityDto.setEmployee_id(user.getEmployee_Id());
        teamActivityDto.setCurrent_role(user.getCurrent_role());
        teamActivityDto.setActivty_by(userService.findUserByIdUser(activityHistory.getActivity_by()).getFull_name());
        teamActivityDto.setActivity_on(activityHistory.getActivity_on());
        teamActivityDto.setDescription(activityHistory.getDescription());
        teamActivityDto.setOld_data(activityHistory.getOld_data());
        teamActivityDto.setNew_data(activityHistory.getNew_data());
        return teamActivityDto;
    }
//	userService.findUserByIdUser(activityHistory.getActivity_by()).getFull_name()
}
