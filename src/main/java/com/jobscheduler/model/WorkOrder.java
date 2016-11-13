package com.jobscheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "workorder")
public class WorkOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false)
	private Long id;

	@OneToOne
	@JoinColumn(name = "userid")
	private User user;
	
	@Column(name = "status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "customername", nullable = false)
	private String customerName;

	@Column(name = "customercontact", nullable = false)
	private Long customerContact;

	@Column(name = "customeraddress", nullable = false)
	private String customerAddress;

	@Column(name = "skill", nullable = false)
	private String skill;

	@Column(name = "priority", nullable = false)
	@Enumerated(EnumType.STRING)
	private Priority priority;

	@Column(name = "cutoffdate", nullable = false)
	private Long cutoffDate;

	@Column(name = "workduration", nullable = false)
	private Float workDuration;

	@Column(name = "creationtime")
	private Long creationTime;

	@Column(name = "resolutionstarttime")
	private Long resolutionStartTime;
	
	@Column(name = "ordernumber")
	private Integer orderNumber;
	
	@Column(name = "assignmentdate")
	private Long assignmentDate;	
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public Priority getPriority() {
		return priority;
	}

	public void setPriority(Priority priority) {
		this.priority = priority;
	}

	public Long getCutoffDate() {
		return cutoffDate;
	}

	public void setCutoffDate(Long cutoffdate) {
		this.cutoffDate = cutoffdate;
	}

	public Long getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Long creationTime) {
		this.creationTime = creationTime;
	}

	public Long getResolutionStartTime() {
		return resolutionStartTime;
	}

	public void setResolutionStartTime(Long resolutionStartTime) {
		this.resolutionStartTime = resolutionStartTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Long getCustomerContact() {
		return customerContact;
	}

	public void setCustomerContact(Long customerContact) {
		this.customerContact = customerContact;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public Float getWorkDuration() {
		return workDuration;
	}

	public void setWorkDuration(Float workDuration) {
		this.workDuration = workDuration;
	}

	public Long getAssignmentDate() {
		return assignmentDate;
	}

	public void setAssignmentDate(Long assignmentDate) {
		this.assignmentDate = assignmentDate;
	}

}
