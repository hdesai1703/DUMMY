package com.olympus.OEKG.service;


import java.sql.Connection;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.olympus.OEKG.AddRefService.XxAdditionalReferenceProcess;
import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.model.SchedulerAddRef;

import lombok.Data;

@Data
public class MultithreadOfAddRef implements Runnable {
	
    final Logger LOGGER =LoggerFactory.getLogger(MultithreadOfAddRef.class);


	
	
	public static List<Integer> Batch_Id_List = new ArrayList<Integer>();
	
	private  XxAdditionalReferenceProcess xxAdditionalReferenceProcess = new XxAdditionalReferenceProcess();

	private Connection con = null;
	List<Map<String, String>> map = null;
	private String flag = "";
	private Integer BATCH_ID = 0;
	
	private SchedulerAddRef scheduler = SchedulerAddRef.getSchedulerObj();

	
	public MultithreadOfAddRef(List<Map<String, String>> map, Connection con, String flag) {
		super();
		this.con = con;
		this.map = map;
		this.flag = flag;
	}
	
	public MultithreadOfAddRef(Integer B_Id) {
		super();
		this.BATCH_ID = B_Id;
		
	}

	
	public MultithreadOfAddRef(List<Map<String, String>> map, Connection con,
			String flag, Integer BATCH_ID) {
		super();
		this.con = con;
		this.map = map;
		this.flag = flag;
		this.BATCH_ID = BATCH_ID;
	}
	public MultithreadOfAddRef() {
		super();
	}

	DateTimeFormatter dtf = DateTimeFormatter
			.ofPattern("yyyy/MM/dd HH:mm:ss.Ss");

	@Override
	public void run()  {
		try {

			xxAdditionalReferenceProcess.additionalReferenceProcess(scheduler.getBUSINESS_UNIT_SITE_ID(), scheduler.getSYSTEM_NAME(), BATCH_ID);

		} catch (Exception e) {
			utility.exceptionLog("MultithreadOfAddRef", "addUpdateBusinessCenterData", "", LOGGER, null, "", e.getMessage());

		}
//		delayMethod();
//		String endDate_Batch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
//		objUtil.updateBatchLogCust(BATCH_ID, utility.PARENT_BATCH_ID_ADD_REF, endDate_Batch, "COMPLETED", "Execution Completed Successfully");
	}
	
	public static void delayMethod(){
		long secondsToSleep = 2;
		try {
			TimeUnit.MINUTES.sleep(secondsToSleep);
		} catch (Exception e) {

		}
	}
	

	
}
