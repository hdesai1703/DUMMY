package com.olympus.OEKG.ComplaintService;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.olympus.OEKG.Utility.utility;

public class XxComplaintInterface {

	final Logger logger = LoggerFactory.getLogger(XxComplaintInterface.class);

	private utility objUtil = new utility();

	XxComplaintInterfaceBean objInterfaceBean = new XxComplaintInterfaceBean();
	XxComplaintInterfaceValidation objValidation = new XxComplaintInterfaceValidation();

	XxComplaintInterface(XxComplaintInterfaceBean objInterfaceBean) {
		this.objInterfaceBean = objInterfaceBean;
		this.objInterfaceBean.setInterfaceRecordId(this.objInterfaceBean.getMapResultSet().get("1"));
		this.objInterfaceBean.setSystemSourceRefNo(this.objInterfaceBean.getMapResultSet().get("2"));
		this.objInterfaceBean.setDateFormat(new SimpleDateFormat("MMM dd, yyyy hh:mm a"));
		this.objInterfaceBean.setDateOnlyFormat(new SimpleDateFormat("MMM dd, yyyy"));
	}

	public String runComplaintInterface() {
		String eMsg = "SUCCESS";
		try {
			Map<String, String> complaintMap = this.objInterfaceBean.getMapResultSet();
			/* Interface Validation */
			this.objInterfaceBean = objValidation.validateComplaintFields(this.objInterfaceBean);
			String strPhaseName = this.objInterfaceBean.getPhaseName();
			String strSecondaryPhase = this.objInterfaceBean.getSecondaryPhaseName();
			if (this.objInterfaceBean.getErrorMessage() != null && this.objInterfaceBean.getErrorMessage().length() > 0
					&& this.objInterfaceBean.getErrorMessage().contains("Error: ")) {
				eMsg = this.objInterfaceBean.getErrorMessage();
				logger.error(eMsg);
				updateComplaintTables(this.objInterfaceBean, eMsg, "");
				updateActivityTables(this.objInterfaceBean, eMsg, "");
			} else {
				this.objInterfaceBean.setAsReportedCode(getAsReportedCode());
				logger.info("Product Item Type: " + this.objInterfaceBean.getItemType());
				this.objInterfaceBean = objValidation.validateCompAsReportedCode(this.objInterfaceBean);
				if (this.objInterfaceBean.getErrorMessage() != null
						&& this.objInterfaceBean.getErrorMessage().length() > 0
						&& this.objInterfaceBean.getErrorMessage().contains("Error: ")) {
					eMsg = this.objInterfaceBean.getErrorMessage();
					logger.error(eMsg);
					updateComplaintTables(this.objInterfaceBean, eMsg, "");
					updateActivityTables(this.objInterfaceBean, eMsg, "");
				} else {
					// Added for MDMTESTING-4473
					String strCodingValidated = "Yes";
					if (this.objInterfaceBean.getPhaseName() != null
							&& this.objInterfaceBean.getPhaseName().length() > 0
					// Commented for ETQCR-880 &&
					// !this.objInterfaceBean.getPhaseName().equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_DRAFT")
					) {
						// Added for MDMTESTING-4473
						this.objInterfaceBean = objValidation.validateCoding(this.objInterfaceBean);

						if (this.objInterfaceBean.getErrorMessage() != null
								&& this.objInterfaceBean.getErrorMessage().length() > 0
								&& this.objInterfaceBean.getErrorMessage().contains("Error: ")) {
							strCodingValidated = "No";
							eMsg = this.objInterfaceBean.getErrorMessage();
							logger.error(eMsg);
							updateComplaintTables(this.objInterfaceBean, eMsg, "");
							updateActivityTables(this.objInterfaceBean, eMsg, "");
						} else {
							this.objInterfaceBean = getCoding(this.objInterfaceBean);
						}
					}

					if (this.objInterfaceBean.getErrorMessage() != null
							&& this.objInterfaceBean.getErrorMessage().length() > 0
							&& this.objInterfaceBean.getErrorMessage().contains("Error: ")) {
						eMsg = this.objInterfaceBean.getErrorMessage();
						logger.error(eMsg);
						updateComplaintTables(this.objInterfaceBean, eMsg, "");
						updateActivityTables(this.objInterfaceBean, eMsg, "");
					} else {
						// Added for MDMTESTING-4473
						if (strCodingValidated.equalsIgnoreCase("Yes")) {
							if (this.objInterfaceBean.getInvestigationRequired() != null
									&& this.objInterfaceBean.getInvestigationRequired().length() > 0
									&& this.objInterfaceBean.getInvestigationRequired().equalsIgnoreCase("Yes")
									&& !this.objInterfaceBean.getPhaseName()
											.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_DRAFT")
									&& !this.objInterfaceBean.getPhaseName()
											.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_INVESTIGATION")) {
								this.objInterfaceBean = objValidation
										.validateInvestigationActivityFields(this.objInterfaceBean);
							}

							if (this.objInterfaceBean.getErrorMessage() != null
									&& this.objInterfaceBean.getErrorMessage().length() > 0
									&& this.objInterfaceBean.getErrorMessage().contains("Error: ")) {
								eMsg = this.objInterfaceBean.getErrorMessage();
								logger.error(eMsg);
								updateComplaintTables(this.objInterfaceBean, eMsg, "");
								updateActivityTables(this.objInterfaceBean, eMsg, "");
							} else {
								JSONObject parent = getJsonDocument(complaintMap, strPhaseName, strSecondaryPhase,
										this.objInterfaceBean.getEtQUser());

								if (this.objInterfaceBean.getErrorMessage() != null
										&& this.objInterfaceBean.getErrorMessage().length() > 0
										&& this.objInterfaceBean.getErrorMessage().contains("Error: ")) {
									eMsg = this.objInterfaceBean.getErrorMessage();
									logger.error(eMsg);
									updateComplaintTables(this.objInterfaceBean, eMsg, "");
									updateActivityTables(this.objInterfaceBean, eMsg, "");
								} else {
									eMsg = createComplaint(strPhaseName, strSecondaryPhase, parent, getCurrentDate());
									if (strSecondaryPhase != null && strSecondaryPhase.length() > 0
											&& this.objInterfaceBean.getComplaintDocumentId() != null) {
										routeDocument(this.objInterfaceBean, "COMPLAINTS",
												"COMPLAINTS_COMPLAINT_DOCUMENT", strPhaseName, getCurrentDate());
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			eMsg = "Error: " + e.getMessage();
			logger.error("", e);
			updateComplaintTables(this.objInterfaceBean, eMsg, "");
			updateActivityTables(this.objInterfaceBean, eMsg, "");
		}
		return eMsg;
	}

	public JSONObject getJsonDocument(Map<String, String> compRes, String strPhase, String strSecondaryPhase,
			String strCompAuthor) {
		JSONObject parent = new JSONObject();
		JSONObject parentNode = new JSONObject();
		JSONArray childNode = new JSONArray();
		try {
			parent.put("Document", parentNode);

			parentNode.put("applicationName", "COMPLAINTS");
			parentNode.put("formName", "COMPLAINTS_COMPLAINT_DOCUMENT");
			parentNode.put("documentId", "");
			if (strSecondaryPhase != null && strSecondaryPhase.length() > 0) {
				parentNode.put("phase", strSecondaryPhase);
			} else {
				parentNode.put("phase", strPhase);
			}
			parentNode.put("Fields", childNode);

			Map<String, Object> compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "CH_HIDE_INITIATOR_P");

			if (compRes.get("6") != null && compRes.get("6").length() > 0) {
				JSONArray initValObj = new JSONArray();
				initValObj.put(compRes.get("6"));
				compFields.put("Values", initValObj);
				childNode.put(compFields);
			} else {
				JSONArray initValObj = new JSONArray();
				initValObj.put(this.objInterfaceBean.getEtQUser());
				compFields.put("Values", initValObj);
				childNode.put(compFields);
			}

			String strReportPer = compRes.get("9");
			if (strReportPer == null) {
				if (compRes.get("95") != null) {
					strReportPer = compRes.get("95");
				}
			}
			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "REPORTING_PERSON_P");
			JSONArray repValObj = new JSONArray();
			repValObj.put(strReportPer);
			compFields.put("Values", repValObj);
			childNode.put(compFields);

			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "REPORTING_PERSON_LOCAL_LANG_P");
			JSONArray repLocValObj = new JSONArray();
			repLocValObj.put(compRes.get("95"));
			compFields.put("Values", repLocValObj);
			childNode.put(compFields);

			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "CH_USR_RCV_NTFY_INV_CLOSED_P");
			JSONArray userRcvNtfyValObj = new JSONArray();
			userRcvNtfyValObj.put(this.objInterfaceBean.getEtQUser());
			compFields.put("Values", userRcvNtfyValObj);
			childNode.put(compFields);

			if (this.objInterfaceBean.getInitiatorLocation() != null
					&& this.objInterfaceBean.getInitiatorLocation().length() > 0) {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "ETQ$LOCATIONS");
				JSONArray initLocValObj = new JSONArray();
				initLocValObj.put(this.objInterfaceBean.getInitiatorLocation());
				compFields.put("Values", initLocValObj);
				childNode.put(compFields);
			}

			ArrayList<String> arrArCode = this.objInterfaceBean.getAsReportedCode();
			// system.out.println("ARRCODE
			// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$"+arrArCode.toString());
			if (arrArCode != null && arrArCode.size() > 0) {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "HIDE_AS_REPORTED_CODE_P");

				Set<String> set = new HashSet<String>(arrArCode);
				arrArCode.clear();
				arrArCode.addAll(set);

				compFields.put("Values", set);
				childNode.put(compFields);
			}

			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "ETQ$CREATED_DATE");
			JSONArray createdValObj = new JSONArray();
			createdValObj.put(getCreatedDate(this.objInterfaceBean.getTimeZones()));
			compFields.put("Values", createdValObj);
			childNode.put(compFields);

			compFields = new LinkedHashMap<String, Object>(1);
			if (compRes.get("7") != null && compRes.get("7").length() > 0 && !compRes.get("7").contains("1900")) {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "EVENT_DATE_P");
				JSONArray eventDateValObj = new JSONArray();
				eventDateValObj.put(getDateByTimeZone(compRes.get("7"), this.objInterfaceBean.getTimeZones()));
				compFields.put("Values", eventDateValObj);
				childNode.put(compFields);
			} else {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "EVENT_DATE_UNKNOWN_P");
				JSONArray eventDtUnknownValObj = new JSONArray();
				eventDtUnknownValObj.put("Yes");
				compFields.put("Values", eventDtUnknownValObj);
				childNode.put(compFields);
			}

			String strAwareDate = null, strNotiDate = null;

			// Added for ETQCR-1052
			if (compRes.get("8") != null && compRes.get("8").length() > 0 && compRes.get("93") != null
					&& compRes.get("93").length() > 0) {
				strAwareDate = compRes.get("93");
				strNotiDate = compRes.get("93");
			} else {
				if (compRes.get("8") != null && compRes.get("8").length() > 0) {
					strAwareDate = compRes.get("8");
				} else {
					if (compRes.get("93") != null && compRes.get("93").length() > 0) {
						strAwareDate = compRes.get("93");
					}
				}

				if (compRes.get("93") != null && compRes.get("93").length() > 0) {
					strNotiDate = compRes.get("93");
				} else {
					strNotiDate = strAwareDate;
				}
			}

