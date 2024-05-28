package com.olympus.OEKG.AddRefService;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;

public class XxAdditionalReferenceInterfaceBean {
	private String additionalReferenceUid;
	private String shiseiNumber;
	private String etQURL;
	private String etQUser;
	private String serverCred;
	private String businessUnitName;
	private String debugTbl;
	private String batchLogTbl;
	private String businessUnitDriver;
	private String serverName;
	private int batchId,businessUnitID;
	private Connection businessUnitConnection;
	private Connection baseConnection;
	private ResultSet resultSet;
	private SimpleDateFormat dateFormat;
	private String errorMessage;
	private String isUpdate;
	/**
	 * @return the additionalReferenceUid
	 */
	public String getAdditionalReferenceUid() {
		return additionalReferenceUid;
	}
	/**
	 * @param additionalReferenceUid the additionalReferenceUid to set
	 */
	public void setAdditionalReferenceUid(String additionalReferenceUid) {
		this.additionalReferenceUid = additionalReferenceUid;
	}
	/**
	 * @return the shiseiNumber
	 */
	public String getShiseiNumber() {
		return shiseiNumber;
	}
	/**
	 * @param shiseiNumber the shiseiNumber to set
	 */
	public void setShiseiNumber(String shiseiNumber) {
		this.shiseiNumber = shiseiNumber;
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
	 * @return the resultSet
	 */
	public ResultSet getResultSet() {
		return resultSet;
	}
	/**
	 * @param resultSet the resultSet to set
	 */
	public void setResultSet(ResultSet resultSet) {
		this.resultSet = resultSet;
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
	/**
	 * @return the isUpdate
	 */
	public String getIsUpdate() {
		return isUpdate;
	}
	/**
	 * @param isUpdate the isUpdate to set
	 */
	public void setIsUpdate(String isUpdate) {
		this.isUpdate = isUpdate;
	}
}