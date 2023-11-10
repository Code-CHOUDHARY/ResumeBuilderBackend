package com.resumebuilder.teamactivity;

import java.security.Principal;
import java.util.List;

import com.resumebuilder.DTO.TeamActivityDto;

public interface TeamActivityService {
	List<TeamActivityDto> findAllByMangers(Long id,Principal p);
	TeamActivityDto addTeamTActivity(TeamActivity req,Principal p);
	TeamActivityDto getTeamActvityChanges(Long activityId,Principal p);
}