			// Commented for ETQCR-1052
			/*
			 * if(this.objInterfaceBean.getIsPotentialAdverseEvent()!=null &&
			 * this.objInterfaceBean.getIsPotentialAdverseEvent().equalsIgnoreCase("Yes")) {
			 */
			if (strNotiDate != null && strNotiDate.length() > 0 && !strNotiDate.contains("1900")) {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "CH_NOTIFICATION_DATE_P");
				JSONArray notiDtValObj = new JSONArray();
				notiDtValObj.put(getDateByTimeZone(strNotiDate, this.objInterfaceBean.getTimeZones()));
				compFields.put("Values", notiDtValObj);
				childNode.put(compFields);
			}

			if (strAwareDate != null && strAwareDate.length() > 0 && !strAwareDate.contains("1900")) {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "AWARE_DATE_P");
				JSONArray awareDateValObj = new JSONArray();
				awareDateValObj.put(getDateByTimeZone(strAwareDate, this.objInterfaceBean.getTimeZones()));
				compFields.put("Values", awareDateValObj);
				childNode.put(compFields);
			}
			/*
			 * } //ETQCR-808 else if(this.objInterfaceBean.getPaeQuestionAns2()!=null &&
			 * this.objInterfaceBean.getPaeQuestionAns2().length()>0) { if(strNotiDate!=null
			 * && strNotiDate.length()>0 && !strNotiDate.contains("1900")) { compFields =
			 * new LinkedHashMap<String, Object>(1);
			 * compFields.put("fieldName","CH_NOTIFICATION_DATE_P"); JSONArray notiDtValObj
			 * = new JSONArray(); notiDtValObj.put(getDateByTimeZone(strNotiDate,
			 * this.objInterfaceBean.getTimeZones())); compFields.put("Values",
			 * notiDtValObj); childNode.put(compFields); }
			 * 
			 * if(strAwareDate!=null && strAwareDate.length()>0 &&
			 * !strAwareDate.contains("1900")) { compFields = new LinkedHashMap<String,
			 * Object>(1); compFields.put("fieldName","AWARE_DATE_P"); JSONArray
			 * awareDateValObj = new JSONArray();
			 * awareDateValObj.put(getDateByTimeZone(strAwareDate,
			 * this.objInterfaceBean.getTimeZones())); compFields.put("Values",
			 * awareDateValObj); childNode.put(compFields); } } else { if(strNotiDate!=null
			 * && strNotiDate.length()>0 && !strNotiDate.contains("1900")) { compFields =
			 * new LinkedHashMap<String, Object>(1);
			 * compFields.put("fieldName","CH_NOTIFICATION_DATE_P"); JSONArray notiDtValObj
			 * = new JSONArray(); notiDtValObj.put(getDateByTimeZone(strNotiDate,
			 * this.objInterfaceBean.getTimeZones())); compFields.put("Values",
			 * notiDtValObj); childNode.put(compFields); } }
			 */

			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "ETQ$AUTHOR");
			JSONArray authorValObj = new JSONArray();
			authorValObj.put(strCompAuthor);
			compFields.put("Values", authorValObj);
			childNode.put(compFields);

			if (this.objInterfaceBean.getInitiatorBranch() != null
					&& this.objInterfaceBean.getInitiatorBranch().length() > 0
					&& this.objInterfaceBean.getInitiatorBranch().equalsIgnoreCase("Yes")) {
				if (compRes.get("63") != null && compRes.get("63").length() > 0) {
					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "INITIATOR_BRANCH_P");
					JSONArray branchValObj = new JSONArray();
					branchValObj.put(compRes.get("63"));
					compFields.put("Values", branchValObj);
					childNode.put(compFields);
				}
			}

			String strReportPerPos = compRes.get("64");
			if (strReportPerPos == null) {
				if (compRes.get("102") != null) {
					strReportPerPos = compRes.get("102");
				}
			}
			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "REPORTING_PERSON_TITLE_P");
			JSONArray repPosValObj = new JSONArray();
			repPosValObj.put(strReportPerPos);
			compFields.put("Values", repPosValObj);
			childNode.put(compFields);

			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "REPORTING_PERSON_POSITION_LOCAL_LANG_P");
			JSONArray repPosLocValObj = new JSONArray();
			repPosLocValObj.put(compRes.get("102"));
			compFields.put("Values", repPosLocValObj);
			childNode.put(compFields);

			if (this.objInterfaceBean.getCustomerCountry() != null
					&& this.objInterfaceBean.getCustomerCountry().equalsIgnoreCase("United States")) {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "REPORTING_PERSON_TELEPHONE_P");
				JSONArray repTelValObj = new JSONArray();
				repTelValObj.put(this.objInterfaceBean.getReportingPersonTelephone());
				compFields.put("Values", repTelValObj);
				childNode.put(compFields);
			} else {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "REPORTING_PERSON_TELEPHONE_P");
				JSONArray repTelValObj = new JSONArray();
				repTelValObj.put(compRes.get("10"));
				compFields.put("Values", repTelValObj);
				childNode.put(compFields);
			}

			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "REPORTING_PERSON_EMAIL_P");
			JSONArray repEmailValObj = new JSONArray();
			repEmailValObj.put(compRes.get("11"));
			compFields.put("Values", repEmailValObj);
			childNode.put(compFields);

			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "DID_THE_USER_FACILITY_SEND_A_REPORT_TO_REGULATORY_BODY_P");
			JSONArray didUstFsSentValObj = new JSONArray();
			didUstFsSentValObj.put(getYesNo(compRes.get("65")));
			compFields.put("Values", didUstFsSentValObj);
			childNode.put(compFields);

			if (compRes.get("65") != null && compRes.get("65").contains("Y")) {
				String strNameOfComLoc = compRes.get("66");
				if (strNameOfComLoc == null) {
					if (compRes.get("103") != null) {
						strNameOfComLoc = compRes.get("103");
					}
				}

				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "CH_NAME_OF_COMPETENT_AUTHORITY_P");
				JSONArray nameCompAuthValObj = new JSONArray();
				nameCompAuthValObj.put(strNameOfComLoc);
				compFields.put("Values", nameCompAuthValObj);
				childNode.put(compFields);

				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "NAME_OF_COM_AUT_LOCAL_LAN_P");
				JSONArray nameComAutLocValObj = new JSONArray();
				nameComAutLocValObj.put(compRes.get("103"));
				compFields.put("Values", nameComAutLocValObj);
				childNode.put(compFields);
			}

			String strEventDesc = compRes.get("12");
			if (strEventDesc == null) {
				if (compRes.get("96") != null) {
					strEventDesc = compRes.get("96");
				}
			}

			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "CH_EVENTDESC_P");
			JSONArray eventDescValObj = new JSONArray();
			eventDescValObj.put(strEventDesc);
			compFields.put("Values", eventDescValObj);
			childNode.put(compFields);

			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "EVENT_DESCRIPTION_LOC_LANG_P");
			JSONArray eventDecLocValObj = new JSONArray();
			eventDecLocValObj.put(compRes.get("96"));
			compFields.put("Values", eventDecLocValObj);
			childNode.put(compFields);

			if (this.objInterfaceBean.getIsThisAComplaint() != null
					&& this.objInterfaceBean.getIsThisAComplaint().length() > 0) {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "IS_THIS_A_COMPLAINT_P");
				JSONArray isCompValObj = new JSONArray();
				isCompValObj.put(this.objInterfaceBean.getIsThisAComplaint());
				compFields.put("Values", isCompValObj);
				childNode.put(compFields);
			} else {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "IS_THIS_A_COMPLAINT_P");
				JSONArray isCompValObj = new JSONArray();
				isCompValObj.put("Yes");
				compFields.put("Values", isCompValObj);
				childNode.put(compFields);
			}

			if (this.objInterfaceBean.getAnyActionsAlreadyTakenAtTheCustomerSite() != null
					&& this.objInterfaceBean.getAnyActionsAlreadyTakenAtTheCustomerSite().length() > 0) {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "ANY_ACTIONS_TAKEN_AT_CUSTOMER_SITE_P");
				JSONArray actTakenCustValObj = new JSONArray();
				actTakenCustValObj.put(this.objInterfaceBean.getAnyActionsAlreadyTakenAtTheCustomerSite());
				compFields.put("Values", actTakenCustValObj);
				childNode.put(compFields);
			}

			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "REF_NUM_ASSOC_W_CUST_CARE_P");
			JSONArray refNoAsWctValObj = new JSONArray();
			refNoAsWctValObj.put(compRes.get("51"));
			compFields.put("Values", refNoAsWctValObj);
			childNode.put(compFields);

			if (this.objInterfaceBean.getEventFoundAt() != null
					&& this.objInterfaceBean.getEventFoundAt().length() > 0) {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "CH_INIT_EVENTFOUND_P");
				JSONArray eventFoundAtValObj = new JSONArray();
				eventFoundAtValObj.put(this.objInterfaceBean.getEventFoundAt());
				compFields.put("Values", eventFoundAtValObj);
				childNode.put(compFields);
			} else {
				// Added for ETQCR-889
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "CH_INIT_EVENTFOUND_P");
				JSONArray eventFoundAtValObj = new JSONArray();
				eventFoundAtValObj.put("Unknown");
				compFields.put("Values", eventFoundAtValObj);
				childNode.put(compFields);
			}

			if (this.objInterfaceBean.getCustomerResponseRequested() != null
					&& this.objInterfaceBean.getCustomerResponseRequested().length() > 0) {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "COMPLAINTS_RESPONSE_REQUESTED");
				JSONArray custResValObj = new JSONArray();
				custResValObj.put(this.objInterfaceBean.getCustomerResponseRequested());
				compFields.put("Values", custResValObj);
				childNode.put(compFields);
			}

			if (this.objInterfaceBean.getIsPotentialAdverseEvent() != null
					&& this.objInterfaceBean.getIsPotentialAdverseEvent().equalsIgnoreCase("Yes")) {
				if (this.objInterfaceBean.getWasTheProcedureTherapeuticOrDiagnostic() != null
						&& this.objInterfaceBean.getWasTheProcedureTherapeuticOrDiagnostic().length() > 0) {
					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "PROCEDURE_THERAPEUTIC_P");
					JSONArray procTherValObj = new JSONArray();
					procTherValObj.put(this.objInterfaceBean.getWasTheProcedureTherapeuticOrDiagnostic());
					compFields.put("Values", procTherValObj);
					childNode.put(compFields);
				}

				// Commented for ETQCR-931
				/*
				 * if(this.objInterfaceBean.getPatientInvolvementCode()!=null &&
				 * this.objInterfaceBean.getPatientInvolvementCode().size()>0) { compFields =
				 * new LinkedHashMap<String, Object>(1);
				 * compFields.put("fieldName","CH_PATINT_INVOLV_CODE_P");
				 * compFields.put("Values", this.objInterfaceBean.getPatientInvolvementCode());
				 * childNode.put(compFields); }
				 */
			}
			// ETQCR-808
			else if (this.objInterfaceBean.getPaeQuestionAns2() != null
					&& this.objInterfaceBean.getPaeQuestionAns2().length() > 0) {
				String strMapWasProceTherVal = this.objInterfaceBean.getMapResultSet().get("86");
				if (strMapWasProceTherVal != null && strMapWasProceTherVal.length() > 0) {
					String strWasPrcVal = this.objValidation
							.validateWasProcedureTheraDiaForCodes(strMapWasProceTherVal);
					if (strWasPrcVal != null && strWasPrcVal.length() > 0 && !strWasPrcVal.contains("Error")) {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "PROCEDURE_THERAPEUTIC_P");
						JSONArray procTherValObj = new JSONArray();
						procTherValObj.put(strWasPrcVal);
						compFields.put("Values", procTherValObj);
						childNode.put(compFields);
					} else {
						this.objInterfaceBean.setErrorMessage(strWasPrcVal);
					}
				}

				// Commented for ETQCR-931
				/*
				 * if(this.objInterfaceBean.getPatientInvolvementCode()!=null &&
				 * this.objInterfaceBean.getPatientInvolvementCode().size()>0) { compFields =
				 * new LinkedHashMap<String, Object>(1);
				 * compFields.put("fieldName","CH_PATINT_INVOLV_CODE_P");
				 * compFields.put("Values", this.objInterfaceBean.getPatientInvolvementCode());
				 * childNode.put(compFields); }
				 */
			}

			if (!strPhase.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_DRAFT")) {
				if (compRes.get("37") != null && compRes.get("37").length() > 0
						&& !compRes.get("37").contains("1900")) {
					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "PRODUCT_RECEIPT_DATE_P");
					JSONArray prdRecDtValObj = new JSONArray();
					prdRecDtValObj.put(getDateByTimeZone(compRes.get("37"), this.objInterfaceBean.getTimeZones()));
					compFields.put("Values", prdRecDtValObj);
					childNode.put(compFields);
				}

				String strEvalRes = compRes.get("38");
				if (strEvalRes == null) {
					if (compRes.get("109") != null) {
						strEvalRes = compRes.get("109");
					}
				}

				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "CH_EVAL_RESULT_P");
				JSONArray evalResValObj = new JSONArray();
				evalResValObj.put(strEvalRes);
				compFields.put("Values", evalResValObj);
				childNode.put(compFields);

				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "EVALUATION_RESULT_LOC_LANG_P");
				JSONArray evalRestLocValObj = new JSONArray();
				evalRestLocValObj.put(compRes.get("109"));
				compFields.put("Values", evalRestLocValObj);
				childNode.put(compFields);

				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "EVALUATION_COMPLETION_DATE_P");
				JSONArray evalComDtValObj = new JSONArray();
				evalComDtValObj.put(getFormatedDate(compRes.get("39")));
				compFields.put("Values", evalComDtValObj);
				childNode.put(compFields);

				String strEvalComBy = compRes.get("40");
				if (strEvalComBy != null && strEvalComBy.length() > 0) {
					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "EVALUATION_COMPLETED_BY_TX_P");
					JSONArray evalComByValObj = new JSONArray();
					evalComByValObj.put(strEvalComBy);
					compFields.put("Values", evalComByValObj);
					childNode.put(compFields);
				} else {
					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "EVALUATION_COMPLETED_BY_TX_P");
					JSONArray evalComByValObj = new JSONArray();
					evalComByValObj.put("");
					compFields.put("Values", evalComByValObj);
					childNode.put(compFields);
				}

				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "EVALUATION_INITIATION_DATE_P");
				JSONArray evalInitDtValObj = new JSONArray();
				//prdRecDtValObj.put(getDateByTimeZone(rs.getString(14),this.objInterfaceBean.getTimeZones()));
				//evalInitDtValObj.put(getDateByTimeZone(compRes.get("54"),this.objInterfaceBean.getTimeZones()));
				System.out.println("Value of RS 54 "+compRes.get("54"));
				evalInitDtValObj.put(getFormatedDate(compRes.get("54")));
				System.out.println("Result of RS 54 "+getFormatedDate(compRes.get("54")));
				compFields.put("Values", evalInitDtValObj);
				childNode.put(compFields);

				String strInvComBy = compRes.get("43");
				if (strInvComBy != null && strInvComBy.length() > 0) {
					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "NO_INV_DECISION_BY_TX_P");
					JSONArray invDecByValObj = new JSONArray();
					invDecByValObj.put(strInvComBy);
					compFields.put("Values", invDecByValObj);
					childNode.put(compFields);
				} else {
					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "NO_INV_DECISION_BY_TX_P");
					JSONArray invDecByValObj = new JSONArray();
					invDecByValObj.put("");
					compFields.put("Values", invDecByValObj);
					childNode.put(compFields);
				}

				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "NO_INVESTIGATION_DECISION_DATE_P");
				JSONArray invDecDtValObj = new JSONArray();
				invDecDtValObj.put(getFormatedDate(compRes.get("44")));
				compFields.put("Values", invDecDtValObj);
				childNode.put(compFields);

				if (this.objInterfaceBean.getInvestigationRequired() != null
						&& this.objInterfaceBean.getInvestigationRequired().length() > 0) {
					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "CH_INVEST_REQ_P");
					JSONArray isInvReqValObj = new JSONArray();
					isInvReqValObj.put(this.objInterfaceBean.getInvestigationRequired());
					compFields.put("Values", isInvReqValObj);
					childNode.put(compFields);

					if (this.objInterfaceBean.getInvestigationRequired().equalsIgnoreCase("No")) {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "NO_INVESTIGATION_RATIONALE_P");
						JSONArray invRetionaleValObj = new JSONArray();
						invRetionaleValObj.put(compRes.get("42"));
						compFields.put("Values", invRetionaleValObj);
						childNode.put(compFields);
					}
				}

				String strCorrDesc = compRes.get("45");
				if (strCorrDesc == null) {
					if (compRes.get("110") != null) {
						strCorrDesc = compRes.get("110");
					}
				}

				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "CORRECTION_DESCRIPTION_P");
				JSONArray corrDecValObj = new JSONArray();
				corrDecValObj.put(strCorrDesc);
				compFields.put("Values", corrDecValObj);
				childNode.put(compFields);

				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "CORRECTION_DESC_LOC_LANG_P");
				JSONArray corrDescLocObj = new JSONArray();
				corrDescLocObj.put(compRes.get("110"));
				compFields.put("Values", corrDescLocObj);
				childNode.put(compFields);

				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", "CH_RATIONALE_FOR_NO_ACT_P");
				JSONArray corrActValObj = new JSONArray();
				corrActValObj.put(compRes.get("46"));
				compFields.put("Values", corrActValObj);
				childNode.put(compFields);

				if (this.objInterfaceBean.getIsWarrantyRequired() != null
						&& this.objInterfaceBean.getIsWarrantyRequired().length() > 0) {
					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "CH_WARRANTY_REVU_RQD_P");
					JSONArray wrtReqValObj = new JSONArray();
					wrtReqValObj.put(this.objInterfaceBean.getIsWarrantyRequired());
					compFields.put("Values", wrtReqValObj);
					childNode.put(compFields);
				}

				if (strPhase.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED")) {
					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "CH_COMP_CLOS_DATE_1_P");
					JSONArray closedCompDtValObj = new JSONArray();
