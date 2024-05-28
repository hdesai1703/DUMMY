package com.olympus.OEKG.Job;

import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import com.microsoft.sqlserver.jdbc.SQLServerException;

import org.apache.commons.dbutils.DbUtils;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.olympus.OEKG.Utility.DBUtil;
import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.model.SchedulerAddRef;
import com.olympus.OEKG.repository.SchedulerDAOImpl;
import com.olympus.OEKG.service.AdditionalRefBatchService;
import com.olympus.OEKG.service.CustomerBatchService;


public class QuartzJobAddRef implements Job {

	private utility objUtil = new utility();
	final Logger LOGGER =LoggerFactory.getLogger(QuartzJobAddRef.class);
	
	private SchedulerAddRef scheduler = SchedulerAddRef.getSchedulerObj();

	private AdditionalRefBatchService additionalRefbatchservice = new AdditionalRefBatchService();

	private SchedulerDAOImpl schedulerDAOImpl = new SchedulerDAOImpl();

	DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		boolean isJobRunning = false;
		try {
			isJobRunning = isJobRunning(context, "QuartzJobAddRef", "");
		} catch (SchedulerException e) {
			utility.exceptionLog("QuartzJobAddRef", "execute", "", LOGGER, null, "", " ErrorMessage: " + e.getMessage());

		}
		if (!isJobRunning) {
			try {
				addRef();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			LOGGER.info("Previous Scheduler Job Already in Running State...");
		}

	}

	public static boolean isJobRunning(JobExecutionContext ctx, String jobName,
			String groupName) throws SchedulerException {
		List<JobExecutionContext> currentJobs = ctx.getScheduler()
				.getCurrentlyExecutingJobs();

		for (JobExecutionContext jobCtx : currentJobs) {
			String thisJobName = jobCtx.getJobDetail().getKey().getName();

			if (jobName.equalsIgnoreCase(thisJobName)
					&& !jobCtx.getFireTime().equals(ctx.getFireTime())) {
				return true;
			}
		}
		return false;
	}

	// public void demo() throws InterruptedException {
	// MappingDAOImpl mappingDAOImpl = new MappingDAOImpl();
	// Map<String, Object> mapResponse = mappingDAOImpl.getalldata();
	// LocalDateTime now = LocalDateTime.now();
	// System.out.println("Mari2: " + dtf.format(now) + mapResponse.toString());
	//
	// long secondsToSleep = 120l;
	// TimeUnit.SECONDS.sleep(secondsToSleep);
	//
	// }

	public void addRef() throws Exception {
		
		List<Map<String, String>> data = objUtil.getBCDatabaseCount(
				scheduler.getBUSINESS_UNIT_SITE_ID(),
				scheduler.getINTERFACE_NAME());

		List<Map<String, String>> details = objUtil.getBCDatabaseDetails(
				scheduler.getBUSINESS_UNIT_SITE_ID(),
				scheduler.getINTERFACE_NAME());
		
		String DB_COUNT = null;
		
		if (!data.isEmpty()) {
			for (Map<String, String> map : data) {
				DB_COUNT = map.get("DATA_COUNT");
			}
		}
		if (!details.isEmpty()) {			
			for (Map<String, String> map : details) {
				String server_path = map.get("SQL_DRIVER");
				DBUtil.DRIVER_CLASS_AD = server_path;
				DBUtil.SERVER_PATH_AD = map.get("SERVER_PATH");
				DBUtil.SERVER_USER_AD = map.get("SERVER_USER");
				DBUtil.SERVER_PASSWORD_AD = map.get("SERVER_PASSWORD");
				scheduler.setSYSTEM_NAME(map.get("SYSTEM_NAME"));
			}
		}

		if (DB_COUNT.equalsIgnoreCase("1")) {

			try(Connection connection = DBUtil.getDataSourceClientAddRef().getConnection();){						
				additionalRefbatchservice.testBatch();
				if(scheduler.getEVENT_TYPE().equalsIgnoreCase("Immediate")){
					schedulerDAOImpl.removeSchedule(scheduler.getSCHEDULER_ID(), scheduler.getINTERFACE_NAME(), "COMPLETED",-1);
				}
				DbUtils.close(connection);
			} catch (SQLServerException e) {
				if(scheduler.getEVENT_TYPE().equalsIgnoreCase("Immediate")){
					schedulerDAOImpl.removeSchedule(scheduler.getSCHEDULER_ID(), scheduler.getINTERFACE_NAME(), "ERROR",-1);
				}
				utility.exceptionLog("QuartzJobAddRef", "addRef", "", LOGGER, null, "", e.getMessage());
				objUtil.addBatchLogAddRef("NCW_DB");
			}
		} else {
			objUtil.addBatchLogAddRef("NO_DB");
		}
	}

}
