package com.jobscheduler.service;

import java.util.List;

import com.jobscheduler.dto.UserDTO;
import com.jobscheduler.model.User;
import com.jobscheduler.util.JobSchedulerException;


public interface UserService {
	
	User findById(long id);
	User findOneByUsername(String username) throws JobSchedulerException;
	List<User> findAllBySkillName(String skillName) throws JobSchedulerException;
	void saveUser(UserDTO dto);
	void updateUser(UserDTO dto) throws JobSchedulerException;
	void deleteUser(long id);
	List<User> findAllUsers();
	User verifyIfUserValid(String username, String password) throws JobSchedulerException;
}