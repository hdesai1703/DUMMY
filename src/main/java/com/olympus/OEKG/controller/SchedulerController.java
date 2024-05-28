package com.olympus.OEKG.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.repository.SchedulerDAO;

@Controller
@RequestMapping("/scheduler")
public class SchedulerController {
	
	@Value("${app.baseurl}")
	private String baseUrl;

	@Autowired
	private SchedulerDAO schedulerDAO;

	@RequestMapping("/scheduler")
	public String mappingPage() {
		return "scheduler";
	}

	@PostMapping("/updateSchedule")
	public String updateSchedule(String flag, Integer SCHEDULE_ID,
			Integer BUSINESS_UNIT_SITE_ID, String INTERFACE_NAME,
			String INTERFACE_TYPE, String EVENT_TYPE, String START_TIME,
			String SCHEDULE_TYPE, String BATCH_EXECUTE_ON, Integer MONTH_DAY,
			String WEEK_DAY, String START_DATE, String END_DATE,Integer BATCH,Integer NO_OF_RECORD,Integer CREATED_BY)
			throws ParseException {
		
		boolean isValidEndDate = false;
		String datePattern = "\\d{1,2}-\\d{1,2}-\\d{4}";
		isValidEndDate = END_DATE.matches(datePattern);

		if (!isValidEndDate) {
			schedulerDAO.updateSchedule(utility.update, SCHEDULE_ID, BUSINESS_UNIT_SITE_ID, INTERFACE_NAME,
				INTERFACE_TYPE, EVENT_TYPE, START_TIME, SCHEDULE_TYPE, "ACTIVE",
					BATCH_EXECUTE_ON, MONTH_DAY, WEEK_DAY, START_DATE, null,BATCH,NO_OF_RECORD, CREATED_BY);
		} else if(END_DATE == "" || END_DATE == null){
			schedulerDAO.updateSchedule(utility.update, SCHEDULE_ID, BUSINESS_UNIT_SITE_ID, INTERFACE_NAME,
					INTERFACE_TYPE, EVENT_TYPE, START_TIME, SCHEDULE_TYPE, "COMPLETED",
					BATCH_EXECUTE_ON, MONTH_DAY, WEEK_DAY, START_DATE, null,BATCH,NO_OF_RECORD, CREATED_BY);
		}else{
			schedulerDAO.updateSchedule(utility.update, SCHEDULE_ID, BUSINESS_UNIT_SITE_ID, INTERFACE_NAME,
					INTERFACE_TYPE, EVENT_TYPE, START_TIME, SCHEDULE_TYPE, "COMPLETED",
					BATCH_EXECUTE_ON, MONTH_DAY, WEEK_DAY, START_DATE, END_DATE,BATCH,NO_OF_RECORD, CREATED_BY);
		}

		return "redirect:"+baseUrl+"/oekg/scheduler/scheduler";
	}

	@PostMapping("/addSchedule")
	public String addSchedule(String flag, Integer BUSINESS_UNIT_SITE_ID,
			String INTERFACE_NAME, String INTERFACE_TYPE, String EVENT_TYPE,
			String START_TIME, String SCHEDULE_TYPE, String BATCH_EXECUTE_ON,
			Integer MONTH_DAY, String WEEK_DAY, String START_DATE,
			String END_DATE,Integer BATCH,Integer NO_OF_RECORD,Integer CREATED_BY) throws ParseException {

		if (END_DATE == "" || END_DATE == null) {
			schedulerDAO.updateSchedule(utility.insert, 0, BUSINESS_UNIT_SITE_ID, INTERFACE_NAME,
					INTERFACE_TYPE, EVENT_TYPE, START_TIME, SCHEDULE_TYPE, "ACTIVE",
					BATCH_EXECUTE_ON, MONTH_DAY, WEEK_DAY, START_DATE, null,BATCH,NO_OF_RECORD, CREATED_BY);
		} else {
			schedulerDAO.updateSchedule(utility.insert, 0, BUSINESS_UNIT_SITE_ID, INTERFACE_NAME,
					INTERFACE_TYPE, EVENT_TYPE, START_TIME, SCHEDULE_TYPE, "COMPLETED",
					BATCH_EXECUTE_ON, MONTH_DAY, WEEK_DAY, START_DATE, END_DATE,BATCH,NO_OF_RECORD, CREATED_BY);
		}

		return "redirect:"+baseUrl+"/oekg/scheduler/scheduler";
	}
	
	@PostMapping("/removeScheduler")
	public String removeScheduler(Integer SCHEDULE_ID,String INTERFACE_NAME,Integer CREATED_BY){
		
		System.out.println("inside remove scheduler method");
		schedulerDAO.removeSchedule(SCHEDULE_ID,INTERFACE_NAME, "STOPPED", CREATED_BY);
		return "redirect:"+baseUrl+"/oekg/scheduler/scheduler";
	}

}
