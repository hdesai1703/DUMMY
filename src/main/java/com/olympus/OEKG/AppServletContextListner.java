package com.olympus.OEKG;

import java.text.ParseException;


import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.service.QuartzConfiguration;

@Component
public class AppServletContextListner{
	
	utility objUtility = new utility();
	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ServletInitializer.class);
	
	@EventListener(ApplicationReadyEvent.class)
	public static void scheduler() throws ParseException {
		LOGGER.info("================ || EMA START || ================");
		System.out.println("EMA Started");
		utility util = new utility();
		String schedularTime = util.getSchedularCornExpression();
//		String schedularTime = "0 09/1 13 * * ?";
//		String schedularTime = "0/10 * * ? * *";
		if (schedularTime != "") {			
			
			QuartzConfiguration quartzConfiguration = QuartzConfiguration.getQuartzConfigObj();
			quartzConfiguration.setSchedularTime(schedularTime);
			quartzConfiguration.setStartDateStr("2022-08-22 00:00:00.0");
			quartzConfiguration.initJob();			
			System.out.println("Scheduler Started");
			LOGGER.info("Schedular Start Succesfully. Schedular Time :" + schedularTime);
		}else{
			LOGGER.error("Issue in configuring Schedular. Please contact EMA admin to setup!");
		}
	}
	
}