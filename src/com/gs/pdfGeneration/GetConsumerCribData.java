package com.gs.pdfGeneration;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.gs.cribObj.CFForLast24Months;
import com.gs.cribObj.CFOfGlanceStatus;
import com.gs.cribObj.CatalogueDescription;
import com.gs.cribObj.ConsumerCribObject;
import com.gs.cribObj.CreditFacilityDetails;
import com.gs.cribObj.DemographicDetails;
import com.gs.cribObj.DishonChequeDetails;
import com.gs.cribObj.DishonChequeSummary;
import com.gs.cribObj.DisputeDetails;
import com.gs.cribObj.EmploymentDetails;
import com.gs.cribObj.InquiriesBySubject;
import com.gs.cribObj.Last24Months;
import com.gs.cribObj.Last5Years;
import com.gs.cribObj.LendingInstInquiries;
import com.gs.cribObj.MailingAddresses;
import com.gs.cribObj.PermanentAddresses;
import com.gs.cribObj.PotAndCurrLiabilities;
import com.gs.cribObj.RelationshipDetails;
import com.gs.cribObj.ReportedNames;
import com.gs.cribObj.RequestMaster;
import com.gs.cribObj.SettledCFDetails;
import com.gs.cribObj.SettledCFSummary;

public class GetConsumerCribData {
	
	public static Logger log = Logger.getLogger(GetConsumerCribData.class);
	
	DemographicDetails demographicDetails;
	List<MailingAddresses> mailingAddressesList;
	List<PermanentAddresses> permanentAddressesList;
	List<ReportedNames> reportedNamesList;
	List<EmploymentDetails> employmentDetailsList;
	List<RelationshipDetails> relationshipDetailsList;
	List<SettledCFDetails> settledCFDetailsList;
	List<SettledCFSummary> settledCFSummaryList;
	List<LendingInstInquiries> lendingInstInquiriesList;
	List<InquiriesBySubject> inquiriesBySubjectList;
	List<CreditFacilityDetails> creditFacilityDetailsList;
	List<CFForLast24Months> cFForLast24MonthsList;
	List<DisputeDetails> disputeDetailsList;
	List<PotAndCurrLiabilities> potAndCurrLiabilitiesList;
	List<CFOfGlanceStatus> cFOfGlanceStatusList;
	List<DishonChequeSummary> dishonChequeSummaryList;
	List<DishonChequeDetails> dishonChequeDetailsList;
	List<CatalogueDescription> catalogueDescriptionList;
	Last24Months last24Months;
	Last5Years last5Years;
	RequestMaster requestMaster;

