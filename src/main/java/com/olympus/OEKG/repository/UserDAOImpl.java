package com.olympus.OEKG.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import com.olympus.OEKG.Utility.DBUtil;
import com.olympus.OEKG.Utility.utility;

@Repository
public class UserDAOImpl implements UserDAO {
	final Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);
	private utility objUtil = new utility();
	@Value("${spring.datasource.url}")
	private String mssqlUrl;
	@Value("${spring.datasource.username}")
	private String mssqlUsername;
	@Value("${spring.datasource.password}")
	private String mssqlPassword;

	@Override
	public Map<String, Object> getalldata() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("GET_ALL_USER");
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			utility.exceptionLog("UserDAOImpl", "getalldata", "", LOGGER, null, "", e.getMessage());
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getSearchUserData(String USER_NAME, String START_DATE, String END_DATE,
			String IS_ACTIVE) {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call XX_ETQ_GET_USER_PRC (?,?,?,?,?)}");
			callableStatement.setString(1, null);
			callableStatement.setString(2, USER_NAME);
			if (START_DATE.equals("")) {
				callableStatement.setString(3, null);
			} else {
				callableStatement.setString(3, START_DATE);
			}
			if (END_DATE.equals("")) {
				callableStatement.setString(4, null);
			} else {
				callableStatement.setString(4, END_DATE);
			}
			callableStatement.setString(5, IS_ACTIVE);
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("UserDAOImpl", "getSearchUserData", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting Config Search Data");
		} catch (Exception e) {
			utility.exceptionLog("UserDAOImpl", "getSearchUserData", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting Config Search Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> updateUser(String flag, Integer USER_ID, String USER_NAME, String USER_TYPE,
			String PASSWORD, String EMAIL_ADDRESS, String IS_ACTIVE, String START_DATE, String END_DATE,
			Integer CREATED_BY) throws ParseException {

		System.out.println(flag + USER_ID + USER_NAME + USER_TYPE + PASSWORD + EMAIL_ADDRESS + IS_ACTIVE + START_DATE
				+ END_DATE + CREATED_BY);
		CallableStatement callableStatement = null;
		String startDate = START_DATE;
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date = sdf1.parse(startDate);
		java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
		SimpleDateFormat edf1 = new SimpleDateFormat("dd-MM-yyyy");
		java.util.Date date1 = null;
		java.sql.Date sqlEndDate = null;
		String endDate = END_DATE;
		if (endDate == null) {
			sqlEndDate = null;
		} else {
			date1 = edf1.parse(endDate);
			sqlEndDate = new java.sql.Date(date1.getTime());
		}
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try {
			Connection con = DriverManager.getConnection(mssqlUrl, mssqlUsername, mssqlPassword);
//			con.setAutoCommit(false);
			callableStatement = (CallableStatement) con
					.prepareCall("{call XX_USER_PRC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			callableStatement.setString(1, flag);
			callableStatement.setInt(2, USER_ID);
			callableStatement.setString(3, USER_NAME);
			callableStatement.setString(4, EMAIL_ADDRESS);
			callableStatement.setString(5, IS_ACTIVE);
			callableStatement.setDate(6, sqlStartDate);
			callableStatement.setDate(7, sqlEndDate);
			callableStatement.setInt(8, CREATED_BY);
			callableStatement.setString(9, null);
			callableStatement.setString(10, null);
			callableStatement.setString(11, null);
			callableStatement.setString(12, null);
			callableStatement.setString(13, null);
			callableStatement.setString(14, null);
			callableStatement.setString(15, null);
			callableStatement.setString(16, null);
			callableStatement.setString(17, null);
			callableStatement.setString(18, null);
			callableStatement.setString(19, USER_TYPE);
		
			if (flag.equalsIgnoreCase("I")) {
				BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				String Encodedpassword = encoder.encode(PASSWORD);
				System.out.println("encoded password:" + Encodedpassword);
//				callableStatement.setString(20, new String(Base64.encodeBase64(PASSWORD.getBytes())));
				callableStatement.setString(20, Encodedpassword);
			}else {
				callableStatement.setString(20, null);
			}
			
			callableStatement.setString(21, null);
			callableStatement.executeUpdate();
//			DbUtils.closeQuietly(callableStatement);
//			con.commit();
			if (flag.equalsIgnoreCase("I")) {
				LOGGER.info("The username " + USER_NAME + "  was created by USER ID :" + CREATED_BY);
			} else {
				LOGGER.info("The username  " + USER_NAME + "  was updated by USER ID :" + CREATED_BY);
			}
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			e.printStackTrace();
			utility.exceptionLog("UserDAOImpl", "updateUser", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting updateUser Data");
		} catch (Exception e) {
			e.printStackTrace();
			utility.exceptionLog("UserDAOImpl", "updateUser", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting updateUser Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> changepassword(String EMAIL_ADDRESS, String PASSWORD, String NEW_PASSWORD) {
		CallableStatement callableStatement = null;
		CallableStatement callableStatement1 = null;
		ResultSet rs = null;
		List<Map<String, String>> data = null;
		String hash = null;
		String newencodedpassword = null;

		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DriverManager.getConnection(mssqlUrl, mssqlUsername, mssqlPassword);) {
			callableStatement = (CallableStatement) con.prepareCall("{call XX_ETQ_GET_USER_DETAILS_PRC (?)}");
			callableStatement.setString(1, EMAIL_ADDRESS);
			rs = callableStatement.executeQuery();
			data = objUtil.generateResultSetToMap(rs);
			BCryptPasswordEncoder b = new BCryptPasswordEncoder();
			for (Map<String, String> map : data) {
				hash = map.get("PASSWORD");
			}
			if (b.matches(PASSWORD, hash)) {
				newencodedpassword = b.encode(NEW_PASSWORD);
			} else {
				mapResponse.put("MSG", "currentpassword was wrong");
				mapResponse.put("CODE", 1);
				mapResponse.put("MESSAGE", "Data Recieved");
				DbUtils.closeQuietly(rs);
				DbUtils.closeQuietly(callableStatement);
				DbUtils.closeQuietly(con);
				return mapResponse;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			utility.exceptionLog("UserDAOImpl", "changepassword", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e1.getErrorCode()) + " ErrorMessage: " + e1.getMessage());
			e1.printStackTrace();
		}
		try (Connection con1 = DriverManager.getConnection(mssqlUrl, mssqlUsername, mssqlPassword);) {
			callableStatement1 = (CallableStatement) con1.prepareCall("{call XX_ETQ_UPDATE_USER_DETAILS_PRC (?,?)}");
			callableStatement1.setString(1, EMAIL_ADDRESS);
			callableStatement1.setString(2, newencodedpassword);
			callableStatement1.executeUpdate();
			mapResponse.put("MSG", "Password was changed successfully");
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(callableStatement1);
			DbUtils.closeQuietly(con1);
		} catch (SQLException e) {
			utility.exceptionLog("UserDAOImpl", "changepassword", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting changepassword Data");
		} catch (Exception e) {
			utility.exceptionLog("UserDAOImpl", "changepassword", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting changepassword Data");
		}
		return mapResponse;
	}
}
