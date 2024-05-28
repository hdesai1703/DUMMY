package com.olympus.OEKG.AddRefService;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.olympus.OEKG.Utility.utility;

public class XxAdditionalReferenceInterface {
	
    final Logger logger =LoggerFactory.getLogger(XxAdditionalReferenceInterface.class);


	private static utility objUtil = new utility();

	
	XxAdditionalReferenceInterfaceBean objInterfaceBean=new XxAdditionalReferenceInterfaceBean();
	XxAdditionalReferenceInterface(Connection baseSqlConn,ResultSet rs, String strEtQURL, String strEtQUser, String strSvrCred, String strBusinessName,Connection sqlBUConn,String bUsqldriver, int batch_id, int businessUnit,String businessUnitSystem)
	{
		this.objInterfaceBean.setResultSet(rs);
		this.objInterfaceBean.setEtQURL(strEtQURL);
		this.objInterfaceBean.setEtQUser(strEtQUser);
		this.objInterfaceBean.setBusinessUnitName(strBusinessName);
		this.objInterfaceBean.setBusinessUnitConnection(sqlBUConn);
		this.objInterfaceBean.setBusinessUnitDriver(bUsqldriver);
		this.objInterfaceBean.setServerCred(strSvrCred);		
		this.objInterfaceBean.setBatchId(batch_id);
		this.objInterfaceBean.setServerName(businessUnitSystem);
		this.objInterfaceBean.setBusinessUnitID(businessUnit);
		this.objInterfaceBean.setBaseConnection(baseSqlConn);
		this.objInterfaceBean.setDateFormat(new SimpleDateFormat("MMM dd, yyyy hh:mm a"));
	}
	
