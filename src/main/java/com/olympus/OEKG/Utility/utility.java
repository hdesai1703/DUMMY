package com.olympus.OEKG.Utility;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.dbutils.DbUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.olympus.OEKG.model.BusinessDetails;
import com.olympus.OEKG.model.SchedulerAddRef;
import com.olympus.OEKG.model.SchedulerComp;
import com.olympus.OEKG.model.SchedulerCust;

@Component
public class utility {
	final Logger LOGGER = LoggerFactory.getLogger(utility.class);
	BusinessDetails bu_Details = BusinessDetails.buObj();
	public static Integer BATCH_ID = null;
	public static Integer ATTACHMENT_SIZE_LIMIT = null;
	public static Integer BATCH_ID_ADD_REF = null;
	public static Integer BATCH_ID_COMP = null;
	public static Integer PARENT_BATCH_ID = null;
	public static Integer PARENT_BATCH_ID_ADD_REF = null;
	public static Integer PARENT_BATCH_ID_COMP = null;
	SchedulerComp schcomp = SchedulerComp.getSchedulerObj();
	SchedulerCust schcus = SchedulerCust.getSchedulerObj();
	SchedulerAddRef schaddref = SchedulerAddRef.getSchedulerObj();
	final String strFrom = "ema.notification@evidentscientific.com";
//	final String strPwd = "pxgnezkxdommbrio";
	final String host = "SMTP.gss.local";
	public static String update = "U";
	public static String insert = "I";

	public List<Map<String, String>> generateResultSetToMap(ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		List<String> columns = new ArrayList<String>(rsmd.getColumnCount());
		for (int i = 1; i <= rsmd.getColumnCount(); i++) {
			columns.add(rsmd.getColumnName(i));
		}
		List<Map<String, String>> data = new ArrayList<Map<String, String>>();
		while (rs.next()) {
			Map<String, String> row = new LinkedHashMap<String, String>(columns.size());
			for (String col : columns) {
				row.put(col, rs.getString(col));
			}
			data.add(row);
		}
		return data;
	}

