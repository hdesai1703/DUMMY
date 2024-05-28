package com.olympus.OEKG.model;

import lombok.Data;

@Data
public class SchedulerCust {
	private Integer SCHEDULER_ID;
	private Integer BUSINESS_UNIT_SITE_ID;
	private String INTERFACE_TYPE;
	private String INTERFACE_NAME;
	private String START_DATE;
	private Integer BATCH;
	private Integer NO_OF_RECORD;
	private String SYSTEM_NAME;
	private String EVENT_TYPE;
	
	private static SchedulerCust schedulerobj = null;
	
	private SchedulerCust() {
	}

	public static SchedulerCust getSchedulerObj() {
		if (schedulerobj == null) {
			schedulerobj = new SchedulerCust();
		}
		return schedulerobj;
	}
}
