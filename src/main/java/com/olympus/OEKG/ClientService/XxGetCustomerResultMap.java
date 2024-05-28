package com.olympus.OEKG.ClientService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XxGetCustomerResultMap {
    final Logger LOGGER =LoggerFactory.getLogger(XxGetCustomerResultMap.class);

	public Map<String,String> generateCustomerMap(Connection conn,String strUId) 
	{
		Map<String,String> list = new HashMap<String,String>();
		StringBuffer sql = new StringBuffer();
		Statement sta = null;
		ResultSet rs = null;
		
	try
	{
		sta = conn.createStatement();
		sql.append("SELECT U_ID,ACC_CODE,CUST_NAME,LOC_CUST_NAME,CITY,LOC_CITY,ADDRESS,")
			.append("LOC_ADDRESS,STATE_NAME,LOC_STATE_NAME,TEL_NO,LOC_TEL_NO,INACTIVE,")
			.append("ZIP_CODE,COUNTRY,LOCATION,FAX_NO,LOC_FAX_NO,ETQ_CUSTOMER_NUMBER from XX_ETQ_CUSTOMER_PROFILE_DETAIL")
			.append(" WHERE (ETQ_UPLOAD_STATUS=1 OR ETQ_UPLOAD_STATUS IS NULL OR ETQ_UPLOAD_STATUS=0) AND U_ID=").append(strUId);
		
		rs = sta.executeQuery(sql.toString());
		 ResultSetMetaData md = rs.getMetaData();
		    int columns = md.getColumnCount();
		    		    while (rs.next()) {
		        for(int i=1; i<=columns; ++i) {
		        	list.put(i+"",rs.getString(i));
		        }
		    }		    
		    		    
		    
		    return list;
		    
	}
	catch (Exception e) {
		LOGGER.error(e.getMessage());
	}
	finally{
		DbUtils.closeQuietly(rs);
		DbUtils.closeQuietly(sta);
		
	}
	return list;
	}
}