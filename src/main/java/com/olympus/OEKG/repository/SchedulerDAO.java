package com.olympus.OEKG.repository;

import java.text.ParseException;
import java.util.Map;


public interface SchedulerDAO {

	Map<String, Object> getalldata();
	Map<String, Object> getSearchSchedulerData(String EVENT_TYPE, String INTERFACE_NAME, String START_DATE, String END_DATE, String IS_ACTIVE);
	Map<String, Object> updateSchedule(String flag, Integer SCHEDULE_ID, Integer BUSINESS_UNIT_SITE_ID,
			String INTERFACE_NAME, String INTERFACE_TYPE, String EVENT_TYPE, String START_TIME,
			String SCHEDULE_TYPE, String STATUS, String BATCH_EXECTE_ON, Integer MONTH_DAY,
			String WEEK_DAY, String START_DATE, String END_DATE,Integer BATCH,Integer NO_OF_RECORD,Integer CREATED_BY) throws ParseException;
	Map<String, Object> getScheduleTypeList();
	Map<String, Object> getInterfaceList();	
	Map<String, Object> getInterfaceTypeList();	
	Map<String, Object> removeSchedule(Integer SCHEDULE_ID ,String INTERFACE_NAME, String STATUS,Integer CREATED_BY);
	Map<String, Object> getExsitSchedulerData(Integer BUSINESS_UNIT_SITE_ID,
			String INTERFACE_NAME, String EVENT_TYPE);
	Map<String, Object> getMappingBUList(String EMAILADDRESS);
}
