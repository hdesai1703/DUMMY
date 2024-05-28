package com.olympus.OEKG.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.olympus.OEKG.Job.QuartzJobAddRef;
import com.olympus.OEKG.Job.QuartzJobComp;
import com.olympus.OEKG.Job.QuartzJobCust;
import com.olympus.OEKG.Utility.DBUtil;
import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.model.BusinessDetails;
import com.olympus.OEKG.model.SchedulerAddRef;
import com.olympus.OEKG.model.SchedulerComp;
import com.olympus.OEKG.model.SchedulerCust;
import com.olympus.OEKG.service.QuartzConfiguration;

@Component
public class SchedulerDAOImpl implements SchedulerDAO {
	BusinessDetails bu_Details = BusinessDetails.buObj();
	final Logger LOGGER = LoggerFactory.getLogger(SchedulerDAOImpl.class);
	SchedulerComp schcomp = SchedulerComp.getSchedulerObj();
	SchedulerCust schcus = SchedulerCust.getSchedulerObj();
	SchedulerAddRef schaddref = SchedulerAddRef.getSchedulerObj();
	private utility objUtil = new utility();
	private static Map<String, Object> mapResponseBatchLog = new HashMap<String, Object>();
	private static ResultSet batchResultSet = null;

	@Override
	public Map<String, Object> getalldata() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call GET_ALL_SCHEDULER_PRC (?)}");
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
			utility.exceptionLog("SchedulerDAOImpl", "getalldata", "", LOGGER, null, "", e.getMessage());
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getSearchSchedulerData(String EVENT_TYPE, String INTERFACE_NAME, String START_DATE,
			String END_DATE, String IS_ACTIVE) {
		System.out.println("IS_ACTIVE "+IS_ACTIVE);
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call XX_ETQ_GET_SCHEDULER_DATA_PRC (?,?,?,?,?,?)}");
			callableStatement.setString(1, EVENT_TYPE);
			callableStatement.setString(2, INTERFACE_NAME);
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
			if (IS_ACTIVE.equals("Y")) {
				callableStatement.setString(5, "ACTIVE");
			} else if (IS_ACTIVE.equals("N")) {
				callableStatement.setString(5, "STOPPED");
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
			utility.exceptionLog("SchedulerDAOImpl", "getSearchSchedulerData", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getSearchSchedulerData Data");
		} catch (Exception e) {
			utility.exceptionLog("SchedulerDAOImpl", "getSearchSchedulerData", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getSearchSchedulerData Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getScheduleTypeList() {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("XX_ETQ_GET_SCHEDULER_TYPE_PRC");
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			utility.exceptionLog("SchedulerDAOImpl", "getScheduleTypeList", "", LOGGER, null, "", e.getMessage());
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
			utility.exceptionLog("SchedulerDAOImpl", "getInterfaceList", "", LOGGER, null, "", e.getMessage());
		} finally {
//			DbUtils.closeQuietly(rs);
//			DbUtils.closeQuietly(callableStatement);
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> updateSchedule(String flag, Integer SCHEDULE_ID, Integer BUSINESS_UNIT_SITE_ID,
			String INTERFACE_NAME, String INTERFACE_TYPE, String EVENT_TYPE, String START_TIME, String SCHEDULE_TYPE,
			String STATUS, String BATCH_EXECTE_ON, Integer MONTH_DAY, String WEEK_DAY, String START_DATE,
			String END_DATE, Integer BATCH, Integer NO_OF_RECORD, Integer CREATED_BY) throws ParseException {
		CallableStatement callableStatement = null;
		CallableStatement callableStatement1 = null;
		ResultSet rs = null;
		String cronExpression = objUtil.cornExpression(SCHEDULE_TYPE, BATCH_EXECTE_ON, MONTH_DAY, WEEK_DAY);
		String startDate = START_DATE;
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
		String START_DATE_STR = null;
		java.sql.Date sqlStartDate = null;
		// java.util.Date utilstartdate = null;
		// java.sql.Timestamp sqlstartdatestr = null;
		if (startDate == null || startDate == "") {

			LocalDateTime now = LocalDateTime.now();
			START_DATE_STR = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(now);
		} else {
			java.util.Date date = sdf1.parse(startDate);
			sqlStartDate = new java.sql.Date(date.getTime());
			START_DATE_STR = sqlStartDate + " " + START_TIME + ":00.0";
			// SimpleDateFormat ssdf1 = new
			// SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
			// utilstartdate = ssdf1.parse(START_DATE_STR);
			// sqlstartdatestr = new
			// java.sql.Timestamp(utilstartdate.getTime());
		}
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
			callableStatement = (CallableStatement) con.prepareCall(
					"{call XX_SCHEDULE_CONFIG_PRC(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			callableStatement.setString(1, flag);
			callableStatement.setInt(2, SCHEDULE_ID);
			callableStatement.setInt(3, BUSINESS_UNIT_SITE_ID);
			callableStatement.setString(4, INTERFACE_NAME);
			callableStatement.setString(5, INTERFACE_TYPE);
			callableStatement.setString(6, EVENT_TYPE);
			callableStatement.setString(7, START_TIME);
			callableStatement.setString(8, SCHEDULE_TYPE);
			callableStatement.setString(9, STATUS);
			callableStatement.setString(10, BATCH_EXECTE_ON);
			callableStatement.setInt(11, MONTH_DAY);
			callableStatement.setString(12, WEEK_DAY);
			callableStatement.setString(13, START_DATE_STR);
			callableStatement.setDate(14, sqlEndDate);
			callableStatement.setInt(15, BATCH);
			callableStatement.setInt(16, NO_OF_RECORD);
			callableStatement.setInt(17, CREATED_BY);
			callableStatement.setString(18, null);
			callableStatement.setString(19, null);
			callableStatement.setString(20, null);
			callableStatement.setString(21, null);
			callableStatement.setString(22, null);
			callableStatement.setString(23, null);
			callableStatement.setString(24, null);
			callableStatement.setString(25, null);
			callableStatement.setString(26, null);
			callableStatement.setString(27, null);
			callableStatement.setString(28, null);
			callableStatement.executeUpdate();
			try (Connection con1 = DBUtil.dataSource.getConnection();) {
				callableStatement1 = (CallableStatement) con
						.prepareCall("{call XX_ETQ_GET_MAX_SCHEDULER_ID_PRC(?,?,?,?,?,?,?,?,?,?,?,?)}");
				callableStatement1.setInt(1, BUSINESS_UNIT_SITE_ID);
				callableStatement1.setString(2, INTERFACE_NAME);
				callableStatement1.setString(3, INTERFACE_TYPE);
				callableStatement1.setString(4, EVENT_TYPE);
				callableStatement1.setString(5, START_TIME);
				callableStatement1.setString(6, SCHEDULE_TYPE);
				callableStatement1.setString(7, STATUS);
				callableStatement1.setString(8, BATCH_EXECTE_ON);
				callableStatement1.setInt(9, MONTH_DAY);
				callableStatement1.setString(10, WEEK_DAY);
				callableStatement1.setDate(11, sqlStartDate);
				callableStatement1.setDate(12, sqlEndDate);
				rs = callableStatement1.executeQuery();
				batchResultSet = rs;
				List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
				if (INTERFACE_NAME.equalsIgnoreCase("COMPLAINT")) {
					if (!data.isEmpty()) {
						for (Map<String, String> map : data) {
							schcomp.setSCHEDULER_ID(Integer.parseInt(map.get("SCHEDULER_ID")));
							schcomp.setBUSINESS_UNIT_SITE_ID(Integer.parseInt(map.get("BUSINESS_UNIT_SITE_ID")));
							schcomp.setINTERFACE_NAME(map.get("INTERFACE_NAME"));
							schcomp.setINTERFACE_TYPE(map.get("INTERFACE_TYPE"));
							schcomp.setSTART_DATE(map.get("START_DATE"));
							schcomp.setBATCH(Integer.parseInt(map.get("BATCH")));
							schcomp.setNO_OF_RECORD(Integer.parseInt(map.get("NO_OF_RECORD")));
							schcomp.setEVENT_TYPE(map.get("EVENT_TYPE"));
						}
					}
				}
				if (INTERFACE_NAME.equalsIgnoreCase("CUSTOMER")) {
					if (!data.isEmpty()) {
						for (Map<String, String> map : data) {
							schcus.setSCHEDULER_ID(Integer.parseInt(map.get("SCHEDULER_ID")));
							schcus.setBUSINESS_UNIT_SITE_ID(Integer.parseInt(map.get("BUSINESS_UNIT_SITE_ID")));
							schcus.setINTERFACE_NAME(map.get("INTERFACE_NAME"));
							schcus.setINTERFACE_TYPE(map.get("INTERFACE_TYPE"));
							schcus.setSTART_DATE(map.get("START_DATE"));
							schcus.setBATCH(Integer.parseInt(map.get("BATCH")));
							schcus.setNO_OF_RECORD(Integer.parseInt(map.get("NO_OF_RECORD")));
							schcus.setEVENT_TYPE(map.get("EVENT_TYPE"));
						}
					}
				}
				if (INTERFACE_NAME.equalsIgnoreCase("ADDITIONAL REFERENCE")) {
					if (!data.isEmpty()) {
						for (Map<String, String> map : data) {
							schaddref.setSCHEDULER_ID(Integer.parseInt(map.get("SCHEDULER_ID")));
							schaddref.setBUSINESS_UNIT_SITE_ID(Integer.parseInt(map.get("BUSINESS_UNIT_SITE_ID")));
							schaddref.setINTERFACE_NAME(map.get("INTERFACE_NAME"));
							schaddref.setINTERFACE_TYPE(map.get("INTERFACE_TYPE"));
							schaddref.setSTART_DATE(map.get("START_DATE"));
							schaddref.setBATCH(Integer.parseInt(map.get("BATCH")));
							schaddref.setNO_OF_RECORD(Integer.parseInt(map.get("NO_OF_RECORD")));
							schaddref.setEVENT_TYPE(map.get("EVENT_TYPE"));
						}
					}
				}
				mapResponseBatchLog.put("Data", data);
				mapResponseBatchLog.put("CODE", 1);
				mapResponseBatchLog.put("MESSAGE", "Data Recieved");
				DbUtils.closeQuietly(rs);
				DbUtils.closeQuietly(callableStatement1);
				DbUtils.closeQuietly(con1);
			}
			if (EVENT_TYPE.equalsIgnoreCase("schedule")) {
				if (INTERFACE_NAME.equalsIgnoreCase("COMPLAINT")) {
					String existingJobName = "QuartzJobComp";
					QuartzConfiguration quartzConfiguration = QuartzConfiguration.getQuartzConfigObj();

					quartzConfiguration.removeTrigger(existingJobName);
					quartzConfiguration.setStartDateStr(START_DATE_STR);
					quartzConfiguration.setInterfaceName(INTERFACE_NAME);
					quartzConfiguration.setSchedularTime(cronExpression);
					quartzConfiguration.addTriggerComp("QuartzJobComp", cronExpression);
				}
				if (INTERFACE_NAME.equalsIgnoreCase("CUSTOMER")) {
					String existingJobName = "QuartzJobCust";
					QuartzConfiguration quartzConfiguration = QuartzConfiguration.getQuartzConfigObj();

					quartzConfiguration.removeTrigger(existingJobName);
					quartzConfiguration.setStartDateStr(START_DATE_STR);
					quartzConfiguration.setInterfaceName(INTERFACE_NAME);
					quartzConfiguration.setSchedularTime(cronExpression);
					quartzConfiguration.addTriggerCust("QuartzJobCust", cronExpression);
				}
				if (INTERFACE_NAME.equalsIgnoreCase("ADDITIONAL REFERENCE")) {
					String existingJobName = "QuartzJobAddRef";
					QuartzConfiguration quartzConfiguration = QuartzConfiguration.getQuartzConfigObj();
					quartzConfiguration.removeTrigger(existingJobName);
					quartzConfiguration.setStartDateStr(START_DATE_STR);
					quartzConfiguration.setInterfaceName(INTERFACE_NAME);
					quartzConfiguration.setSchedularTime(cronExpression);
					quartzConfiguration.addTriggerAddRef("QuartzJobAddRef", cronExpression);
				}
			} else if (EVENT_TYPE.equalsIgnoreCase("Immediate")) {
				if (INTERFACE_NAME.equalsIgnoreCase("COMPLAINT")) {
//					QuartzJobComp qj = new QuartzJobComp();
//					qj.complaint();
					String existingJobName = "QuartzJobCompImmediate";
					QuartzConfiguration quartzConfiguration = QuartzConfiguration.getQuartzConfigObj();
					quartzConfiguration.removeTrigger(existingJobName);
					quartzConfiguration.compimmediate();

				}
				if (INTERFACE_NAME.equalsIgnoreCase("CUSTOMER")) {
//					QuartzJobCust qj = new QuartzJobCust();
//					qj.customer();
					String existingJobName = "QuartzJobCusImmediate";
					QuartzConfiguration quartzConfiguration = QuartzConfiguration.getQuartzConfigObj();
					quartzConfiguration.removeTrigger(existingJobName);
					quartzConfiguration.cusimmediate();
				}
				if (INTERFACE_NAME.equalsIgnoreCase("ADDITIONAL REFERENCE")) {
//					QuartzJobAddRef qj = new QuartzJobAddRef();
//					qj.addRef();
					String existingJobName = "QuartzJobAddrefImmediate";
					QuartzConfiguration quartzConfiguration = QuartzConfiguration.getQuartzConfigObj();
					quartzConfiguration.removeTrigger(existingJobName);
					quartzConfiguration.cusimmediate();
				}
			}
			if (flag.equalsIgnoreCase("I")) {
				LOGGER.info("The Scheduler for the  " + INTERFACE_NAME + "  was created by USER ID :" + CREATED_BY
						+ " on the event type of " + EVENT_TYPE);
			} else {
				LOGGER.info("The Scheduler for the  " + INTERFACE_NAME + "  was updated by USER ID :" + CREATED_BY
						+ " on the event type of " + EVENT_TYPE);
			}
//			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("SchedulerDAOImpl", "updateSchedule", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting updateSchedule Data");
		} catch (Exception e) {
			utility.exceptionLog("SchedulerDAOImpl", "updateSchedule", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting updateSchedule Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> removeSchedule(Integer SCHEDULE_ID, String INTERFACE_NAME, String STATUS,
			Integer CREATED_BY) {
		LocalDateTime now = LocalDateTime.now();
		String end_date = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(now);
		CallableStatement callableStatement = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con.prepareCall("{call XX_SCHEDULE_REMOVE_PRC(?,?,?,?)}");
			callableStatement.setInt(1, SCHEDULE_ID);
			callableStatement.setString(2, end_date);
			callableStatement.setString(3, STATUS);
			callableStatement.setInt(4, CREATED_BY);
			callableStatement.executeUpdate();
			String existingJobName = null;
			if (schcomp.getEVENT_TYPE().equalsIgnoreCase("Schedule")) {
				if (INTERFACE_NAME.equalsIgnoreCase("COMPLAINT")) {
					existingJobName = "QuartzJobComp";
				}
				if (INTERFACE_NAME.equalsIgnoreCase("CUSTOMER")) {
					existingJobName = "QuartzJobCust";
				}
				if (INTERFACE_NAME.equalsIgnoreCase("ADDITIONAL REFERENCE")) {
					existingJobName = "QuartzJobAddRef";
				}
				QuartzConfiguration quartzConfiguration = QuartzConfiguration.getQuartzConfigObj();
				quartzConfiguration.removeTrigger(existingJobName);
			}
			LOGGER.info("The Scheduler for the  " + INTERFACE_NAME + "  was removed by USER ID :" + CREATED_BY);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("SchedulerDAOImpl", "removeSchedule", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting Config update Data");
		} catch (Exception e) {
			utility.exceptionLog("SchedulerDAOImpl", "removeSchedule", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting removeSchedule Data");
		}
		return mapResponse;
	}

	@Override
	public Map<String, Object> getExsitSchedulerData(Integer BUSINESS_UNIT_SITE_ID, String INTERFACE_NAME,
			String EVENT_TYPE) {
		CallableStatement callableStatement = null;
		ResultSet rs = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		String output = null;
		try (Connection con = DBUtil.dataSource.getConnection();) {
			callableStatement = (CallableStatement) con
					.prepareCall("{call XX_ETQ_GET_COUNT_SHEDULER_DATA_PRC (?,?,?)}");
			callableStatement.setInt(1, BUSINESS_UNIT_SITE_ID);
			callableStatement.setString(2, INTERFACE_NAME);
			callableStatement.setString(3, EVENT_TYPE);
			rs = callableStatement.executeQuery();
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			if (!data.isEmpty()) {
				for (Map<String, String> map : data) {
					output = map.get("DATA_COUNT");
				}
			}
			mapResponse.put("Data", data);
			mapResponse.put("CODE", 1);
			if (output.equalsIgnoreCase("1")) {
				mapResponse.put("MESSAGE", "Scheduler Data Exist");
			} else {
				mapResponse.put("MESSAGE", "Scheduler Data Not Exist");
			}
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(callableStatement);
			DbUtils.closeQuietly(con);
		} catch (SQLException e) {
			utility.exceptionLog("SchedulerDAOImpl", "getExsitSchedulerData", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getExsitSchedulerData Data");
		} catch (Exception e) {
			utility.exceptionLog("SchedulerDAOImpl", "getExsitSchedulerData", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getExsitSchedulerData Data");
		}
		return mapResponse;
	}

	public static ResultSet getMapResponseBatchLog() {
		return batchResultSet;
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
			utility.exceptionLog("SchedulerDAOImpl", "getInterfaceTypeList", "", LOGGER, null, "", e.getMessage());
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
			utility.exceptionLog("SchedulerDAOImpl", "getMappingBUList", "", LOGGER, null, "", e.getMessage());
		}
		return mapResponse;
	}
}
