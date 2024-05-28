package com.olympus.OEKG.service;


import java.sql.Connection;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.olympus.OEKG.ClientService.XxCustomerProcess;
import com.olympus.OEKG.model.SchedulerCust;

import lombok.Data;

@Data
public class multithread implements Runnable {

	
	
	public static List<Integer> Batch_Id_List = new ArrayList<Integer>();
	
	private  XxCustomerProcess xxCustomerProcess = new XxCustomerProcess();

	private Connection con = null;
	List<Map<String, String>> map = null;
	private String flag = "";
	private Integer BATCH_ID = 0;
	
	private SchedulerCust scheduler = SchedulerCust.getSchedulerObj();

	
	public multithread(List<Map<String, String>> map, Connection con, String flag) {
		super();
		this.con = con;
		this.map = map;
		this.flag = flag;
	}
	
	public multithread(Integer B_Id) {
		super();
		this.BATCH_ID = B_Id;
		
	}

	
	public multithread(List<Map<String, String>> map, Connection con,
			String flag, Integer bATCH_ID) {
		super();
		this.con = con;
		this.map = map;
		this.flag = flag;
		this.BATCH_ID = bATCH_ID;
	}
	public multithread() {
		super();
	}

	DateTimeFormatter dtf = DateTimeFormatter
			.ofPattern("yyyy/MM/dd HH:mm:ss.Ss");

	@Override
	public void run()  {
		try {
			xxCustomerProcess.customerProcess(scheduler.getBUSINESS_UNIT_SITE_ID(), scheduler.getSYSTEM_NAME(), BATCH_ID);
		} catch (SQLException e) {

		}
//		delayMethod();
//		String endDate_Batch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
//		objUtil.updateBatchLogCust(BATCH_ID, utility.PARENT_BATCH_ID, endDate_Batch, "COMPLETED", "Execution Completed Successfully");
	}
	
	public static void delayMethod(){
		long secondsToSleep = 2;
		try {
			TimeUnit.MINUTES.sleep(secondsToSleep);
		} catch (Exception e) {

		}
	}
	

	
}
