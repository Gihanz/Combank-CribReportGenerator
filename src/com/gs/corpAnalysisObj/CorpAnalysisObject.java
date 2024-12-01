package com.gs.corpAnalysisObj;

import java.util.List;

public class CorpAnalysisObject {
	
	private int requestDetailId;
	private String requestNumber;
	private String CRIBReportNumber;
	private String CRIBOrderDate;
	private String fullName;
	private String BR;
	private List<DishonoredChequesSummary> dishonoredChequesSummary;
	private List<CreditFacilitySummary> creditFacilitySummary;
	private List<CreditFacilitySummaryBasedLendingIns> creditFacilitySummaryBasedLendingIns;
	private List<CreditFacilityAtAGlanceCondensed> creditFacilityAtAGlanceCondensed;
	private List<CreditFacilityAtAGlance> creditFacilityAtAGlance;
	private List<OverdueFacilities1> overdueFacilities1;
	private List<OverdueFacilities1> overdueFacilities2;
	private List<OverdueFacilities1> overdueFacilities3;
	private List<OverdueFacilities1> overdueFacilities4;
	private List<DetailsOfProblematicFacilities> detailsOfProblematicFacilities;
	
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
	 * @return the bR
	 */
	public String getBR() {
		return BR;
	}
	/**
	 * @param bR the bR to set
	 */
	public void setBR(String bR) {
		BR = bR;
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
	 * @return the creditFacilitySummary
	 */
	public List<CreditFacilitySummary> getCreditFacilitySummary() {
		return creditFacilitySummary;
	}
	/**
	 * @param creditFacilitySummary the creditFacilitySummary to set
	 */
	public void setCreditFacilitySummary(List<CreditFacilitySummary> creditFacilitySummary) {
		this.creditFacilitySummary = creditFacilitySummary;
	}
	/**
	 * @return the creditFacilitySummaryBasedLendingIns
	 */
	public List<CreditFacilitySummaryBasedLendingIns> getCreditFacilitySummaryBasedLendingIns() {
		return creditFacilitySummaryBasedLendingIns;
	}
	/**
	 * @param creditFacilitySummaryBasedLendingIns the creditFacilitySummaryBasedLendingIns to set
	 */
	public void setCreditFacilitySummaryBasedLendingIns(
			List<CreditFacilitySummaryBasedLendingIns> creditFacilitySummaryBasedLendingIns) {
		this.creditFacilitySummaryBasedLendingIns = creditFacilitySummaryBasedLendingIns;
	}
	/**
	 * @return the creditFacilityAtAGlanceCondensed
	 */
	public List<CreditFacilityAtAGlanceCondensed> getCreditFacilityAtAGlanceCondensed() {
		return creditFacilityAtAGlanceCondensed;
	}
	/**
	 * @param creditFacilityAtAGlanceCondensed the creditFacilityAtAGlanceCondensed to set
	 */
	public void setCreditFacilityAtAGlanceCondensed(
			List<CreditFacilityAtAGlanceCondensed> creditFacilityAtAGlanceCondensed) {
		this.creditFacilityAtAGlanceCondensed = creditFacilityAtAGlanceCondensed;
	}
	/**
	 * @return the creditFacilityAtAGlance
	 */
	public List<CreditFacilityAtAGlance> getCreditFacilityAtAGlance() {
		return creditFacilityAtAGlance;
	}
	/**
	 * @param creditFacilityAtAGlance the creditFacilityAtAGlance to set
	 */
	public void setCreditFacilityAtAGlance(List<CreditFacilityAtAGlance> creditFacilityAtAGlance) {
		this.creditFacilityAtAGlance = creditFacilityAtAGlance;
	}
	/**
	 * @return the overdueFacilities1
	 */
	public List<OverdueFacilities1> getOverdueFacilities1() {
		return overdueFacilities1;
	}
	/**
	 * @param overdueFacilities1 the overdueFacilities1 to set
	 */
	public void setOverdueFacilities1(List<OverdueFacilities1> overdueFacilities1) {
		this.overdueFacilities1 = overdueFacilities1;
	}
	/**
	 * @return the overdueFacilities2
	 */
	public List<OverdueFacilities1> getOverdueFacilities2() {
		return overdueFacilities2;
	}
	/**
	 * @param overdueFacilities2 the overdueFacilities2 to set
	 */
	public void setOverdueFacilities2(List<OverdueFacilities1> overdueFacilities2) {
		this.overdueFacilities2 = overdueFacilities2;
	}
	/**
	 * @return the overdueFacilities3
	 */
	public List<OverdueFacilities1> getOverdueFacilities3() {
		return overdueFacilities3;
	}
	/**
	 * @param overdueFacilities3 the overdueFacilities3 to set
	 */
	public void setOverdueFacilities3(List<OverdueFacilities1> overdueFacilities3) {
		this.overdueFacilities3 = overdueFacilities3;
	}
	/**
	 * @return the overdueFacilities4
	 */
	public List<OverdueFacilities1> getOverdueFacilities4() {
		return overdueFacilities4;
	}
	/**
	 * @param overdueFacilities4 the overdueFacilities4 to set
	 */
	public void setOverdueFacilities4(List<OverdueFacilities1> overdueFacilities4) {
		this.overdueFacilities4 = overdueFacilities4;
	}
	/**
	 * @return the detailsOfProblematicFacilities
	 */
	public List<DetailsOfProblematicFacilities> getDetailsOfProblematicFacilities() {
		return detailsOfProblematicFacilities;
	}
	/**
	 * @param detailsOfProblematicFacilities the detailsOfProblematicFacilities to set
	 */
	public void setDetailsOfProblematicFacilities(List<DetailsOfProblematicFacilities> detailsOfProblematicFacilities) {
		this.detailsOfProblematicFacilities = detailsOfProblematicFacilities;
	}
}
