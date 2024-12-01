package com.gs.pdfGeneration;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.gs.scoreObj.ConsumerScoreObject;
import com.gs.scoreObj.CribScore;
import com.gs.scoreObj.ScoreGeneralDetails;
import com.gs.scoreObj.ScoreObservations;

public class GetConsumerScoreData {
	
	public static Logger log = Logger.getLogger(GetConsumerScoreData.class);
	
	ScoreGeneralDetails scoreGeneralDetails;
	CribScore cribScore;
	List<ScoreObservations> scoreObservationsList;
	
	// Get consumer score data
	 public ConsumerScoreObject getConsumerScoreDataByDetailId(Connection conn, int requestDetailId) throws SQLException, Exception {
	 	
		 ConsumerScoreObject consumerObj = new ConsumerScoreObject();
		 
 		CallableStatement call = conn.prepareCall("{call GET_CON_SCORE_DATA(?)}");
		call.setInt(1, requestDetailId);
		call.execute();
				
		ResultSet generalDetailResultSet = call.getResultSet();
		while (generalDetailResultSet.next()) {
			scoreGeneralDetails = new ScoreGeneralDetails();
			scoreGeneralDetails.setReportOrderNumber(generalDetailResultSet.getObject(1) != null ? generalDetailResultSet.getObject(1).toString() : "");
			scoreGeneralDetails.setReportOrderdate(formateDate(generalDetailResultSet.getObject(2) != null ? generalDetailResultSet.getObject(2).toString() : ""));
        }
		consumerObj.setScoreGeneralDetails(scoreGeneralDetails);
		generalDetailResultSet.close();
		
		call.getMoreResults();
		ResultSet observationsResultSet = call.getResultSet();
		scoreObservationsList = new ArrayList<ScoreObservations>();
		while (observationsResultSet.next()) {
			ScoreObservations scoreObservation = new ScoreObservations();
			scoreObservation.setCode(observationsResultSet.getObject(1) != null ? observationsResultSet.getObject(1).toString() : "");
			scoreObservation.setStatement(observationsResultSet.getObject(2) != null ? observationsResultSet.getObject(2).toString() : "");
			scoreObservationsList.add(scoreObservation);
        }
		consumerObj.setScoreObservations(scoreObservationsList);
		observationsResultSet.close();
				
		call.getMoreResults();
		ResultSet scoreResultSet = call.getResultSet();
		while (scoreResultSet.next()) {
			cribScore = new CribScore();
			cribScore.setCribScore(scoreResultSet.getObject(1) != null ? scoreResultSet.getObject(1).toString() : "");
			cribScore.setRiskGrade(scoreResultSet.getObject(2) != null ? scoreResultSet.getObject(2).toString() : "");
			cribScore.setRiskGradeColor(getGradeColor(scoreResultSet.getObject(2) != null ? scoreResultSet.getObject(2).toString() : ""));
        }
		consumerObj.setCribScore(cribScore);
		scoreResultSet.close();
	 			
		return consumerObj;
	 }
	 
	 public String getGradeColor(String value){
		 String color = "background:#808080";
		 if(value != null  && !value.equals("") && !value.equals("XX")){
			 if(value.contains("A")) {
				color = "background:#3c5c26";	 
			 }else if(value.contains("B")) {
				 color = "background:#83bb5d"; 
			 }else if(value.contains("C")) {
				 color = "background:#ffc000"; 
			 }else if(value.contains("D")) {
				 color = "background:#ff5d5d"; 
			 }else if(value.contains("E")) {
				 color = "background:#c00000"; 
			 }else{
				 color = "background:#808080"; 
			 }
		 }	 
		return color + "; color: white; font-weight:bold; padding:3px 15px 3px 15px; border-radius:5px";	
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

}
