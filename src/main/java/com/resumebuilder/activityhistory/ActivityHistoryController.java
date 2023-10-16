package com.resumebuilder.activityhistory;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.exception.ActivityException;

/**
 * REST controller for managing technology records.
 */

@RestController
@RequestMapping("api/activity")
public class ActivityHistoryController {
	
	@Autowired
	private ActivityHistoryService activityHistoryService;
	
	@PostMapping("/add")
    public ResponseEntity<ActivityHistory> addActivity(@RequestBody ActivityHistory activity) {
        try {
            // Add the activity
            ActivityHistory addedActivity = activityHistoryService.addActivity(activity);
            
            // Return the added activity with a success response
            return new ResponseEntity<>(addedActivity, HttpStatus.CREATED);
        } catch (ActivityException e) {
            // Handle exceptions and return an error response
        	 return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Get all activities.
     *
     * @return A list of all activities.
     */
    @GetMapping("/all")
    public ResponseEntity<List<ActivityHistory>> getAllActivities() {
        try {
            // Retrieve all activities
            List<ActivityHistory> activities = activityHistoryService.getAllAcitivities();

            // Return the list of activities with a success response
            return new ResponseEntity<>(activities, HttpStatus.OK);
        } catch (ActivityException e) {
            // Handle exceptions and return an error response
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
