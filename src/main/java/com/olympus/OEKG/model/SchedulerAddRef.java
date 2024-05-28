package com.olympus.OEKG.model;

import lombok.Data;

@Data
public class SchedulerAddRef {
	private Integer SCHEDULER_ID;
	private Integer BUSINESS_UNIT_SITE_ID;
	private String INTERFACE_TYPE;
	private String INTERFACE_NAME;
	private String START_DATE;
	private Integer BATCH;
	private Integer NO_OF_RECORD;
	private String SYSTEM_NAME;
	private String EVENT_TYPE;
	
	private static SchedulerAddRef schedulerobj = null;
	
	private SchedulerAddRef() {
	}

	public static SchedulerAddRef getSchedulerObj() {
		if (schedulerobj == null) {
			schedulerobj = new SchedulerAddRef();
		}
		return schedulerobj;
	}
}
