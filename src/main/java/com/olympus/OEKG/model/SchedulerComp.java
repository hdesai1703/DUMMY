package com.olympus.OEKG.model;


import lombok.Data;

@Data
public class SchedulerComp {
	
	private Integer SCHEDULER_ID;
	private Integer BUSINESS_UNIT_SITE_ID;
	private String INTERFACE_TYPE;
	private String INTERFACE_NAME;
	private String START_DATE;
	private Integer BATCH;
	private Integer NO_OF_RECORD;
	private String SYSTEM_NAME;
	private String EVENT_TYPE;
	
	private static SchedulerComp schedulerobj = null;
	
	private SchedulerComp() {
	}

	public static SchedulerComp getSchedulerObj() {
		if (schedulerobj == null) {
			schedulerobj = new SchedulerComp();
		}
		return schedulerobj;
	}
}
