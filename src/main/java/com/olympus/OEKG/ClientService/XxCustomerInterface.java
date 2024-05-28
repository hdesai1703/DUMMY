package com.olympus.OEKG.ClientService;

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

public class XxCustomerInterface {
	
	private utility objUtil = new utility();
    final Logger logger =LoggerFactory.getLogger(XxCustomerInterface.class);

	XxCustomerInterfaceBean objInterfaceBean=new XxCustomerInterfaceBean();
	XxCustomerInterfaceValidation objValidation=new XxCustomerInterfaceValidation();	
	XxCustomerInterface(Connection baseSqlConn,Map<String,String> mapResultSet, String strEtQURL, String strEtQUser, String strSvrCred, String strBusinessName,Connection sqlBUConn,String bUsqldriver,int batch_id,int businessUnit,String businessUnitSystem)
	{
		this.objInterfaceBean.setMapResultSet(mapResultSet);
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
		this.objInterfaceBean.setCustomerUid(mapResultSet.get("1"));
		this.objInterfaceBean.setAccountCode(mapResultSet.get("2"));
		this.objInterfaceBean.setDateFormat(new SimpleDateFormat("MMM dd, yyyy hh:mm a"));		
	}
	
	public String runCustomerInterface()
	{
		String eMsg="SUCCESS";
		try 
    	{
			this.objInterfaceBean=objValidation.validateCustomerFields(this.objInterfaceBean);
			if(this.objInterfaceBean.getErrorMessage()!=null && this.objInterfaceBean.getErrorMessage().length()>0 && this.objInterfaceBean.getErrorMessage().contains("Error: "))
			{
				eMsg=this.objInterfaceBean.getErrorMessage();
				logger.error(eMsg);
				updateCustomerTables(eMsg,"");
			}
			else
			{
				JSONObject parent=getJsonDocument(this.objInterfaceBean.getMapResultSet());
				eMsg=createCustomer(parent);
			}
	    }
    	catch (Exception e) 
    	{
    		eMsg=new String("Error: "+ e.getMessage());
    		logger.error("Java Exception: "+e);
    		updateCustomerTables(eMsg,"");
		}
		return eMsg;
	}