//					closedCompDtValObj.put(getDateByTimeZone(compRes.get("47"), this.objInterfaceBean.getTimeZones()));
					closedCompDtValObj.put(getFormatedDate(compRes.get("47")));
					compFields.put("Values", closedCompDtValObj);
					childNode.put(compFields);

					String strComCloseBy = compRes.get("48");
					if (strComCloseBy != null && strComCloseBy.length() > 0) {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "CH_CLOSED_BY_TEXT_P");
						JSONArray closedCompByValObj = new JSONArray();
						closedCompByValObj.put(strComCloseBy);
						compFields.put("Values", closedCompByValObj);
						childNode.put(compFields);
					} else {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "CH_CLOSED_BY_TEXT_P");
						JSONArray closedCompByValObj = new JSONArray();
						closedCompByValObj.put("");
						compFields.put("Values", closedCompByValObj);
						childNode.put(compFields);
					}

					// Added for ETQCR-1031
					if (this.objInterfaceBean.getItemType() == null || (this.objInterfaceBean.getItemType() != null
							&& !this.objInterfaceBean.getItemType().contains("Scientific"))) {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "CH_TIER_LEVEL_P");
						JSONArray tierValObj = new JSONArray();
						tierValObj.put("Tier 3");
						compFields.put("Values", tierValObj);
						childNode.put(compFields);
					}
				}

			}

			/*****************************************
			 * Set Attachment Fields
			 **********************************************************/
			ArrayList<Object> attchObj = getAttachmentForComplaint();
			if (attchObj != null && attchObj.size() > 0 && attchObj.get(0) != null) {
				compFields = new LinkedHashMap<String, Object>(1);
				compFields.put("fieldName", attchObj.get(0));
				compFields.put("attachmentType", "base64");
				compFields.put("Values", attchObj.get(1));
				childNode.put(compFields);
			}

			/*------------------------Set System Source Reference Number Subform--------------------------------------------*/
			JSONObject subFormMap = new JSONObject();
			JSONArray sbRecordNode = new JSONArray();
			subFormMap.put("SubformName", "REFERENCE_NUMBERS_P");
			JSONArray subChildNode = new JSONArray();

			JSONObject subRObj = new JSONObject();
			subRObj.put("recordId", "");
			subRObj.put("recordOrder", "0");

			Map<String, Object> sbFields = new LinkedHashMap<String, Object>(1);
			sbFields.put("fieldName", "ADDITIONAL_REFERENCE_NO_P");
			JSONArray refNoValObj = new JSONArray();
			refNoValObj.put(compRes.get("2"));
			sbFields.put("Values", refNoValObj);
			subChildNode.put(sbFields);

			if (this.objInterfaceBean.getSystemSource() != null
					&& this.objInterfaceBean.getSystemSource().length() > 0) {
				sbFields = new LinkedHashMap<String, Object>(1);
				sbFields.put("fieldName", "SYSTEM_SOURCE1_P");
				JSONArray systemSourceValObj = new JSONArray();
				systemSourceValObj.put(this.objInterfaceBean.getSystemSource());
				sbFields.put("Values", systemSourceValObj);
				subChildNode.put(sbFields);
			}

			if (compRes.get("58") != null && compRes.get("58").length() > 0 && !compRes.get("58").contains("1900")) {
				sbFields = new LinkedHashMap<String, Object>(1);
				sbFields.put("fieldName", "SOURCE_SYSTEM_INITIATION_DATE_P");
				JSONArray sysSrInitDateValObj = new JSONArray();
				sysSrInitDateValObj.put(getDateByTimeZone(compRes.get("58"), this.objInterfaceBean.getTimeZones()));
				sbFields.put("Values", sysSrInitDateValObj);
				subChildNode.put(sbFields);
			}

			subRObj.put("Fields", subChildNode);
			sbRecordNode.put(subRObj);

			if (compRes.get("89") != null && compRes.get("89").length() > 0 && compRes.get("90") != null
					&& compRes.get("90").length() > 0) {
				JSONArray subChild2Node = new JSONArray();

				JSONObject subR2Obj = new JSONObject();
				subR2Obj.put("recordId", "");
				subR2Obj.put("recordOrder", "1");

				Map<String, Object> sb2Fields = new LinkedHashMap<String, Object>(1);
				sb2Fields.put("fieldName", "ADDITIONAL_REFERENCE_NO_P");
				JSONArray refNo2ValObj = new JSONArray();
				refNo2ValObj.put(compRes.get("90"));
				sb2Fields.put("Values", refNo2ValObj);
				subChild2Node.put(sb2Fields);

				if (this.objInterfaceBean.getSystemSource2() != null
						&& this.objInterfaceBean.getSystemSource2().length() > 0) {
					sb2Fields = new LinkedHashMap<String, Object>(1);
					sb2Fields.put("fieldName", "SYSTEM_SOURCE1_P");
					JSONArray systemSource2ValObj = new JSONArray();
					systemSource2ValObj.put(this.objInterfaceBean.getSystemSource2());
					sb2Fields.put("Values", systemSource2ValObj);
					subChild2Node.put(sb2Fields);
				}

				if (compRes.get("94") != null && compRes.get("94").length() > 0
						&& !compRes.get("94").contains("1900")) {
					sb2Fields = new LinkedHashMap<String, Object>(1);
					sb2Fields.put("fieldName", "SOURCE_SYSTEM_INITIATION_DATE_P");
					JSONArray sysSrInitDate2ValObj = new JSONArray();
					sysSrInitDate2ValObj
							.put(getDateByTimeZone(compRes.get("94"), this.objInterfaceBean.getTimeZones()));
					sb2Fields.put("Values", sysSrInitDate2ValObj);
					subChild2Node.put(sb2Fields);
				}

				subR2Obj.put("Fields", subChild2Node);
				sbRecordNode.put(subR2Obj);

			}

			subFormMap.put("SubformRecords", sbRecordNode);

			JSONArray sbMainNode = new JSONArray();
			sbMainNode.put(subFormMap);

			/*------------------------Set Product Information Subform--------------------------------------------*/
			JSONObject modelSbFormMap = new JSONObject();
			JSONArray modelSBChildNode = new JSONArray();
			modelSbFormMap.put("SubformName", "COMPLAINTS_PRODUCTINFO");

			JSONObject modelSbRObj = new JSONObject();
			modelSbRObj.put("recordId", "");
			modelSbRObj.put("recordOrder", "0");

			Map<String, Object> modelSbFields = new LinkedHashMap<String, Object>(1);
			if (this.objInterfaceBean.getModelNumber() != null && this.objInterfaceBean.getModelNumber().length() > 0) {
				modelSbFields.put("fieldName", "MODEL_NUMBER_WS_P");
				JSONArray modelNoValObj = new JSONArray();
				modelNoValObj.put(this.objInterfaceBean.getModelNumber());
				modelSbFields.put("Values", modelNoValObj);
				modelSBChildNode.put(modelSbFields);
			}

			modelSbFields = new LinkedHashMap<String, Object>(1);
			modelSbFields.put("fieldName", "CH_WILL_PRD_BE_RTRN_OLY_P");
			JSONArray prdToRetValObj = new JSONArray();
			prdToRetValObj.put(getBooleanValue(compRes.get("4")));
			modelSbFields.put("Values", prdToRetValObj);
			modelSBChildNode.put(modelSbFields);

			modelSbFields = new LinkedHashMap<String, Object>(1);
			modelSbFields.put("fieldName", "CH_PRD_QTTY_1_P");
			JSONArray prdQtyValObj = new JSONArray();
			prdQtyValObj.put(this.objInterfaceBean.getProductQuntity());
			modelSbFields.put("Values", prdQtyValObj);
			modelSBChildNode.put(modelSbFields);

			if (compRes.get("28") != null && compRes.get("28").length() > 0) {
				modelSbFields = new LinkedHashMap<String, Object>(1);
				modelSbFields.put("fieldName", "SERIAL_NUMBER_P");
				JSONArray prdLotSerialValObj = new JSONArray();
				prdLotSerialValObj.put(compRes.get("28"));
				modelSbFields.put("Values", prdLotSerialValObj);
				modelSBChildNode.put(modelSbFields);
			} else {
				modelSbFields = new LinkedHashMap<String, Object>(1);
				modelSbFields.put("fieldName", "SERIAL_NUMBER_P");
				JSONArray prdLotSerialValObj = new JSONArray();
				prdLotSerialValObj.put("Unknown");
				modelSbFields.put("Values", prdLotSerialValObj);
				modelSBChildNode.put(modelSbFields);
			}

			modelSbFields = new LinkedHashMap<String, Object>(1);
			modelSbFields.put("fieldName", "CH_PRODUCT_ERROR_CODE_P");
			JSONArray prdErrCodeValObj = new JSONArray();
			prdErrCodeValObj.put(compRes.get("29"));
			modelSbFields.put("Values", prdErrCodeValObj);
			modelSBChildNode.put(modelSbFields);

			if (compRes.get("36") != null && compRes.get("36").length() > 0 && !compRes.get("36").contains("1900")) {
				modelSbFields = new LinkedHashMap<String, Object>(1);
				modelSbFields.put("fieldName", "CH_PURCH_DATE_P");
				JSONArray purchDtValObj = new JSONArray();
				purchDtValObj.put(getDateByTimeZone(compRes.get("36"), this.objInterfaceBean.getTimeZones()));
				modelSbFields.put("Values", purchDtValObj);
				modelSBChildNode.put(modelSbFields);
			}

			// Added for ETQCR-944
			if (compRes.get("111") != null && compRes.get("111").length() > 0) {
				modelSbFields = new LinkedHashMap<String, Object>(1);
				modelSbFields.put("fieldName", "CH_PI_ITEM_UDI_P");
				JSONArray itemUDIValObj = new JSONArray();
				itemUDIValObj.put(compRes.get("111"));
				modelSbFields.put("Values", itemUDIValObj);
				modelSBChildNode.put(modelSbFields);
			}

			// Added for ETQCR-824 but CR is rejected
			/*
			 * if(compRes.get("111")!=null && compRes.get("111").length()>0 &&
			 * !compRes.get("111").contains("1900")) { modelSbFields = new
			 * LinkedHashMap<String, Object>(1);
			 * modelSbFields.put("fieldName","CH_EXPIRATION_DATE_P"); JSONArray expDtValObj
			 * = new JSONArray();
			 * 
			 * DateFormat fromFormatter = new SimpleDateFormat("yyyy-MM-dd"); Date expDate =
			 * fromFormatter.parse(compRes.get("111")); expDtValObj.put(new
			 * SimpleDateFormat("MMM dd, yyyy").format(expDate));
			 * modelSbFields.put("Values", expDtValObj);
			 * modelSBChildNode.put(modelSbFields); }
			 */

			if (compRes.get("60") != null && compRes.get("60").length() > 0 && !compRes.get("60").contains("1900")) {
				modelSbFields = new LinkedHashMap<String, Object>(1);
				modelSbFields.put("fieldName", "CH_INSTALL_DATE_1_P");
				JSONArray installDtValObj = new JSONArray();
				installDtValObj.put(getDateByTimeZone(compRes.get("60"), this.objInterfaceBean.getTimeZones()));
				modelSbFields.put("Values", installDtValObj);
				modelSBChildNode.put(modelSbFields);
			}

			if (this.objInterfaceBean.getOrderType() != null && this.objInterfaceBean.getOrderType().length() > 0) {
				modelSbFields = new LinkedHashMap<String, Object>(1);
				modelSbFields.put("fieldName", "CH_ORDER_TYPE_P");
				JSONArray orderTypeValObj = new JSONArray();
				orderTypeValObj.put(this.objInterfaceBean.getOrderType());
				modelSbFields.put("Values", orderTypeValObj);
				modelSBChildNode.put(modelSbFields);
			}

			String strCdsMethod = compRes.get("69");
			if (strCdsMethod == null) {
				if (compRes.get("107") != null) {
					strCdsMethod = compRes.get("107");
				}
			}

			modelSbFields = new LinkedHashMap<String, Object>(1);
			modelSbFields.put("fieldName", "CH_PD_CDS_METHOD_P");
			JSONArray cdsMethodValObj = new JSONArray();
			cdsMethodValObj.put(strCdsMethod);
			modelSbFields.put("Values", cdsMethodValObj);
			modelSBChildNode.put(modelSbFields);

			modelSbFields = new LinkedHashMap<String, Object>(1);
			modelSbFields.put("fieldName", "CDS_METHOD_LOCAL_LANGUAGE_P");
			JSONArray cdsMethodLocValObj = new JSONArray();
			cdsMethodLocValObj.put(compRes.get("107"));
			modelSbFields.put("Values", cdsMethodLocValObj);
			modelSBChildNode.put(modelSbFields);

			if (this.objInterfaceBean.getIsProductReturn() != null
					&& this.objInterfaceBean.getIsProductReturn().length() > 0) {
				if (this.objInterfaceBean.getIsProductReturn().contains("Yes")) {
					modelSbFields = new LinkedHashMap<String, Object>(1);
					modelSbFields.put("fieldName", "CH_CARRIER_P");
					JSONArray carrierValObj = new JSONArray();
					carrierValObj.put(compRes.get("70"));
					modelSbFields.put("Values", carrierValObj);
					modelSBChildNode.put(modelSbFields);

					modelSbFields = new LinkedHashMap<String, Object>(1);
					modelSbFields.put("fieldName", "CH_TRACKING_NUMBER_1_P");
					JSONArray trackNoValObj = new JSONArray();
					trackNoValObj.put(compRes.get("71"));
					modelSbFields.put("Values", trackNoValObj);
					modelSBChildNode.put(modelSbFields);

					modelSbFields = new LinkedHashMap<String, Object>(1);
					modelSbFields.put("fieldName", "CH_ND_RTN_ITM_CUST_AFTR_INV_P");
					JSONArray needToRetValObj = new JSONArray();
					needToRetValObj.put(getYesNo(compRes.get("72")));
					modelSbFields.put("Values", needToRetValObj);
					modelSBChildNode.put(modelSbFields);
				}
			}

			if (this.objInterfaceBean.getCdsMethodBeforeRetOly() != null
					&& this.objInterfaceBean.getCdsMethodBeforeRetOly().length() > 0) {
				if (this.objInterfaceBean.getIsProductReturn() != null
						&& this.objInterfaceBean.getIsProductReturn().equalsIgnoreCase("Yes")) {
					modelSbFields = new LinkedHashMap<String, Object>(1);
					modelSbFields.put("fieldName", "CH_CDS_MTHD_B4_RET_DEV_TO_OLY_P");
					JSONArray beforeRetOlyValObj = new JSONArray();
					beforeRetOlyValObj.put(this.objInterfaceBean.getCdsMethodBeforeRetOly());
					modelSbFields.put("Values", beforeRetOlyValObj);
					modelSBChildNode.put(modelSbFields);

					if (this.objInterfaceBean.getCdsMethodBeforeRetOly().equalsIgnoreCase("Other")) {
						modelSbFields = new LinkedHashMap<String, Object>(1);
						modelSbFields.put("fieldName", "CH_CDS_METHOD_DETAILS_P");
						JSONArray cdsMthDetValObj = new JSONArray();
						cdsMthDetValObj.put(compRes.get("74"));
						modelSbFields.put("Values", cdsMthDetValObj);
						modelSBChildNode.put(modelSbFields);
					}
				}
			}

			String strOtherPrdInv = compRes.get("75");
			if (strOtherPrdInv == null) {
				if (compRes.get("108") != null) {
					strOtherPrdInv = compRes.get("108");
				}
			}

			modelSbFields = new LinkedHashMap<String, Object>(1);
			modelSbFields.put("fieldName", "CH_OTHER_PRODUCTS_INVOLVED_P");
			JSONArray otherPrdInvValObj = new JSONArray();
			otherPrdInvValObj.put(strOtherPrdInv);
			modelSbFields.put("Values", otherPrdInvValObj);
			modelSBChildNode.put(modelSbFields);

			modelSbFields = new LinkedHashMap<String, Object>(1);
			modelSbFields.put("fieldName", "OTHER_PRO_INV_LOC_LANG_P");
			JSONArray otherPrdInvLocValObj = new JSONArray();
			otherPrdInvLocValObj.put(compRes.get("108"));
			modelSbFields.put("Values", otherPrdInvLocValObj);
			modelSBChildNode.put(modelSbFields);

			if (this.objInterfaceBean.getIsSoftware() != null && this.objInterfaceBean.getIsSoftware().length() > 0) {
				modelSbFields = new LinkedHashMap<String, Object>(1);
				modelSbFields.put("fieldName", "CH_SOFTWRE");
				JSONArray prdSoftWareValObj = new JSONArray();
				prdSoftWareValObj.put(this.objInterfaceBean.getIsSoftware());
				modelSbFields.put("Values", prdSoftWareValObj);
				modelSBChildNode.put(modelSbFields);
			}

			if (this.objInterfaceBean.getIsSoftware() != null
					&& this.objInterfaceBean.getIsSoftware().equalsIgnoreCase("Yes")) {
				if (this.objInterfaceBean.getSoftwareVersion() != null
						&& this.objInterfaceBean.getSoftwareVersion().length() > 0) {
					modelSbFields = new LinkedHashMap<String, Object>(1);
					modelSbFields.put("fieldName", "CH_SOFTWRE_VERSION_P");
					JSONArray prdSoftWareVerValObj = new JSONArray();
					prdSoftWareVerValObj.put(this.objInterfaceBean.getSoftwareVersion());
					modelSbFields.put("Values", prdSoftWareVerValObj);
					modelSBChildNode.put(modelSbFields);
				}

				if (this.objInterfaceBean.getOperatingSystemVersion() != null
						&& this.objInterfaceBean.getOperatingSystemVersion().length() > 0) {
					modelSbFields = new LinkedHashMap<String, Object>(1);
					modelSbFields.put("fieldName", "CH_OPERATING_SYS_VER_P");
					JSONArray osVerValObj = new JSONArray();
					osVerValObj.put(this.objInterfaceBean.getOperatingSystemVersion());
					modelSbFields.put("Values", osVerValObj);
					modelSBChildNode.put(modelSbFields);
				}

				modelSbFields = new LinkedHashMap<String, Object>(1);
				modelSbFields.put("fieldName", "CH_HRDWRE_TYP_P");
				JSONArray hardWareTypeValObj = new JSONArray();
				hardWareTypeValObj.put(compRes.get("78"));
				modelSbFields.put("Values", hardWareTypeValObj);
				modelSBChildNode.put(modelSbFields);

				modelSbFields = new LinkedHashMap<String, Object>(1);
				modelSbFields.put("fieldName", "CH_HRDWRE_SERIAL_P");
				JSONArray hwSerValObj = new JSONArray();
				hwSerValObj.put(compRes.get("79"));
				modelSbFields.put("Values", hwSerValObj);
				modelSBChildNode.put(modelSbFields);

				modelSbFields = new LinkedHashMap<String, Object>(1);
				modelSbFields.put("fieldName", "CH_MFR_MODEL_P");
				JSONArray manModelNoValObj = new JSONArray();
				manModelNoValObj.put(compRes.get("80"));
				modelSbFields.put("Values", manModelNoValObj);
				modelSBChildNode.put(modelSbFields);

				modelSbFields = new LinkedHashMap<String, Object>(1);
				modelSbFields.put("fieldName", "CH_MFR_MODEL_SERIAL_P");
				JSONArray manModelSerNoValObj = new JSONArray();
				manModelSerNoValObj.put(compRes.get("81"));
				modelSbFields.put("Values", manModelSerNoValObj);
				modelSBChildNode.put(modelSbFields);

				if (compRes.get("82") != null && compRes.get("82").length() > 0) {
					modelSbFields = new LinkedHashMap<String, Object>(1);
					modelSbFields.put("fieldName", "CH_RMT_SPPRT_CONNECT_P");
					JSONArray remoteSupValObj = new JSONArray();
					remoteSupValObj.put(getBooleanValue(compRes.get("82")));
					modelSbFields.put("Values", remoteSupValObj);
					modelSBChildNode.put(modelSbFields);
				}

				if (compRes.get("83") != null && compRes.get("83").length() > 0) {
					modelSbFields = new LinkedHashMap<String, Object>(1);
					modelSbFields.put("fieldName", "CH_CUST_SUPP_HDWRE_P");
					JSONArray custSupHwValObj = new JSONArray();
					custSupHwValObj.put(getBooleanValue(compRes.get("83")));
					modelSbFields.put("Values", custSupHwValObj);
					modelSBChildNode.put(modelSbFields);
				}

				modelSbFields = new LinkedHashMap<String, Object>(1);
				modelSbFields.put("fieldName", "CH_ANTI_VIRUS_SFTWR_P");
				JSONArray antiSwValObj = new JSONArray();
				antiSwValObj.put(compRes.get("84"));
				modelSbFields.put("Values", antiSwValObj);
				modelSBChildNode.put(modelSbFields);
			}

			modelSbFields = new LinkedHashMap<String, Object>(1);
			modelSbFields.put("fieldName", "CH_COMPLAINT_PRODUCT_COMMENT_P");
			JSONArray prdCmtValObj = new JSONArray();
			prdCmtValObj.put(compRes.get("85"));
			modelSbFields.put("Values", prdCmtValObj);
			modelSBChildNode.put(modelSbFields);

			modelSbRObj.put("Fields", modelSBChildNode);

			JSONArray modelsbRecordNode = new JSONArray();
			modelsbRecordNode.put(modelSbRObj);
			modelSbFormMap.put("SubformRecords", modelsbRecordNode);

			sbMainNode.put(modelSbFormMap);

			/*------------------------Set Customer Information Subform--------------------------------------------*/
			JSONObject custSBFormMap = new JSONObject();
			JSONArray custSBChildNode = new JSONArray();
			custSBFormMap.put("SubformName", "CUSTOMER_INFO_SUBFORM_P");

			JSONObject custSBObj = new JSONObject();
			custSBObj.put("recordId", "");
			custSBObj.put("recordOrder", "0");

			Map<String, Object> custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "CUSTOMER_ACCOUNT_CODE_P");
			JSONArray custAccValObj = new JSONArray();
			custAccValObj.put(compRes.get("17"));
			custSBFields.put("Values", custAccValObj);
			custSBChildNode.put(custSBFields);

			String strCustName = compRes.get("21");
			if (strCustName == null) {
				if (compRes.get("98") != null) {
					strCustName = compRes.get("98");
				}
			}
			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "CUSTOMER_NAME_P");
			JSONArray custNameValObj = new JSONArray();
			custNameValObj.put(strCustName);
			custSBFields.put("Values", custNameValObj);
			custSBChildNode.put(custSBFields);

			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "CUSTOMER_NAME_LOC_LANG_1_P");
			JSONArray custNameLocValObj = new JSONArray();
			custNameLocValObj.put(compRes.get("98"));
			custSBFields.put("Values", custNameLocValObj);
			custSBChildNode.put(custSBFields);

			String strCustDept = compRes.get("67");
			if (strCustDept == null) {
				if (compRes.get("104") != null) {
					strCustDept = compRes.get("104");
				}
			}
			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "DEPARTMENT_P");
			JSONArray custDeptValObj = new JSONArray();
			custDeptValObj.put(strCustDept);
			custSBFields.put("Values", custDeptValObj);
			custSBChildNode.put(custSBFields);

			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "DEPARTMENT_LOCAL_LANGUAGE_P");
			JSONArray custDeptLocValObj = new JSONArray();
			custDeptLocValObj.put(compRes.get("104"));
			custSBFields.put("Values", custDeptLocValObj);
			custSBChildNode.put(custSBFields);

			String strCustAdd = compRes.get("23");
			if (strCustAdd == null) {
				if (compRes.get("100") != null) {
					strCustAdd = compRes.get("100");
				}
			}
			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "ADDRESS_P");
			JSONArray custAddValObj = new JSONArray();
			custAddValObj.put(strCustAdd);
			custSBFields.put("Values", custAddValObj);
			custSBChildNode.put(custSBFields);

			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "ADDRESS_LOCAL_LANG_1_P");
			JSONArray custAddLocValObj = new JSONArray();
			custAddLocValObj.put(compRes.get("100"));
			custSBFields.put("Values", custAddLocValObj);
			custSBChildNode.put(custSBFields);

			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "POSTAL_CODE_P");
			JSONArray custZipValObj = new JSONArray();
			custZipValObj.put(compRes.get("18"));
			custSBFields.put("Values", custZipValObj);
			custSBChildNode.put(custSBFields);

			String strCustTel = compRes.get("19");
			if (strCustTel == null) {
				if (compRes.get("97") != null) {
					strCustTel = compRes.get("97");
				}
			}

			if (this.objInterfaceBean.getCustomerCountry() != null
					&& this.objInterfaceBean.getCustomerCountry().equalsIgnoreCase("United States")) {
				custSBFields = new LinkedHashMap<String, Object>(1);
				custSBFields.put("fieldName", "TELEPHONE_P");
				JSONArray custTelValObj = new JSONArray();
				custTelValObj.put(this.objInterfaceBean.getCustomerTelephone());
				custSBFields.put("Values", custTelValObj);
				custSBChildNode.put(custSBFields);
			} else {
				custSBFields = new LinkedHashMap<String, Object>(1);
				custSBFields.put("fieldName", "TELEPHONE_P");
				JSONArray custTelValObj = new JSONArray();
				custTelValObj.put(strCustTel);
				custSBFields.put("Values", custTelValObj);
				custSBChildNode.put(custSBFields);
			}
			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "TELEPHONE_LOCAL_LANGUAGE_P");
			JSONArray custTelLocValObj = new JSONArray();
			custTelLocValObj.put(compRes.get("97"));
			custSBFields.put("Values", custTelLocValObj);
			custSBChildNode.put(custSBFields);

			String strcustFax = compRes.get("68");
			if (strcustFax == null) {
				if (compRes.get("105") != null) {
					strcustFax = compRes.get("105");
				}
			}

			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "FAX_P");
			JSONArray custFaxValObj = new JSONArray();
			custFaxValObj.put(strcustFax);
			custSBFields.put("Values", custFaxValObj);
			custSBChildNode.put(custSBFields);

			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "FAX_LOCAL_LANGUAGE_1_P");
			JSONArray custFaxLocValObj = new JSONArray();
			custFaxLocValObj.put(compRes.get("105"));
			custSBFields.put("Values", custFaxLocValObj);
			custSBChildNode.put(custSBFields);

			String strCustCity = compRes.get("22");
			if (strCustCity == null) {
				if (compRes.get("99") != null) {
					strCustCity = compRes.get("99");
				}
			}
			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "CITY_P");
			JSONArray custCityValObj = new JSONArray();
			custCityValObj.put(strCustCity);
			custSBFields.put("Values", custCityValObj);
			custSBChildNode.put(custSBFields);

			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "CITY_LOCAL_LANGUAGE_1_P");
			JSONArray custCityLocValObj = new JSONArray();
			custCityLocValObj.put(compRes.get("99"));
			custSBFields.put("Values", custCityLocValObj);
			custSBChildNode.put(custSBFields);

			if (this.objInterfaceBean.getCustomerCountry() != null
					&& this.objInterfaceBean.getCustomerCountry().length() > 0) {
				custSBFields = new LinkedHashMap<String, Object>(1);
				custSBFields.put("fieldName", "COUNTRY_P");
				JSONArray custCountryValObj = new JSONArray();
				custCountryValObj.put(this.objInterfaceBean.getCustomerCountry());
				custSBFields.put("Values", custCountryValObj);
				custSBChildNode.put(custSBFields);
			}

			if (this.objInterfaceBean.getCustomerLocation() != null
					&& this.objInterfaceBean.getCustomerLocation().length() > 0) {
				custSBFields = new LinkedHashMap<String, Object>(1);
				custSBFields.put("fieldName", "CUSTOMER_LOCATION_P");
				JSONArray custLocValObj = new JSONArray();
				custLocValObj.put(this.objInterfaceBean.getCustomerLocation());
				custSBFields.put("Values", custLocValObj);
				custSBChildNode.put(custSBFields);
			} else {
				custSBFields = new LinkedHashMap<String, Object>(1);
				custSBFields.put("fieldName", "CUSTOMER_LOCATION_P");
				JSONArray custLocValObj = new JSONArray();
				custLocValObj.put(this.objInterfaceBean.getCustomerLocation());
				custSBFields.put("Values", custLocValObj);
				custSBChildNode.put(custSBFields);
			}

			String strCustState = this.objInterfaceBean.getCustomerState();
			if (strCustState == null) {
				if (compRes.get("101") != null) {
					strCustState = compRes.get("101");
				}
			}

			custSBFields = new LinkedHashMap<String, Object>(1);
			JSONArray custStateValObj = new JSONArray();
			if (this.objInterfaceBean.getCustomerCountry() != null
					&& this.objInterfaceBean.getCustomerCountry().equalsIgnoreCase("United States")) {
				if (this.objInterfaceBean.getCustomerState() != null
						&& this.objInterfaceBean.getCustomerState().length() > 0) {
					custSBFields.put("fieldName", "STATE_PROVINCE_COUNTY_2_P");
					custStateValObj.put(this.objInterfaceBean.getCustomerState());
					custSBFields.put("Values", custStateValObj);
					custSBChildNode.put(custSBFields);
				}
			} else {
				custSBFields.put("fieldName", "CH_CUSTOMER_STATE_TEXT_P");
				custStateValObj.put(this.objInterfaceBean.getCustomerState());
				custSBFields.put("Values", custStateValObj);
				custSBChildNode.put(custSBFields);
			}
			custSBObj.put("Fields", custSBChildNode);

			custSBFields = new LinkedHashMap<String, Object>(1);
			custSBFields.put("fieldName", "STATE_LOCAL_LANGUAGE_P");
			JSONArray custStateLocValObj = new JSONArray();
			custStateLocValObj.put(compRes.get("101"));
			custSBFields.put("Values", custStateLocValObj);
			custSBChildNode.put(custSBFields);

			JSONArray custSBRecordNode = new JSONArray();
			custSBRecordNode.put(custSBObj);
			custSBFormMap.put("SubformRecords", custSBRecordNode);

			sbMainNode.put(custSBFormMap);

			/*------------------------Set Contact Information Subform--------------------------------------------*/
			JSONObject contactSBFormMap = new JSONObject();
			JSONArray contactSBChildNode = new JSONArray();
			contactSBFormMap.put("SubformName", "COMPLAINTS_CONTACT");

			JSONObject contactSBObj = new JSONObject();
			contactSBObj.put("recordId", "");
			contactSBObj.put("recordOrder", "0");

			Map<String, Object> contactSBFields = new LinkedHashMap<String, Object>(1);
			if (this.objInterfaceBean.getOccupation() != null && this.objInterfaceBean.getOccupation().length() > 0) {
				contactSBFields.put("fieldName", "CI_OCCUPATION_P");
				JSONArray occupValObj = new JSONArray();
				occupValObj.put(this.objInterfaceBean.getOccupation());
				contactSBFields.put("Values", occupValObj);
				contactSBChildNode.put(contactSBFields);
			}

			contactSBFields = new LinkedHashMap<String, Object>(1);
			contactSBFields.put("fieldName", "CI_HEALTH_PROFESSIONAL_P");
			JSONArray healthProValObj = new JSONArray();
			// Modified for ETQCR-931
			if (this.objInterfaceBean.getHealthProfessional() != null
					&& this.objInterfaceBean.getHealthProfessional().length() > 0
					&& !this.objInterfaceBean.getHealthProfessional().equalsIgnoreCase("No Information")) {
				healthProValObj.put(this.objInterfaceBean.getHealthProfessional());
			}
			// Commented for ETQCR-931
			/*
			 * else { healthProValObj.put("No Information"); }
			 */
			contactSBFields.put("Values", healthProValObj);
			contactSBChildNode.put(contactSBFields);

			String strContactPer = compRes.get("32");
			if (strContactPer == null) {
				if (compRes.get("106") != null) {
					strContactPer = compRes.get("106");
				}
			}
			contactSBFields = new LinkedHashMap<String, Object>(1);
			contactSBFields.put("fieldName", "CONTACT_CONTACT_NAME");
			JSONArray contactPerNameValObj = new JSONArray();
			contactPerNameValObj.put(strContactPer);
			contactSBFields.put("Values", contactPerNameValObj);
			contactSBChildNode.put(contactSBFields);

			contactSBFields = new LinkedHashMap<String, Object>(1);
			contactSBFields.put("fieldName", "CONTACT_PERSON_LOCAL_LANGUAGE_P");
			JSONArray contactPerNameLocValObj = new JSONArray();
			contactPerNameLocValObj.put(compRes.get("106"));
			contactSBFields.put("Values", contactPerNameLocValObj);
			contactSBChildNode.put(contactSBFields);

			contactSBFields = new LinkedHashMap<String, Object>(1);
			contactSBFields.put("fieldName", "COMPLAINTS_EMAIL");
			JSONArray contactPerEmailValObj = new JSONArray();
			contactPerEmailValObj.put(compRes.get("33"));
			contactSBFields.put("Values", contactPerEmailValObj);
			contactSBChildNode.put(contactSBFields);

			if (this.objInterfaceBean.getCustomerCountry() != null
					&& this.objInterfaceBean.getCustomerCountry().equalsIgnoreCase("United States")) {
				contactSBFields = new LinkedHashMap<String, Object>(1);
				contactSBFields.put("fieldName", "CONTACT_CONTACT_PHONE_NUMBER");
				JSONArray contactTelValObj = new JSONArray();
				contactTelValObj.put(this.objInterfaceBean.getContactTelephone());
				contactSBFields.put("Values", contactTelValObj);
				contactSBChildNode.put(contactSBFields);
			} else {
				contactSBFields = new LinkedHashMap<String, Object>(1);
				contactSBFields.put("fieldName", "CONTACT_CONTACT_PHONE_NUMBER");
				JSONArray contactTelValObj = new JSONArray();
				contactTelValObj.put(compRes.get("34"));
				contactSBFields.put("Values", contactTelValObj);
				contactSBChildNode.put(contactSBFields);
			}

			contactSBObj.put("Fields", contactSBChildNode);

			JSONArray contactSBRecordNode = new JSONArray();
			contactSBRecordNode.put(contactSBObj);
			contactSBFormMap.put("SubformRecords", contactSBRecordNode);

			sbMainNode.put(contactSBFormMap);

			/*------------------------Set Coding Subform--------------------------------------------*/
			// Removed !strPhase.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_DRAFT") for
			// ETQCR-880
			if (this.objInterfaceBean.getCompCoding() != null && this.objInterfaceBean.getCompCoding().length() > 0) {
				JSONObject codingSbFormMap = new JSONObject();
				codingSbFormMap.put("SubformName", "CH_CODING_SUBFORM_P");
				codingSbFormMap.put("SubformRecords", this.objInterfaceBean.getCompCoding());
				sbMainNode.put(codingSbFormMap);
			}

			/*------------------------Set Decision Tree Subform--------------------------------------------*/
			JSONObject dtSbFormMap = new JSONObject();
			JSONArray dtSBChildNode = new JSONArray();
			JSONArray dtSbRecordNode = new JSONArray();

			dtSbFormMap.put("SubformName", "COMPLAINTS_DECTREE_P");

			JSONObject dtSbRObj = new JSONObject();
			dtSbRObj.put("recordId", "");
			dtSbRObj.put("recordOrder", "0");

			Map<String, Object> dtSbFields = new LinkedHashMap<String, Object>(1);
			dtSbFields.put("fieldName", "ETQ$DECISION_TREE_QUESTION");
			JSONArray dtQ1ValObj = new JSONArray();
			dtQ1ValObj.put("1) Was there a death or injury?");
			dtSbFields.put("Values", dtQ1ValObj);
			dtSBChildNode.put(dtSbFields);

			dtSbFields = new LinkedHashMap<String, Object>(1);
			dtSbFields.put("fieldName", "ETQ$DECISION_TREE_PROFILE_QUESTION_ID");
			JSONArray dtQidValObj = new JSONArray();
			dtQidValObj.put("142");
			dtSbFields.put("Values", dtQidValObj);
			dtSBChildNode.put(dtSbFields);

			dtSbFields = new LinkedHashMap<String, Object>(1);
			dtSbFields.put("fieldName", "ETQ$DECISION_TREE_FIRST_POSSIBLE_ANSWER");
			JSONArray dtfpaValObj = new JSONArray();
			dtfpaValObj.put("Yes");
			dtSbFields.put("Values", dtfpaValObj);
			dtSBChildNode.put(dtSbFields);

			dtSbFields = new LinkedHashMap<String, Object>(1);
			dtSbFields.put("fieldName", "ETQ$DECISION_TREE_SECOND_POSSIBLE_ANSWER");
			JSONArray dtspaValObj = new JSONArray();
			dtspaValObj.put("No");
			dtSbFields.put("Values", dtspaValObj);
			dtSBChildNode.put(dtSbFields);

			dtSbFields = new LinkedHashMap<String, Object>(1);
			dtSbFields.put("fieldName", "ETQ$DECISION_TREE_THIRD_POSSIBLE_ANSWER");
			JSONArray dttpaValObj = new JSONArray();
			dttpaValObj.put("Unknown");
			dtSbFields.put("Values", dttpaValObj);
			dtSBChildNode.put(dtSbFields);

			dtSbFields = new LinkedHashMap<String, Object>(1);
			dtSbFields.put("fieldName", "ETQ$DECISION_TREE_ANSWER");
			JSONArray dtAnsValObj = new JSONArray();
			dtAnsValObj.put(getDtAnswer(compRes.get("15")));
			dtSbFields.put("Values", dtAnsValObj);
			dtSBChildNode.put(dtSbFields);
			dtSbRObj.put("Fields", dtSBChildNode);

			dtSbRecordNode.put(dtSbRObj);

			if (compRes.get("15") != null && !compRes.get("15").contains("Y")) {

				/*---------------------------Set DT Question 2 --------------------------------*/
				JSONArray dtQ2SBChildNode = new JSONArray();
				JSONObject dtQ2RObj = new JSONObject();
				dtQ2RObj.put("recordId", "");
				dtQ2RObj.put("recordOrder", "1");

				Map<String, Object> dtQ2SbFields = new LinkedHashMap<String, Object>(1);
				dtQ2SbFields.put("fieldName", "ETQ$DECISION_TREE_QUESTION");
				JSONArray dtQ2ValObj = new JSONArray();
				dtQ2ValObj.put(
						"2) Is it likely that the malfunction or the reported event could cause or contribute to a death or injury?");
				dtQ2SbFields.put("Values", dtQ2ValObj);
				dtQ2SBChildNode.put(dtQ2SbFields);

				dtQ2SbFields = new LinkedHashMap<String, Object>(1);
				dtQ2SbFields.put("fieldName", "ETQ$DECISION_TREE_PROFILE_QUESTION_ID");
				JSONArray dtQ2idValObj = new JSONArray();
				dtQ2idValObj.put("143");
				dtQ2SbFields.put("Values", dtQ2idValObj);
				dtQ2SBChildNode.put(dtQ2SbFields);

				dtQ2SbFields = new LinkedHashMap<String, Object>(1);
				dtQ2SbFields.put("fieldName", "ETQ$DECISION_TREE_FIRST_POSSIBLE_ANSWER");
				JSONArray dtQ2fpaValObj = new JSONArray();
				dtQ2fpaValObj.put("Yes");
				dtQ2SbFields.put("Values", dtQ2fpaValObj);
				dtQ2SBChildNode.put(dtQ2SbFields);

				dtQ2SbFields = new LinkedHashMap<String, Object>(1);
				dtQ2SbFields.put("fieldName", "ETQ$DECISION_TREE_SECOND_POSSIBLE_ANSWER");
				JSONArray dtQ2spaValObj = new JSONArray();
				dtQ2spaValObj.put("No");
				dtQ2SbFields.put("Values", dtQ2spaValObj);
				dtQ2SBChildNode.put(dtQ2SbFields);

				dtQ2SbFields = new LinkedHashMap<String, Object>(1);
				dtQ2SbFields.put("fieldName", "ETQ$DECISION_TREE_THIRD_POSSIBLE_ANSWER");
				JSONArray dtQ2tpaValObj = new JSONArray();
				dtQ2tpaValObj.put("Unknown");
				dtQ2SbFields.put("Values", dtQ2tpaValObj);
				dtQ2SBChildNode.put(dtQ2SbFields);

				dtQ2SbFields = new LinkedHashMap<String, Object>(1);
				dtQ2SbFields.put("fieldName", "ETQ$DECISION_TREE_ANSWER");
				JSONArray dtQ2AnsValObj = new JSONArray();
				// Modified for ETQCR-760 & ETQCR-850
				if (this.objInterfaceBean.getPaeQuestionAns2() != null
						&& this.objInterfaceBean.getPaeQuestionAns2().length() > 0) {
					dtQ2AnsValObj.put(this.objInterfaceBean.getPaeQuestionAns2());
				} else {
					dtQ2AnsValObj.put(getDtAnswer(compRes.get("16")));
				}
				dtQ2SbFields.put("Values", dtQ2AnsValObj);
				dtQ2SBChildNode.put(dtQ2SbFields);
				dtQ2RObj.put("Fields", dtQ2SBChildNode);

				dtSbRecordNode.put(dtQ2RObj);
			}
			dtSbFormMap.put("SubformRecords", dtSbRecordNode);
			sbMainNode.put(dtSbFormMap);

			parentNode.put("Subforms", sbMainNode);
