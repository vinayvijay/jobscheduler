package com.jobscheduler.resource;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.jobscheduler.core.JobAllocatorFactory;
import com.jobscheduler.dto.UserDTO;
import com.jobscheduler.dto.WorkOrderDTO;
import com.jobscheduler.model.Skill;
import com.jobscheduler.model.User;
import com.jobscheduler.model.WorkOrder;
import com.jobscheduler.service.SkillsService;
import com.jobscheduler.service.UserService;
import com.jobscheduler.service.WorkOrderService;
import com.jobscheduler.util.JobSchedulerException;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@RestController
@RequestMapping("/jobscheduler")
public class JobSchedulerResource {

	@Autowired
	private UserService userService;

	@Autowired
	private WorkOrderService workOrderService;

	@Autowired
	private SkillsService skillsService;
	
	@Autowired
	private JobAllocatorFactory jobAllocatorFactory;
	
	@RequestMapping(path = "authenticate", method = RequestMethod.POST)
	public LoginResponse authenticate(@RequestBody UserDTO userDto) throws JobSchedulerException {
	
		if(userDto == null) {
			throw new JobSchedulerException("Invalid input");
		}
		User user = userService.verifyIfUserValid(userDto.getUsername(), userDto.getPassword());
		
		Date dt = new Date();
		Calendar cal = Calendar.getInstance(); // creates calendar
	    cal.setTime(dt); // sets calendar time/date
	    cal.add(Calendar.HOUR_OF_DAY, 1); // adds one hour

	    //create a jwt and return it to the client
		String token = Jwts.builder()
			.setSubject(user.getUsername())
			.claim("roles", user.getRole())
			.setIssuedAt(new Date())
			.setExpiration(cal.getTime()) //token expires in 1 hour
			.setIssuer("JobSchedulerApp")
			.signWith(SignatureAlgorithm.HS256, "QEYFNSG@#$")
			.compact();
		return new LoginResponse(token);
	}
	
    private static class LoginResponse {
        public String token;

        public LoginResponse(final String token) {
            this.token = token;
        }
    }
    
	@RequestMapping(path = "workorder", method = RequestMethod.POST)
	public void addWorkOrder(@RequestBody WorkOrderDTO wo ) throws JobSchedulerException {

		workOrderService.addWorkOrder(wo);
		
	}
	
	@RequestMapping(path = "skills", method = RequestMethod.GET)
	public List<String> readAvailableSkills() throws JobSchedulerException {
		List<Skill> skillList = skillsService.retrieveSkills();
		List<String> skills = new ArrayList<String>();
		for(Skill s: skillList)
			skills.add(s.getSkillname());
		return skills;
	}
	
	@RequestMapping(path = "myschedule/{username}", method = RequestMethod.GET)
	public List<WorkOrderDTO> retrieveScheduleForTechnician(@PathVariable String username) throws JobSchedulerException {
		List<WorkOrder> wos = workOrderService.findWorkOrderScheduleForTechnician(username);
		List<WorkOrderDTO> workList = this.convertToDto(wos);
		
		return workList;
	}

	@RequestMapping(path = "vieworders", method = RequestMethod.GET)
	public List<WorkOrderDTO> viewOrders() throws JobSchedulerException {
		List<WorkOrder> wos = workOrderService.findAllWorkOrders();
		List<WorkOrderDTO> workList = this.convertToDto(wos);
		
		return workList;
	}
	
	@RequestMapping(path = "testAlgo", method = RequestMethod.GET)
	public void testSchedulingAlgorithm() throws JobSchedulerException {
		jobAllocatorFactory.getJobAllocator().assignWorkOrdersToTechnicians();
	}
	
	private List<WorkOrderDTO> convertToDto(List<WorkOrder> wos) {
		List<WorkOrderDTO> workList = new ArrayList<>();
		for(WorkOrder w : wos) {
			WorkOrderDTO dto = new WorkOrderDTO();
			dto.setAddress(w.getCustomerAddress());
			dto.setContact(w.getCustomerContact());
			dto.setCustomerName(w.getCustomerName());
			dto.setWorkDuration(w.getWorkDuration());
			dto.setStatus(w.getStatus().toString());
			if (w.getUser() != null)
				dto.setAssignedTechnician(w.getUser().getUsername());
			if(w.getOrderNumber() != null)
				dto.setOrderNumber(w.getOrderNumber().intValue());
			dto.setWorkDuration(w.getWorkDuration());
			dto.setPriority(w.getPriority().toString());
			workList.add(dto);
		}
		
		return workList;
	}
}
