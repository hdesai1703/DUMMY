package com.olympus.OEKG.model;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.json.JSONArray;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class XxCustomerInterfaceBean {
	private String customerUid;
	private String accountCode;
	private String etQURL;
	private String etQUser;
	private String serverCred;
	private String businessUnitName;
	private String debugTbl;
	private String batchLogTbl;
	private String businessUnitDriver;
	private String serverName;
	private String customerDocumentId;
	private String customerNumber;
	private int batchId,businessUnitID;
	private Connection businessUnitConnection;
	private Connection baseConnection;
	private Map<String,String> mapResultSet;
	private Map<String,String> mapContactSeqNo;
	private Map<String,String> mapContactUid;
	private Map<String,String> mapContactName;
	private SimpleDateFormat dateFormat;
	private String customerCountry;
	private String customerState;
	private String customerLocation;
	private String errorMessage;
	private String isCustomerUpdate;
	private JSONArray custContact;
}