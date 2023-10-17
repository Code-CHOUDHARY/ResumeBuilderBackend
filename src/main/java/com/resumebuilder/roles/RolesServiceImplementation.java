package com.resumebuilder.roles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.resumebuilder.activityhistory.ActivityHistoryService;
import com.resumebuilder.exception.RoleException;
import com.resumebuilder.user.UserRepository;

@Service
public class RolesServiceImplementation implements RolesService{
	
	
	private RolesRepository rolesRepository;
	@Autowired
    private UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    @Autowired
    private ActivityHistoryService activityHistoryService;
    
    @Autowired
    public RolesServiceImplementation(RolesRepository roleRepository, ApplicationEventPublisher eventPublisher) {
        this.rolesRepository = roleRepository;
        this.eventPublisher = eventPublisher;
    }
    
    
	/**
     * Add a new role.
     *
     * @param role The role to be added.
     * @principal represent user identity.
     * @throws RoleException  if there is an issue adding the role.
     * @return Save the role.
     */
	
    @Override
    public Roles addRole(Roles role) throws RoleException {
        try {
            // Check if a role with the same name already exists
            Roles existingRole = rolesRepository.findByRoleName(role.getRole_name());

            if (existingRole != null) {
                throw new RoleException("Role with the same name already exists: " + role.getRole_name());
            }

            Roles saveRole = new Roles();
            saveRole.setRole_name(role.getRole_name());
            saveRole.setModified_by(role.getModified_by());
            saveRole.set_deleted(false);
            
             String activityType = "Add Role";
		     String description = "New Role Added";
		     
		    activityHistoryService.addActivity(activityType, description, role.getRole_name(), null, null);

            return rolesRepository.save(saveRole);
        } catch (Exception e) {
            throw new RoleException("Failed to add role, because the role with the same name already exists.");
        }
    }

	
	
	@Override
	public Roles updateRole(Long id, Roles updatedRole) throws RoleException {
	    try {
	        Roles existingRole = rolesRepository.findById(id)
	                .orElseThrow(() -> new RoleException("Role not found with id: " + id));

	        // Check if the role is marked as deleted
	        if (existingRole.is_deleted()) {
	            throw new RoleException("Cannot update a deleted role.");
	        }

	        // Update the role properties
	        existingRole.setRole_name(updatedRole.getRole_name());
	        existingRole.setModified_by(updatedRole.getModified_by());
	        
	         String activityType = "Update Role";
		     String description = "Change in role data";
		     
		     activityHistoryService.addActivity(activityType, description, updatedRole.getRole_name(), existingRole.getRole_name(),null);

	        return rolesRepository.save(existingRole);
	    } catch (Exception e) {
	        throw new RoleException("Role does not exist.");
	    }
	}

    @Override
    public void deleteRole(Long id) throws RoleException {
        try {
            Roles existingRole = rolesRepository.findById(id)
                    .orElseThrow(() -> new RoleException("Role not found with id: " + id));

            // Soft delete the role by marking it as deleted
            existingRole.set_deleted(true);
            
             String activityType = "Delete Role";
		     String description = "Deleted a Role";
		     
		     activityHistoryService.addActivity(activityType, description, existingRole.getRole_name() + "is Deleted", null, null);
            
            rolesRepository.save(existingRole);
        } catch (Exception e) {
            throw new RoleException("Role does not exist.");
        }
    }

	@Override
	public List<Roles> getAllRoles() {
		  //Retrieve all roles (including deleted ones)
     return rolesRepository.findAll();
	}



}
