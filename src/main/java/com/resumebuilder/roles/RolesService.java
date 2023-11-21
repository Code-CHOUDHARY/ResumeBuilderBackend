package com.resumebuilder.roles;

import java.security.Principal;
import java.util.List;

import com.resumebuilder.DTO.RolesDto;
import com.resumebuilder.exception.RoleException;

//service interface for required method according to functionality.
public interface RolesService {
	
	public Roles addRole(Roles role, Principal principal);

	void deleteRole(Long id,Principal principal) throws RoleException;

	List<RolesDto> getAllRoles();

	Roles updateRole(Long id, Roles updatedRole, Principal principal) throws RoleException;

}
