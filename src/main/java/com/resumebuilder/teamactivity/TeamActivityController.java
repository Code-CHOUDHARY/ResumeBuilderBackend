package com.resumebuilder.teamactivity;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.DTO.TeamActivityDto;
import com.resumebuilder.user.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/teamActivities")
public class TeamActivityController {

	@Autowired
	private TeamActivityService service;
	@Autowired
	 private UserService userService;
	
	 @GetMapping("/getAllActivities/{managerId}")
	   public ResponseEntity<?> getAllActvitiesBasedManager(@PathVariable("managerId")Long managerId,Principal p){
		
		 
		 try {
			List<TeamActivityDto> list=service.findAllByMangers(managerId,p);
			 
			 if(!list.isEmpty()) {
				 
				 return   ResponseEntity.ok(new TeamActivityResponse(HttpStatus.OK,true,"List of Team Activity",list));
			 }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 return ResponseEntity.ok(new TeamActivityResponse(HttpStatus.INTERNAL_SERVER_ERROR,false,"unable fetch Team Activity for this Manager",null));
		}
		 return ResponseEntity.ok(new TeamActivityResponse(HttpStatus.OK,true,"unable fetch to Team Activity for this Manager",null));
		 
	 }
	
	 @PostMapping("/addActivity")
	   public ResponseEntity<?> addActivity(@RequestBody TeamActivity req,Principal p){
		 //we are not using this controller method in prod     
		// req.setActivity_by(userService.findUserByIdUser(20l));
		 TeamActivityDto activity=service.addTeamTActivity(req,p);
		if(activity!=null) {
			
			return ResponseEntity.ok(new TeamActivityResponse(HttpStatus.OK,true,"Team Activity Added",activity)); 
		}
		return 	ResponseEntity.ok(new TeamActivityResponse(HttpStatus.INTERNAL_SERVER_ERROR,false,"unable fetch Team Activity for this Manager",null));
			
	 }
	 @GetMapping("/getChanges/{activityId}")
	   public ResponseEntity<?> addActivity(@PathVariable("activityId") Long activityId,Principal p){
		      
		 TeamActivityDto activity=service.getTeamActvityChanges(activityId, p);
		 if(activity!=null) {
				
				return ResponseEntity.ok(new TeamActivityResponse(HttpStatus.OK,true,"Team Activity Fetched",activity)); 
			}
		 return ResponseEntity.ok(new TeamActivityResponse(HttpStatus.INTERNAL_SERVER_ERROR,false,"unable fetch Team Activity for this Manager",null));

	 }
	 
}
