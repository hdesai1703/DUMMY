package com.olympus.OEKG.ClientService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XxCustomerInterfaceValidation {
	
    final Logger logger =LoggerFactory.getLogger(XxCustomerInterfaceValidation.class);

	XxCustomerInterfaceBean objCustomerInterfaceBean = new XxCustomerInterfaceBean();
	public XxCustomerInterfaceBean validateCustomerFields(XxCustomerInterfaceBean objCustomerInterfaceBean)
	{
		this.objCustomerInterfaceBean=objCustomerInterfaceBean;
		String strRtnStatus = null;
		String strErrorMsg ="";
		
		Map<String, String> mapCustomer = new HashMap<String, String>();
		mapCustomer = this.objCustomerInterfaceBean.getMapResultSet();
		
		strRtnStatus = validateCustomerAccCode(mapCustomer.get("2"));
		if (strRtnStatus != null &&  strRtnStatus.length() > 0)
		{
			if(strRtnStatus.contains("Error: "))
			{
				if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000)
				{
					strErrorMsg = strErrorMsg +" || "+ strRtnStatus; //
				}
			}	
		}
		
		strRtnStatus = validateCustomerName(mapCustomer.get("3"));
		if (strRtnStatus != null && strRtnStatus.length() > 0)
		{
			if(strRtnStatus.contains("Error: "))
			{
				if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000)
				{
					strErrorMsg = strErrorMsg +" || "+ strRtnStatus;
				}
			}	
		}
		
		strRtnStatus = validateCustomerLocation(mapCustomer.get("16"));
		if (strRtnStatus != null)
		{
			if(strRtnStatus.contains("Error: "))
			{
				if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000)
				{
					strErrorMsg = strErrorMsg +" || "+ strRtnStatus;
				}
			}
			else
			{
				this.objCustomerInterfaceBean.setCustomerLocation(strRtnStatus);
			}
		}
		
		strRtnStatus = validateSystemDataSource(this.objCustomerInterfaceBean.getBusinessUnitName());
		if (strRtnStatus != null)
		{
			if(strRtnStatus.contains("Error: "))
			{
				if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000)
				{
					strErrorMsg = strErrorMsg +" || "+ strRtnStatus;
				}
			}
			else
			{
				this.objCustomerInterfaceBean.setBusinessUnitName(strRtnStatus);
			}
		}
		
		strRtnStatus = validateCustomerCountry(mapCustomer.get("15"));
		if (strRtnStatus != null)
		{
			if(strRtnStatus.contains("Error: "))
			{
				if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000)
				{
					strErrorMsg = strErrorMsg +" || "+ strRtnStatus;
				}
			}
			else
			{
				this.objCustomerInterfaceBean.setCustomerCountry(strRtnStatus);
		
				strRtnStatus = validateCustomerState(mapCustomer.get("9"),this.objCustomerInterfaceBean.getCustomerCountry());
				if (strRtnStatus != null)
				{
					if(strRtnStatus.contains("Error: "))
					{
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000)
						{
							strErrorMsg = strErrorMsg +" || "+ strRtnStatus;
						}
					}
					else
					{
						this.objCustomerInterfaceBean.setCustomerState(strRtnStatus);
					}
				}
			}
		}
		
		strRtnStatus = validateContactName();
		if (strRtnStatus != null)
		{
			if(strRtnStatus.contains("Error: "))
			{
				if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000)
				{
					strErrorMsg = strErrorMsg +" || "+ strRtnStatus;
				}
			}
		}
		
		if (strErrorMsg != null && strErrorMsg.length() > 0)
		{
			this.objCustomerInterfaceBean.setErrorMessage(strErrorMsg.substring(4));
		}
		
		return this.objCustomerInterfaceBean;
	}
	
	//Set the Connection with Webservice
	public StringBuffer getConnection(URL url) 
	{
		StringBuffer response = new StringBuffer();
		try 
		{
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			String encoded = Base64.getEncoder().encodeToString((this.objCustomerInterfaceBean.getServerCred()).getBytes(StandardCharsets.UTF_8));
			connection.setRequestProperty("Authorization", "Basic " + encoded);
			int responseCode = connection.getResponseCode();
			
			if (responseCode == HttpURLConnection.HTTP_OK) // success
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) 
				{
					response.append(inputLine);
				}

				connection.disconnect();
				in.close();
			}else{
				response.append("Invalid URL");
			}
		} 
		catch (Exception ex)
		{
			logger.error("Error: ",ex);
			this.objCustomerInterfaceBean.setErrorMessage("Error: "+ex.getMessage());
		}
		
		return response;
	}

	//Validate Customer Account Code
	public String validateCustomerAccCode(String strCustomerAccCode)
	{
		String strErrorMsg = null;
		
		if (strCustomerAccCode == null || strCustomerAccCode.length() == 0)
		{
			strErrorMsg ="Error: Customer Account Code can not be null.";
		}
		return strErrorMsg;
	}
	
	//Validate Customer Name Code
	public String validateCustomerName(String strCustomerName)
	{
		String strErrorMsg = null;
		
		if (strCustomerName == null || strCustomerName.length() == 0)
		{
			strErrorMsg ="Error: Customer Name can not be null.";
		}
		return strErrorMsg;		
	}
	
	//Validate Customer Location
	public String validateCustomerLocation(String strCustomerLocation)
	{
		String strStatus = null;
		String strErrorMsg = null;
		URL url=null;
		
		if (strCustomerLocation == null || strCustomerLocation.length() == 0)
		{
			strErrorMsg ="Error: Customer Location can not be null.";
		}
		else 
		{
			try
			{		
				strCustomerLocation = strCustomerLocation.replaceAll(" ","%20");
				url = new URL(this.objCustomerInterfaceBean.getEtQURL()+"/dao/DATACENTER/LOCATION_PROFILE/where?columns=DISPLAY_NAME&keys=DISPLAY_NAME&values="+strCustomerLocation);
			
				if (url != null) 
				{
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;
					
					if (response != null && response.length() > 0)
					{
						jsonObj = new JSONObject(response.toString());
						cntRec = Integer.parseInt(jsonObj.get("count").toString());
						
						if (cntRec == 0) 
						{
							strErrorMsg = "Error: Customer Location - Display value(s): [" + strCustomerLocation.replaceAll("%20", " ")+ "] are not listed in the field's keyword options ";
						}
						else
						{
							String strRec = jsonObj.get("Records").toString();
							JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));
							
							String strCol = jsonRecObj.get("Columns").toString();
							JSONObject jsonColObj = new JSONObject(strCol.substring(1,strCol.length() -1));
							
							strCustomerLocation= jsonColObj.get("value").toString();
						}
					}	
				}	
			}
			catch (Exception ex)
			{
				logger.error("Error: ",ex);
				this.objCustomerInterfaceBean.setErrorMessage("Error: "+ex.getMessage());
			}
		}	
		if (strCustomerLocation != null && strErrorMsg == null)
		{
			strStatus = strCustomerLocation;
		}
		else
		{
			strStatus = strErrorMsg;
		}
	
		return strStatus;
	}
	
	//Validate Command Result for the System Data Source
	public String validateSystemDataSource(String strSystemDataSource)
	{
		String strStatus = null;
		URL url = null;
		
		if (strSystemDataSource == null || strSystemDataSource.length() == 0)
		{
			strStatus ="Error: Data System Source can not be null.";
		}
		else
		{
			try
			{		
				strSystemDataSource = strSystemDataSource.replaceAll(" ","%20");
				url = new URL(this.objCustomerInterfaceBean.getEtQURL()+"/dao/LOOKUPS/DATA_SYSTEM_SOURCE/where?columns=DESCRIPTION&keys=DESCRIPTION&values="+strSystemDataSource);
			
				if (url != null) 
				{
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;
					
					if (response != null && response.length() > 0)
					{
						jsonObj = new JSONObject(response.toString());
						cntRec = Integer.parseInt(jsonObj.get("count").toString());
						
						if (cntRec == 0) 
						{
							strStatus = "Error: Data System Source - Display value(s): [" + strSystemDataSource.replaceAll("%20", " ")+ "] are not listed in the field's keyword options ";
						}
						else
						{
							String strRec = jsonObj.get("Records").toString();
							JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));
							
							String strCol = jsonRecObj.get("Columns").toString();
							JSONObject jsonColObj = new JSONObject(strCol.substring(1,strCol.length() -1));
							
							strStatus= jsonColObj.get("value").toString();
						}
					}	
				}	
			}
			catch (Exception ex)
			{
				logger.error("Error: ",ex);
				this.objCustomerInterfaceBean.setErrorMessage("Error: "+ex.getMessage());
			}
		}
		return strStatus;
	}
	
	// Validate Customer Country
	public String validateCustomerCountry(String strCusCountry) 
	{
		String strStatus = null;
		String strErrorMsg = null;
		String strCountry=null;
		URL url = null;

		if (strCusCountry != null && strCusCountry.length() == 0) 
		{
			strCusCountry = null;
		}

		if (strCusCountry == null) 
		{
			strErrorMsg = "Error: Customer Country is required field.";
		} 
		else 
		{
			try 
			{
				if (strCusCountry.length() > 0) 
				{
					strCusCountry = strCusCountry.replaceAll(" ", "%20");
					url = new URL(this.objCustomerInterfaceBean.getEtQURL() + "/dao/LOOKUPS/COUNTRY/where?columns=DESCRIPTION&keys=DESCRIPTION&values="+ strCusCountry);
				}

				if (url != null) 
				{
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;
					
					if (response != null && response.length() > 0)
					{
						jsonObj = new JSONObject(response.toString());
						cntRec = Integer.parseInt(jsonObj.get("count").toString());
						
						if (cntRec == 0) 
						{
							strErrorMsg = "Error: Customer Country - Display value(s): [" + strCusCountry.replaceAll("%20", " ")+ "] are not listed in the field's keyword options.";
						}
						else
						{
							String strRec = jsonObj.get("Records").toString();
							JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));
							
							String strCol = jsonRecObj.get("Columns").toString();
							JSONObject jsonColObj = new JSONObject(strCol.substring(1,strCol.length() -1));
							
							strCountry= jsonColObj.get("value").toString();
						}
					}	
				}
			} 
			catch (Exception ex) 
			{
				logger.error("Error: ",ex);
				this.objCustomerInterfaceBean.setErrorMessage("Error: "+ex.getMessage());
			}
		}
		
		if (strCountry != null && strErrorMsg == null)
		{
			strStatus = strCountry;
		}
		else
		{
			strStatus = strErrorMsg;
		}
		
		return strStatus;
	}


	//Validate Customer State
	public String validateCustomerState(String strCusState, String strCusCountry) 
	{
		String strStatus = null;
		String strErrorMsg = null;
		String strState = null;
		URL url = null;
		
		if (strCusState != null && strCusState.length() == 0) 
		{
			strCusState = null;
		}

		if (strCusState == null && strCusCountry.equalsIgnoreCase("United States")) 
		{
			strErrorMsg = "Error: Customer State is required field.";
		} 
		else 
		{
			try 
			{
				if (strCusState!=null && strCusState.length() > 0 && strCusCountry.equalsIgnoreCase("United States")) 
				{
					strCusState = strCusState.replaceAll(" ", "%20");
					url = new URL(this.objCustomerInterfaceBean.getEtQURL() + "/dao/LOOKUPS/STATES_LIST/where?columns=DESCRIPTION&keys=DESCRIPTION&values="+ strCusState);
				
					if (url != null) 
					{
						StringBuffer response = getConnection(url);
						JSONObject jsonObj = null;
						int cntRec = -1;
						
						if (response != null && response.length() > 0)
						{

							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());
							
							if (cntRec == 0) 
							{
								strErrorMsg = "Error: Customer State - Display value(s): [" + strCusState.replaceAll("%20", " ")+ "] are not listed in the field's keyword options.";
							}
							else
							{
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));
								
								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1,strCol.length() -1));
								
								strState= jsonColObj.get("value").toString();
							}
						}
					}	
				}
				else if (strCusState!=null && strCusState.length() > 0 && !strCusCountry.equalsIgnoreCase("United States"))
				{
					strState = strCusState;
				}
			} 
			catch (Exception ex) 
			{
				logger.error("Error: ",ex);
				this.objCustomerInterfaceBean.setErrorMessage("Error: "+ex.getMessage());
			}
		}
				
		if (strState != null && strErrorMsg == null)
		{
			strStatus = strState;
		}
		else
		{
			strStatus = strErrorMsg;
		}
		
		return strStatus;
	}
	
	//Validate Customer Contact Name
	public String validateContactName()
	{
		String strErrorMsg = null;
		ResultSet rsContact = null;
		
		try
		{
			Statement stmt = this.objCustomerInterfaceBean.getBusinessUnitConnection().createStatement();
			String strContactQry = "select CONTACT_NAME  from XX_ETQ_CUSTOMER_CONTACT_DETAIL where CUST_U_ID = " + this.objCustomerInterfaceBean.getCustomerUid();
			
			rsContact = stmt.executeQuery(strContactQry);
	
			if (rsContact != null)
			{	
				while (rsContact.next())
				{
					if (rsContact.getString(1) == null || rsContact.getString(1).length() == 0)
					{
						strErrorMsg = "Error: Contact Name is required.";
						break;
					}	
				}
			}	
		}
		catch(Exception ex)
		{
			logger.error("Error: ",ex);
			this.objCustomerInterfaceBean.setErrorMessage("Error: "+ex.getMessage());
		}
		
		return strErrorMsg;
	}
}