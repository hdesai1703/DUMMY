package com.olympus.OEKG.ClientService;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.json.JSONArray;

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
	
	/**
	 * @return the mapContactName
	 */
	public Map<String, String> getMapContactName() {
		return mapContactName;
	}
	/**
	 * @param mapContactName the mapContactName to set
	 */
	public void setMapContactName(Map<String, String> mapContactName) {
		this.mapContactName = mapContactName;
	}
	
	/**
	 * @return the mapContactUid
	 */
	public Map<String, String> getMapContactUid() {
		return mapContactUid;
	}
	/**
	 * @param mapContactUid the mapContactUid to set
	 */
	public void setMapContactUid(Map<String, String> mapContactUid) {
		this.mapContactUid = mapContactUid;
	}
	
	/**
	 * @return the custContact
	 */
	public JSONArray getCustContact() {
		return custContact;
	}
	/**
	 * @param custContact the custContact to set
	 */
	public void setCustContact(JSONArray custContact) {
		this.custContact = custContact;
	}
	/**
	 * @return the isCustomerUpdate
	 */
	public String getIsCustomerUpdate() {
		return isCustomerUpdate;
	}
	/**
	 * @param isCustomerUpdate the isCustomerUpdate to set
	 */
	public void setIsCustomerUpdate(String isCustomerUpdate) {
		this.isCustomerUpdate = isCustomerUpdate;
	}
	/**
	 * @return the customerUid
	 */
	public String getCustomerUid() {
		return customerUid;
	}
	/**
	 * @param customerUid the customerUid to set
	 */
	public void setCustomerUid(String customerUid) {
		this.customerUid = customerUid;
	}
	/**
	 * @return the accountCode
	 */
	public String getAccountCode() {
		return accountCode;
	}
	/**
	 * @param accountCode the accountCode to set
	 */
	public void setAccountCode(String accountCode) {
		this.accountCode = accountCode;
	}
	/**
	 * @return the etQURL
	 */
	public String getEtQURL() {
		return etQURL;
	}
	/**
	 * @param etQURL the etQURL to set
	 */
	public void setEtQURL(String etQURL) {
		this.etQURL = etQURL;
	}
	/**
	 * @return the etQUser
	 */
	public String getEtQUser() {
		return etQUser;
	}
	/**
	 * @param etQUser the etQUser to set
	 */
	public void setEtQUser(String etQUser) {
		this.etQUser = etQUser;
	}
	/**
	 * @return the serverCred
	 */
	public String getServerCred() {
		return serverCred;
	}
	/**
	 * @param serverCred the serverCred to set
	 */
	public void setServerCred(String serverCred) {
		this.serverCred = serverCred;
	}
	/**
	 * @return the businessUnitName
	 */
	public String getBusinessUnitName() {
		return businessUnitName;
	}
	/**
	 * @param businessUnitName the businessUnitName to set
	 */
	public void setBusinessUnitName(String businessUnitName) {
		this.businessUnitName = businessUnitName;
	}
	/**
	 * @return the debugTbl
	 */
	public String getDebugTbl() {
		return debugTbl;
	}
	/**
	 * @param debugTbl the debugTbl to set
	 */
	public void setDebugTbl(String debugTbl) {
		this.debugTbl = debugTbl;
	}
	/**
	 * @return the batchLogTbl
	 */
	public String getBatchLogTbl() {
		return batchLogTbl;
	}
	/**
	 * @param batchLogTbl the batchLogTbl to set
	 */
	public void setBatchLogTbl(String batchLogTbl) {
		this.batchLogTbl = batchLogTbl;
	}
	/**
	 * @return the businessUnitDriver
	 */
	public String getBusinessUnitDriver() {
		return businessUnitDriver;
	}
	/**
	 * @param businessUnitDriver the businessUnitDriver to set
	 */
	public void setBusinessUnitDriver(String businessUnitDriver) {
		this.businessUnitDriver = businessUnitDriver;
	}
	/**
	 * @return the serverName
	 */
	public String getServerName() {
		return serverName;
	}
	/**
	 * @param serverName the serverName to set
	 */
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	/**
	 * @return the customerDocumentId
	 */
	public String getCustomerDocumentId() {
		return customerDocumentId;
	}
	/**
	 * @param customerDocumentId the customerDocumentId to set
	 */
	public void setCustomerDocumentId(String customerDocumentId) {
		this.customerDocumentId = customerDocumentId;
	}
	/**
	 * @return the customerNumber
	 */
	public String getCustomerNumber() {
		return customerNumber;
	}
	/**
	 * @param customerNumber the customerNumber to set
	 */
	public void setCustomerNumber(String customerNumber) {
		this.customerNumber = customerNumber;
	}
	/**
	 * @return the batchId
	 */
	public int getBatchId() {
		return batchId;
	}
	/**
	 * @param batchId the batchId to set
	 */
	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}
	/**
	 * @return the businessUnitID
	 */
	public int getBusinessUnitID() {
		return businessUnitID;
	}
	/**
	 * @param businessUnitID the businessUnitID to set
	 */
	public void setBusinessUnitID(int businessUnitID) {
		this.businessUnitID = businessUnitID;
	}
	/**
	 * @return the businessUnitConnection
	 */
	public Connection getBusinessUnitConnection() {
		return businessUnitConnection;
	}
	/**
	 * @param businessUnitConnection the businessUnitConnection to set
	 */
	public void setBusinessUnitConnection(Connection businessUnitConnection) {
		this.businessUnitConnection = businessUnitConnection;
	}
	/**
	 * @return the baseConnection
	 */
	public Connection getBaseConnection() {
		return baseConnection;
	}
	/**
	 * @param baseConnection the baseConnection to set
	 */
	public void setBaseConnection(Connection baseConnection) {
		this.baseConnection = baseConnection;
	}
	/**
	 * @return the mapResultSet
	 */
	public Map<String, String> getMapResultSet() {
		return mapResultSet;
	}
	/**
	 * @param mapResultSet the mapResultSet to set
	 */
	public void setMapResultSet(Map<String, String> mapResultSet) {
		this.mapResultSet = mapResultSet;
	}
	/**
	 * @return the mapContactSeqNo
	 */
	public Map<String, String> getMapContactSeqNo() {
		return mapContactSeqNo;
	}
	/**
	 * @param mapContactSeqNo the mapContactSeqNo to set
	 */
	public void setMapContactSeqNo(Map<String, String> mapContactSeqNo) {
		this.mapContactSeqNo = mapContactSeqNo;
	}
	/**
	 * @return the dateFormat
	 */
	public SimpleDateFormat getDateFormat() {
		return dateFormat;
	}
	/**
	 * @param dateFormat the dateFormat to set
	 */
	public void setDateFormat(SimpleDateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}
	/**
	 * @return the customerCountry
	 */
	public String getCustomerCountry() {
		return customerCountry;
	}
	/**
	 * @param customerCountry the customerCountry to set
	 */
	public void setCustomerCountry(String customerCountry) {
		this.customerCountry = customerCountry;
	}
	/**
	 * @return the customerState
	 */
	public String getCustomerState() {
		return customerState;
	}
	/**
	 * @param customerState the customerState to set
	 */
	public void setCustomerState(String customerState) {
		this.customerState = customerState;
	}
	/**
	 * @return the customerLocation
	 */
	public String getCustomerLocation() {
		return customerLocation;
	}
	/**
	 * @param customerLocation the customerLocation to set
	 */
	public void setCustomerLocation(String customerLocation) {
		this.customerLocation = customerLocation;
	}
	/**
	 * @return the errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	/**
	 * @param errorMessage the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}