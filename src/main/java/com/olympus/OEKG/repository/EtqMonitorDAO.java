package com.olympus.OEKG.repository;

import java.util.Map;

public interface EtqMonitorDAO {
	
	Map<String, Object> getallCompdata();
	public Map<String, Object> getSearchCompData(String REFERENCE_NUMBER,
			Integer ETQ_UPLOAD_STATUS, Integer DEBUG_LEVEL, String REQUEST_DATE_FROM,
			String REQUEST_DATE_TO, Integer BATCH_ID);
	public Map<String, Object> getSearchCusData(String REFERENCE_NUMBER,
			Integer ETQ_UPLOAD_STATUS, Integer DEBUG_LEVEL,
			String REQUEST_DATE_FROM, String REQUEST_DATE_TO, Integer BATCH_ID);
	public Map<String, Object> getActivityDetails(Integer INTERFACE_DOCUMENT_ID, int BATCH_ID);
	public Map<String, Object> getContactDetails(Integer INTERFACE_DOCUMENT_ID, int BATCH_ID);
	public Map<String, Object> getallCustdata();
	public Map<String, Object> getallAddRefdata();
	public Map<String, Object> getSearchAddRefData(String REFERENCE_NUMBER,
			Integer ETQ_UPLOAD_STATUS, Integer DEBUG_LEVEL,
			String REQUEST_DATE_FROM, String REQUEST_DATE_TO, Integer BATCH_ID);
	public Map<String, Object> getRequestStatus();
	public Map<String, Object> getPendingCompData(String REFERENCE_NUMBER, String INTERFACE_NAME);
	public Map<String, Object> getPendingCusData(String REFERENCE_NUMBER, String INTERFACE_NAME);
	public Map<String, Object> getPendingAddRefData(String REFERENCE_NUMBER, String INTERFACE_NAME);
	public Map<String, Object> generateExcelSheet();
	Map<String, Object> getallErrorLogdata();
	Map<String, Object> updateErrorLogdata(String REFERENCE_NUMBER, String COMPLAINT_NUMBER);
	Map<String, Object> getBuID(String BUSSINESS_UNIT_SITE);
}
