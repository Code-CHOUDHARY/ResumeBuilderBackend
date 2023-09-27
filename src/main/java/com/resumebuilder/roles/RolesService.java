package com.resumebuilder.roles;

import java.util.List;

import com.resumebuilder.exception.RoleException;

//service interface for required method according to functionality.
public interface RolesService {
	
	public Roles addRole(Roles role);

	void deleteRole(Long id) throws RoleException;

	List<Roles> getAllRoles();

	Roles updateRole(Long id, Roles updatedRole) throws RoleException;

}
