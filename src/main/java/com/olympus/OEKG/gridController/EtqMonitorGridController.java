package com.olympus.OEKG.gridController;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.olympus.OEKG.ComplaintService.UploadDataBase;
import com.olympus.OEKG.Utility.DBUtil;
import com.olympus.OEKG.Utility.utility;
import com.olympus.OEKG.model.Invoice;
import com.olympus.OEKG.repository.EtqMonitorDAO;
import com.olympus.OEKG.repository.EtqMonitorDAOImpl;
import com.olympus.OEKG.view.InvoiceDataExcelExport;

//import javax.ws.rs.Consumes;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.ResponseBuilder;

@RestController
@RequestMapping("/etqMonitor")
public class EtqMonitorGridController {
	final Logger LOGGER = LoggerFactory.getLogger(EtqMonitorGridController.class);
	private utility objUtil = new utility();
	@Autowired
	private EtqMonitorDAO etqMonitorDAO;

	private UploadDataBase up = new UploadDataBase();

	@GetMapping("/getAllCompData")
	public Map<String, Object> getAllCompData() {
		Map<String, Object> getallCompdata = etqMonitorDAO.getallCompdata();
		return getallCompdata;
	}

	@GetMapping("/getAllCusData")
	public Map<String, Object> getAllCusData() {
		Map<String, Object> getallCustdata = etqMonitorDAO.getallCustdata();
		return getallCustdata;
	}

	@GetMapping("/getAllAddRefData")
	public Map<String, Object> getAllAddRefData() {
		Map<String, Object> getallAddRefdata = etqMonitorDAO.getallAddRefdata();
		return getallAddRefdata;
	}

	@PostMapping("/getSearchCompData")
	public Map<String, Object> getSearchCompData(@RequestParam("REFERENCE_NUMBER") String REFERENCE_NUMBER,
			@RequestParam("ETQ_UPLOAD_STATUS") Integer ETQ_UPLOAD_STATUS,
			@RequestParam("DEBUG_LEVEL") Integer DEBUG_LEVEL,
			@RequestParam("REQUEST_DATE_FROM") String REQUEST_DATE_FROM,
			@RequestParam("REQUEST_DATE_TO") String REQUEST_DATE_TO, @RequestParam("BATCH_ID") Integer BATCH_ID,
			@RequestParam("INTERFACE_NAME") String INTERFACE_NAME) {

		return etqMonitorDAO.getSearchCompData(REFERENCE_NUMBER, ETQ_UPLOAD_STATUS, 1, REQUEST_DATE_FROM,
				REQUEST_DATE_TO, BATCH_ID);
	}

	@PostMapping("/getPendingCompData")
	public Map<String, Object> getPendingCompData(@RequestParam("REFERENCE_NUMBER") String REFERENCE_NUMBER,
			@RequestParam("ETQ_UPLOAD_STATUS") Integer ETQ_UPLOAD_STATUS,
			@RequestParam("DEBUG_LEVEL") Integer DEBUG_LEVEL,
			@RequestParam("REQUEST_DATE_FROM") String REQUEST_DATE_FROM,
			@RequestParam("REQUEST_DATE_TO") String REQUEST_DATE_TO, @RequestParam("BATCH_ID") Integer BATCH_ID,
			@RequestParam("INTERFACE_NAME") String INTERFACE_NAME) {

		System.out.println("Inside pending data");

		Map<String, Object> pendingMap = null;

		if (INTERFACE_NAME.equalsIgnoreCase("COMPLAINT")) {
			pendingMap = etqMonitorDAO.getPendingCompData(REFERENCE_NUMBER, INTERFACE_NAME);
		}

		if (INTERFACE_NAME.equalsIgnoreCase("CUSTOMER")) {
			pendingMap = etqMonitorDAO.getPendingCusData(REFERENCE_NUMBER, INTERFACE_NAME);
		}

		if (INTERFACE_NAME.equalsIgnoreCase("ADDITIONAL REFERENCE")) {
			pendingMap = etqMonitorDAO.getPendingAddRefData(REFERENCE_NUMBER, INTERFACE_NAME);
		}

		return pendingMap;
	}

