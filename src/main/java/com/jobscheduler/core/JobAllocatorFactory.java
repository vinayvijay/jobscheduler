package com.jobscheduler.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.jobscheduler.util.JobSchedulerException;
import com.jobscheduler.util.JobSchedulerProperties;

@Component
public class JobAllocatorFactory {
	
	@Autowired
	private ApplicationContext appContext;
	
	@Autowired
	private JobSchedulerProperties jobSchedulerProperties;
	
	private JobAllocator jobAllocator;
	
	public JobAllocator getJobAllocator() throws JobSchedulerException {

		if(jobAllocator == null) {
			//create a plain java object
			jobAllocator = this.createJobAllocator();
			//convert the java object into spring bean
			appContext.getAutowireCapableBeanFactory().autowireBean(jobAllocator);
		}
		return jobAllocator;
	}
	
	private JobAllocator createJobAllocator() throws JobSchedulerException {
		JobAllocator allocator;
		try {
			String jobAllocator = jobSchedulerProperties.getJobAllocatorClassName();
			Class<?> clazz = Class.forName(jobAllocator);
			Constructor<?> ctor = clazz.getConstructor();
			allocator = (JobAllocator) ctor.newInstance(new Object[] {});
		} catch (InstantiationException e) {
			throw new JobSchedulerException(e.getMessage(), e);
		} catch (IllegalAccessException e) {
			throw new JobSchedulerException(e.getMessage(), e);
		} catch (IllegalArgumentException e) {
			throw new JobSchedulerException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new JobSchedulerException(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			throw new JobSchedulerException(e.getMessage(), e);
		} catch (NoSuchMethodException e) {
			throw new JobSchedulerException(e.getMessage(), e);
		} catch (SecurityException e) {
			throw new JobSchedulerException(e.getMessage(), e);
		}
		
		return allocator;
	}
}
