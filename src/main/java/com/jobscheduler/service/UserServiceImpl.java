package com.jobscheduler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobscheduler.dto.UserDTO;
import com.jobscheduler.model.User;
import com.jobscheduler.repository.UserRepository;
import com.jobscheduler.util.JobSchedulerException;


@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	public User findById(long id) {
		return userRepository.findOne(id);
	}

	public void saveUser(UserDTO dto) {
		User user = new User();
		user.setAddress(dto.getAddress());
		user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
		user.setRole(dto.getRole());
		user.setShiftDuration(dto.getShiftDuration());
		user.setSkill(dto.getSkill());
		user.setUsername(dto.getUsername());
		userRepository.save(user);
	}

	public void updateUser(UserDTO dto) throws JobSchedulerException {
		User user = userRepository.findOne(dto.getId());
		if(user!=null){
			user.setAddress(dto.getAddress());
			user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
			user.setRole(dto.getRole());
			user.setShiftDuration(dto.getShiftDuration());
			user.setSkill(dto.getSkill());
			user.setUsername(dto.getUsername());

			userRepository.save(user);
		}
		else {
			throw new JobSchedulerException("The user with username"+dto.getUsername()+" not found.");
		}
	}

	
	public void deleteUser(long id) {
		userRepository.delete(id);
	}

	public List<User> findAllUsers() {
		return (List<User>) userRepository.findAll();
	}
	
	public User findOneByUsername(String username) throws JobSchedulerException {
		User user = userRepository.findOneByUsername(username);
		if(user == null)
			throw new JobSchedulerException("User with username "+username+" not found.");
		return user;
	}
	
	public List<User> findAllBySkillName(String skillName) throws JobSchedulerException {
		List<User> users = userRepository.findAllBySkill(skillName);
		if(users == null)
			throw new JobSchedulerException("There are no users with the skill name "+skillName+" in repository.");
		return users;
	}

	@Override
	public User verifyIfUserValid(String username, String password) throws JobSchedulerException {
		
		if(username  == null  || username.equals(""))  {			
			throw new JobSchedulerException("User name is empty");			
		}

		if (password == null || password.equals("")) {			
			throw new JobSchedulerException("Password is empty");			
		}
	
		User user = userRepository.findOneByUsername(username);
		if(user == null)
			throw new JobSchedulerException("User with username "+username+" not found.");

		if (!passwordEncoder.matches(password, user.getPasswordHash())) {
			throw new JobSchedulerException("Password is incorrect");
		}	
		return user;
	}

}
