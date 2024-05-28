package com.olympus.OEKG.ComplaintService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.microsoft.sqlserver.jdbc.SQLServerException;
import com.olympus.OEKG.Utility.DBUtil;
import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.repository.EtqMonitorDAOImpl;

public class UploadDataBase {
	private utility objUtil = new utility();
	final Logger LOGGER = LoggerFactory.getLogger(UploadDataBase.class);

	public static Connection con;

	public Map<String, Object> ValidateFileToUpload() {
		CallableStatement callableStatement = null;
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try {

		} catch (Exception e) {
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", e.getMessage());
			LOGGER.error(e.getMessage());
		} finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
			} catch (SQLException e) {
				mapResponse.put("MESSAGE", e.getMessage());
				LOGGER.error(e.getMessage());
			}
		}
		return mapResponse;
	}

	public Map<String, Object> uploadFileData(InputStream uploadedInputStream,
			int createdBy, String filedetail, int businessUnitID) {
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		byte[] bytes = null;
		InputStream fileInputStrem = null;
		try {
			List<Map<String, String>> data = objUtil.getBCDatabaseCount(businessUnitID,
					"COMPLAINT");

			List<Map<String, String>> details = objUtil.getBCDatabaseDetails(businessUnitID,
					"COMPLAINT");

			String DB_COUNT = null;

			if (!data.isEmpty()) {
				for (Map<String, String> map : data) {
					DB_COUNT = map.get("DATA_COUNT");
				}
			}
			if (!details.isEmpty()) {
				for (Map<String, String> map : details) {
					
					String server_path = map.get("SQL_DRIVER");
					DBUtil.DRIVER_CLASS_CP = server_path;
					DBUtil.SERVER_PATH_CP = map.get("SERVER_PATH");
					DBUtil.SERVER_USER_CP = map.get("SERVER_USER");
					DBUtil.SERVER_PASSWORD_CP = map.get("SERVER_PASSWORD");

				}
			}
			con = DBUtil.getDataSourceClientComp().getConnection();

			bytes = IOUtils.toByteArray(uploadedInputStream);
			fileInputStrem = new ByteArrayInputStream(bytes);
			String filename = filedetail;
			String extension = filename.substring(
					filename.lastIndexOf(".") + 1, filename.length());
			String excelType = "xls";
			

			if (!excelType.equalsIgnoreCase(extension)) {
				
				mapResponse = uploadXlsxFileData(fileInputStrem, createdBy,
						bytes, filename);
			} else {
				
				mapResponse = uploadXlsFileData(fileInputStrem, createdBy,
						bytes, filename);
			}
			LOGGER.info("The complaint file was uploaded by the User ID : " + createdBy);
			
		} catch (Exception e) {
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", e.getMessage());
			LOGGER.error(e.getMessage());
		} finally {
			bytes = null;
			try {
				if (fileInputStrem != null)
					fileInputStrem.close();
			} catch (IOException e) {
				LOGGER.error(e.getMessage());
			}
			try {
				if (uploadedInputStream != null)
					uploadedInputStream.close();
			} catch (IOException e) {
				mapResponse.put("MESSAGE", e.getMessage());
				LOGGER.error(e.getMessage());
			}
			try {
				if (con != null)
					con.close();
			} catch (SQLException e) {
				mapResponse.put("MESSAGE", e.getMessage());
				LOGGER.error(e.getMessage());
			}
		}
		return mapResponse;
	}

	public Map<String, Object> storeFile(InputStream uploadedInputStream,
			String filename, int createdBy) throws IOException {
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		SQLServerCallableStatement callableStatement = null;
		long length = uploadedInputStream.available();
		try {
			
			String query = ("INSERT INTO xx_etq_complaint_file_tbl (FILE_OBJECT,FILE_NAME,CREATED_BY,CREATION_DATE) VALUES(?,?,?,GETDATE())");
			PreparedStatement pstmt;
			pstmt = con.prepareStatement(query);
			if (filename.contains(".xls")) {
				pstmt.setBinaryStream(1, uploadedInputStream, length);
			} else {
				pstmt.setBinaryStream(1, uploadedInputStream, length);
			}
			pstmt.setString(2, filename);

			pstmt.setInt(3, createdBy);
			pstmt.executeUpdate();
			
			mapResponse.put("CODE", 4);
			mapResponse.put("MESSAGE", "File Stored succesfully.");
		} catch (Exception e) {
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", e.getMessage());
			LOGGER.error(e.getMessage());
		} finally {
			try {
				if (callableStatement != null)
					callableStatement.close();
			} catch (SQLException e) {
				mapResponse.put("MESSAGE", e.getMessage());
				LOGGER.error(e.getMessage());
			}
		}
		return mapResponse;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "deprecation", "static-access" })
	public Map<String, Object> uploadXlsxFileData(
			InputStream uploadedInputStream, int createdBy, byte[] bytes,
			String filename) {
		
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try {

			XSSFWorkbook wb = new XSSFWorkbook(uploadedInputStream);

			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row = sheet.getRow(0);

			short minColIx = row.getFirstCellNum();
			short maxColIx = row.getLastCellNum();
			int totalRows = sheet.getLastRowNum() + 1;
		
			Map<String, Integer> headerMap = new HashMap<String, Integer>();
			ArrayList<String> colList = getColumnMapping();
			for (short colIx = minColIx; colIx < maxColIx; colIx++) {
				XSSFCell cell1 = row
						.getCell(
								colIx,
								org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
				headerMap.put(cell1.getStringCellValue(),
						cell1.getColumnIndex());
			}

			boolean fileValid = true;
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < colList.size(); i++) {
				if (!headerMap.containsKey(colList.get(i))) {
					sb.append(colList.get(i) + ",");
					fileValid = false;
				}
			}

			List<List> sheetData = new ArrayList<List>();
			List<String> data = null;
			
			if (!fileValid) {
				
				System.out
						.println("File template is not valid. Following Column is/are missing ["
								+ sb + "]");
				if (sb.length() > 0)
					sb.deleteCharAt(sb.length() - 1);
				wb.close();
				mapResponse.put("CODE", 999);
				mapResponse.put("MESSAGE",
						"File template is not valid. Following Column is/are missing ["
								+ sb + "]");
				return mapResponse;
			} else {
			
				for (int x = 1; x < totalRows; x++) {
					XSSFRow dataRow = sheet.getRow(x);
					if (dataRow == null) {
						continue;
					}

					data = new ArrayList<String>();
					int idxForColumnx = 0;
					for (int i = 0; i < colList.size(); i++) {
						idxForColumnx = headerMap.get(colList.get(i));
						XSSFCell cell2 = dataRow
								.getCell(
										idxForColumnx,
										org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
						if (cell2.getCellTypeEnum() == CellType.NUMERIC) {
							int numberValue = (int) cell2.getNumericCellValue();
							data.add(numberValue + "");
						} else {
							data.add(cell2.getStringCellValue());
						}
					}
					
					sheetData.add(data);
				}
				wb.close();
				
				mapResponse = storeFile(uploadedInputStream, filename,
						createdBy);
				if (mapResponse.get("CODE").toString().equals("999")) {
					wb.close();
					return mapResponse;
				} else {
					List<String> listData = null;
					ArrayList<String> colIdx = getColumnMapping();
					colIdx.set(3, "AWARE_DATE");
					
					Map<String, String> defaultVal = setDefaultValue();
				
					for (int i = 0; i < sheetData.size(); i++) {
						listData = new ArrayList<String>();
						listData = sheetData.get(i);
						
						
						String strcolumnList = colIdx.toString().substring(1,
								colIdx.toString().length() - 1);
						ArrayList<String> colValue = new ArrayList<String>();
						String strEvalCompDate = "", strNotiDate = "", strCreateDate = "", strSrNo = "";
						for (int x = 0; x < colIdx.size(); x++) {

							strSrNo = listData.get(0);
							
							if (listData.get(3) != null
									&& listData.get(3).length() > 0) {
								strNotiDate = validateDateFormat(listData
										.get(3));
							}
							if (listData.get(27) != null
									&& listData.get(27).length() > 0) {
								strCreateDate = validateDateFormat(listData
										.get(27));
							}
							if (listData.get(28) != null
									&& listData.get(28).length() > 0) {
								
								strEvalCompDate = validateDateFormat(listData
										.get(28));
							}

							if (strNotiDate != null && strNotiDate.length() > 0
									&& strNotiDate.equalsIgnoreCase("No")) {
								mapResponse.put("CODE", 999);
								mapResponse
										.put("MESSAGE",
												"Invalid Date format for NOTIFICATION_DATE");
								
								return mapResponse;
							}

							if (strEvalCompDate != null
									&& strEvalCompDate.length() > 0
									&& strEvalCompDate.equalsIgnoreCase("No")) {
								mapResponse.put("CODE", 999);
								mapResponse
										.put("MESSAGE",
												"Invalid Date format for EVALUATION_COMPLETION_DATE");
							
								return mapResponse;

							}

							if (strCreateDate != null
									&& strCreateDate.length() > 0
									&& strCreateDate.equalsIgnoreCase("No")) {
								mapResponse.put("CODE", 999);
								mapResponse
										.put("MESSAGE",
												"Invalid Date format for INITIATION_DATE");
								
								return mapResponse;
							}

							if ((listData.get(x) == null || listData.get(x)
									.length() == 0)
									&& colIdx.get(x) != "INVESTIGATION_DECISION_DATE"
									&& colIdx.get(x) != "EVALUATION_INITIATION_DATE"
									&& colIdx.get(x) != "AWARE_DATE"
									&& colIdx.get(x) != "EVALUATION_COMPLETION_DATE") {
								String defaultValue = defaultVal.get(colIdx
										.get(x));
								if (defaultValue != null
										&& defaultValue.length() > 0) {
									colValue.add("'"
											+ defaultValue.replace("'", "''")
											+ "'");
								} else {
									colValue.add("NULL");
								}
							} else if ((listData.get(x) == null || listData
									.get(x).length() == 0)
									&& colIdx.get(x) == "AWARE_DATE") {
								if (strCreateDate != null
										&& strCreateDate.length() > 0) {
									colValue.add("'" + strCreateDate + "'");
								} else {
									colValue.add("NULL");
								}
							} else if ((listData.get(x) == null || listData
									.get(x).length() == 0)
									&& colIdx.get(x) == "EVALUATION_INITIATION_DATE") {
								if (strCreateDate != null
										&& strCreateDate.length() > 0) {
									colValue.add("'" + strCreateDate + "'");
								} else {
									colValue.add("NULL");
								}
							} else if ((listData.get(x) == null || listData
									.get(x).length() == 0)
									&& colIdx.get(x) == "EVALUATION_COMPLETION_DATE") {
	
									colValue.add("NULL");
								
							} else if ((listData.get(x) == null || listData
									.get(x).length() == 0)
									&& colIdx.get(x) == "INVESTIGATION_DECISION_DATE") {

								if (strEvalCompDate != null
										&& strEvalCompDate.length() > 0) {
									colValue.add("'" + strEvalCompDate + "'");
								} else {
									colValue.add("NULL");
								}

							}

							else {
								String dataValue = listData.get(x);
								colValue.add("'" + dataValue.replace("'", "''")
										+ "'");
							}
						}
						
						

						// Added for ETQCR-973
						CallableStatement cs = con
								.prepareCall("{call XX_CARDIFF_COMPLAINT_UPLOAD_PRC(?,?,?,?)}");
						cs.setString(1, strSrNo);
						cs.setString(2, strcolumnList);
						// colValue.toString().replaceAll("'NULL'", "NULL");
					
						cs.setString(
								3,
								colValue.toString().substring(1,
										colValue.toString().length() - 1));
						cs.registerOutParameter(4, Types.VARCHAR);
						cs.execute();
						String strErrMsg = cs.getString(4);
						if (!strErrMsg.equals("Inserted")) {
							
							mapResponse.put("CODE", 999);
							mapResponse.put("MESSAGE", strErrMsg);
							
						}
						cs.close();
					}
				}
			}

		} catch (Exception e) {
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", e.getMessage());
			LOGGER.error("", e);
		}
		return mapResponse;
	}

	@SuppressWarnings({ "unchecked", "rawtypes", "deprecation", "static-access" })
	public Map<String, Object> uploadXlsFileData(
			InputStream uploadedInputStream, int createdBy, byte[] bytes,
			String filename) {
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try {
			HSSFWorkbook wb = new HSSFWorkbook(uploadedInputStream);
			HSSFSheet sheet = wb.getSheetAt(0);
			HSSFRow row = sheet.getRow(0);

			short minColIx = row.getFirstCellNum();
			short maxColIx = row.getLastCellNum();
			int totalRows = sheet.getPhysicalNumberOfRows();

			Map<String, Integer> headerMap = new HashMap<String, Integer>();
			ArrayList<String> colList = getColumnMapping();

			for (short colIx = minColIx; colIx < maxColIx; colIx++) {
				HSSFCell cell = row
						.getCell(
								colIx,
								org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
				headerMap.put(cell.getStringCellValue(), cell.getColumnIndex());
			}

			boolean fileValid = true;
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < colList.size(); i++) {
				if (!headerMap.containsKey(colList.get(i))) {
					sb.append(colList.get(i) + ",");
					fileValid = false;
				}
			}

			List<List> sheetData = new ArrayList<List>();
			List<String> data = null;
			HSSFCell cell = null;

			if (!fileValid) {
				if (sb.length() > 0)
					sb.deleteCharAt(sb.length() - 1);
				wb.close();
				mapResponse.put("CODE", 999);
				mapResponse.put("MESSAGE",
						"File template is not valid. Following Column is/are missing ["
								+ sb + "]");
				return mapResponse;
			} else {
				for (int x = 1; x < totalRows; x++) {
					HSSFRow dataRow = sheet.getRow(x);
					if (dataRow == null) {
						continue;
					}
					data = new ArrayList<String>();
					int idxForColumnx = 0;
					for (int i = 0; i < colList.size(); i++) {
						idxForColumnx = headerMap.get(colList.get(i));
						cell = dataRow.getCell((short) idxForColumnx);
						if (cell != null
								&& cell.getCellTypeEnum() == CellType.NUMERIC) {
							int numberValue = (int) cell.getNumericCellValue();
							data.add(numberValue + "");
						} else if (cell != null) {
							data.add(cell.getStringCellValue());
						} else {
							data.add("");
						}
					}
					sheetData.add(data);
				}
				wb.close();

				mapResponse = storeFile(uploadedInputStream, filename,
						createdBy);
				if (mapResponse.get("CODE").toString().equals("999")) {
					wb.close();
					return mapResponse;
				} else {
					List<String> listData = null;
					ArrayList<String> colIdx = getColumnMapping();
					Map<String, String> defaultVal = setDefaultValue();
					colIdx.set(3, "AWARE_DATE");
					for (int i = 0; i < sheetData.size(); i++) {
						listData = new ArrayList<String>();
						listData = sheetData.get(i);
						String strcolumnList = colIdx.toString().substring(1,
								colIdx.toString().length() - 1);
						ArrayList<String> colValue = new ArrayList<String>();
						String strEvalCompDate = "", strNotiDate = "", strCreateDate = "", strSrNo = "";
						for (int x = 0; x < colIdx.size(); x++) {

							strSrNo = listData.get(0);
							

							if (listData.get(3) != null
									&& listData.get(3).length() > 0) {
								strNotiDate = validateDateFormat(listData
										.get(3));
							}

							if (listData.get(27) != null
									&& listData.get(27).length() > 0) {
								strCreateDate = validateDateFormat(listData
										.get(27));
							}

							if (listData.get(28) != null
									&& listData.get(28).length() > 0) {
								
								strEvalCompDate = validateDateFormat(listData
										.get(28));
							}

							if (strNotiDate != null && strNotiDate.length() > 0
									&& strNotiDate.equalsIgnoreCase("No")) {
								mapResponse.put("CODE", 999);
								mapResponse
										.put("MESSAGE",
												"Invalid Date format for NOTIFICATION_DATE");
								return mapResponse;
							}

							if (strEvalCompDate != null
									&& strEvalCompDate.length() > 0
									&& strEvalCompDate.equalsIgnoreCase("No")) {
								mapResponse.put("CODE", 999);
								mapResponse
										.put("MESSAGE",
												"Invalid Date format for EVALUATION_COMPLETION_DATE");
								return mapResponse;
							}

							if (strCreateDate != null
									&& strCreateDate.length() > 0
									&& strCreateDate.equalsIgnoreCase("No")) {
								mapResponse.put("CODE", 999);
								mapResponse
										.put("MESSAGE",
												"Invalid Date format for INITIATION_DATE");
								return mapResponse;
							}

							if ((listData.get(x) == null || listData.get(x)
									.length() == 0)
									&& colIdx.get(x) != "INVESTIGATION_DECISION_DATE"
									&& colIdx.get(x) != "EVALUATION_INITIATION_DATE"
									&& colIdx.get(x) != "AWARE_DATE"
									&& colIdx.get(x) != "EVALUATION_COMPLETION_DATE") {
								String defaultValue = defaultVal.get(colIdx
										.get(x));
								if (defaultValue != null
										&& defaultValue.length() > 0) {
									colValue.add("'"
											+ defaultValue.replace("'", "''")
											+ "'");
								} else {
									colValue.add("NULL");
								}
							} else if ((listData.get(x) == null || listData
									.get(x).length() == 0)
									&& colIdx.get(x) == "AWARE_DATE") {
								if (strCreateDate != null
										&& strCreateDate.length() > 0) {
									colValue.add("'" + strCreateDate + "'");
								} else {
									colValue.add("NULL");
								}
							} else if ((listData.get(x) == null || listData
									.get(x).length() == 0)
									&& colIdx.get(x) == "EVALUATION_INITIATION_DATE") {
								if (strCreateDate != null
										&& strCreateDate.length() > 0) {
									colValue.add("'" + strCreateDate + "'");
								} else {
									colValue.add("NULL");
								}
							}
							else if ((listData.get(x) == null || listData
									.get(x).length() == 0)
									&& colIdx.get(x) == "EVALUATION_COMPLETION_DATE") {
	
									colValue.add("NULL");
								
							}else if ((listData.get(x) == null || listData
									.get(x).length() == 0)
									&& colIdx.get(x) == "INVESTIGATION_DECISION_DATE") {
								if (strEvalCompDate != null
										&& strEvalCompDate.length() > 0) {
									colValue.add("'" + strEvalCompDate + "'");
								} else {
									colValue.add("NULL");
								}
							} else {
								String dataValue = listData.get(x);
								colValue.add("'" + dataValue.replace("'", "''")
										+ "'");
							}
						}
						// Added for ETQCR-973
						CallableStatement cs = con
								.prepareCall("{call XX_CARDIFF_COMPLAINT_UPLOAD_PRC(?,?,?,?)}");
						cs.setString(1, strSrNo);
						cs.setString(2, strcolumnList);
						colValue.toString().replaceAll("'NULL'", "NULL");
						cs.setString(
								3,
								colValue.toString().substring(1,
										colValue.toString().length() - 1));
						cs.registerOutParameter(4, Types.VARCHAR);
						cs.execute();
						String strErrMsg = cs.getString(4);
						if (!strErrMsg.equals("Inserted")) {
							
							mapResponse.put("CODE", 999);
							mapResponse.put("MESSAGE", strErrMsg);
						}
						cs.close();
					}
				}
			}

		} catch (Exception e) {
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", e.getMessage());
			LOGGER.error("", e);
		}
		return mapResponse;
	}

	public ArrayList<String> getColumnMapping() {
		ArrayList<String> columList = new ArrayList<String>();
		try {
			columList.add("SYSTEM_SOURCE_REF_NO");
			columList.add("SYSTEM_SOURCE");
			columList.add("INITIATOR");
			columList.add("NOTIFICATION_DATE");
			columList.add("REPORTING_PERSON");
			columList.add("EVENT_DESCRIPTION");
			columList.add("DEC_TREE_ANS_1");
			columList.add("DEC_TREE_ANS_2");
			columList.add("CUSTOMER_TELEPHONE");
			columList.add("CUSTOMER_NAME");
			columList.add("CUSTOMER_CITY");
			columList.add("CUSTOMER_ADDRESS");
			columList.add("CUSTOMER_ZIP");
			columList.add("COUNTRY");
			columList.add("STATE");
			columList.add("MODEL_NUMBER");
			columList.add("ITEM_CODE");
			columList.add("LOT_SERIAL");
			columList.add("PRODUCT_QTY");
			columList.add("INVESTIGATION_REQUIRED");
			columList.add("NO_INVESTIGATION_RATIONALE");
			columList.add("CORRECTION_DESCRIPTION");
			columList.add("CORRECTION_ACTION");
			columList.add("COMPLAINT_CLOSURE_DATE");
			columList.add("COMPLAINT_CLOSED_BY");
			columList.add("EVENTDATE_UNKNOWN");
			columList.add("INITIATOR_LOCATION");
			columList.add("INITIATION_DATE");
			columList.add("EVALUATION_COMPLETION_DATE");
			columList.add("INVESTIGATION_DECISION_DATE");
			columList.add("EVALUATION_INITIATION_DATE");
			columList.add("EVALUATION_COMPLETED_BY");
			columList.add("INVESTIGATION_DECISION_BY");
			columList.add("EVALUATION_INITIATION_BY");
			columList.add("EVALUATION_RESULT");
		} catch (Exception e) {

			LOGGER.error(e.getMessage());
		}
		return columList;
	}

	// Modified for ETQCR-973
	public Map<String, String> setDefaultValue() {
		Map<String, String> defaultValue = new HashMap<String, String>();
		try {
			Connection conn = DBUtil.dataSource.getConnection();
			Statement cs = conn.createStatement();
			ResultSet rs = cs
					.executeQuery("SELECT COLUMN_NAME,DEFAULT_VALUE FROM XX_ETQ_DEFAULT_VALUE_TBL WHERE TABLE_NAME='XX_COMPLAINT_INTERFACE'");
			if (rs != null) {
				Date date = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				String strDate = formatter.format(date);
				while (rs.next()) {
					if (!rs.getString(1).equalsIgnoreCase(
							"COMPLAINT_CLOSURE_DATE")) {
						defaultValue.put(rs.getString(1), rs.getString(2));
					} else if (rs.getString(1).equalsIgnoreCase(
							"COMPLAINT_CLOSURE_DATE")) {
						if (rs.getString(2).equalsIgnoreCase("CURRENT_DATE")) {
							defaultValue.put("COMPLAINT_CLOSURE_DATE", strDate);
						} else {
							defaultValue.put(rs.getString(1), rs.getString(2));
						}
					}
				}
			}
		} catch (Exception e) {

			LOGGER.error(e.getMessage());
		}
		return defaultValue;
	}

	@SuppressWarnings("static-access")
	public String validateDateFormat(String strDate) {
		String strResult = "Yes";
		try {
			if (strDate.length() <= 10) {
				strDate = strDate + " 00:00:00";
			}

			SimpleDateFormat sdfrmt = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			sdfrmt.setLenient(false);

			Date javaDate = sdfrmt.parse(strDate);
			Calendar calendar = new GregorianCalendar();
			int year = calendar.get(Calendar.YEAR);
			if ((year + 1900) < 2000) {
				strResult = "No";
			}
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(javaDate);
		} catch (Exception e) {
			try {
				if (strDate.length() <= 10) {
					strDate = strDate + " 00:00:00";
				}

				SimpleDateFormat sdfrmt = new SimpleDateFormat(
						"dd/MM/yyyy HH:mm:ss");
				sdfrmt.setLenient(false);

				Date javaDate = sdfrmt.parse(strDate);
				Calendar calendar = new GregorianCalendar();
				int year = calendar.get(Calendar.YEAR);
				if ((year + 1900) < 2000) {
					strResult = "No";
				} else {
					return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
							.format(javaDate);
				}
			} catch (Exception ex) {
				strResult = "No";
				LOGGER.error(e.getMessage());
			}
		}
		return strResult;
	}
}