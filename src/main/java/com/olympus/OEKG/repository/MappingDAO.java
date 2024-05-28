package com.olympus.OEKG.repository;

import java.text.ParseException;
import java.util.Map;

public interface MappingDAO {


	Map<String, Object> getalldata();
	Map<String, Object> getSearchMappingData(Integer ROLE_ID, String USER_NAME, String IS_ACTIVE, String START_DATE, String END_DATE);
	Map<String, Object> updateMapping(String flag, Integer ROLE_USER_ID, Integer ROLE_ID,
			Integer USER_ID, String IS_ACTIVE, String START_DATE, String END_DATE, Integer BUSINESS_UNIT_SITE_ID,Integer CREATED_BY) throws ParseException;
	Map<String, Object> getEmailAddressList();	
	Map<String, Object> getMappingBUList(String EMAILADDRESS);	
//	Map<String, Object> testScheduler();
}
