package com.resumebuilder.roles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.resumebuilder.exception.RoleException;
import com.resumebuilder.user.UserRepository;

@Service
public class RolesServiceImplementation implements RolesService{
	
	private RolesRepository rolesRepository;
    private UserRepository userRepository;
    
    @Autowired
    public RolesServiceImplementation(RolesRepository rolesRepository, UserRepository userRepository) {
        this.rolesRepository = rolesRepository;
        this.userRepository = userRepository;
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

            return rolesRepository.save(saveRole);
        } catch (Exception e) {
            throw new RoleException("Failed to add role. Because the role with the same name already exists.");
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

	        return rolesRepository.save(existingRole);
	    } catch (Exception e) {
	        throw new RoleException("Failed to update role. " + e.getMessage());
	    }
	}

    @Override
    public void deleteRole(Long id) throws RoleException {
        try {
            Roles existingRole = rolesRepository.findById(id)
                    .orElseThrow(() -> new RoleException("Role not found with id: " + id));

            // Soft delete the role by marking it as deleted
            existingRole.set_deleted(true);
            rolesRepository.save(existingRole);
        } catch (Exception e) {
            throw new RoleException("Failed to delete role. " + e.getMessage());
        }
    }

	@Override
	public List<Roles> getAllRoles() {
		  //Retrieve all roles (including deleted ones)
     return rolesRepository.findAll();
	}



}
