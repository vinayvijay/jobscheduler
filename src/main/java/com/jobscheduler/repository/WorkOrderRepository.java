package com.jobscheduler.repository;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.jobscheduler.model.Priority;
import com.jobscheduler.model.Status;
import com.jobscheduler.model.WorkOrder;

@RepositoryRestResource
public interface WorkOrderRepository extends PagingAndSortingRepository<WorkOrder, Long> {

	List<WorkOrder> findAllByCutoffDateAndSkillAndStatusOrderByPriorityAsc(Long cutoffdate, String skill, Status status);

	List<WorkOrder> findAllByPriorityAndSkillAndStatusOrderByCutoffDateAsc(Priority priority, String skill, Status status);
	
	List<WorkOrder> findAllByUser_IdAndStatusOrderByOrderNumberAsc(Long userId, Status status);
}
