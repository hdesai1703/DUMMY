package com.olympus.OEKG.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.olympus.OEKG.Job.QuartzJobAddRef;
import com.olympus.OEKG.Job.QuartzJobComp;
import com.olympus.OEKG.Job.QuartzJobCust;
import com.olympus.OEKG.Utility.utility;

/**
 * @author Marikannan & Marieswaran
 * 
 *         This class is used for scheduling JOB. Version 1.0
 *
 */
@Component
public class QuartzConfiguration {
	
	private String schedularTime;
	private String startDateStr;
	private String interfaceName;
	private Scheduler scheduler = null;
	private static QuartzConfiguration objQuartzConfiguration = null;
    final Logger LOGGER =LoggerFactory.getLogger(QuartzConfiguration.class);


	private QuartzConfiguration() {
	}

	public static QuartzConfiguration getQuartzConfigObj() {
		if (objQuartzConfiguration == null) {
			objQuartzConfiguration = new QuartzConfiguration();
		}
		return objQuartzConfiguration;
	}

	/**
	 * This function is used to initialize initial JOB when application is UP.
	 * @throws ParseException 
	 */
	public void initJob() throws ParseException {
		
		JobDetail jobDetailComp = createrJobComp(QuartzJobComp.class, "QuartzJobComp");
		JobDetail jobDetailCust = createrJobCust(QuartzJobCust.class, "QuartzJobCust");
		JobDetail jobDetailAddRef = createrJobAddRef(QuartzJobAddRef.class, "QuartzJobAddRef");
		CronTrigger triggerComp = getTrigger("QuartzJobComp", schedularTime);
		CronTrigger triggerCust = getTrigger("QuartzJobCust", schedularTime);
		CronTrigger triggerAddRef = getTrigger("QuartzJobAddRef", schedularTime);
		try {
			scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(jobDetailComp, triggerComp);
			scheduler.scheduleJob(jobDetailCust, triggerCust);
			scheduler.scheduleJob(jobDetailAddRef, triggerAddRef);
		} catch (Exception e) {
			utility.exceptionLog("QuartzConfiguration", "initJob", "", LOGGER,
					null, "", e.getMessage());
		}
	}

	/**
	 * @param classType
	 * @param jobName
	 * @return This Function is used to Create a scheduled JOB.
	 */
	@SuppressWarnings({ "rawtypes" })
	private JobDetail createrJobComp(Class classType, String jobName) {
		
			return JobBuilder.newJob(QuartzJobComp.class)
					.withIdentity(new JobKey(jobName))
					.usingJobData("app_name", interfaceName)
					.build();
		
		
			
		
		
	}
	/**
	 * @param classType
	 * @param jobName
	 * @return This Function is used to Create a scheduled JOB.
	 */
	@SuppressWarnings({ "rawtypes" })
	private JobDetail createrJobCust(Class classType, String jobName) {
		
		return JobBuilder.newJob(QuartzJobCust.class)
				.withIdentity(new JobKey(jobName))
				.usingJobData("app_name", interfaceName)
				.build();
	
	}
	/**
	 * @param classType
	 * @param jobName
	 * @return This Function is used to Create a scheduled JOB.
	 */
	@SuppressWarnings({ "rawtypes" })
	private JobDetail createrJobAddRef(Class classType, String jobName) {
		
		return JobBuilder.newJob(QuartzJobAddRef.class)
				.withIdentity(new JobKey(jobName))
				.usingJobData("app_name", interfaceName)
				.build();
	}

	/**
	 * @param jobKeyName
	 * @param cronExpression
	 * @return This Function is used to get Trigger.
	 * @throws ParseException 
	 */
	private CronTrigger getTrigger(String jobKeyName, String cronExpression) throws ParseException {
		Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(startDateStr);
		try {
			CronTrigger trigger = TriggerBuilder
					.newTrigger()
					.withIdentity(jobKeyName)
					.startAt(startDate)
					.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
					.build();
			return trigger;
		} catch (Exception e) {
			utility.exceptionLog("QuartzConfiguration", "getTrigger", "",
					LOGGER, null, "", e.getMessage());
		}
		return null;
	}

	/**
	 * @param jobKeyName
	 * @return This Function is used to remove Trigger.
	 */
	public Boolean removeTrigger(String jobKeyName) {
		try {
			return scheduler.deleteJob(new JobKey(jobKeyName));
		} catch (Exception e) {
			utility.exceptionLog("QuartzConfiguration", "removeTrigger", "",
					LOGGER, null, "", e.getMessage());
		}
		return null;
	}