//			System.out.println(parent.toString());

		} catch (Exception e) {
			logger.error("Error: ", e);
		}
		return parent;
	}

	public String createComplaint(String strPhaseName, String strSecondaryPhase, JSONObject jsonString,
			String strDate) {
		String eMsg = "SUCCESS";
		HttpURLConnection connection = null;
		try {
			String strEtQURL = this.objInterfaceBean.getEtQURL();
			String strSvrCred = this.objInterfaceBean.getServerCred();
			String strInterfaceRecordID = this.objInterfaceBean.getInterfaceRecordId();
			String strAssigned = this.objInterfaceBean.getAssignedTo();
			URL url = null;

			if (!strPhaseName.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED")) {
				url = new URL(strEtQURL + "/documents?assignedusers=" + strAssigned + "&Duedate=" + strDate
						+ "&allsystemfields=true");
			} else if (strSecondaryPhase != null && strSecondaryPhase.length() > 0) {
				url = new URL(strEtQURL + "/documents?assignedusers=" + strAssigned + "&Duedate=" + strDate
						+ "&allsystemfields=true");
			} else {
				url = new URL(strEtQURL + "/documents");
			}

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setReadTimeout(180000);
			connection.setRequestMethod("POST");

			String encoded = Base64.getEncoder().encodeToString((strSvrCred).getBytes(StandardCharsets.UTF_8));

			connection.setRequestProperty("Authorization", "Basic " + encoded);

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

				Gson gson = new Gson();
				String strSb = sb.toString();
				strSb = strSb.replaceAll("\\[", "");
				strSb = strSb.replaceAll("\\]", "");
				Map<String, String> map = gson.fromJson(strSb, new TypeToken<HashMap<String, String>>() {
				}.getType());

				if (map.get("Messages").contains("Operation done successfully")) {
					this.objInterfaceBean.setComplaintDocumentId(map.get("documentId"));
					String strDocumentNumber = getDocumentNumberById("COMPLAINT_DOCUMENT", "COMPLAINT_DOCUMENT_ID",
							this.objInterfaceBean.getComplaintDocumentId());
					logger.info("Complaint Number: " + strDocumentNumber + " & Complait Document ID: "
							+ this.objInterfaceBean.getComplaintDocumentId());
					this.objInterfaceBean.setComplaintNumber(strDocumentNumber);
					updateComplaintTables(this.objInterfaceBean, eMsg, strDocumentNumber);

					if ((this.objInterfaceBean.getPhaseName() != null
							&& this.objInterfaceBean.getPhaseName().length() > 0
							&& this.objInterfaceBean.getPhaseName()
									.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_REVIEW"))
							|| (this.objInterfaceBean.getPhaseName() != null
									&& this.objInterfaceBean.getPhaseName().length() > 0
									&& this.objInterfaceBean.getPhaseName()
											.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED"))
									&& this.objInterfaceBean.getMapResultSet().get("41") != null
									&& this.objInterfaceBean.getMapResultSet().get("41").length() > 0
									&& this.objInterfaceBean.getMapResultSet().get("41").contains("Y")) {
						Statement stmt = this.objInterfaceBean.getBusinessUnitConnection().createStatement();
						// system.out.println("Before rs :" + strInterfaceRecordID);
						ResultSet rs = stmt.executeQuery(
								"select SEQ_NUMBER,INTERFACE_RECORD_ID,M_BC_MANUFACTURE_DATE,INVESTIGATION_METHODS,"
										+ "CONCLUSION_SUMMARY,ADDITIONAL_INFORMATION,NEW_INFO_RECEIVED_FOR_PAE,INVESTIGATION_COMPLETED_BY,INVESTIGATION_COMPLETED_DATE,"
										+ "INVESTIGATION_CLOSURE_DATE,INVESTIGATION_CLOSED_BY,CODE_CORR_ACTION_COMP,RETURN_INVEST_SITE,"
										+ "PRODUCT_RECEIPT_DATE from  XX_COMP_INV_ACT_INTERFACE WHERE INTERFACE_RECORD_ID='"
										+ strInterfaceRecordID + "'");

						JSONObject activityJson = createActivityJson(rs);
						if (activityJson.length() > 0) {
							eMsg = createComplaintInvestigation(activityJson, map.get("documentId"), strDocumentNumber);
						}

						rs.close();
						stmt.close();
					}
				} else {
					eMsg = "Error: " + map.get("Messages");
					logger.info(eMsg);
					logger.error("-------------------------Error In Complaint Json-----------------------------");
					logger.error(jsonString.toString());
					updateComplaintTables(this.objInterfaceBean, eMsg, "");
				}
			} else {
				eMsg = "Error: Unable to Connect with EtQ System!";
				logger.error(eMsg);
				logger.error("-------------------------Error In Complaint Json-----------------------------");
				logger.error(jsonString.toString());
				updateComplaintTables(this.objInterfaceBean, eMsg, "");
			}
		} catch (Exception e) {
			logger.error("Error: ", e);
			eMsg = "Error: " + e.getMessage();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return eMsg;
	}

	public String createComplaintInvestigation(JSONObject jsonString, String strComDocumentId, String strCompNumber) {
		// system.out.println("strDocNo: " + strComDocumentId + " " + strCompNumber);
		String strActivityNo = null;
		HttpURLConnection connection = null;
		try {
			String strEtQURL = this.objInterfaceBean.getEtQURL();
			String strSvrCred = this.objInterfaceBean.getServerCred();

			URL url = null;
			if (!jsonString.toString().contains("CI_COMPLETED_PHASE_P")) {
				url = new URL(strEtQURL + "/documents?assignedusers=" + this.objInterfaceBean.getAssignedTo()
						+ "&Duedate=" + getCurrentDate() + "&allsystemfields=true");
			} else {
				url = new URL(strEtQURL + "/documents");
			}

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setReadTimeout(180000);
			connection.setRequestMethod("POST");
			String encoded = Base64.getEncoder().encodeToString((strSvrCred).getBytes(StandardCharsets.UTF_8));

			connection.setRequestProperty("Authorization", "Basic " + encoded);

			OutputStream os = connection.getOutputStream();
			os.write(jsonString.toString().getBytes("UTF-8"));
			os.close();
			int responseCode = connection.getResponseCode();
			// system.out.println("ResponseCode: "+ responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// read the response
				InputStream in = new BufferedInputStream(connection.getInputStream());

				BufferedReader br = new BufferedReader(new InputStreamReader((in)));
				StringBuilder sb = new StringBuilder();
				String output;
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
				br.close();
				in.close();

				JSONArray jsonArray = new JSONArray(sb.toString());
				// system.out.println("sb: " + sb.toString());
				// system.out.println("jsonArray: " + jsonArray);
				String strConSummary = "";
				if (jsonArray.length() > 0) {
					ArrayList<String> arrListActivityNo = new ArrayList<String>();
					for (int i = 0; i < jsonArray.length(); i++) {
						Gson gson = new Gson();
						String strSb = jsonArray.get(i).toString();
						// system.out.println("strsb: " + strSb.toString());

						strSb = strSb.replaceAll("\\[", "");
						strSb = strSb.replaceAll("\\]", "");

						Map<String, String> map = gson.fromJson(strSb, new TypeToken<HashMap<String, String>>() {
						}.getType());
						String strActSeq = this.objInterfaceBean.getMapActSeqNo().get((i + 1) + "");
						// system.out.println("Gson Map: "+ map);
						if (map.get("Messages").contains("Operation done successfully")) {
							String strDocumentNumber = getDocumentNumberById("COMPLAI_INVESTIGATIO",
									"COMPL_INVESTIGATI_ID", map.get("documentId"));
							this.objInterfaceBean.setInvestigationNumber(strDocumentNumber);
							logger.info("Complaint Investigation Number: " + strDocumentNumber
									+ " & Complait Investigation Document ID: " + map.get("documentId"));
							arrListActivityNo.add("COMPLAINTS/COMPLAINT_INVESTIGATION_P/" + map.get("documentId"));
							strActivityNo = map.get("documentId");
							String strMapCS = this.objInterfaceBean.getMapConclusionSummary().get(strActSeq);
							updateActivityTables(this.objInterfaceBean, "", strActSeq);
							strConSummary = strConSummary + strDocumentNumber + "" + strMapCS;
						} else {
							strActivityNo = "Error: " + map.get("Messages");
							logger.error(strActivityNo);
							logger.error(
									"-------------------------Error In Activity Json-----------------------------");
							logger.error(jsonString.toString());
							updateActivityTables(this.objInterfaceBean, strActivityNo, strActivityNo);
						}
					}
					this.objInterfaceBean.setConclusionSummary(strConSummary);
					strActivityNo = setActivityLinkInComplaint(arrListActivityNo);
				}
			} else {
				strActivityNo = "Error: Unable to Connect with EtQ System!";
				logger.error(strActivityNo);
				logger.error("-------------------------Error In Activity Json-----------------------------");
				logger.error(jsonString.toString());
				updateActivityTables(this.objInterfaceBean, strActivityNo, "");
			}
		} catch (Exception e) {
			logger.error("Error: " + e);
			strActivityNo = e.getMessage();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return strActivityNo;
	}

	public JSONObject createActivityJson(ResultSet rs) {
		JSONObject parent = new JSONObject();
		try {
			String strComplaintDocumentID = this.objInterfaceBean.getComplaintDocumentId();
			if (rs != null && rs.isBeforeFirst()) {
				int i = 1;
				Map<String, String> mapActSeqNo = new HashMap<String, String>();
				Map<String, String> mapConSum = new HashMap<String, String>();
				JSONArray activityNode = new JSONArray();
				parent.put("Document", activityNode);
				while (rs.next()) {
					String strActSeq = rs.getString(1);
					mapActSeqNo.put(i + "", strActSeq);
					JSONObject parentNode = new JSONObject();
					activityNode.put(parentNode);
					JSONArray childNode = new JSONArray();
					parentNode.put("applicationName", "COMPLAINTS");
					parentNode.put("formName", "COMPLAINT_INVESTIGATION_P");
					parentNode.put("documentId", "");
					if (rs.getString(10) != null) {
						parentNode.put("phase", "CI_COMPLETED_PHASE_P");
					} else {
						parentNode.put("phase", "CI_INVESTIGATE_PHASE_P");
					}
					parentNode.put("Fields", childNode);

					Map<String, Object> compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "CI_RA_MBCMANUFDATE_P");
					JSONArray awareDateValObj = new JSONArray();
					awareDateValObj.put(getFormatedDate(rs.getString(3)));
					compFields.put("Values", awareDateValObj);
					childNode.put(compFields);

					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "CI_INV_METHODDET_P");
					JSONArray invMethodValObj = new JSONArray();
					invMethodValObj.put(rs.getString(4));
					compFields.put("Values", invMethodValObj);
					childNode.put(compFields);

					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "CI_INV_CONCSUMMARY_P");
					JSONArray invSummValObj = new JSONArray();
					invSummValObj.put(rs.getString(5));
					compFields.put("Values", invSummValObj);
					childNode.put(compFields);

					mapConSum.put(strActSeq, " : " + rs.getString(5) + "\n");

					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "CI_INV_ADDINFO_P");
					JSONArray addInfoValObj = new JSONArray();
					addInfoValObj.put(rs.getString(6));
					compFields.put("Values", addInfoValObj);
					childNode.put(compFields);

					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "CI_INV_NEWINFO_P");
					JSONArray intNewInfoValObj = new JSONArray();
					intNewInfoValObj.put(getYesNo(rs.getString(7)));
					compFields.put("Values", intNewInfoValObj);
					childNode.put(compFields);

					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "IS_CC_ACTION_COMPLETE_P");
					JSONArray isCorrActComValObj = new JSONArray();
					if (rs.getString(12) != null && rs.getString(12) != "" && rs.getString(12).length() > 0) {
						isCorrActComValObj.put(getYesNo(rs.getString(12)));
					} else {
						isCorrActComValObj.put("Yes");
					}
					compFields.put("Values", isCorrActComValObj);
					childNode.put(compFields);

					if (this.objInterfaceBean.getIsProductReturn() != null
							&& this.objInterfaceBean.getIsProductReturn().equalsIgnoreCase("Yes")) {
						if (rs.getString(13) != null && rs.getString(13).length() > 0) {
							compFields = new LinkedHashMap<String, Object>(1);
							JSONArray returnInvSiteValObj = new JSONArray();
							compFields.put("fieldName", "CH_RETURN_INVEST_SITE_P");
							String strBoolean = getYesNo(rs.getString(13));
							if (strBoolean == null || strBoolean.length() == 0) {
								returnInvSiteValObj.put("No");
								// Added ETQCR-878
								this.objInterfaceBean.setLatestActivityWillPrdVal(strBoolean);
							} else {
								returnInvSiteValObj.put(strBoolean);
								// Added ETQCR-878
								this.objInterfaceBean.setLatestActivityWillPrdVal(strBoolean);
							}

							compFields.put("Values", returnInvSiteValObj);
							childNode.put(compFields);

						} else {
							compFields = new LinkedHashMap<String, Object>(1);
							compFields.put("fieldName", "CH_RETURN_INVEST_SITE_P");
							JSONArray returnInvSiteValObj = new JSONArray();
							returnInvSiteValObj.put("No");
							compFields.put("Values", returnInvSiteValObj);
							childNode.put(compFields);

							// ETQCR-878
							this.objInterfaceBean.setLatestActivityWillPrdVal("No");
						}
					}

					String strInvComBy = rs.getString(8);
					if (strInvComBy != null && strInvComBy.length() > 0) {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "CI_INV_COMPBY_TX_P");
						JSONArray invComByValObj = new JSONArray();
						invComByValObj.put(rs.getString(8));
						compFields.put("Values", invComByValObj);
						childNode.put(compFields);
					} else {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "CI_INV_COMPBY_TX_P");
						JSONArray invComByValObj = new JSONArray();
						invComByValObj.put("");
						compFields.put("Values", invComByValObj);
						childNode.put(compFields);
					}

					if (rs.getString(9) != null && rs.getString(9) != "") {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "CI_INV_COMPDATE_P");
						JSONArray invComDtValObj = new JSONArray();
						invComDtValObj.put(getFormatedDate(rs.getString(9)));
						compFields.put("Values", invComDtValObj);
						childNode.put(compFields);
					}

					String strInvCloseBy = rs.getString(11);
					if (strInvCloseBy != null && strInvCloseBy.length() > 0) {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "CI_INV_CLOSEDBY_TX_P");
						JSONArray invCloseByValObj = new JSONArray();
						invCloseByValObj.put(strInvCloseBy);
						compFields.put("Values", invCloseByValObj);
						childNode.put(compFields);
					} else {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "CI_INV_CLOSEDBY_TX_P");
						JSONArray invCloseByValObj = new JSONArray();
						invCloseByValObj.put("");
						compFields.put("Values", invCloseByValObj);
						childNode.put(compFields);
					}

					if (rs.getString(10) != null && rs.getString(10) != "") {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "CI_INV_CLOSUREDATE_P");
						JSONArray invComDtValObj = new JSONArray();
						invComDtValObj.put(getFormatedDate(rs.getString(10)));
						compFields.put("Values", invComDtValObj);
						childNode.put(compFields);
					}

					if (rs.getString(13) != null && rs.getString(13).contains("Y")) {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "CI_DATEPRODREC_P");
						JSONArray prdRecDtValObj = new JSONArray();
