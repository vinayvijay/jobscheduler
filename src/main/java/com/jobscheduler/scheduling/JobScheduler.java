package com.jobscheduler.scheduling;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jobscheduler.core.JobAllocatorFactory;
import com.jobscheduler.util.JobSchedulerException;

@Component
public class JobScheduler {

	@Autowired
	private JobAllocatorFactory jobAllocatorFactory;
	
	//run this job on every weekday at 5 am in the morning
	@Scheduled(cron="0 0 5 ? * MON-FRI ")
	public void scheduleJobs() throws JobSchedulerException {
		jobAllocatorFactory.getJobAllocator().assignWorkOrdersToTechnicians();
	}

}
