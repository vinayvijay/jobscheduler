package com.jobscheduler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobscheduler.model.Skill;
import com.jobscheduler.repository.SkillRepository;
import com.jobscheduler.util.JobSchedulerException;

@Service("skillsService")
@Transactional
public class SkillsServiceImpl implements SkillsService {

	@Autowired
	private SkillRepository skillRepository;

	public List<Skill> retrieveSkills() throws JobSchedulerException {
		return (List<Skill>) skillRepository.findAll();
	}
}
