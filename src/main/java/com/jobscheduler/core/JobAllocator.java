package com.jobscheduler.core;

import com.jobscheduler.util.JobSchedulerException;

public interface JobAllocator {

	void assignWorkOrdersToTechnicians() throws JobSchedulerException;
	
}
