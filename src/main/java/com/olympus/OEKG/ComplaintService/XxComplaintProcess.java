package com.olympus.OEKG.ComplaintService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.dbutils.DbUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.olympus.OEKG.Utility.DBUtil;
import com.olympus.OEKG.Utility.SmtpMailSender;
import com.olympus.OEKG.Utility.utility;

@Service
public class XxComplaintProcess {

	final Logger logger = LoggerFactory.getLogger(XxComplaintProcess.class);

	private static XxComplaintInterfaceBean objInterfaceBean = new XxComplaintInterfaceBean();
	private static utility objUtil = new utility();

	String bUSqlURL = null;
	String bUSQLUser = null;
	String bUSQLPswd = null;

	public void complaintProcess(int intBusinessUnit, String strInstanceName, String strInterfaceType,
			int intChildPID) {
		int errorCnt = 0;
		ResultSet rs = null;
		Statement stmt = null;

		Statement Clientstmt = null;
		ResultSet ClientRS = null;

		try (Connection con = DBUtil.dataSource.getConnection();) {
			this.objInterfaceBean.setBusinessUnitID(intBusinessUnit);
			this.objInterfaceBean.setServerName(strInstanceName);
			this.objInterfaceBean.setInterfaceType(strInterfaceType);
			this.objInterfaceBean.setBatchId(intChildPID);

			this.objInterfaceBean.setBaseConnection(con);
			stmt = con.createStatement();
			rs = stmt.executeQuery(
					"SELECT SERVER_PATH,SERVER_USER,SERVER_PASSWORD,ETQ_SERVICE_USER,ETQ_SERVICE_PASSWORD,ETQ_URL,"
							+ "SQL_DRIVER,DATA_SYSTEM_SOURCE,DAY_LIGHT_SAVING FROM XX_ETQ_SERVER_CONFIG_TBL "
							+ "WHERE BUSINESS_UNIT_SITE_ID=" + intBusinessUnit
							+ " AND IS_ACTIVE='Y' AND FILE_NAME='COMPLAINT'");

			List<Map<String, String>> serverdata = objUtil.generateResultSetToMap(rs);

			for (Map<String, String> map : serverdata) {
				this.bUSqlURL = map.get("SERVER_PATH");
				this.bUSQLUser = map.get("SERVER_USER");
				this.bUSQLPswd = map.get("SERVER_PASSWORD");
				this.objInterfaceBean.setEtQUser(map.get("ETQ_SERVICE_USER"));
				this.objInterfaceBean.setServerCred(map.get("ETQ_SERVICE_PASSWORD"));
				this.objInterfaceBean.setEtQURL(map.get("ETQ_URL"));
				this.objInterfaceBean.setBusinessUnitDriver(map.get("SQL_DRIVER"));
				this.objInterfaceBean.setBusinessUnitName(map.get("DATA_SYSTEM_SOURCE"));
				this.objInterfaceBean.setTimeZones(map.get("DAY_LIGHT_SAVING"));
			}

			try (Connection ClientConnection = DBUtil.dataSourceClientComp.getConnection();) {
				this.objInterfaceBean.setBusinessUnitConnection(ClientConnection);
				Clientstmt = ClientConnection.createStatement();
				String compQuery = "SELECT INTERFACE_RECORD_ID FROM XX_COMPLAINT_INTERFACE WHERE (ETQ_UPLOAD_STATUS IS NULL OR ETQ_UPLOAD_STATUS=1 OR ETQ_UPLOAD_STATUS=0) AND ETQ_BATCH_ID="
						+ intChildPID;

				if (this.objInterfaceBean.getInterfaceType().equalsIgnoreCase("PAE")) {
					compQuery = compQuery
							+ " AND (UPPER(DEC_TREE_ANS_1) IN ('Y','YES') OR (UPPER(DEC_TREE_ANS_1) IN ('N','NO','U','UNKNOWN') AND UPPER(DEC_TREE_ANS_2) IN ('Y','YES','U','UNKNOWN')))";
				} else if (this.objInterfaceBean.getInterfaceType().equalsIgnoreCase("NON-PAE")) {
					compQuery = compQuery
							+ " AND UPPER(DEC_TREE_ANS_1) IN ('NO','N','U','UNKNOWN') AND UPPER(DEC_TREE_ANS_2) IN ('NO','N')";
				}

				ClientRS = Clientstmt.executeQuery(compQuery);
				XxGetComplaintResultMap objCompJson = new XxGetComplaintResultMap();
				Map<String, String> map = null;

				while (ClientRS.next()) {
					this.objInterfaceBean.clearBean();
					map = objCompJson.generateComplaintMap(ClientConnection, ClientRS.getString(1));
					if (map.size() > 0) {
						objUtil.updateBatchLogCust(intChildPID, utility.PARENT_BATCH_ID_COMP, "", "RUNNING",
								"Execution In Progress");
						this.objInterfaceBean.setMapResultSet(map);
						XxComplaintInterface objComp = new XxComplaintInterface(this.objInterfaceBean);
						String strMessage = objComp.runComplaintInterface();
						if (strMessage != null && strMessage.contains("Error: ")) {
							System.out.println("strMEssage: " + strMessage);
							errorCnt++;
						}
					}
				}
				System.out.println("errorCount: " + errorCnt);

				if (errorCnt > 0) {
					System.out.println("into update batch log");
					String endDate_Batch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(Calendar.getInstance().getTime());
					objUtil.updateBatchLogCust(intChildPID, utility.PARENT_BATCH_ID_COMP, endDate_Batch, "ERROR",
							"Execution Completed Successfully");
					objUtil.sendMail(intChildPID);
				} else {
					String endDate_Batch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(Calendar.getInstance().getTime());
					objUtil.updateBatchLogCust(intChildPID, utility.PARENT_BATCH_ID_COMP, endDate_Batch, "COMPLETED",
							"Execution Completed Successfully");
				}
				DbUtils.closeQuietly(ClientRS);
				DbUtils.closeQuietly(Clientstmt);
				DbUtils.closeQuietly(ClientConnection);
			} catch (Exception e) {

				String eMsg = "";

				if (e != null) {
					logger.error("System Error:", e);
					eMsg = e.getMessage().replaceAll("'", "''");
				}
				try {
					String endDate_Batch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(Calendar.getInstance().getTime());
					objUtil.dDebug(1, endDate_Batch, eMsg, "", null, "COMPLAINT", "COMPLAINT", 0, "", "", null, null, 3,
							this.objInterfaceBean.getBusinessUnitID(), intChildPID);
					objUtil.dDebug(2, endDate_Batch, eMsg, "", null, "COMPLAINT", "COMPLAINT", 0, "", "", null, null, 3,
							this.objInterfaceBean.getBusinessUnitID(), intChildPID);
					objUtil.updateBatchLogCust(intChildPID, utility.PARENT_BATCH_ID_COMP, endDate_Batch, "ERROR",
							"Execution Completed Successfully");
					objUtil.sendMail(intChildPID);
				} catch (Exception er) {
					String endDate_Batch = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(Calendar.getInstance().getTime());
					logger.error("System Error:", er);
					objUtil.updateBatchLogCust(intChildPID, utility.PARENT_BATCH_ID_COMP, endDate_Batch, "ERROR",
							"Execution Completed Successfully");
					objUtil.sendMail(intChildPID);
				}
			} finally {
				try {
					if (this.objInterfaceBean.getBusinessUnitConnection() != null
							&& !this.objInterfaceBean.getBusinessUnitConnection().isClosed()) {
						this.objInterfaceBean.getBusinessUnitConnection().close();
					}
				} catch (SQLException ex) {
					logger.error("SQLException Error:", ex);
				}
//				DbUtils.closeQuietly(ClientRS);
//				DbUtils.closeQuietly(Clientstmt);
			}
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(con);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (this.objInterfaceBean.getBusinessUnitConnection() != null
						&& !this.objInterfaceBean.getBusinessUnitConnection().isClosed()) {
					this.objInterfaceBean.getBusinessUnitConnection().close();
				}
			} catch (SQLException ex) {
				logger.error("SQLException Error:", ex);
			}
//			DbUtils.closeQuietly(rs);
//			DbUtils.closeQuietly(stmt);
		}
	}

}
