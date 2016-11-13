package com.jobscheduler.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.jobscheduler.model.Priority;
import com.jobscheduler.model.Skill;
import com.jobscheduler.model.Status;
import com.jobscheduler.model.User;
import com.jobscheduler.model.WorkOrder;
import com.jobscheduler.service.SkillsService;
import com.jobscheduler.service.UserService;
import com.jobscheduler.service.WorkOrderService;
import com.jobscheduler.util.JobSchedulerException;

@Component
public class SimpleJobAllocator implements JobAllocator {

	@Autowired
	public SkillsService skillsServiceImpl;

	@Autowired
	private UserService userService;

	@Autowired
	private WorkOrderService workOrderService;

	private Map<String, List<WorkOrder>> technicianWorkOrderMap;

	@Override
	public void assignWorkOrdersToTechnicians() throws JobSchedulerException {

		List<Skill> skills = skillsServiceImpl.retrieveSkills();
		for(Skill s : skills) {
			String skill = s.getSkillname();
			//1. Get all the technicians for a skill
			List<User> technicians = userService.findAllBySkillName(skill);
			//if no technicians are there for the skill then continue to next skill
			if(technicians == null || technicians.isEmpty())
				continue;
			//reset the available time for technicians to 8 hours
			SimpleJobAllocatorHelper.setRemainingFreeTime(technicians);
			//initialize addressTechnicianMap
			Map<String,User> addressTechnicianMap = SimpleJobAllocatorHelper.buildAddressTechnicianMap(technicians);
			System.out.println("-----ALLOCATING ORDERS TBC TODAY----------");
			//2. Allocate work orders that have to be completed today
			this.allocateWorkOrdersToBeCompletedToday(skill, technicians, addressTechnicianMap);
			System.out.println("-----ALLOCATING CRITICAL ORDERS----------");
			//3. Allocate all the critical work orders whose cut off date is in future date
			this.allocateCriticalWorkOrders(skill, technicians, addressTechnicianMap);
			System.out.println("-----ALLOCATING MAJOR ORDERS----------");
			//4. Allocate all the major work orders whose cut off date is in future date
			this.allocateMajorWorkOrders(skill, technicians, addressTechnicianMap);
			System.out.println("-----ALLOCATING MINOR ORDERS----------");
			//5. Allocate all the minor work orders whose cut off date is in futture date
			this.allocateMinorWorkOrders(skill, technicians, addressTechnicianMap);

			//6. For each technician, do route planning for the assigned work orders by updating the order number in WO
			this.doRoutePlanningForTechnicians(technicians);

			//7. Reset the class level map to null
			technicianWorkOrderMap = null;
		}

	}

	private void allocateWorkOrdersToBeCompletedToday(String skill, List<User> technicians, Map<String,User> addressTechnicianMap) throws JobSchedulerException {

		// Get all the open work orders that have to be completed today for the skill in order of priority
		List<WorkOrder> orders = workOrderService.findAllWorkOrdersToCompleteTodayForSkill(skill);
		if(orders == null || orders.size() == 0)
			return;

		this.allocateWorkOrders(skill, technicians, addressTechnicianMap, orders);
	}

	private void allocateCriticalWorkOrders(String skill, List<User> technicians, Map<String, User> addressTechnicianMap) throws JobSchedulerException {

		List<WorkOrder> orders = workOrderService.findAllByPriorityAndStatusForSkill(skill, Priority.CRITICAL);
		if(orders == null || orders.size() == 0)
			return;
		this.allocateWorkOrders(skill, technicians, addressTechnicianMap, orders);
	}

	private void allocateMajorWorkOrders(String skill, List<User> technicians, Map<String, User> addressTechnicianMap) throws JobSchedulerException {
		List<WorkOrder> orders = workOrderService.findAllByPriorityAndStatusForSkill(skill, Priority.MAJOR);
		if(orders == null || orders.size() == 0)
			return;
		this.allocateWorkOrders(skill, technicians, addressTechnicianMap, orders);
	}

	private void allocateMinorWorkOrders(String skill, List<User> technicians, Map<String, User> addressTechnicianMap) throws JobSchedulerException {
		List<WorkOrder> orders = workOrderService.findAllByPriorityAndStatusForSkill(skill, Priority.MINOR);
		if(orders == null || orders.size() == 0)
			return;
		this.allocateWorkOrders(skill, technicians, addressTechnicianMap, orders);
	}

