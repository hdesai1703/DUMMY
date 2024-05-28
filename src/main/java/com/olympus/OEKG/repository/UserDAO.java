package com.olympus.OEKG.repository;

import java.text.ParseException;
import java.util.Map;

public interface UserDAO {
	
	Map<String, Object> getalldata();
	Map<String, Object> getSearchUserData(String USER_NAME, String START_DATE, String END_DATE, String IS_ACTIVE);
	Map<String, Object> updateUser(String flag, Integer USER_ID, String USER_NAME,String USER_TYPE,String PASSWORD,String EMAIL_ADDRESS, String IS_ACTIVE, String START_DATE, String END_DATE,Integer CREATED_BY ) throws ParseException;
	Map<String, Object> changepassword(String EMAIL_ADDRESS, String PASSWORD, String NEW_PASSWORD);
}
