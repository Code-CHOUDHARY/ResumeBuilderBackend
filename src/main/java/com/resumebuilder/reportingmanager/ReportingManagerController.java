package com.resumebuilder.reportingmanager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.resumebuilder.user.User;
import com.resumebuilder.user.UserRepository;

@RestController
public class ReportingManagerController {

	@Autowired
    private ReportingManagerService reportingManagerService;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/managers")
    public List<User> getManagers() {
    	List<User> managers = userRepository.findManagers();
        return managers;
    }
	
    @PostMapping("/allocate")
    public ResponseEntity<String> allocateReportingManager(@RequestParam("employeeId") Long employeeId, @RequestParam("managerId") Long managerId) {
        reportingManagerService.allocateReportingManager(employeeId, managerId);
        return ResponseEntity.ok("Reporting manager allocated successfully.");
    }
}
