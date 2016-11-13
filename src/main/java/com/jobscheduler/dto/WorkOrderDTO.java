package com.jobscheduler.dto;

import com.jobscheduler.model.Priority;
import com.jobscheduler.model.Status;

public class WorkOrderDTO {

	private String customerName;
	
	private long contact;
	
	private String address;
	
	private String priority;
	
	private String skill;
	
	private float workDuration;
	
	private int orderNumber;

	private String status;
	
	private String assignedTechnician;
	
	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public long getContact() {
		return contact;
	}

	public void setContact(long contact) {
		this.contact = contact;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getSkill() {
		return skill;
	}

	public void setSkill(String skill) {
		this.skill = skill;
	}

	public float getWorkDuration() {
		return workDuration;
	}

	public void setWorkDuration(float workDuration) {
		this.workDuration = workDuration;
	}

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssignedTechnician() {
		return assignedTechnician;
	}

	public void setAssignedTechnician(String assignedTechnician) {
		this.assignedTechnician = assignedTechnician;
	}

}
