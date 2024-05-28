package com.olympus.OEKG.ComplaintService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
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
import java.util.Map;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.olympus.OEKG.Utility.utility;

public class XxComplaintInterfaceValidation {

	final Logger logger = LoggerFactory.getLogger(XxComplaintInterfaceValidation.class);

	XxComplaintInterfaceBean objComplaintBean;

	public XxComplaintInterfaceBean validateComplaintFields(XxComplaintInterfaceBean objComplaintBean) {
		this.objComplaintBean = objComplaintBean;
		Map<String, String> mapComplaintData = this.objComplaintBean.getMapResultSet();
		this.objComplaintBean.setMapResultSet(mapComplaintData);
		Connection baseConnection = this.objComplaintBean.getBaseConnection();
		int strBusinessUditID = this.objComplaintBean.getBusinessUnitID();
		String strEtQAuthor = this.objComplaintBean.getEtQUser();
		this.objComplaintBean.setInterfaceRecordId(mapComplaintData.get("1"));
		this.objComplaintBean.setSystemSourceRefNo(mapComplaintData.get("2"));
		// Added for MDMTESTING-4473
		this.objComplaintBean.setComplaintClosureDate(mapComplaintData.get("47"));

		if (mapComplaintData.get("41") != null && mapComplaintData.get("41").length() > 0) {
			String strInveReq = getYesNoValue(mapComplaintData.get("41"));

			if (strInveReq != null && strInveReq.length() > 0) {
				this.objComplaintBean.setInvestigationRequired(strInveReq);
			} else {
				this.objComplaintBean.setInvestigationRequired("");
			}
		} else {
			this.objComplaintBean.setInvestigationRequired("");
		}

		if (mapComplaintData.get("47") != null && mapComplaintData.get("47").length() > 0) {
			this.objComplaintBean.setPhaseName("COMPLAINTS_COMPLAINT_HANDLING_CLOSED");

			if (mapComplaintData.get("39") != null && mapComplaintData.get("39").length() > 0) {
				if (this.objComplaintBean.getInvestigationRequired() != null
						&& this.objComplaintBean.getInvestigationRequired().length() > 0) {
					if (this.objComplaintBean.getInvestigationRequired().equalsIgnoreCase("Yes")) {
						this.objComplaintBean.setSecondaryPhaseName("COMPLAINTS_COMPLAINT_HANDLING_REVIEW");
					} else {
						this.objComplaintBean.setSecondaryPhaseName("COMPLAINTS_COMPLAINT_HANDLING_INVESTIGATION");
					}
				} else {
					this.objComplaintBean.setSecondaryPhaseName("COMPLAINTS_COMPLAINT_HANDLING_INVESTIGATION");
				}
			} else {
				if (mapComplaintData.get("54") != null && mapComplaintData.get("54").length() > 0) {
					this.objComplaintBean.setSecondaryPhaseName("COMPLAINTS_COMPLAINT_HANDLING_INVESTIGATION");
				} else {
					this.objComplaintBean.setSecondaryPhaseName("COMPLAINTS_COMPLAINT_HANDLING_DRAFT");
				}
			}
		} else {
			this.objComplaintBean.setSecondaryPhaseName("");
			if (mapComplaintData.get("39") != null && mapComplaintData.get("39").length() > 0) {
				if (this.objComplaintBean.getInvestigationRequired() != null
						&& this.objComplaintBean.getInvestigationRequired().length() > 0) {
					if (this.objComplaintBean.getInvestigationRequired().equalsIgnoreCase("Yes")) {
						this.objComplaintBean.setPhaseName("COMPLAINTS_COMPLAINT_HANDLING_REVIEW");
					} else {
						this.objComplaintBean.setPhaseName("COMPLAINTS_COMPLAINT_HANDLING_INVESTIGATION");
					}
				} else {
					this.objComplaintBean.setPhaseName("COMPLAINTS_COMPLAINT_HANDLING_INVESTIGATION");
				}
			} else {
				if (mapComplaintData.get("54") != null && mapComplaintData.get("54").length() > 0) {
					this.objComplaintBean.setPhaseName("COMPLAINTS_COMPLAINT_HANDLING_INVESTIGATION");
				} else {
					this.objComplaintBean.setPhaseName("COMPLAINTS_COMPLAINT_HANDLING_DRAFT");
				}
			}
		}

		String strPhaseName = this.objComplaintBean.getPhaseName();
		String strReturnStatus = new String();
		String strErrorMsg = new String();
		int intPhaseID = 0;
		// Added for ETQCR-661
		String strAwareDate = null;
		int intAwareDtFlag = 0;
		String strNotificationDate = null;
		int intNotificationDtFlag = 0;

		if (strPhaseName.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_DRAFT")) {
			intPhaseID = 1;
		} else if (strPhaseName.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_INVESTIGATION")) {
			intPhaseID = 2;
		} else if (strPhaseName.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_REVIEW")) {
			intPhaseID = 3;
		} else if (strPhaseName.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED")) {
			intPhaseID = 4;
		}

		String strStstemSourRefNo = null;
		String strPAEQuestion1 = null;
		String strModelNumber = null;

		if (mapComplaintData.size() > 0) {
			ResultSet valiResultSet = null;
			try {
				Statement smt = baseConnection.createStatement();
				String query = "select RECORD_SEQUENCE,FIELD_NAME from XX_ETQ_VALIDATION_TBL where BUSINESS_UNIT_SITE_ID ="
						+ strBusinessUditID
						+ "and IS_ACTIVE='Y' and VALIDATION_TYPE='MANDATORY' AND PHASE_DESIGN_NAME <=" + intPhaseID;

				valiResultSet = smt.executeQuery(query);

				if (valiResultSet != null) {
					while (valiResultSet.next()) {
						String strCompRsSeq = valiResultSet.getString(1);
						String strCompRsValue = mapComplaintData.get(strCompRsSeq);
//						System.out.println("valiResultSet value:" + strCompRsSeq);
//						System.out.println("valiResultSet value2:" + strCompRsValue);
						if (strCompRsSeq.equalsIgnoreCase("2")) {
							if (strCompRsValue == null || strCompRsValue.length() == 0) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || "
											+ "Error: System Source Reference No. is required field ";
								}
							} else {
								strStstemSourRefNo = strCompRsValue;
							}
						} else if (strCompRsSeq.equalsIgnoreCase("3")) {
							if (strCompRsValue == null || strCompRsValue.length() == 0) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + "Error: System Source is required field ";
								}
							} else if (strStstemSourRefNo != null && strCompRsValue != null) {
								strReturnStatus = validateSysRefAndSysSource(strStstemSourRefNo, strCompRsValue);
								if (strReturnStatus != null) {
									if (strReturnStatus.contains("Error: ")) {
										if ((strErrorMsg == null || strErrorMsg != null)
												&& strErrorMsg.length() <= 3000) {
											strErrorMsg = strErrorMsg + " || " + strReturnStatus;
										}
									} else {
										this.objComplaintBean.setSystemSource(strReturnStatus);
									}
								}
							}
						}
						// Added for ETQCR-661
						else if (strCompRsSeq.equalsIgnoreCase("93")) {
							strNotificationDate = strCompRsValue;
							intNotificationDtFlag = 1;
						} else if (strCompRsSeq.equalsIgnoreCase("8")) {
							// Commented for ETQCR-661
							/*
							 * if (strCompRsValue == null || strCompRsValue.length() == 0) { if
							 * ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <=
							 * 3000) { strErrorMsg = strErrorMsg +" || "+
							 * "Error: Aware Date is required field "; } }
							 */
							strAwareDate = strCompRsValue;
							intAwareDtFlag = 1;
						} else if (strCompRsSeq.equalsIgnoreCase("9")) {
							if ((strCompRsValue == null || strCompRsValue.length() == 0)
									&& (mapComplaintData.get("95") == null
											|| mapComplaintData.get("95").length() == 0)) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + "Error: Reporting Person is required field ";
								}
							}
						} else if (strCompRsSeq.equalsIgnoreCase("12")) {
							if ((strCompRsValue == null || strCompRsValue.length() == 0)
									&& (mapComplaintData.get("96") == null
											|| mapComplaintData.get("96").length() == 0)) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + "Error: Event Description is required field ";
								}
							}
						} else if (strCompRsSeq.equalsIgnoreCase("15")) {
							if (strCompRsValue == null || strCompRsValue.length() == 0) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + "Error: PAE Question 1 is required field ";
								}
							} else {
								strCompRsValue = getDTBooleanValue(strCompRsValue);
								strPAEQuestion1 = strCompRsValue;
							}
						} else if (strCompRsSeq.equalsIgnoreCase("16")) {
							if (strPAEQuestion1 != null && !strPAEQuestion1.equalsIgnoreCase("Yes")
									&& (strCompRsValue == null || strCompRsValue.length() == 0)) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + "Error: PAE Question 2 is required field ";
								}
							} else if (strPAEQuestion1 != null || strCompRsValue != null) {
								strCompRsValue = getDTBooleanValue(strCompRsValue);
								strReturnStatus = validatePAEQuestion(strPAEQuestion1, strCompRsValue, strPhaseName);

								if (strReturnStatus != null) {
									if (strReturnStatus.contains("Error: ")) {
										if ((strErrorMsg == null || strErrorMsg != null)
												&& strErrorMsg.length() <= 3000) {
											strErrorMsg = strErrorMsg + " || " + strReturnStatus;
										}
									}
								}
							}
						} else if (strCompRsSeq.equalsIgnoreCase("19")) {
							if ((strCompRsValue == null || strCompRsValue.length() == 0)
									&& (mapComplaintData.get("97") == null
											|| mapComplaintData.get("97").length() == 0)) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + "Error: Customer Telephone is required field ";
								}
							} else {
								String strCustomerTelephone = null;
								if (strCompRsValue == null || strCompRsValue.length() == 0) {
									if (mapComplaintData.get("97") != null && mapComplaintData.get("97").length() > 0) {
										strCustomerTelephone = mapComplaintData.get("97");
									}
								} else {
									strCustomerTelephone = strCompRsValue;
								}
								this.objComplaintBean.setCustomerTelephone(strCustomerTelephone);

								/*
								 * Commented for ETQCR-912 if (strCustomerTelephone != null &&
								 * strCustomerTelephone.length() > 0) { if (mapComplaintData.get("24") != null
								 * && mapComplaintData.get("24").equalsIgnoreCase("United States")) {
								 * strReturnStatus = validateTelephoneFormat(strCustomerTelephone); if
								 * (strReturnStatus != null) { if
								 * (strReturnStatus.contains("number requires min 10 numbers")) { if
								 * ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <=
								 * 3000) { strErrorMsg = strErrorMsg +" || Error: Customer Telephone" +
								 * strReturnStatus; } } else {
								 * this.objComplaintBean.setCustomerTelephone(strReturnStatus); } } } }
								 */
							}
						} else if (strCompRsSeq.equalsIgnoreCase("21")) {
							if ((strCompRsValue == null || strCompRsValue.length() == 0)
									&& (mapComplaintData.get("98") == null
											|| mapComplaintData.get("98").length() == 0)) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + "Error: Customer Name is required field ";
								}
							}
						} else if (strCompRsSeq.equalsIgnoreCase("22")) {
							if ((strCompRsValue == null || strCompRsValue.length() == 0)
									&& (mapComplaintData.get("99") == null
											|| mapComplaintData.get("99").length() == 0)) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + "Error: Customer City is required field ";
								}
							}
						} else if (strCompRsSeq.equalsIgnoreCase("23")) {
							if ((strCompRsValue == null || strCompRsValue.length() == 0)
									&& (mapComplaintData.get("100") == null
											|| mapComplaintData.get("100").length() == 0)) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + "Error: Customer Address is required field ";
								}
							}
						} else if (strCompRsSeq.equalsIgnoreCase("24")) {
							if (strCompRsValue == null || strCompRsValue.length() == 0) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + "Error: Customer Country is required field ";
								}
							} else {
								strReturnStatus = validateCustomerCountry(strCompRsValue);
								if (strReturnStatus != null) {
									if (strReturnStatus.contains("Error: ")) {
										if ((strErrorMsg == null || strErrorMsg != null)
												&& strErrorMsg.length() <= 3000) {
											strErrorMsg = strErrorMsg + " || " + strReturnStatus;
										}
									} else {
										this.objComplaintBean.setCustomerCountry(strReturnStatus);

										if (this.objComplaintBean.getCustomerCountry()
												.equalsIgnoreCase("United States")) {
											String strCusState = mapComplaintData.get("25");
											strReturnStatus = validateCustomerState(strCusState);
											if (strReturnStatus != null) {
												if (strReturnStatus.contains("Error: ")) {
													if ((strErrorMsg == null || strErrorMsg != null)
															&& strErrorMsg.length() <= 3000) {
														strErrorMsg = strErrorMsg + " || " + strReturnStatus;
													}
												} else {
													this.objComplaintBean.setCustomerState(strReturnStatus);
												}
											}
										} else {
											this.objComplaintBean.setCustomerState(mapComplaintData.get("25"));
										}
									}
								}
							}
						} else if (strCompRsSeq.equalsIgnoreCase("88")) {
							if (strCompRsValue == null || strCompRsValue.length() == 0) {
								strModelNumber = null;
							} else {
								strModelNumber = strCompRsValue;
							}
						} else if (strCompRsSeq.equalsIgnoreCase("26")) {
							if (strCompRsValue != null && strCompRsValue.length() == 0) {
								strCompRsValue = null;
							}
							strReturnStatus = validateModelNumber(strModelNumber, strCompRsValue);
							if (strReturnStatus != null) {
								if (strReturnStatus.contains("Error: ")) {
									if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
										strErrorMsg = strErrorMsg + " || " + strReturnStatus;
									}
								}
							}
						} else if (strCompRsSeq.equalsIgnoreCase("42")) {
							String strInveReq = this.objComplaintBean.getInvestigationRequired();

							if (strInveReq != null && strInveReq.length() > 0) {
								if (!strInveReq.equalsIgnoreCase("Yes") && !strInveReq.equalsIgnoreCase("No")) {
									if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
										strErrorMsg = strErrorMsg
												+ " || Error: Investigation Required? - Display value(s): ["
												+ strInveReq.replaceAll("%20", " ")
												+ "] is not listed in the field's keyword options ";
									}
								} else if (strInveReq.equalsIgnoreCase("No")
										&& (strCompRsValue == null || strCompRsValue.length() == 0)) {
									if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
										strErrorMsg = strErrorMsg
												+ " || Error: No Investigation Rationale is required field ";
									}
								}
							} else {
								if ((this.objComplaintBean.getPhaseName() != null
										&& this.objComplaintBean.getPhaseName().length() > 0
										&& this.objComplaintBean.getPhaseName()
												.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_REVIEW"))
										|| (this.objComplaintBean.getPhaseName() != null
												&& this.objComplaintBean.getPhaseName().length() > 0
												&& this.objComplaintBean.getPhaseName()
														.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED"))) {
									strErrorMsg = strErrorMsg + " || Error: Investigation required is required field ";
								}
							}
						} else if (strCompRsSeq.equalsIgnoreCase("45")) {
							if ((strCompRsValue == null || strCompRsValue.length() == 0)
									&& (mapComplaintData.get("110") == null
											|| mapComplaintData.get("110").length() == 0)) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || "
											+ "Error: Correction Description is required field ";
								}
							}
						}

						// Added Validation for ETQCR-824 but CR got rejected
						/*
						 * else if (strCompRsSeq.equalsIgnoreCase("111")) { if
						 * (mapComplaintData.get("111") != null &&
						 * mapComplaintData.get("111").length()>0) { if ((strErrorMsg == null ||
						 * strErrorMsg != null) && strErrorMsg.length() <= 3000) {
						 * if(mapComplaintData.get("111").toString().equalsIgnoreCase("1900-01-01")) {
						 * strErrorMsg = strErrorMsg +" || "+
						 * "Error: Expiration date contains invalid date format "; } else
						 * if(validateDateFormat(mapComplaintData.get("111")).equalsIgnoreCase("No")) {
						 * strErrorMsg = strErrorMsg +" || "+
						 * "Error: Expiration date contains invalid date format "; } } } }
						 */
					}
				}

				// Added for ETQCR-950
				if (mapComplaintData.get("7") != null && mapComplaintData.get("7").length() > 0) {
					String strValidateEventDate = validateDate("Event Date", mapComplaintData.get("7"));
					if (strValidateEventDate != null && strValidateEventDate.contains("Error")) {
						strErrorMsg = strErrorMsg + " || " + strValidateEventDate + " ";
					}
				}

				// Added for ETQCR-950
				if (mapComplaintData.get("36") != null && mapComplaintData.get("36").length() > 0) {
					String strValidatePurchaseDate = validateDate("Purchase Date", mapComplaintData.get("36"));
					if (strValidatePurchaseDate != null && strValidatePurchaseDate.contains("Error")) {
						strErrorMsg = strErrorMsg + " || " + strValidatePurchaseDate + " ";
					}
				}

				// Added for ETQCR-950
				if (mapComplaintData.get("37") != null && mapComplaintData.get("37").length() > 0) {
					String strValidateProductRecDt = validateDate("Complaint Product Receipt Date",
							mapComplaintData.get("37"));
					if (strValidateProductRecDt != null && strValidateProductRecDt.contains("Error")) {
						strErrorMsg = strErrorMsg + " || " + strValidateProductRecDt + " ";
					}
				}

				// Added for ETQCR-950
				if (mapComplaintData.get("39") != null && mapComplaintData.get("39").length() > 0) {
					String strValidateEvalCompDt = validateDate("Evaluation Completion Date",
							mapComplaintData.get("39"));
					if (strValidateEvalCompDt != null && strValidateEvalCompDt.contains("Error")) {
						strErrorMsg = strErrorMsg + " || " + strValidateEvalCompDt + " ";
					}
				}

				// Added for ETQCR-950
				if (mapComplaintData.get("44") != null && mapComplaintData.get("44").length() > 0) {
					String strValidateInvDecDt = validateDate("Investigation Decision Date",
							mapComplaintData.get("44"));
					if (strValidateInvDecDt != null && strValidateInvDecDt.contains("Error")) {
						strErrorMsg = strErrorMsg + " || " + strValidateInvDecDt + " ";
					}
				}

				// Added for ETQCR-950
				if (mapComplaintData.get("47") != null && mapComplaintData.get("47").length() > 0) {
					String strValidateCompClosureDt = validateDate("Complaint Closure Date",
							mapComplaintData.get("47"));
					if (strValidateCompClosureDt != null && strValidateCompClosureDt.contains("Error")) {
						strErrorMsg = strErrorMsg + " || " + strValidateCompClosureDt + " ";
					}

					// Added for ETQCR-1024
					if (strNotificationDate != null && strNotificationDate.length() > 0) {
						String strCompareDt = compareDate(mapComplaintData.get("47"), strNotificationDate);
						if (strCompareDt != null && strCompareDt.contains("Error")) {
							strErrorMsg = strErrorMsg + " || Error: Complaint Closure Date["
									+ mapComplaintData.get("47") + "] shall not be prior to Notification Date["
									+ strNotificationDate + "] ";
						}
					} else if (strAwareDate != null && strAwareDate.length() > 0) {
						String strCompareDt = compareDate(mapComplaintData.get("47"), strAwareDate);
						if (strCompareDt != null && strCompareDt.contains("Error")) {
							strErrorMsg = strErrorMsg + " || Error: Complaint Closure Date["
									+ mapComplaintData.get("47") + "] shall not be prior to Notification Date["
									+ strAwareDate + "] ";
						}
					}
				}

				// Added for ETQCR-950
				if (mapComplaintData.get("54") != null && mapComplaintData.get("54").length() > 0) {
					String strValidateEvalInitDt = validateDate("Evaluation Initiation Date",
							mapComplaintData.get("54"));
					if (strValidateEvalInitDt != null && strValidateEvalInitDt.contains("Error")) {
						strErrorMsg = strErrorMsg + " || " + strValidateEvalInitDt + " ";
					}
				}

				// Added for ETQCR-950
				if (mapComplaintData.get("58") != null && mapComplaintData.get("58").length() > 0) {
					String strValidateSystemSourceInitDt = validateDate("Initiation Date", mapComplaintData.get("58"));
					if (strValidateSystemSourceInitDt != null && strValidateSystemSourceInitDt.contains("Error")) {
						strErrorMsg = strErrorMsg + " || " + strValidateSystemSourceInitDt + " ";
					}
				}

				// Added for ETQCR-950
				if (mapComplaintData.get("60") != null && mapComplaintData.get("60").length() > 0) {
					String strValidateInstallDt = validateDate("Installation Date", mapComplaintData.get("60"));
					if (strValidateInstallDt != null && strValidateInstallDt.contains("Error")) {
						strErrorMsg = strErrorMsg + " || " + strValidateInstallDt + " ";
					}
				}

				// Added for ETQCR-950
				if (mapComplaintData.get("94") != null && mapComplaintData.get("94").length() > 0) {
					String strValidateSrDate2 = validateDate("System Source Initiation Date 2",
							mapComplaintData.get("94"));
					if (strValidateSrDate2 != null && strValidateSrDate2.contains("Error")) {
						strErrorMsg = strErrorMsg + " || " + strValidateSrDate2 + " ";
					}
				}

				// Added for ETQCR-950
				if (strNotificationDate != null && strNotificationDate.length() > 0) {
					String strValidateNtDate = validateDate("Notification Date", strNotificationDate);
					if (strValidateNtDate != null && strValidateNtDate.contains("Error")) {
						strErrorMsg = strErrorMsg + " || " + strValidateNtDate + " ";
					}
				}

				// Added for ETQCR-950
				if (strAwareDate != null && strAwareDate.length() > 0) {
					String strValidateAwareDate = validateDate("Aware Date", strAwareDate);
					if (strValidateAwareDate != null && strValidateAwareDate.contains("Error")) {
						strErrorMsg = strErrorMsg + " || " + strValidateAwareDate + " ";
					}
				}

				// Validation for Notification Date and Aware Date
				// Added for ETQCR-661
				String strIsPAE = this.objComplaintBean.getIsPotentialAdverseEvent();
				// system.out.println("strAwareDate: " + strAwareDate + " " + strIsPAE);
				if (strIsPAE != null && strIsPAE.length() > 0
						&& (strIsPAE.equalsIgnoreCase("No") || strIsPAE.equalsIgnoreCase("Unknown"))) {
					if (((strAwareDate == null || strAwareDate.length() == 0) && intAwareDtFlag == 1)
							&& ((strNotificationDate == null || strNotificationDate.length() == 0)
									&& intNotificationDtFlag == 1)) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + "Error: Notification Date is required field ";
						}
					}
				} else if (strIsPAE != null && strIsPAE.length() > 0 && strIsPAE.equalsIgnoreCase("Yes")) {
					if (((strAwareDate == null || strAwareDate.length() == 0) && intAwareDtFlag == 1)
							&& ((strNotificationDate == null || strNotificationDate.length() == 0)
									&& intNotificationDtFlag == 1)) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || "
									+ "Error: Notification Date and Aware Date are required fields ";
						}
					} else if (((strAwareDate == null || strAwareDate.length() == 0) && intAwareDtFlag == 1)) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + "Error: Aware Date is required field ";
						}
					}
				}

				String strSysSource2 = mapComplaintData.get("89");
				String strSysSourceRef2 = mapComplaintData.get("90");

				if (strSysSource2 != null && strSysSource2.length() > 0) // Modified for MDMTESTING-4503 & ETQCR-943
				{
					if (strSysSourceRef2 != null && strSysSourceRef2.length() > 0) {
						strReturnStatus = validateSysRefAndSysSource(strSysSourceRef2, strSysSource2);
						if (strReturnStatus != null && strReturnStatus.length() > 0) {
							if (strReturnStatus.contains("Error: ")) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + strReturnStatus;
								}
							} else {
								this.objComplaintBean.setSystemSource2(strReturnStatus);
							}
						}
						/*
						 * strReturnStatus = validateSystemSource(strSysSource2); if
						 * (strReturnStatus.contains("value(s)") || strReturnStatus.contains("Error: "))
						 * { if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <=
						 * 3000) { if (strReturnStatus.contains("Error: ")) { strErrorMsg = strErrorMsg
						 * +" || "+ strReturnStatus +"2 "; } else { strErrorMsg = strErrorMsg +" || "+
						 * "Error: System Source 2 " + strReturnStatus; } } } else {
						 * this.objComplaintBean.setSystemSource2(strReturnStatus); }
						 */
					}
				}

				strReturnStatus = validateInitatorLocation(mapComplaintData.get("62"),
						this.objComplaintBean.getEtQUser());
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setInitiatorLocation(strReturnStatus);
					}
				}

				strReturnStatus = validateInitatorBranch(mapComplaintData.get("63"));
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setInitiatorBranch("Yes");
					}
				}

				strReturnStatus = validateEventFoundAt(mapComplaintData.get("14"));
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setEventFoundAt(strReturnStatus);
					}
				}
				strReturnStatus = validateIsThisAComplaint(mapComplaintData.get("13"));
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setIsThisAComplaint(strReturnStatus);
					}
				}

				strReturnStatus = validateAnyActionsTakenAtCusSite(mapComplaintData.get("50"));
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setAnyActionsAlreadyTakenAtTheCustomerSite(strReturnStatus);
					}
				}

				strReturnStatus = validateCusRespReq(mapComplaintData.get("20"));
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setCustomerResponseRequested(strReturnStatus);
					}
				}

				String strRepPerTelephone = mapComplaintData.get("10");
				String strContactTelephone = mapComplaintData.get("34");
				if (this.objComplaintBean.getCustomerCountry() != null
						&& this.objComplaintBean.getCustomerCountry().equalsIgnoreCase("United States")) {
					if (strRepPerTelephone != null && strRepPerTelephone.length() > 0) {
						// Commented for ETQCR-912
						/*
						 * strReturnStatus = validateTelephoneFormat(strRepPerTelephone); if
						 * (strReturnStatus != null) { if
						 * (strReturnStatus.contains("number requires min 10 numbers")) { if
						 * ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <=
						 * 3000) { strErrorMsg = strErrorMsg +" || Error: Reporting Person Telephone" +
						 * strReturnStatus; } } else {
						 * this.objComplaintBean.setReportingPersonTelephone(strReturnStatus); } }
						 */
						this.objComplaintBean.setReportingPersonTelephone(strRepPerTelephone);
					}

					if (strContactTelephone != null && strContactTelephone.length() > 0) {
						// Commented for ETQCR-912
						/*
						 * strReturnStatus = validateTelephoneFormat(strContactTelephone); if
						 * (strReturnStatus != null) { if
						 * (strReturnStatus.contains("number requires min 10 numbers")) { if
						 * ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <=
						 * 3000) { strErrorMsg = strErrorMsg +" || Error: Contact Telephone" +
						 * strReturnStatus; } } else {
						 * this.objComplaintBean.setContactTelephone(strReturnStatus); } }
						 */
						this.objComplaintBean.setContactTelephone(strContactTelephone);
					}
				}

				String strCusLocation = mapComplaintData.get("91");
				if (strCusLocation != null && strCusLocation.length() > 0) {
					strReturnStatus = validateCustomerLocation(strCusLocation);
					if (strReturnStatus != null) {
						if (strReturnStatus.contains("Error: ")) {
							if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
								strErrorMsg = strErrorMsg + " || " + strReturnStatus;
							}
						} else {
							this.objComplaintBean.setCustomerLocation(strReturnStatus);
						}
					}
				} else if (strCusLocation == null || strCusLocation.length() == 0) {
					this.objComplaintBean.setCustomerLocation(this.objComplaintBean.getInitiatorLocation());
				}

				// Commented for ETQCR-912
				/*
				 * String strRepPersonEmail = mapComplaintData.get("11"); if (strRepPersonEmail
				 * != null && strRepPersonEmail.length() > 0) { strReturnStatus =
				 * validateEmailFormat(strRepPersonEmail); if(strReturnStatus.contains("false"))
				 * { if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <=
				 * 3000) { strErrorMsg = strErrorMsg
				 * +" || Error: Reporting Person Email ["+strRepPersonEmail+"] is not valid "; }
				 * } }
				 */

				// Commented for ETQCR-912
				/*
				 * String strContactPersonEmail = mapComplaintData.get("33"); if
				 * (strContactPersonEmail != null && strContactPersonEmail.length() > 0) {
				 * strReturnStatus = validateEmailFormat(strContactPersonEmail);
				 * if(strReturnStatus.contains("false")) { if ((strErrorMsg == null ||
				 * strErrorMsg != null) && strErrorMsg.length() <= 3000) { strErrorMsg =
				 * strErrorMsg
				 * +" || Error: Contact Email Address ["+strContactPersonEmail+"] is not valid "
				 * ; } } }
				 */

				strReturnStatus = validateHealthProfessional(mapComplaintData.get("31"));
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setHealthProfessional(strReturnStatus);
					}
				}

				strReturnStatus = validateOccupation(mapComplaintData.get("27"));
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setOccupation(strReturnStatus);
					}
				}

				String strIsSerial = this.objComplaintBean.getIsProductSerial();
				if (strIsSerial != null && strIsSerial.length() > 0
						&& (strIsSerial.equalsIgnoreCase("Y") || strIsSerial.equalsIgnoreCase("Yes"))) {
					this.objComplaintBean.setProductQuntity("1");
				} else if (mapComplaintData.get("5") != null && mapComplaintData.get("5").length() > 0
						&& Integer.parseInt(mapComplaintData.get("5")) > 0) {
					this.objComplaintBean.setProductQuntity(mapComplaintData.get("5").toString());
				} else {
					this.objComplaintBean.setProductQuntity("1");
				}

				strReturnStatus = validateCDSMethodBeforeRetOly(mapComplaintData.get("73"));
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setCdsMethodBeforeRetOly(strReturnStatus);
					}
				}

				strReturnStatus = valdiateWillProductRetnOly(mapComplaintData.get("4"));
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setIsProductReturn(strReturnStatus);
					}
				} else {
					this.objComplaintBean.setIsProductReturn("Unknown");
				}

				strReturnStatus = validateOrderType(mapComplaintData.get("59"));
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setOrderType(strReturnStatus);
					}
				}

				String strIsProductSoftware = this.objComplaintBean.getIsProductSoftware();
				if (strIsProductSoftware != null && strIsProductSoftware.length() > 0) {
					String strIsSoft = null;

					if (mapComplaintData.get("76") != null && mapComplaintData.get("76").length() > 0) {
						strIsSoft = getYesNoValue(mapComplaintData.get("76"));
						if (strIsSoft != null && strIsSoft.length() > 0) {
							this.objComplaintBean.setIsSoftware(strIsSoft);
						} else {
							this.objComplaintBean.setIsSoftware("");
						}
					} else {
						this.objComplaintBean.setIsSoftware("");
					}

					if (this.objComplaintBean.getIsSoftware() != null
							&& this.objComplaintBean.getIsSoftware().length() > 0
							&& !this.objComplaintBean.getIsSoftware().equalsIgnoreCase("Yes")
							&& !this.objComplaintBean.getIsSoftware().equalsIgnoreCase("No")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || Error: Software - Display value(s): [" + strIsSoft
									+ "] is not listed in the field's keyword options ";
						}
					}
					// Commented for ETQCR-693
					/*
					 * else if (this.objComplaintBean.getIsSoftware() != null &&
					 * this.objComplaintBean.getIsSoftware().length()>0 &&
					 * this.objComplaintBean.getIsSoftware().equalsIgnoreCase("Yes") &&
					 * strIsProductSoftware.equalsIgnoreCase("0")) { if ((strErrorMsg == null ||
					 * strErrorMsg != null) && strErrorMsg.length() <= 3000) { strErrorMsg =
					 * strErrorMsg
					 * +" || Error: Software Model Number or Item Code is not identified as Software "
					 * ; } }
					 */
					else if (this.objComplaintBean.getIsSoftware() != null
							&& this.objComplaintBean.getIsSoftware().length() > 0
							&& this.objComplaintBean.getIsSoftware().equalsIgnoreCase("Yes")
							&& strIsProductSoftware.equalsIgnoreCase("1")) {
						strReturnStatus = validateSoftwareVersion(mapComplaintData.get("56"));
						if (strReturnStatus != null) {
							if (strReturnStatus.contains("Error: ")) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + strReturnStatus;
								}
							} else {
								this.objComplaintBean.setSoftwareVersion(strReturnStatus);
							}
						}

						strReturnStatus = validateOperatingSysVersion(mapComplaintData.get("77"));
						if (strReturnStatus != null) {
							if (strReturnStatus.contains("Error: ")) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + strReturnStatus;
								}
							} else {
								this.objComplaintBean.setOperatingSystemVersion(strReturnStatus);
							}
						}
					}
				}

				String strFinalPAE = this.objComplaintBean.getIsPotentialAdverseEvent();
				if (strFinalPAE != null) {
					if (strFinalPAE.equalsIgnoreCase("Yes") || strFinalPAE.equalsIgnoreCase("Y")) {
						strReturnStatus = validateWasProcedureTheraDia(mapComplaintData.get("86"), strPhaseName);
						if (strReturnStatus != null) {
							if (strReturnStatus.contains("Error: ")) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || " + strReturnStatus;
								}
							} else {
								this.objComplaintBean.setWasTheProcedureTherapeuticOrDiagnostic(strReturnStatus);
							}
						}
					}
					// Commented for ETQCR-931
					/*
					 * strReturnStatus = validatePatientInvolCode(mapComplaintData.get("92")); if
					 * (strReturnStatus != null) { if(strReturnStatus.contains("Error: ")) { if
					 * ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <=
					 * 3000) { strErrorMsg = strErrorMsg +" || "+ strReturnStatus; } } else {
					 * //this.objComplaintBean.setPatientInvolvementCode(strReturnStatus); } }
					 */
				}

				strReturnStatus = validateWarrantyReviewRequitred(mapComplaintData.get("53"));
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setIsWarrantyRequired(strReturnStatus);
					}
				} else {
					this.objComplaintBean.setIsWarrantyRequired("No");
				}

				strReturnStatus = validateAssignedTo(mapComplaintData.get("87"), strEtQAuthor);
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("does not exist")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || Error: Assigned to User or Group " + strReturnStatus;
						}
					} else {
						this.objComplaintBean.setAssignedTo(strReturnStatus);
					}
				}

				strReturnStatus = validateAllAttachments();
				if (strReturnStatus != null) {
					if (strReturnStatus.contains("Error: ")) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg + " || " + strReturnStatus;
						}
					}
				}
			} catch (Exception ex) {
				logger.error("Error: ", ex);
				this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
			}
		}
		if (strErrorMsg != null && strErrorMsg.length() > 0) {
			if (this.objComplaintBean.getSystemSourceRefNo() != null
					&& this.objComplaintBean.getSystemSourceRefNo().length() > 0) {
				this.objComplaintBean.setErrorMessage(strErrorMsg.substring(4) + "for System Source Ref No: "
						+ this.objComplaintBean.getSystemSourceRefNo());
			} else {
				this.objComplaintBean.setErrorMessage(strErrorMsg.substring(4));
			}
		}

		return this.objComplaintBean;
	}

	// Set the Connection with Webservice
	public StringBuffer getConnection(URL url) {
		StringBuffer response = new StringBuffer();
		HttpURLConnection connection = null;
		try {
			connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			String encoded = Base64.getEncoder()
					.encodeToString((this.objComplaintBean.getServerCred()).getBytes(StandardCharsets.UTF_8));
			connection.setRequestProperty("Authorization", "Basic " + encoded);
			connection.setReadTimeout(180000);
			int responseCode = connection.getResponseCode();
			// system.out.println("");
			if (responseCode == HttpURLConnection.HTTP_OK) // success
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String inputLine;

				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}

				in.close();
			} else if (responseCode == HttpURLConnection.HTTP_BAD_GATEWAY) // Bad Getway
			{
				response = new StringBuffer("Error: HTTP Status-Code 502: Bad Gateway ");
			} else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) // Bad Request
			{
				response = new StringBuffer("400");
			} else if (responseCode == HttpURLConnection.HTTP_CLIENT_TIMEOUT) // Request Time-Out
			{
				response = new StringBuffer("Error: HTTP Status-Code 408: Request Time-Out ");
			} else if (responseCode == HttpURLConnection.HTTP_ENTITY_TOO_LARGE) // Request Entity Too Large
			{
				response = new StringBuffer("Error: HTTP Status-Code 413: Request Entity Too Large ");
			} else if (responseCode == HttpURLConnection.HTTP_ENTITY_TOO_LARGE) // Request Entity Too Large
			{
				response = new StringBuffer("Error: HTTP Status-Code 413: Request Entity Too Large ");
			} else if (responseCode == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) // Gateway Timeout
			{
				response = new StringBuffer("Error: HTTP Status-Code 504: Gateway Timeout ");
			} else if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) // Internal Server Error
			{
				response = new StringBuffer("Error: HTTP Status-Code 500: Internal Server Error ");
			} else if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) // No Content
			{
				response = new StringBuffer("Error: HTTP Status-Code 204: No Content ");
			} else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) // Not Found
			{
				response = new StringBuffer("Error: HTTP Status-Code 404: Not Found ");
			} else if (responseCode == HttpURLConnection.HTTP_REQ_TOO_LONG) // Request-URI Too Large
			{
				response = new StringBuffer("Error: HTTP Status-Code 414: Request-URI Too Large ");
			} else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) // Unauthorized
			{
				response = new StringBuffer("Error: HTTP Status-Code 401: Unauthorized ");
			} else if (responseCode == HttpURLConnection.HTTP_UNAVAILABLE) // Service Unavailable
			{
				response = new StringBuffer("Error: HTTP Status-Code 503: Service Unavailable ");
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return response;
	}

	// Validate System Source
	public String validateSystemSource(String strSystemSource) {
		String strStatus = null;
		URL url = null;
		try {
			if (strSystemSource != null) {
				strSystemSource = strSystemSource.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/LOOKUPS/SYSTEM_SOURCE/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,DESCRIPTION&values=0,"
						+ strSystemSource);

				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int intCntRec = -1;

					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strStatus = "- Display value(s): [" + strSystemSource.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strStatus = response.toString() + "in System Source ";
						} else {
							jsonObj = new JSONObject(response.toString());
							intCntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (intCntRec > 0) {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strStatus = jsonColObj.get("value").toString();
							} else {
								strStatus = "- Display value(s): [" + strSystemSource.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}
		return strStatus;
	}

	// Validate System Reference Number and System Source
	public String validateSysRefAndSysSource(String strSysRefNum, String strSysSource) {
		String strStatus = null;
		String strErrorMsg = null;
		String strSystemSource = null;

		URL url = null;

		if (strSysRefNum != null && strSysRefNum.length() == 0) {
			strSysRefNum = null;
		}

		if (strSysSource != null && strSysSource.length() == 0) {
			strSysSource = null;
		}

		if (strSysRefNum != null && strSysSource != null) {
			try {
				if (strSysSource != null) {
					strSystemSource = validateSystemSource(strSysSource);
					if (strSystemSource != null && strSystemSource.length() > 0
							&& (!strSystemSource.contains("value(s)") && !strSystemSource.contains("Error: "))) {
						url = new URL(this.objComplaintBean.getEtQURL()
								+ "/datasources/WS_SYS_REF_NUM_SYS_SOURCE_DS_P/execute?VAR$SYSREF_NO="
								+ URLEncoder.encode(strSysRefNum, "UTF-8") + "&VAR$SYS_SOURCE="
								+ URLEncoder.encode(strSystemSource, "UTF-8"));

						if (url != null) {
							int intSysSourCnt = -1;
							StringBuffer strSysResponse = getConnection(url);
							JSONObject jsonSysObj = null;
							int intCntSysRec = -1;

							if (strSysResponse != null && strSysResponse.length() > 0) {
								if (strSysResponse.toString().contains("400")) {
									strStatus = "Error: System Source Reference Number : ["
											+ strSysRefNum.replaceAll("%20", " ") + "] is invalid  ";
								} else if (strSysResponse.toString().contains("Error: ")) {
									strStatus = strSysResponse.toString()
											+ "in System Source Reference Number and System Source ";
								} else {
									jsonSysObj = new JSONObject(strSysResponse.toString());
									intCntSysRec = Integer.parseInt(jsonSysObj.get("count").toString());

									if (intCntSysRec > 0) {
										String strSysRecRec = jsonSysObj.get("Records").toString();
										JSONObject jsonSysRecObj = new JSONObject(
												strSysRecRec.substring(1, strSysRecRec.length() - 1));

										String strSysCol = jsonSysRecObj.get("Columns").toString();
										JSONObject jsonsSysColObj = new JSONObject(
												strSysCol.substring(1, strSysCol.length() - 1));

										intSysSourCnt = Integer.parseInt(jsonsSysColObj.get("value").toString());

										if (intSysSourCnt > 0) {
											strErrorMsg = "Error: Complaint for System Reference Number : ["
													+ strSysRefNum.replaceAll("%20", " ") + "] and System Source : ["
													+ strSysSource.replaceAll("%20", " ") + "] is already created ";
										}
									}
								}
							}
						}
					} else {
						if (strSystemSource.contains("Error: ")) {
							strErrorMsg = strSystemSource;
						} else {
							strErrorMsg = "Error: System Source " + strSystemSource.replace("%20", " ");
						}
					}
				}
			} catch (Exception ex) {
				logger.error("Error: ", ex);
				this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
			}
		}

		if (strSystemSource != null && strErrorMsg == null && !strSystemSource.contains("Error: ")) {
			strStatus = strSystemSource.replace("%20", " ");
		} else {
			strStatus = strErrorMsg;
		}
		return strStatus;
	}

	// Validate Initiator Location
	public String validateInitatorLocation(String strInitLocation, String strInitUser) {
		String strStatus = null;
		String strErrorMsg = null;
		String strInitiatorLoc = null;
		URL url = null;

		try {
			if (strInitLocation != null && strInitLocation.length() == 0) {
				strInitLocation = null;
			}

			StringBuffer response = null;
			JSONObject jsonObj = null;
			int cntRec = -1;

			if (strInitLocation != null) {
				strInitLocation = strInitLocation.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/DATACENTER/LOCATION_PROFILE/where?columns=DISPLAY_NAME&keys=DISABLED,DISPLAY_NAME&values=0,"
						+ strInitLocation);

				if (url != null) {
					response = getConnection(url);
					jsonObj = null;
					cntRec = -1;

					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strErrorMsg = "Error: Initiator Location - Display value(s): ["
									+ strInitLocation.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strErrorMsg = response.toString() + "in Initiator Location ";
						} else {
							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (cntRec == 0) {
								strErrorMsg = "Error: Initiator Location - Display value(s): ["
										+ strInitLocation.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strInitiatorLoc = jsonColObj.get("value").toString();
							}
						}
					}
				}
			} else {
				strInitUser = strInitUser.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/datasources/WS_USER_PRIMARY_LOC_DS_P/execute?VAR$USER_NAME=" + strInitUser);

				if (url != null) {
					response = getConnection(url);
					jsonObj = null;
					cntRec = -1;

					if (response != null && response.length() > 0) {
						jsonObj = new JSONObject(response.toString());
						cntRec = Integer.parseInt(jsonObj.get("count").toString());

						if (cntRec > 0) {
							String strRec = jsonObj.get("Records").toString();
							JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

							JSONObject jsonColObj = new JSONObject(jsonRecObj.toString());
							String strCol = jsonColObj.get("Columns").toString();

							JSONObject jsonValObj = new JSONObject(strCol.substring(1, strCol.length() - 1));
							String strVal = jsonValObj.get("value").toString();
							if (strVal != null) {
								strInitiatorLoc = strVal;
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strInitiatorLoc != null && strErrorMsg == null) {
			strStatus = strInitiatorLoc;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Validate Initiator Branch
	public String validateInitatorBranch(String strInitBranch) {
		String strStatus = null;
		String strErrorMsg = null;
		String strInitiatorBranch = null;
		URL url = null;

		if (strInitBranch != null && strInitBranch.length() == 0) {
			strInitBranch = null;
		}
		try {
			if (strInitBranch != null) {
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/LOOKUPS/INITIATOR_BRANCH/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,DESCRIPTION&values=0,"
						+ URLEncoder.encode(strInitBranch, "UTF-8"));

				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						jsonObj = new JSONObject(response.toString());
						cntRec = Integer.parseInt(jsonObj.get("count").toString());

						if (cntRec == 0) {
							strErrorMsg = "Error: Initiator Branch - Display value(s): ["
									+ URLDecoder.decode(strInitBranch, "UTF-8")
									+ "] is not listed in the field's keyword options ";
						} else {
							String strRec = jsonObj.get("Records").toString();
							JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

							JSONObject jsonColObj = new JSONObject(jsonRecObj.toString());
							String strCol = jsonColObj.get("Columns").toString();

							JSONObject jsonValObj = new JSONObject(strCol.substring(1, strCol.length() - 1));
							String strVal = jsonValObj.get("value").toString();

							strInitiatorBranch = strVal;
						}
					}
				}
			}
			// Added for MDMTESTING-4503
			else {
				this.objComplaintBean.setInitiatorBranch("");
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strInitiatorBranch != null && strErrorMsg == null) {
			strStatus = strInitiatorBranch;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Validate Event Found At
	public String validateEventFoundAt(String strEventFoundAt) {
		String strStatus = null;
		String strErrorMsg = null;
		String strEvent = null;
		URL url = null;

		if (strEventFoundAt != null && strEventFoundAt.length() == 0) {
			strEventFoundAt = null;
		}
		try {
			if (strEventFoundAt != null) {
				strEventFoundAt = strEventFoundAt.replace(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/LOOKUPS/EVENT_FOUND_AT/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,DESCRIPTION&values=0,"
						+ strEventFoundAt);
				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;
					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strErrorMsg = "Error: Event Found At - Display value(s): ["
									+ strEventFoundAt.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strErrorMsg = response.toString() + "in Event Found At ";
						} else {
							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (cntRec == 0) {
								strErrorMsg = "Error: Event Found At - Display value(s): ["
										+ strEventFoundAt.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strEvent = jsonColObj.get("value").toString();
							}
						}
					}
				}
			}
			// Added for MDMTESTING-4503
			else {
				this.objComplaintBean.setEventFoundAt("");
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strEvent != null && strErrorMsg == null) {
			strStatus = strEvent;
		} else {
			strStatus = strErrorMsg;
		}
		return strStatus;
	}

	// Validate PAE Questions
	public String validatePAEQuestion(String strPAEQes1, String strPAEQes2, String strCompPhase) {
		String strStatus = null;

		if (strPAEQes1 != null) {
			if (strPAEQes1.equalsIgnoreCase("Yes")) {
				this.objComplaintBean.setIsPotentialAdverseEvent("Yes");
				this.objComplaintBean.setPaeQuestionAns1("Yes");
			} else if (strPAEQes1.equalsIgnoreCase("No") || strPAEQes1.equalsIgnoreCase("Unknown")) {
				this.objComplaintBean.setPaeQuestionAns1(strPAEQes1);
				if (strPAEQes2 != null && strPAEQes2.length() > 0) {
					if (strPAEQes2.equalsIgnoreCase("Yes")) {
						this.objComplaintBean.setIsPotentialAdverseEvent("Yes");
					} else if (strPAEQes2.equalsIgnoreCase("Unknown")) {
						this.objComplaintBean.setIsPotentialAdverseEvent("Unknown");
					} else if (strPAEQes2.equalsIgnoreCase("No")) {
						this.objComplaintBean.setIsPotentialAdverseEvent("No");
					}
				} else {
					this.objComplaintBean.setIsPotentialAdverseEvent("No");
				}
			}
		}

		if (strPAEQes1 != null && !strPAEQes1.equalsIgnoreCase("Yes") && !strPAEQes1.equalsIgnoreCase("No")
				&& !strPAEQes1.equalsIgnoreCase("Unknown")) {
			strStatus = "Error: PAE Answer 1 - Display value(s): [" + strPAEQes1
					+ "] is not listed in the field's keyword options ";
		} else if (strPAEQes1 != null && !strPAEQes1.equalsIgnoreCase("Yes")) {
			if (strPAEQes2 != null && !strPAEQes2.equalsIgnoreCase("Yes") && !strPAEQes2.equalsIgnoreCase("No")
					&& !strPAEQes2.equalsIgnoreCase("Unknown")) {
				strStatus = "Error: PAE Answer 2 - Display value(s): [" + strPAEQes2
						+ "] is not listed in the field's keyword options ";
			}
		}

		if ((this.objComplaintBean.getIsPotentialAdverseEvent() != null
				&& (this.objComplaintBean.getIsPotentialAdverseEvent().equalsIgnoreCase("Yes")
						|| this.objComplaintBean.getIsPotentialAdverseEvent().equalsIgnoreCase("Unknown")))
				&& strCompPhase.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED")) {
			strStatus = "Error: Closed complaint should be interfaced as PAE = No ";
		}

		return strStatus;
	}

	// Validate Is This A Complaint Flag
	public String validateIsThisAComplaint(String strIsThisComp) {
		String strStatus = null;
		if (strIsThisComp != null && strIsThisComp.length() > 0) {
			strIsThisComp = getYesNoValue(strIsThisComp);

			if (strIsThisComp != null && !strIsThisComp.equalsIgnoreCase("Yes")
					&& strIsThisComp.equalsIgnoreCase("No")) {
				strStatus = "Error: \"Is This a Complaint?\" value must be \"Yes\" ";
			} else if (strIsThisComp != null && !strIsThisComp.equalsIgnoreCase("Yes")
					&& !strIsThisComp.equalsIgnoreCase("No")) {
				strStatus = "Error: Is This a Complaint? - Display value(s): [" + strIsThisComp
						+ "] is not listed in the field's keyword options ";
			} else {
				strStatus = strIsThisComp;
			}
		} else {
			strStatus = "Yes";
		}
		return strStatus;
	}

	// Validate Any Actions already taken at the Customer Site
	public String validateAnyActionsTakenAtCusSite(String strActionTakenCusSite) {
		String strStatus = null;
		String strErrorMsg = null;
		String strActTakenCusSite = null;
		URL url = null;

		if (strActionTakenCusSite != null && strActionTakenCusSite.length() == 0) {
			strActionTakenCusSite = null;
		}
		try {
			if (strActionTakenCusSite != null) {
				strActionTakenCusSite = strActionTakenCusSite.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/LOOKUPS/ACT_AT_CUST_SITE/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,DESCRIPTION&values=0,"
						+ strActionTakenCusSite);

				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strErrorMsg = "Error: Any Actions already taken at the Customer Site - Display value(s): ["
									+ strActionTakenCusSite.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strErrorMsg = response.toString() + "in Any Actions already taken at the Customer Site ";
						} else {
							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (cntRec == 0) {
								strErrorMsg = "Error: Any Actions already taken at the Customer Site - Display value(s): ["
										+ strActionTakenCusSite.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strActTakenCusSite = jsonColObj.get("value").toString();
							}
						}
					}
				}
			}
			// Added for MDMTESTING-4503
			else {
				this.objComplaintBean.setAnyActionsAlreadyTakenAtTheCustomerSite("");
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strActTakenCusSite != null && strErrorMsg == null) {
			strStatus = strActTakenCusSite;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Commented for ETQCR-912
	// Validate Telephone Number
	/*
	 * public String validateTelephoneFormat(String strphnNum) { String
	 * strphnNumber=null; String strErrorMsg = null; String strStatus = null;
	 * 
	 * try { strphnNum=strphnNum.replaceAll("[^\\d.]", ""); int intLen =
	 * strphnNum.length(); String fchar=strphnNum.substring(0,1);
	 * 
	 * if (fchar.equals("1")) { if (intLen <= 10) { strErrorMsg =
	 * " number requires min 10 numbers "; } else { strphnNumber = "+" + fchar + "("
	 * + strphnNum.substring(1, 4) + ")" + strphnNum.substring(4, 7) + "-"; if
	 * (intLen > 11) { strphnNumber = strphnNumber + strphnNum.substring(7, 11) +
	 * "x" + strphnNum.substring(11, intLen); } else { strphnNumber = strphnNumber +
	 * strphnNum.substring(7, 11); } } } else { if (intLen < 10) { strErrorMsg =
	 * " number requires min 10 numbers "; } else { strphnNumber = "+1(" +
	 * strphnNum.substring(0, 3) + ")" + strphnNum.substring(3, 6) + "-"; if (intLen
	 * > 10) { strphnNumber = strphnNumber + strphnNum.substring(6, 10) + "x" +
	 * strphnNum.substring(10, intLen); } else { strphnNumber = strphnNumber +
	 * strphnNum.substring(6, intLen); } } } } catch(Exception er) { return
	 * "Error : "+er.getMessage(); }
	 * 
	 * if (strphnNumber != null && strErrorMsg == null) { strStatus = strphnNumber;
	 * } else { strStatus = strErrorMsg; }
	 * 
	 * return strStatus; }
	 */

	// Validate Customer Response Requested?
	public String validateCusRespReq(String strCusRespReq) {
		String strStatus = null;

		if (strCusRespReq != null && strCusRespReq.length() > 0) {
			strCusRespReq = getYesNoValue(strCusRespReq);

			if (strCusRespReq != null && !strCusRespReq.equalsIgnoreCase("Yes")
					&& !strCusRespReq.equalsIgnoreCase("No")) {
				strStatus = "Error: Customer Response Requested? - Display value(s): [" + strCusRespReq
						+ "] is not listed in the field's keyword options ";
			} else {
				strStatus = strCusRespReq;
			}
		} else {
			strStatus = "No";
		}
		return strStatus;
	}

	// Validate Customer Location
	public String validateCustomerLocation(String strCusLocation) {
		String strStatus = null;
		String strErrorMsg = null;
		String strCusLoc = null;
		URL url = null;

		if (strCusLocation != null && strCusLocation.length() == 0) {
			strCusLocation = null;
		}
		try {
			if (strCusLocation != null) {
				strCusLocation = strCusLocation.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/DATACENTER/LOCATION_PROFILE/where?columns=DISPLAY_NAME&keys=DISABLED,DISPLAY_NAME&values=0,"
						+ strCusLocation);

				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strErrorMsg = "Error: Customer Location - Display value(s): ["
									+ strCusLocation.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strErrorMsg = response.toString() + "in Customer Location ";
						} else {
							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (cntRec == 0) {
								strErrorMsg = "Error: Customer Location - Display value(s): ["
										+ strCusLocation.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strCusLoc = jsonColObj.get("value").toString();
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strCusLoc != null && strErrorMsg == null) {
			strStatus = strCusLoc;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Validate Health Professional
	public String validateHealthProfessional(String strHealthProfessional) {
		String strStatus = null;
		String strHealthPro = null;

		if (strHealthProfessional != null && strHealthProfessional.length() > 0) {
			strHealthPro = getYesNoNoInformationValue(strHealthProfessional);

			if (strHealthPro != null && !strHealthPro.equalsIgnoreCase("Yes") && !strHealthPro.equalsIgnoreCase("No")
					&& !strHealthPro.equalsIgnoreCase("No Information")) {
				strStatus = "Error: Health Professional - Display value(s): [" + strHealthPro
						+ "] is not listed in the field's keyword options ";
			} else {
				strStatus = strHealthPro;
			}
		}
		// Commented for ETQCR-931
		/*
		 * else { strStatus = "No Information"; }
		 */

		return strStatus;
	}

	// Validate Occupation
	public String validateOccupation(String strOccupation) {
		String strStatus = null;
		String strErrorMsg = null;
		String strOccup = null;
		URL url = null;

		if (strOccupation != null && strOccupation.length() == 0) {
			strOccupation = null;
		}
		try {
			if (strOccupation != null) {
				strOccupation = strOccupation.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/datasources/WS_OCCUPATION_DS_P/execute?VAR$OCCUPATION=" + strOccupation);

				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strErrorMsg = "Error: Occupation - Display value(s): ["
									+ strOccupation.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strErrorMsg = response.toString() + "in Occupation ";
						} else {
							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (cntRec == 0) {
								strErrorMsg = "Error: Occupation - Display value(s): ["
										+ strOccupation.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strOccup = jsonColObj.get("value").toString();
							}
						}
					}
				}
			}
			// Added for MDMTESTING-4503
			else {
				this.objComplaintBean.setOccupation("");
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strOccup != null && strErrorMsg == null) {
			strStatus = strOccup;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Validate Customer Country
	public String validateCustomerCountry(String strCusCountry) {
		String strStatus = null;
		String strErrorMsg = null;
		String strCountry = null;
		URL url = null;

		if (strCusCountry != null && strCusCountry.length() == 0) {
			strCusCountry = null;
		}

		if (strCusCountry != null) {
			try {
				if (strCusCountry.length() > 0) {
					strCusCountry = strCusCountry.replaceAll(" ", "%20");
					url = new URL(this.objComplaintBean.getEtQURL()
							+ "/dao/LOOKUPS/COUNTRY/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,DESCRIPTION&values=0,"
							+ strCusCountry);

					if (url != null) {
						StringBuffer response = getConnection(url);
						JSONObject jsonObj = null;
						int cntRec = -1;

						if (response != null && response.length() > 0) {
							if (response.toString().contains("400")) {
								strErrorMsg = "Error: Customer Country - Display value(s): ["
										+ strCusCountry.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else if (response.toString().contains("Error: ")) {
								strErrorMsg = response.toString() + "in Customer Country ";
							} else {
								jsonObj = new JSONObject(response.toString());
								cntRec = Integer.parseInt(jsonObj.get("count").toString());

								if (cntRec == 0) {
									strErrorMsg = "Error: Customer Country - Display value(s): ["
											+ strCusCountry.replaceAll("%20", " ")
											+ "] is not listed in the field's keyword options ";
								} else {
									String strRec = jsonObj.get("Records").toString();
									JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

									String strCol = jsonRecObj.get("Columns").toString();
									JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

									strCountry = jsonColObj.get("value").toString();
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				logger.error("Error: ", ex);
				this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
			}
		}

		if (strCountry != null && strErrorMsg == null) {
			strStatus = strCountry;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Validate Customer Sate
	public String validateCustomerState(String strCusState) {
		String strStatus = null;
		String strErrorMsg = null;
		String strState = null;
		URL url = null;

		if (strCusState != null && strCusState.length() == 0) {
			strCusState = null;
		}

		if (strCusState == null) {
			strErrorMsg = "Error: Customer State is required field ";
		} else {
			try {
				strCusState = strCusState.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/LOOKUPS/STATES_LIST/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,DESCRIPTION&values=0,"
						+ strCusState);

				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strErrorMsg = "Error: Customer State - Display value(s): ["
									+ strCusState.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strErrorMsg = response.toString() + "in Customer State ";
						} else {
							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (cntRec == 0) {
								strErrorMsg = "Error: Customer State - Display value(s): ["
										+ strCusState.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strState = jsonColObj.get("value").toString();
							}
						}
					}
				}
			} catch (Exception ex) {
				logger.error("Error: ", ex);
				this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
			}
		}
		if (strState != null && strErrorMsg == null) {
			strStatus = strState;
		} else {
			strStatus = strErrorMsg;
		}
		return strStatus;
	}

	// Check Model Number and Item Code
	// Modified for ETQCR-947
	// Modified for ETQTESTING-80068
	public String validateModelNumber(String strModelNumber, String strItemCode) {
		String strMessage = new String();
		String strStatus = null;
		String strModelNum = strModelNumber;
		String strItmCode = strItemCode;
		URL url = null;
		try {
			if (strModelNumber == null && strItemCode == null) {
				strMessage = "Error: Record must contain either Model Number or Item Code ";
			} else {
				if (strModelNumber != null && strItemCode != null) {
					strModelNumber = strModelNumber.replaceAll("'", "''");
					strItemCode = strItemCode.replaceAll("'", "''");
					/*
					 * strModelNumber = strModelNumber.replaceAll(" ", "%20"); strModelNumber =
					 * strModelNumber.replaceAll("&", "%26"); strItemCode =
					 * strItemCode.replaceAll(" ", "%20");
					 */
					url = new URL(this.objComplaintBean.getEtQURL()
							+ "/datasources/WS_MODEL_NUMBER_DS_P/execute?VAR$ITEM_CODE="
							+ URLEncoder.encode(strItemCode, "UTF-8") + "&VAR$MODEL_NUM="
							+ URLEncoder.encode(strModelNumber, "UTF-8"));
					strMessage = "Error: Either Model Number:[" + strModelNum + "] and Item Code:[" + strItmCode
							+ "] combination is not valid or is inactive in EtQ ";
				} else if (strModelNumber != null && strItemCode == null) {
					strModelNumber = strModelNumber.replaceAll("'", "''");
//					strModelNumber = strModelNumber.replaceAll("&", "%26");
					url = new URL(
							this.objComplaintBean.getEtQURL() + "/datasources/WS_MODEL_OR_ITEM_DS_P/execute?VAR$ITEM="
									+ URLEncoder.encode(strModelNumber, "UTF-8"));
					strMessage = "Error: Either Model Number:[" + strModelNum
							+ "] is not available or is inactive in EtQ ";
				} else if (strModelNumber == null && strItemCode != null) {
					strItemCode = strItemCode.replaceAll("'", "''");
					url = new URL(
							this.objComplaintBean.getEtQURL() + "/datasources/WS_MODEL_OR_ITEM_DS_P/execute?VAR$ITEM="
									+ URLEncoder.encode(strItemCode, "UTF-8"));
					strMessage = "Error: Either No Model Number available for Item Code:[" + strItmCode
							+ "] or is inactive in EtQ ";
				}

				if (url != null) {
					StringBuffer response = getConnection(url);

					if (response != null && response.length() > 0) {
						JSONObject jsonObj = new JSONObject(response.toString());
						int cntRec = Integer.parseInt(jsonObj.get("count").toString());

						Map<String, String> jsonValMap = new HashMap<String, String>();

						if (cntRec > 0) {
							String strRec = jsonObj.get("Records").toString();
							JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));
							JSONObject jsonColObj = new JSONObject(jsonRecObj.toString());
							JSONArray jsonColAry = (JSONArray) jsonColObj.get("Columns");

							for (int i = 0; i < jsonColAry.length(); i++) {
								JSONObject jsonValObj = (JSONObject) jsonColAry.get(i);
								String strColNM = jsonValObj.get("name").toString();
								String strColVal = jsonValObj.get("value").toString();
								jsonValMap.put(strColNM, strColVal);
							}
							// Modified for ETQCR-760 //Commented for ETQCR-947
							/*
							 * if (jsonValMap.get("IS_INACTIVE_MATERIAL").toString().equals("1")) {
							 * strMessage = "Error: Model Number: [" +
							 * jsonValMap.get("PRODUCT_ITEM_CODE").toString().split(":")[1]+
							 * "] is inactive for Item Code:[" + jsonValMap.get("ETQ$NUMBER")+
							 * "] in EtQ MDM "; } else {
							 */
							// Modified for ETQCR-760
							strStatus = jsonValMap.get("PRODUCT_ITEM_CODE").toString().split(":")[0];
							this.objComplaintBean
									.setModelNumber(jsonValMap.get("PRODUCT_ITEM_CODE").toString().split(":")[0]);
							this.objComplaintBean
									.setItemType(jsonValMap.get("PRODUCT_ITEM_CODE").toString().split(":")[2]);
							this.objComplaintBean.setIsProductSoftware(jsonValMap.get("IS_SOFTWARE"));
							this.objComplaintBean.setIsProductSerial(jsonValMap.get("PRODUCT_SERIAL"));
							// }
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strStatus != null) {
			strStatus = null;
		} else {
			strStatus = strMessage;
		}
		return strStatus;
	}

	// Validate CDS Method Before Returning Device to Olympus
	public String validateCDSMethodBeforeRetOly(String strCDSMethod) {
		String strStatus = null;
		String strErrorMsg = null;
		String strCDS = null;
		URL url = null;

		if (strCDSMethod != null && strCDSMethod.length() == 0) {
			strCDSMethod = null;
		}
		try {
			if (strCDSMethod != null) {
				strCDSMethod = strCDSMethod.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/LOOKUPS/CH_CDS_B4_RET_DEV_OLY/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,DESCRIPTION&values=0,"
						+ strCDSMethod);

				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strErrorMsg = "Error: CDS Method Before Returning Device to Olympus - Display value(s): ["
									+ strCDSMethod.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strErrorMsg = response.toString() + "in CDS Method Before Returning Device to Olympus ";
						} else {
							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (cntRec == 0) {
								strErrorMsg = "Error: CDS Method Before Returning Device to Olympus - Display value(s): ["
										+ strCDSMethod.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strCDS = jsonColObj.get("value").toString();
							}
						}
					}
				}
			}
			// Added for MDMTESTING-4503
			else {
				this.objComplaintBean.setCdsMethodBeforeRetOly("");
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strCDS != null && strErrorMsg == null) {
			strStatus = strCDS;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Will Product be returned to Olympus?
	public String valdiateWillProductRetnOly(String strProductToBeRetnOlym) {
		String strStatus = null;

		if (strProductToBeRetnOlym != null && strProductToBeRetnOlym.length() > 0) {
			strProductToBeRetnOlym = getDTBooleanValue(strProductToBeRetnOlym);

			if (!strProductToBeRetnOlym.equalsIgnoreCase("Yes") && !strProductToBeRetnOlym.equalsIgnoreCase("No")
					&& !strProductToBeRetnOlym.equalsIgnoreCase("Unknown")) {
				strStatus = "Error: Will Product be returned to Olympus? - Display value(s): [" + strProductToBeRetnOlym
						+ "] is not listed in the field's keyword options ";
			}
		}

		if (strStatus == null || strStatus.length() == 0) {
			strStatus = strProductToBeRetnOlym;
		}
		return strStatus;
	}

	// Validate Order Type
	public String validateOrderType(String strOrderType) {
		String strStatus = null;
		String strErrorMsg = null;
		String strOrdTyp = null;
		URL url = null;

		if (strOrderType != null && strOrderType.length() == 0) {
			strOrderType = null;
		}
		try {
			if (strOrderType != null) {
				strOrderType = strOrderType.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/LOOKUPS/CH_ORDER_TYPE/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,DESCRIPTION&values=0,"
						+ strOrderType);

				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strErrorMsg = "Error: Order Type - Display value(s): ["
									+ strOrderType.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strErrorMsg = response.toString() + "in Order Type ";
						} else {
							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (cntRec == 0) {
								strErrorMsg = "Error: Order Type - Display value(s): ["
										+ strOrderType.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strOrdTyp = jsonColObj.get("value").toString();
							}
						}
					}
				}
			}
			// Added for MDMTESTING-4503
			else {
				this.objComplaintBean.setOrderType("");
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strOrdTyp != null && strErrorMsg == null) {
			strStatus = strOrdTyp;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Validate Software Version
	public String validateSoftwareVersion(String strSoftwareVersion) {
		String strStatus = null;
		String strErrorMsg = null;
		String strSoftVer = null;
		URL url = null;

		if (strSoftwareVersion != null && strSoftwareVersion.length() == 0) {
			strSoftwareVersion = null;
		}
		try {
			if (strSoftwareVersion != null) {
				strSoftwareVersion = strSoftwareVersion.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/LOOKUPS/CH_SOFTWRE_VERSION/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,DESCRIPTION&values=0,"
						+ strSoftwareVersion);

				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strErrorMsg = "Error: Software Version - Display value(s): ["
									+ strSoftwareVersion.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strErrorMsg = response.toString() + "in Software Version ";
						} else {
							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (cntRec == 0) {
								strErrorMsg = "Error: Software Version - Display value(s): ["
										+ strSoftwareVersion.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strSoftVer = jsonColObj.get("value").toString();
							}
						}
					}
				}
			}
			// Added for MDMTESTING-4503
			else {
				this.objComplaintBean.setSoftwareVersion("");
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strSoftVer != null && strErrorMsg == null) {
			strStatus = strSoftVer;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Validate Operating System & Version
	public String validateOperatingSysVersion(String strOperatingSysVersion) {
		String strStatus = null;
		String strErrorMsg = null;
		String strOpeSysVer = null;
		URL url = null;

		if (strOperatingSysVersion != null && strOperatingSysVersion.length() == 0) {
			strOperatingSysVersion = null;
		}
		try {
			if (strOperatingSysVersion != null) {
				strOperatingSysVersion = strOperatingSysVersion.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/LOOKUPS/CH_OPERATING_SYS_VER/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,DESCRIPTION&values=0,"
						+ strOperatingSysVersion);

				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strErrorMsg = "Error: Operating System & Version - Display value(s): ["
									+ strOperatingSysVersion.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strErrorMsg = response.toString() + "in Operating System & Version ";
						} else {
							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (cntRec == 0) {
								strErrorMsg = "Error: Operating System & Version - Display value(s): ["
										+ strOperatingSysVersion.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strOpeSysVer = jsonColObj.get("value").toString();
							}
						}
					}
				}
			}
			// Added for MDMTESTING-4503
			else {
				this.objComplaintBean.setOperatingSystemVersion("");
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strOpeSysVer != null && strErrorMsg == null) {
			strStatus = strOpeSysVer;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Validate Was the procedure therapeutic or diagnostic?
	public String validateWasProcedureTheraDia(String strWasProThereDia, String strPhaseName) {
		String strStatus = null;
		String strErrorMsg = null;
		String strWasProTheDia = null;
		URL url = null;

		if (strWasProThereDia != null && strWasProThereDia.length() == 0) {
			strWasProThereDia = null;
		}
		try {
			if (strWasProThereDia == null && (!strPhaseName.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_DRAFT")
					&& !strPhaseName.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED"))) {
				strErrorMsg = "Error: Was the procedure therapeutic or diagnostic? is required field ";
				// Added for MDMTESTING-4503
				this.objComplaintBean.setWasTheProcedureTherapeuticOrDiagnostic("");
			} else if (strWasProThereDia != null
					&& !strPhaseName.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED")) {
				strWasProThereDia = strWasProThereDia.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/LOOKUPS/PROCEDURE_THERAPEUTIC/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,DESCRIPTION&values=0,"
						+ strWasProThereDia);

				if (url != null) {

					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strErrorMsg = "Error: Was the procedure therapeutic or diagnostic? - Display value(s): ["
									+ strWasProThereDia.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strErrorMsg = response.toString() + "in Was the procedure therapeutic or diagnostic? ";
						} else {
							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (cntRec == 0) {
								strErrorMsg = "Error: Was the procedure therapeutic or diagnostic? - Display value(s): ["
										+ strWasProThereDia.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strWasProTheDia = jsonColObj.get("value").toString();
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strWasProTheDia != null && strErrorMsg == null) {
			strStatus = strWasProTheDia;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Validate Was the procedure therapeutic or diagnostic?
	public String validateWasProcedureTheraDiaForCodes(String strWasProThereDia) {
		String strStatus = null;
		String strErrorMsg = null;
		String strWasProTheDia = null;
		URL url = null;

		if (strWasProThereDia != null && strWasProThereDia.length() == 0) {
			strWasProThereDia = null;
		}
		try {
			if (strWasProThereDia != null) {
				strWasProThereDia = strWasProThereDia.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/LOOKUPS/PROCEDURE_THERAPEUTIC/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,DESCRIPTION&values=0,"
						+ strWasProThereDia);

				if (url != null) {

					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						if (response.toString().contains("400")) {
							strErrorMsg = "Error: Was the procedure therapeutic or diagnostic? - Display value(s): ["
									+ strWasProThereDia.replaceAll("%20", " ")
									+ "] is not listed in the field's keyword options ";
						} else if (response.toString().contains("Error: ")) {
							strErrorMsg = response.toString() + "in Was the procedure therapeutic or diagnostic? ";
						} else {
							jsonObj = new JSONObject(response.toString());
							cntRec = Integer.parseInt(jsonObj.get("count").toString());

							if (cntRec == 0) {
								strErrorMsg = "Error: Was the procedure therapeutic or diagnostic? - Display value(s): ["
										+ strWasProThereDia.replaceAll("%20", " ")
										+ "] is not listed in the field's keyword options ";
							} else {
								String strRec = jsonObj.get("Records").toString();
								JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

								String strCol = jsonRecObj.get("Columns").toString();
								JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

								strWasProTheDia = jsonColObj.get("value").toString();
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strWasProTheDia != null && strErrorMsg == null) {
			strStatus = strWasProTheDia;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Validate Patient Involvement Code
	public String validatePatientInvolCode(String strPatientInvolCode) {
		String strErrorMsg = null;
		String strAryPatientCode[] = null;
		String strNotFoundPatientCodes = new String();
		ArrayList<String> strAryLstFoundPatientCodes = new ArrayList<String>();

		URL url = null;

		if (strPatientInvolCode != null && strPatientInvolCode.length() == 0) {
			strPatientInvolCode = null;
		}
		try {
			if (strPatientInvolCode != null) {
				strAryPatientCode = strPatientInvolCode.split(",");
				for (int i = 0; i < strAryPatientCode.length; i++) {
					strPatientInvolCode = strAryPatientCode[i].replaceAll(" ", "%20");
					if (!strPatientInvolCode.equalsIgnoreCase("NULL") && !strPatientInvolCode.equalsIgnoreCase("''")) {
						url = new URL(this.objComplaintBean.getEtQURL()
								+ "/dao/LOOKUPS/MEDWATCH_PATIENT_CODE/where?columns=DESCRIPTION&keys=ETQ$IS_DISABLED,VALUE&values=0,"
								+ strPatientInvolCode);
						if (url != null) {
							StringBuffer response = getConnection(url);
							JSONObject jsonObj = null;
							int cntRec = -1;

							if (response != null && response.length() > 0) {
								if (response.toString().contains("400")) {
									strErrorMsg = "Error: Patient Involvement Code - Display value(s): ["
											+ strPatientInvolCode.replaceAll("%20", " ")
											+ "] is not listed in the field's keyword options ";
									break;
								} else if (response.toString().contains("Error: ")) {
									strErrorMsg = response.toString() + "in Patient Involvement Code ";
								} else {
									jsonObj = new JSONObject(response.toString());
									cntRec = Integer.parseInt(jsonObj.get("count").toString());

									if (cntRec == 0) {
										strNotFoundPatientCodes = strNotFoundPatientCodes + ", "
												+ strPatientInvolCode.replaceAll("%20", " ");
									} else {
										String strRec = jsonObj.get("Records").toString();
										JSONObject jsonRecObj = new JSONObject(
												strRec.substring(1, strRec.length() - 1));

										String strCol = jsonRecObj.get("Columns").toString();
										JSONObject jsonColObj = new JSONObject(
												strCol.substring(1, strCol.length() - 1));

										strAryLstFoundPatientCodes.add(strPatientInvolCode.replaceAll("%20", " ")
												+ " - " + jsonColObj.get("value").toString());
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strNotFoundPatientCodes != null && strNotFoundPatientCodes.length() > 0) {
			strErrorMsg = "Error: Patient Involvement Code - Display value(s): [" + strNotFoundPatientCodes.substring(2)
					+ "] is not listed in the field's keyword options ";
			this.objComplaintBean.setPatientInvolvementCode(null);
		} else {
			this.objComplaintBean.setPatientInvolvementCode(strAryLstFoundPatientCodes);
		}
		return strErrorMsg;
	}

	// Validate Warranty Review Required
	public String validateWarrantyReviewRequitred(String strWarReviewReq) {
		String strStatus = null;

		if (strWarReviewReq != null && strWarReviewReq.length() > 0) {
			strWarReviewReq = getYesNoValue(strWarReviewReq);

			if (strWarReviewReq != null && !strWarReviewReq.equalsIgnoreCase("Yes")
					&& !strWarReviewReq.equalsIgnoreCase("No")) {
				strStatus = "Error: Warranty Review Required? - Display value(s): [" + strWarReviewReq
						+ "] is not listed in the field's keyword options ";
			}
		}

		if (strStatus == null || strStatus.length() == 0) {
			strStatus = strWarReviewReq;
		}
		return strStatus;
	}

	// Validate Assigned To
	public String validateAssignedTo(String strAssignedTo, String strEtQAuthor) {
		String strStatus = null;
		String strErrorMsg = null;
		String strAssTo = null;
		URL url = null;

		try {
			if (strAssignedTo == null || strAssignedTo.length() == 0) {
				strAssignedTo = strEtQAuthor;
			}

			if (strAssignedTo != null) {
				strAssignedTo = strAssignedTo.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/dao/ENGINE/USER_SETTINGS/where?columns=USER_NAME&keys=IS_DISABLED,DISPLAY_NAME&values=0,"
						+ strAssignedTo);
				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						jsonObj = new JSONObject(response.toString());
						cntRec = Integer.parseInt(jsonObj.get("count").toString());

						if (cntRec == 0) {
							strErrorMsg = "[" + strAssignedTo.replaceAll("%20", " ") + "] does not exist ";
						} else {
							String strRec = jsonObj.get("Records").toString();
							JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

							String strCol = jsonRecObj.get("Columns").toString();
							JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

							strAssTo = jsonColObj.get("value").toString();

						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}
		if (strAssTo != null && strErrorMsg == null) {
			strStatus = strAssTo;
		} else {
			strStatus = strErrorMsg;
		}

		return strStatus;
	}

	// Validate As Reported Code with Product Family
	public XxComplaintInterfaceBean validateCompAsReportedCode(XxComplaintInterfaceBean objComplaintBean) {

		// system.out.println("Inside validateCompAsReportedCode method");
		logger.info("Validating As Reported Code");
		this.objComplaintBean = objComplaintBean;
		String strErrorMsg = new String();
		String strAsRepCode = null;
		String strAsRepDesc = null;
		String strAryARCodeDesc[] = null;
		String strWrongAsRepCodes = new String();
		ArrayList<String> aryRtnARCodeDesc = new ArrayList<String>();
		ArrayList<String> nonRSACode = new ArrayList<String>();
		String strModelNum = this.objComplaintBean.getModelNumber();
		String strComPhase = this.objComplaintBean.getPhaseName();
		// system.out.println("AR value"+this.objComplaintBean.getAsReportedCode());
		ArrayList<String> aryAsReportedCode = this.objComplaintBean.getAsReportedCode();
		URL url = null;

		try {
			if (!strComPhase.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_DRAFT")
					&& (aryAsReportedCode == null || aryAsReportedCode.size() == 0)) {
				strErrorMsg = "Error: As Reported Codes are mandatory for Source Ref. No. : "
						+ this.objComplaintBean.getSystemSourceRefNo();
				this.objComplaintBean.setErrorMessage(strErrorMsg);
			} else if (strModelNum != null && aryAsReportedCode != null && aryAsReportedCode.size() > 0) {
				// system.out.println("Inside Else loop -------------------------------");
				// Added for ETQCR-859
				String isPriorities = "No";
				for (int i = 0; i < aryAsReportedCode.size(); i++) {
					strAryARCodeDesc = aryAsReportedCode.get(i).split("@", 2);

					strAsRepCode = strAryARCodeDesc[0];
					strAsRepDesc = strAryARCodeDesc[1];

					if ((strAsRepCode == null || strAsRepCode.length() == 0 || strAsRepCode.equalsIgnoreCase("null"))
							&& (strAsRepDesc == null || strAsRepDesc.length() == 0
									|| strAsRepDesc.equalsIgnoreCase("null"))) {
						// system.out.println("Insdie Break loop");
						strErrorMsg = "Error: As Reported Codes or As Reported Codes Description cannot be null at the same time for Source Ref. No. : "
								+ this.objComplaintBean.getSystemSourceRefNo();
						this.objComplaintBean.setErrorMessage(strErrorMsg);
						break;
					} else if (strAsRepCode == null || strAsRepCode.length() == 0
							|| strAsRepCode.equalsIgnoreCase("null")) {
						strAsRepCode = strAsRepDesc;
					}

					if (strAsRepCode != null && strAsRepCode.length() > 0) {
						/*
						 * strAsRepCode = strAsRepCode.replace("/", "%2F"); strAsRepCode =
						 * strAsRepCode.replace("|", "%7C"); strAsRepCode =
						 * strAsRepCode.replaceAll(" ","%20"); strModelNum=
						 * strModelNum.replaceAll(" ","%20");
						 */
						url = new URL(this.objComplaintBean.getEtQURL()
								+ "/datasources/WS_AS_REPORTED_CODE_DS_P/execute?VAR$AR_CODE="
								+ URLEncoder.encode(strAsRepCode, "UTF-8"));

						if (url != null) {
							StringBuffer response = getConnection(url);
							JSONObject jsonObj = null;
							int cntRec = -1;

							if (response != null && response.length() > 0) {
								jsonObj = new JSONObject(response.toString());
								cntRec = Integer.parseInt(jsonObj.get("count").toString());

								if (cntRec > 0) {
									String strRec = jsonObj.get("Records").toString();
									JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

									String strCol = jsonRecObj.get("Columns").toString();
									JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

									String[] strArrayARCode = jsonColObj.get("value").toString().split(":");
									// Modified for ETQCR-760
									String strIsCodePAE = strArrayARCode[1];
									String strIsPAE = this.objComplaintBean.getIsPotentialAdverseEvent();
									if (strIsPAE != null && strIsPAE.length() > 0 && strIsPAE.equalsIgnoreCase("Yes")) {
										aryRtnARCodeDesc.add(strArrayARCode[0]);
										// Added for ETQCR-850
										if (strIsCodePAE != null && strIsCodePAE.length() > 0
												&& strIsCodePAE.equalsIgnoreCase("Unknown")
												&& !this.objComplaintBean.getPhaseName()
														.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED")) {
											if (this.objComplaintBean.getPaeQuestionAns1() != null
													&& this.objComplaintBean.getPaeQuestionAns1().length() > 0
													&& !this.objComplaintBean.getPaeQuestionAns1()
															.equalsIgnoreCase("Yes")) {
												if (!isPriorities.equalsIgnoreCase("Yes") && (this.objComplaintBean
														.getPaeQuestionAns2() == null
														|| this.objComplaintBean.getPaeQuestionAns2().length() == 0)) {
													this.objComplaintBean.setPaeQuestionAns2("Answer 3");
													// Added for ETQCR-859
													isPriorities = "Yes";
												}
											}
										}
										// Added for ETQCR-859
										else if (strIsCodePAE != null && strIsCodePAE.length() > 0
												&& strIsCodePAE.equalsIgnoreCase("Yes")
												&& !this.objComplaintBean.getPhaseName()
														.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED")) {
											this.objComplaintBean.setPaeQuestionAns2("");
											isPriorities = "Yes";
										}
									} else {
										if (strIsCodePAE != null && strIsCodePAE.length() > 0
												&& strIsCodePAE.equalsIgnoreCase("Yes")
												&& !this.objComplaintBean.getPhaseName()
														.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED")) {
											this.objComplaintBean.setPaeQuestionAns2("Answer 1");
											aryRtnARCodeDesc.add(strArrayARCode[0]);
										}
										// Added for ETQCR-850
										else if (strIsCodePAE != null && strIsCodePAE.length() > 0
												&& strIsCodePAE.equalsIgnoreCase("Unknown")
												&& !this.objComplaintBean.getPhaseName()
														.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED")
												&& strIsPAE != null && strIsPAE.length() > 0
												&& !strIsPAE.equalsIgnoreCase("Unknown")) {
											aryRtnARCodeDesc.add(strArrayARCode[0]);
											if (this.objComplaintBean.getPaeQuestionAns2() == null
													|| this.objComplaintBean.getPaeQuestionAns2().length() == 0) {
												this.objComplaintBean.setPaeQuestionAns2("Answer 3");
											}
										} else if (strIsCodePAE != null && strIsCodePAE.length() > 0
												&& this.objComplaintBean.getPhaseName()
														.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED")
												&& strIsCodePAE.equalsIgnoreCase("Yes")) {
											this.objComplaintBean.setErrorMessage(
													"Error: Complaint with PAE As Reported Code(s)/Intake Universal Code(s): ["
															+ URLDecoder.decode(strAsRepCode, "UTF-8")
															+ "] must be PAE for Source Ref. No. : "
															+ this.objComplaintBean.getSystemSourceRefNo());
										}
										// Added for ETQCR-850
										else if (strIsCodePAE != null && strIsCodePAE.length() > 0
												&& this.objComplaintBean.getPhaseName()
														.equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED")
												&& strIsCodePAE.equalsIgnoreCase("Unknown")) {
											this.objComplaintBean.setErrorMessage(
													"Error: Complaint with PAE Unknown As Reported Code(s)/Intake Universal Code(s): ["
															+ URLDecoder.decode(strAsRepCode, "UTF-8")
															+ "] must be PAE Unknown for Source Ref. No. : "
															+ this.objComplaintBean.getSystemSourceRefNo());
										} else {
											// Added for ETQCR-816 & ETQCR-847
											if (this.objComplaintBean.getItemType() != null
													&& this.objComplaintBean.getItemType().length() > 0
													&& !this.objComplaintBean.getItemType().equalsIgnoreCase("Medical")
													&& strArrayARCode[0] != null && strArrayARCode[0].length() > 0
													&& !strArrayARCode[0].startsWith("AR")
													&& !strArrayARCode[0].startsWith("000")) {
												nonRSACode.add(strArrayARCode[0]);
											} else {
												aryRtnARCodeDesc.add(strArrayARCode[0]);
											}
										}
									}
								} else {
									strWrongAsRepCodes = strWrongAsRepCodes + ", "
											+ URLDecoder.decode(strAsRepCode, "UTF-8");
								}
							}
						}
					}
				}

				if (strWrongAsRepCodes != null && strWrongAsRepCodes.length() > 0) {
					this.objComplaintBean.setErrorMessage(
							"Error: As Reported Code(s) - Display value(s): [" + strWrongAsRepCodes.substring(2)
									+ "] is not listed in the field's keyword options for Source Ref. No. : "
									+ this.objComplaintBean.getSystemSourceRefNo());
				}
				// Added for ETQCR-816
				else if (nonRSACode != null && nonRSACode.size() > 0) {
					this.objComplaintBean.setErrorMessage("Error: RSS Intake Universal Code(s): "
							+ nonRSACode.toString() + " is selected for SSBU Source Ref. No. : "
							+ this.objComplaintBean.getSystemSourceRefNo());
				} else if ((strErrorMsg == null || strErrorMsg.length() == 0)
						&& (strWrongAsRepCodes == null || strWrongAsRepCodes.length() == 0)) {
					// system.out.println("Error MSG"+strErrorMsg);
					// system.out.println("ARTRTN"+aryRtnARCodeDesc);
					this.objComplaintBean.setAsReportedCode(aryRtnARCodeDesc);
				}
				// system.out.println("Error MSG line 3329
				// _______________________"+strErrorMsg);
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}
		logger.info("Validating Completed for As Reported Code");
		return this.objComplaintBean;
	}

	// Added for MDMTESTING-4473
	public XxComplaintInterfaceBean validateCoding(XxComplaintInterfaceBean objComplaintBean) {
		this.objComplaintBean = objComplaintBean;
		String strIsInveReq = this.objComplaintBean.getInvestigationRequired();
		int intCodingFlag = 1;
		int intInveClosureDateFlag = 0;

		try {
			Statement stmtCoding = this.objComplaintBean.getBusinessUnitConnection().createStatement();
			Statement stmtInveAct = this.objComplaintBean.getBusinessUnitConnection().createStatement();

			String strCodingQry = "SELECT COUNT(INTERFACE_RECORD_ID) CODING_COUNT FROM XX_COMP_PART_ANA_CAU_CODE_INT WHERE INTERFACE_RECORD_ID = "
					+ this.objComplaintBean.getInterfaceRecordId();
			String strInveQry = "SELECT COUNT(INVESTIGATION_CLOSURE_DATE) INVE_CLOSE_DATE_COUNT FROM  XX_COMP_INV_ACT_INTERFACE WHERE INVESTIGATION_CLOSURE_DATE IS NOT NULL AND INTERFACE_RECORD_ID="
					+ this.objComplaintBean.getInterfaceRecordId();

			ResultSet rsCoding = null;
			ResultSet rsInveAct = null;

			rsCoding = stmtCoding.executeQuery(strCodingQry);
			rsInveAct = stmtInveAct.executeQuery(strInveQry);

			if (rsCoding != null) {
				while (rsCoding.next()) {
					intCodingFlag = rsCoding.getInt(1);
				}
			}
			stmtCoding.close();
			rsCoding.close();

			if (rsInveAct != null) {
				while (rsInveAct.next()) {
					intInveClosureDateFlag = rsInveAct.getInt(1);
				}
			}
			stmtInveAct.close();
			rsInveAct.close();

			if (intCodingFlag == 0) {
				if (strIsInveReq != null && strIsInveReq.length() > 0
						&& (strIsInveReq.equalsIgnoreCase("No") || strIsInveReq.equalsIgnoreCase("N"))
						&& this.objComplaintBean.getComplaintClosureDate() != null
						&& this.objComplaintBean.getComplaintClosureDate().length() > 0) {
					this.objComplaintBean.setErrorMessage("Error: Coding data is required for Source Ref. No. : "
							+ this.objComplaintBean.getSystemSourceRefNo());
				} else if (strIsInveReq != null && strIsInveReq.length() > 0
						&& (strIsInveReq.equalsIgnoreCase("Yes") || strIsInveReq.equalsIgnoreCase("Y"))
						&& intInveClosureDateFlag > 0) {
					this.objComplaintBean.setErrorMessage("Error: Coding data is required for Source Ref. No. : "
							+ this.objComplaintBean.getSystemSourceRefNo());
				}
			}
			// Added for ETQCR-760
			else {
				if (this.objComplaintBean.getErrorMessage() == null
						|| !this.objComplaintBean.getErrorMessage().contains("Error: ")) {
					Statement stmtEtQCode = this.objComplaintBean.getBusinessUnitConnection().createStatement();

					String strEtQAllCodeQry = "SELECT A.ALL_CODE_COUNT,B.PART_CODE_COUNT,C.CAUSE_CODE_COUNT,D.AA_CODE_COUNT FROM (SELECT COUNT(*) ALL_CODE_COUNT FROM XX_COMP_PART_ANA_CAU_CODE_INT WHERE INTERFACE_RECORD_ID = "
							+ this.objComplaintBean.getInterfaceRecordId()
							+ " AND CASE WHEN PART_CODE IS NULL THEN 0 WHEN PART_CODE='' THEN 0 END=0 AND CASE WHEN PART_CODE_DESC IS NULL THEN 0 WHEN PART_CODE_DESC='' THEN 0 END=0"
							+ " AND CASE WHEN CAUSE_CODE IS NULL THEN 0 WHEN CAUSE_CODE='' THEN 0 END=0 AND CASE WHEN CAUSE_CODE_DESC IS NULL THEN 0 WHEN CAUSE_CODE_DESC='' THEN 0 END=0"
							+ " AND CASE WHEN AS_ANALYZED_CODE IS NULL THEN 0 WHEN AS_ANALYZED_CODE='' THEN 0 END=0 AND CASE WHEN AS_ANALYZED_CODE_DESC IS NULL THEN 0 WHEN AS_ANALYZED_CODE_DESC='' THEN 0 END=0) A,"

							+ " (SELECT COUNT(*) PART_CODE_COUNT FROM XX_COMP_PART_ANA_CAU_CODE_INT WHERE INTERFACE_RECORD_ID = "
							+ this.objComplaintBean.getInterfaceRecordId()
							+ " AND (CASE WHEN AS_ANALYZED_CODE IS NULL THEN 0 WHEN AS_ANALYZED_CODE='' THEN 0 ELSE 1 END>0 OR CASE WHEN AS_ANALYZED_CODE_DESC IS NULL THEN 0 WHEN AS_ANALYZED_CODE_DESC='' THEN 0 ELSE 1 END>0)"
							+ " AND CASE WHEN PART_CODE IS NULL THEN 0 WHEN PART_CODE='' THEN 0 END=0 AND CASE WHEN PART_CODE_DESC IS NULL THEN 0 WHEN PART_CODE_DESC='' THEN 0 END=0"
							+ " AND (CASE WHEN CAUSE_CODE IS NULL THEN 0 WHEN CAUSE_CODE='' THEN 0 ELSE 1 END>0 OR CASE WHEN CAUSE_CODE_DESC IS NULL THEN 0 WHEN CAUSE_CODE_DESC='' THEN 0 ELSE 1 END>0)) B,"

							+ " (SELECT COUNT(*) CAUSE_CODE_COUNT FROM XX_COMP_PART_ANA_CAU_CODE_INT WHERE INTERFACE_RECORD_ID = "
							+ this.objComplaintBean.getInterfaceRecordId()
							+ " AND (CASE WHEN AS_ANALYZED_CODE IS NULL THEN 0 WHEN AS_ANALYZED_CODE='' THEN 0 ELSE 1 END>0 OR CASE WHEN AS_ANALYZED_CODE_DESC IS NULL THEN 0 WHEN AS_ANALYZED_CODE_DESC='' THEN 0 ELSE 1 END>0)"
							+ " AND (CASE WHEN PART_CODE IS NULL THEN 0 WHEN PART_CODE='' THEN 0 ELSE 1 END>0 OR CASE WHEN PART_CODE_DESC IS NULL THEN 0 WHEN PART_CODE_DESC='' THEN 0 ELSE 1 END>0)"
							+ " AND CASE WHEN CAUSE_CODE IS NULL THEN 0 WHEN CAUSE_CODE='' THEN 0 END=0 AND CASE WHEN CAUSE_CODE_DESC IS NULL THEN 0 WHEN CAUSE_CODE_DESC='' THEN 0 END=0)C,"

							+ " (SELECT COUNT(*) AA_CODE_COUNT FROM XX_COMP_PART_ANA_CAU_CODE_INT WHERE INTERFACE_RECORD_ID = "
							+ this.objComplaintBean.getInterfaceRecordId()
							+ " AND CASE WHEN AS_ANALYZED_CODE IS NULL THEN 0 WHEN AS_ANALYZED_CODE='' THEN 0 END=0 AND CASE WHEN AS_ANALYZED_CODE_DESC IS NULL THEN 0 WHEN AS_ANALYZED_CODE_DESC='' THEN 0 END=0)D";

					ResultSet rsCode = stmtEtQCode.executeQuery(strEtQAllCodeQry);
					int intAllCodeFlag = 0, intPartCodeFlag = 0, intCauseCodeFlag = 0, intAACodeFlag = 0;
					if (rsCode != null) {
						while (rsCode.next()) {
							intAllCodeFlag = rsCode.getInt(1);
							intPartCodeFlag = rsCode.getInt(2);
							intCauseCodeFlag = rsCode.getInt(3);
							intAACodeFlag = rsCode.getInt(4);
						}
					}
					rsCode.close();
					stmtEtQCode.close();

					if (intAllCodeFlag > 0) {
						// Commented ETQCR-880
						/*
						 * if (strIsInveReq != null && strIsInveReq.length() > 0 &&
						 * (strIsInveReq.equalsIgnoreCase("No") || strIsInveReq.equalsIgnoreCase("N"))
						 * && this.objComplaintBean.getComplaintClosureDate() != null &&
						 * this.objComplaintBean.getComplaintClosureDate().length() > 0) {
						 * this.objComplaintBean.
						 * setErrorMessage("Error: Coding data is required for Source Ref. No. : " +
						 * this.objComplaintBean.getSystemSourceRefNo()); } else if (strIsInveReq !=
						 * null && strIsInveReq.length() > 0 && (strIsInveReq.equalsIgnoreCase("Yes") ||
						 * strIsInveReq.equalsIgnoreCase("Y"))) {
						 */
						this.objComplaintBean.setErrorMessage("Error: Coding data is required for Source Ref. No. : "
								+ this.objComplaintBean.getSystemSourceRefNo());
						// }
					} else if (intPartCodeFlag > 0) {
						// Commented ETQCR-880
						/*
						 * if (strIsInveReq != null && strIsInveReq.length() > 0 &&
						 * (strIsInveReq.equalsIgnoreCase("No") || strIsInveReq.equalsIgnoreCase("N"))
						 * && this.objComplaintBean.getComplaintClosureDate() != null &&
						 * this.objComplaintBean.getComplaintClosureDate().length() > 0) {
						 * this.objComplaintBean.
						 * setErrorMessage("Error: Part Code and Part Code Description can not be null at the same time for Source Ref. No. : "
						 * + this.objComplaintBean.getSystemSourceRefNo()); } else if (strIsInveReq !=
						 * null && strIsInveReq.length() > 0 && (strIsInveReq.equalsIgnoreCase("Yes") ||
						 * strIsInveReq.equalsIgnoreCase("Y"))) {
						 */
						this.objComplaintBean.setErrorMessage(
								"Error: Part Code and Part Code Description can not be null at the same time for Source Ref. No. : "
										+ this.objComplaintBean.getSystemSourceRefNo());
						// }
					} else if (intCauseCodeFlag > 0) {
						// Commented ETQCR-880
						/*
						 * if (strIsInveReq != null && strIsInveReq.length() > 0 &&
						 * (strIsInveReq.equalsIgnoreCase("No") || strIsInveReq.equalsIgnoreCase("N"))
						 * && this.objComplaintBean.getComplaintClosureDate() != null &&
						 * this.objComplaintBean.getComplaintClosureDate().length() > 0) {
						 * this.objComplaintBean.
						 * setErrorMessage("Error: Cause Code and Cause Code Description can not be null at the same time for Source Ref. No. : "
						 * + this.objComplaintBean.getSystemSourceRefNo()); } else if (strIsInveReq !=
						 * null && strIsInveReq.length() > 0 && (strIsInveReq.equalsIgnoreCase("Yes") ||
						 * strIsInveReq.equalsIgnoreCase("Y"))) {
						 */
						this.objComplaintBean.setErrorMessage(
								"Error: Cause Code and Cause Code Description can not be null at the same time for Source Ref. No. : "
										+ this.objComplaintBean.getSystemSourceRefNo());
						// }
					} else if (intAACodeFlag > 0) {
						// Commented ETQCR-880
						/*
						 * if (strIsInveReq != null && strIsInveReq.length() > 0 &&
						 * (strIsInveReq.equalsIgnoreCase("No") || strIsInveReq.equalsIgnoreCase("N"))
						 * && this.objComplaintBean.getComplaintClosureDate() != null &&
						 * this.objComplaintBean.getComplaintClosureDate().length() > 0) {
						 * this.objComplaintBean.
						 * setErrorMessage("Error: As Analyzed Code and As Analyzed Code Description can not be null at the same time for Source Ref. No. : "
						 * + this.objComplaintBean.getSystemSourceRefNo()); } else if (strIsInveReq !=
						 * null && strIsInveReq.length() > 0 && (strIsInveReq.equalsIgnoreCase("Yes") ||
						 * strIsInveReq.equalsIgnoreCase("Y"))) {
						 */
						this.objComplaintBean.setErrorMessage(
								"Error: As Analyzed Code and As Analyzed Code Description can not be null at the same time for Source Ref. No. : "
										+ this.objComplaintBean.getSystemSourceRefNo());
						// }
					}
				}

				// Validate existing Final Universal Code
				if (this.objComplaintBean.getErrorMessage() == null
						|| !this.objComplaintBean.getErrorMessage().contains("Error: ")) {
					Statement stmtFinalCode = this.objComplaintBean.getBusinessUnitConnection().createStatement();

					String strFinalCodeQry = "SELECT DISTINCT AS_ANALYZED_CODE,AS_ANALYZED_CODE_DESC FROM XX_COMP_PART_ANA_CAU_CODE_INT "
							+ "WHERE INTERFACE_RECORD_ID= " + this.objComplaintBean.getInterfaceRecordId()
							+ " AND CASE WHEN PART_CODE IS NULL THEN 0 WHEN PART_CODE='' THEN 0 END=0 AND CASE WHEN PART_CODE_DESC IS NULL THEN 0 WHEN PART_CODE_DESC='' THEN 0 END=0"
							+ " AND CASE WHEN CAUSE_CODE IS NULL THEN 0 WHEN CAUSE_CODE='' THEN 0 END=0 AND CASE WHEN CAUSE_CODE_DESC IS NULL THEN 0 WHEN CAUSE_CODE_DESC='' THEN 0 END=0";

					ResultSet rsFinalCode = stmtFinalCode.executeQuery(strFinalCodeQry);
					if (rsFinalCode != null) {
						// Added for ETQCR-859
						String isPriorities = "No";
						ArrayList<String> aryRtnFinalCode = new ArrayList<String>();
						while (rsFinalCode.next()) {
							String strAsAnalyzedCode = rsFinalCode.getString(1);
							if (strAsAnalyzedCode == null || strAsAnalyzedCode.length() == 0) {
								strAsAnalyzedCode = rsFinalCode.getString(2);
							}

							if (strAsAnalyzedCode != null && strAsAnalyzedCode.length() > 0) {
								/*
								 * strAsAnalyzedCode = strAsAnalyzedCode.replace("/", "%2F"); strAsAnalyzedCode
								 * = strAsAnalyzedCode.replace("|", "%7C"); strAsAnalyzedCode =
								 * strAsAnalyzedCode.replaceAll(" ","%20");
								 */
								URL url = new URL(this.objComplaintBean.getEtQURL()
										+ "/datasources/WS_AS_REPORTED_CODE_DS_P/execute?VAR$AR_CODE="
										+ URLEncoder.encode(strAsAnalyzedCode, "UTF-8"));

								if (url != null) {
									StringBuffer response = getConnection(url);
									JSONObject jsonObj = null;
									int cntRec = -1;

									if (response != null && response.length() > 0) {
										jsonObj = new JSONObject(response.toString());
										cntRec = Integer.parseInt(jsonObj.get("count").toString());

										if (cntRec > 0) {
											String strRec = jsonObj.get("Records").toString();
											JSONObject jsonRecObj = new JSONObject(
													strRec.substring(1, strRec.length() - 1));

											String strCol = jsonRecObj.get("Columns").toString();
											JSONObject jsonColObj = new JSONObject(
													strCol.substring(1, strCol.length() - 1));

											String[] strArrayARCode = jsonColObj.get("value").toString().split(":");
											// Modified for ETQCR-760
											String strIsCodePAE = strArrayARCode[1];
											// Added for ETQCR-987
											// String strTreeType=strArrayARCode[2];
											String strIsPAE = this.objComplaintBean.getIsPotentialAdverseEvent();
											if (strIsPAE != null && strIsPAE.length() > 0
													&& strIsPAE.equalsIgnoreCase("Yes")) {
												aryRtnFinalCode.add(strArrayARCode[0]);
												// Added for ETQCR-850
												if (strIsCodePAE != null && strIsCodePAE.length() > 0
														&& strIsCodePAE.equalsIgnoreCase("Unknown")
														&& !this.objComplaintBean.getPhaseName().equalsIgnoreCase(
																"COMPLAINTS_COMPLAINT_HANDLING_CLOSED")) {
													if (this.objComplaintBean.getPaeQuestionAns1() != null
															&& this.objComplaintBean.getPaeQuestionAns1().length() > 0
															&& !this.objComplaintBean.getPaeQuestionAns1()
																	.equalsIgnoreCase("Yes")) {
														if (!isPriorities.equalsIgnoreCase("Yes")
																&& (this.objComplaintBean.getPaeQuestionAns2() == null
																		|| this.objComplaintBean.getPaeQuestionAns2()
																				.length() == 0)) {
															this.objComplaintBean.setPaeQuestionAns2("Answer 3");
															// Added for ETQCR-859
															isPriorities = "Yes";
														}
													}
												}
												// Added for ETQCR-859
												else if (strIsCodePAE != null && strIsCodePAE.length() > 0
														&& strIsCodePAE.equalsIgnoreCase("Yes")
														&& !this.objComplaintBean.getPhaseName().equalsIgnoreCase(
																"COMPLAINTS_COMPLAINT_HANDLING_CLOSED")) {
													this.objComplaintBean.setPaeQuestionAns2("");
													isPriorities = "Yes";
												}
											} else {
												if (strIsCodePAE != null && strIsCodePAE.length() > 0
														&& strIsCodePAE.equalsIgnoreCase("Yes")
														&& !this.objComplaintBean.getPhaseName().equalsIgnoreCase(
																"COMPLAINTS_COMPLAINT_HANDLING_CLOSED")) {
													this.objComplaintBean.setPaeQuestionAns2("Answer 1");
													aryRtnFinalCode.add(strArrayARCode[0]);
												}
												// Added for ETQCR-850
												else if (strIsCodePAE != null && strIsCodePAE.length() > 0
														&& strIsCodePAE.equalsIgnoreCase("Unknown")
														&& !this.objComplaintBean.getPhaseName().equalsIgnoreCase(
																"COMPLAINTS_COMPLAINT_HANDLING_CLOSED")
														&& strIsPAE != null && strIsPAE.length() > 0
														&& !strIsPAE.equalsIgnoreCase("Unknown")) {
													aryRtnFinalCode.add(strArrayARCode[0]);
													if (this.objComplaintBean.getPaeQuestionAns2() == null
															|| this.objComplaintBean.getPaeQuestionAns2()
																	.length() == 0) {
														this.objComplaintBean.setPaeQuestionAns2("Answer 3");
													}
												} else if (strIsCodePAE != null && strIsCodePAE.length() > 0
														&& this.objComplaintBean.getPhaseName().equalsIgnoreCase(
																"COMPLAINTS_COMPLAINT_HANDLING_CLOSED")
														&& strIsCodePAE.equalsIgnoreCase("Yes")) {

													this.objComplaintBean.setErrorMessage(
															"Error: Complaint with PAE Final Universal Code(s): ["
																	+ strArrayARCode[0]
																	+ "] must be PAE for Source Ref. No. : "
																	+ this.objComplaintBean.getSystemSourceRefNo());

												}
												// Added for ETQCR-850
												else if (strIsCodePAE != null && strIsCodePAE.length() > 0
														&& this.objComplaintBean.getPhaseName().equalsIgnoreCase(
																"COMPLAINTS_COMPLAINT_HANDLING_CLOSED")
														&& strIsCodePAE.equalsIgnoreCase("Unknown")) {
													this.objComplaintBean.setErrorMessage(
															"Error: Complaint with PAE Unknown Final Universal Code(s): ["
																	+ strArrayARCode[0]
																	+ "] must be PAE Unknown for Source Ref. No. : "
																	+ this.objComplaintBean.getSystemSourceRefNo());
												}
												// Added for ETQCR-987
												/*
												 * else if (strTreeType!=null && strTreeType.length()>0 &&
												 * this.objComplaintBean.getPhaseName().equalsIgnoreCase(
												 * "COMPLAINTS_COMPLAINT_HANDLING_CLOSED") &&
												 * strTreeType.equalsIgnoreCase("Intake")) { this.objComplaintBean.
												 * setErrorMessage("Error: Estimation code is required for the field �Local System As Analyzed/Final Universal Code� as code["
												 * +strArrayARCode[0]+"] is intake for Source Ref. No. :" +
												 * this.objComplaintBean.getSystemSourceRefNo()); }
												 */
												else {
													aryRtnFinalCode.add(strArrayARCode[0]);
												}
											}
										} else {
											strAsAnalyzedCode = strAsAnalyzedCode.replace("%2F", "/");
											strAsAnalyzedCode = strAsAnalyzedCode.replace("%7C", "|");
											strAsAnalyzedCode = strAsAnalyzedCode.replaceAll("%20", " ");
											this.objComplaintBean
													.setErrorMessage("Error: Final Universal Code - Display value(s): ["
															+ URLDecoder.decode(strAsAnalyzedCode, "UTF-8")
															+ "] is not listed in the field's keyword options for Source Ref. No. : "
															+ this.objComplaintBean.getSystemSourceRefNo());
										}
									}
								}
							} else {
								this.objComplaintBean.setErrorMessage(
										"Error: Final Universal Code can not be null for Source Ref. No. : "
												+ this.objComplaintBean.getSystemSourceRefNo());
							}
						}
						if (aryRtnFinalCode != null && aryRtnFinalCode.size() > 0) {
							this.objComplaintBean.setFinalUniversalCode(aryRtnFinalCode);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		return this.objComplaintBean;
	}

	// Validate Part Codes
	public XxComplaintInterfaceBean validateCompPartCode(XxComplaintInterfaceBean objComplaintBean) {
		this.objComplaintBean = objComplaintBean;
		// String strErrorMsg = new String();
		URL url = null;
		String strPartCodeFound = null;
		String strPartCode = this.objComplaintBean.getPartCodeFound();
		String strModelNum = this.objComplaintBean.getModelNumber();

		try {
			if (strPartCode != null && strPartCode.length() == 0) {
				strPartCode = null;
			}
			// Commented for ETQCR-690
			/*
			 * if (strPartCode == null || strPartCode.equalsIgnoreCase("null") ||
			 * strPartCode.length() == 0) { strErrorMsg =
			 * "Error: Part Code and Part Code Description can not be null at the same time for Source Ref. No. : "
			 * + this.objComplaintBean.getSystemSourceRefNo(); } else
			 */
			if (strPartCode != null && strPartCode.length() > 0) {
				strPartCode = strPartCode.replace("/", "%2F");
				strPartCode = strPartCode.replace("|", "%7C");
				strPartCode = strPartCode.replaceAll(" ", "%20");
				url = new URL(this.objComplaintBean.getEtQURL()
						+ "/datasources/WS_AA_AND_PART_CODE_DS_P/execute?VAR$MODEL_NUM=" + strModelNum
						+ "&VAR$PART_CODE=" + strPartCode + "&VAR$AA_CODE=null");

				if (url != null) {
					StringBuffer response = getConnection(url);
					JSONObject jsonObj = null;
					int cntRec = -1;

					if (response != null && response.length() > 0) {
						jsonObj = new JSONObject(response.toString());
						cntRec = Integer.parseInt(jsonObj.get("count").toString());

						if (cntRec > 0) {
							String strRec = jsonObj.get("Records").toString();
							JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

							String strCol = jsonRecObj.get("Columns").toString();
							JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

							strPartCodeFound = jsonColObj.get("value").toString();
							this.objComplaintBean.setPartCodeFound(strPartCodeFound);
						} else {
							strPartCode = strPartCode.replace("%2F", "/");
							strPartCode = strPartCode.replace("%7C", "|");
							strPartCode = strPartCode.replaceAll("%20", " ");
							this.objComplaintBean.setPartCodeNotFound(strPartCode);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}
		// Commented for ETQCR-690
		/*
		 * if (strErrorMsg != null && strErrorMsg.length() > 0) {
		 * this.objComplaintBean.setErrorMessage(strErrorMsg); }
		 */
		return this.objComplaintBean;
	}

	// Validate As Analyzed Code with Product Family
	public XxComplaintInterfaceBean validateCompAsAnalyzedCode(XxComplaintInterfaceBean objComplaintBean) {
		this.objComplaintBean = objComplaintBean;
		String strErrorMsg = null;
		String strAsAnalyzedCode = null;
		String strAsAnalyzedDesc = null;
		String strAryAnalyzedCodeDesc[] = null;
		String strAsAnalyzedCodeNotFound = new String();
		ArrayList<String> aryLstAsAnalyzedCodeFound = new ArrayList<String>();
		String strModelNum = this.objComplaintBean.getModelNumber();
		String strPartCode = this.objComplaintBean.getPartCodeFound();
		ArrayList<String> aryLstAsAnalyzedCode = this.objComplaintBean.getAsAnalyzedCodeFound();
		this.objComplaintBean.setAsAnalyzedCodeFound(new ArrayList<String>());

		URL url = null;

		try {
			if (aryLstAsAnalyzedCode == null || aryLstAsAnalyzedCode.size() == 0) {
				strErrorMsg = "Error: Analyzed Codes and Analyzed Codes Description are mandatory for Source Ref. No. : "
						+ this.objComplaintBean.getSystemSourceRefNo();
			} else if (aryLstAsAnalyzedCode != null && aryLstAsAnalyzedCode.size() > 0) {
				for (int j = 0; j < aryLstAsAnalyzedCode.size(); j++) {
					strAryAnalyzedCodeDesc = aryLstAsAnalyzedCode.get(j).split("@", 2);
					strAsAnalyzedCode = strAryAnalyzedCodeDesc[0];
					strAsAnalyzedDesc = strAryAnalyzedCodeDesc[1];

					if ((strAsAnalyzedCode == null || strAsAnalyzedCode.length() == 0
							|| strAsAnalyzedCode.equalsIgnoreCase("null"))
							&& (strAsAnalyzedDesc == null || strAsAnalyzedDesc.length() == 0
									|| strAsAnalyzedDesc.equalsIgnoreCase("null"))) {
						strErrorMsg = "Error: Analyzed Codes and Analyzed Codes Description can not be null at the same time for Source Ref. No. : "
								+ this.objComplaintBean.getSystemSourceRefNo();
						break;
					} else if (strAsAnalyzedCode == null || strAsAnalyzedCode.length() == 0
							|| strAsAnalyzedCode.equalsIgnoreCase("null")) {
						strAsAnalyzedCode = strAsAnalyzedDesc;
					}

					if (strAsAnalyzedCode != null && strAsAnalyzedCode.length() > 0) {
						strAsAnalyzedCode = strAsAnalyzedCode.replace("/", "%2F");
						strAsAnalyzedCode = strAsAnalyzedCode.replace("|", "%7C");
						strAsAnalyzedCode = strAsAnalyzedCode.replaceAll(" ", "%20");
						strPartCode = strPartCode.replace("/", "%2F");
						strPartCode = strPartCode.replace("|", "%7C");
						strPartCode = strPartCode.replaceAll(" ", "%20");
						strModelNum = strModelNum.replaceAll(" ", "%20");

						url = new URL(this.objComplaintBean.getEtQURL()
								+ "/datasources/WS_AA_AND_PART_CODE_DS_P/execute?VAR$MODEL_NUM=" + strModelNum
								+ "&VAR$PART_CODE=" + strPartCode + "&VAR$AA_CODE=" + strAsAnalyzedCode);

						if (url != null) {
							StringBuffer response = getConnection(url);
							JSONObject jsonObj = null;
							int cntRec = -1;

							if (response != null && response.length() > 0) {
								jsonObj = new JSONObject(response.toString());
								cntRec = Integer.parseInt(jsonObj.get("count").toString());

								Map<String, String> jsonValMap = new HashMap<String, String>();

								if (cntRec > 0) {
									String strRec = jsonObj.get("Records").toString();
									JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

									JSONObject jsonColObj = new JSONObject(jsonRecObj.toString());
									JSONArray jsonColAry = (JSONArray) jsonColObj.get("Columns");

									for (int i = 0; i < jsonColAry.length(); i++) {
										JSONObject jsonValObj = (JSONObject) jsonColAry.get(i);
										String strColNM = jsonValObj.get("name").toString();
										String strColVal = jsonValObj.get("value").toString();
										jsonValMap.put(strColNM, strColVal);
									}
									if (jsonValMap.get("SUPP_AS_ANALYZED_CODE_DES") != null
											&& jsonValMap.get("SUPP_AS_ANALYZED_CODE_DES").length() > 0) {
										aryLstAsAnalyzedCodeFound.add(jsonValMap.get("SUPP_AS_ANALYZED_CODE_DES"));
									}

								} else {
									String strAANotFound = aryLstAsAnalyzedCode.get(j);
									logger.info("No As Analyzed Code Found for [" + strAANotFound + "] URL: " + url);
									strAANotFound = strAANotFound.replace("%2F", "/");
									strAANotFound = strAANotFound.replace("%7C", "|");
									strAANotFound = strAANotFound.replace("%20", " ");
									strAANotFound = strAANotFound.replace("@", " ");
									strAsAnalyzedCodeNotFound = strAsAnalyzedCodeNotFound + " , " + strAANotFound;
								}
							} else {
								logger.info("No Reponse received for As Analyzed Code [" + strAsAnalyzedCode + "] URL: "
										+ url);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		if (strErrorMsg != null && strErrorMsg.length() > 0) {
			this.objComplaintBean.setErrorMessage(strErrorMsg);
		}
		if (aryLstAsAnalyzedCodeFound != null && aryLstAsAnalyzedCodeFound.size() > 0) {
			this.objComplaintBean.setAsAnalyzedCodeFound(aryLstAsAnalyzedCodeFound);
		}
		if (strAsAnalyzedCodeNotFound != null && strAsAnalyzedCodeNotFound.length() > 0) {
			this.objComplaintBean.setAsAnalyzedCodeNotFound(strAsAnalyzedCodeNotFound.substring(3));
		}
		return this.objComplaintBean;
	}

	// Validate Cause Code
	public XxComplaintInterfaceBean validateCompCauseCode(XxComplaintInterfaceBean objComplaintBean) {
		this.objComplaintBean = objComplaintBean;
		// String strErrorMsg = null;
		String strAryCauseCodeDesc[] = null;
		String strCauseCode = null;
		String strCauseDesc = null;
		String strWrongCauseCode = new String();
		ArrayList<String> aryLstRtnCauseCode = new ArrayList<String>();
		ArrayList<String> aryLstCauseCode = this.objComplaintBean.getCauseCode();
		URL url = null;

		try {
			// Commented for ETQCR-690
			/*
			 * if (aryLstCauseCode == null || aryLstCauseCode.size() == 0) { strErrorMsg =
			 * "Error: Cause Code or Cause Code Description is mandatory for Source Ref. No. : "
			 * + this.objComplaintBean.getSystemSourceRefNo(); } else
			 */

			if (aryLstCauseCode != null && aryLstCauseCode.size() > 0) {
				for (int i = 0; i < aryLstCauseCode.size(); i++) {
					strAryCauseCodeDesc = aryLstCauseCode.get(i).split("@", 2);

					strCauseCode = strAryCauseCodeDesc[0];
					strCauseDesc = strAryCauseCodeDesc[1];

					// Commented for ETQCR-690
					/*
					 * if ((strCauseCode == null || strCauseCode.equalsIgnoreCase("null") ||
					 * strCauseCode.length() == 0) && (strCauseDesc == null ||
					 * strCauseDesc.equalsIgnoreCase("null") || strCauseDesc.length() == 0)) {
					 * strErrorMsg =
					 * "Error: Cause Code or Cause Code Description can not be null at the same time for Source Ref. No. : "
					 * + this.objComplaintBean.getSystemSourceRefNo(); break; } else
					 */

					if (strCauseCode == null || strCauseCode.equalsIgnoreCase("null") || strCauseCode.length() == 0) {
						strCauseCode = strCauseDesc;
					}

					if (strCauseCode != null && strCauseCode.length() > 0) {
						strCauseCode = strCauseCode.replace("/", "%2F");
						strCauseCode = strCauseCode.replace("|", "%7C");
						strCauseCode = strCauseCode.replaceAll(" ", "%20");
						url = new URL(this.objComplaintBean.getEtQURL()
								+ "/datasources/WS_CAUSE_CODE_DS_P/execute?VAR$CAUSE_CODE=" + strCauseCode);

						if (url != null) {
							StringBuffer response = getConnection(url);
							JSONObject jsonObj = null;
							int cntRec = -1;

							if (response != null && response.length() > 0) {
								jsonObj = new JSONObject(response.toString());
								cntRec = Integer.parseInt(jsonObj.get("count").toString());

								if (cntRec > 0) {
									String strRec = jsonObj.get("Records").toString();
									JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));

									String strCol = jsonRecObj.get("Columns").toString();
									JSONObject jsonColObj = new JSONObject(strCol.substring(1, strCol.length() - 1));

									aryLstRtnCauseCode.add(jsonColObj.get("value").toString());
								} else {
									strCauseCode = strCauseCode.replace("%2F", "/");
									strCauseCode = strCauseCode.replace("%7C", "|");
									strCauseCode = strCauseCode.replaceAll("%20", " ");
									strWrongCauseCode = strWrongCauseCode + ", " + strCauseCode.replaceAll("%20", " ")
											.replaceAll("%2F", "/").replaceAll("%7C", "|");
									;
								}
							}
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}

		// Commented for ETQCR-690
		/*
		 * if(strErrorMsg != null && strErrorMsg.length() > 0) {
		 * this.objComplaintBean.setErrorMessage(strErrorMsg); } else
		 */
		if (strWrongCauseCode != null && strWrongCauseCode.length() > 0) {
			this.objComplaintBean
					.setErrorMessage("Error: Cause Code(s) - Display value(s): [" + strWrongCauseCode.substring(2)
							+ "] is not listed in the field's keyword options for Source Ref. No. : "
							+ objComplaintBean.getSystemSourceRefNo());
		} else // if ((strErrorMsg == null || strErrorMsg.length() == 0) && (strWrongCauseCode
				// == null || strWrongCauseCode.length() == 0)) Commented ETQCR-690
		{
			this.objComplaintBean.setCauseCode(aryLstRtnCauseCode);
		}
		return this.objComplaintBean;
	}

	// Validate Attachments
	public String validateAllAttachments() {
		String strErrorMsg = "";
		ResultSet rsAllAttachment = null;
		try {
			Connection connection = this.objComplaintBean.getBusinessUnitConnection();
			String strInterfaceRecId = this.objComplaintBean.getInterfaceRecordId();
			if (connection != null) {
				Statement stmtAttach = connection.createStatement();
				String strQryAttach = "SELECT FILE_DATA,ATTACHMENT_FIELD_NAME, FILE_NAME,GLOBAL_ATTRIBUTE1 FROM XX_COMP_INI_ATTACHMENT WHERE INTERFACE_RECORD_ID="
						+ strInterfaceRecId;

				rsAllAttachment = stmtAttach.executeQuery(strQryAttach);
				String strAttachErrorMsg = "";

				if (rsAllAttachment != null) {
					while (rsAllAttachment.next()) {
						Blob blob = rsAllAttachment.getBlob(1);
						String strAttachFieldName = rsAllAttachment.getString(2);
						String strFileName = rsAllAttachment.getString(3);
						String strAttachType = rsAllAttachment.getString(4);

						if (blob == null || blob.length() == 0) {
							strAttachErrorMsg = strAttachErrorMsg + ", File Data";
						}
						if (blob.length() > (utility.ATTACHMENT_SIZE_LIMIT * (1024 * 1024))) {
							strAttachErrorMsg = strAttachErrorMsg + ", File of size less than 10 MB";
						}

						if (strAttachFieldName == null || strAttachFieldName.length() == 0) {
							strAttachErrorMsg = strAttachErrorMsg + ", Attachment Field Name";
						}

						if (strFileName == null || strFileName.length() == 0) {
							strAttachErrorMsg = strAttachErrorMsg + ", File Name";
						}

						if (strAttachType == null || strAttachType.length() == 0) {
							strAttachErrorMsg = strAttachErrorMsg + ", Form Name";
						}

						if (strAttachErrorMsg != null && strAttachErrorMsg.length() > 0) {
							strErrorMsg = "Error: " + strAttachErrorMsg.substring(2)
									+ " field(s) is/are required for attachment ";
							break;
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}
		return strErrorMsg;
	}

	public XxComplaintInterfaceBean validateInvestigationActivityFields(XxComplaintInterfaceBean objComplaintBean) {
		this.objComplaintBean = objComplaintBean;
		// Added for MDMTESTING-4473
		String strComplanitClosureDate = this.objComplaintBean.getComplaintClosureDate();
		String strErrorMsg = new String();
		String strWillProInveSite = null;
		String strNewInfo = null;
		String strCodingCompleted = null;
		ResultSet rsInveAct = null;
		int flag = 0;
		try {
			Statement stmt = this.objComplaintBean.getBusinessUnitConnection().createStatement();
			String strInveQry = "select NEW_INFO_RECEIVED_FOR_PAE,CODE_CORR_ACTION_COMP,RETURN_INVEST_SITE,CONCLUSION_SUMMARY,"
					+ "INVESTIGATION_CLOSURE_DATE,INVESTIGATION_COMPLETED_BY,INVESTIGATION_CLOSED_BY,M_BC_MANUFACTURE_DATE,INVESTIGATION_COMPLETED_DATE,PRODUCT_RECEIPT_DATE"
					+ " from  XX_COMP_INV_ACT_INTERFACE WHERE INTERFACE_RECORD_ID='"
					+ this.objComplaintBean.getInterfaceRecordId() + "'"
					+ " and (ETQ_UPLOAD_STATUS IS NULL OR ETQ_UPLOAD_STATUS=1 OR ETQ_UPLOAD_STATUS=0) AND (ETQ_BATCH_ID IS NULL OR ETQ_BATCH_ID=0)";

			rsInveAct = stmt.executeQuery(strInveQry);

			if (rsInveAct != null) {
				while (rsInveAct.next()) {
					flag = 1;
					if (rsInveAct.getString(3) != null && rsInveAct.getString(3).length() > 0) {
						strWillProInveSite = getYesNoNoInformationValue(rsInveAct.getString(3));
						if (strWillProInveSite != null && !strWillProInveSite.equalsIgnoreCase("Yes")
								&& !strWillProInveSite.equalsIgnoreCase("No")
								&& !strWillProInveSite.equalsIgnoreCase("No Information")) {
							if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
								strErrorMsg = strErrorMsg
										+ " || Error: Will Product be Returned to Investigating Site? - Display value(s): ["
										+ rsInveAct.getString(3) + "] is not listed in the field's keyword options";
							}
						} else {
							this.objComplaintBean.setIsProductReturnToInv(strWillProInveSite);
						}
					} else if (this.objComplaintBean.getIsProductReturn() != null
							&& this.objComplaintBean.getIsProductReturn().equalsIgnoreCase("Yes")) {
						this.objComplaintBean.setIsProductReturnToInv("No Information");
					}

					if (rsInveAct.getString(1) != null && rsInveAct.getString(1).length() > 0) {
						strNewInfo = getYesNoValue(rsInveAct.getString(1));
						if (strNewInfo != null && !strNewInfo.equalsIgnoreCase("Yes")
								&& !strNewInfo.equalsIgnoreCase("No")) {
							if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
								strErrorMsg = strErrorMsg
										+ " || Error: New Information received for Potentially Adverse Event -Display value(s): ["
										+ rsInveAct.getString(1) + "] is not listed in the field's keyword options";
							}
						} else {
							this.objComplaintBean.setIsNewInvInfoRec(strNewInfo);
						}
					}

					if (rsInveAct.getString(2) != null && rsInveAct.getString(2).length() > 0) {
						strCodingCompleted = getYesNoValue(rsInveAct.getString(2));
						if (strCodingCompleted != null && !strCodingCompleted.equalsIgnoreCase("Yes")
								&& !strCodingCompleted.equalsIgnoreCase("No")) {
							if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
								strErrorMsg = strErrorMsg
										+ " || Error: Coding / Corrective Action is complete in associated Complaint? - Display value(s): ["
										+ rsInveAct.getString(1) + "] is not listed in the field's keyword options";
							}
						} else {
							this.objComplaintBean.setIsCorrectiveActionComplete(strCodingCompleted);
						}
					}

					// Added for MDMTESTING-4473 rsInveAct.getString(5) != null &&
					// rsInveAct.getString(5).length() > 0)
					if ((rsInveAct.getString(4) == null || rsInveAct.getString(4).length() == 0)
							&& (rsInveAct.getString(5) != null && rsInveAct.getString(5).length() > 0)) {
						if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
							strErrorMsg = strErrorMsg
									+ " || Error: Investigation Conclusion Summary/Results is required field ";
						}
					}

					// Added for MDMTESTING-4473
					if (strComplanitClosureDate != null && strComplanitClosureDate.length() > 0) {
						if (this.objComplaintBean.getInvestigationRequired() != null
								&& this.objComplaintBean.getInvestigationRequired().length() > 0
								&& (this.objComplaintBean.getInvestigationRequired().equalsIgnoreCase("Y")
										|| this.objComplaintBean.getInvestigationRequired().equalsIgnoreCase("Yes"))) {
							if (rsInveAct.getString(5) == null || rsInveAct.getString(5).length() == 0) {
								if ((strErrorMsg == null || strErrorMsg != null) && strErrorMsg.length() <= 3000) {
									strErrorMsg = strErrorMsg + " || Error: Investigation Activity must be completed ";
								}
							}
						}
					}

					// Added for ETQCR-950
					if (rsInveAct.getString(8) != null && rsInveAct.getString(8).length() > 0) {
						String strValidateMbcDt = validateDate("M-BC Manufacture Date", rsInveAct.getString(8));
						if (strValidateMbcDt != null && strValidateMbcDt.contains("Error")) {
							strErrorMsg = strErrorMsg + " || " + strValidateMbcDt + " ";
						}
					}

					// Added for ETQCR-950
					if (rsInveAct.getString(9) != null && rsInveAct.getString(9).length() > 0) {
						String strValidateInvCompDt = validateDate("Investigation Completion Date",
								rsInveAct.getString(9));
						if (strValidateInvCompDt != null && strValidateInvCompDt.contains("Error")) {
							strErrorMsg = strErrorMsg + " || " + strValidateInvCompDt + " ";
						}
					}

					// Added for ETQCR-950
					if (rsInveAct.getString(5) != null && rsInveAct.getString(5).length() > 0) {
						String strValidateInvClDt = validateDate("Investigation Closure Date", rsInveAct.getString(5));
						if (strValidateInvClDt != null && strValidateInvClDt.contains("Error")) {
							strErrorMsg = strErrorMsg + " || " + strValidateInvClDt + " ";
						}
					}

					// Added for ETQCR-950
					if (rsInveAct.getString(10) != null && rsInveAct.getString(10).length() > 0) {
						String strValidatePrdRecDt = validateDate("Investigation Product Receipt Date",
								rsInveAct.getString(10));
						if (strValidatePrdRecDt != null && strValidatePrdRecDt.contains("Error")) {
							strErrorMsg = strErrorMsg + " || " + strValidatePrdRecDt + " ";
						}
					}

					if (strErrorMsg != null && strErrorMsg.length() > 0) {
						strErrorMsg = strErrorMsg.substring(4) + "for System Source Ref No: "
								+ this.objComplaintBean.getSystemSourceRefNo();
						this.objComplaintBean.setErrorMessage(strErrorMsg);
						break;
					}

				}
			}

			if (flag == 0 && this.objComplaintBean.getPhaseName() != null
					&& this.objComplaintBean.getPhaseName().length() > 0
					&& this.objComplaintBean.getPhaseName().equalsIgnoreCase("COMPLAINTS_COMPLAINT_HANDLING_CLOSED")) {
				strErrorMsg = "Error: Investigation Activity Required for System Source Ref No: "
						+ this.objComplaintBean.getSystemSourceRefNo();
				this.objComplaintBean.setErrorMessage(strErrorMsg);
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
			this.objComplaintBean.setErrorMessage("Error: " + ex.getMessage());
		}
		return objComplaintBean;
	}

	// Commented for ETQCR-912
	/*
	 * public String validateEmailFormat(String strEmail) { Boolean isValid = false;
	 * String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w-]+\\.)+[\\w]+[\\w]$"; isValid
	 * = strEmail.matches(regex);
	 * 
	 * return isValid.toString(); }
	 */

	public String getDTBooleanValue(String strValue) {
		String strStatus = null;

		if (strValue != null && strValue.length() > 0) {
			if (strValue.equalsIgnoreCase("Y") || strValue.equalsIgnoreCase("Yes")) {
				strStatus = "Yes";
			} else if (strValue.equalsIgnoreCase("N") || strValue.equalsIgnoreCase("No")) {
				strStatus = "No";
			} else if (strValue.equalsIgnoreCase("U") || strValue.equalsIgnoreCase("Unknown")) {
				strStatus = "Unknown";
			} else {
				strStatus = strValue;
			}
		}
		return strStatus;
	}

	public String getYesNoNoInformationValue(String strValue) {
		String strStatus = null;

		if (strValue != null) {
			if (strValue.equalsIgnoreCase("Y") || strValue.equalsIgnoreCase("Yes")) {
				strStatus = "Yes";
			} else if (strValue.equalsIgnoreCase("N") || strValue.equalsIgnoreCase("No")) {
				strStatus = "No";
			} else if (strValue.equalsIgnoreCase("No Information")) {
				strStatus = "No Information";
			} else {
				strStatus = strValue;
			}
		}
		return strStatus;
	}

	public String getYesNoValue(String strValue) {
		String strStatus = null;

		if (strValue != null) {
			if (strValue.equalsIgnoreCase("Y") || strValue.equalsIgnoreCase("Yes")) {
				strStatus = "Yes";
			} else if (strValue.equalsIgnoreCase("N") || strValue.equalsIgnoreCase("No")) {
				strStatus = "No";
			} else {
				strStatus = strValue;
			}
		}
		return strStatus;
	}

	public Map<String, String> getComplaintModelDetails(String strComplaintId) {
		URL url = null;
		Map<String, String> jsonValMap = new HashMap<String, String>();
		try {
			url = new URL(this.objComplaintBean.getEtQURL()
					+ "/datasources/WS_SET_PRODUCT_INFO_IN_ACTIVITY_P/execute?VAR$DOC_ID=" + strComplaintId);

			if (url != null) {
				StringBuffer response = getConnection(url);

				if (response != null && response.length() > 0) {
					JSONObject jsonObj = new JSONObject(response.toString());
					int cntRec = Integer.parseInt(jsonObj.get("count").toString());

					if (cntRec > 0) {
						String strRec = jsonObj.get("Records").toString();
						JSONObject jsonRecObj = new JSONObject(strRec.substring(1, strRec.length() - 1));
						JSONObject jsonColObj = new JSONObject(jsonRecObj.toString());
						JSONArray jsonColAry = (JSONArray) jsonColObj.get("Columns");

						for (int i = 0; i < jsonColAry.length(); i++) {
							JSONObject jsonValObj = (JSONObject) jsonColAry.get(i);
							String strColNM = jsonValObj.get("name").toString();
							String strColVal = jsonValObj.get("value").toString();
							jsonValMap.put(strColNM, strColVal);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error("Error: ", ex);
		}

		return jsonValMap;
	}

	public String validateDateFormat(String strDate) {
		String strResult = "Yes";
		try {
			SimpleDateFormat sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
			sdfrmt.setLenient(false);

			Date javaDate = sdfrmt.parse(strDate);
			@SuppressWarnings("deprecation")
			int year = javaDate.getYear();
			if ((year + 1900) < 2000) {
				strResult = "No";
			}
			if (strDate.length() > 10) {
				strResult = "No";
			}
			logger.info("Expiration Date for SR. " + this.objComplaintBean.getSystemSourceRefNo() + " is: " + javaDate);
		} catch (Exception e) {
			logger.error(e.getMessage());
			strResult = "No";
		}
		return strResult;
	}

	public String validateDate(String strField, String strDate) {
		String strResult = "Yes";
		try {
			SimpleDateFormat sdfrmt = null;
			if (strDate.length() <= 10) {
				sdfrmt = new SimpleDateFormat("yyyy-MM-dd");
				sdfrmt.setLenient(false);
			} else {
				sdfrmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				sdfrmt.setLenient(false);
			}

			Date javaDate = sdfrmt.parse(strDate);
			@SuppressWarnings("deprecation")
			int year = javaDate.getYear();
			// Modified for ETQTESTING-79812
			if ((year + 1900) <= 1900) {
				strResult = "Error: Invalid " + strField + " value[" + strDate + "] is provided ";
				logger.info(strResult);
			}
			if (strResult.equalsIgnoreCase("Yes")) {
				String currDate = getCurrentDate(this.objComplaintBean.getTimeZones());
				String localDate = getDateByTimeZone(strDate, this.objComplaintBean.getTimeZones());

				if (new SimpleDateFormat("MMM dd, yyyy hh:mm a").parse(localDate)
						.after(new SimpleDateFormat("MMM dd, yyyy hh:mm a").parse(currDate))) {
					strResult = "Error: " + strField + "[" + strDate + "] can not be future ";
					logger.info(strResult);
				}
			}
		} catch (Exception e) {
			logger.error("", e);
			strResult = "Error: " + e.getMessage();
		}
		return strResult;
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
				this.objComplaintBean.getDateFormat().setTimeZone(toTimeZone);
				strConvertedDt = this.objComplaintBean.getDateFormat().format(currentDate);
			}
		} catch (Exception e) {
			logger.error("Error During Timezone Conversion: ", e);
		}
		return strConvertedDt;
	}

	public String getCurrentDate(String strTimeZones) throws ParseException {
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

	// Added for ETQCR-1024
	public String compareDate(String strFirstDate, String strSecondDate) {
		String strResult = "Yes";
		try {
			String firstDate = getDateByTimeZone(strFirstDate, this.objComplaintBean.getTimeZones());
			String secondDate = getDateByTimeZone(strSecondDate, this.objComplaintBean.getTimeZones());

			if (new SimpleDateFormat("MMM dd, yyyy hh:mm a").parse(firstDate)
					.before(new SimpleDateFormat("MMM dd, yyyy hh:mm a").parse(secondDate))) {
				strResult = "Error: [" + firstDate + "] shall not be prior than[" + secondDate + "]";
				logger.error(strResult);
			}
		} catch (Exception e) {
			logger.error("", e);
			strResult = "Error: " + e.getMessage();
		}
		return strResult;
	}
}