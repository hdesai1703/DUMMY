package com.olympus.OEKG.repository;

import java.util.Map;

public interface BatchLogDAO {


	Map<String, Object> getSearchBLData(String INTERFACE_NAME, String INTERFACE_TYPE,String STATUS,
			String START_DATE,String END_DATE);
	Map<String, Object> getInterfaceList();	
	Map<String, Object> getalldata();
	Map<String, Object> getLogList();	
	Map<String, Object> getInterfaceTypeList();	
	
	public static void  demo(){
		
	}
	
}
