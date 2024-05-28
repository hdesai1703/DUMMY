package com.olympus.OEKG.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
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
public class BatchLogDAOimpl implements BatchLogDAO {
	BusinessDetails bu_Details = BusinessDetails.buObj();
	final Logger LOGGER = LoggerFactory.getLogger(BatchLogDAOimpl.class);
	private utility objUtil = new utility();
	@Value("${spring.datasource.url}")
	private String mssqlUrl;
	@Value("${spring.datasource.username}")
	private String mssqlUsername;
	@Value("${spring.datasource.password}")
	private String mssqlPassword;
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	public Map<String, Object> getalldata() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call GET_ALL_SCHEDULER_LOGS (?)}");
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
			utility.exceptionLog("BatchLogDAOImpl", "getalldata", "", LOGGER, null, "", e.getMessage());
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getSearchBLData(String INTERFACE_NAME, String INTERFACE_TYPE, String STATUS,
			String START_DATE, String END_DATE) {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con
					.prepareCall("{call XX_ETQ_GET_SCHEDULER_LOGS_DATA_PRC(?,?,?,?,?,?)}");
			callableStatement.setString(1, INTERFACE_NAME);
			callableStatement.setString(2, INTERFACE_TYPE);
			callableStatement.setString(3, STATUS);
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
			utility.exceptionLog("BatchLogDAOImpl", "getSearchBLData", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Search batch log data");
		} catch (Exception e) {
			utility.exceptionLog("BatchLogDAOImpl", "getSearchBLData", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Search batch log data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getLogList() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("XX_ETQ_GET_LOG_STATUS_NAME_PRC");
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			utility.exceptionLog("BatchLogDAOImpl", "getLogList", "", LOGGER, null, "", e.getMessage());
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getInterfaceTypeList() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("XX_ETQ_GET_INTERFACE_TYPE_PRC");
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			utility.exceptionLog("BatchLogDAOImpl", "getInterfaceTypeList", "", LOGGER, null, "", e.getMessage());
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getInterfaceList() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("XX_ETQ_GET_INTERFACE_NAME_PRC");
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			utility.exceptionLog("BatchLogDAOImpl", "getInterfaceList", "", LOGGER, null, "", e.getMessage());
		} finally {
//			DbUtils.closeQuietly(rs);
//			DbUtils.closeQuietly(callableStatement);
		}
		return mapResponse;
	}
}