	public void getCustomerContact(String strCustomerDocumentID)
	{
		try
		{
			URL url= new URL(this.objInterfaceBean.getEtQURL()+"/dao/SUPPLIER/SUPPLIER_CONTACTS/where?columns=SUPPLIER_CONTACTS_ID&keys=SUPPLIER_PROFILE_ID&values="+strCustomerDocumentID);
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
					Map<String,String> arrListContactNo=new HashMap<String,String>();
					JSONArray jsonArrayRec=new JSONArray(jsonObj.get("Records").toString());
					for(int i=0;i<jsonArrayRec.length();i++)
					{
						Gson gson= new Gson();
						JSONObject jsonObjCol = new JSONObject(jsonArrayRec.get(i).toString());
						String strColJson=jsonObjCol.get("Columns").toString();
						strColJson=strColJson.replace("[", "");
						strColJson=strColJson.replace("]", "");
						Map<String, String> map = gson.fromJson(strColJson,new TypeToken<HashMap<String, String>>(){}.getType());
						arrListContactNo.put(i+"", map.get("value"));
					}
					this.objInterfaceBean.setMapContactSeqNo(arrListContactNo);//List of Contacts of a Customer
				}
				in.close();
			}
			connection.disconnect();
		}
		catch(Exception ex)
		{
			logger.info(ex.getMessage());
		}
	}
	
	public JSONObject getJsonDocument(Map<String,String> custRes)
	{
		JSONArray childNode = new JSONArray();
		JSONObject parentNode=new JSONObject();
		JSONObject parent=new JSONObject();
		try
		{
			parent.put("Document", parentNode);
			
			parentNode.put("applicationName", "SUPPLIER");
		
			parentNode.put("formName", "SUPPLIER_SUPPLIER_PROFILE");
			parentNode.put("Fields", childNode);
			
			String strCustDocID=getDocumentNumberById("SUPPLIER_PROFILE_ID", "ETQ$NUMBER", custRes.get("19"));
			Map<String, Object> m = new LinkedHashMap<String, Object>(1);
			if(strCustDocID!=null && strCustDocID.length()>0)
			{
				this.objInterfaceBean.setIsCustomerUpdate("Yes");
				parentNode.put("documentId", strCustDocID);
				getCustomerContact(strCustDocID);			
			}
			else
			{
				this.objInterfaceBean.setIsCustomerUpdate("No");
			}
			
			m.put("fieldName","CUSTOMER_ACCOUNT_NUMBER_P");
			JSONObject valueCustAcc=new JSONObject();
			valueCustAcc.put("Value", custRes.get("2"));
			m.put("Values", valueCustAcc);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1);
			m.put("fieldName","SUPPLIER_PROFILE_SUPPLIER_NAME");
			JSONObject valueCust=new JSONObject();
			valueCust.put("Value", custRes.get("3"));
			m.put("Values", valueCust);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","CUSTOMER_NAME_LOCAL_LANGUAGE_P");
			JSONObject valueLocCust=new JSONObject();
			valueLocCust.put("Value", custRes.get("4"));
			m.put("Values", valueLocCust);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","SUPPLIER_PROFILE_CITY");
			JSONObject valueCity=new JSONObject();
			valueCity.put("Value", custRes.get("5"));
			m.put("Values", valueCity);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","CITY_LOCAL_LANGUAGE_P");
			JSONObject valueLocCity=new JSONObject();
			valueLocCity.put("Value", custRes.get("6"));
			m.put("Values", valueLocCity);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","SUPPLIER_PROFILE_ADDRESS");
			JSONObject valueAdd=new JSONObject();
			valueAdd.put("Value", custRes.get("7"));
			m.put("Values", valueAdd);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","ADDRESS_LOCAL_LANGUAGE_P");
			JSONObject valueLocAdd=new JSONObject();
			valueLocAdd.put("Value", custRes.get("8"));
			m.put("Values", valueLocAdd);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","SUPPLIER_SUPPLIER_STATE");
			JSONObject valueState=new JSONObject();
			valueState.put("Value", custRes.get("9"));
			m.put("Values", valueState);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","STATE_PROVINCE_COUNTY_LOCAL_LANGUAGE_P");
			JSONObject valueLocState=new JSONObject();
			valueLocState.put("Value", custRes.get("10"));
			m.put("Values", valueLocState);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","SUPPLIER_PROFILE_PHONE");
			JSONObject valueTel=new JSONObject();
			valueTel.put("Value", custRes.get("11"));
			m.put("Values", valueTel);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","PHONE_LOCAL_LANGUAGE_P");
			JSONObject valueLocTel=new JSONObject();
			valueLocTel.put("Value", custRes.get("12"));
			m.put("Values", valueLocTel);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","SUPPLIER_PROFILE_INACTIVE");
			JSONObject valueInactive=new JSONObject();
			if(custRes.get("12")!=null && custRes.get("12").equalsIgnoreCase("1"))
			{
				valueInactive.put("Value", "Yes");
			}
			else
			{
				valueInactive.put("Value", "No");
			}
			
			m.put("Values", valueInactive);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","SUPPLIER_PROFILE_ZIP");
			JSONObject valueZip=new JSONObject();
			valueZip.put("Value", custRes.get("14"));
			m.put("Values", valueZip);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","SUPPLIER_COUNTRY_P");
			JSONObject valueCountry=new JSONObject();
			valueCountry.put("Value", custRes.get("15"));
			m.put("Values", valueCountry);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","LOCATIONS_P");
			JSONObject valueLoc=new JSONObject();
			valueLoc.put("Value", custRes.get("16"));
			m.put("Values", valueLoc);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","SUPPLIER_PROFILE_FAX");
			JSONObject valueFax=new JSONObject();
			valueFax.put("Value", custRes.get("17"));
			m.put("Values", valueFax);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","DATA_SYSTEM_SOURCE_P");
			JSONObject valueSyso=new JSONObject();
			valueSyso.put("Value", this.objInterfaceBean.getBusinessUnitName());
			m.put("Values", valueSyso);
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","FAX_LOCAL_LANGUAGE_P");
			JSONObject valueLocFax=new JSONObject();
			valueLocFax.put("Value", valueLocFax);
			m.put("Values", custRes.get("18"));
			childNode.put(m);
			
			m = new LinkedHashMap<String, Object>(1); 
			m.put("fieldName","ETQ$AUTHOR");
			JSONObject valueAuthor=new JSONObject();
			valueAuthor.put("Value", this.objInterfaceBean.getEtQUser());
			m.put("Values", valueAuthor);
			childNode.put(m);
			
			this.objInterfaceBean=createContactJson(this.objInterfaceBean);
			JSONObject subFormMap = new JSONObject();
			JSONArray sbRecordNode = new JSONArray();
			sbRecordNode=this.objInterfaceBean.getCustContact();
			
			subFormMap.put("SubformName","SUPPLIER_PROFILE_CONTACTS");
			subFormMap.put("SubformRecords",sbRecordNode);
			JSONArray sbMainNode = new JSONArray();
			sbMainNode.put(subFormMap);
			
			parentNode.put("Subforms", sbMainNode);
			
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		return parent;
	}
	
	public String createCustomer(JSONObject jsonString)
	{
		String eMsg="SUCCESS";
		 try
		 {
			URL url = new URL(this.objInterfaceBean.getEtQURL()+"/documents");

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			connection.setDoInput(true);

			if (this.objInterfaceBean.getIsCustomerUpdate() != null && this.objInterfaceBean.getIsCustomerUpdate().equalsIgnoreCase("Yes")) {
				connection.setRequestMethod("PUT");
				eMsg = "Customer updated successfully";
			} else {
				connection.setRequestMethod("POST");
				eMsg = "Customer created successfully";
			}

			String encoded = Base64.getEncoder().encodeToString((this.objInterfaceBean.getServerCred()).getBytes(StandardCharsets.UTF_8));

			connection.setRequestProperty("Authorization", "Basic " + encoded);

			OutputStream os = connection.getOutputStream();
			os.write(jsonString.toString().getBytes("UTF-8"));
			os.close();
			
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
					String strDocumentNumber=getDocumentNumberById("ETQ$NUMBER", "SUPPLIER_PROFILE_ID", map.get("documentId"));	
					System.out.println("strDocumentNumber and map: " + strDocumentNumber + " " + map.toString());
					logger.info("Customer Number: "+strDocumentNumber+" & Customer Document ID: "+map.get("documentId"));
					updateCustomerTables(eMsg,strDocumentNumber);						
				}
				else
				{
					eMsg="Error: "+map.get("Messages");
					logger.error(eMsg);
					updateCustomerTables(eMsg,"");						
				}
			}
			else
			{
				eMsg="Error: Unable to Connect with EtQ System!";
				logger.error(eMsg);
				updateCustomerTables(eMsg,"");						
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
	
	public String getFormatedDate(String strDate) throws ParseException
	{
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
	
	public XxCustomerInterfaceBean createContactJson(XxCustomerInterfaceBean objInterfaceBean)		
	{
		this.objInterfaceBean=objInterfaceBean;
		JSONArray contactSbRecordNode = new JSONArray();
		
		try
		{
			Statement statement = this.objInterfaceBean.getBusinessUnitConnection().createStatement();
			String sql="SELECT U_ID,CUST_U_ID,CONTACT_NAME,TITLE,PHONE,CELL,EMAIL,INACTIVE FROM XX_ETQ_CUSTOMER_CONTACT_DETAIL "
					+ "WHERE CUST_U_ID="+this.objInterfaceBean.getCustomerUid()+" AND (ETQ_UPLOAD_STATUS=1 OR ETQ_UPLOAD_STATUS IS NULL OR ETQ_UPLOAD_STATUS=0)";
			
			ResultSet rs = statement.executeQuery(sql.toString());
			if(rs!=null && rs.isBeforeFirst())
			{
				int i=0;
				Map<String,String> mapContactUid=new HashMap<String,String>();
				Map<String,String> mapContactName=new HashMap<String,String>();
				while(rs.next())
				{				
					JSONArray contactSBChildNode = new JSONArray();
					JSONObject contactSbRObj=new JSONObject();
					
					mapContactUid.put(i+"", rs.getString(1));
					Map<String, Object> contactSbFields = new LinkedHashMap<String, Object>(1);
					if(this.objInterfaceBean.getIsCustomerUpdate().equalsIgnoreCase("Yes"))
					{
						contactSbRObj.put("recordId", this.objInterfaceBean.getMapContactSeqNo().get(i+""));
					}
					else
					{
						contactSbRObj.put("recordId", "");
					}
					contactSbRObj.put("recordOrder", i);			
										
					contactSbFields.put("fieldName","SUPPLIER_CONTACTS_CONTACT_NAME");
					JSONArray contNameValObj = new JSONArray();
					contNameValObj.put(rs.getString(3));
					mapContactName.put(i+"", rs.getString(3));
					contactSbFields.put("Values", contNameValObj);
					contactSBChildNode.put(contactSbFields);
					
					contactSbFields = new LinkedHashMap<String, Object>(1);
					contactSbFields.put("fieldName","SUPPLIER_CONTACTS_TITLE");
					JSONArray contTitleValObj = new JSONArray();
					contTitleValObj.put(rs.getString(4));
					contactSbFields.put("Values", contTitleValObj);
					contactSBChildNode.put(contactSbFields);
					
					contactSbFields = new LinkedHashMap<String, Object>(1);
					contactSbFields.put("fieldName","SUPPLIER_CONTACTS_PHONE");
					JSONArray contPhoneValObj = new JSONArray();
					contPhoneValObj.put(rs.getString(5));
					contactSbFields.put("Values", contPhoneValObj);
					contactSBChildNode.put(contactSbFields);
					
					contactSbFields = new LinkedHashMap<String, Object>(1);
					contactSbFields.put("fieldName","SUPPLIER_CONTACTS_CELL");
					JSONArray contCellValObj = new JSONArray();
					contCellValObj.put(rs.getString(6));
					contactSbFields.put("Values", contCellValObj);
					contactSBChildNode.put(contactSbFields);
					
					contactSbFields = new LinkedHashMap<String, Object>(1);
					contactSbFields.put("fieldName","SUPPLIER_CONTACTS_EMAIL");
					JSONArray contEmailValObj = new JSONArray();
					contEmailValObj.put(rs.getString(7));
					contactSbFields.put("Values", contEmailValObj);
					contactSBChildNode.put(contactSbFields);
					
					contactSbFields = new LinkedHashMap<String, Object>(1);
					contactSbFields.put("fieldName","INACTIVE_P");
					JSONArray contInactiveValObj = new JSONArray();
					contInactiveValObj.put(getYesNo(rs.getString(8)));
					contactSbFields.put("Values", contInactiveValObj);
					contactSBChildNode.put(contactSbFields);
					
					contactSbRObj.put("Fields", contactSBChildNode);
					
					contactSbRecordNode.put(contactSbRObj);
					i++;
				}
				this.objInterfaceBean.setMapContactUid(mapContactUid);
				this.objInterfaceBean.setMapContactName(mapContactName);
			}
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
		}
		this.objInterfaceBean.setCustContact(contactSbRecordNode);
		return this.objInterfaceBean;
	}
	
	public String getDocumentNumberById(String strSelectedColumn,String strRequiredColumn,String strRequiredColumnVal)
	{
		String strDocumentNumber="";
		try
		{
			URL url= new URL(this.objInterfaceBean.getEtQURL()+"/dao/SUPPLIER/SUPPLIER_PROFILE/where?columns="+strSelectedColumn+"&keys="+strRequiredColumn+"&values="+strRequiredColumnVal);
			
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
	
	public void updateCustomerTables(String eMsg,String strDocumentNumber)
	{
		try
		{
			String strBusinessUnitDriver=this.objInterfaceBean.getBusinessUnitDriver();
			int batch_id=this.objInterfaceBean.getBatchId();
			int businessUnit=this.objInterfaceBean.getBusinessUnitID();
			
			String updateCustomerTbl=null;
			String updateContactTbl=null;
			eMsg=eMsg.replaceAll("'", "");
			
			
			
			Date date = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			String strUID=this.objInterfaceBean.getCustomerUid();
			String strCustAccCode=this.objInterfaceBean.getAccountCode();
			String strDataSystemSource = this.objInterfaceBean.getBusinessUnitName();
			
			if(eMsg.contains("Error: "))
			{
				objUtil.dDebug(1, ft.format(date), eMsg, strUID, null,
						"CUSTOMER", "CUSTOMER", 0, strCustAccCode, 
						strDataSystemSource, null, null, 3, businessUnit, batch_id);
				
				if(strBusinessUnitDriver.equals("oracle.jdbc.driver.OracleDriver"))
		        {
					updateCustomerTbl="UPDATE XX_ETQ_CUSTOMER_PROFILE_DETAIL SET ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='"+eMsg+"',UPLOADED_TO_ETQ_DATE=TO_DATE('"+ft.format(date)+"','YYYY-MM-DD HH24:MI:SS'),ETQ_BATCH_ID="+batch_id+" WHERE U_ID='"+strUID+"' AND (ETQ_BATCH_ID=-1 OR ETQ_BATCH_ID="+batch_id+")";
					updateContactTbl="UPDATE XX_ETQ_CUSTOMER_CONTACT_DETAIL SET ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='"+eMsg+"',UPLOADED_TO_ETQ_DATE=TO_DATE('"+ft.format(date)+"','YYYY-MM-DD HH24:MI:SS'),ETQ_BATCH_ID="+batch_id+" WHERE CUST_U_ID="+strUID; 
		         }
				else
				{	
					updateCustomerTbl="UPDATE XX_ETQ_CUSTOMER_PROFILE_DETAIL SET ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='"+eMsg+"',UPLOADED_TO_ETQ_DATE='"+ft.format(date)+"',ETQ_BATCH_ID="+batch_id+" WHERE U_ID='"+strUID+"' AND (ETQ_BATCH_ID=-1 OR ETQ_BATCH_ID="+batch_id+")";
					updateContactTbl="UPDATE XX_ETQ_CUSTOMER_CONTACT_DETAIL SET ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='"+eMsg+"',UPLOADED_TO_ETQ_DATE='"+ft.format(date)+"',ETQ_BATCH_ID="+batch_id+" WHERE CUST_U_ID="+strUID;
				}
			}
			else
			{
				String strCustMsg="",strContMsg="";
				if(this.objInterfaceBean.getIsCustomerUpdate()!=null && this.objInterfaceBean.getIsCustomerUpdate().equalsIgnoreCase("Yes"))
				{
					strCustMsg="Customer updated successfully";
					strContMsg="Contact updated successfully";
				}
				else
				{
					strCustMsg="Customer created successfully";
					strContMsg="Contact created successfully";
				}

				objUtil.dDebug(1, ft.format(date), strCustMsg, strUID, strDocumentNumber,
						"CUSTOMER", "CUSTOMER", 0, strCustAccCode, 
						strDataSystemSource, null, null, 4, businessUnit, batch_id);							
				
				if(this.objInterfaceBean.getMapContactUid()!=null && this.objInterfaceBean.getMapContactUid().size()>0 && this.objInterfaceBean.getMapContactSeqNo() == null)
				{
					for (int i = 0; i < this.objInterfaceBean.getMapContactUid().size(); i++) {
						objUtil.dDebug(1, ft.format(date), strContMsg, this.objInterfaceBean.getMapContactUid().get(i+""),
								strDocumentNumber, "CUSTOMER", "CONTACT", Long.parseLong(strUID),
								strCustAccCode, strDataSystemSource, null, null, 4,
								businessUnit, batch_id);
					}					
				}else if(this.objInterfaceBean.getMapContactUid()!=null && this.objInterfaceBean.getMapContactUid().size()>0)
				{		
						System.out.println("Contact Test: "+ this.objInterfaceBean.getMapContactSeqNo());
						for(int i=0;i<this.objInterfaceBean.getMapContactUid().size();i++)
						{							
//							objUtil.dDebug(1, ft.format(date), strContMsg, strUID, strDocumentNumber,
//									"CUSTOMER", "CONTACT", 0, strCustAccCode, 
//									strDataSystemSource, null, null, 4, businessUnit, batch_id);
							objUtil.dDebug(1, ft.format(date), strContMsg, this.objInterfaceBean.getMapContactUid().get(i+""),
									strDocumentNumber, "CUSTOMER", "CONTACT", Long.parseLong(strUID),
									strCustAccCode, strDataSystemSource, null, null, 4,
									businessUnit, batch_id);
						}
				}
	    		if(strBusinessUnitDriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver"))
	            {
	    			updateCustomerTbl="update XX_ETQ_CUSTOMER_PROFILE_DETAIL set ETQ_BATCH_ID="+batch_id +",ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='"+strCustMsg+"',UPLOADED_TO_ETQ_DATE=TO_DATE('"+ft.format(date)+"','YYYY-MM-DD HH24:MI:SS'),ETQ_CUSTOMER_NUMBER= '"+strDocumentNumber+"' WHERE U_ID='"+strUID+"' AND (ETQ_BATCH_ID=-1 OR ETQ_BATCH_ID="+batch_id+")";
	    			updateContactTbl="update  XX_ETQ_CUSTOMER_CONTACT_DETAIL set  ETQ_BATCH_ID="+batch_id +", ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='"+strContMsg+"',UPLOADED_TO_ETQ_DATE=TO_DATE('"+ft.format(date)+"','YYYY-MM-DD HH24:MI:SS'),ETQ_CUSTOMER_NUMBER= '"+strDocumentNumber+"' WHERE CUST_U_ID="+strUID;
	            	
	            }
	    		else
	    		{
	    			updateCustomerTbl="update XX_ETQ_CUSTOMER_PROFILE_DETAIL set ETQ_BATCH_ID="+batch_id +",ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='"+strCustMsg+"',UPLOADED_TO_ETQ_DATE='" + ft.format(date) + "',ETQ_CUSTOMER_NUMBER= '"+strDocumentNumber+"' WHERE U_ID='"+strUID+"' AND (ETQ_BATCH_ID=-1 OR ETQ_BATCH_ID="+batch_id+")";
	    			updateContactTbl="update  XX_ETQ_CUSTOMER_CONTACT_DETAIL set  ETQ_BATCH_ID="+batch_id +", ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='"+strContMsg+"',UPLOADED_TO_ETQ_DATE='" + ft.format(date) + "',ETQ_CUSTOMER_NUMBER= '"+strDocumentNumber+"' WHERE CUST_U_ID="+strUID;
	    		}
			}
			
			Statement updateCompTbl = this.objInterfaceBean.getBusinessUnitConnection().createStatement();        
	        updateCompTbl.executeUpdate(updateCustomerTbl);
	        updateCompTbl.executeUpdate(updateContactTbl);
	        if(strBusinessUnitDriver.equals("oracle.jdbc.driver.OracleDriver"))
	        {			 
	        	updateCompTbl.executeUpdate("commit");
	        }
		}
		catch(Exception er)
		{
			logger.error("System Error:",er);
		}
	}
}