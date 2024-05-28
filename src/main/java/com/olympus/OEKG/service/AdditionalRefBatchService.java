package com.olympus.OEKG.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.olympus.OEKG.Utility.DBUtil;
import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.model.SchedulerAddRef;

@Service
public class AdditionalRefBatchService {
	
    final Logger LOGGER =LoggerFactory.getLogger(AdditionalRefBatchService.class);


	private static utility objUtil = new utility();

	public static Integer PARENT_ID_ADD_REF = 0;
	// public static Integer CHILD_BATCH_ID_ADD_REF = 0;
	// public static List<Integer> BATCH_LIST_ADD_REF = new
	// ArrayList<Integer>();

	private SchedulerAddRef scheduler = SchedulerAddRef.getSchedulerObj();

	// public static List<List<Map<String, String>>> CHILD_BATCH_ADD_REF = null;

	public static List<Integer> getBatches(int pendingCount, int noOfBatch, int recordsPerbatch)
			throws SQLException, InterruptedException {

		List<Integer> actualChildBatchCount = new ArrayList<Integer>();
		int actual_batch = noOfBatch;
		int child_records_count = 0;
		int child_batch = 0;

		if (pendingCount > (noOfBatch * recordsPerbatch)) {
			child_records_count = pendingCount - (noOfBatch * recordsPerbatch); // 2
																				// records
			if (child_records_count % recordsPerbatch == 0) {
				child_batch = child_records_count / recordsPerbatch;// 1 batch
			} else {
				child_batch = (child_records_count / recordsPerbatch) + 1;
			}

		}

		if ((pendingCount <= (noOfBatch * recordsPerbatch))) {
			if (pendingCount % recordsPerbatch == 0) {
				actual_batch = pendingCount / recordsPerbatch;
			} else {
				actual_batch = (pendingCount / recordsPerbatch) + 1;
			}
		}

		actualChildBatchCount.add(actual_batch);
		actualChildBatchCount.add(child_batch);
		return actualChildBatchCount;
	}

	public String testBatch() throws SQLException, InterruptedException {

		PARENT_ID_ADD_REF = objUtil.addBatchLogAddRef("PB");
		ResultSet rs = null;
		Integer db = 0;
		List<Map<String, String>> pendingData = null;
		ResultSet resultSet = null;

		try (Connection connection = DBUtil.dataSourceClientAddRef.getConnection();) {
			Statement stmt3 = connection.createStatement();
			// U_ID List
			Statement stmt2 = connection.createStatement();
			resultSet = stmt2.executeQuery(
					"SELECT U_ID FROM  XX_OT_CUST_REFERENCE_NUM WHERE (ETQ_UPLOAD_STATUS=1 OR ETQ_UPLOAD_STATUS IS NULL OR ETQ_UPLOAD_STATUS=0) AND (ETQ_BATCH_ID IS NULL OR ETQ_BATCH_ID=0)");
			pendingData = objUtil.generateResultSetToMap(resultSet);
			List<Integer> Uid_list = new ArrayList<>();
			List<Integer> indBatchList = new ArrayList<>();
			List<Integer> B_Id_List = new ArrayList<>();

			List<Integer> indChildBatchList = new ArrayList<>();

			for (Map<String, String> map : pendingData) {
				Uid_list.add(Integer.parseInt(map.get("U_ID")));
			}
			// Count
			Statement stmt = connection.createStatement();
			rs = stmt.executeQuery(
					"SELECT COUNT(*) as DATA_COUNT FROM XX_OT_CUST_REFERENCE_NUM WHERE (ETQ_UPLOAD_STATUS=1 OR ETQ_UPLOAD_STATUS IS NULL OR ETQ_UPLOAD_STATUS=0) AND (ETQ_BATCH_ID IS NULL OR ETQ_BATCH_ID=0)");
			List<Map<String, String>> data = objUtil.generateResultSetToMap(rs);
			for (Map<String, String> map : data) {
				db = Integer.parseInt(map.get("DATA_COUNT"));
			}
			List<Integer> actualChildBatchCount = getBatches(db, scheduler.getBATCH(), scheduler.getNO_OF_RECORD());

			for (int i = 0; i < actualChildBatchCount.get(0); i++) {
				int B_Id = 0;
				B_Id = objUtil.addBatchLogAddRef("ABL_CR");
				B_Id_List.add(B_Id);
				for (int j = 0; j < scheduler.getNO_OF_RECORD(); j++) {
					if (Uid_list.size() >= 1) {
						indBatchList.add(Uid_list.get(0));
						Uid_list.remove(Integer.valueOf(Uid_list.get(0)));
					}
				}
				for (Integer uId : indBatchList) {
					stmt3.executeUpdate(
							"UPDATE XX_OT_CUST_REFERENCE_NUM SET ETQ_BATCH_ID = " + B_Id + " WHERE U_ID = " + uId);
				}
				indBatchList.clear();

			}
			if (actualChildBatchCount.get(1) != 0) {
				for (int i = 0; i < actualChildBatchCount.get(1); i++) {
					int B_Id = 0;
					B_Id = objUtil.addBatchLogAddRef("CBL_CR");
					B_Id_List.add(B_Id);
					for (int j = 0; j < scheduler.getNO_OF_RECORD(); j++) {
						if (Uid_list.size() >= 1) {
							indChildBatchList.add(Uid_list.get(0));
							Uid_list.remove(Integer.valueOf(Uid_list.get(0)));
						}
					}
					for (Integer uId : indChildBatchList) {
						stmt3.executeUpdate(
								"UPDATE XX_OT_CUST_REFERENCE_NUM SET ETQ_BATCH_ID = " + B_Id + " WHERE U_ID = " + uId);
					}
					indChildBatchList.clear();
				}
			}

			if (!data.isEmpty()) {
				ExecutorService executor = Executors.newFixedThreadPool(scheduler.getBATCH());
				executorThreadprocessForEMAData(B_Id_List, executor);
				executor.shutdown();
				while (!executor.isTerminated()) {
				}
				String endDate_parent = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.format(Calendar.getInstance().getTime());
				objUtil.updateBatchLogCust(PARENT_ID_ADD_REF, 0, endDate_parent, "COMPLETED",
						"Execution Completed Successfully");
			}
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(stmt2);
			DbUtils.closeQuietly(stmt3);
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(resultSet);

			DbUtils.close(connection);

		}
		catch (Exception e) {
			utility.exceptionLog("AdditionalRefBatchService", "testBatch", "", LOGGER, null, "", e.getMessage());
		}

		return null;
	}

	private void executorThreadprocessForEMAData(List<Integer> B_Id_List, ExecutorService executor)
			throws InterruptedException {
		try {

			for (Iterator<Integer> B_Id_Iterator = B_Id_List.iterator(); B_Id_Iterator.hasNext();) {
				Integer B_Id = B_Id_Iterator.next();

				executor.execute(new MultithreadOfAddRef(B_Id));
			}

		} catch (Exception e) {
			utility.exceptionLog("AdditionalRefBatchService", "executorThreadprocessForEMAData", "", LOGGER, null, "", e.getMessage());

		}
	}
}