	public static void exceptionLog(String className, String methodName, String requestParam, Logger logger, Object obj,
			String code, String message) {
		String objStr = "";
		// Logger wsLogger = Logger.getLogger("webserviceLogger");
		try {
			ObjectMapper objMapper = new ObjectMapper();
			if (obj != null) {
				objStr = "::" + objMapper.writeValueAsString(obj);
			}
			logger.error("???????????????	EXCEPTION	???????????????");
			logger.error(className + "::" + methodName + "()::EXCEPTION::" + objStr + "::" + "\nException ::CODE::"
					+ code + "::MESSAGE:"+ message);
			logger.error("????????????????????????????????????????????");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getSchedularCornExpression() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = con.prepareCall("XX_ETQ_SCHEDULER_DEFAULT_PRC");
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = generateResultSetToMap(rs);
			if (!data.isEmpty()) {
				for (Map<String, String> map : data) {
					return (map.get("CONFIG_VALUE_1"));
				}
			}
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("utility", "getSchedularCornExpression", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			return "";
		} catch (Exception e) {
			utility.exceptionLog("utility", "getSchedularCornExpression", "", LOGGER, null, "", e.getMessage());
			return "";
		} finally {
//			DbUtils.closeQuietly(rs);
//			DbUtils.closeQuietly(callableStatement);
		}
		return "";
	}

	public String cornExpression(String SCHEDULE_TYPE, String BATCH_EXECTE_ON, Integer MONTH_DAY, String WEEK_DAY) {
		String exp = "";
		final String type = SCHEDULE_TYPE;
		// String] split = START_TIME.split(":");
		// String hour = split[0];
		// String Start_min = split[1];
		String executevery = BATCH_EXECTE_ON;
		if ("00".equalsIgnoreCase(executevery)) {
			executevery = "0";
		}
		if ("daily".equalsIgnoreCase(type)) {
			exp = "0" + " " + "*" + "/" + executevery + " " + "*" + " " + "*" + " " + "*" + " " + "?";
		} else if ("monthly".equalsIgnoreCase(type)) {
			Integer date = MONTH_DAY;
			exp = "0" + " " + "*" + " " + "*" + " " + " " + date + " " + "*" + " " + "?";
		} else if ("weekly".equalsIgnoreCase(type)) {
			final String dayOfWeek = WEEK_DAY;
			exp = "0" + " " + "*" + " " + "*" + " " + " " + "*" + " " + "*" + " " + dayOfWeek;
		}
		return exp;
	}

	public List<Map<String, String>> getBCDatabaseCount(Integer BUSINESS_UNIT_SITE_ID, String INTERFACE_NAME) {
		System.out.println("Inside getBCDatabasecount : " + bu_Details.getBU_ID());
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		List<Map<String, String>> data = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = con.prepareCall("{call XX_ETQ_GET_BC_DB_COUNT_PRC(?,?)}");
			callableStatement.setInt(1, bu_Details.getBU_ID());
			callableStatement.setString(2, INTERFACE_NAME);
			rs = callableStatement.executeQuery();
			data = generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("utility", "getBCDatabaseCount", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			return data;
		} catch (Exception e) {
			utility.exceptionLog("utility", "getBCDatabaseCount", "", LOGGER, null, "", e.getMessage());
			return data;
		} finally {
//			DbUtils.closeQuietly(rs);
//			DbUtils.closeQuietly(callableStatement);
		}
		return data;
	}

	public List<Map<String, String>> getBCDatabaseDetails(Integer BUSINESS_UNIT_SITE_ID, String INTERFACE_NAME) {
		System.out.println("Inside getBCDatabaseDetails : " + bu_Details.getBU_ID());
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		List<Map<String, String>> data = null;
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = con.prepareCall("{call XX_ETQ_GET_BC_DB_DETAILS_PRC(?,?)}");
			callableStatement.setInt(1, bu_Details.getBU_ID());
			callableStatement.setString(2, INTERFACE_NAME);
			rs = callableStatement.executeQuery();
			data = generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("utility", "getBCDatabaseDetails", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			return data;
		} catch (Exception e) {
			utility.exceptionLog("utility", "getBCDatabaseDetails", "", LOGGER, null, "", e.getMessage());
			return data;
		} finally {
//			DbUtils.closeQuietly(rs);
//			DbUtils.closeQuietly(callableStatement);
		}
		return data;
	}

	public Integer addBatchLogComp(String flag) throws SQLException {
		Integer sch_id = schcomp.getSCHEDULER_ID();
		Integer bu_Id = schcomp.getBUSINESS_UNIT_SITE_ID();
		String inter_Name = schcomp.getINTERFACE_NAME();
		String inter_Type = schcomp.getINTERFACE_TYPE();
		int parent_batch_Id = 0;
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String endTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String endDate = null;
		CallableStatement callableStatement = null;
		String STATUS = null;
		String MESSAGE = null;
		if (flag.equalsIgnoreCase("YES_DB")) {
			STATUS = "COMPLETED";
			MESSAGE = "Database details is present";
		} else if (flag.equalsIgnoreCase("NO_DB")) {
			endDate = endTimeStamp;
			STATUS = "ERROR";
			MESSAGE = "Database details was not present";
		} else if (flag.equalsIgnoreCase("NCW_DB")) {
			endDate = endTimeStamp;
			STATUS = "ERROR";
			MESSAGE = "Not connected with the database";
		} else if (flag.equalsIgnoreCase("CW_DB")) {
			STATUS = "RUNNING";
			MESSAGE = "Working on Batches";
		} else if (flag.equalsIgnoreCase("ABL_CR")) {
			STATUS = "RUNNING";
			MESSAGE = "Execution In Progress";
			parent_batch_Id = PARENT_BATCH_ID_COMP;
		} else if (flag.equalsIgnoreCase("ABL_CP")) {
			STATUS = "COMPLETED";
			MESSAGE = "Batch Log Completed";
		} else if (flag.equalsIgnoreCase("PB")) {
			STATUS = "RUNNING";
			MESSAGE = " ";
		} else if (flag.equalsIgnoreCase("CBL_CR")) {
			STATUS = "PENDING";
			MESSAGE = " ";
			parent_batch_Id = PARENT_BATCH_ID_COMP;
		}
		Connection con = DBUtil.dataSource.getConnection();
		callableStatement = (CallableStatement) con
				.prepareCall("{call XX_SCHEDULE_LOG_CONFIG_ERROR_PRC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		callableStatement.setInt(1, bu_Id);
		callableStatement.setString(2, inter_Name);
		callableStatement.setString(3, inter_Type);
		callableStatement.setString(4, STATUS);
		callableStatement.setString(5, timeStamp);
		callableStatement.setString(6, endDate);
		callableStatement.setString(7, MESSAGE);
		callableStatement.setInt(8, sch_id);
		callableStatement.setInt(9, parent_batch_Id);
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
		callableStatement.registerOutParameter(20, java.sql.Types.VARCHAR);
		callableStatement.registerOutParameter(21, java.sql.Types.INTEGER);
		callableStatement.executeUpdate();
		if (flag.equalsIgnoreCase("PB")) {
			PARENT_BATCH_ID_COMP = callableStatement.getInt(21);
		}
		CallableStatement callableStatement1 = null;
		callableStatement1 = (CallableStatement) con.prepareCall("{call XX_ETQ_GET_BATCH_ID_PRC(?,?,?,?,?,?,?,?)}");
		callableStatement1.setInt(1, bu_Id);
		callableStatement1.setString(2, inter_Name);
		callableStatement1.setString(3, inter_Type);
		callableStatement1.setString(4, STATUS);
		callableStatement1.setString(5, timeStamp);
		callableStatement1.setTimestamp(6, null);
		callableStatement1.setInt(7, sch_id);
		callableStatement1.setInt(8, 123);
		callableStatement1.executeQuery();
		BATCH_ID_COMP = callableStatement.getInt(21);
		DbUtils.closeQuietly(callableStatement);

		DbUtils.closeQuietly(callableStatement1);

		DbUtils.closeQuietly(con);
		return BATCH_ID_COMP;
	}

	public Integer addBatchLogCust(String flag) throws SQLException {
		Integer sch_id = schcus.getSCHEDULER_ID();
		Integer bu_Id = schcus.getBUSINESS_UNIT_SITE_ID();
		String inter_Name = schcus.getINTERFACE_NAME();
		String inter_Type = schcus.getINTERFACE_TYPE();
		int parent_batch_Id = 0;
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String endTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String endDate = null;
		CallableStatement callableStatement = null;
		String STATUS = null;
		String MESSAGE = null;
		if (flag.equalsIgnoreCase("YES_DB")) {
			STATUS = "COMPLETED";
			MESSAGE = "Database details is present";
		} else if (flag.equalsIgnoreCase("NO_DB")) {
			endDate = endTimeStamp;
			STATUS = "ERROR";
			MESSAGE = "Database details was not present";
		} else if (flag.equalsIgnoreCase("NCW_DB")) {
			endDate = endTimeStamp;
			STATUS = "ERROR";
			MESSAGE = "Not connected with the database";
		} else if (flag.equalsIgnoreCase("CW_DB")) {
			STATUS = "RUNNING";
			MESSAGE = "Working on Batches";
		} else if (flag.equalsIgnoreCase("ABL_CR")) {
			STATUS = "RUNNING";
			MESSAGE = "Execution In Progress";
			parent_batch_Id = PARENT_BATCH_ID;
		} else if (flag.equalsIgnoreCase("ABL_CP")) {
			STATUS = "COMPLETED";
			MESSAGE = "Batch Log Completed";
		} else if (flag.equalsIgnoreCase("PB")) {
			STATUS = "RUNNING";
			MESSAGE = " ";
		} else if (flag.equalsIgnoreCase("CBL_CR")) {
			STATUS = "PENDING";
			MESSAGE = " ";
			parent_batch_Id = PARENT_BATCH_ID;
		}
		Connection con = DBUtil.dataSource.getConnection();
		callableStatement = (CallableStatement) con
				.prepareCall("{call XX_SCHEDULE_LOG_CONFIG_ERROR_PRC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		callableStatement.setInt(1, bu_Id);
		callableStatement.setString(2, inter_Name);
		callableStatement.setString(3, inter_Type);
		callableStatement.setString(4, STATUS);
		callableStatement.setString(5, timeStamp);
		callableStatement.setString(6, endDate);
		callableStatement.setString(7, MESSAGE);
		callableStatement.setInt(8, sch_id);
		callableStatement.setInt(9, parent_batch_Id);
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
		callableStatement.registerOutParameter(20, java.sql.Types.VARCHAR);
		callableStatement.registerOutParameter(21, java.sql.Types.INTEGER);
		callableStatement.executeUpdate();
		if (flag.equalsIgnoreCase("PB")) {
			PARENT_BATCH_ID = callableStatement.getInt(21);
		}
		CallableStatement callableStatement1 = null;
		callableStatement1 = (CallableStatement) con.prepareCall("{call XX_ETQ_GET_BATCH_ID_PRC(?,?,?,?,?,?,?,?)}");
		callableStatement1.setInt(1, bu_Id);
		callableStatement1.setString(2, inter_Name);
		callableStatement1.setString(3, inter_Type);
		callableStatement1.setString(4, STATUS);
		callableStatement1.setString(5, timeStamp);
		callableStatement1.setTimestamp(6, null);
		callableStatement1.setInt(7, sch_id);
		callableStatement1.setInt(8, 123);
		callableStatement1.executeQuery();
		BATCH_ID = callableStatement.getInt(21);
		DbUtils.closeQuietly(callableStatement);

		DbUtils.closeQuietly(callableStatement1);

		DbUtils.closeQuietly(con);
		return BATCH_ID;
	}

	public Integer addBatchLogAddRef(String flag) throws SQLException {
		Integer sch_id = schaddref.getSCHEDULER_ID();
		Integer bu_Id = schaddref.getBUSINESS_UNIT_SITE_ID();
		String inter_Name = schaddref.getINTERFACE_NAME();
		String inter_Type = schaddref.getINTERFACE_TYPE();
		int parent_batch_Id = 0;
		String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String endTimeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		String endDate = null;
		CallableStatement callableStatement = null;
		String STATUS = null;
		String MESSAGE = null;
		if (flag.equalsIgnoreCase("YES_DB")) {
			STATUS = "COMPLETED";
			MESSAGE = "Database details is present";
		} else if (flag.equalsIgnoreCase("NO_DB")) {
			endDate = endTimeStamp;
			STATUS = "ERROR";
			MESSAGE = "Database details was not present";
		} else if (flag.equalsIgnoreCase("NCW_DB")) {
			endDate = endTimeStamp;
			STATUS = "ERROR";
			MESSAGE = "Not connected with the database";
		} else if (flag.equalsIgnoreCase("CW_DB")) {
			STATUS = "RUNNING";
			MESSAGE = "Working on Batches";
		} else if (flag.equalsIgnoreCase("ABL_CR")) {
			STATUS = "RUNNING";
			MESSAGE = "Execution In Progress";
			parent_batch_Id = PARENT_BATCH_ID_ADD_REF;
		} else if (flag.equalsIgnoreCase("ABL_CP")) {
			STATUS = "COMPLETED";
			MESSAGE = "Batch Log Completed";
		} else if (flag.equalsIgnoreCase("PB")) {
			STATUS = "RUNNING";
			MESSAGE = " ";
		} else if (flag.equalsIgnoreCase("CBL_CR")) {
			STATUS = "PENDING";
			MESSAGE = " ";
			parent_batch_Id = PARENT_BATCH_ID_ADD_REF;
		}
		Connection con = DBUtil.dataSource.getConnection();
		callableStatement = (CallableStatement) con
				.prepareCall("{call XX_SCHEDULE_LOG_CONFIG_ERROR_PRC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
		callableStatement.setInt(1, bu_Id);
		callableStatement.setString(2, inter_Name);
		callableStatement.setString(3, inter_Type);
		callableStatement.setString(4, STATUS);
		callableStatement.setString(5, timeStamp);
		callableStatement.setString(6, endDate);
		callableStatement.setString(7, MESSAGE);
		callableStatement.setInt(8, sch_id);
		callableStatement.setInt(9, parent_batch_Id);
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
		callableStatement.registerOutParameter(20, java.sql.Types.VARCHAR);
		callableStatement.registerOutParameter(21, java.sql.Types.INTEGER);
		callableStatement.executeUpdate();
		if (flag.equalsIgnoreCase("PB")) {
			PARENT_BATCH_ID_ADD_REF = callableStatement.getInt(21);
		}
		CallableStatement callableStatement1 = null;
		callableStatement1 = (CallableStatement) con.prepareCall("{call XX_ETQ_GET_BATCH_ID_PRC(?,?,?,?,?,?,?,?)}");
		callableStatement1.setInt(1, bu_Id);
		callableStatement1.setString(2, inter_Name);
		callableStatement1.setString(3, inter_Type);
		callableStatement1.setString(4, STATUS);
		callableStatement1.setString(5, timeStamp);
		callableStatement1.setTimestamp(6, null);
		callableStatement1.setInt(7, sch_id);
		callableStatement1.setInt(8, 123);
		callableStatement1.executeQuery();
		BATCH_ID_ADD_REF = callableStatement.getInt(21);
		DbUtils.closeQuietly(callableStatement);

		DbUtils.closeQuietly(callableStatement1);

		DbUtils.closeQuietly(con);
		return BATCH_ID_ADD_REF;
	}

	public Map<String, Object> updateBatchLogCust(Integer BATCH_ID, Integer PARENT_BATCH_ID, String END_DATE,
			String STATUS, String MESSAGE) {
		CallableStatement callableStatement = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con
					.prepareCall("{call XX_UPDATE_SCHEDULER_LOG_CONFIG_ERROR_PRC(?,?,?,?,?)}");
			callableStatement.setInt(1, BATCH_ID);
			callableStatement.setString(2, END_DATE);
			callableStatement.setString(3, STATUS);
			callableStatement.setString(4, MESSAGE);
			callableStatement.setInt(5, PARENT_BATCH_ID);
			callableStatement.executeUpdate();
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("utility", "updateBatchLogCust", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting Config update Data");
		} catch (Exception e) {
			utility.exceptionLog("utility", "updateBatchLogCust", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting Config update Data");
		} finally {
//			DbUtils.closeQuietly(callableStatement);
		}
		return mapResponse;
	}

	public void dDebug(int DEBUG_LEVEL, String ETQ_UPLOADED_DATE, String ETQ_UPLOAD_MESSAGE,
			String INTERFACE_DOCUMENT_ID, String ETQ$NUMBER, String INTERFACE_NAME, String REQ_FOR,
			long COMPLAINT_INTERFACE_ID, String REFERENCE_NUM, String SYSTEM_SOURCE, String REFERENCE_NUM2,
			String SYSTEM_SOURCE2, int ETQ_UPLOAD_STATUS, int BUSINESS_UNIT_SITE_ID, int BATCH_ID) {
		try (Connection connection = DBUtil.dataSource.getConnection();) {
			Statement stmt = connection.createStatement();
			stmt.executeUpdate(
					"INSERT INTO XX_ETQ_GEN_DEBUG_TBL(DEBUG_LEVEL, ETQ_UPLOADED_DATE, ETQ_UPLOAD_MESSAGE, INTERFACE_DOCUMENT_ID, "
							+ "ETQ$NUMBER, INTERFACE_NAME, REQ_FOR, COMPLAINT_INTERFACE_ID, REFERENCE_NUM, SYSTEM_SOURCE, REFERENCE_NUM2, SYSTEM_SOURCE2, "
							+ "ETQ_UPLOAD_STATUS, BUSINESS_UNIT_ID, BATCH_ID) VALUES(" + DEBUG_LEVEL + ",'"
							+ ETQ_UPLOADED_DATE + "','" + ETQ_UPLOAD_MESSAGE + "','" + INTERFACE_DOCUMENT_ID + "','"
							+ ETQ$NUMBER + "','" + INTERFACE_NAME + "','" + REQ_FOR + "'," + COMPLAINT_INTERFACE_ID
							+ ",'" + REFERENCE_NUM + "','" + SYSTEM_SOURCE + "','" + REFERENCE_NUM2 + "','"
							+ SYSTEM_SOURCE2 + "'," + ETQ_UPLOAD_STATUS + "," + BUSINESS_UNIT_SITE_ID + "," + BATCH_ID
							+ ")");
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(connection);
		} catch (Exception e) {
			utility.exceptionLog("utility", "dDebug", "", LOGGER, null, "", e.getMessage());
		}
	}

	public void sendMail(int BATCH_ID) throws MessagingException {
		String body = "<p>Dear Administrator,<br><br>Batch ID: " + BATCH_ID
				+ " has resulted in an Upload Error.<br>Please refer to EtQ Monitoring Upload Message for details.<br><br>Thanks,<br>EMA Admin.</p>";
		JavaMailSender javamailSender = getJavaMailSender();
		MimeMessage message = javamailSender.createMimeMessage();
		MimeMessageHelper helper;
		helper = new MimeMessageHelper(message, true);// true indicates multipart message
		helper.setFrom(strFrom);// <--- THIS IS IMPORTANT
		helper.setSubject("EtQ Monitoring Notification: Dev Instance Batch ID " + BATCH_ID);
		helper.setTo(new String[]{"marikannan.m@evidentscientific.com","marieswaran.n@evidentscientific.com"});
//		helper.setTo("marieswaran.n@evidentscientific.com");
		helper.setText(body, true);// true indicates body is html
		javamailSender.send(message);
	}

	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(host);
		mailSender.setPort(25);
		mailSender.setUsername(strFrom);
//		mailSender.setPassword(strPwd);
		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "false");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
		return mailSender;
	}

	public void attachmentFileSize() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call XX_ETQ_ATTACHMENT_LIMIT_PRC()}");
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = generateResultSetToMap(rs);
			if (!data.isEmpty()) {
				for (Map<String, String> map : data) {
					ATTACHMENT_SIZE_LIMIT = Integer.parseInt(map.get("CONFIG_VALUE_1"));
				}
			}
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			utility.exceptionLog("utility", "attachmentFileSize", "", LOGGER, null, "", e.getMessage());
		} finally {
//			DbUtils.closeQuietly(callableStatement);
		}
	}
}
