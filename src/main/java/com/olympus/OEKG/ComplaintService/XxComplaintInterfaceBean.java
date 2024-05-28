package com.olympus.OEKG.ComplaintService;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;

public class XxComplaintInterfaceBean {
	
	private String interfaceRecordId;
	private String systemSourceRefNo;
	private String etQURL;
	private String etQUser;
	private String serverCred;
	private String businessUnitName;
	private String debugTbl;
	private String batchLogTbl;
	private String businessUnitDriver;
	private String serverName;
	private String complaintDocumentId;
	private String investigationDocumentId;
	private String complaintNumber;
	private String investigationNumber;
	private int batchId,businessUnitID;
	private Connection businessUnitConnection;
	private Connection baseConnection;
	private Map<String,String> mapResultSet;
	private Map<String,String> mapActSeqNo;
	private Map<String,String> mapConclusionSummary;
	private String conclusionSummary;
	private JSONArray compCoding;
	private SimpleDateFormat dateFormat;
	private String systemSource;
	private String customerCountry;
	private String customerState;
	private String modelNumber;
	private String investigationRequired;
	private String systemSource2;
	private String initiatorLocation;
	private String complaintAuthor;
	private String initiatorBranch;
	private String eventFoundAt;
	private String isThisAComplaint;
	private String anyActionsAlreadyTakenAtTheCustomerSite;
	private String customerResponseRequested;
	private String customerLocation;
	private String healthProfessional;
	private String occupation;
	private String cdsMethodBeforeRetOly;
	private String orderType;
	private String isSoftware;
	private String softwareVersion;
	private String operatingSystemVersion;
	private String wasTheProcedureTherapeuticOrDiagnostic;
	private ArrayList<String> patientInvolvementCode;
	private String assignedTo;
	private ArrayList<String> asReportedCode;
	private String partCodeNotFound;
	private String partCodeFound;
	private ArrayList<String> asAnalyzedCodeFound;
	private String asAnalyzedCodeNotFound;
	private ArrayList<String> causeCode;
	private String errorMessage;
	private String secondaryPhaseName;
	private String phaseName;
	private String productQuntity;
	private String isPotentialAdverseEvent;
	private String decisionTreeAnswer1;
	private String decisionTreeAnswer2;
	private String isWarrantyRequired;
	private String isProductReturn;
	private String isProductSerial;
	private String isProductSoftware;
	private String isProductReturnToInv;
	private String isNewInvInfoRec;
	private String isCorrectiveActionComplete;
	private String scheduleLogTbl;
	private String interfaceName;
	private String interfaceType;
	private String reportingPersonTelephone;
	private String customerTelephone;
	private String contactTelephone;
	private String timeZones;
	private String paeQuestionAns2;
	private String paeQuestionAns1;
	private ArrayList<String> finalUniversalCode;
	private String itemType;
	private String latestActivityWillPrdVal;
	private String treeType;
	private SimpleDateFormat dateOnlyFormat;
	
	public SimpleDateFormat getDateOnlyFormat() {
		return dateOnlyFormat;
	}
	public void setDateOnlyFormat(SimpleDateFormat dateOnlyFormat) {
		this.dateOnlyFormat = dateOnlyFormat;
	}
	/**
	 * @return the latestActivityWillPrdVal
	 */
	public String getLatestActivityWillPrdVal() {
		return latestActivityWillPrdVal;
	}
	/**
	 * @param latestActivityWillPrdVal the latestActivityWillPrdVal to set
	 */
	public void setLatestActivityWillPrdVal(String latestActivityWillPrdVal) {
		this.latestActivityWillPrdVal = latestActivityWillPrdVal;
	}
	/**
	 * @return the finalUniversalCode
	 */
	//Added for ETQCR-760
	public ArrayList<String> getFinalUniversalCode() {
		return finalUniversalCode;
	}
	/**
	 * @return the itemType
	 */
	public String getItemType() {
		return itemType;
	}
	//Added in ETQCR-987
	public String getTreeType() {
		return treeType;
	}
	public void setTreeType(String treeType) {
		this.treeType = treeType;
	}
	/**
	 * @param itemType the itemType to set
	 */
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	/**
	 * @param finalUniversalCode the finalUniversalCode to set
	 */
	public void setFinalUniversalCode(ArrayList<String> finalUniversalCode) {
		this.finalUniversalCode = finalUniversalCode;
	}
	
