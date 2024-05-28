package com.olympus.OEKG.service;


import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.olympus.OEKG.ComplaintService.XxComplaintProcess;
import com.olympus.OEKG.model.SchedulerComp;

import lombok.Data;

@Data
public class MultithreadOfComp implements Runnable {

	
	
	public static List<Integer> Batch_Id_List = new ArrayList<Integer>();
	
	private  XxComplaintProcess xxComplaintProcess = new XxComplaintProcess();
	private SchedulerComp schedulerComp = SchedulerComp.getSchedulerObj();
	
	private Connection con = null;
	List<Map<String, String>> map = null;
	private String flag = "";
	private Integer BATCH_ID = 0;
	
	public MultithreadOfComp(List<Map<String, String>> map, Connection con, String flag) {
		super();
		this.con = con;
		this.map = map;
		this.flag = flag;
	}
	
	public MultithreadOfComp(Integer B_Id) {
		super();
		this.BATCH_ID = B_Id;
		
	}

	
	public MultithreadOfComp(List<Map<String, String>> map, Connection con,
			String flag, Integer BATCH_ID) {
		super();
		this.con = con;
		this.map = map;
		this.flag = flag;
		this.BATCH_ID = BATCH_ID;
	}
	public MultithreadOfComp() {
		super();
	}

	DateTimeFormatter dtf = DateTimeFormatter
			.ofPattern("yyyy/MM/dd HH:mm:ss.Ss");

	@Override
	public void run()  {
		try {

			xxComplaintProcess.complaintProcess(schedulerComp.getBUSINESS_UNIT_SITE_ID(), schedulerComp.getSYSTEM_NAME(), schedulerComp.getINTERFACE_TYPE(), BATCH_ID);

		} catch (Exception e) {

		}
//		delayMethod();
//		String endDate_Batch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
//		objUtil.updateBatchLogCust(BATCH_ID, utility.PARENT_BATCH_ID_COMP, endDate_Batch, "COMPLETED", "Execution Completed Successfully");
	}
	
	public static void delayMethod(){
		long secondsToSleep = 2;
		try {
			TimeUnit.MINUTES.sleep(secondsToSleep);
		} catch (Exception e) {

		}
	}
	

	
}