	@PostMapping("/getActivityData")
	public Map<String, Object> getActivityData(@RequestParam("INTERFACE_DOCUMENT_ID") Integer INTERFACE_DOCUMENT_ID,
			@RequestParam("BATCH_ID") Integer BATCH_ID) {
		return etqMonitorDAO.getActivityDetails(INTERFACE_DOCUMENT_ID, BATCH_ID);
	}

	@PostMapping("/getContactData")
	public Map<String, Object> getContactData(@RequestParam("INTERFACE_DOCUMENT_ID") int INTERFACE_DOCUMENT_ID,
			@RequestParam("BATCH_ID") int BATCH_ID) {
		return etqMonitorDAO.getContactDetails(INTERFACE_DOCUMENT_ID, BATCH_ID);
	}

	@PostMapping("/getSearchCusData")
	public Map<String, Object> getSearchCusData(@RequestParam("REFERENCE_NUMBER") String REFERENCE_NUMBER,
			@RequestParam("ETQ_UPLOAD_STATUS") Integer ETQ_UPLOAD_STATUS,
			@RequestParam("DEBUG_LEVEL") Integer DEBUG_LEVEL,
			@RequestParam("REQUEST_DATE_FROM") String REQUEST_DATE_FROM,
			@RequestParam("REQUEST_DATE_TO") String REQUEST_DATE_TO, @RequestParam("BATCH_ID") Integer BATCH_ID) {

		return etqMonitorDAO.getSearchCusData(REFERENCE_NUMBER, ETQ_UPLOAD_STATUS, 1, REQUEST_DATE_FROM,
				REQUEST_DATE_TO, BATCH_ID);
	}

	@PostMapping("/getSearchAddRefData")
	public Map<String, Object> getSearchAddRefData(@RequestParam("REFERENCE_NUMBER") String REFERENCE_NUMBER,
			@RequestParam("ETQ_UPLOAD_STATUS") Integer ETQ_UPLOAD_STATUS,
			@RequestParam("DEBUG_LEVEL") Integer DEBUG_LEVEL,
			@RequestParam("REQUEST_DATE_FROM") String REQUEST_DATE_FROM,
			@RequestParam("REQUEST_DATE_TO") String REQUEST_DATE_TO, @RequestParam("BATCH_ID") Integer BATCH_ID) {

		return etqMonitorDAO.getSearchAddRefData(REFERENCE_NUMBER, ETQ_UPLOAD_STATUS, 1, REQUEST_DATE_FROM,
				REQUEST_DATE_TO, BATCH_ID);
	}

	@GetMapping("/getRequestStatus")
	public Map<String, Object> getRequestStatus() {
		return etqMonitorDAO.getRequestStatus();
	}

	@GetMapping("/excelview")
	public ModelAndView excelView() {

		Map<String, Object> mapResponse = new LinkedHashMap<String, Object>();

		String fileName = "";

		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String file_name = "EMA_DATA_EXTRACT";
		fileName = file_name + "_" + formatter.format(date) + ".xlsx";

		mapResponse = etqMonitorDAO.generateExcelSheet();

		ModelAndView mav = new ModelAndView();
		mav.setView(new InvoiceDataExcelExport());
		mav.addObject("listData", mapResponse.get("Data"));
//		mav.addObject("resultSet", mapResponse.get("resultSet"));

		return mav;
	}

	@GetMapping("/getAllErrorLogData")
	public Map<String, Object> getAllErrorLogData() {
		Map<String, Object> getallErrorLogdata = etqMonitorDAO.getallErrorLogdata();
		return getallErrorLogdata;
	}

	@PostMapping("/updateErrorLogData")
	public Map<String, Object> updateErrorLogData(@RequestParam("REFERENCE_NUMBER") String REFERENCE_NUMBER,
			@RequestParam("COMPLAINT_NUMBER") String COMPLAINT_NUMBER) {
		Map<String, Object> getallErrorLogdata = etqMonitorDAO.updateErrorLogdata(REFERENCE_NUMBER, COMPLAINT_NUMBER);
		return getallErrorLogdata;
	}

