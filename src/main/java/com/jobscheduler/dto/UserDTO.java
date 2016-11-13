package com.jobscheduler.dto;

import com.jobscheduler.model.Role;



public class UserDTO {

	
    private String username;

    private String password;

    private Role role;
    
    private String skill;

    private int shiftDuration;

    private String address;
    
    private Long id;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public int getShiftDuration() {
		return shiftDuration;
	}

	public void setShiftDuration(int shiftDuration) {
		this.shiftDuration = shiftDuration;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
