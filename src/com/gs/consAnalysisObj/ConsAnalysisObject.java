package com.gs.consAnalysisObj;

import java.util.List;

public class ConsAnalysisObject {
	
	private int requestDetailId;
	private String requestNumber;
	private String CRIBReportNumber;
	private String CRIBOrderDate;
	private String fullName;
	private String NIC;
	private String passport;
	private String drivingLicense;
	private String profession;
	private String employerName;
	private String permenantAddress;
	private List<RelationshipDetails> relationshipDetails;
	private List<DishonoredChequesSummary> dishonoredChequesSummary;
	private List<CFSummary> CFSummary;
	private List<SummaryOfCreditFacilityAtGlance> summaryOfCreditFacilityAtGlance;
	private List<CFDetails> CFDetails;
	
	/**
	 * @return the requestDetailId
	 */
	public int getRequestDetailId() {
		return requestDetailId;
	}
	/**
	 * @param requestDetailId the requestDetailId to set
	 */
	public void setRequestDetailId(int requestDetailId) {
		this.requestDetailId = requestDetailId;
	}
	/**
	 * @return the requestNumber
	 */
	public String getRequestNumber() {
		return requestNumber;
	}
	/**
	 * @param requestNumber the requestNumber to set
	 */
	public void setRequestNumber(String requestNumber) {
		this.requestNumber = requestNumber;
	}
	/**
	 * @return the cRIBReportNumber
	 */
	public String getCRIBReportNumber() {
		return CRIBReportNumber;
	}
	/**
	 * @param cRIBReportNumber the cRIBReportNumber to set
	 */
	public void setCRIBReportNumber(String cRIBReportNumber) {
		CRIBReportNumber = cRIBReportNumber;
	}
	/**
	 * @return the cRIBOrderDate
	 */
	public String getCRIBOrderDate() {
		return CRIBOrderDate;
	}
	/**
	 * @param cRIBOrderDate the cRIBOrderDate to set
	 */
	public void setCRIBOrderDate(String cRIBOrderDate) {
		CRIBOrderDate = cRIBOrderDate;
	}
	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}
	/**
	 * @param fullName the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	/**
	 * @return the nIC
	 */
	public String getNIC() {
		return NIC;
	}
	/**
	 * @param nIC the nIC to set
	 */
	public void setNIC(String nIC) {
		NIC = nIC;
	}
	/**
	 * @return the passport
	 */
	public String getPassport() {
		return passport;
	}
	/**
	 * @param passport the passport to set
	 */
	public void setPassport(String passport) {
		this.passport = passport;
	}
	/**
	 * @return the drivingLicense
	 */
	public String getDrivingLicense() {
		return drivingLicense;
	}
	/**
	 * @param drivingLicense the drivingLicense to set
	 */
	public void setDrivingLicense(String drivingLicense) {
		this.drivingLicense = drivingLicense;
	}
	/**
	 * @return the profession
	 */
	public String getProfession() {
		return profession;
	}
	/**
	 * @param profession the profession to set
	 */
	public void setProfession(String profession) {
		this.profession = profession;
	}
	/**
	 * @return the employerName
	 */
	public String getEmployerName() {
		return employerName;
	}
	/**
	 * @param employerName the employerName to set
	 */
	public void setEmployerName(String employerName) {
		this.employerName = employerName;
	}
	/**
	 * @return the permenantAddress
	 */
	public String getPermenantAddress() {
		return permenantAddress;
	}
	/**
	 * @param permenantAddress the permenantAddress to set
	 */
	public void setPermenantAddress(String permenantAddress) {
		this.permenantAddress = permenantAddress;
	}
	/**
	 * @return the relationshipDetails
	 */
	public List<RelationshipDetails> getRelationshipDetails() {
		return relationshipDetails;
	}
	/**
	 * @param relationshipDetails the relationshipDetails to set
	 */
	public void setRelationshipDetails(List<RelationshipDetails> relationshipDetails) {
		this.relationshipDetails = relationshipDetails;
	}
	/**
	 * @return the dishonoredChequesSummary
	 */
	public List<DishonoredChequesSummary> getDishonoredChequesSummary() {
		return dishonoredChequesSummary;
	}
	/**
	 * @param dishonoredChequesSummary the dishonoredChequesSummary to set
	 */
	public void setDishonoredChequesSummary(List<DishonoredChequesSummary> dishonoredChequesSummary) {
		this.dishonoredChequesSummary = dishonoredChequesSummary;
	}
	/**
	 * @return the cFSummary
	 */
	public List<CFSummary> getCFSummary() {
		return CFSummary;
	}
	/**
	 * @param cFSummary the cFSummary to set
	 */
	public void setCFSummary(List<CFSummary> cFSummary) {
		CFSummary = cFSummary;
	}
	/**
	 * @return the summaryOfCreditFacilityAtGlance
	 */
	public List<SummaryOfCreditFacilityAtGlance> getSummaryOfCreditFacilityAtGlance() {
		return summaryOfCreditFacilityAtGlance;
	}
	/**
	 * @param summaryOfCreditFacilityAtGlance the summaryOfCreditFacilityAtGlance to set
	 */
	public void setSummaryOfCreditFacilityAtGlance(List<SummaryOfCreditFacilityAtGlance> summaryOfCreditFacilityAtGlance) {
		this.summaryOfCreditFacilityAtGlance = summaryOfCreditFacilityAtGlance;
	}
	/**
	 * @return the cFDetails
	 */
	public List<CFDetails> getCFDetails() {
		return CFDetails;
	}
	/**
	 * @param cFDetails the cFDetails to set
	 */
	public void setCFDetails(List<CFDetails> cFDetails) {
		CFDetails = cFDetails;
	}
	
}