	// Get consumer data
	public ConsumerCribObject getConsumerCribDataByDetailId(Connection conn, int requestDetailId) throws SQLException, Exception {
	 	
		ConsumerCribObject consumerObj = new ConsumerCribObject();
		
		CallableStatement call = conn.prepareCall("{call GET_CON_CRIB_DATA(?)}");
		call.setInt(1, requestDetailId);
		call.execute();
		
		ResultSet demographicResultSet = call.getResultSet();
		while (demographicResultSet.next()) {
			demographicDetails = new DemographicDetails();
			demographicDetails.setREPORT_NUMBER((demographicResultSet.getObject(1)) != null ? demographicResultSet.getObject(1).toString() : "");
			demographicDetails.setREPORT_ORDER_DATE((demographicResultSet.getObject(2)) != null ? formateDate(demographicResultSet.getObject(2).toString()) : "-");
			demographicDetails.setPRODUCT_NAME((demographicResultSet.getObject(3)) != null ? demographicResultSet.getObject(3).toString() : "-");
			
			Map<String, String> reasonsMap = new HashMap<String, String>() {{
		        put("59", "Evaluating of a borrower for a new credit facility");
		        put("60", "Review as a Guarantor for a new credit facility");
		        put("61", "Review as a partner/proprietor for a new credit facility");
		        put("62", "Review as a director for a new credit facility");
		        put("63", "Opening of a Current Account");
		        put("64", "Monitoring and reviewing of an existing borrower");
		        put("999", "-");
		    }};
		    
			demographicDetails.setREASON(reasonsMap.get(demographicResultSet.getObject(4) != null ? demographicResultSet.getObject(4).toString() : "999"));
			demographicDetails.setNAME(demographicResultSet.getObject(5) != null ? demographicResultSet.getObject(5).toString() : "");
			demographicDetails.setNIC_NUMBER((demographicResultSet.getObject(6)) != null ? (demographicResultSet.getObject(6).toString()) : "");
			demographicDetails.setPASSPORT_NUMBER((demographicResultSet.getObject(7)) != null ? (demographicResultSet.getObject(7).toString()) : "");
			demographicDetails.setDOB((demographicResultSet.getObject(8)) != null ? formateDate(demographicResultSet.getObject(8).toString()) : "");
			demographicDetails.setGENDER((demographicResultSet.getObject(9)) != null ? (demographicResultSet.getObject(9).toString().equalsIgnoreCase("001") ? "Male" : "Female") : "");
			demographicDetails.setCITIZENSHIP((demographicResultSet.getObject(10)) != null ? (demographicResultSet.getObject(10).toString().equalsIgnoreCase("001") ? "Sri Lankan Citizen" : "Non Sri Lankan Citizen") : "");
			demographicDetails.setMARITAL_STATUS(demographicResultSet.getObject(11) != null ? demographicResultSet.getObject(11).toString() : "");
			demographicDetails.setSPOUSE_NAME(demographicResultSet.getObject(12) != null ? demographicResultSet.getObject(12).toString() : "");
			demographicDetails.setTELEPHONE_NUMBER(demographicResultSet.getObject(13) != null ? demographicResultSet.getObject(13).toString() : "");
			demographicDetails.setMOBILE_NUMBER(demographicResultSet.getObject(14) != null ? demographicResultSet.getObject(14).toString() : "");
		}
		consumerObj.setDemographicDetails(demographicDetails);
		demographicResultSet.close();
		
		call.getMoreResults();
		ResultSet mailingAddressResultSet = call.getResultSet();
		mailingAddressesList = new ArrayList<MailingAddresses>();
		while (mailingAddressResultSet.next()) {
			MailingAddresses mailingAddress = new MailingAddresses();
			mailingAddress.setMNO((mailingAddressResultSet.getObject(1)) != null ? mailingAddressResultSet.getObject(1).toString() : "");
			mailingAddress.setADDRESS(mailingAddressResultSet.getObject(2) != null ? mailingAddressResultSet.getObject(2).toString() : "");
			mailingAddress.setREPORTED_DATE((mailingAddressResultSet.getObject(3)) != null ? formateDate(mailingAddressResultSet.getObject(3).toString()) : "");
			mailingAddressesList.add(mailingAddress);
		}
		consumerObj.setMailingAddresses(mailingAddressesList);
		mailingAddressResultSet.close();
		
		call.getMoreResults();
		ResultSet permanentAddressResultSet = call.getResultSet();
		permanentAddressesList = new ArrayList<PermanentAddresses>();
		while (permanentAddressResultSet.next()) {
			PermanentAddresses permanentAddresses = new PermanentAddresses();
			permanentAddresses.setPNO((permanentAddressResultSet.getObject(1)) != null ? permanentAddressResultSet.getObject(1).toString() : "");
			permanentAddresses.setADDRESS(permanentAddressResultSet.getObject(2) != null ? permanentAddressResultSet.getObject(2).toString() : "");
			permanentAddresses.setREPORTED_DATE((permanentAddressResultSet.getObject(3)) != null ? formateDate(permanentAddressResultSet.getObject(3).toString()) : "");
			permanentAddressesList.add(permanentAddresses);
		}
		consumerObj.setPermanentAddresses(permanentAddressesList);
		permanentAddressResultSet.close();
		
		call.getMoreResults();
		ResultSet reportedNameResultSet = call.getResultSet();
		reportedNamesList = new ArrayList<ReportedNames>();
		while (reportedNameResultSet.next()) {
			ReportedNames reportedNames = new ReportedNames();
			reportedNames.setSNO((reportedNameResultSet.getObject(1)) != null ? reportedNameResultSet.getObject(1).toString() : "");
			reportedNames.setREPORTED_INSTITUTION(reportedNameResultSet.getObject(2) != null ? reportedNameResultSet.getObject(2).toString() : "");
			reportedNames.setNAME(reportedNameResultSet.getObject(3) != null ? reportedNameResultSet.getObject(3).toString() : "");
			reportedNames.setREPORTED_DATE((reportedNameResultSet.getObject(4)) != null ? formateDate(reportedNameResultSet.getObject(4).toString()) : "");
			reportedNamesList.add(reportedNames);
		}
		consumerObj.setReportedNames(reportedNamesList);
		reportedNameResultSet.close();
		
		call.getMoreResults();
		ResultSet employmentResultSet = call.getResultSet();
		employmentDetailsList = new ArrayList<EmploymentDetails>();
		while (employmentResultSet.next()) {
			EmploymentDetails employmentDetail = new EmploymentDetails();
			employmentDetail.setEMPLOYMENT(employmentResultSet.getObject(1) != null ? employmentResultSet.getObject(1).toString() : "");
			employmentDetail.setPROFESSION(employmentResultSet.getObject(2) != null ? employmentResultSet.getObject(2).toString() : "");
			employmentDetail.setEMPLOYER_NAME(employmentResultSet.getObject(3) != null ? employmentResultSet.getObject(3).toString() : "");
			employmentDetail.setBUSINESS_NAME(employmentResultSet.getObject(4) != null ? employmentResultSet.getObject(4).toString() : "");
			employmentDetail.setBR_NUMBER(employmentResultSet.getObject(5) != null ? employmentResultSet.getObject(5).toString() : "");
			employmentDetail.setREPORTED_DATE((employmentResultSet.getObject(6)) != null ? formateDate(employmentResultSet.getObject(6).toString()) : "");
			employmentDetailsList.add(employmentDetail);
		}
		consumerObj.setEmploymentDetails(employmentDetailsList);
		employmentResultSet.close();
		
		call.getMoreResults();
		ResultSet relationshipResultSet = call.getResultSet();
		relationshipDetailsList = new ArrayList<RelationshipDetails>();
		while (relationshipResultSet.next()) {
			RelationshipDetails relationshipDetail = new RelationshipDetails();
			relationshipDetail.setRNO((relationshipResultSet.getObject(1)) != null ? relationshipResultSet.getObject(1).toString() : "");
			relationshipDetail.setENTITY_ID(relationshipResultSet.getObject(2) != null ? relationshipResultSet.getObject(2).toString() : "");
			relationshipDetail.setENTITY_NAME(relationshipResultSet.getObject(3) != null ? relationshipResultSet.getObject(3).toString() : "");
			relationshipDetail.setNATURE_OF_RELATIONSHIP(relationshipResultSet.getObject(4) != null ? relationshipResultSet.getObject(4).toString() : "");
			relationshipDetailsList.add(relationshipDetail);
		}
		consumerObj.setRelationshipDetails(relationshipDetailsList);
		relationshipResultSet.close();
		
		call.getMoreResults();
		ResultSet settledCFDetailsResultSet = call.getResultSet();
		settledCFDetailsList = new ArrayList<SettledCFDetails>();
		while (settledCFDetailsResultSet.next()) {
			SettledCFDetails settledCFDetail = new SettledCFDetails();
			settledCFDetail.setCURRENCY(settledCFDetailsResultSet.getObject(1) != null ? settledCFDetailsResultSet.getObject(1).toString() : "");
			settledCFDetail.setCF_TYPE(settledCFDetailsResultSet.getObject(2) != null ? settledCFDetailsResultSet.getObject(2).toString() : "");
			settledCFDetail.setNO_OF_CREDIT_FACILITIES_AS_BORROWER((settledCFDetailsResultSet.getObject(3)) != null ? settledCFDetailsResultSet.getObject(3).toString() : "");
			settledCFDetail.setAMOUNT_GRANTED_AS_BORROWER((settledCFDetailsResultSet.getObject(4)) != null ? formateAmounts(settledCFDetailsResultSet.getObject(4).toString()) : "");
			settledCFDetail.setNO_OF_CREDIT_FACILITIES_AS_GUARANTOR((settledCFDetailsResultSet.getObject(5)) != null ? settledCFDetailsResultSet.getObject(5).toString() : "");
			settledCFDetail.setAMOUNT_GRANTED_AS_GUARANTOR((settledCFDetailsResultSet.getObject(6)) != null ? formateAmounts(settledCFDetailsResultSet.getObject(6).toString()) : "");
			settledCFDetailsList.add(settledCFDetail);
		}
		consumerObj.setSettledCFDetails(settledCFDetailsList);
		settledCFDetailsResultSet.close();
		
		call.getMoreResults();
		ResultSet lendingInstInquiryResultSet = call.getResultSet();
		lendingInstInquiriesList = new ArrayList<LendingInstInquiries>();
		while (lendingInstInquiryResultSet.next()) {
			LendingInstInquiries lendingInstInquiry = new LendingInstInquiries();
			lendingInstInquiry.setINO((lendingInstInquiryResultSet.getObject(1)) != null ? lendingInstInquiryResultSet.getObject(1).toString() : "");
			lendingInstInquiry.setINQUIRY_DATE((lendingInstInquiryResultSet.getObject(2)) != null ? formateDate(lendingInstInquiryResultSet.getObject(2).toString()) : "");
			lendingInstInquiry.setINSTITUTION_NAME(lendingInstInquiryResultSet.getObject(3) != null ? lendingInstInquiryResultSet.getObject(3).toString() : "");
			lendingInstInquiry.setBRANCH_NAME(lendingInstInquiryResultSet.getObject(4) != null ? lendingInstInquiryResultSet.getObject(4).toString() : "");
			lendingInstInquiry.setREASON(lendingInstInquiryResultSet.getObject(5) != null ? lendingInstInquiryResultSet.getObject(5).toString() : "");
			lendingInstInquiry.setCF_TYPE(lendingInstInquiryResultSet.getObject(6) != null ? lendingInstInquiryResultSet.getObject(6).toString() : "");
			lendingInstInquiry.setCURRENCY(lendingInstInquiryResultSet.getObject(7) != null ? lendingInstInquiryResultSet.getObject(7).toString() : "");
			lendingInstInquiry.setAMOUNT((lendingInstInquiryResultSet.getObject(8)) != null ? formateAmounts(lendingInstInquiryResultSet.getObject(8).toString()) : "");
			lendingInstInquiriesList.add(lendingInstInquiry);
		}
		consumerObj.setLendingInstInquiries(lendingInstInquiriesList);
		lendingInstInquiryResultSet.close();
		
		call.getMoreResults();
		ResultSet inquiriesBySubjectResultSet = call.getResultSet();
		inquiriesBySubjectList = new ArrayList<InquiriesBySubject>();
		while (inquiriesBySubjectResultSet.next()) {
			InquiriesBySubject inquiriesBySubject = new InquiriesBySubject();
			inquiriesBySubject.setINO((inquiriesBySubjectResultSet.getObject(1)) != null ? inquiriesBySubjectResultSet.getObject(1).toString() : "");
			inquiriesBySubject.setINQUIRY_DATE((inquiriesBySubjectResultSet.getObject(2)) != null ? formateDate(inquiriesBySubjectResultSet.getObject(2).toString()) : "");
			inquiriesBySubject.setREASON(inquiriesBySubjectResultSet.getObject(3) != null ? inquiriesBySubjectResultSet.getObject(3).toString() : "");
			inquiriesBySubjectList.add(inquiriesBySubject);
		}
		consumerObj.setInquiriesBySubject(inquiriesBySubjectList);
		inquiriesBySubjectResultSet.close();
		
		call.getMoreResults();
		ResultSet creditFacilityDetailResultSet = call.getResultSet();
		creditFacilityDetailsList = new ArrayList<CreditFacilityDetails>();
		while (creditFacilityDetailResultSet.next()) {
			CreditFacilityDetails creditFacilityDetail = new CreditFacilityDetails();
			creditFacilityDetail.setCNO((creditFacilityDetailResultSet.getObject(1)) != null ? creditFacilityDetailResultSet.getObject(1).toString() : "");
			creditFacilityDetail.setINSTITUTION_CATG(creditFacilityDetailResultSet.getObject(2) != null ? creditFacilityDetailResultSet.getObject(2).toString() : "");
			creditFacilityDetail.setINSTITUTION_BRANCH(creditFacilityDetailResultSet.getObject(3) != null ? creditFacilityDetailResultSet.getObject(3).toString() : "");				
			creditFacilityDetail.setCF_TYPE(creditFacilityDetailResultSet.getObject(4) != null ? creditFacilityDetailResultSet.getObject(4).toString() : "");
			creditFacilityDetail.setCF_STATUS(creditFacilityDetailResultSet.getObject(5) != null ? creditFacilityDetailResultSet.getObject(5).toString() : "");
			creditFacilityDetail.setOWNERSHIP(creditFacilityDetailResultSet.getObject(6) != null ? creditFacilityDetailResultSet.getObject(6).toString() : "");
			creditFacilityDetail.setCURRENCY(creditFacilityDetailResultSet.getObject(7) != null ? creditFacilityDetailResultSet.getObject(7).toString() : "");
			creditFacilityDetail.setAMOUNT_GRANTED((creditFacilityDetailResultSet.getObject(8)) != null ? formateAmounts(creditFacilityDetailResultSet.getObject(8).toString()) : "");
			creditFacilityDetail.setCURRENT_BALANCE((creditFacilityDetailResultSet.getObject(9)) != null ? formateAmounts(creditFacilityDetailResultSet.getObject(9).toString()) : "");
			creditFacilityDetail.setARREAS_AMOUNT((creditFacilityDetailResultSet.getObject(10)) != null ? formateAmounts(creditFacilityDetailResultSet.getObject(10).toString()) : "");
			creditFacilityDetail.setINSTALLMENT_AMOUNT((creditFacilityDetailResultSet.getObject(11)) != null ? formateAmounts(creditFacilityDetailResultSet.getObject(11).toString()) : "");
			creditFacilityDetail.setAMOUNT_WRITTEN_OFF((creditFacilityDetailResultSet.getObject(12)) != null ? formateAmounts(creditFacilityDetailResultSet.getObject(12).toString()) : "");
			creditFacilityDetail.setREPORTED_DATE((creditFacilityDetailResultSet.getObject(13)) != null ? formateDate(creditFacilityDetailResultSet.getObject(13).toString()) : "");
			creditFacilityDetail.setFIRST_DISBURSE_DATE((creditFacilityDetailResultSet.getObject(14)) != null ? formateDate(creditFacilityDetailResultSet.getObject(14).toString()) : "");
			creditFacilityDetail.setLATEST_PAYMENT_DATE((creditFacilityDetailResultSet.getObject(15)) != null ? formateDate(creditFacilityDetailResultSet.getObject(15).toString()) : "");
			creditFacilityDetail.setRESTRUCTURING_DATE((creditFacilityDetailResultSet.getObject(16)) != null ? formateDate(creditFacilityDetailResultSet.getObject(16).toString()) : "");
			creditFacilityDetail.setEND_DATE((creditFacilityDetailResultSet.getObject(17)) != null ? formateDate(creditFacilityDetailResultSet.getObject(17).toString()) : "");
			creditFacilityDetail.setREPAY_TYPE(creditFacilityDetailResultSet.getObject(18) != null ? creditFacilityDetailResultSet.getObject(18).toString() : "");
			creditFacilityDetail.setPURPOSE(creditFacilityDetailResultSet.getObject(19) != null ? creditFacilityDetailResultSet.getObject(19).toString() : "");
			creditFacilityDetail.setCOVERAGE(creditFacilityDetailResultSet.getObject(20) != null ? creditFacilityDetailResultSet.getObject(20).toString() : "");
			creditFacilityDetailsList.add(creditFacilityDetail);
		}
		consumerObj.setCreditFacilityDetails(creditFacilityDetailsList);
		creditFacilityDetailResultSet.close();
		
		call.getMoreResults();
		ResultSet cFForLast24MonthsResultSet = call.getResultSet();
		cFForLast24MonthsList = new ArrayList<CFForLast24Months>();
		int i = 1;
		while (cFForLast24MonthsResultSet.next()) {
			CFForLast24Months cFForLast24Months = new CFForLast24Months();
			cFForLast24Months.setCNO(String.valueOf(i));
			cFForLast24Months.setMONTH_1(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_1_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_2(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_2_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_3(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_3_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_4(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_4_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_5(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_5_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_6(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_6_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_7(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_7_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_8(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_8_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_9(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_9_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_10(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_10_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_11(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_11_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_12(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_12_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_13(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_13_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_14(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_14_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_15(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_15_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_16(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_16_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_17(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_17_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_18(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_18_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_19(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_19_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_20(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_20_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_21(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_21_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_22(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_22_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_23(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_23_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsResultSet.next();
			cFForLast24Months.setMONTH_24(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : "");
			cFForLast24Months.setMONTH_24_COLOR(getCellColor(cFForLast24MonthsResultSet.getObject(1) != null ? cFForLast24MonthsResultSet.getObject(1).toString() : ""));
			cFForLast24MonthsList.add(cFForLast24Months);
			i++;
		}
		consumerObj.setcFForLast24Months(cFForLast24MonthsList);
		cFForLast24MonthsResultSet.close();
		
		call.getMoreResults();
		ResultSet cFOfGlanceStatusResultSet = call.getResultSet();
		cFOfGlanceStatusList = new ArrayList<CFOfGlanceStatus>();
		while (cFOfGlanceStatusResultSet.next()) {
			CFOfGlanceStatus cFOfGlanceStatus = new CFOfGlanceStatus();
			cFOfGlanceStatus.setSTATUS(cFOfGlanceStatusResultSet.getObject(1) != null ? cFOfGlanceStatusResultSet.getObject(1).toString() : "");
			cFOfGlanceStatus.setARREAS_DAYS_0((cFOfGlanceStatusResultSet.getObject(2)) != null ? cFOfGlanceStatusResultSet.getObject(2).toString() : "");
			cFOfGlanceStatus.setARREAS_DAYS_1_30((cFOfGlanceStatusResultSet.getObject(3)) != null ? cFOfGlanceStatusResultSet.getObject(3).toString() : "");
			cFOfGlanceStatus.setARREAS_DAYS_31_60((cFOfGlanceStatusResultSet.getObject(4)) != null ? cFOfGlanceStatusResultSet.getObject(4).toString() : "");
			cFOfGlanceStatus.setARREAS_DAYS_61_90((cFOfGlanceStatusResultSet.getObject(5)) != null ? cFOfGlanceStatusResultSet.getObject(5).toString() : "");
			cFOfGlanceStatus.setARREAS_DAYS_OVER_90((cFOfGlanceStatusResultSet.getObject(6)) != null ? cFOfGlanceStatusResultSet.getObject(6).toString() : "");
			cFOfGlanceStatusList.add(cFOfGlanceStatus);
		}
		consumerObj.setcFOfGlanceStatus(cFOfGlanceStatusList);
		cFOfGlanceStatusResultSet.close();
		
		call.getMoreResults();
		ResultSet dishonChequeDetailResultSet = call.getResultSet();
		dishonChequeDetailsList = new ArrayList<DishonChequeDetails>();
		while (dishonChequeDetailResultSet.next()) {
			DishonChequeDetails dishonChequeDetail = new DishonChequeDetails();
			dishonChequeDetail.setINSTITUTION_AND_BRANCH(dishonChequeDetailResultSet.getObject(1) != null ? dishonChequeDetailResultSet.getObject(1).toString() : "");
			dishonChequeDetail.setCHEQUE_NUMBER(dishonChequeDetailResultSet.getObject(2) != null ? dishonChequeDetailResultSet.getObject(2).toString() : "");
			dishonChequeDetail.setCHEQUE_AMOUNT((dishonChequeDetailResultSet.getObject(3)) != null ? formateAmounts(dishonChequeDetailResultSet.getObject(3).toString()) : "");
			dishonChequeDetail.setDISHONOURED_DATE((dishonChequeDetailResultSet.getObject(4)) != null ? formateDate(dishonChequeDetailResultSet.getObject(4).toString()) : "");
			dishonChequeDetail.setREASON(dishonChequeDetailResultSet.getObject(5) != null ? dishonChequeDetailResultSet.getObject(5).toString() : "");
			dishonChequeDetailsList.add(dishonChequeDetail);
		}
		consumerObj.setDishonChequeDetails(dishonChequeDetailsList);
		dishonChequeDetailResultSet.close();
					
		call.getMoreResults();
		ResultSet dishonChequeSummaryResultSet = call.getResultSet();
		dishonChequeSummaryList = new ArrayList<DishonChequeSummary>();
		while (dishonChequeSummaryResultSet.next()) {
			DishonChequeSummary dishonChequeSummary = new DishonChequeSummary();
			dishonChequeSummary.setNUMBER_OF_CHEQUES((dishonChequeSummaryResultSet.getObject(1)) != null ? dishonChequeSummaryResultSet.getObject(1).toString() : "");
			dishonChequeSummary.setCHEQUE_VALUE((dishonChequeSummaryResultSet.getObject(2)) != null ? formateAmounts(dishonChequeSummaryResultSet.getObject(2).toString()) : "");
			dishonChequeSummaryList.add(dishonChequeSummary);
		}
		consumerObj.setDishonChequeSummary(dishonChequeSummaryList);
		dishonChequeSummaryResultSet.close();
		
		call.getMoreResults();
		ResultSet potAndCurrLiabilityResultSet = call.getResultSet();
		potAndCurrLiabilitiesList = new ArrayList<PotAndCurrLiabilities>();
		while (potAndCurrLiabilityResultSet.next()) {
			PotAndCurrLiabilities potAndCurrLiability = new PotAndCurrLiabilities();
			potAndCurrLiability.setCURRENCY(potAndCurrLiabilityResultSet.getObject(1) != null ? potAndCurrLiabilityResultSet.getObject(1).toString() : "");
			potAndCurrLiability.setOWNERSHIP(potAndCurrLiabilityResultSet.getObject(2) != null ? potAndCurrLiabilityResultSet.getObject(2).toString() : "");
			potAndCurrLiability.setNO_OF_CREDIT_FACILITIES((potAndCurrLiabilityResultSet.getObject(3)) != null ? potAndCurrLiabilityResultSet.getObject(3).toString() : "");
			potAndCurrLiability.setTOTAL_AMOUNT_GRANTED_LIMIT((potAndCurrLiabilityResultSet.getObject(4)) != null ? formateAmounts(potAndCurrLiabilityResultSet.getObject(4).toString()) : "");
			potAndCurrLiability.setTOTAL_OUTSTANDING((potAndCurrLiabilityResultSet.getObject(5)) != null ? formateAmounts(potAndCurrLiabilityResultSet.getObject(5).toString()) : "");
			potAndCurrLiabilitiesList.add(potAndCurrLiability);
		}
		consumerObj.setPotAndCurrLiabilities(potAndCurrLiabilitiesList);
		potAndCurrLiabilityResultSet.close();
				
		call.getMoreResults();
		ResultSet settledCFSummaryResultSet = call.getResultSet();
		settledCFSummaryList = new ArrayList<SettledCFSummary>();
		while (settledCFSummaryResultSet.next()) {
			SettledCFSummary settledCFSummary = new SettledCFSummary();
			settledCFSummary.setCURRENCY(settledCFSummaryResultSet.getObject(1) != null ? settledCFSummaryResultSet.getObject(1).toString() : "");
			settledCFSummary.setOWNERSHIP(settledCFSummaryResultSet.getObject(2) != null ? settledCFSummaryResultSet.getObject(2).toString() : "");
			settledCFSummary.setNO_OF_CREDIT_FACILITIES_1((settledCFSummaryResultSet.getObject(3)) != null ? settledCFSummaryResultSet.getObject(3).toString() : "");
			settledCFSummary.setAMOUNT_GRANTED_1((settledCFSummaryResultSet.getObject(4)) != null ? formateAmounts(settledCFSummaryResultSet.getObject(4).toString()) : "");
			settledCFSummary.setNO_OF_CREDIT_FACILITIES_2((settledCFSummaryResultSet.getObject(5)) != null ? settledCFSummaryResultSet.getObject(5).toString() : "");
			settledCFSummary.setAMOUNT_GRANTED_2((settledCFSummaryResultSet.getObject(6)) != null ? formateAmounts(settledCFSummaryResultSet.getObject(6).toString()) : "");
			settledCFSummary.setNO_OF_CREDIT_FACILITIES_3((settledCFSummaryResultSet.getObject(7)) != null ? settledCFSummaryResultSet.getObject(7).toString() : "");
			settledCFSummary.setAMOUNT_GRANTED_3((settledCFSummaryResultSet.getObject(8)) != null ? formateAmounts(settledCFSummaryResultSet.getObject(8).toString()) : "");
			settledCFSummary.setNO_OF_CREDIT_FACILITIES_4((settledCFSummaryResultSet.getObject(9)) != null ? settledCFSummaryResultSet.getObject(9).toString() : "");
			settledCFSummary.setAMOUNT_GRANTED_4((settledCFSummaryResultSet.getObject(10)) != null ? formateAmounts(settledCFSummaryResultSet.getObject(10).toString()) : "");
			settledCFSummary.setNO_OF_CREDIT_FACILITIES_5((settledCFSummaryResultSet.getObject(11)) != null ? settledCFSummaryResultSet.getObject(11).toString() : "");
			settledCFSummary.setAMOUNT_GRANTED_5((settledCFSummaryResultSet.getObject(12)) != null ? formateAmounts(settledCFSummaryResultSet.getObject(12).toString()) : "");
			settledCFSummaryList.add(settledCFSummary);
		}
		consumerObj.setSettledCFSummary(settledCFSummaryList);
		settledCFSummaryResultSet.close();
			
		call.getMoreResults();
		ResultSet catalogueDescriptionResultSet = call.getResultSet();
		catalogueDescriptionList = new ArrayList<CatalogueDescription>();
		while (catalogueDescriptionResultSet.next()) {
			CatalogueDescription catalogueDescription = new CatalogueDescription();
			catalogueDescription.setCATG_LABEL(catalogueDescriptionResultSet.getObject(1) != null ? catalogueDescriptionResultSet.getObject(1).toString() : "");
			catalogueDescription.setCATG_VALUE(catalogueDescriptionResultSet.getObject(2) != null ? catalogueDescriptionResultSet.getObject(2).toString() : "");
			catalogueDescriptionList.add(catalogueDescription);
		}
		consumerObj.setCatalogueDescription(catalogueDescriptionList);
		catalogueDescriptionResultSet.close();	
			
		call.getMoreResults();
		ResultSet requestMasterResultSet = call.getResultSet();
		while (requestMasterResultSet.next()) {
			requestMaster = new RequestMaster();
			requestMaster.setREQUEST_NUMBER(requestMasterResultSet.getObject(1) != null ? requestMasterResultSet.getObject(1).toString() : "");
			requestMaster.setINITIATOR_NAME(requestMasterResultSet.getObject(2) != null ? requestMasterResultSet.getObject(2).toString() : "");
			requestMaster.setNAME((requestMasterResultSet.getObject(3)) != null ? requestMasterResultSet.getObject(3).toString() : "");
			requestMaster.setCUSTOMER_ID(requestMasterResultSet.getObject(4) != null ? requestMasterResultSet.getObject(4).toString() : "");				
			requestMaster.setGENDER((requestMasterResultSet.getObject(5)) != null ? (requestMasterResultSet.getObject(5).toString().equalsIgnoreCase("001") ? "Male" : "Female") : "");
		    }
			consumerObj.setRequestMaster(requestMaster);
			requestMasterResultSet.close();
			
		call.getMoreResults();
		ResultSet last24MonthsResultSet = call.getResultSet();
		while (last24MonthsResultSet.next()) {
			last24Months = new Last24Months();
			last24Months.setMONTH_1(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_2(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_3(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_4(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_5(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_6(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_7(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_8(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_9(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_10(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_11(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_12(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_13(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_14(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_15(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_16(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_17(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_18(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_19(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_20(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_21(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_22(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_23(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");
			last24MonthsResultSet.next();
			last24Months.setMONTH_24(last24MonthsResultSet.getObject(1) != null ? last24MonthsResultSet.getObject(1).toString() : "");				
	    }
		consumerObj.setLast24Months(last24Months);
		last24MonthsResultSet.close();
		
		call.getMoreResults();
		ResultSet last5YearsResultSet = call.getResultSet();
		while (last5YearsResultSet.next()) {
			last5Years = new Last5Years();
			last5Years.setYEAR_1(last5YearsResultSet.getObject(1) != null ? last5YearsResultSet.getObject(1).toString() : "");
			last5Years.setYEAR_2(last5YearsResultSet.getObject(2) != null ? last5YearsResultSet.getObject(2).toString() : "");
			last5Years.setYEAR_3(last5YearsResultSet.getObject(3) != null ? last5YearsResultSet.getObject(3).toString() : "");
			last5Years.setYEAR_4(last5YearsResultSet.getObject(4) != null ? last5YearsResultSet.getObject(4).toString() : "");
			last5Years.setYEAR_5(last5YearsResultSet.getObject(5) != null ? last5YearsResultSet.getObject(5).toString() : "");			
	    }
		consumerObj.setLast5Years(last5Years);
		last5YearsResultSet.close();
		
		call.getMoreResults();
		ResultSet disputeDetailResultSet = call.getResultSet();
		disputeDetailsList = new ArrayList<DisputeDetails>();
		while (disputeDetailResultSet.next()) {
			DisputeDetails disputeDetail = new DisputeDetails();
			disputeDetail.setDESCRIPTION(disputeDetailResultSet.getObject(1) != null ? disputeDetailResultSet.getObject(1).toString() : "");
			disputeDetailsList.add(disputeDetail);
	    }
		consumerObj.setDisputeDetails(disputeDetailsList);
		disputeDetailResultSet.close();	 	
				
		return consumerObj;
	}
	 
	 public String getCellColor(String value){
		 String color = "background:white";
		 if(value != null  && !value.equals("") && !value.equals("--") && !value.equals("ND") && !value.equals("Cls")){
			 if(!value.matches("-?\\d+(\\.\\d+)?")) {
				 value = value.trim();
				 if(value.equalsIgnoreCase("OK")){
					 color = "background:#88cc00";
				 }else{	
					 //color = "background:darkcyan; color:white";
					 color = "background:white";
				 }			 
			 }else{
				 int number = Integer.parseInt(value);
				 if(number > 90){        
					 color = "background:#e60000; color:white";
				 }else if (number <= 90){
					 color = "background:yellow";
				 }
			 }
		 }	 
		return color + "; text-align: center";	
	}
	 
	public String formateDate(String date) {
        if(date != null && date.trim().length() != 0){
        	date = date.substring(0,10);
        	String day = date.split("-")[2];
        	String month = date.split("-")[1];
        	String year = date.split("-")[0];
        	date = day+"/"+month+"/"+year;
        } else {
        	date = "";
        }  
		return date;		
	}
	
	public String formateAmounts(String amount) {
		if(amount != null && amount.trim().length() != 0){
			double d = Double.parseDouble(amount);
			NumberFormat nf = NumberFormat.getCurrencyInstance();
			DecimalFormatSymbols decimalFormatSymbols = ((DecimalFormat) nf).getDecimalFormatSymbols();
			// show hide currency symbol
			decimalFormatSymbols.setCurrencySymbol("");
			((DecimalFormat) nf).setDecimalFormatSymbols(decimalFormatSymbols);
			// show hide decimal points
			nf.setMaximumFractionDigits(0);
			return nf.format(d).trim();
		}else {
			return amount;
		}
	}
	 	
}
