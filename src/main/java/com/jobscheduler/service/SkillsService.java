package com.jobscheduler.service;

import java.util.List;

import com.jobscheduler.model.Skill;
import com.jobscheduler.util.JobSchedulerException;

public interface SkillsService {
	List<Skill> retrieveSkills() throws JobSchedulerException;
}
