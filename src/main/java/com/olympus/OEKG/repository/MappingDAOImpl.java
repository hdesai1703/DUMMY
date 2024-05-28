package com.olympus.OEKG.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.olympus.OEKG.Utility.DBUtil;
import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.model.BusinessDetails;

@Component
public class MappingDAOImpl implements MappingDAO {
	BusinessDetails bu_Details = BusinessDetails.buObj();
	final Logger LOGGER = LoggerFactory.getLogger(MappingDAOImpl.class);
	private utility objUtil = new utility();
	@Value("${spring.datasource.url}")
	public String mssqlUrl;
	@Value("${spring.datasource.username}")
	public String mssqlUsername;
	@Value("${spring.datasource.password}")
	public String mssqlPassword;

	@Override
	public Map<String, Object> getalldata() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call GET_ALL_ADMIN_MAPPING (?)}");
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
			utility.exceptionLog("MappingDAOImpl", "getalldata", "", LOGGER, null, "", e.getMessage());
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getSearchMappingData(Integer ROLE_ID, String USER_NAME, String IS_ACTIVE,
			String START_DATE, String END_DATE) {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call XX_ETQ_GET_MAPPING_DATA_PRC (?,?,?,?,?,?)}");
			callableStatement.setInt(1, ROLE_ID);
			callableStatement.setString(2, USER_NAME);
			callableStatement.setString(3, IS_ACTIVE);
			if (START_DATE.equals("")) {
				callableStatement.setString(4, null);
			} else {
				callableStatement.setString(4, START_DATE);
			}
			if (END_DATE.equals("")) {
				callableStatement.setString(5, null);
			} else {
				callableStatement.setString(5, END_DATE);
			}
			callableStatement.setInt(6, bu_Details.getBU_ID());
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("MappingDAOImpl", "getSearchMappingData", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getSearchMappingData Data");
		} catch (Exception e) {
			utility.exceptionLog("MappingDAOImpl", "getSearchMappingData", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getSearchMappingData Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getEmailAddressList() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("XX_ETQ_GET_EMAIL_ADDRESS_FROM_USER_TBL_PRC");
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			utility.exceptionLog("MappingDAOImpl", "getEmailAddressList", "", LOGGER, null, "", e.toString());
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getMappingBUList(String EMAILADDRESS) {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call XX_ETQ_GET_BUSINESS_UNIT_BY_USER_PRC(?)}");
			callableStatement.setString(1, EMAILADDRESS);
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			utility.exceptionLog("MappingDAOImpl", "getMappingBUList", "", LOGGER, null, "", e.getMessage());
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> updateMapping(String flag, Integer ROLE_USER_ID, Integer ROLE_ID, Integer USER_ID,
			String IS_ACTIVE, String START_DATE, String END_DATE, Integer BUSINESS_UNIT_SITE_ID, Integer CREATED_BY)
			throws ParseException {
		CallableStatement callableStatement = null;
		String startDate = START_DATE;
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date = sdf1.parse(startDate);
		java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
		String endDate = END_DATE;
		SimpleDateFormat edf1 = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date1 = null;
		java.sql.Date sqlEndDate = null;
		if (endDate == null) {
			sqlEndDate = null;
		} else {
			date1 = edf1.parse(endDate);
			sqlEndDate = new java.sql.Date(date1.getTime());
		}
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con
					.prepareCall("{call XX_ROLE_USER_PRC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			callableStatement.setString(1, flag);
			callableStatement.setInt(2, ROLE_USER_ID);
			callableStatement.setInt(3, ROLE_ID);
			callableStatement.setInt(4, USER_ID);
			callableStatement.setString(5, IS_ACTIVE);
			callableStatement.setDate(6, sqlStartDate);
			callableStatement.setDate(7, sqlEndDate);
			callableStatement.setInt(8, CREATED_BY);
			callableStatement.setInt(9, BUSINESS_UNIT_SITE_ID);
			callableStatement.setString(10, null);
			callableStatement.setString(11, null);
			callableStatement.setString(12, null);
			callableStatement.setString(13, null);
			callableStatement.setString(14, null);
			callableStatement.setString(15, null);
			callableStatement.setString(16, null);
			callableStatement.setString(17, null);
			callableStatement.setString(18, null);
			callableStatement.setString(19, null);
			callableStatement.setString(20, null);
			callableStatement.executeUpdate();
			if (flag.equalsIgnoreCase("I")) {
				LOGGER.info("The user with the user id " + USER_ID + "  was assigned to a new role by USER ID :"
						+ CREATED_BY);
			} else {
				LOGGER.info("The user with the user id " + USER_ID + "  role was updated by USER ID :" + CREATED_BY);
			}
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("MappingDAOImpl", "updateMapping", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting updateMapping Data");
		} catch (Exception e) {
			utility.exceptionLog("MappingDAOImpl", "updateMapping", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting updateMapping Data");
		}
		return mapResponse;
	}
//	@Override
//	public Map<String, Object> testScheduler() {
//		QuartzConfiguration quartzConfiguration = QuartzConfiguration.getQuartzConfigObj();
//		CallableStatement callableStatement = null;
//		ResultSet rs = null;
//		Map<String, Object> mapResponse = new HashMap<String, Object>();
//		try (Connection con = DBUtil.dataSource.getConnection();) {
//			callableStatement = (CallableStatement) con.prepareCall("XX_ETQ_GET_SCHEDULER_REQ_PRC");
//			rs = callableStatement.executeQuery();
//			String dummy = "QuartzJob";
//			String reqValue = null;
//			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
//			if (!data.isEmpty()) {
//				for (Map<String, String> map : data) {
//					reqValue = (map.get("CONFIG_VALUE_2"));
//				}
//			}
//			System.out.println(reqValue);
//			boolean isTriggerremoved = false;
//			System.out.println(quartzConfiguration);
//			isTriggerremoved = quartzConfiguration.removeTrigger(dummy);
//			quartzConfiguration.setSchedularTime(reqValue);
////			quartzConfiguration.addTrigger("QuartzJob", reqValue);
//			System.out.println(quartzConfiguration);
//			mapResponse.put("Data", data);
//			mapResponse.put("CODE", 1);
//			mapResponse.put("MESSAGE", "Data Recieved");
//			LOGGER.info(
//					"Schedular Time Succefull Changed By User ID: " + data + " New Schedular time :" + rs.toString());
////			System.out.println(mapResponse.toString());
//			DbUtils.closeQuietly(rs);
//			DbUtils.closeQuietly(callableStatement);
//			DbUtils.closeQuietly(con);
//		} catch (Exception e) {
//			utility.exceptionLog("MappingDAOImpl", "addUpdateBusinessCenterData", "", LOGGER, null, "", e.getMessage());
//			System.out.println("error came");
//			e.printStackTrace();
//		}
//
//		return mapResponse;
//	}
}
