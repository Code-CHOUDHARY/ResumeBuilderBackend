package com.resumebuilder.bulkupload;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.resumebuilder.DTO.RolesDto;
import com.resumebuilder.roles.Roles;
import com.resumebuilder.roles.RolesRepository;

@Service
public class RoleExcelService {
	
	@Autowired
    private RolesRepository rolesRepository;
    
   
    public void save(MultipartFile file) throws IOException {
        List<RolesDto> roles = RoleExcelHelper.convertExcelToListofRoles(file.getInputStream());
        for (RolesDto role : roles) {
            // Check if a record with the same role name already exists
            Roles existingRole = rolesRepository.findByRoleName(role.getRole_name());
            
            Roles roleSave;
            
            if (existingRole != null) {
                // If the record exists, update it
                roleSave = existingRole;
            } else {
                // If the record does not exist, create a new one
                roleSave = new Roles();
            }
            
            roleSave.setRole_name(role.getRole_name());
            roleSave.setModified_on(role.getModified_on());            
            // Save the role 
            rolesRepository.save(roleSave);
        }
    }
    
    public List<Roles> getAllRoles() {
        return this.rolesRepository.findAll();
    }

		
}
