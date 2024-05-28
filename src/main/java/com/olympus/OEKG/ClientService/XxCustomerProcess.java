package com.olympus.OEKG.ClientService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.olympus.OEKG.ComplaintService.XxComplaintProcess;
import com.olympus.OEKG.Utility.DBUtil;
import com.olympus.OEKG.Utility.utility;

public class XxCustomerProcess {
	final Logger logger = LoggerFactory.getLogger(XxCustomerProcess.class);

	private static utility objUtil = new utility();
	String eMsg = null;
	String bUSqlURL = null;
	String bUSQLUser = null;
	String bUSQLPswd = null;
	String bUEtQUser = null;
	String bUsqldriver = null;
	String compQuery = null;
	String tomcatCrd = null;
	String buName = null;
	String etqURL = null;
	String debugTbl = null;
	String batchLogTbl = null;
	Connection ClientConnection = null;
	Connection con = null;

	public void customerProcess(int businessUnit, String businessUnitSystem, int batch_id) throws SQLException {
		int errorCnt = 0;
		ResultSet rs = null;
		Statement stmt = null;
		;
		Statement Clientstmt = null;
		ResultSet ClientRS = null;
		try {
			con = DBUtil.dataSource.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(
					"SELECT SERVER_PATH,SERVER_USER,SERVER_PASSWORD,ETQ_SERVICE_USER,ETQ_SERVICE_PASSWORD,ETQ_URL,SQL_DRIVER,DATA_SYSTEM_SOURCE FROM XX_ETQ_SERVER_CONFIG_TBL WHERE BUSINESS_UNIT_SITE_ID='"
							+ businessUnit + "' AND IS_ACTIVE='Y' AND FILE_NAME='CUSTOMER'");
			List<Map<String, String>> serverdata = objUtil.generateResultSetToMap(rs);

			for (Map<String, String> map : serverdata) {
				this.bUSqlURL = map.get("SERVER_PATH");
				this.bUSQLUser = map.get("SERVER_USER");
				this.bUSQLPswd = map.get("SERVER_PASSWORD");
				this.bUEtQUser = map.get("ETQ_SERVICE_USER");
				this.tomcatCrd = map.get("ETQ_SERVICE_PASSWORD");
				this.etqURL = map.get("ETQ_URL");
				this.bUsqldriver = map.get("SQL_DRIVER");
				this.buName = map.get("DATA_SYSTEM_SOURCE");
			}

			try {
				ClientConnection = DBUtil.dataSourceClient.getConnection();
				Clientstmt = ClientConnection.createStatement();
				ClientRS = Clientstmt.executeQuery(
						"SELECT U_ID from XX_ETQ_CUSTOMER_PROFILE_DETAIL WHERE (ETQ_UPLOAD_STATUS=1 OR ETQ_UPLOAD_STATUS IS NULL OR ETQ_UPLOAD_STATUS=0) AND ETQ_BATCH_ID="
								+ batch_id);
				XxGetCustomerResultMap objCustMap = new XxGetCustomerResultMap();
				Map<String, String> map = null;
				while (ClientRS.next()) {
					map = objCustMap.generateCustomerMap(ClientConnection, ClientRS.getString(1));
					if (map.size() > 0) {
						XxCustomerInterface objCust = new XxCustomerInterface(con, map, etqURL, bUEtQUser, tomcatCrd,
								buName, ClientConnection, bUsqldriver, batch_id, businessUnit, businessUnitSystem);
						String strMessage = objCust.runCustomerInterface();
						if (strMessage != null && strMessage.contains("Error: ")) {
							errorCnt++;
						}
					}

				}
				if (errorCnt > 0) {
					String endDate_Batch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(Calendar.getInstance().getTime());
					objUtil.updateBatchLogCust(batch_id, utility.PARENT_BATCH_ID, endDate_Batch, "ERROR",
							"Execution Completed Successfully");
				} else {
					String endDate_Batch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(Calendar.getInstance().getTime());
					objUtil.updateBatchLogCust(batch_id, utility.PARENT_BATCH_ID, endDate_Batch, "COMPLETED",
							"Execution Completed Successfully");
				}
				DbUtils.closeQuietly(ClientRS);
				DbUtils.closeQuietly(Clientstmt);
				DbUtils.closeQuietly(ClientConnection);

			} catch (Exception e) {

				if (e != null) {
					logger.error("System Error:", e);
					eMsg = e.getMessage().replaceAll("'", "''");
				}
				try {
					String endDate_Batch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(Calendar.getInstance().getTime());
					objUtil.updateBatchLogCust(batch_id, utility.PARENT_BATCH_ID, endDate_Batch, "ERROR",
							"Execution Completed Successfully");
				} catch (Exception er) {
					String endDate_Batch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(Calendar.getInstance().getTime());
					logger.error("System Error:", er);
					objUtil.updateBatchLogCust(batch_id, utility.PARENT_BATCH_ID, endDate_Batch, "ERROR",
							"Execution Completed Successfully");
				}
			} finally {
				try {
					if (ClientConnection != null && !ClientConnection.isClosed()) {
						ClientConnection.close();
					}
				} catch (SQLException ex) {
					logger.error("SQLException Error:", ex);
				}

				DbUtils.closeQuietly(ClientRS);
				DbUtils.closeQuietly(Clientstmt);

			}
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(con);
		}

		catch (Exception er) {
			er.printStackTrace();
		} finally {
			try {
				if (con != null && !con.isClosed()) {
					con.close();
				}
			} catch (SQLException ex) {
				logger.error("SQLException Error:", ex);
			}
//			DbUtils.closeQuietly(rs);
//			DbUtils.closeQuietly(stmt);

		}

	}
}