package com.resumebuilder.projects;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.resumebuilder.user.User;

@Repository
public interface EmployeeProjectRepository extends JpaRepository<EmployeeProject, Long >{

	void save(User user);

}
