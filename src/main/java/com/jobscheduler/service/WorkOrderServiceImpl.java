package com.jobscheduler.service;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jobscheduler.dto.WorkOrderDTO;
import com.jobscheduler.model.Priority;
import com.jobscheduler.model.Status;
import com.jobscheduler.model.User;
import com.jobscheduler.model.WorkOrder;
import com.jobscheduler.repository.UserRepository;
import com.jobscheduler.repository.WorkOrderRepository;
import com.jobscheduler.util.JobSchedulerProperties;

@Service("workOrderService")
@Transactional
public class WorkOrderServiceImpl implements WorkOrderService {

	@Autowired
	private WorkOrderRepository workOrderRepository;

	@Autowired
	private JobSchedulerProperties jobSchedulerProperties;
	
	@Autowired
	private UserRepository userRepository; 

	@Override
	public void addWorkOrder(WorkOrderDTO wo) {

		int days = 0;

		WorkOrder order = new WorkOrder();
		order.setCustomerAddress(wo.getAddress());
		order.setCustomerContact(wo.getContact());
		order.setCustomerName(wo.getCustomerName());
		order.setWorkDuration(wo.getWorkDuration());
		order.setSkill(wo.getSkill());
		order.setCreationTime(System.currentTimeMillis());

		switch (wo.getPriority()) {
		case "CRITICAL":
			order.setPriority(Priority.CRITICAL);
			days = jobSchedulerProperties.getSLACritical();
			break;

		case "MAJOR":
			order.setPriority(Priority.MAJOR);
			days = jobSchedulerProperties.getSLAMajor();
			break;

		case "MINOR":
			order.setPriority(Priority.MINOR);
			days = jobSchedulerProperties.getSLAMinor();
			break;
		}

		DateTime t = new DateTime(System.currentTimeMillis());
		LocalDate date = t.toLocalDate().plusDays(days);
		order.setCutoffDate(date.toDateTimeAtStartOfDay().getMillis());
		order.setStatus(Status.OPEN);
		workOrderRepository.save(order);
	}

	@Override
	public List<WorkOrder> findAllWorkOrdersToCompleteTodayForSkill(String skill) {
		DateTime t = new DateTime(System.currentTimeMillis());
		LocalDate date = t.toLocalDate();
		List<WorkOrder> wo = workOrderRepository.findAllByCutoffDateAndSkillAndStatusOrderByPriorityAsc(date.toDateTimeAtStartOfDay().getMillis(), skill, Status.OPEN);
		return wo;
	}

	@Override
	public void updateWorkOrder(WorkOrder wo) {
		workOrderRepository.save(wo);
	}

	@Override
	public List<WorkOrder> findAllByPriorityAndStatusForSkill(String skill, Priority priority) {
		List<WorkOrder> wo = workOrderRepository.findAllByPriorityAndSkillAndStatusOrderByCutoffDateAsc(priority, skill, Status.OPEN);
		return wo;
	}

	@Override
	public List<WorkOrder> findWorkOrderScheduleForTechnician(String username) {
		
		User user = userRepository.findOneByUsername(username);
		List<WorkOrder> wos = workOrderRepository.findAllByUser_IdAndStatusOrderByOrderNumberAsc(user.getId(), Status.ASSIGNED);
		return wos;
	}

	@Override
	public List<WorkOrder> findAllWorkOrders() {
		return (List<WorkOrder>) workOrderRepository.findAll();
	}
}