//						System.out.println("Before CI : " + rs.getString(14));
//						prdRecDtValObj.put(getFormatedDate(rs.getString(14)));
//						System.out.println("After CI : " + getFormatedDate(rs.getString(14)));
						prdRecDtValObj.put(getDateByTimeZone(rs.getString(14),this.objInterfaceBean.getTimeZones()));
						compFields.put("Values", prdRecDtValObj);
						childNode.put(compFields);
					}

					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "ETQ$AUTHOR");
					JSONArray createdByValObj = new JSONArray();
					createdByValObj.put(this.objInterfaceBean.getEtQUser());
					compFields.put("Values", createdByValObj);
					childNode.put(compFields);

					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "ETQ$SOURCE_LINK");
					JSONArray sourceLinkValObj = new JSONArray();
					sourceLinkValObj.put("COMPLAINTS/COMPLAINTS_COMPLAINT_DOCUMENT/" + strComplaintDocumentID);
					compFields.put("Values", sourceLinkValObj);
					childNode.put(compFields);

					/*-----------------------Set Product Information Subform-------------------------------*/
					Map<String, String> mapInvProdSubForm = objValidation
							.getComplaintModelDetails(strComplaintDocumentID);
					JSONObject subFormMap = new JSONObject();
					JSONArray sbRecordNode = new JSONArray();

					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "CI_EVENT_DESCRIPTION_P");
					JSONArray eventDescValObj = new JSONArray();
					eventDescValObj.put(mapInvProdSubForm.get("EVENT_DESC"));
					compFields.put("Values", eventDescValObj);
					childNode.put(compFields);

					if (mapInvProdSubForm.get("POTENTIAL_ADVERSE_EVENT") != null
							&& mapInvProdSubForm.get("POTENTIAL_ADVERSE_EVENT").length() > 0) {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", "CI_PAE_P");
						JSONArray paeValObj = new JSONArray();
						paeValObj.put(mapInvProdSubForm.get("POTENTIAL_ADVERSE_EVENT"));
						compFields.put("Values", paeValObj);
						childNode.put(compFields);

						if (mapInvProdSubForm.get("POTENTIAL_ADVERSE_EVENT").equalsIgnoreCase("Yes")) {
							compFields = new LinkedHashMap<String, Object>(1);
							compFields.put("fieldName", "CI_GA_PRIORITY_P");
							JSONArray priorityValObj = new JSONArray();
							priorityValObj.put("High");
							compFields.put("Values", priorityValObj);
							childNode.put(compFields);
						} else {
							compFields = new LinkedHashMap<String, Object>(1);
							compFields.put("fieldName", "CI_GA_PRIORITY_P");
							JSONArray priorityValObj = new JSONArray();
							priorityValObj.put("Low");
							compFields.put("Values", priorityValObj);
							childNode.put(compFields);
						}
					}

					compFields = new LinkedHashMap<String, Object>(1);
					compFields.put("fieldName", "CI_EVAL_RESULTS_P");
					JSONArray evalResultValObj = new JSONArray();
					evalResultValObj.put(mapInvProdSubForm.get("EVAL_RESULT"));
					compFields.put("Values", evalResultValObj);
					childNode.put(compFields);

					subFormMap.put("SubformName", "CI_PRODDETAILSSUB_P");
					JSONArray subChildNode = new JSONArray();

					JSONObject subRObj = new JSONObject();
					subRObj.put("recordId", "");
					subRObj.put("recordOrder", "0");

					Map<String, Object> sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_PDS_MODITEMNUM_P");
					JSONArray modelNumValObj = new JSONArray();
					modelNumValObj.put(mapInvProdSubForm.get("MODEL_NUMBER"));
					sbFields.put("Values", modelNumValObj);
					subChildNode.put(sbFields);

					if (mapInvProdSubForm.get("PRODUCT_FAMILY") != null
							&& mapInvProdSubForm.get("PRODUCT_FAMILY").length() > 0) {
						sbFields = new LinkedHashMap<String, Object>(1);
						sbFields.put("fieldName", "CI_PDS_PROD_FLY_P");
						JSONArray prodFamilyValObj = new JSONArray();
						prodFamilyValObj.put(mapInvProdSubForm.get("PRODUCT_FAMILY"));
						sbFields.put("Values", prodFamilyValObj);
						subChildNode.put(sbFields);
					}

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_PDS_PRODNUM_P");
					JSONArray prodGBICValObj = new JSONArray();
					prodGBICValObj.put(mapInvProdSubForm.get("GBIC_NUMBER"));
					sbFields.put("Values", prodGBICValObj);
					subChildNode.put(sbFields);

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_PDS_DEVICETYPE_P");
					JSONArray deviceTypeValObj = new JSONArray();
					deviceTypeValObj.put(mapInvProdSubForm.get("DEV_TYPE"));
					sbFields.put("Values", deviceTypeValObj);
					subChildNode.put(sbFields);

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_PDS_CLEANINGTYPE_P");
					JSONArray cdsMethodValObj = new JSONArray();
					cdsMethodValObj.put(mapInvProdSubForm.get("CDS_METHOD"));
					sbFields.put("Values", cdsMethodValObj);
					subChildNode.put(sbFields);

					if (mapInvProdSubForm.get("ITEM") != null && mapInvProdSubForm.get("ITEM").length() > 0) {
						sbFields = new LinkedHashMap<String, Object>(1);
						sbFields.put("fieldName", "CI_PDS_ITEMSERIAL_1_P");
						JSONArray itemSerialValObj = new JSONArray();
						itemSerialValObj.put(mapInvProdSubForm.get("ITEM"));
						sbFields.put("Values", itemSerialValObj);
						subChildNode.put(sbFields);
					}

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_PDS_SERIALNUM_P");
					JSONArray serialNumValObj = new JSONArray();
					serialNumValObj.put(mapInvProdSubForm.get("SERIAL_NUMBER"));
					sbFields.put("Values", serialNumValObj);
					subChildNode.put(sbFields);

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_PDS_MODELDESC_P");
					JSONArray modelDescValObj = new JSONArray();
					modelDescValObj.put(mapInvProdSubForm.get("MODEL_DESC"));
					sbFields.put("Values", modelDescValObj);
					subChildNode.put(sbFields);

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_PDS_LEGALMANUF_P");
					JSONArray legalManValObj = new JSONArray();
					legalManValObj.put(mapInvProdSubForm.get("LEGAL_MANUFACTURER"));
					sbFields.put("Values", legalManValObj);
					subChildNode.put(sbFields);

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_PDS_MANUFSITE_P");
					JSONArray manSiteValObj = new JSONArray();
					manSiteValObj.put(mapInvProdSubForm.get("MANUFACTURING_SITE"));
					sbFields.put("Values", manSiteValObj);
					subChildNode.put(sbFields);

					if (mapInvProdSubForm.get("PROD_QTY") != null && mapInvProdSubForm.get("PROD_QTY").length() > 0) {
						sbFields = new LinkedHashMap<String, Object>(1);
						sbFields.put("fieldName", "CI_PDS_PRODQTY_P");
						JSONArray prodQtyValObj = new JSONArray();
						prodQtyValObj.put(mapInvProdSubForm.get("PROD_QTY"));
						sbFields.put("Values", prodQtyValObj);
						subChildNode.put(sbFields);
					}

					if (mapInvProdSubForm.get("UOM") != null && mapInvProdSubForm.get("UOM").length() > 0) {
						sbFields = new LinkedHashMap<String, Object>(1);
						sbFields.put("fieldName", "CI_PDS_UOM_P");
						JSONArray uomValObj = new JSONArray();
						uomValObj.put(mapInvProdSubForm.get("UOM"));
						sbFields.put("Values", uomValObj);
						subChildNode.put(sbFields);
					}

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_PDS_PRODRETURN_P");
					JSONArray retToOlyValObj = new JSONArray();
					retToOlyValObj.put(mapInvProdSubForm.get("RETURN_TO_OLY"));
					sbFields.put("Values", retToOlyValObj);
					subChildNode.put(sbFields);

					if (mapInvProdSubForm.get("RETURN_TO_CUST") != null
							&& mapInvProdSubForm.get("RETURN_TO_CUST").length() > 0) {
						sbFields = new LinkedHashMap<String, Object>(1);
						sbFields.put("fieldName", "CI_NEED_TO_RET_TO_CUST_P");
						JSONArray retToCustValObj = new JSONArray();
						retToCustValObj.put(mapInvProdSubForm.get("RETURN_TO_CUST"));
						sbFields.put("Values", retToCustValObj);
						subChildNode.put(sbFields);
					}

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_PDS_PRODCOMM_P");
					JSONArray prodCmtValObj = new JSONArray();
					prodCmtValObj.put(mapInvProdSubForm.get("PROD_COMMENT"));
					sbFields.put("Values", prodCmtValObj);
					subChildNode.put(sbFields);

					if (mapInvProdSubForm.get("SOFTWARE") != null && mapInvProdSubForm.get("SOFTWARE").length() > 0) {
						sbFields = new LinkedHashMap<String, Object>(1);
						sbFields.put("fieldName", "CI_PDS_SOFTWARE_P");
						JSONArray prodSoftwareValObj = new JSONArray();
						prodSoftwareValObj.put(mapInvProdSubForm.get("SOFTWARE"));
						sbFields.put("Values", prodSoftwareValObj);
						subChildNode.put(sbFields);
					}

					if (mapInvProdSubForm.get("SW_VERSION") != null
							&& mapInvProdSubForm.get("SW_VERSION").length() > 0) {
						sbFields = new LinkedHashMap<String, Object>(1);
						sbFields.put("fieldName", "CI_SA_SWVERSION_P");
						JSONArray swVerValObj = new JSONArray();
						swVerValObj.put(mapInvProdSubForm.get("SW_VERSION"));
						sbFields.put("Values", swVerValObj);
						subChildNode.put(sbFields);
					}

					if (mapInvProdSubForm.get("OS_VERSION") != null
							&& mapInvProdSubForm.get("OS_VERSION").length() > 0) {
						sbFields = new LinkedHashMap<String, Object>(1);
						sbFields.put("fieldName", "CI_SA_OSVERSION_P");
						JSONArray osVerValObj = new JSONArray();
						osVerValObj.put(mapInvProdSubForm.get("OS_VERSION"));
						sbFields.put("Values", osVerValObj);
						subChildNode.put(sbFields);
					}

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_SA_HARDWARETYPE_P");
					JSONArray hwTypeValObj = new JSONArray();
					hwTypeValObj.put(mapInvProdSubForm.get("HW_TYPE"));
					sbFields.put("Values", hwTypeValObj);
					subChildNode.put(sbFields);

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_SA_HWSERIAL_P");
					JSONArray hwSerialValObj = new JSONArray();
					hwSerialValObj.put(mapInvProdSubForm.get("HW_SERIAL"));
					sbFields.put("Values", hwSerialValObj);
					subChildNode.put(sbFields);

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_SA_MANUFMODELNUM_P");
					JSONArray mfrModelValObj = new JSONArray();
					mfrModelValObj.put(mapInvProdSubForm.get("MFR_MODEL"));
					sbFields.put("Values", mfrModelValObj);
					subChildNode.put(sbFields);

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_SA_MANUFMODELSR_P");
					JSONArray modelSerialValObj = new JSONArray();
					modelSerialValObj.put(mapInvProdSubForm.get("MODEL_SERIAL"));
					sbFields.put("Values", modelSerialValObj);
					subChildNode.put(sbFields);

					if (mapInvProdSubForm.get("REMOTE_SUPPORT") != null
							&& mapInvProdSubForm.get("REMOTE_SUPPORT").length() > 0) {
						sbFields = new LinkedHashMap<String, Object>(1);
						sbFields.put("fieldName", "CI_SA_REMOTESUPPORT_P");
						JSONArray remoteSuppValObj = new JSONArray();
						remoteSuppValObj.put(mapInvProdSubForm.get("REMOTE_SUPPORT"));
						sbFields.put("Values", remoteSuppValObj);
						subChildNode.put(sbFields);
					}

					if (mapInvProdSubForm.get("CUST_SUPP_HW") != null
							&& mapInvProdSubForm.get("CUST_SUPP_HW").length() > 0) {
						sbFields = new LinkedHashMap<String, Object>(1);
						sbFields.put("fieldName", "CI_SA_CUSTSUPPORTHW_P");
						JSONArray custSuppValObj = new JSONArray();
						custSuppValObj.put(mapInvProdSubForm.get("CUST_SUPP_HW"));
						sbFields.put("Values", custSuppValObj);
						subChildNode.put(sbFields);
					}

					sbFields = new LinkedHashMap<String, Object>(1);
					sbFields.put("fieldName", "CI_SA_ANTIVIRUSSW_P");
					JSONArray antiSwValObj = new JSONArray();
					antiSwValObj.put(mapInvProdSubForm.get("ANTI_VIRUS_SW"));
					sbFields.put("Values", antiSwValObj);
					subChildNode.put(sbFields);

					subRObj.put("Fields", subChildNode);
					sbRecordNode.put(subRObj);

					subFormMap.put("SubformRecords", sbRecordNode);

					JSONArray sbMainNode = new JSONArray();
					sbMainNode.put(subFormMap);
					parentNode.put("Subforms", sbMainNode);

					ArrayList<Object> attchObj = getAttachmentForInvestigation(strActSeq);
					if (attchObj != null && attchObj.size() > 0 && attchObj.get(0) != null) {
						compFields = new LinkedHashMap<String, Object>(1);
						compFields.put("fieldName", attchObj.get(0));
						compFields.put("attachmentType", "base64");
						compFields.put("Values", attchObj.get(1));
						childNode.put(compFields);
					}

					i++;
				}
				this.objInterfaceBean.setMapActSeqNo(mapActSeqNo);
				this.objInterfaceBean.setMapConclusionSummary(mapConSum);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return parent;
	}

	public ArrayList<Object> getAttachmentForComplaint() {
		ArrayList<Object> strAttch = new ArrayList<Object>();
		try {
			Statement attachStmt = this.objInterfaceBean.getBusinessUnitConnection().createStatement();
			String strSQL = "";

			if (this.objInterfaceBean.getBusinessUnitDriver() != null && this.objInterfaceBean.getBusinessUnitDriver()
					.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) {
				strSQL = "SELECT ATTACHMENT_FIELD_NAME,FILE_NAME,FILE_DATA from XX_COMP_INI_ATTACHMENT"
						+ " WHERE GLOBAL_ATTRIBUTE1='COMPLAINT' AND INTERFACE_RECORD_ID='"
						+ this.objInterfaceBean.getInterfaceRecordId() + "'";
			} else {
				strSQL = "SELECT DISTINCT ATTACHMENT_FIELD_NAME,FILE_NAME,FILE_DATA from XX_COMP_INI_ATTACHMENT"
						+ " WHERE GLOBAL_ATTRIBUTE1='COMPLAINT' AND INTERFACE_RECORD_ID='"
						+ this.objInterfaceBean.getInterfaceRecordId() + "'";
			}
			ResultSet attachResultSet = attachStmt.executeQuery(strSQL);
			ArrayList<String> strBase64 = new ArrayList<String>();
			int flag = 0;
			if (attachResultSet.isBeforeFirst()) {
				ArrayList<String> strFieldName = new ArrayList<String>();
				while (attachResultSet.next()) {
					flag = 1;
					strAttch.add(0, attachResultSet.getString(1));
					if (strFieldName == null || strFieldName.size() == 0) {
						strFieldName.add(attachResultSet.getString(2));
						Blob blob = attachResultSet.getBlob(3);
						// system.out.println("blob : "+ blob);
						int blobLength = (int) blob.length();
						// system.out.println("bloblength :"+ blobLength);
						byte[] bytes = blob.getBytes(1, blobLength);
						// system.out.println("byte :"+ bytes.toString());
						byte[] encoded = Base64.getEncoder().encode(bytes);
						// system.out.println("encoded:"+ encoded.toString());
						strBase64.add(attachResultSet.getString(2) + "/" + new String(encoded));
					} else {
						if (!strFieldName.contains((attachResultSet.getString(2)))) {
							strFieldName.add(attachResultSet.getString(2));
							Blob blob = attachResultSet.getBlob(3);
							int blobLength = (int) blob.length();
							byte[] bytes = blob.getBytes(1, blobLength);
							byte[] encoded = Base64.getEncoder().encode(bytes);
							strBase64.add(attachResultSet.getString(2) + "/" + new String(encoded));
						}
					}

				}
				if (flag == 1) {
					strAttch.add(1, strBase64);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return strAttch;
	}

	public ArrayList<Object> getAttachmentForInvestigation(String strSeqNo) {
		ArrayList<Object> strAttch = new ArrayList<Object>();
		try {
			Statement attachStmt = this.objInterfaceBean.getBusinessUnitConnection().createStatement();
			String strSQL = "";

			if (this.objInterfaceBean.getBusinessUnitDriver() != null && this.objInterfaceBean.getBusinessUnitDriver()
					.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) {
				strSQL = "SELECT ATTACHMENT_FIELD_NAME,FILE_NAME,FILE_DATA from XX_COMP_INI_ATTACHMENT"
						+ " WHERE GLOBAL_ATTRIBUTE1='INVESTIGATION' AND INTERFACE_RECORD_ID='"
						+ this.objInterfaceBean.getInterfaceRecordId() + "' AND NVL(GLOBAL_ATTRIBUTE2,'" + strSeqNo
						+ "')='" + strSeqNo + "'";
			} else {
				strSQL = "SELECT DISTINCT ATTACHMENT_FIELD_NAME,FILE_NAME,FILE_DATA from XX_COMP_INI_ATTACHMENT"
						+ " WHERE GLOBAL_ATTRIBUTE1='INVESTIGATION' AND INTERFACE_RECORD_ID='"
						+ this.objInterfaceBean.getInterfaceRecordId() + "' AND ISNULL(GLOBAL_ATTRIBUTE2,'" + strSeqNo
						+ "')='" + strSeqNo + "'";
			}
			ResultSet attachResultSet = attachStmt.executeQuery(strSQL);
			ArrayList<String> strBase64 = new ArrayList<String>();
			int flag = 0;
			if (attachResultSet != null && attachResultSet.isBeforeFirst()) {
				ArrayList<String> strFieldName = new ArrayList<String>();
				while (attachResultSet.next()) {
					flag = 1;
					strAttch.add(0, attachResultSet.getString(1));
					if (strFieldName == null || strFieldName.size() == 0) {
						strFieldName.add(attachResultSet.getString(2));
						Blob blob = attachResultSet.getBlob(3);
						int blobLength = (int) blob.length();
						byte[] bytes = blob.getBytes(1, blobLength);
						byte[] encoded = Base64.getEncoder().encode(bytes);
						strBase64.add(attachResultSet.getString(2) + "/" + new String(encoded));
					} else {
						if (!strFieldName.contains((attachResultSet.getString(2)))) {
							strFieldName.add(attachResultSet.getString(2));
							Blob blob = attachResultSet.getBlob(3);
							int blobLength = (int) blob.length();
							byte[] bytes = blob.getBytes(1, blobLength);
							byte[] encoded = Base64.getEncoder().encode(bytes);
							strBase64.add(attachResultSet.getString(2) + "/" + new String(encoded));
						}
					}
				}
				if (flag == 1) {
					strAttch.add(1, strBase64);
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return strAttch;
	}

	public ArrayList<String> getAsReportedCode() {
		ArrayList<String> strArCode = new ArrayList<String>();
		try {
			String strInterfaceId = this.objInterfaceBean.getInterfaceRecordId();
			Statement arCodeStmt = this.objInterfaceBean.getBusinessUnitConnection().createStatement();
			ResultSet arCodeResultSet = arCodeStmt.executeQuery(
					"SELECT AS_REPORTED_CODE,AS_REPORTED_DESCRIPTION FROM XX_COMP_AR_CODE_INTERFACE WHERE (ETQ_UPLOAD_STATUS IS NULL OR ETQ_UPLOAD_STATUS=1 OR ETQ_UPLOAD_STATUS=0) AND INTERFACE_RECORD_ID='"
							+ strInterfaceId + "'");
			while (arCodeResultSet.next()) {
				strArCode.add(arCodeResultSet.getString(1) + "@" + arCodeResultSet.getString(2));
			}
			if (strArCode != null && strArCode.size() > 0) {
				Set<String> set = new HashSet<String>(strArCode);
				strArCode.clear();
				strArCode.addAll(set);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return strArCode;
	}

	// Modified for ETQCR-760
	public XxComplaintInterfaceBean getCoding(XxComplaintInterfaceBean objInterfaceBean) {
		this.objInterfaceBean = objInterfaceBean;
		JSONArray codingSbRecordNode = new JSONArray();
		try {
			int i = 0;
			String strInterfaceId = this.objInterfaceBean.getInterfaceRecordId();
			Statement codeStmt = objInterfaceBean.getBusinessUnitConnection().createStatement();
			ResultSet partCodeResultSet = codeStmt
					.executeQuery("SELECT LTRIM(PART_CODE),LTRIM(PART_CODE_DESC) FROM XX_COMP_PART_ANA_CAU_CODE_INT"
							+ " WHERE INTERFACE_RECORD_ID=" + strInterfaceId
							+ " AND (CASE WHEN PART_CODE IS NULL THEN 0 WHEN PART_CODE='' THEN 0 ELSE 1 END>0 OR CASE WHEN PART_CODE_DESC IS NULL THEN 0 WHEN PART_CODE_DESC='' THEN 0 ELSE 1 END>0)"
							+ " AND (CASE WHEN CAUSE_CODE IS NULL THEN 0 WHEN CAUSE_CODE='' THEN 0 ELSE 1 END>0 OR CASE WHEN CAUSE_CODE_DESC IS NULL THEN 0 WHEN CAUSE_CODE_DESC='' THEN 0 ELSE 1 END>0)"
							+ " AND (CASE WHEN AS_ANALYZED_CODE IS NULL THEN 0 WHEN AS_ANALYZED_CODE='' THEN 0 ELSE 1 END>0 OR CASE WHEN AS_ANALYZED_CODE_DESC IS NULL THEN 0 WHEN AS_ANALYZED_CODE_DESC='' THEN 0 ELSE 1 END>0)"
							+ " GROUP BY PART_CODE,PART_CODE_DESC");

			while (partCodeResultSet.next()) {
				this.objInterfaceBean.setPartCodeFound("");
				this.objInterfaceBean.setPartCodeNotFound("");

				this.objInterfaceBean.setAsAnalyzedCodeFound(new ArrayList<String>());
				this.objInterfaceBean.setAsAnalyzedCodeNotFound("");

				this.objInterfaceBean.setCauseCode(new ArrayList<String>());

				JSONArray codingSBChildNode = new JSONArray();
				JSONObject codingSbRObj = new JSONObject();
				codingSbRObj.put("recordId", "");
				codingSbRObj.put("recordOrder", i);

				Map<String, Object> codingSbFields = new LinkedHashMap<String, Object>(1);

				String strPartCode = "";
				if (partCodeResultSet.getString(1) != null && partCodeResultSet.getString(1).length() > 0
						&& partCodeResultSet.getString(2) != null && partCodeResultSet.getString(2).length() > 0) {
					strPartCode = partCodeResultSet.getString(1) + " : " + partCodeResultSet.getString(2);
				} else if ((partCodeResultSet.getString(1) == null || partCodeResultSet.getString(1).length() == 0)
						&& partCodeResultSet.getString(2) != null && partCodeResultSet.getString(2).length() > 0) {
					strPartCode = partCodeResultSet.getString(2);
				} else if ((partCodeResultSet.getString(2) == null || partCodeResultSet.getString(2).length() == 0)
						&& partCodeResultSet.getString(1) != null && partCodeResultSet.getString(1).length() > 0) {
					strPartCode = partCodeResultSet.getString(1);
				}

				this.objInterfaceBean.setPartCodeFound(strPartCode);
				if (this.objInterfaceBean.getPartCodeFound() != null
						&& this.objInterfaceBean.getPartCodeFound().length() > 0) {
					this.objInterfaceBean = objValidation.validateCompPartCode(this.objInterfaceBean);
				}

				if (this.objInterfaceBean.getErrorMessage() == null
						|| !this.objInterfaceBean.getErrorMessage().contains("Error: ")) {
					String strFoundPartCode = this.objInterfaceBean.getPartCodeFound();
					String strNotFoundPartCode = this.objInterfaceBean.getPartCodeNotFound();

					if (strNotFoundPartCode != null && strNotFoundPartCode.length() > 0) {
						codingSbFields = new LinkedHashMap<String, Object>(1);
						codingSbFields.put("fieldName", "LOCAL_SYSTEM_PART_CODE_P");
						JSONArray partCodeValObj = new JSONArray();
						partCodeValObj.put(strNotFoundPartCode);
						codingSbFields.put("Values", partCodeValObj);
						codingSBChildNode.put(codingSbFields);
					} else if (strFoundPartCode != null && strFoundPartCode.length() > 0) {
						codingSbFields = new LinkedHashMap<String, Object>(1);
						codingSbFields.put("fieldName", "PART_CODE_WS_P");
						JSONArray partCodeValObj = new JSONArray();
						partCodeValObj.put(strFoundPartCode);
						codingSbFields.put("Values", partCodeValObj);
						codingSBChildNode.put(codingSbFields);
					}

					Statement acCodeStmt = this.objInterfaceBean.getBusinessUnitConnection().createStatement();
					String strCodeSQL = "SELECT LTRIM(AS_ANALYZED_CODE),LTRIM(AS_ANALYZED_CODE_DESC),LTRIM(CAUSE_CODE),LTRIM(CAUSE_CODE_DESC) FROM XX_COMP_PART_ANA_CAU_CODE_INT"
							+ " WHERE INTERFACE_RECORD_ID=" + strInterfaceId;
					if (partCodeResultSet.getString(1) != null && partCodeResultSet.getString(1).length() > 0) {
						strCodeSQL = strCodeSQL + " AND PART_CODE='" + partCodeResultSet.getString(1) + "'";
					} else {
						strCodeSQL = strCodeSQL + " AND (PART_CODE IS NULL OR PART_CODE='')";
					}

					if (partCodeResultSet.getString(2) != null && partCodeResultSet.getString(2).length() > 0) {
						strCodeSQL = strCodeSQL + " AND PART_CODE_DESC='" + partCodeResultSet.getString(2) + "'";
					} else {
						strCodeSQL = strCodeSQL + " AND (PART_CODE_DESC IS NULL OR PART_CODE_DESC='')";
					}

					ResultSet acCodeResultSet = acCodeStmt.executeQuery(strCodeSQL);
					ArrayList<String> arListAA = new ArrayList<String>();
					ArrayList<String> arListCC = new ArrayList<String>();
					while (acCodeResultSet.next()) {
						if (acCodeResultSet.getString(1) != null && acCodeResultSet.getString(1).length() > 0
								&& acCodeResultSet.getString(2) != null && acCodeResultSet.getString(2).length() > 0) {
							arListAA.add(acCodeResultSet.getString(1) + "@" + acCodeResultSet.getString(2));
						} else if (acCodeResultSet.getString(1) != null && acCodeResultSet.getString(1).length() > 0) {
							arListAA.add(acCodeResultSet.getString(1) + "@");
						} else if (acCodeResultSet.getString(2) != null && acCodeResultSet.getString(2).length() > 0) {
							arListAA.add(acCodeResultSet.getString(2) + "@");
						}

						if (acCodeResultSet.getString(3) != null && acCodeResultSet.getString(3).length() > 0
								&& acCodeResultSet.getString(4) != null && acCodeResultSet.getString(4).length() > 0) {
							arListCC.add(acCodeResultSet.getString(3) + "@" + acCodeResultSet.getString(4));
						} else if (acCodeResultSet.getString(3) != null && acCodeResultSet.getString(3).length() > 0) {
							arListCC.add(acCodeResultSet.getString(3) + "@");
						} else if (acCodeResultSet.getString(4) != null && acCodeResultSet.getString(4).length() > 0) {
							arListCC.add(acCodeResultSet.getString(4) + "@");
						}
					}

					this.objInterfaceBean.setAsAnalyzedCodeFound(arListAA);
					this.objInterfaceBean.setCauseCode(arListCC);

					this.objInterfaceBean = objValidation.validateCompAsAnalyzedCode(this.objInterfaceBean);
					if (this.objInterfaceBean.getErrorMessage() == null
							|| !this.objInterfaceBean.getErrorMessage().contains("Error: ")) {
						ArrayList<String> arListAAFound = this.objInterfaceBean.getAsAnalyzedCodeFound();
						Set<String> set = new HashSet<String>(arListAAFound);
						arListAAFound.clear();
						arListAAFound.addAll(set);

						String arListAANotFound = this.objInterfaceBean.getAsAnalyzedCodeNotFound();
						if (arListAANotFound != null && arListAANotFound.length() > 0) {
							codingSbFields = new LinkedHashMap<String, Object>(1);
							codingSbFields.put("fieldName", "LOCAL_SYSTEM_AS_ANALYZED_CODE_P");
							JSONArray aaCodeValObj = new JSONArray();
							aaCodeValObj.put(arListAANotFound);
							codingSbFields.put("Values", aaCodeValObj);
							codingSBChildNode.put(codingSbFields);
						}

						if (arListAAFound != null && arListAAFound.size() > 0) {
							codingSbFields = new LinkedHashMap<String, Object>(1);
							codingSbFields.put("fieldName", "AS_ANALYZED_CODES_WS_P");
							codingSbFields.put("Values", arListAAFound);
							codingSBChildNode.put(codingSbFields);
						}
					} else {
						return this.objInterfaceBean;
					}

					if (this.objInterfaceBean.getCauseCode() != null
							&& this.objInterfaceBean.getCauseCode().size() > 0) {
						this.objInterfaceBean = objValidation.validateCompCauseCode(this.objInterfaceBean);
					}

					if (this.objInterfaceBean.getErrorMessage() == null
							|| !this.objInterfaceBean.getErrorMessage().contains("Error: ")) {
						ArrayList<String> arListCCFound = objInterfaceBean.getCauseCode();
						Set<String> set = new HashSet<String>(arListCCFound);
						arListCCFound.clear();
						arListCCFound.addAll(set);
						if (arListCCFound != null && arListCCFound.size() > 0) {
							codingSbFields = new LinkedHashMap<String, Object>(1);
							codingSbFields.put("fieldName", "CH_CAUSE_CODE_SUB_P");
							codingSbFields.put("Values", arListCCFound);
							codingSBChildNode.put(codingSbFields);
						}
					} else {
						return this.objInterfaceBean;
					}
					codingSbRObj.put("Fields", codingSBChildNode);
					codingSbRecordNode.put(codingSbRObj);
					arListAA.clear();
					arListCC.clear();

					acCodeResultSet.close();
					acCodeStmt.close();
					i++;
				}
			}
			partCodeResultSet.close();
			codeStmt.close();

			if (this.objInterfaceBean.getFinalUniversalCode() != null
					&& this.objInterfaceBean.getFinalUniversalCode().size() > 0) {
				ArrayList<String> arListFinal = this.objInterfaceBean.getFinalUniversalCode();
				for (int j = 0; j < arListFinal.size(); j++) {
					JSONArray codingSBChildNode = new JSONArray();
					JSONObject codingSbRObj = new JSONObject();
					codingSbRObj.put("recordId", "");
					codingSbRObj.put("recordOrder", i);

					Map<String, Object> codingSbFields = new LinkedHashMap<String, Object>(1);
					codingSbFields.put("fieldName", "LOCAL_SYSTEM_AS_ANALYZED_CODE_P");
					JSONArray aaCodeValObj = new JSONArray();
					aaCodeValObj.put(arListFinal.get(j));
					codingSbFields.put("Values", aaCodeValObj);
					codingSBChildNode.put(codingSbFields);
					codingSbRObj.put("Fields", codingSBChildNode);
					codingSbRecordNode.put(codingSbRObj);

					i++;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		this.objInterfaceBean.setCompCoding(codingSbRecordNode);
		return objInterfaceBean;
	}

	public void routeDocument(XxComplaintInterfaceBean objInterfaceBean, String strAppName, String strFormName,
			String strPhaseName, String strDueDate) {
		HttpURLConnection connection = null;
		try {
			this.objInterfaceBean = objInterfaceBean;
			String strEtQURL = objInterfaceBean.getEtQURL();
			String strSvrCred = objInterfaceBean.getServerCred();
			String strAssigned = objInterfaceBean.getAssignedTo();
			String strDocumentId = objInterfaceBean.getComplaintDocumentId();

			URL url = new URL(strEtQURL + "/documents/" + strAppName + "/" + strFormName + "/" + strDocumentId
					+ "/route/" + strPhaseName + "?assignedusers=" + strAssigned + "&allsystemfields=true&Duedate="
					+ strDueDate);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setReadTimeout(180000);
			connection.setRequestMethod("PUT");
			String encoded = Base64.getEncoder().encodeToString((strSvrCred).getBytes(StandardCharsets.UTF_8));

			connection.setRequestProperty("Authorization", "Basic " + encoded);

			// read the response
			InputStream in = new BufferedInputStream(connection.getInputStream());

			BufferedReader br = new BufferedReader(new InputStreamReader((in)));
			StringBuilder sb = new StringBuilder();
			String output;
			while ((output = br.readLine()) != null) {
				sb.append(output);
			}
			br.close();
			in.close();

			Gson gson = new Gson();
			String strSb = sb.toString();
			strSb = strSb.replaceAll("\\[", "");
			strSb = strSb.replaceAll("\\]", "");
			Map<String, String> map = gson.fromJson(strSb, new TypeToken<HashMap<String, String>>() {
			}.getType());

			if (map.get("Messages").contains("Operation done successfully")) {
				logger.info("Document: " + map.get("documentId") + " successfully route.");
			} else {
				logger.info("Error: " + map);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
	}

	public String setActivityLinkInComplaint(ArrayList<String> strInvDoc) {
		String eMsg = "SUCCESS";
		HttpURLConnection connection = null;
		try {
			JSONObject parent = new JSONObject();
			JSONObject parentNode = new JSONObject();
			JSONArray childNode = new JSONArray();
			parent.put("Document", parentNode);

			parentNode.put("applicationName", "COMPLAINTS");
			parentNode.put("formName", "COMPLAINTS_COMPLAINT_DOCUMENT");
			parentNode.put("documentId", objInterfaceBean.getComplaintDocumentId());
			parentNode.put("Fields", childNode);

			Map<String, Object> compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "CH_INVESTIGATION_LINKS_P");
			compFields.put("Values", strInvDoc);
			childNode.put(compFields);

			compFields = new LinkedHashMap<String, Object>(1);
			compFields.put("fieldName", "CH_FAILURE_INVEST_CONC_1_P");
			compFields.put("Values", objInterfaceBean.getConclusionSummary());
			childNode.put(compFields);

			// Added for ETQCR-878
			if (this.objInterfaceBean.getLatestActivityWillPrdVal() != null
					&& this.objInterfaceBean.getLatestActivityWillPrdVal().length() > 0) {
				// String strRecordId=getSubformRecordId("PRODUCTRETURNDETAILS",
				// "C_PR_RET_DETA_SUB_ID", "COMPLAINT_DOCUMENT_ID",
				// this.objInterfaceBean.getComplaintDocumentId());
				String strRecordId = getSubformRecordId("PRODUCTRETURNDETAILS", "PRODUCTRETURNDETAILS_ID",
						"COMPLAINT_DOCUMENT_ID", this.objInterfaceBean.getComplaintDocumentId());

				if (strRecordId != null & strRecordId.length() > 0 && !strRecordId.contains("Error")) {
					JSONObject prdRtSbFormMap = new JSONObject();
					JSONArray prdRtSBChildNode = new JSONArray();
					JSONArray prdRtSbRecordNode = new JSONArray();

					prdRtSbFormMap.put("SubformName", "CH_PRODUCT_RETURN_DETAILS_P");

					JSONObject prdRtSbRObj = new JSONObject();
					prdRtSbRObj.put("recordId", strRecordId);
					prdRtSbRObj.put("recordOrder", "0");

					Map<String, Object> prdRtSbFields = new LinkedHashMap<String, Object>(1);
					prdRtSbFields.put("fieldName", "TEST_FIELD_P");
					// prdRtSbFields.put("fieldName","CH_WILL_PR_TO_INVEST_SIT_P");
					JSONArray willPrdRtValObj = new JSONArray();
					willPrdRtValObj.put(this.objInterfaceBean.getLatestActivityWillPrdVal());
					prdRtSbFields.put("Values", willPrdRtValObj);
					prdRtSBChildNode.put(prdRtSbFields);

					prdRtSbRObj.put("Fields", prdRtSBChildNode);
					prdRtSbRecordNode.put(prdRtSbRObj);
					prdRtSbFormMap.put("SubformRecords", prdRtSbRecordNode);
					JSONArray sbMainNode = new JSONArray();
					sbMainNode.put(prdRtSbFormMap);
					parentNode.put("Subforms", sbMainNode);
				}
			}

			URL url = new URL(objInterfaceBean.getEtQURL() + "/documents");

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			connection.setRequestProperty("Accept", "application/json");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setReadTimeout(180000);
			connection.setRequestMethod("PUT");
			String encoded = Base64.getEncoder()
					.encodeToString((objInterfaceBean.getServerCred()).getBytes(StandardCharsets.UTF_8));

			connection.setRequestProperty("Authorization", "Basic " + encoded);

			OutputStream os = connection.getOutputStream();
			os.write(parent.toString().getBytes("UTF-8"));
			os.close();
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				// read the response
				InputStream in = new BufferedInputStream(connection.getInputStream());

				BufferedReader br = new BufferedReader(new InputStreamReader((in)));
				StringBuilder sb = new StringBuilder();
				String output;
				while ((output = br.readLine()) != null) {
					sb.append(output);
				}
				br.close();
				in.close();

				Gson gson = new Gson();
				String strSb = sb.toString();
				strSb = strSb.replaceAll("\\[", "");
				strSb = strSb.replaceAll("\\]", "");
				Map<String, String> map = gson.fromJson(strSb, new TypeToken<HashMap<String, String>>() {
				}.getType());
				if (map.get("Messages").contains("Operation done successfully")) {
					logger.info("Complaint Activity has been set in complaint document id: " + map.get("documentId"));
					// ETQCR-878
					this.objInterfaceBean.setLatestActivityWillPrdVal("");
				} else {
					eMsg = "Error: " + map.get("Messages");
					logger.info(eMsg);
					updateActivityTables(objInterfaceBean, eMsg, "0");
				}
			} else {
				eMsg = "Error: Unable to Connect with EtQ System!";
				logger.error(eMsg);
				logger.error("-------------------------Error During Setting Acitivy Link-----------------------------");
				logger.error("Investigation URL: " + url);
				updateActivityTables(objInterfaceBean, eMsg, "0");
			}
		} catch (Exception e) {
			eMsg = "Error: " + e;
			logger.error("Error: " + e);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return eMsg;
	}

	public String getDocumentNumberById(String strFormName, String strDocColumn, String strDocId) {
		String strDocumentNumber = "";
		HttpURLConnection connection = null;
		try {
			String strEtQURL = objInterfaceBean.getEtQURL();
			String strSvrCred = objInterfaceBean.getServerCred();
			URL url = new URL(strEtQURL + "/dao/COMPLAINTS/" + strFormName + "/where?columns=ETQ$NUMBER&keys="
					+ strDocColumn + "&values=" + strDocId);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setReadTimeout(180000);
			String encoded = Base64.getEncoder().encodeToString((strSvrCred).getBytes(StandardCharsets.UTF_8));
			connection.setRequestProperty("Authorization", "Basic " + encoded);
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				JSONObject jsonObj = new JSONObject(response.toString());
				if (Integer.parseInt(jsonObj.get("count").toString()) > 0) {
					String strRec = jsonObj.get("Records").toString();
					JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

					JSONObject jsonColObj = new JSONObject(jsonRecObj.toString());
					String strCol = jsonColObj.get("Columns").toString();
					JSONObject jsonValObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

					strDocumentNumber = jsonValObj.get("value").toString();
				}
				in.close();
			}
		} catch (Exception ex) {
			strDocumentNumber = "Error: " + ex;
			logger.error(strDocumentNumber);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return strDocumentNumber;
	}

	public String getSubformRecordId(String strSubFormName, String strSubFormRecordColumn,
			String strSubFormParentColumn, String strParentDocId) {
		String strSubformRecordID = "";
		HttpURLConnection connection = null;
		try {
			String strEtQURL = objInterfaceBean.getEtQURL();
			String strSvrCred = objInterfaceBean.getServerCred();
			URL url = new URL(strEtQURL + "/dao/COMPLAINTS/" + strSubFormName + "/where?columns="
					+ strSubFormRecordColumn + "&keys=" + strSubFormParentColumn + "&values=" + strParentDocId);

			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setReadTimeout(180000);
			String encoded = Base64.getEncoder().encodeToString((strSvrCred).getBytes(StandardCharsets.UTF_8));
			connection.setRequestProperty("Authorization", "Basic " + encoded);
			int responseCode = connection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) { // success
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				JSONObject jsonObj = new JSONObject(response.toString());
				if (Integer.parseInt(jsonObj.get("count").toString()) > 0) {
					String strRec = jsonObj.get("Records").toString();
					JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

					JSONObject jsonColObj = new JSONObject(jsonRecObj.toString());
					String strCol = jsonColObj.get("Columns").toString();
					JSONObject jsonValObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

					strSubformRecordID = jsonValObj.get("value").toString();
				}
				in.close();
			}
		} catch (Exception ex) {
			strSubformRecordID = "Error: " + ex;
			logger.error(strSubformRecordID);
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return strSubformRecordID;
	}

	public void updateComplaintTables(XxComplaintInterfaceBean objInterfaceBean, String eMsg,
			String strDocumentNumber) {
		try {
			String strInterfaceRecordId = this.objInterfaceBean.getInterfaceRecordId();
			String strSystemSourceRef = this.objInterfaceBean.getSystemSourceRefNo();
			int businessUnit = this.objInterfaceBean.getBusinessUnitID();
			int batch_id = this.objInterfaceBean.getBatchId();
			String businessUnitSystem = this.objInterfaceBean.getServerName();
			String strDataSystemSource = this.objInterfaceBean.getBusinessUnitName();
			String debugTbl = this.objInterfaceBean.getDebugTbl();
			String BUsqldriver = this.objInterfaceBean.getBusinessUnitDriver();
			Connection sqlBUConn = this.objInterfaceBean.getBusinessUnitConnection();

			Date date = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String updateComp = null;
			String updateRep = null;
			String updatePart = null;
			String updateAttach = null;
			eMsg = eMsg.replaceAll("'", "");
			if (eMsg.contains("Error: ")) {
//				etQDebug.dDebug(1,"D","COMPLAINT",eMsg,strInterfaceRecordId,"COMPLAINT","","0",strSystemSourceRef,3,businessUnit,batch_id,businessUnitSystem,debugTbl);   	
				objUtil.dDebug(1, ft.format(date), eMsg, strInterfaceRecordId, null, "COMPLAINT", "COMPLAINT", 0,
						strSystemSourceRef, strDataSystemSource, null, null, 3, businessUnit, batch_id);

				if (BUsqldriver.equals("oracle.jdbc.driver.OracleDriver")) {
					updateComp = "update XX_COMPLAINT_INTERFACE set ETQ_BATCH_ID=" + batch_id
							+ ",ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='" + eMsg + "',UPLOADED_TO_ETQ_DATE=TO_DATE('"
							+ ft.format(date) + "','YYYY-MM-DD HH24:MI:SS') where interface_record_id="
							+ strInterfaceRecordId;
					updateRep = "update XX_COMP_AR_CODE_INTERFACE set ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='" + eMsg + "',UPLOADED_TO_ETQ_DATE=TO_DATE('"
							+ ft.format(date) + "','YYYY-MM-DD HH24:MI:SS') where interface_record_id="
							+ strInterfaceRecordId;
					updatePart = "update XX_COMP_PART_ANA_CAU_CODE_INT set ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='" + eMsg + "',UPLOADED_TO_ETQ_DATE=TO_DATE('"
							+ ft.format(date) + "','YYYY-MM-DD HH24:MI:SS') where interface_record_id="
							+ strInterfaceRecordId;
					updateAttach = "update XX_COMP_INI_ATTACHMENT set ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='" + eMsg + "',UPLOADED_TO_ETQ_DATE=TO_DATE('"
							+ ft.format(date) + "','YYYY-MM-DD HH24:MI:SS') where interface_record_id="
							+ strInterfaceRecordId;
				} else {
					updateComp = "update XX_COMPLAINT_INTERFACE set ETQ_BATCH_ID=" + batch_id
							+ ",ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='" + eMsg + "',UPLOADED_TO_ETQ_DATE='"
							+ ft.format(date) + "' where interface_record_id=" + strInterfaceRecordId;
					updateRep = "update XX_COMP_AR_CODE_INTERFACE set ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='" + eMsg + "',UPLOADED_TO_ETQ_DATE='"
							+ ft.format(date) + "' where interface_record_id=" + strInterfaceRecordId;
					updatePart = "update XX_COMP_PART_ANA_CAU_CODE_INT set ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='" + eMsg + "',UPLOADED_TO_ETQ_DATE='"
							+ ft.format(date) + "' where interface_record_id=" + strInterfaceRecordId;
					updateAttach = "update XX_COMP_INI_ATTACHMENT set ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='" + eMsg + "',UPLOADED_TO_ETQ_DATE='"
							+ ft.format(date) + "' where interface_record_id=" + strInterfaceRecordId;
				}
			} else {
				if (this.objInterfaceBean.getPaeQuestionAns2() != null
						&& this.objInterfaceBean.getPaeQuestionAns2().length() > 0) {
					if (this.objInterfaceBean.getPaeQuestionAns2().equalsIgnoreCase("Answer 1")) {
						eMsg = "Complaint created successfully (modified PAE Q2 to Yes)";
					} else if (this.objInterfaceBean.getPaeQuestionAns2().equalsIgnoreCase("Answer 3")) {
						eMsg = "Complaint created successfully (modified PAE Q2 to Unknown)";
					}
				} else {
					eMsg = "Complaint created successfully";
				}

//				etQDebug.dDebug(1,"D","COMPLAINT",eMsg,strInterfaceRecordId,"COMPLAINT",strDocumentNumber,"0",strSystemSourceRef,4,businessUnit,batch_id,businessUnitSystem,debugTbl);
				objUtil.dDebug(1, ft.format(date), eMsg, strInterfaceRecordId, strDocumentNumber, "COMPLAINT",
						"COMPLAINT", 0, strSystemSourceRef, strDataSystemSource, null, null, 4, businessUnit, batch_id);

				if (BUsqldriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) {
					updateComp = "update XX_COMPLAINT_INTERFACE set ETQ_BATCH_ID=" + batch_id
							+ ",ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='" + eMsg + "',UPLOADED_TO_ETQ_DATE=TO_DATE('"
							+ ft.format(date) + "','YYYY-MM-DD HH24:MI:SS'),ETQ_COMPLAINT_NUMBER= '" + strDocumentNumber
							+ "' where interface_record_id=" + strInterfaceRecordId;
					updateRep = "update  XX_COMP_AR_CODE_INTERFACE set  ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='Reported Code value set successfully',UPLOADED_TO_ETQ_DATE=TO_DATE('"
							+ ft.format(date) + "','YYYY-MM-DD HH24:MI:SS'),ETQ_COMPLAINT_NUMBER= '" + strDocumentNumber
							+ "' where INTERFACE_RECORD_ID=" + strInterfaceRecordId;
					updatePart = "update XX_COMP_PART_ANA_CAU_CODE_INT set ETQ_BATCH_ID=" + batch_id
							+ ",ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='Part Code,Analyzed Code and Cause Code value set successfully',UPLOADED_TO_ETQ_DATE=TO_DATE('"
							+ ft.format(date) + "','YYYY-MM-DD HH24:MI:SS'),ETQ_COMPLAINT_NUMBER= '" + strDocumentNumber
							+ "' where INTERFACE_RECORD_ID=" + strInterfaceRecordId;
					updateAttach = "update XX_COMP_INI_ATTACHMENT set ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='File attached successfully',UPLOADED_TO_ETQ_DATE=TO_DATE('"
							+ ft.format(date) + "','YYYY-MM-DD HH24:MI:SS'),ETQ_COMPLAINT_NUMBER='" + strDocumentNumber
							+ "' where interface_record_id=" + strInterfaceRecordId;
				} else {
					updateComp = "update XX_COMPLAINT_INTERFACE set ETQ_BATCH_ID=" + batch_id
							+ ",ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='" + eMsg + "',UPLOADED_TO_ETQ_DATE='"
							+ ft.format(date) + "',ETQ_COMPLAINT_NUMBER= '" + strDocumentNumber
							+ "' where interface_record_id='" + strInterfaceRecordId
							+ "' AND (ETQ_BATCH_ID=-1 OR ETQ_BATCH_ID=" + batch_id + ")";
					updateRep = "update  XX_COMP_AR_CODE_INTERFACE set  ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='Reported Code value set successfully',UPLOADED_TO_ETQ_DATE='"
							+ ft.format(date) + "',ETQ_COMPLAINT_NUMBER= '" + strDocumentNumber
							+ "' where interface_record_id=" + strInterfaceRecordId;
					updatePart = "update XX_COMP_PART_ANA_CAU_CODE_INT set ETQ_BATCH_ID=" + batch_id
							+ ",ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='Part Code,Analyzed Code and Cause Code value set successfully',UPLOADED_TO_ETQ_DATE='"
							+ ft.format(date) + "',ETQ_COMPLAINT_NUMBER= '" + strDocumentNumber
							+ "' where interface_record_id=" + strInterfaceRecordId;
					updateAttach = "update XX_COMP_INI_ATTACHMENT SET ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='File Attached Successfully',UPLOADED_TO_ETQ_DATE='"
							+ ft.format(date) + "',ETQ_COMPLAINT_NUMBER= '" + strDocumentNumber
							+ "' where interface_record_id=" + strInterfaceRecordId;
				}
			}

			Statement updateCompTbl = sqlBUConn.createStatement();
			updateCompTbl.executeUpdate(updateComp);
			updateCompTbl.executeUpdate(updateRep);
			updateCompTbl.executeUpdate(updatePart);
			updateCompTbl.executeUpdate(updateAttach);
			if (BUsqldriver.equals("oracle.jdbc.driver.OracleDriver")) {
				updateCompTbl.executeUpdate("commit");
			}
		} catch (Exception er) {
			logger.error("System Error:", er);
		} finally {
			this.objInterfaceBean.setErrorMessage(null);
		}
	}

	public void updateActivityTables(XxComplaintInterfaceBean objInterfaceBean, String eMsg, String strActSQ) {
		// system.out.println("into the Activity Details");
		try {
			String strInterfaceRecordId = this.objInterfaceBean.getInterfaceRecordId();
			String strSystemSourceRef = this.objInterfaceBean.getSystemSourceRefNo();
			int businessUnit = this.objInterfaceBean.getBusinessUnitID();
			int batch_id = this.objInterfaceBean.getBatchId();
			String businessUnitSystem = this.objInterfaceBean.getServerName();
			String debugTbl = this.objInterfaceBean.getDebugTbl();
			String BUsqldriver = this.objInterfaceBean.getBusinessUnitDriver();
			Connection sqlBUConn = this.objInterfaceBean.getBusinessUnitConnection();
			String strCompNumber = this.objInterfaceBean.getComplaintNumber();
			String strActNumber = this.objInterfaceBean.getInvestigationNumber();
			String strDataSystemSource = this.objInterfaceBean.getBusinessUnitName();

			Date date = new Date();
			SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String updateAct = null;
			eMsg = eMsg.replaceAll("'", "");
			if (eMsg.contains("Error")) {
				// etQDebug.dDebug(1,"D","COMPLAINT",eMsg,strInterfaceRecordId,"ACTIVITY","","0",strSystemSourceRef,3,businessUnit,batch_id,businessUnitSystem,debugTbl);
				objUtil.dDebug(1, ft.format(date), eMsg, strActSQ, null, "COMPLAINT", "ACTIVITY",
						Long.parseLong(strInterfaceRecordId), strSystemSourceRef, strDataSystemSource, null, null, 3,
						businessUnit, batch_id);
				if (BUsqldriver.equals("oracle.jdbc.driver.OracleDriver")) {
					updateAct = "update XX_COMP_INV_ACT_INTERFACE set ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='" + eMsg + "',UPLOADED_TO_ETQ_DATE=TO_DATE('"
							+ ft.format(date) + "','YYYY-MM-DD HH24:MI:SS') where interface_record_id="
							+ strInterfaceRecordId;
				} else {
					updateAct = "update XX_COMP_INV_ACT_INTERFACE set ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=3,ETQ_UPLOAD_MESSAGE='" + eMsg + "',UPLOADED_TO_ETQ_DATE='"
							+ ft.format(date) + "' where interface_record_id=" + strInterfaceRecordId;
				}
			} else {
//				etQDebug.dDebug(1,"D","COMPLAINT","Activity created successfully",strActSQ,"ACTIVITY",strActNumber,strInterfaceRecordId,strSystemSourceRef,4,businessUnit,batch_id,businessUnitSystem,debugTbl);
				objUtil.dDebug(1, ft.format(date), "Activity Created Successfully", strActSQ, strActNumber, "COMPLAINT",
						"ACTIVITY", Long.parseLong(strInterfaceRecordId), strSystemSourceRef, strDataSystemSource, null,
						null, 4, businessUnit, batch_id);

				if (BUsqldriver.equalsIgnoreCase("oracle.jdbc.driver.OracleDriver")) {
					updateAct = "update XX_COMP_INV_ACT_INTERFACE set ETQ_ACTIVITY_NUMBER='" + strActNumber
							+ "', ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='Activity created successfully',UPLOADED_TO_ETQ_DATE=TO_DATE('"
							+ ft.format(date) + "','YYYY-MM-DD HH24:MI:SS'),ETQ_COMPLAINT_NUMBER= '" + strCompNumber
							+ "' where SEQ_NUMBER='" + strActSQ + "' and interface_record_id=" + strInterfaceRecordId;
				} else {
					updateAct = "update XX_COMP_INV_ACT_INTERFACE set ETQ_ACTIVITY_NUMBER='" + strActNumber
							+ "', ETQ_BATCH_ID=" + batch_id
							+ ", ETQ_UPLOAD_STATUS=4,ETQ_UPLOAD_MESSAGE='Activity created successfully',UPLOADED_TO_ETQ_DATE='"
							+ ft.format(date) + "',ETQ_COMPLAINT_NUMBER= '" + strCompNumber + "' where SEQ_NUMBER='"
							+ strActSQ + "' and interface_record_id=" + strInterfaceRecordId;
				}
			}

			Statement updateCompTbl = sqlBUConn.createStatement();
			updateCompTbl.executeUpdate(updateAct);
			if (BUsqldriver.equals("oracle.jdbc.driver.OracleDriver")) {
				updateCompTbl.executeUpdate("commit");
			}
		} catch (Exception er) {
			logger.error("System Error:", er);
		} finally {
			this.objInterfaceBean.setErrorMessage(null);
		}
	}

	public String getFormatedDate(String strDate) throws ParseException {
		Date formateDate = null;
		if (strDate != null) {
			if (strDate.length() > 10) {
				formateDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(strDate);
				return this.objInterfaceBean.getDateFormat().format(formateDate);
				
			} else {
				formateDate = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
				return this.objInterfaceBean.getDateOnlyFormat().format(formateDate);
			}
			
		} else {
			return null;
		}
	}

	public String getDtAnswer(String strDtAns) {
		String decisionTreeAnswer = "";
		if (strDtAns != null && strDtAns.length() > 0) {
			if (strDtAns.equalsIgnoreCase("Y") || strDtAns.equalsIgnoreCase("Yes")) {
				decisionTreeAnswer = "Answer 1";
			} else if (strDtAns.equalsIgnoreCase("N") || strDtAns.equalsIgnoreCase("No")) {
				decisionTreeAnswer = "Answer 2";
			} else if (strDtAns.equalsIgnoreCase("U") || strDtAns.equalsIgnoreCase("Unknown")) {
				decisionTreeAnswer = "Answer 3";
			} else {
				decisionTreeAnswer = "Answer 3";
			}
		}
		return decisionTreeAnswer;
	}

	public String getBooleanValue(String strValue) {
		String strAnswer = "";
		if (strValue != null && strValue.length() > 0) {
			if (strValue.equalsIgnoreCase("Y") || strValue.equalsIgnoreCase("Yes")) {
				strAnswer = "Yes";
			} else if (strValue.equalsIgnoreCase("N") || strValue.equalsIgnoreCase("No")) {
				strAnswer = "No";
			} else if (strValue.equalsIgnoreCase("U") || strValue.equalsIgnoreCase("Unknown")) {
				strAnswer = "Unknown";
			}
		} else {
			strAnswer = "Unknown";
		}
		return strAnswer;
	}

	public String getYesNo(String strValue) {
		String strAnswer = "";
		if (strValue != null && strValue.length() > 0) {
			if (strValue.equalsIgnoreCase("Y") || strValue.equalsIgnoreCase("Yes")) {
				strAnswer = "Yes";
			} else if (strValue.equalsIgnoreCase("N") || strValue.equalsIgnoreCase("No")) {
				strAnswer = "No";
			}
		} else {
			strAnswer = "No";
		}
		return strAnswer;
	}

	public String getYesNoNoInfo(String strValue) {
		String strAnswer = "";
		if (strValue != null && strValue.length() > 0) {
			if (strValue.equalsIgnoreCase("Y") || strValue.equalsIgnoreCase("Yes")) {
				strAnswer = "Yes";
			} else if (strValue.equalsIgnoreCase("N") || strValue.equalsIgnoreCase("No")) {
				strAnswer = "No";
			} else {
				strAnswer = "No Information";
			}
		} else {
			strAnswer = "No Information";
		}
		return strAnswer;
	}

	public String getCurrentDate() {
		Date date = new Date();
		SimpleDateFormat currDtFormat = new SimpleDateFormat("yyyy-MM-dd");
		return currDtFormat.format(date);
	}

	public String getDateByTimeZone(String strDate, String strTimeZones) {
		String strConvertedDt = null;
		try {
			String strFrom = strTimeZones.split(":")[1];
			String strTo = strTimeZones.split(":")[0];
			if (strDate != null) {
				if (strDate.length() <= 10) {
					strDate = strDate + " 00:00:00";
				}

				TimeZone fromTimeZone = TimeZone.getTimeZone(strFrom); // Source timezone
				DateFormat fromFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				fromFormatter.setTimeZone(fromTimeZone);
				Date currentDate = fromFormatter.parse(strDate);

				TimeZone toTimeZone = TimeZone.getTimeZone(strTo); // Target timezone
				this.objInterfaceBean.getDateFormat().setTimeZone(toTimeZone);
				strConvertedDt = this.objInterfaceBean.getDateFormat().format(currentDate);
			}
		} catch (Exception e) {
			logger.error("Error During Timezone Conversion: ", e);
		}
		return strConvertedDt;
	}

	public String getCreatedDate(String strTimeZones) throws ParseException {
		Date date = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
		String strFrom = strTimeZones.split(":")[1];
		TimeZone fromTm = TimeZone.getTimeZone(strFrom);
		dateFormat.setTimeZone(fromTm);
		Date currUserTM = dateFormat.parse(dateFormat.format(date));

		String strTo = strTimeZones.split(":")[0];
		SimpleDateFormat toFormat = new SimpleDateFormat("MMM dd, yyyy hh:mm a");
		toFormat.setTimeZone(TimeZone.getTimeZone(strTo));
		return toFormat.format(currUserTM);
	}
}