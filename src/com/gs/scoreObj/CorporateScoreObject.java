package com.gs.scoreObj;

import java.util.List;

public class CorporateScoreObject {

	private ScoreGeneralDetails scoreGeneralDetails;
	private CribScore cribScore;
	private List<ScoreObservations> ScoreObservations;
	
	/**
	 * @return the scoreGeneralDetails
	 */
	public ScoreGeneralDetails getScoreGeneralDetails() {
		return scoreGeneralDetails;
	}
	/**
	 * @param scoreGeneralDetails the scoreGeneralDetails to set
	 */
	public void setScoreGeneralDetails(ScoreGeneralDetails scoreGeneralDetails) {
		this.scoreGeneralDetails = scoreGeneralDetails;
	}
	/**
	 * @return the cribScore
	 */
	public CribScore getCribScore() {
		return cribScore;
	}
	/**
	 * @param cribScore the cribScore to set
	 */
	public void setCribScore(CribScore cribScore) {
		this.cribScore = cribScore;
	}
	/**
	 * @return the scoreObservations
	 */
	public List<ScoreObservations> getScoreObservations() {
		return ScoreObservations;
	}
	/**
	 * @param scoreObservations the scoreObservations to set
	 */
	public void setScoreObservations(List<ScoreObservations> scoreObservations) {
		ScoreObservations = scoreObservations;
	}
	
}