	/**
	 * @param jobkeyName
	 * @param cronExpression
	 */
	public void addTriggerComp(String jobkeyName, String cronExpression) {
		
			try {
				scheduler.scheduleJob(createrJobComp(QuartzJobComp.class, "QuartzJobComp"),
						getTrigger(jobkeyName, cronExpression));
			} catch (Exception e) {
				utility.exceptionLog("QuartzConfiguration", "addTrigger", "",
						LOGGER, null, "", e.getMessage());
			}
		
		 
			
		}
	/**
	 * @param jobkeyName
	 * @param cronExpression
	 */
	public void addTriggerCust(String jobkeyName, String cronExpression) {
		
			try {
				scheduler.scheduleJob(createrJobCust(QuartzJobCust.class, "QuartzJobCust"),
						getTrigger(jobkeyName, cronExpression));
			} catch (Exception e) {
				utility.exceptionLog("QuartzConfiguration", "addTrigger", "",
						LOGGER, null, "", e.getMessage());
			}
		
			
		}
	/**
	 * @param jobkeyName
	 * @param cronExpression
	 */
	public void addTriggerAddRef(String jobkeyName, String cronExpression) {
				
		try {
			scheduler.scheduleJob(createrJobAddRef(QuartzJobAddRef.class, "QuartzJobAddRef"),
					getTrigger(jobkeyName, cronExpression));
		} catch (Exception e) {
			utility.exceptionLog("QuartzConfiguration", "addTrigger", "",
					LOGGER, null, "", e.getMessage());
		}
	}

	/**
	 * @return
	 */
	public String getSchedularTime() {
		return schedularTime;
	}

	/**
	 * @param schedularTime
	 *            Scheduler time must be in Corn Expression
	 */
	public void setSchedularTime(String schedularTime) {
		this.schedularTime = schedularTime;
	}

	public String getStartDateStr() {
		return startDateStr;
	}

	public void setStartDateStr(String startDateStr) {
		this.startDateStr = startDateStr;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	/**
	 * This function is used to destroy scheduler.
	 */
	public void destroyJob() {
		try {

			if (scheduler != null && scheduler.isStarted())
				scheduler.shutdown(true);
		} catch (Exception e) {
			utility.exceptionLog("QuartzConfiguration", "destroy", "", LOGGER,
					null, "", e.getMessage());
		}
	}

	/**
	 * This function is used to Pause scheduler.
	 */
	public void pauseScheduler() {
		try {
			scheduler.standby();
			LOGGER.info("Scheduler in standby mode");
		} catch (Exception e) {
			utility.exceptionLog("QuartzConfiguration", "pauseScheduler", "",
					LOGGER, null, "", e.getMessage());
		}
	}

	/**
	 * This function is used to Resume scheduler.
	 */
	public void resumeScheduler() {
		try {
			scheduler.start();
			LOGGER.info("Scheduler in start mode");
		} catch (Exception e) {
			utility.exceptionLog("QuartzConfiguration", "resumeScheduler", "",
					LOGGER, null, "", e.getMessage());
		}
	}
	
	public void compimmediate() {
		JobKey jobKey = JobKey.jobKey("QuartzJobCompImmediate", "myJobGroup");
		JobDetail job = JobBuilder.newJob(QuartzJobComp.class).withIdentity(jobKey).storeDurably().build();

		//Register this job to the scheduler
		try {
			scheduler.addJob(job, true);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Immediately fire the Job MyJob.class
		try {
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cusimmediate() {
		JobKey jobKey = JobKey.jobKey("QuartzJobCusImmediate", "myJobGroup");
		JobDetail job = JobBuilder.newJob(QuartzJobCust.class).withIdentity(jobKey).storeDurably().build();

		//Register this job to the scheduler
		try {
			scheduler.addJob(job, true);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Immediately fire the Job MyJob.class
		try {
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void addRefmmediate() {
		JobKey jobKey = JobKey.jobKey("QuartzJobAddrefImmediate", "myJobGroup");
		JobDetail job = JobBuilder.newJob(QuartzJobAddRef.class).withIdentity(jobKey).storeDurably().build();

		//Register this job to the scheduler
		try {
			scheduler.addJob(job, true);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//Immediately fire the Job MyJob.class
		try {
			scheduler.triggerJob(jobKey);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

}