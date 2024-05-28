package com.olympus.OEKG.view;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

public class InvoiceDataExcelExport extends AbstractXlsxView {

	ResultSet resultSet = null;
	final Logger LOGGER =LoggerFactory.getLogger(InvoiceDataExcelExport.class);

	@Override
	protected void buildExcelDocument(Map<String, Object> model,
			Workbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws SQLException {

		String fileName = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date date = new Date();
		String file_name = "EMA_DATA_EXTRACT";
		fileName = file_name + "_" + formatter.format(date) + ".xlsx";
		// define excel file name to be exported
		response.addHeader("Content-Disposition", "attachment;fileName="
				+ fileName);

		// read data provided by controller
		@SuppressWarnings("unchecked")
		List<Map<String, Object>> listData = (List<Map<String, Object>>) model
				.get("listData");

		// create one sheet
		Sheet sheet = workbook.createSheet("DATA");
		
		int totalColumn = 0;
		
		Row row0 = sheet.createRow(0);
		
		if (listData.size() !=0) {
			List<String> l = new ArrayList<String>(listData.get(0).keySet());

			totalColumn = l.size();
			
			for (int i = 0; i < totalColumn; i++) {
				row0.createCell(i).setCellValue(l.get(i).replace("_", " "));
			}

			int rownum = 1;

			for (int i = 0; i < listData.size(); i++) {
				totalColumn = listData.get(i).size();
				Row row = sheet.createRow(rownum++);

				for (int y = 0; y < totalColumn; y++) {
					Object columnData = listData.get(i).get(l.get(y));
					String columnDataTest = (columnData == null) ? ""
							: columnData.toString();
					row.createCell(y).setCellValue(columnDataTest.toString());
				}
			}
		
		}
	}
}
