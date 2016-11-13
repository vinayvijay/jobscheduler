package com.jobscheduler.util;

public class JobSchedulerException extends Exception {
	
	private int errorCode;
	
	public JobSchedulerException() {
		super();
	}
	
	public JobSchedulerException(String msg) {
		super(msg);
	}
	
	public JobSchedulerException(String msg, Throwable throwable) {
		super(msg, throwable);
	}

	public JobSchedulerException(int errorCode, String msg, Throwable throwable) {
		super(msg, throwable);
		this.errorCode = errorCode;
	}
	
}
