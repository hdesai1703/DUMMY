package com.olympus.OEKG.gridController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.olympus.OEKG.repository.SchedulerDAO;

@RestController
@RequestMapping("/scheduler")
public class SchedulerGridController {
	
	@Autowired
	private SchedulerDAO schedulerDAO;

	@GetMapping("/getAllSchedulerData")
	public Map<String, Object> getAllSchedulerData() {
		return schedulerDAO.getalldata();
	}
	
	@PostMapping("/getSearchSchedulerData")
	public Map<String, Object> getSearchSchedulerData(
			@RequestParam("EVENT_TYPE") String EVENT_TYPE,
			@RequestParam("INTERFACE_NAME") String INTERFACE_NAME,			
			@RequestParam("START_DATE") String START_DATE,
			@RequestParam("END_DATE") String END_DATE, 
			@RequestParam("IS_ACTIVE") String IS_ACTIVE) {
	
		return schedulerDAO.getSearchSchedulerData(EVENT_TYPE, INTERFACE_NAME,
				START_DATE, END_DATE, IS_ACTIVE);
	}
	
	@GetMapping("/getSchedulerTypeList")
	public Map<String, Object> getSchedulerTypeList(){
		return schedulerDAO.getScheduleTypeList();
	}
	
	@GetMapping("/getInterfaceList")
	public Map<String, Object> getInterfaceList(){
		return schedulerDAO.getInterfaceList();
	}
	
	@PostMapping("/getExistSchData")
	public Map<String, Object> existSchData (@RequestParam("BUSINESS_UNIT_SITE_ID") Integer BUSINESS_UNIT_SITE_ID,
			@RequestParam("INTERFACE_NAME") String INTERFACE_NAME, @RequestParam("EVENT_TYPE") String EVENT_TYPE){
		return schedulerDAO.getExsitSchedulerData(BUSINESS_UNIT_SITE_ID, INTERFACE_NAME, EVENT_TYPE);
		
	}
	@GetMapping("/getInterfaceTypeList")
	public Map<String, Object> getInterfaceTypeList() {
		return schedulerDAO.getInterfaceTypeList();
	}
	@PostMapping("/getMappingBUList")
	public Map<String, Object> getMappingBUList(@RequestParam("EMAILADDRESS") String EMAILADDRESS){
		System.out.println("Inside getMapping"+EMAILADDRESS);
		return schedulerDAO.getMappingBUList(EMAILADDRESS);
	}
}
