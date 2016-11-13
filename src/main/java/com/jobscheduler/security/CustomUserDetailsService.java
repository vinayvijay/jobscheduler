package com.jobscheduler.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobscheduler.dto.UserDTO;
import com.jobscheduler.model.User;
import com.jobscheduler.service.UserService;
import com.jobscheduler.util.JobSchedulerException;


@Service("customUserDetailsService")
public class CustomUserDetailsService implements UserDetailsService{

	static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserService userService;

	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		UserDTO userDto = new UserDTO();
		try {
			User user = userService.findOneByUsername(username);
			userDto.setAddress(user.getAddress());
			userDto.setPassword(user.getPasswordHash());
			userDto.setRole(user.getRole());
			userDto.setShiftDuration(user.getShiftDuration());
			userDto.setSkill(user.getSkill());
			userDto.setUsername(user.getUsername());

		} catch (JobSchedulerException e) {
			logger.info("User not found");
			throw new UsernameNotFoundException("Username not found");
		}
		return new org.springframework.security.core.userdetails.User(userDto.getUsername(), userDto.getPassword(),
				 true, true, true, true, getGrantedAuthorities(userDto));
	}


	private List<GrantedAuthority> getGrantedAuthorities(UserDTO user){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		//authorities.add(new SimpleGrantedAuthority("ROLE_"+user.getRole()));
		logger.info("authorities : {}", authorities);
		return authorities;
	}

}