	public String runAdditionalReferenceInterface()
	{
		try 
    	{
			ResultSet rs=this.objInterfaceBean.getResultSet();
			while(rs.next())
			{
				this.objInterfaceBean.setAdditionalReferenceUid(rs.getString(1));
				String strShiseiNumber=rs.getString(2);
				if(strShiseiNumber!=null && strShiseiNumber.length()>0)
				{
					this.objInterfaceBean.setShiseiNumber(strShiseiNumber);
					JSONObject parent=getJsonDocument(strShiseiNumber,rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),
							rs.getString(7),rs.getString(8),rs.getString(9));
					this.objInterfaceBean.setBatchId(rs.getInt(10));
					createAdditionalReference(parent);
				}
				else
				{
					updateAdditionalReferenceTables("Error: Shisei Number is required.", "");
				}
			}
	    }
    	catch (Exception e) 
    	{
    		logger.error("Java Exception: "+e);
    		updateAdditionalReferenceTables("Error: "+e.getMessage(),"");
		}
		return this.objInterfaceBean.getErrorMessage();
	}

	public JSONObject getJsonDocument(String strShiseiNo,String strReportedDate,String strReporter,String strFacilityCode,String strFacilityName,
			String strItemCode,String strModelNumber,String strDocumentNumber)
	{
		JSONArray childNode = new JSONArray();
		JSONObject parentNode=new JSONObject();
		JSONObject parent=new JSONObject();
		try
		{
			parent.put("Document", parentNode);
			
			parentNode.put("applicationName", "SUPPLIER");
		
			parentNode.put("formName", "ADDITIONAL_REFERENCE_NUMBER_P");
			parentNode.put("Fields", childNode);
			
			String strArDocID=getDocumentNumberById("ADDITI_REFERE_NUM_ID", "ETQ$NUMBER", strDocumentNumber);
			Map<String, Object> m = new LinkedHashMap<String, Object>(1);
			if(strArDocID!=null && strArDocID.length()>0)
			{
				this.objInterfaceBean.setIsUpdate("Yes");
				parentNode.put("documentId", strArDocID);
			}
			else
			{
				this.objInterfaceBean.setIsUpdate("No");
			}
			
			m.put("fieldName","SHISEI_NUMBER_P");
			JSONObject valueShiseiNum=new JSONObject();
			valueShiseiNum.put("Value", strShiseiNo);
			m.put("Values", valueShiseiNum);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1);
			m.put("fieldName","REPORTER_P");
			JSONObject valueReporte=new JSONObject();
			valueReporte.put("Value", strReporter);
			m.put("Values", valueReporte);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","REPORTED_DATE_P");
			JSONObject valueReportedDate=new JSONObject();
			valueReportedDate.put("Value", getFormatedDate(strReportedDate));
			m.put("Values", valueReportedDate);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","FACILITY_CODE_P");
			JSONObject valueFacilityCode=new JSONObject();
			valueFacilityCode.put("Value", strFacilityCode);
			m.put("Values", valueFacilityCode);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","FACILITY_NAME_P");
			JSONObject valueFacilityName=new JSONObject();
			valueFacilityName.put("Value", strFacilityName);
			m.put("Values", valueFacilityName);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","ITEM_CODE_P");
			JSONObject valueItemCode=new JSONObject();
			valueItemCode.put("Value", strItemCode);
			m.put("Values", valueItemCode);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","MODEL_NAME_P");
			JSONObject valueModelName=new JSONObject();
			valueModelName.put("Value", strModelNumber);
			m.put("Values", valueModelName);
			childNode.put(m);
		
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		return parent;
	}
	
	public String createAdditionalReference(JSONObject jsonString)
	{
		String eMsg="SUCCESS";
		 try
		 {
			URL url=new URL(this.objInterfaceBean.getEtQURL()+"/documents");
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			if(this.objInterfaceBean.getIsUpdate()!=null && this.objInterfaceBean.getIsUpdate().equalsIgnoreCase("Yes"))
			{
				connection.setRequestMethod("PUT");
			}
			else 
			{
				connection.setRequestMethod("POST");
			}
			
			String encoded = Base64.getEncoder().encodeToString((this.objInterfaceBean.getServerCred()).getBytes(StandardCharsets.UTF_8)); 
			
			connection.setRequestProperty("Authorization", "Basic "+encoded);
			
			OutputStream os = connection.getOutputStream();
	        os.write(jsonString.toString().getBytes("UTF-8"));
	        os.close();
	
	        // read the response
	        int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
		        InputStream in = new BufferedInputStream(connection.getInputStream());
		        
		        BufferedReader br = new BufferedReader(new InputStreamReader((in)));
				StringBuilder sb = new StringBuilder();
				String output;
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
				br.close();
				in.close();
				
				Gson gson= new Gson();
				String strSb=sb.toString();
				strSb=strSb.replaceAll("\\[", "");
				strSb=strSb.replaceAll("\\]", "");
				Map<String, String> map = gson.fromJson(strSb,new TypeToken<HashMap<String, String>>(){}.getType());
				
				if(map.get("Messages").contains("Operation done successfully"))
				{
					String strDocumentNumber=getDocumentNumberById("ETQ$NUMBER", "ADDITI_REFERE_NUM_ID", map.get("documentId"));
					logger.info("Additional Reference Number: "+strDocumentNumber+" & Additional Reference Document ID: "+map.get("documentId"));
					updateAdditionalReferenceTables(eMsg,strDocumentNumber);						
				}
				else
				{
					eMsg="Error: "+map.get("Messages");
					logger.error(eMsg);
					updateAdditionalReferenceTables(eMsg,"");						
				}
			}
			else
			{
				eMsg="Error: Unable to Connect with EtQ System!";
				logger.error(eMsg);
				updateAdditionalReferenceTables(eMsg,"");						
			}
			connection.disconnect();
		}
		catch(Exception e)
		{
			eMsg="Error: "+e.getMessage();
			logger.error(e.getMessage());
		}
		return eMsg;
	}
	
	public String getFormatedDate(String strDate) throws ParseException	{
		Date formateDate=null;
		if (strDate!=null)
		{
			if(strDate.length()>10)
			{
				formateDate=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(strDate);
			}
			else
			{
				formateDate=new SimpleDateFormat("yyyy-MM-dd").parse(strDate);	
			}
			return this.objInterfaceBean.getDateFormat().format(formateDate);
		}
		else
		{
			return null;
		}
	}
	
	public String getYesNo(String strValue)
	{
		String strAnswer="";
		if(strValue!=null && strValue.length()>0)
		{
			if(strValue.equalsIgnoreCase("Y") || strValue.equalsIgnoreCase("Yes"))
			{
				strAnswer="Yes";
			}
			else if(strValue.equalsIgnoreCase("N") || strValue.equalsIgnoreCase("No"))
			{
				strAnswer="No";
			}
		}
		else
		{
			strAnswer="No";
		}
		return strAnswer;
	}	
	
	public String getDocumentNumberById(String strSelectedColumn,String strRequiredColumn,String strRequiredColumnVal)
	{
		String strDocumentNumber="";
		try
		{
			URL url= new URL(this.objInterfaceBean.getEtQURL()+"/dao/SUPPLIER/ADDITIO_REFEREN_NUMB/where?columns="+strSelectedColumn+"&keys="+strRequiredColumn+"&values="+strRequiredColumnVal);
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			String encoded = Base64.getEncoder().encodeToString((this.objInterfaceBean.getServerCred()).getBytes(StandardCharsets.UTF_8)); 
			connection.setRequestProperty("Authorization", "Basic "+encoded);
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				
				JSONObject jsonObj = new JSONObject(response.toString());
				if(Integer.parseInt(jsonObj.get("count").toString())>0)
				{
					String strRec=jsonObj.get("Records").toString();
					JSONObject jsonRecObj = new JSONObject(strRec.substring(1,strRec.length()-1));
					
					JSONObject jsonColObj = new JSONObject(jsonRecObj.toString());
					String strCol=jsonColObj.get("Columns").toString();
					JSONObject jsonValObj = new JSONObject(strCol.substring(1,strCol.length()-1));
				
					strDocumentNumber=jsonValObj.get("value").toString();
				}
				in.close();
			}
			connection.disconnect();
		}
		catch(Exception ex)
		{
			logger.info(ex.getMessage());
		}
		return strDocumentNumber;
	}
	
	public String getCurrentDate()
	{
	    Date date = new Date();  
	    SimpleDateFormat currDtFormat = new SimpleDateFormat("yyyy-MM-dd");
	    return currDtFormat.format(date);  
	}
	
	public void updateAdditionalReferenceTables(String eMsg,String strDocumentNumber)
	{
		try
		{
			String strBusinessUnitDriver=this.objInterfaceBean.getBusinessUnitDriver();
			int batch_id=this.objInterfaceBean.getBatchId();
			String strUID=this.objInterfaceBean.getAdditionalReferenceUid();
			String strShieseiNumber=this.objInterfaceBean.getShiseiNumber();
			int businessUnit=this.objInterfaceBean.getBusinessUnitID();
			String strDataSystemSource=this.objInterfaceBean.getBusinessUnitName();
			String businessUnitSystem=this.objInterfaceBean.getServerName();
			String debugTbl=this.objInterfaceBean.getDebugTbl();
			Date date = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String updateAdditionalRefTbl=null;
			eMsg=eMsg.replaceAll("'", "");
			if(eMsg.contains("Error: "))
			{
//				etQDebug.dDebug(1,"D","SUPPLIER",eMsg,strUID,"ADDITIONAL REFERENCE NUMBER","","0",
//						strShieseiNumber,3,businessUnit,batch_id,businessUnitSystem,debugTbl);
				objUtil.dDebug(1, ft.format(date), eMsg, strUID, null,
						"ADDITIONAL REFERENCE", "ADDITIONAL REFERENCE", 0, strShieseiNumber, 
						strDataSystemSource, null, null, 3, businessUnit, batch_id);
				
				if(strBusinessUnitDriver.equals("oracle.jdbc.driver.OracleDriver"))
		        {
					updateAdditionalRefTbl="UPDATE XX_OT_CUST_REFERENCE_NUM SET ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='"+eMsg+"',UPLOADED_TO_ETQ_DATE=TO_DATE('"+ft.format(date)+"','YYYY-MM-DD HH24:MI:SS'),ETQ_BATCH_ID="+batch_id+" WHERE U_ID='"+strUID+"' AND (ETQ_BATCH_ID=-1 OR ETQ_BATCH_ID="+batch_id+")";
		        }
				else
				{	
					updateAdditionalRefTbl="UPDATE XX_OT_CUST_REFERENCE_NUM SET ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='"+eMsg+"',UPLOADED_TO_ETQ_DATE='"+ft.format(date)+"',ETQ_BATCH_ID="+batch_id+" WHERE U_ID='"+strUID+"' AND (ETQ_BATCH_ID=-1 OR ETQ_BATCH_ID="+batch_id+")";
				}
			}
			else
			{
				String strMsg="";
				if(this.objInterfaceBean.getIsUpdate().equalsIgnoreCase("Yes"))
				{
					strMsg="Additional Reference Number Document updated successfully";
				}
				else
				{
					strMsg="Additional Reference Number Document created successfully";
				}
				
				objUtil.dDebug(1, ft.format(date), strMsg, strUID, strDocumentNumber,
						"ADDITIONAL REFERENCE", "ADDITIONAL REFERENCE", 0, strShieseiNumber, 
						strDataSystemSource, null, null, 4, businessUnit, batch_id);	
				
				
	    		if(strBusinessUnitDriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver"))
	            {
	    			updateAdditionalRefTbl="update XX_OT_CUST_REFERENCE_NUM set ETQ_BATCH_ID="+batch_id +",ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='"+strMsg+"',UPLOADED_TO_ETQ_DATE=TO_DATE('"+ft.format(date)+"','YYYY-MM-DD HH24:MI:SS'),ETQ_COMPLAINT_NUMBER= '"+strDocumentNumber+"' WHERE U_ID='"+strUID+"' AND (ETQ_BATCH_ID=-1 OR ETQ_BATCH_ID="+batch_id+")";
	    		}
	    		else
	    		{
	    			updateAdditionalRefTbl="update XX_OT_CUST_REFERENCE_NUM set ETQ_BATCH_ID="+batch_id +",ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='"+strMsg+"',UPLOADED_TO_ETQ_DATE='" + ft.format(date) + "',ETQ_COMPLAINT_NUMBER= '"+strDocumentNumber+"' WHERE U_ID='"+strUID+"' AND (ETQ_BATCH_ID=-1 OR ETQ_BATCH_ID="+batch_id+")";
	    		}
			}
			
			Statement updateCompTbl = this.objInterfaceBean.getBusinessUnitConnection().createStatement();        
	        updateCompTbl.executeUpdate(updateAdditionalRefTbl);
	        if(strBusinessUnitDriver.equals("oracle.jdbc.driver.OracleDriver"))
	        {			 
	        	updateCompTbl.executeUpdate("commit");
	        }
		}
		catch(Exception er)
		{
			logger.error("Error: ",er);
		}
	}
}