	/**
	 * @return the paeQuestionAns1
	 */
	//Added for ETQCR-850
	public String getPaeQuestionAns1() {
		return paeQuestionAns1;
	}
	/**
	 * @param paeQuestionAns1 the paeQuestionAns2 to set
	 */
	public void setPaeQuestionAns1(String paeQuestionAns1) {
		this.paeQuestionAns1 = paeQuestionAns1;
	}
	
	/**
	 * @return the paeQuestionAns2
	 */
	//Added for ETQCR-760
	public String getPaeQuestionAns2() {
		return paeQuestionAns2;
	}
	/**
	 * @param paeQuestionAns2 the paeQuestionAns2 to set
	 */
	public void setPaeQuestionAns2(String paeQuestionAns2) {
		this.paeQuestionAns2 = paeQuestionAns2;
	}

	//Added for MDMTESTING-4473
	private String complaintClosureDate;

	/**
	 * @return the complaintClosureDate
	 */
	public String getComplaintClosureDate() {
		return complaintClosureDate;
	}
	/**
	 * @param complaintClosureDate the complaintClosureDate to set
	 */
	public void setComplaintClosureDate(String complaintClosureDate) {
		this.complaintClosureDate = complaintClosureDate;
	}
	/**
	 * @return the timeZones
	 */
	public String getTimeZones() {
		return timeZones;
	}
	/**
	 * @param timeZones the timeZones to set
	 */
	public void setTimeZones(String timeZones) {
		this.timeZones = timeZones;
	}
	/**
	 * @return the reportingPersonTelephone
	 */
	public String getReportingPersonTelephone() {
		return reportingPersonTelephone;
	}
	/**
	 * @param reportingPersonTelephone the reportingPersonTelephone to set
	 */
	public void setReportingPersonTelephone(String reportingPersonTelephone) {
		this.reportingPersonTelephone = reportingPersonTelephone;
	}
	/**
	 * @return the customerTelephone
	 */
	public String getCustomerTelephone() {
		return customerTelephone;
	}
	/**
	 * @param customerTelephone the customerTelephone to set
	 */
	public void setCustomerTelephone(String customerTelephone) {
		this.customerTelephone = customerTelephone;
	}
	/**
	 * @return the contactTelephone
	 */
	public String getContactTelephone() {
		return contactTelephone;
	}
	/**
	 * @param contactTelephone the contactTelephone to set
	 */
	public void setContactTelephone(String contactTelephone) {
		this.contactTelephone = contactTelephone;
	}
	/**
	 * @return the interfaceName
	 */
	public String getInterfaceName() {
		return interfaceName;
	}
	/**
	 * @param interfaceName the interfaceName to set
	 */
	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}
	/**
	 * @return the interfaceType
	 */
	public String getInterfaceType() {
		return interfaceType;
	}
	/**
	 * @param interfaceType the interfaceType to set
	 */
	public void setInterfaceType(String interfaceType) {
		this.interfaceType = interfaceType;
	}
	/**
	 * @return the scheduleLogTbl
	 */
	public String getScheduleLogTbl() {
		return scheduleLogTbl;
	}
	/**
	 * @param scheduleLogTbl the scheduleLogTbl to set
	 */
	public void setScheduleLogTbl(String scheduleLogTbl) {
		this.scheduleLogTbl = scheduleLogTbl;
	}
	/**
	 * @return the isProductReturnToInv
	 */
	public String getIsProductReturnToInv() {
		return isProductReturnToInv;
	}
	/**
	 * @param isProductReturnToInv the isProductReturnToInv to set
	 */
	public void setIsProductReturnToInv(String isProductReturnToInv) {
		this.isProductReturnToInv = isProductReturnToInv;
	}
	/**
	 * @return the isNewInvInfoRec
	 */
	public String getIsNewInvInfoRec() {
		return isNewInvInfoRec;
	}
	/**
	 * @param isNewInvInfoRec the isNewInvInfoRec to set
	 */
	public void setIsNewInvInfoRec(String isNewInvInfoRec) {
		this.isNewInvInfoRec = isNewInvInfoRec;
	}
	/**
	 * @return the isCorrectiveActionComplete
	 */
	public String getIsCorrectiveActionComplete() {
		return isCorrectiveActionComplete;
	}
	/**
	 * @param isCorrectiveActionComplete the isCorrectiveActionComplete to set
	 */
	public void setIsCorrectiveActionComplete(String isCorrectiveActionComplete) {
		this.isCorrectiveActionComplete = isCorrectiveActionComplete;
	}
	
	public String getIsProductSoftware() {
		return isProductSoftware;
	}
	public void setIsProductSoftware(String isProductSoftware) {
		this.isProductSoftware = isProductSoftware;
	}
	/**
	 * @return the isProductSerial
	 */
	public String getIsProductSerial() {
		return isProductSerial;
	}
	/**
	 * @param isProductSerial the isProductSerial to set
	 */
	public void setIsProductSerial(String isProductSerial) {
		this.isProductSerial = isProductSerial;
	}
	/**
	 * @return the cdsMethodBeforeRetOly
	 */
	public String getCdsMethodBeforeRetOly() {
		return cdsMethodBeforeRetOly;
	}
	/**
	 * @param cdsMethodBeforeRetOly the cdsMethodBeforeRetOly to set
	 */
	public void setCdsMethodBeforeRetOly(String cdsMethodBeforeRetOly) {
		this.cdsMethodBeforeRetOly = cdsMethodBeforeRetOly;
	}
	
	/**
	 * @return the isProductReturn
	 */
	public String getIsProductReturn() {
		return isProductReturn;
	}
	/**
	 * @param isProductReturn the isProductReturn to set
	 */
	public void setIsProductReturn(String isProductReturn) {
		this.isProductReturn = isProductReturn;
	}
	/**
	 * @return the isWarrantyRequired
	 */
	public String getIsWarrantyRequired() {
		return isWarrantyRequired;
	}
	/**
	 * @param isWarrantyRequired the isWarrantyRequired to set
	 */
	public void setIsWarrantyRequired(String isWarrantyRequired) {
		this.isWarrantyRequired = isWarrantyRequired;
	}
	/**
	 * @return the decisionTreeAnswer1
	 */
	public String getDecisionTreeAnswer1() {
		return decisionTreeAnswer1;
	}
	/**
	 * @param decisionTreeAnswer1 the decisionTreeAnswer1 to set
	 */
	public void setDecisionTreeAnswer1(String decisionTreeAnswer1) {
		if(decisionTreeAnswer1!=null)
		{
			if(decisionTreeAnswer1.equalsIgnoreCase("Y") && decisionTreeAnswer1.equalsIgnoreCase("Yes"))
			{
				this.decisionTreeAnswer1="Answer 1";
			}
			else if(decisionTreeAnswer1.equalsIgnoreCase("N") && decisionTreeAnswer1.equalsIgnoreCase("No"))
			{
				this.decisionTreeAnswer1="Answer 2";
			}
			else if(decisionTreeAnswer1.equalsIgnoreCase("U") && decisionTreeAnswer1.equalsIgnoreCase("Unknown"))
			{
				this.decisionTreeAnswer1="Answer 3";
			}
		}
	}
	/**
	 * @return the decisionTreeAnswer2
	 */
	public String getDecisionTreeAnswer2() {
		return decisionTreeAnswer2;
	}
	/**
	 * @param decisionTreeAnswer2 the decisionTreeAnswer2 to set
	 */
	public void setDecisionTreeAnswer2(String decisionTreeAnswer2) {
		if(decisionTreeAnswer2!=null)
		{
			if(decisionTreeAnswer2.equalsIgnoreCase("Y") && decisionTreeAnswer2.equalsIgnoreCase("Yes"))
			{
				this.decisionTreeAnswer2="Answer 1";
			}
			else if(decisionTreeAnswer2.equalsIgnoreCase("N") && decisionTreeAnswer2.equalsIgnoreCase("No"))
			{
				this.decisionTreeAnswer2="Answer 2";
			}
			else if(decisionTreeAnswer2.equalsIgnoreCase("U") && decisionTreeAnswer2.equalsIgnoreCase("Unknown"))
			{
				this.decisionTreeAnswer2="Answer 3";
			}
		}
	}
	/**
	 * @return the interfaceRecordId
	 */
	public String getInterfaceRecordId() {
		return interfaceRecordId;
	}
	/**
	 * @param interfaceRecordId the interfaceRecordId to set
	 */
	public void setInterfaceRecordId(String interfaceRecordId) {
		this.interfaceRecordId = interfaceRecordId;
	}
	/**
	 * @return the systemSourceRefNo
	 */
	public String getSystemSourceRefNo() {
		return systemSourceRefNo;
	}
	/**
	 * @param systemSourceRefNo the systemSourceRefNo to set
	 */
	public void setSystemSourceRefNo(String systemSourceRefNo) {
		this.systemSourceRefNo = systemSourceRefNo;
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
	 * @return the complaintDocumentId
	 */
	public String getComplaintDocumentId() {
		return complaintDocumentId;
	}
	/**
	 * @param complaintDocumentId the complaintDocumentId to set
	 */
	public void setComplaintDocumentId(String complaintDocumentId) {
		this.complaintDocumentId = complaintDocumentId;
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
	 * @return the mapActSeqNo
	 */
	public Map<String, String> getMapActSeqNo() {
		return mapActSeqNo;
	}
	/**
	 * @param mapActSeqNo the mapActSeqNo to set
	 */
	public void setMapActSeqNo(Map<String, String> mapActSeqNo) {
		this.mapActSeqNo = mapActSeqNo;
	}

	/**
	 * @return the compCoding
	 */
	public JSONArray getCompCoding() {
		return compCoding;
	}
	/**
	 * @param compCoding the compCoding to set
	 */
	public void setCompCoding(JSONArray compCoding) {
		this.compCoding = compCoding;
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
	 * @return the systemSource
	 */
	public String getSystemSource() {
		return systemSource;
	}
	/**
	 * @param systemSource the systemSource to set
	 */
	public void setSystemSource(String systemSource) {
		this.systemSource = systemSource;
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
	 * @return the modelNumber
	 */
	public String getModelNumber() {
		return modelNumber;
	}
	/**
	 * @param modelNumber the modelNumber to set
	 */
	public void setModelNumber(String modelNumber) {
		this.modelNumber = modelNumber;
	}
	/**
	 * @return the investigationRequied
	 */
	public String getInvestigationRequired() {
		return investigationRequired;
	}
	/**
	 * @param investigationRequied the investigationRequired to set
	 */
	public void setInvestigationRequired(String investigationRequired) {
		this.investigationRequired = investigationRequired;
	}
	/**
	 * @return the systemSource2
	 */
	public String getSystemSource2() {
		return systemSource2;
	}
	/**
	 * @param systemSource2 the systemSource2 to set
	 */
	public void setSystemSource2(String systemSource2) {
		this.systemSource2 = systemSource2;
	}
	/**
	 * @return the initiatorLocation
	 */
	public String getInitiatorLocation() {
		return initiatorLocation;
	}
	/**
	 * @param initiatorLocation the initiatorLocation to set
	 */
	public void setInitiatorLocation(String initiatorLocation) {
		this.initiatorLocation = initiatorLocation;
	}
	/**
	 * @return the complaintAuthor
	 */
	public String getComplaintAuthor() {
		return complaintAuthor;
	}
	/**
	 * @param complaintAuthor the complaintAuthor to set
	 */
	public void setComplaintAuthor(String complaintAuthor) {
		this.complaintAuthor = complaintAuthor;
	}
	/**
	 * @return the initiatorBranch
	 */
	public String getInitiatorBranch() {
		return initiatorBranch;
	}
	/**
	 * @param initiatorBranch the initiatorBranch to set
	 */
	public void setInitiatorBranch(String initiatorBranch) {
		this.initiatorBranch = initiatorBranch;
	}
	/**
	 * @return the eventFoundAt
	 */
	public String getEventFoundAt() {
		return eventFoundAt;
	}
	/**
	 * @param eventFoundAt the eventFoundAt to set
	 */
	public void setEventFoundAt(String eventFoundAt) {
		this.eventFoundAt = eventFoundAt;
	}
	/**
	 * @return the isThisAComplaint
	 */
	public String getIsThisAComplaint() {
		return isThisAComplaint;
	}
	/**
	 * @param isThisAComplaint the isThisAComplaint to set
	 */
	public void setIsThisAComplaint(String isThisAComplaint) {
		this.isThisAComplaint = isThisAComplaint;
	}
	/**
	 * @return the anyActionsAlreadyTakenAtTheCustomerSite
	 */
	public String getAnyActionsAlreadyTakenAtTheCustomerSite() {
		return anyActionsAlreadyTakenAtTheCustomerSite;
	}
	/**
	 * @param anyActionsAlreadyTakenAtTheCustomerSite the anyActionsAlreadyTakenAtTheCustomerSite to set
	 */
	public void setAnyActionsAlreadyTakenAtTheCustomerSite(
			String anyActionsAlreadyTakenAtTheCustomerSite) {
		this.anyActionsAlreadyTakenAtTheCustomerSite = anyActionsAlreadyTakenAtTheCustomerSite;
	}
	/**
	 * @return the customerResponseRequested
	 */
	public String getCustomerResponseRequested() {
		return customerResponseRequested;
	}
	/**
	 * @param customerResponseRequested the customerResponseRequested to set
	 */
	public void setCustomerResponseRequested(String customerResponseRequested) {
		this.customerResponseRequested = customerResponseRequested;
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
	 * @return the healthProfessional
	 */
	public String getHealthProfessional() {
		return healthProfessional;
	}
	/**
	 * @param healthProfessional the healthProfessional to set
	 */
	public void setHealthProfessional(String healthProfessional) {
		this.healthProfessional = healthProfessional;
	}
	/**
	 * @return the occupation
	 */
	public String getOccupation() {
		return occupation;
	}
	/**
	 * @param occupation the occupation to set
	 */
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	/**
	 * @return the orderType
	 */
	public String getOrderType() {
		return orderType;
	}
	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	/**
	 * @return the isSoftware
	 */
	public String getIsSoftware() {
		return isSoftware;
	}
	/**
	 * @param isSoftware the isSoftware to set
	 */
	public void setIsSoftware(String isSoftware) {
		this.isSoftware = isSoftware;
	}
	/**
	 * @return the softwareVersion
	 */
	public String getSoftwareVersion() {
		return softwareVersion;
	}
	/**
	 * @param softwareVersion the softwareVersion to set
	 */
	public void setSoftwareVersion(String softwareVersion) {
		this.softwareVersion = softwareVersion;
	}
	/**
	 * @return the operatingSystemVersion
	 */
	public String getOperatingSystemVersion() {
		return operatingSystemVersion;
	}
	/**
	 * @param operatingSystemVersion the operatingSystemVersion to set
	 */
	public void setOperatingSystemVersion(String operatingSystemVersion) {
		this.operatingSystemVersion = operatingSystemVersion;
	}
	/**
	 * @return the wasTheProcedureTherapeuticOrDiagnostic
	 */
	public String getWasTheProcedureTherapeuticOrDiagnostic() {
		return wasTheProcedureTherapeuticOrDiagnostic;
	}
	/**
	 * @param wasTheProcedureTherapeuticOrDiagnostic the wasTheProcedureTherapeuticOrDiagnostic to set
	 */
	public void setWasTheProcedureTherapeuticOrDiagnostic(
			String wasTheProcedureTherapeuticOrDiagnostic) {
		this.wasTheProcedureTherapeuticOrDiagnostic = wasTheProcedureTherapeuticOrDiagnostic;
	}
	/**
	 * @return the patientInvolvementCode
	 */
	public  ArrayList<String> getPatientInvolvementCode() {
		return patientInvolvementCode;
	}
	/**
	 * @param patientInvolvementCode the patientInvolvementCode to set
	 */
	public void setPatientInvolvementCode( ArrayList<String> patientInvolvementCode) {
		this.patientInvolvementCode = patientInvolvementCode;
	}
	/**
	 * @return the assignedTo
	 */
	public String getAssignedTo() {
		return assignedTo;
	}
	/**
	 * @param assignedTo the assignedTo to set
	 */
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	/**
	 * @return the asReportedCode
	 */
	public ArrayList<String> getAsReportedCode() {
		return asReportedCode;
	}
	/**
	 * @param asReportedCode the asReportedCode to set
	 */
	public void setAsReportedCode(ArrayList<String> asReportedCode) {
		this.asReportedCode = asReportedCode;
	}
	/**
	 * @return the partCodeNotFound
	 */
	public String getPartCodeNotFound() {
		return partCodeNotFound;
	}
	/**
	 * @param partCodeNotFound the partCodeNotFound to set
	 */
	public void setPartCodeNotFound(String partCodeNotFound) {
		this.partCodeNotFound = partCodeNotFound;
	}
	/**
	 * @return the partCodeFound
	 */
	public String getPartCodeFound() {
		return partCodeFound;
	}
	/**
	 * @param partCodeFound the partCodeFound to set
	 */
	public void setPartCodeFound(String partCodeFound) {
		this.partCodeFound = partCodeFound;
	}
	/**
	 * @return the asAnalyzedCodeFound
	 */
	public ArrayList<String> getAsAnalyzedCodeFound() {
		return asAnalyzedCodeFound;
	}
	/**
	 * @param asAnalyzedCodeFound the asAnalyzedCodeFound to set
	 */
	public void setAsAnalyzedCodeFound(ArrayList<String> asAnalyzedCodeFound) {
		this.asAnalyzedCodeFound = asAnalyzedCodeFound;
	}
	/**
	 * @return the asAnalyzedCodeNotFound
	 */
	public String getAsAnalyzedCodeNotFound() {
		return asAnalyzedCodeNotFound;
	}
	/**
	 * @param asAnalyzedCodeNotFound the asAnalyzedCodeNotFound to set
	 */
	public void setAsAnalyzedCodeNotFound(String asAnalyzedCodeNotFound) {
		this.asAnalyzedCodeNotFound = asAnalyzedCodeNotFound;
	}
	/**
	 * @return the causeCodeDesc
	 */
	public ArrayList<String> getCauseCode() {
		return causeCode;
	}
	/**
	 * @param causeCodeDesc the causeCodeDesc to set
	 */
	public void setCauseCode(ArrayList<String> causeCode) {
		this.causeCode = causeCode;
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
	 * @return the secondaryPhaseName
	 */
	public String getSecondaryPhaseName() {
		return secondaryPhaseName;
	}
	/**
	 * @param secondaryPhaseName the secondaryPhaseName to set
	 */
	public void setSecondaryPhaseName(String secondaryPhaseName) {
		this.secondaryPhaseName = secondaryPhaseName;
	}
	/**
	 * @return the phaseName
	 */
	public String getPhaseName() {
		return phaseName;
	}
	/**
	 * @param phaseName the phaseName to set
	 */
	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}
	/**
	 * @return the productQuntity
	 */
	public String getProductQuntity() {
		return productQuntity;
	}
	/**
	 * @param productQuntity the productQuntity to set
	 */
	public void setProductQuntity(String productQuntity) {
		this.productQuntity = productQuntity;
	}
	/**
	 * @return the isPotentialAdverseEvent
	 */
	public String getIsPotentialAdverseEvent() {
		return isPotentialAdverseEvent;
	}
	/**
	 * @param isPotentialAdverseEvent the isPotentialAdverseEvent to set
	 */
	public void setIsPotentialAdverseEvent(String isPotentialAdverseEvent) {
		this.isPotentialAdverseEvent = isPotentialAdverseEvent;
	}
	
	/**
	 * @return the conclusionSummary
	 */
	public String getConclusionSummary() {
		return conclusionSummary;
	}
	/**
	 * @param conclusionSummary the conclusionSummary to set
	 */
	public void setConclusionSummary(String conclusionSummary) {
		this.conclusionSummary = conclusionSummary;
	}
	/**
	 * @return the mapConclusionSummary
	 */
	public Map<String, String> getMapConclusionSummary() {
		return mapConclusionSummary;
	}
	/**
	 * @param mapConclusionSummary the mapConclusionSummary to set
	 */
	public void setMapConclusionSummary(Map<String, String> mapConclusionSummary) {
		this.mapConclusionSummary = mapConclusionSummary;
	}	   
	/**
	 * @return the investigationDocumentId
	 */
	public String getInvestigationDocumentId() {
		return investigationDocumentId;
	}
	/**
	 * @param investigationDocumentId the investigationDocumentId to set
	 */
	public void setInvestigationDocumentId(String investigationDocumentId) {
		this.investigationDocumentId = investigationDocumentId;
	}
	/**
	 * @return the complaintNumber
	 */
	public String getComplaintNumber() {
		return complaintNumber;
	}
	/**
	 * @param complaintNumber the complaintNumber to set
	 */
	public void setComplaintNumber(String complaintNumber) {
		this.complaintNumber = complaintNumber;
	}
	/**
	 * @return the investigationNumber
	 */
	public String getInvestigationNumber() {
		return investigationNumber;
	}
	/**
	 * @param investigationNumber the investigationNummber to set
	 */
	public void setInvestigationNumber(String investigationNumber) {
		this.investigationNumber = investigationNumber;
	}
	
	/**
	 * clearBean to clear all values from bean
	 */
	public void clearBean() {
		this.interfaceRecordId=null;
		this.systemSourceRefNo=null;		
		this.complaintDocumentId=null;
		this.investigationDocumentId=null;
		this.complaintNumber=null;
		this.investigationNumber=null;
		this.mapResultSet=new HashMap<String, String>();
		this.mapActSeqNo=new HashMap<String, String>();
		this.mapConclusionSummary=new HashMap<String, String>();
		this.conclusionSummary=null;
		this.compCoding=new JSONArray();
		this.systemSource=null;
		this.customerCountry=null;
		this.customerState=null;
		this.modelNumber=null;
		this.investigationRequired=null;
		this.systemSource2=null;
		this.initiatorLocation=null;
		this.complaintAuthor=null;
		this.initiatorBranch=null;
		this.eventFoundAt=null;
		this.isThisAComplaint=null;
		this.anyActionsAlreadyTakenAtTheCustomerSite=null;
		this.customerResponseRequested=null;
		this.customerLocation=null;
		this.healthProfessional=null;
		this.occupation=null;
		this.cdsMethodBeforeRetOly=null;
		this.orderType=null;
		this.isSoftware=null;
		this.softwareVersion=null;
		this.operatingSystemVersion=null;
		this.wasTheProcedureTherapeuticOrDiagnostic=null;
		this.patientInvolvementCode=new ArrayList<String>();
		this.assignedTo=null;
		this.asReportedCode=new ArrayList<String>();
		this.partCodeNotFound=null;
		this.partCodeFound=null;
		this.asAnalyzedCodeFound=new ArrayList<String>();
		this.asAnalyzedCodeNotFound=null;
		this.causeCode=new ArrayList<String>();
		this.errorMessage=null;
		this.secondaryPhaseName=null;
		this.phaseName=null;
		this.productQuntity=null;
		this.isPotentialAdverseEvent=null;
		this.decisionTreeAnswer1=null;
		this.decisionTreeAnswer2=null;
		this.isWarrantyRequired=null;
		this.isProductReturn=null;
		this.isProductSerial=null;
		this.isProductSoftware=null;
		this.isProductReturnToInv=null;
		this.isNewInvInfoRec=null;
		this.isCorrectiveActionComplete=null;
		this.reportingPersonTelephone=null;
		this.customerTelephone=null;
		this.contactTelephone=null;
		this.complaintClosureDate=null;
		this.paeQuestionAns2=null;
		this.paeQuestionAns1=null;
		this.itemType=null;
		this.finalUniversalCode=new ArrayList<String>();
		this.treeType=null;
	}
}