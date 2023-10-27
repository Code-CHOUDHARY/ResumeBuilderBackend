package com.resumebuilder.reportingmanager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.resumebuilder.exception.UserNotFoundException;
import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

@Service
public class ReportingManagerServiceImplementation implements ReportingManagerService{
	
	@Autowired
    private ReportingManagerRepository allocationRepository;
    @Autowired
    private UserRepository userRepository;

    public void allocateReportingManager(Long employeeId, Long managerId) {
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new UserNotFoundException("Employee not found"));

        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new UserNotFoundException("Manager not found"));

        // Create a new entry in the ReportingManagerAllocation table
        ReportingManager allocation = new ReportingManager();
        allocation.setEmployee(employee);
        allocation.setManager(manager);

        allocationRepository.save(allocation);
    }

}
