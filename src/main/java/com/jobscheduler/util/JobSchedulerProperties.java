package com.jobscheduler.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Component
@RefreshScope
public class JobSchedulerProperties {

	@Value("${jobscheduling.algorithm}")
	private String jobAllocator;
		
	@Value("${jobscheduling.workorder.minor}")
	private int minor;
	
	@Value("${jobscheduling.workorder.major}")
	private int major;
	
	@Value("${jobscheduling.workorder.critical}")
	private int ciritical;
	
	
	public String getJobAllocatorClassName() {		
		return this.jobAllocator;		
	}
	
	public int getSLAMinor() {
		return this.minor;
	}
	
	public int getSLAMajor() {
		return this.major;
	}
	
	public int getSLACritical() {
		return this.ciritical;
	}
}
