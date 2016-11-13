package com.jobscheduler.service;

import java.util.List;

import com.jobscheduler.dto.WorkOrderDTO;
import com.jobscheduler.model.Priority;
import com.jobscheduler.model.WorkOrder;

public interface WorkOrderService {

	void addWorkOrder(WorkOrderDTO wo);
	
	List<WorkOrder> findAllWorkOrdersToCompleteTodayForSkill(String skill);
	
	List<WorkOrder> findAllByPriorityAndStatusForSkill(String skill, Priority priority);
	
	List<WorkOrder> findWorkOrderScheduleForTechnician(String username);
	
	List<WorkOrder> findAllWorkOrders();
	
	void updateWorkOrder(WorkOrder wo);
}
