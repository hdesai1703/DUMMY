package com.olympus.OEKG.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import com.olympus.OEKG.Utility.DBUtil;
import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.model.BusinessDetails;

@Repository
public class EtqMonitorDAOImpl implements EtqMonitorDAO {
	BusinessDetails bu_Details = BusinessDetails.buObj();
	final Logger LOGGER = LoggerFactory.getLogger(EtqMonitorDAOImpl.class);
	private utility objUtil = new utility();
	@Value("${spring.datasource.url}")
	private String mssqlUrl;
	@Value("${spring.datasource.username}")
	private String mssqlUsername;
	@Value("${spring.datasource.password}")
	private String mssqlPassword;

	@Override
	public Map<String, Object> getallCompdata() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call GET_ALL_COMP_DATA (?)}");
			callableStatement.setInt(1, bu_Details.getBU_ID());
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getallCompdata", "", LOGGER, null, "", e.getMessage());
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getallCustdata() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call GET_ALL_CUST_DATA (?)}");
			System.out.println("BU_NAME: " + bu_Details.getBU_ID());
			callableStatement.setInt(1, bu_Details.getBU_ID());
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getallCustdata", "", LOGGER, null, "", e.getMessage());
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getallAddRefdata() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call GET_ALL_ADD_REF_DATA (?)}");
			callableStatement.setInt(1, bu_Details.getBU_ID());
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getallAddRefdata", "", LOGGER, null, "", e.getMessage());
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getSearchCompData(String REFERENCE_NUMBER, Integer ETQ_UPLOAD_STATUS,
			Integer DEBUG_LEVEL, String REQUEST_DATE_FROM, String REQUEST_DATE_TO, Integer BATCH_ID) {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call XX_ETQ_GET_COMPLAINT_PRC (?,?,?,?,?,?,?)}");
			callableStatement.setString(1, REFERENCE_NUMBER);
			callableStatement.setInt(2, ETQ_UPLOAD_STATUS);
			callableStatement.setInt(3, DEBUG_LEVEL);
			if (REQUEST_DATE_FROM.equals("")) {
				callableStatement.setString(4, null);
			} else {
				callableStatement.setString(4, REQUEST_DATE_FROM);
			}
			if (REQUEST_DATE_TO.equals("")) {
				callableStatement.setString(5, null);
			} else {
				callableStatement.setString(5, REQUEST_DATE_TO);
			}
			callableStatement.setInt(6, BATCH_ID);
			callableStatement.setInt(7, bu_Details.getBU_ID());
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getSearchCompData", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getSearchCompData  Data");
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getSearchCompData", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getSearchCompData Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getSearchCusData(String REFERENCE_NUMBER, Integer ETQ_UPLOAD_STATUS, Integer DEBUG_LEVEL,
			String REQUEST_DATE_FROM, String REQUEST_DATE_TO, Integer BATCH_ID) {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call XX_ETQ_GET_CUSTOMER_PRC (?,?,?,?,?,?,?)}");
			callableStatement.setString(1, REFERENCE_NUMBER);
			callableStatement.setInt(2, ETQ_UPLOAD_STATUS);
			callableStatement.setInt(3, DEBUG_LEVEL);
			if (REQUEST_DATE_FROM.equals("")) {
				callableStatement.setString(4, null);
			} else {
				callableStatement.setString(4, REQUEST_DATE_FROM);
			}
			if (REQUEST_DATE_TO.equals("")) {
				callableStatement.setString(5, null);
			} else {
				callableStatement.setString(5, REQUEST_DATE_TO);
			}
			callableStatement.setInt(6, BATCH_ID);
			callableStatement.setInt(7, bu_Details.getBU_ID());
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getSearchCusData", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getSearchCusData Data");
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getSearchCusData", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getSearchCusData Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getSearchAddRefData(String REFERENCE_NUMBER, Integer ETQ_UPLOAD_STATUS,
			Integer DEBUG_LEVEL, String REQUEST_DATE_FROM, String REQUEST_DATE_TO, Integer BATCH_ID) {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call XX_ETQ_GET_ADD_REF_PRC (?,?,?,?,?,?,?)}");
			callableStatement.setString(1, REFERENCE_NUMBER);
			callableStatement.setInt(2, ETQ_UPLOAD_STATUS);
			callableStatement.setInt(3, DEBUG_LEVEL);
			if (REQUEST_DATE_FROM.equals("")) {
				callableStatement.setString(4, null);
			} else {
				callableStatement.setString(4, REQUEST_DATE_FROM);
			}
			if (REQUEST_DATE_TO.equals("")) {
				callableStatement.setString(5, null);
			} else {
				callableStatement.setString(5, REQUEST_DATE_TO);
			}
			callableStatement.setInt(6, BATCH_ID);
			callableStatement.setInt(7, bu_Details.getBU_ID());
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getSearchAddRefData", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getSearchAddRefData Data");
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getSearchAddRefData", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getSearchAddRefData Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getActivityDetails(Integer INTERFACE_DOCUMENT_ID, int BATCH_ID) {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call XX_GET_ACTIVITY_DETAILS_PRC(?,?)}");
			callableStatement.setInt(1, INTERFACE_DOCUMENT_ID);
			callableStatement.setInt(2, BATCH_ID);
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getActivityDetails", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting Config Search Data");
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getActivityDetails", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting Config Search Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getContactDetails(Integer INTERFACE_DOCUMENT_ID, int BATCH_ID) {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call XX_GET_CONTACT_DETAILS_PRC(?,?)}");
			callableStatement.setInt(1, INTERFACE_DOCUMENT_ID);
			callableStatement.setInt(2, BATCH_ID);
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getContactDetails", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting Config Search Data");
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getContactDetails", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting Config Search Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getRequestStatus() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("XX_ETQ_GET_REQUEST_STATUS_PRC");
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getRequestStatus", "", LOGGER, null, "", e.getMessage());
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getPendingCompData(String REFERENCE_NUMBER, String INTERFACE_NAME) {
		List<Map<String, String>> data = objUtil.getBCDatabaseCount(6, INTERFACE_NAME);
		List<Map<String, String>> details = objUtil.getBCDatabaseDetails(6, INTERFACE_NAME);
		String server_path ="";
		if (!data.isEmpty()) {
			for (Map<String, String> map : data) {
				map.get("DATA_COUNT");
			}
		}
		if (!details.isEmpty()) {
			for (Map<String, String> map : details) {
				server_path = map.get("SQL_DRIVER");
				DBUtil.DRIVER_CLASS_CP = server_path;
				DBUtil.SERVER_PATH_CP = map.get("SERVER_PATH");
				DBUtil.SERVER_USER_CP = map.get("SERVER_USER");
				DBUtil.SERVER_PASSWORD_CP = map.get("SERVER_PASSWORD");
			}
		}
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try {
			Connection ClientConnection = DBUtil.getDataSourceClientComp().getConnection();
			Statement Clientstmt = ClientConnection.createStatement();
			String Sqlquery ="";
			if(server_path.contains("sqlserver")) {
				Sqlquery="SELECT SYSTEM_SOURCE_REF_NO AS REFERENCE_NUM, ETQ_UPLOAD_MESSAGE, ETQ_UPLOAD_STATUS AS UPLOAD_STATUS FROM  XX_COMPLAINT_INTERFACE "
						+ "WHERE (UPPER(SYSTEM_SOURCE_REF_NO) LIKE ISNULL(UPPER('" +REFERENCE_NUMBER
						+ "'), UPPER(SYSTEM_SOURCE_REF_NO)) + '%') AND (ETQ_UPLOAD_STATUS=1 OR ETQ_UPLOAD_STATUS IS NULL OR ETQ_UPLOAD_STATUS=0) AND (ETQ_BATCH_ID IS NULL OR ETQ_BATCH_ID=0)";
			}
			else if (server_path.contains("oracle")) {
				Sqlquery ="SELECT SYSTEM_SOURCE_REF_NO AS REFERENCE_NUM, ETQ_UPLOAD_MESSAGE, ETQ_UPLOAD_STATUS AS UPLOAD_STATUS FROM  XX_COMPLAINT_INTERFACE "
						+ "WHERE (UPPER(SYSTEM_SOURCE_REF_NO) LIKE NVL(UPPER('" +REFERENCE_NUMBER+ "'), UPPER(SYSTEM_SOURCE_REF_NO)) || '%') AND (ETQ_UPLOAD_STATUS=1 OR ETQ_UPLOAD_STATUS IS NULL OR ETQ_UPLOAD_STATUS=0) AND (ETQ_BATCH_ID IS NULL OR ETQ_BATCH_ID=0)";
			}
			ResultSet ClientRS = Clientstmt.executeQuery(Sqlquery);
			List<Map<String, String>> pendingData = objUtil.generateResultSetToMap(ClientRS);
			mapResponse.put("Data", pendingData);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(ClientRS);
			DbUtils.closeQuietly(Clientstmt);
			DbUtils.closeQuietly(ClientConnection);
		} catch (SQLException e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getPendingCompData", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getPendingCompData Data");
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getPendingCompData", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getPendingCompData Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getPendingCusData(String REFERENCE_NUMBER, String INTERFACE_NAME) {
		List<Map<String, String>> data = objUtil.getBCDatabaseCount(6, INTERFACE_NAME);
		List<Map<String, String>> details = objUtil.getBCDatabaseDetails(6, INTERFACE_NAME);
		if (!data.isEmpty()) {
			for (Map<String, String> map : data) {
				map.get("DATA_COUNT");
			}
		}
		if (!details.isEmpty()) {
			for (Map<String, String> map : details) {
				String server_path = map.get("SQL_DRIVER");
				DBUtil.DRIVER_CLASS_CT = server_path;
				DBUtil.SERVER_PATH_CT = map.get("SERVER_PATH");
				DBUtil.SERVER_USER_CT = map.get("SERVER_USER");
				DBUtil.SERVER_PASSWORD_CT = map.get("SERVER_PASSWORD");
			}
		}
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try {
			Connection ClientConnection = DBUtil.getDataSourceClient().getConnection();
			Statement Clientstmt = ClientConnection.createStatement();
			ResultSet ClientRS = Clientstmt.executeQuery(
					"SELECT ACC_CODE AS REFERENCE_NUM, ETQ_UPLOAD_MESSAGE, ETQ_UPLOAD_STATUS AS UPLOAD_STATUS FROM XX_ETQ_CUSTOMER_PROFILE_DETAIL "
							+ "WHERE (UPPER(ACC_CODE) LIKE ISNULL(UPPER('" + REFERENCE_NUMBER
							+ "'), UPPER(ACC_CODE)) + '%') AND (ETQ_UPLOAD_STATUS=1 OR ETQ_UPLOAD_STATUS IS NULL OR ETQ_UPLOAD_STATUS=0) AND (ETQ_BATCH_ID IS NULL OR ETQ_BATCH_ID=0)");
			List<Map<String, String>> pendingData = objUtil.generateResultSetToMap(ClientRS);
			mapResponse.put("Data", pendingData);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(ClientRS);
			DbUtils.closeQuietly(Clientstmt);
			DbUtils.closeQuietly(ClientConnection);
		} catch (SQLException e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getPendingCusData", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getPendingCusData Data");
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getPendingCusData", "", LOGGER, null, "",
					" ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getPendingCusData Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getPendingAddRefData(String REFERENCE_NUMBER, String INTERFACE_NAME) {
		List<Map<String, String>> data = objUtil.getBCDatabaseCount(3, INTERFACE_NAME);
		List<Map<String, String>> details = objUtil.getBCDatabaseDetails(3, INTERFACE_NAME);
		if (!data.isEmpty()) {
			for (Map<String, String> map : data) {
				map.get("DATA_COUNT");
			}
		}
		if (!details.isEmpty()) {
			for (Map<String, String> map : details) {
				String server_path = map.get("SQL_DRIVER");
				DBUtil.DRIVER_CLASS_AD = server_path;
				DBUtil.SERVER_PATH_AD = map.get("SERVER_PATH");
				DBUtil.SERVER_USER_AD = map.get("SERVER_USER");
				DBUtil.SERVER_PASSWORD_AD = map.get("SERVER_PASSWORD");
			}
		}
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try {
			Connection ClientConnection = DBUtil.getDataSourceClientAddRef().getConnection();
			Statement Clientstmt = ClientConnection.createStatement();
			ResultSet ClientRS = Clientstmt.executeQuery(
					"SELECT SHISEI_NO AS REFERENCE_NUM, ETQ_UPLOAD_MESSAGE, ETQ_UPLOAD_STATUS AS UPLOAD_STATUS FROM XX_OT_CUST_REFERENCE_NUM "
							+ "WHERE (UPPER(SHISEI_NO) LIKE ISNULL(UPPER('" + REFERENCE_NUMBER
							+ "'), UPPER(SHISEI_NO)) + '%') AND (ETQ_UPLOAD_STATUS=1 OR ETQ_UPLOAD_STATUS IS NULL OR ETQ_UPLOAD_STATUS=0) AND (ETQ_BATCH_ID IS NULL OR ETQ_BATCH_ID=0)");
			List<Map<String, String>> pendingData = objUtil.generateResultSetToMap(ClientRS);
			mapResponse.put("Data", pendingData);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(ClientRS);
			DbUtils.closeQuietly(Clientstmt);
			DbUtils.closeQuietly(ClientConnection);
		} catch (SQLException e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getPendingAddRefData", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getPendingAddRefData Data");
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getPendingAddRefData", "", LOGGER, null, "",
					" ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getPendingAddRefData Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> generateExcelSheet() {
		List<Map<String, String>> data = objUtil.getBCDatabaseCount(6, "COMPLAINT");
		List<Map<String, String>> details = objUtil.getBCDatabaseDetails(6, "COMPLAINT");
		if (!data.isEmpty()) {
			for (Map<String, String> map : data) {
				map.get("DATA_COUNT");
			}
		}
		if (!details.isEmpty()) {
			for (Map<String, String> map : details) {
				String server_path = map.get("SQL_DRIVER");
				DBUtil.DRIVER_CLASS_CP = server_path;
				DBUtil.SERVER_PATH_CP = map.get("SERVER_PATH");
				DBUtil.SERVER_USER_CP = map.get("SERVER_USER");
				DBUtil.SERVER_PASSWORD_CP = map.get("SERVER_PASSWORD");
			}
		}
		CallableStatement callableStatement = null;
		Map<String, Object> mapResponse = new LinkedHashMap<String, Object>();
		try {
			Connection ClientConnection = DBUtil.getDataSourceClientComp().getConnection();
			String strSQL = "SELECT INTERFACE_RECORD_ID AS \"Interface Record ID\",SYSTEM_SOURCE_REF_NO AS \"System Source Reference Number\","
					+ "SYSTEM_SOURCE AS \"System Source\", SYSTEM_SOURCE_REF_NO_2 AS \"System Source Ref. No. (2)\",SYSTEM_SOURCE_2 AS \"System Source (2)\","
					+ "ETQ_BATCH_ID AS \"EtQ Batch ID\",DEC_TREE_ANS_1 AS \"PAE Answer 1\", DEC_TREE_ANS_2 AS \"PAE Answer 2\","
					+ "CASE WHEN COMPLAINT_CLOSURE_DATE IS NOT NULL THEN 'Tier 3' ELSE NULL END AS \"Investigation Level\","
					+ "ETQ_UPLOAD_MESSAGE AS \"EtQ Upload Message\",MAX(UPLOADED_TO_ETQ_DATE) AS \"Uploaded to EtQ Date\" FROM XX_COMPLAINT_INTERFACE CIT WHERE ETQ_UPLOAD_STATUS=3 "
					+ "AND NOT EXISTS (SELECT 1 FROM XX_COMPLAINT_INTERFACE WHERE ETQ_UPLOAD_STATUS=4 AND (SYSTEM_SOURCE_REF_NO=CIT.SYSTEM_SOURCE_REF_NO OR SYSTEM_SOURCE_REF_NO_2=CIT.SYSTEM_SOURCE_REF_NO OR SYSTEM_SOURCE_REF_NO=CIT.SYSTEM_SOURCE_REF_NO_2 OR SYSTEM_SOURCE_REF_NO_2=CIT.SYSTEM_SOURCE_REF_NO_2)) "
					+ "AND INTERFACE_RECORD_ID=(SELECT MAX(INTERFACE_RECORD_ID) FROM XX_COMPLAINT_INTERFACE WHERE SYSTEM_SOURCE_REF_NO=CIT.SYSTEM_SOURCE_REF_NO OR SYSTEM_SOURCE_REF_NO_2=CIT.SYSTEM_SOURCE_REF_NO OR SYSTEM_SOURCE_REF_NO=CIT.SYSTEM_SOURCE_REF_NO_2 OR SYSTEM_SOURCE_REF_NO_2=CIT.SYSTEM_SOURCE_REF_NO_2) "
					+ "GROUP BY INTERFACE_RECORD_ID,SYSTEM_SOURCE_REF_NO,SYSTEM_SOURCE,SYSTEM_SOURCE_REF_NO_2,SYSTEM_SOURCE_2,ETQ_BATCH_ID,DEC_TREE_ANS_1,DEC_TREE_ANS_2,COMPLAINT_CLOSURE_DATE,ETQ_UPLOAD_MESSAGE order by INTERFACE_RECORD_ID DESC";
			Statement Clientstmt = ClientConnection.createStatement();
			ResultSet resultSetEV = Clientstmt.executeQuery(strSQL);
			List<Map<String, String>> listData = objUtil.generateResultSetToMap(resultSetEV);
			mapResponse.put("Data", listData);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
//			mapResponse.put("resultSet", rs);
//			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(ClientConnection);
		} catch (SQLException e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "generateExcelSheet", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting Config Search Data");
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "generateExcelSheet", "", LOGGER, null, "",
					" ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting Config Search Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getallErrorLogdata() {
		List<Map<String, String>> data = objUtil.getBCDatabaseCount(6, "COMPLAINT");
		List<Map<String, String>> details = objUtil.getBCDatabaseDetails(6, "COMPLAINT");
		if (!data.isEmpty()) {
			for (Map<String, String> map : data) {
				map.get("DATA_COUNT");
			}
		}
		if (!details.isEmpty()) {
			for (Map<String, String> map : details) {
				String server_path = map.get("SQL_DRIVER");
				DBUtil.DRIVER_CLASS_CP = server_path;
				DBUtil.SERVER_PATH_CP = map.get("SERVER_PATH");
				DBUtil.SERVER_USER_CP = map.get("SERVER_USER");
				DBUtil.SERVER_PASSWORD_CP = map.get("SERVER_PASSWORD");
			}
		}
		CallableStatement callableStatement = null;
		Map<String, Object> mapResponse = new LinkedHashMap<String, Object>();
		try {
			Connection ClientConnection = DBUtil.getDataSourceClientComp().getConnection();
			String strSQL = "SELECT ETQ_COMPLAINT_NUMBER AS \"Complaint Number\", INTERFACE_RECORD_ID AS \"Interface Record ID\",SYSTEM_SOURCE_REF_NO AS \"System Source Reference Number\","
					+ "SYSTEM_SOURCE AS \"System Source\", SYSTEM_SOURCE_REF_NO_2 AS \"System Source Ref. No. (2)\",SYSTEM_SOURCE_2 AS \"System Source (2)\","
					+ "ETQ_BATCH_ID AS \"EtQ Batch ID\",DEC_TREE_ANS_1 AS \"PAE Answer 1\", DEC_TREE_ANS_2 AS \"PAE Answer 2\","
					+ "CASE WHEN COMPLAINT_CLOSURE_DATE IS NOT NULL THEN 'Tier 3' ELSE NULL END AS \"Investigation Level\","
					+ "ETQ_UPLOAD_MESSAGE AS \"EtQ Upload Message\",MAX(UPLOADED_TO_ETQ_DATE) AS \"Uploaded to EtQ Date\" FROM XX_COMPLAINT_INTERFACE CIT WHERE ETQ_UPLOAD_STATUS=3 "
					+ "AND NOT EXISTS (SELECT 1 FROM XX_COMPLAINT_INTERFACE WHERE ETQ_UPLOAD_STATUS=4 AND (SYSTEM_SOURCE_REF_NO=CIT.SYSTEM_SOURCE_REF_NO OR SYSTEM_SOURCE_REF_NO_2=CIT.SYSTEM_SOURCE_REF_NO OR SYSTEM_SOURCE_REF_NO=CIT.SYSTEM_SOURCE_REF_NO_2 OR SYSTEM_SOURCE_REF_NO_2=CIT.SYSTEM_SOURCE_REF_NO_2)) "
					+ "AND INTERFACE_RECORD_ID=(SELECT MAX(INTERFACE_RECORD_ID) FROM XX_COMPLAINT_INTERFACE WHERE SYSTEM_SOURCE_REF_NO=CIT.SYSTEM_SOURCE_REF_NO OR SYSTEM_SOURCE_REF_NO_2=CIT.SYSTEM_SOURCE_REF_NO OR SYSTEM_SOURCE_REF_NO=CIT.SYSTEM_SOURCE_REF_NO_2 OR SYSTEM_SOURCE_REF_NO_2=CIT.SYSTEM_SOURCE_REF_NO_2) "
					+ "GROUP BY ETQ_COMPLAINT_NUMBER,INTERFACE_RECORD_ID,SYSTEM_SOURCE_REF_NO,SYSTEM_SOURCE,SYSTEM_SOURCE_REF_NO_2,SYSTEM_SOURCE_2,ETQ_BATCH_ID,DEC_TREE_ANS_1,DEC_TREE_ANS_2,COMPLAINT_CLOSURE_DATE,ETQ_UPLOAD_MESSAGE order by INTERFACE_RECORD_ID DESC";
			Statement Clientstmt = ClientConnection.createStatement();
			ResultSet resultSetEV = Clientstmt.executeQuery(strSQL);
			List<Map<String, String>> listData = objUtil.generateResultSetToMap(resultSetEV);
			mapResponse.put("Data", listData);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
//			mapResponse.put("resultSet", rs);
//			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(resultSetEV);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(ClientConnection);
		} catch (SQLException e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getallErrorLogdata", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getallErrorLogdata Data");
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getallErrorLogdata", "", LOGGER, null, "",
					" ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getallErrorLogdata Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> updateErrorLogdata(String REFERENCE_NUMBER, String COMPLAINT_NUMBER) {
		List<Map<String, String>> data = objUtil.getBCDatabaseCount(6, "COMPLAINT");
		List<Map<String, String>> details = objUtil.getBCDatabaseDetails(6, "COMPLAINT");
		if (!data.isEmpty()) {
			for (Map<String, String> map : data) {
				map.get("DATA_COUNT");
			}
		}
		if (!details.isEmpty()) {
			for (Map<String, String> map : details) {
				String server_path = map.get("SQL_DRIVER");
				DBUtil.DRIVER_CLASS_CP = server_path;
				DBUtil.SERVER_PATH_CP = map.get("SERVER_PATH");
				DBUtil.SERVER_USER_CP = map.get("SERVER_USER");
				DBUtil.SERVER_PASSWORD_CP = map.get("SERVER_PASSWORD");
			}
		}
		CallableStatement callableStatement = null;
		Map<String, Object> mapResponse = new LinkedHashMap<String, Object>();
		try {
			Connection ClientConnection = DBUtil.getDataSourceClientComp().getConnection();
			String strSQL = "UPDATE XX_COMPLAINT_INTERFACE SET ETQ_COMPLAINT_NUMBER = '" + COMPLAINT_NUMBER
					+ "', ETQ_UPLOAD_MESSAGE = ETQ_UPLOAD_MESSAGE+' || Record was reprocessed by manually' , ETQ_UPLOAD_STATUS = 4 WHERE SYSTEM_SOURCE_REF_NO = '"
					+ REFERENCE_NUMBER + "'";
			Statement Clientstmt = ClientConnection.createStatement();
			Clientstmt.executeUpdate(strSQL);
			Connection ClientConnection2 = DBUtil.getDataSourceClientComp().getConnection();
			String strSQL2 = "SELECT ETQ_COMPLAINT_NUMBER AS 'Complaint Number' ,SYSTEM_SOURCE_REF_NO AS 'System Source Reference Number' ,ETQ_UPLOAD_MESSAGE AS 'EtQ Upload Message'  FROM XX_COMPLAINT_INTERFACE WHERE SYSTEM_SOURCE_REF_NO = '"
					+ REFERENCE_NUMBER + "'";
			Statement Clientstmt2 = ClientConnection2.createStatement();
			ResultSet resultSetEV = Clientstmt2.executeQuery(strSQL2);
			List<Map<String, String>> listData = objUtil.generateResultSetToMap(resultSetEV);
			mapResponse.put("Data", listData);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
//			mapResponse.put("resultSet", rs);
//			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(resultSetEV);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(ClientConnection);
		} catch (SQLException e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "updateErrorLogdata", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting Config Search Data");
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "updateErrorLogdata", "", LOGGER, null, "",
					" ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting updateErrorLogdata Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getBuID(String BUSSINESS_UNIT_SITE) {
		System.out.println("Ind=side get buid :" +BUSSINESS_UNIT_SITE);
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		Integer BUID = 0;
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con
					.prepareCall("{call XX_ETQ_GET_BUSINESS_UNIT_ID_UPLOAD_FILE_PRC (?)}");
			callableStatement.setString(1, BUSSINESS_UNIT_SITE);
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			if (!data.isEmpty()) {
				for (Map<String, String> map : data) {
					BUID = Integer.parseInt(map.get("BUSINESS_UNIT_SITE_ID"));
					
				}
			}
			bu_Details.setBU_ID(BUID);
			bu_Details.setBU_NAME(BUSSINESS_UNIT_SITE);
			System.out.println("In dao"+BUID);
			mapResponse.put("Data", data);
			mapResponse.put("BUID", BUID);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			e.printStackTrace();
			utility.exceptionLog("EtqMonitorDAOImpl", "getBuID", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getBuID Data");
		} catch (Exception e) {
			e.printStackTrace();
			utility.exceptionLog("EtqMonitorDAOImpl", "getBuID", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getBuID Data");
		}
		return mapResponse;
	}
}
