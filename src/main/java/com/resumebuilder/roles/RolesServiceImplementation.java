package com.resumebuilder.roles;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.resumebuilder.exception.RoleException;
import com.resumebuilder.user.User;
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
    
//    //add role with same name then it soft delete is activated and if role with new name that will be add
//    @Override
//    public Roles addRole(Roles role, Principal principal) throws RoleException {
//        try {
//        	User user = userRepository.findByEmailId(principal.getName());
//      	
//            // Check if a role with the same name already exists, including deleted roles
//            Roles existingRole = rolesRepository.findByRoleName(role.getRole_name());            
//            if (existingRole != null) {
//                if (existingRole.is_deleted()) {
//                    // If the existing role is soft-deleted, we can either update it or throw an exception.
//                    // Here, we are updating the existing role.
//                    existingRole.set_deleted(false);
//                    existingRole.setModified_by(user.getFull_name());
//                    return rolesRepository.save(existingRole);
//                } else {
//                    // If a role with the same name exists and is not soft-deleted, throw an exception.
//                    throw new RoleException("Role with the same name already exists: " + role.getRole_name());
//                }
//            }
//
//            // If no existing role with the same name, create a new role
//            Roles saveRole = new Roles();
//            saveRole.setRole_name(role.getRole_name());
//            saveRole.setModified_by(user.getFull_name());
//            saveRole.set_deleted(false);
//
//            return rolesRepository.save(saveRole);
//        } catch (Exception e) {
//            throw new RoleException("Failed to add role.");
//        }
//    }
    
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
                    return rolesRepository.save(newRole);
                } else {
                    // If an existing role with the same name exists and is not soft-deleted, throw an exception.
                    throw new RoleException("Role with the same name already exists: " + role.getRole_name());
                }
            }

            // If no existing role with the same name is found, create a new role
            Roles saveRole = new Roles();
            saveRole.setRole_name(role.getRole_name());
            saveRole.setModified_by(user.getFull_name());
            saveRole.set_deleted(false);

            return rolesRepository.save(saveRole);
        } catch (Exception e) {
            throw new RoleException("Failed to add role.");
        }
    }

	
	
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

	        return rolesRepository.save(existingRole);
	    } catch (Exception e) {
	        throw new RoleException("Role does not exist.");
	    }
	}

    @Override
    public void deleteRole(Long id, Principal principal) throws RoleException {
        try {
        	User user = userRepository.findByEmailId(principal.getName());
            Roles existingRole = rolesRepository.findById(id)
                    .orElseThrow(() -> new RoleException("Role not found with id: " + id));

            // Soft delete the role by marking it as deleted
            existingRole.set_deleted(true);
            existingRole.setModified_by(user.getFull_name());
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
