package com.jobscheduler.core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.DistanceMatrixElement;
import com.jobscheduler.model.User;
import com.jobscheduler.model.WorkOrder;
import com.jobscheduler.util.JobSchedulerException;

public class SimpleJobAllocatorHelper {
	
	public static Map<String, User> buildAddressTechnicianMap(List<User> users) {
		Map<String, User> map = new HashMap<>();
		for(User u : users)
			map.put(u.getAddress(), u);
		return map;
	}
	
	public static Map<String, List<DistanceMatrixElement>> buildAddressDistMatElemMap(List<String>addressList, DistanceMatrix distMatrix) {
		int iMax = distMatrix.rows.length;
		Map<String, List<DistanceMatrixElement>> addressDistElementsMap = new HashMap<>();
		for(int i = 0; i < iMax; i++) {
			addressDistElementsMap.put(addressList.get(i), Arrays.asList(distMatrix.rows[i].elements));
		}
		return addressDistElementsMap;
	}
	
	public static Map<String, WorkOrder> buildAddressWorkOrderMaps(List<WorkOrder> orders) {
		Map<String, WorkOrder> map = new HashMap<>();
		for(WorkOrder w : orders)
			map.put(w.getCustomerAddress(), w);
		return map;
	}

	//returns the technician with smallest distance from work order and who has enough free time to service the work order
	public static User findNearestAvailableTechnician(DistanceMatrixElement[] techAdd, List<User> technicians, WorkOrder wo) {
		
		User tech = null;
		DistanceMatrixElement elem = null;
		int jMax = technicians.size();
		long smallest = Long.MAX_VALUE;
		for(int j = 0; j < jMax; j++) {
			if(SimpleJobAllocatorHelper.canTechnicianAcceptWorkOrder(techAdd[j], technicians.get(j), wo)) {
				//see if this technician having enough free time is nearer than previous
				if(smallest > techAdd[j].distance.inMeters) {
	                smallest = techAdd[j].distance.inMeters;			
	                elem = techAdd[j];
	                tech = technicians.get(j);
				}
			}
		}
			
		if(elem == null)
			return null;
		
		//update the remaining free time of the chosen technician
        float timeNeeded = elem.durationInTraffic.inSeconds + wo.getWorkDuration().floatValue()*3600;
        tech.setRemainingFreeTime(tech.getRemainingFreeTime() - timeNeeded);
        
        return tech;
	}
	
	public static void setRemainingFreeTime(List<User> technicians) {
		for(User u : technicians)
			u.setRemainingFreeTime(8*3600);
	}
	
	public static DistanceMatrix calculateDistances(String[] origins, String[] destinations) throws JobSchedulerException{
		DistanceMatrix matrix = null;
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAv2n8LM3DRtenUASl_3ZANcWg_B7Si8e8");
		try {
			
			DateTime today = new DateTime(); 
			//set the departure time to 10am tomorrow
			DateTime depTime = new DateTime(today.getYear(), today.getMonthOfYear(), today.getDayOfMonth()+1, 10, 0);
			matrix=DistanceMatrixApi.newRequest(context).departureTime(depTime).origins(origins).destinations(destinations).await(); 
		} catch (Exception e) {
			throw new JobSchedulerException(e.getMessage(), e);
		}
		return matrix;
	}
	
	public static String findTechnicianAddressForUsername(String username, List<User> technicians) {
		for(User u : technicians) 
			if(u.getUsername().equalsIgnoreCase(username))
				return u.getAddress();
		return null;
		
	}
	
	public static void buildRoute(DistanceMatrixElement[] elements, List<String> addressList, Map<String, List<DistanceMatrixElement>> addressDistElementsMap, List<String> routeOrderList) {

		String addr = null;
		int jMax = addressList.size();
		long smallest = Long.MAX_VALUE;
		for(int j = 0; j < jMax; j++) {
			if(smallest > elements[j].distance.inMeters && !routeOrderList.contains(addressList.get(j))) {
				smallest = elements[j].distance.inMeters;
				addr = addressList.get(j);
			}
		}
		if (addr != null)
			routeOrderList.add(addr);
	}
	
	public static void resetInvalidPairsToMaxValue(DistanceMatrixElement[] elements, int rowCount) {
		for(int i = 0; i <= rowCount; i++) {
			elements[i].distance.inMeters = Long.MAX_VALUE;
		}
	}
	
	private static boolean canTechnicianAcceptWorkOrder(DistanceMatrixElement distMatrix, User tech, WorkOrder wo) {
		float timeNeeded = distMatrix.durationInTraffic.inSeconds + wo.getWorkDuration().floatValue()*3600;
		System.out.println("Available free time for techinician "+tech.getUsername()+" is :"+tech.getRemainingFreeTime()+" seconds");
		if(tech.getRemainingFreeTime() >= timeNeeded) {
			return true;
		}
		System.out.println("Due to shortage of time technician "+tech.getUsername()+" cannot accept work order at "+wo.getCustomerAddress());
		return false;
	}

//	public static void main(String[] args) {
//		DateTime t = new DateTime(System.currentTimeMillis());
//		LocalDate date = t.toLocalDate();
//		System.out.println(date.toDateTimeAtStartOfDay().getMillis());
//		try {
//			String[] origins=new String[]{"8th Cross, Malleshwaram, Bangalore, India","Belandur, Bangalore, India"};
//			String[] destinations=new String[]{"J.P.Techno park, Millers road, Bangalore, India","Olety Landmark, Shankar mutt, Bangalore, India","Brigade Gateway, Yeshwanthpur, Bangalore, India","Prestige Tech Park, Outer ring road, Bangalore, India"};
//			DistanceMatrix d = SimpleJobAllocatorHelper.calculateDistances(origins, destinations);
//		} catch (JobSchedulerException e) {
//			e.printStackTrace();
//		}
//	}
}
