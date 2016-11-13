package com.jobscheduler.model;

import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;
    
    @Column(name = "skill", nullable = false)
    private String skill;

    @Column(name = "shiftduration", nullable = false)
    private Integer shiftDuration;

    @Column(name = "address", nullable = false)
    private String address;

    @Transient
    private float remainingFreeTime;
    
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
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

	public Integer getShiftDuration() {
		return shiftDuration;
	}

	public void setShiftDuration(Integer shiftDuration) {
		this.shiftDuration = shiftDuration;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getRemainingFreeTime() {
		return remainingFreeTime;
	}

	public void setRemainingFreeTime(float remainingFreeTime) {
		this.remainingFreeTime = remainingFreeTime;
	}

}
