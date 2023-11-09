package com.resumebuilder.teamactivity;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.DTO.TeamActivityDto;
import com.resumebuilder.exception.TeamActivityHistoryException;
import com.resumebuilder.user.UserService;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class TeamActivityServiceImplementation implements TeamActivityService{

	@Autowired
	private TeamActivityRepository repo;
	@Autowired
	 private UserService userService;
	
	public List<TeamActivityDto> findAllByMangers(Long id,Principal p){
		
		if(userService.findUserByIdUser(id)!=null) {
			
			List<TeamActivityDto> responseList=new ArrayList<>();
			List<TeamActivity> list=repo.findAllByManagers(id);
			if(list!=null) {
//         System.out.println("size"+repo.findAllByManagers(id));
			for(TeamActivity a:list) {
				TeamActivityDto res=new TeamActivityDto();
				res.setTeam_acivity_id(a.getTeam_acivity_id());
				res.setActivity_on(a.getActivity_on());
				res.setActivty_by(a.getActivity_by().getFull_name());
				res.setCurrent_role(a.getCurrent_role());
				res.setDescription(a.getDescription());
				res.setEmployee_id(userService.findUserByIdUser(a.getEmployee_id()).getEmployee_Id());
				res.setEmployee_name(a.getEmployee_name());
				res.setNew_data(a.getNew_data());
				res.setOld_data(a.getOld_data());
				responseList.add(res);       
			}
			return responseList;
		}
			return null;
			}
		else {
			throw new TeamActivityHistoryException("No any Emp found for given Manger Id");
		}
	};
	
	public TeamActivityDto addTeamTActivity(TeamActivity req,Principal p) throws TeamActivityHistoryException {
		req.setActivity_on(new Date());
		
		TeamActivity activity=repo.save(req);
		if(activity!=null) {
			TeamActivityDto res=new TeamActivityDto();
			res.setTeam_acivity_id(activity.getTeam_acivity_id());
			res.setActivity_on(activity.getActivity_on());
			res.setActivty_by(activity.getActivity_by().getFull_name());
			res.setCurrent_role(activity.getCurrent_role());
			res.setDescription(activity.getDescription());
			res.setEmployee_id(userService.findUserByIdUser(activity.getEmployee_id()).getEmployee_Id());
			res.setEmployee_name(activity.getEmployee_name());
			res.setNew_data(activity.getNew_data());
			res.setOld_data(activity.getOld_data());
			return res;
		}
		else throw new TeamActivityHistoryException("unable to Insert team activity");
	};
	
	
	public TeamActivityDto getTeamActvityChanges(Long activityId,Principal p) {
		TeamActivity activity=repo.getById(activityId);
		
		if(activity!=null) {
			TeamActivityDto res=new TeamActivityDto();
			res.setTeam_acivity_id(activity.getTeam_acivity_id());
			res.setActivity_on(activity.getActivity_on());
			res.setActivty_by(activity.getActivity_by().getFull_name());
			res.setCurrent_role(activity.getCurrent_role());
			res.setDescription(activity.getDescription());
			res.setEmployee_id(userService.findUserByIdUser(activity.getEmployee_id()).getEmployee_Id());
			res.setEmployee_name(activity.getEmployee_name());
			res.setNew_data(activity.getNew_data());
			res.setOld_data(activity.getOld_data());
			return res;
		}
		
		return null;
	}

}