	@SuppressWarnings("resource")
	@PostMapping("/uploadcomplaintfile")
	public Map<String, Object> uploadcomp(@RequestParam("formFile") MultipartFile excel,
			@RequestParam("CREATED_BY") Integer CREATED_BY,
			@RequestParam("BUSINESS_UNIT_SITE_ID") Integer BUSINESS_UNIT_SITE_ID) {
		System.out.println("file name: " + excel.getOriginalFilename());
		try {
			up.uploadFileData(excel.getInputStream(), CREATED_BY, excel.getOriginalFilename(), BUSINESS_UNIT_SITE_ID);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try {
//			XSSFWorkbook workbook = new XSSFWorkbook(excel.getInputStream());
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			
//			for(int i=0; i<sheet.getPhysicalNumberOfRows();i++) {
//				XSSFRow row = sheet.getRow(i);
//				for(int j=0;j<row.getPhysicalNumberOfCells();j++) {
//					System.out.print(row.getCell(j) +" ");
//				}
//				System.out.println("");
//			}
//			
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		return null;

	}

	@PostMapping("/getbuid")
	public Map<String, Object> getBuID(@RequestParam("BUSSINESS_UNIT_SITE") String BUSSINESS_UNIT_SITE) {
		Map<String, Object> getallErrorLogdata = etqMonitorDAO.getBuID(BUSSINESS_UNIT_SITE);
		return getallErrorLogdata;
	}
	@PostMapping("/getbuiddatabase")
	public Map<String, Object> getBuIDdatabase(@RequestParam("BUSSINESS_UNIT_SITE") String BUSSINESS_UNIT_SITE) {
		Map<String, Object> getallErrorLogdata = etqMonitorDAO.getBuID(BUSSINESS_UNIT_SITE);
		return getallErrorLogdata;
	}
	@GetMapping("/testconnection")
	public Map<String, Object> testConnection() {

		List<Map<String, String>> data = objUtil.getBCDatabaseCount(2, "COMPLAINT");
		System.out.println("data count"+data);
		List<Map<String, String>> details = objUtil.getBCDatabaseDetails(2, "COMPLAINT");
//		if (!data.isEmpty()) {
//			for (Map<String, String> map : data) {
//				map.get("DATA_COUNT");
//			}
//		}
		if (!details.isEmpty()) {
			System.out.println("details:"+details);
			for (Map<String, String> map : details) {
				String server_path = map.get("SQL_DRIVER");
				DBUtil.DRIVER_CLASS_CP = server_path;
				DBUtil.SERVER_PATH_CP = map.get("SERVER_PATH");
				DBUtil.SERVER_USER_CP = map.get("SERVER_USER");
				DBUtil.SERVER_PASSWORD_CP = map.get("SERVER_PASSWORD");
			}
		}
//		DBUtil.DRIVER_CLASS_CP = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//		DBUtil.SERVER_PATH_CP = "jdbc:sqlserver://10.2.36.68\\Trans_ExDB;databaseName=ETQ_C;encrypt = true;trustServerCertificate=true";
//		DBUtil.SERVER_USER_CP = "funct_ETQC";
//		DBUtil.SERVER_PASSWORD_CP = "#\"zv&fSk,EM\\";
		Map<String, Object> mapResponse = new HashMap<String, Object>();
		try {
			Connection ClientConnection = DBUtil.getDataSourceClientComp().getConnection();
			Statement Clientstmt = ClientConnection.createStatement();
			ResultSet ClientRS = Clientstmt.executeQuery("SELECT * FROM  XX_COMPLAINT_INTERFACE");
			List<Map<String, String>> pendingData = objUtil.generateResultSetToMap(ClientRS);
			mapResponse.put("Data", pendingData);
			mapResponse.put("CODE", 1);
			mapResponse.put("MESSAGE", "Data Recieved");
			DbUtils.closeQuietly(ClientRS);
			DbUtils.closeQuietly(Clientstmt);
			DbUtils.closeQuietly(ClientConnection);
		} catch (SQLException e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getPendingCompData", "", LOGGER, null, "",
					"ErrorCode :" + String.valueOf(e.getErrorCode()) + " ErrorMessage: " + e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getPendingCompData Data");
		} catch (Exception e) {
			utility.exceptionLog("EtqMonitorDAOImpl", "getPendingCompData", "", LOGGER, null, "", e.getMessage());
			mapResponse.put("CODE", 999);
			mapResponse.put("MESSAGE", "Error in Getting getPendingCompData Data");
		}
		return mapResponse;
	}
}