	private void allocateWorkOrders(String skill, List<User> technicians, Map<String,User> addressTechnicianMap, List<WorkOrder> orders) throws JobSchedulerException {
		//1. Prepare the data for calculating distances and work order allocation

		Map<String, WorkOrder> addressWorkOrderMap  = SimpleJobAllocatorHelper.buildAddressWorkOrderMaps(orders);
		//create a address list of work orders
		String[] cAddresses = new String[addressWorkOrderMap.keySet().size()];
		cAddresses = (String[]) addressWorkOrderMap.keySet().toArray(cAddresses);
		//create a address list of all technicians
		String[] tAddresses = new String[addressTechnicianMap.keySet().size()];
		tAddresses = (String[]) addressTechnicianMap.keySet().toArray(tAddresses);

		//2. For each work order, calculate the distance between work order and all available technicians using google maps distance matrix api

		DistanceMatrix distMatrix = SimpleJobAllocatorHelper.calculateDistances(cAddresses, tAddresses);

		//3. for each work order, identify the nearest available technician and assign the same

		int numberOfWO = distMatrix.rows.length;
		WorkOrder wo;
		if(technicianWorkOrderMap == null)
			technicianWorkOrderMap = new HashMap<>();
		for(int i = 0; i < numberOfWO; i++) {
			System.out.println("---------->>>>>>>>Allocating work order "+i);
			//get the work order details
			wo = addressWorkOrderMap.get(cAddresses[i]);
			DistanceMatrixElement[] techAddresses = distMatrix.rows[i].elements;
			User technician = SimpleJobAllocatorHelper.findNearestAvailableTechnician(techAddresses, technicians, wo);
			//if no technician is available then move to next work order, to see if any other order can be completed within available free time of any technicians
			if(technician == null)
				continue;
			System.out.println(wo.getCustomerAddress()+"======"+technician.getUsername());
			//update the technician for this work order
			wo.setUser(technician);
			//update the assignment date for the work order
			DateTime t = new DateTime(System.currentTimeMillis());
			LocalDate date = t.toLocalDate();
			wo.setAssignmentDate(date.toDateTimeAtStartOfDay().getMillis());
			//update the work order status
			wo.setStatus(Status.ASSIGNED);
			//update the work order in the database
			workOrderService.updateWorkOrder(wo);

			//add the work order to technicianWorkOrderMap
			if(technicianWorkOrderMap.containsKey(technician.getUsername())) {
				List<WorkOrder> list = technicianWorkOrderMap.get(technician.getUsername());
				list.add(wo);
				technicianWorkOrderMap.put(technician.getUsername(), list);
				System.out.println("Adding wo to global map "+wo.getCustomerAddress());
			}
			else {
				List<WorkOrder> list = new ArrayList<WorkOrder>();
				list.add(wo);
				technicianWorkOrderMap.put(technician.getUsername(), list);
				System.out.println("Adding wo to global map "+wo.getCustomerAddress());
			}
		}
	}

	private void doRoutePlanningForTechnicians(List<User> technicians) throws JobSchedulerException {
		//for each technician
		for (Entry<String, List<WorkOrder>> entry : technicianWorkOrderMap.entrySet()) {

			//calculate distances between various work orders and technicians place to create the shortest path
			//create a 2-d array with distances calculated between each pair, Example shown below
			//				techAddress		WO1		WO2		WO3
			//techAddress		0			4		7		2
			//    WO1			3			0		28		4
			//    WO2			22			23		0		22
			//    WO3			5			6		8		0
			//Shortest Route is techAddress-WO1-WO3-WO2

			////////create various data structures for route calculation//////////
			//1. customer address to work order map
			Map<String, WorkOrder> addressWorkOrderMap  = SimpleJobAllocatorHelper.buildAddressWorkOrderMaps(entry.getValue());

			//2. create a address list having order as sent to google maps api and route list (which will have shortest route)
			List<String> addressList = new ArrayList<>();
			List<String> routeOrderList = new ArrayList<>();
			String techAddress = SimpleJobAllocatorHelper.findTechnicianAddressForUsername(entry.getKey(), technicians);
			addressList.add(techAddress);
			routeOrderList.add(techAddress);//technician starts from his home, hence it will be the first point
			String[] addr = new String[addressWorkOrderMap.keySet().size()];
			addr = (String[]) addressWorkOrderMap.keySet().toArray(addr);
			addressList.addAll(Arrays.asList(addr));
			String[] addressListArray = new String[addressList.size()];
			addressListArray = addressList.toArray(addressListArray);

			//3. pass the addressList as origin and destination to get the distance matrix from google api
			DistanceMatrix distMatrix = SimpleJobAllocatorHelper.calculateDistances(addressListArray, addressListArray);

			//4. create a map having adress and distanceMatrixElements
			Map<String, List<DistanceMatrixElement>> addressDistElementsMap = SimpleJobAllocatorHelper.buildAddressDistMatElemMap(addressList, distMatrix);

			//5. Find the shortest distance from origin to destination starting with technician's address as origin
			int iMax = distMatrix.rows.length;
			for(int i = 0; i < iMax; i++) {

				String address = null;
				int jMax = addressList.size();
				long smallest = Long.MAX_VALUE;
				List<DistanceMatrixElement> elements;
				if(i == 0) {
					elements = Arrays.asList(distMatrix.rows[i].elements);
				}
				else {
					elements = addressDistElementsMap.get(routeOrderList.get(i));
				}
				for(int j = 0; j < jMax; j++) {
					//if smallest distance is greater than that in element and route list does not have the address (as technician goes to one address only once)
					//then go inside
					if(smallest > elements.get(j).distance.inMeters && !routeOrderList.contains(addressList.get(j))) {
						smallest = elements.get(j).distance.inMeters;
						address = addressList.get(j);
					}
				}
				//6. Create a route list of addresses which essentially is the route that technician has to take
				if (address != null)
					routeOrderList.add(address);
			}
			System.out.println("--------------Route for technician "+entry.getKey()+"----------------");

			//7. update the work order 'Order Number' field that stores the route info and persist the work order in  database
			WorkOrder order;
			for(int i = 0; i < iMax; i++) {

				if(i == 0)
					continue;//skip the first element in the list as it is the technician's address
				System.out.println(routeOrderList.get(i));
				order = addressWorkOrderMap.get(routeOrderList.get(i));
				order.setOrderNumber(i);
				order.setAssignmentDate(System.currentTimeMillis());

				//update the work order in database
				workOrderService.updateWorkOrder(order);
			}
		}
	}
}
