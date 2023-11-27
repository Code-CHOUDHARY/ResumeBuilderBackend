package com.resumebuilder.reportingmanager;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private ReportingManagerRepository reportingManagerRepository;

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
    
    public List<Long> getManagerIdsByUser(User user) {
        List<ReportingManager> reportingManagers = reportingManagerRepository.findByEmployee(user);
        return reportingManagers.stream()
                .map(reportingManager -> reportingManager.getManager().getUser_id())
                .collect(Collectors.toList());
    }

}
