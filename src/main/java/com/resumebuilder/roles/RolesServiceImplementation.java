package com.resumebuilder.roles;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.exception.RoleException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

@Service
public class RolesServiceImplementation implements RolesService{
	
	
	private RolesRepository rolesRepository;
	@Autowired
    private UserRepository userRepository;
    @Autowired
    private ActivityHistoryService activityHistoryService;
    
    @Autowired
    public RolesServiceImplementation(RolesRepository roleRepository, ApplicationEventPublisher eventPublisher) {
        this.rolesRepository = roleRepository;
    }
    
    /**
     * Add a new role.
     *
     * @param role The role to be added.
     * @principal represent user identity.
     * @throws RoleException  if there is an issue adding the role.
     * @return Save the role.
     */
    
    //add role with same name of soft deleted role and already same role (soft deleted) also be maintain in DB 
    @Override
    public Roles addRole(Roles role, Principal principal) throws RoleException {
        try {
            User user = userRepository.findByEmailId(principal.getName());
            
            // Check if a role with the same name already exists (including soft-deleted roles)
            Roles existingRole = rolesRepository.findByRoleName(role.getRole_name());

            if (existingRole != null) {
                // If an existing role with the same name exists
                if (existingRole.is_deleted()) {
                    // If it's soft-deleted, create a new role without overwriting the existing soft-deleted role
                    Roles newRole = new Roles();
                    newRole.setRole_name(role.getRole_name());
                    newRole.setModified_by(user.getFull_name());
                    newRole.set_deleted(false);
                    
                    // Save the new role to the database
                    rolesRepository.save(newRole);

                    String activityType = "Add Role";
                    String description = "New Role Added";
                    activityHistoryService.addActivity(activityType, description, role.getRole_name(), null, user.getFull_name());
                    
                    return newRole; // Return the newly created role
                } else {
                    // If an existing role with the same name exists and is not soft-deleted, throw an exception.
                    throw new RoleException("Role with the same name already exists: " + role.getRole_name());
                }
            } else {
                // If no role with the same name exists, create and save the new role
                role.setModified_by(user.getFull_name());
                role.set_deleted(false);
                role = rolesRepository.save(role);
                
                String activityType = "Add Role";
                String description = "New Role Added";
                activityHistoryService.addActivity(activityType, description, role.getRole_name(), null, user.getFull_name());
                
                return role; // Return the newly created role
            }
        } catch (Exception e) {
            // Handle exceptions appropriately
            throw new RoleException("An error occurred while adding the role: " + e.getMessage());
        }
    }
	
    /**
     * Update an existing role by ID.
     *
     * @param id         The ID of the role to be updated.
     * @param updatedRole The updated role details.
     * @param principal  Represents user identity.
     * @throws RoleException if there is an issue updating the role.
     * @return The updated role.
     */
    
	@Override
	public Roles updateRole(Long id, Roles updatedRole, Principal principal) throws RoleException {
	    try {
	    	User user = userRepository.findByEmailId(principal.getName());
	        Roles existingRole = rolesRepository.findById(id)
	                .orElseThrow(() -> new RoleException("Role not found with id: " + id));

	        // Check if the role is marked as deleted
	        if (existingRole.is_deleted()) {
	            throw new RoleException("Cannot update a deleted role.");
	        }

	        // Update the role properties
	        existingRole.setRole_name(updatedRole.getRole_name());
	        existingRole.setModified_by(user.getFull_name());
	        
	         String activityType = "Update Role";
		     String description = "Change in role data";
		     
		     activityHistoryService.addActivity(activityType, description, updatedRole.getRole_name(), existingRole.getRole_name(),user.getFull_name());

	        return rolesRepository.save(existingRole);
	    } catch (Exception e) {
	        throw new RoleException("Role does not exist.");
	    }
	}
	
	/**
     * Delete a role by ID.
     *
     * @param id        The ID of the role to be deleted.
     * @param principal Represents user identity.
     * @throws RoleException if there is an issue deleting the role.
     */

    @Override
    public void deleteRole(Long id, Principal principal) throws RoleException {
        try {
        	User user = userRepository.findByEmailId(principal.getName());
            Roles existingRole = rolesRepository.findById(id)
                    .orElseThrow(() -> new RoleException("Role not found with id: " + id));

            // Soft delete the role by marking it as deleted
            existingRole.set_deleted(true);

            existingRole.setModified_by(user.getFull_name());

            
             String activityType = "Delete Role";
		     String description = "Deleted a Role";
		     
		     activityHistoryService.addActivity(activityType, description, existingRole.getRole_name() + "is Deleted", null, user.getFull_name());

            rolesRepository.save(existingRole);
        } catch (Exception e) {
            throw new RoleException("Role does not exist.");
        }
    }
    
    /**
     * Get a list of all roles.
     *
     * @return A list of roles.
     */

	@Override
	public List<Roles> getAllRoles() {
		  //Retrieve all roles (including deleted ones)
     return rolesRepository.findAll();
	}



}
