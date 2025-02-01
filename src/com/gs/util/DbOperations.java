package com.gs.util;

import java.sql.*;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.gs.encrypt.CribEncryption;

public class DbOperations {
		
	private static String JDBC_URL;
	private static String JDBC_CLASS;
	private static String DBUSERNAME;
	private static String DBPASSWORD;
	public static Logger log = Logger.getLogger(DbOperations.class);
	
	public DbOperations() {
	    try {
	    	PropertyReader pr = new PropertyReader();
	    	Properties prop = pr.loadPropertyFile();
	    	JDBC_URL = prop.getProperty("JDBC_URL");
	    	JDBC_CLASS = prop.getProperty("JDBC_CLASS");
	    	DBUSERNAME = prop.getProperty("DBUSERNAME");
	    	DBPASSWORD = prop.getProperty("DBPASSWORD");	        
		} catch(Exception e) {
			System.out.println("Error : " +e.fillInStackTrace());
			log.info("Error : " +e.fillInStackTrace());
		}
	}
	
	// Get connection to DB.
	public Connection getConnection(String decryptionKey) {
		
		Connection connection = null;
        try {        	
        	CribEncryption cribEncryptor = new CribEncryption();
        	DBUSERNAME = cribEncryptor.decryptorSHA(DBUSERNAME, decryptionKey);
        	DBPASSWORD = cribEncryptor.decryptorSHA(DBPASSWORD, decryptionKey);
        	
            Class.forName(JDBC_CLASS);
            connection = DriverManager.getConnection(JDBC_URL, DBUSERNAME, DBPASSWORD);
            if(connection!=null){
                System.out.println("Connected to database successfully.");
                log.info("Connected to database successfully.");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            log.info("Error : " +e.fillInStackTrace());
        } catch (SQLException e) {
            e.printStackTrace();
            log.info("Error : " +e.fillInStackTrace());
        }
        return connection;
	}
	
	// Disconnect from DB.
	public void closeConnection(Connection conn) {
		
        try {
            if(conn!=null){
            	conn.close();
                System.out.println("Database connection disconnected.");
                log.info("Database connection disconnected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.info("Error : " +e.fillInStackTrace());
        }
	}
		 
}
