package com.resumebuilder.activityhistory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.DTO.ActivityHistoryDto;
import com.resumebuilder.DTO.TeamActivityDto;
import com.resumebuilder.exception.ActivityException;
import com.resumebuilder.exception.UserNotFoundException;

/**
 * REST controller for managing technology records.
 */

@RestController
@RequestMapping("api/activity")
public class ActivityHistoryController {
	
	@Autowired
	private ActivityHistoryService activityHistoryService;

    /**
     * Retrieves all activities.
     *
     * @return ResponseEntity with a list of activities or an error message.
     */
    @GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllActivities() {
        try {
            List<ActivityHistoryDto> activities = activityHistoryService.getAllAcitivities();
            return new ResponseEntity<>(activities, HttpStatus.OK);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return new ResponseEntity<>("Failed to retrieve activities", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Retrieves activities for a specific user.
     *
     * @param userId The ID of the user.
     * @return ResponseEntity with a list of activities or an error message.
     */
    @GetMapping("/byUser/{userId}")
    @PreAuthorize("hasAnyRole('MANAGER', 'USER')")
    public ResponseEntity<?> getActivitiesByUser(@PathVariable Long userId) {
        try {
            List<ActivityHistoryDto> activities = activityHistoryService.getActivitiesByUser(userId);
            return new ResponseEntity<>(activities, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to retrieve activities", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Retrieves team activities based on activity history.
     *
     * @param managerId The ID of the manager.
     * @return ResponseEntity with a list of team activities or an error message.
     */
    @GetMapping("/byManager/{managerId}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<List<TeamActivityDto>> getTeamActivitiesByManager(@PathVariable Long managerId) {
        try {
            List<TeamActivityDto> teamActivities = activityHistoryService.getActivitiesByReportingManager(managerId);
            return new ResponseEntity<>(teamActivities, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
