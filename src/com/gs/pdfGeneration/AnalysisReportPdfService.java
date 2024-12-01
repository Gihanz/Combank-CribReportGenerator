package com.gs.pdfGeneration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import com.google.gson.Gson;
import com.gs.consAnalysisObj.ConsAnalysisObject;
import com.gs.corpAnalysisObj.CorpAnalysisObject;
import com.gs.scoreObj.ConsumerScoreObject;
import com.gs.scoreObj.CorporateScoreObject;
import com.gs.util.DbOperations;
import com.gs.util.PropertyReader;
import com.lowagie.text.DocumentException;

import static org.thymeleaf.templatemode.TemplateMode.HTML;

public class AnalysisReportPdfService {
	
	private static Properties prop;
	public static Logger log = Logger.getLogger(AnalysisReportPdfService.class);
		
	public AnalysisReportPdfService(){
		try{
			PropertyReader pr = new PropertyReader();
	    	prop = pr.loadPropertyFile();
	    	
			String pathSep = System.getProperty("file.separator");
	        String logpath = prop.getProperty("LOG4J_FILE_PATH");
	        String activityRoot = prop.getProperty("LOG_PATH");
			String logPropertyFile =logpath+pathSep+"log4j.properties"; 
	
			PropertyConfigurator.configure(logPropertyFile);
			PropertyReader.loadLogConfiguration(logPropertyFile, activityRoot+"/PdfService/", "PdfService.log");
		}catch(Exception e){
			System.out.println("Error : " +e.fillInStackTrace());
			log.info("Error : " +e.fillInStackTrace());
		}	
	}
	
	public String generatePdf(int subjectType, String reportJson, boolean scoreRequired, String encryptionKey) throws IOException, DocumentException {

		Context context = null;
		String html = null;
		
		try {

			if(subjectType == 1) {				
				ConsAnalysisObject consAnalysisObject = new Gson().fromJson(reportJson, ConsAnalysisObject.class);
				ConsumerScoreObject consScoreObj = new ConsumerScoreObject();
				if(scoreRequired) {
					
					DbOperations dbOperations = new DbOperations();
					Connection conn = dbOperations.getConnection(encryptionKey);
					
					GetConsumerScoreData consScore = new GetConsumerScoreData();
					consScoreObj = consScore.getConsumerScoreDataByDetailId(conn, consAnalysisObject.getRequestDetailId());
				}
				
				context = getConsumerAnalysisContext(consAnalysisObject, scoreRequired, consScoreObj);
		        html = loadAndFillTemplate(context, "cons_analysis_report_pdf_template");
		        return renderPdf(html);
		        
			} else {
				CorpAnalysisObject corpAnalysisObject = new Gson().fromJson(reportJson, CorpAnalysisObject.class);
				CorporateScoreObject corpScoreObj = new CorporateScoreObject();
				if(scoreRequired) {	
					
					DbOperations dbOperations = new DbOperations();
					Connection conn = dbOperations.getConnection(encryptionKey);
					
					GetCorporateScoreData corpScore = new GetCorporateScoreData();
					corpScoreObj = corpScore.getCorporateScoreDataByDetailId(conn, corpAnalysisObject.getRequestDetailId());
				}
				
				context = getCorporateAnalysisContext(corpAnalysisObject, scoreRequired, corpScoreObj);
		        html = loadAndFillTemplate(context, "corp_analysis_report_pdf_template");
		        return renderPdf(html);
			}
		
		} catch (Exception e) {
			final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            
            e.printStackTrace();
            log.info("Exception occured : " + sw.toString());
            return "Analysis Report Generation Error";
		}
    }
	
	private String renderPdf(String html) throws IOException, DocumentException {

        ByteArrayOutputStream pdfStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer(20f * 4f / 3f, 20);
        renderer.setDocumentFromString(html);
        renderer.layout();
        renderer.createPDF(pdfStream);
        pdfStream.close();
        
        byte[] encoded = Base64.encodeBase64(pdfStream.toByteArray());
		String encodedString = new String(encoded);
		
        return encodedString;
    }

    private Context getConsumerAnalysisContext(ConsAnalysisObject analysisData, Boolean scoreRequired, ConsumerScoreObject scoreData) {
        Context context = new Context();
        context.setVariable("requestNumber", analysisData.getRequestNumber());
        context.setVariable("CRIBReportNumber", analysisData.getCRIBReportNumber()); 
        context.setVariable("CRIBOrderDate", analysisData.getCRIBOrderDate()); 
        context.setVariable("fullName", analysisData.getFullName()); 
        context.setVariable("NIC", analysisData.getNIC()); 
        context.setVariable("passport", analysisData.getPassport()); 
        context.setVariable("drivingLicense", analysisData.getDrivingLicense()); 
        context.setVariable("profession", analysisData.getProfession()); 
        context.setVariable("employerName", analysisData.getEmployerName()); 
        context.setVariable("permenantAddress", analysisData.getPermenantAddress());       
        context.setVariable("relationshipDetails", analysisData.getRelationshipDetails());
        context.setVariable("dishonChequesSummary", analysisData.getDishonoredChequesSummary());     
        context.setVariable("CFSummary", analysisData.getCFSummary());
        context.setVariable("summaryOfCFAtGlance", analysisData.getSummaryOfCreditFacilityAtGlance());
        context.setVariable("creditFacilityDetails", analysisData.getCFDetails());
        
        context.setVariable("scoreRequired", scoreRequired);
	    context.setVariable("scoreGeneralDetail", scoreData.getScoreGeneralDetails()); 
	    context.setVariable("cribScore", scoreData.getCribScore()); 
	    context.setVariable("scoreObservations", scoreData.getScoreObservations()); 
                 
        return context;
    }
    
    private Context getCorporateAnalysisContext(CorpAnalysisObject analysisData, Boolean scoreRequired, CorporateScoreObject scoreData) {
        Context context = new Context();
        context.setVariable("requestNumber", analysisData.getRequestNumber());
        context.setVariable("CRIBReportNumber", analysisData.getCRIBReportNumber()); 
        context.setVariable("CRIBOrderDate", analysisData.getCRIBOrderDate()); 
        context.setVariable("fullName", analysisData.getFullName()); 
        context.setVariable("BR", analysisData.getBR());
        context.setVariable("dishonChequesSummary", analysisData.getDishonoredChequesSummary());
        context.setVariable("CFSummary", analysisData.getCreditFacilitySummary());
        context.setVariable("CFSummaryBasedLendingIns", analysisData.getCreditFacilitySummaryBasedLendingIns());
        context.setVariable("creditFacilityAtAGlanceCondensed", analysisData.getCreditFacilityAtAGlanceCondensed());
        context.setVariable("creditFacilityAtAGlance", analysisData.getCreditFacilityAtAGlance());
        context.setVariable("overdueFacilities1", analysisData.getOverdueFacilities1());
        context.setVariable("overdueFacilities2", analysisData.getOverdueFacilities2());
        context.setVariable("overdueFacilities3", analysisData.getOverdueFacilities3());
        context.setVariable("overdueFacilities4", analysisData.getOverdueFacilities4());
        context.setVariable("detailsOfProblematicFacilities", analysisData.getDetailsOfProblematicFacilities());
        
        context.setVariable("scoreRequired", scoreRequired);
	    context.setVariable("scoreGeneralDetail", scoreData.getScoreGeneralDetails()); 
	    context.setVariable("cribScore", scoreData.getCribScore()); 
	    context.setVariable("scoreObservations", scoreData.getScoreObservations()); 
            
        return context;
    }

    private String loadAndFillTemplate(Context context, String template) {
    	
    	ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCheckExistence(true);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine.process(template, context);            
    }
    
         
    public static void main(String[] args) {
    	try {
    		AnalysisReportPdfService asd= new AnalysisReportPdfService();
    		String consjson = "{'requestNumber':'98798797','CRIBReportNumber':'232323','CRIBOrderDate':'friday','fullName':'gihan liyanage','NIC':'094122V','passport':'dfdf','drivingLicense':'84985495','profession':'GH','employerName':'nable','permenantAddress':'moratuwa','CFDetails':[{'CF_TYPE':'CRCD','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'US & NG','FIRST_DISBURSE_DATE':'06/08/2007','REPAY_TYPE':'DMND','END_DATE':'','AMOUNT_GRANTED':300000,'CF_NO':1,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'29/01/2019','ARREAS_AMOUNT':0,'INSTALLMENT_AMOUNT':6995,'CURRENT_BALANCE':139913,'OWNERSHIP':'OWN','RESTRUCT_DATE':''},{'CF_TYPE':'OVDR','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'US & NG','FIRST_DISBURSE_DATE':'03/12/2013','REPAY_TYPE':'DMND','END_DATE':'','AMOUNT_GRANTED':4000000,'CF_NO':2,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'28/02/2019','ARREAS_AMOUNT':3678849,'INSTALLMENT_AMOUNT':null,'CURRENT_BALANCE':7678849,'OWNERSHIP':'OWN','RESTRUCT_DATE':''},{'CF_TYPE':'LOAN','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'US & NG','FIRST_DISBURSE_DATE':'17/01/2013','REPAY_TYPE':'MNLY','END_DATE':'','AMOUNT_GRANTED':15000000,'CF_NO':3,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'22/01/2019','ARREAS_AMOUNT':230235,'INSTALLMENT_AMOUNT':200000,'CURRENT_BALANCE':2398419,'OWNERSHIP':'OWN','RESTRUCT_DATE':''},{'CF_TYPE':'OVDR','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'FS & NG','FIRST_DISBURSE_DATE':'29/04/2010','REPAY_TYPE':'DMND','END_DATE':'','AMOUNT_GRANTED':0,'CF_NO':4,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'29/04/2010','ARREAS_AMOUNT':0,'INSTALLMENT_AMOUNT':0,'CURRENT_BALANCE':0,'OWNERSHIP':'OWN','RESTRUCT_DATE':''},{'CF_TYPE':'OVDR','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'FS & NG','FIRST_DISBURSE_DATE':'25/07/2013','REPAY_TYPE':'DMND','END_DATE':'','AMOUNT_GRANTED':0,'CF_NO':5,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'','ARREAS_AMOUNT':1922914,'INSTALLMENT_AMOUNT':null,'CURRENT_BALANCE':1922914,'OWNERSHIP':'OWN','RESTRUCT_DATE':''},{'CF_TYPE':'CRCD','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'US & NG','FIRST_DISBURSE_DATE':'13/02/2014','REPAY_TYPE':'DMND','END_DATE':'','AMOUNT_GRANTED':250000,'CF_NO':6,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'01/05/2018','ARREAS_AMOUNT':0,'INSTALLMENT_AMOUNT':8125,'CURRENT_BALANCE':5984,'OWNERSHIP':'OWN','RESTRUCT_DATE':''},{'CF_TYPE':'LOAN','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'US & NG','FIRST_DISBURSE_DATE':'06/11/2015','REPAY_TYPE':'MNLY','END_DATE':'','AMOUNT_GRANTED':2500000,'CF_NO':7,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'22/01/2019','ARREAS_AMOUNT':54376,'INSTALLMENT_AMOUNT':42000,'CURRENT_BALANCE':904000,'OWNERSHIP':'OWN','RESTRUCT_DATE':''},{'CF_TYPE':'CRCD','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'US & NG','FIRST_DISBURSE_DATE':'06/01/2016','REPAY_TYPE':'DMND','END_DATE':'','AMOUNT_GRANTED':300000,'CF_NO':8,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'17/01/2019','ARREAS_AMOUNT':0,'INSTALLMENT_AMOUNT':8250,'CURRENT_BALANCE':0,'OWNERSHIP':'OWN','RESTRUCT_DATE':''},{'CF_TYPE':'LOAN','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'US & NG','FIRST_DISBURSE_DATE':'10/03/2017','REPAY_TYPE':'MNLY','END_DATE':'','AMOUNT_GRANTED':6000000,'CF_NO':9,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'14/02/2019','ARREAS_AMOUNT':0,'INSTALLMENT_AMOUNT':100000,'CURRENT_BALANCE':3700000,'OWNERSHIP':'OWN','RESTRUCT_DATE':''},{'CF_TYPE':'OVDR','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'US & NG','FIRST_DISBURSE_DATE':'15/02/2019','REPAY_TYPE':'DMND','END_DATE':'','AMOUNT_GRANTED':62775,'CF_NO':10,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'','ARREAS_AMOUNT':62775,'INSTALLMENT_AMOUNT':null,'CURRENT_BALANCE':62775,'OWNERSHIP':'OWN','RESTRUCT_DATE':''},{'CF_TYPE':'LOAN','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'US & NG','FIRST_DISBURSE_DATE':'22/01/2018','REPAY_TYPE':'MNLY','END_DATE':'','AMOUNT_GRANTED':4500000,'CF_NO':12,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'29/01/2019','ARREAS_AMOUNT':133323,'INSTALLMENT_AMOUNT':75000,'CURRENT_BALANCE':4275000,'OWNERSHIP':'OWN','RESTRUCT_DATE':''},{'CF_TYPE':'LEAS','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'FS & NG','FIRST_DISBURSE_DATE':'17/01/2019','REPAY_TYPE':'MNLY','END_DATE':'','AMOUNT_GRANTED':5600000,'CF_NO':13,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'25/02/2019','ARREAS_AMOUNT':0,'INSTALLMENT_AMOUNT':151928,'CURRENT_BALANCE':5424502,'OWNERSHIP':'OWN','RESTRUCT_DATE':''},{'CF_TYPE':'LOAN','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'FS & NG','FIRST_DISBURSE_DATE':'19/01/2016','REPAY_TYPE':'MNLY','END_DATE':'','AMOUNT_GRANTED':50000000,'CF_NO':14,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'19/12/2018','ARREAS_AMOUNT':1515200,'INSTALLMENT_AMOUNT':757600,'CURRENT_BALANCE':41968995,'OWNERSHIP':'JNT','RESTRUCT_DATE':''},{'CF_TYPE':'LOAN','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'FS & NG','FIRST_DISBURSE_DATE':'20/02/2018','REPAY_TYPE':'MNLY','END_DATE':'','AMOUNT_GRANTED':25000000,'CF_NO':15,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'15/02/2019','ARREAS_AMOUNT':781250,'INSTALLMENT_AMOUNT':390625,'CURRENT_BALANCE':23261509,'OWNERSHIP':'JNT','RESTRUCT_DATE':''},{'CF_TYPE':'LOAN','CURRENCY':'LKR','INSTITUTION_CATG':'FCC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'FS & OG','FIRST_DISBURSE_DATE':'27/04/2016','REPAY_TYPE':'MNLY','END_DATE':'','AMOUNT_GRANTED':820000,'CF_NO':16,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'05/02/2019','ARREAS_AMOUNT':19538,'INSTALLMENT_AMOUNT':0,'CURRENT_BALANCE':788331,'OWNERSHIP':'GRT','RESTRUCT_DATE':''},{'CF_TYPE':'LOAN','CURRENCY':'LKR','INSTITUTION_CATG':'FCC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'FS & OG','FIRST_DISBURSE_DATE':'09/05/2016','REPAY_TYPE':'MNLY','END_DATE':'','AMOUNT_GRANTED':1000000,'CF_NO':17,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'01/02/2019','ARREAS_AMOUNT':0,'INSTALLMENT_AMOUNT':0,'CURRENT_BALANCE':288771,'OWNERSHIP':'GRT','RESTRUCT_DATE':''},{'CF_TYPE':'LOAN','CURRENCY':'LKR','INSTITUTION_CATG':'CBC','REPORTED_DATE':'28/02/2019','between16_60Days':0,'AMOUNT_WRITTEN_OFF':null,'between61Days':0,'CF_STAT':'ACTV','PURP':'-','COVERAGE':'US & NG','FIRST_DISBURSE_DATE':'19/03/2018','REPAY_TYPE':'MNLY','END_DATE':'','AMOUNT_GRANTED':4000000,'CF_NO':11,'INSTITUTION_BRANCH':'-','between0_15Days':0,'LATEST_PAYMENT_DATE':'22/01/2019','ARREAS_AMOUNT':112079,'INSTALLMENT_AMOUNT':66700,'CURRENT_BALANCE':3333000,'OWNERSHIP':'OWN','RESTRUCT_DATE':''}],'relationshipDetails':[{'RELATED_ENTITY_NAME':'B.H RANASINGHE LANKA INDHANA PIRAWUMHALA','R_NO':1,'RELATED_ENTITY_ID_DETAILS':'GA162931','NATURE_OF_RELATIONSHIP':'Proprietor'}],'dishonoredChequesSummary':[],'permenantAddress':'ELPITIYA ROAD BH RANASINGHE LANKA FILLING STATION HIPANKANDA ELPITIYA RD HIPANKANDA','CFSummary':[{'totalOutstanding':30922458,'totalNumberOfFacilities':15,'typeOfLiabilities':'Direct Liabilities','totalAmountGrantedLimit':44332775},{'totalOutstanding':65230504,'totalNumberOfFacilities':2,'typeOfLiabilities':'Indirect Liabilities','totalAmountGrantedLimit':75000000},{'totalOutstanding':96152962,'totalNumberOfFacilities':17,'typeOfLiabilities':'Total','totalAmountGrantedLimit':119332775}]}";
    		String corpjson = "{'requestNumber':'98798797','CRIBReportNumber':'232323','CRIBOrderDate':'friday','fullName':'KFC ltd','BR':'1191919','creditFacilityAtAGlance':[{'b31':0,'g61':0,'g31':0,'b90':0,'g0':0,'g1':0,'label':'Total No of facilities - Direct','b61':0,'b0':20,'b1':0,'g90':0},{'b31':0,'g61':0,'g31':0,'b90':0,'g0':0,'g1':0,'label':'Total No of facilities - Indirect','b61':0,'b0':289,'b1':0,'g90':0},{'b31':0,'g61':0,'g31':0,'b90':0,'g0':0,'g1':0,'label':'Total No of facilities','b61':0,'b0':309,'b1':0,'g90':0},{'b31':0,'g61':0,'g31':0,'b90':0,'g0':0,'g1':0,'label':'Total amount arrears(LKR) - Direct','b61':0,'b0':0,'b1':0,'g90':0},{'b31':0,'g61':0,'g31':0,'b90':0,'g0':0,'g1':0,'label':'Total amount arrears(LKR) - Indirect','b61':0,'b0':0,'b1':0,'g90':0},{'b31':0,'g61':0,'g31':0,'b90':0,'g0':0,'g1':0,'label':'Total amount arrears(LKR)','b61':0,'b0':0,'b1':0,'g90':0}],'dishonoredChequesSummary':[],'creditFacilitySummary':[{'bType':'Direct','gType':'Direct','bAmountGranted':1166000000,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':20,'currency':'LKR','bMonthlyCommitment':1166000000,'label':'LKR','gNoOfFacilities':0,'bOutstanding':1166208680,'gOutstanding':0},{'bType':'Indirect','gType':'Indirect','bAmountGranted':187589690,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':12,'currency':'LKR','bMonthlyCommitment':102589690,'label':'LKR','gNoOfFacilities':0,'bOutstanding':98872634,'gOutstanding':0},{'bType':'Direct','gType':'Direct','bAmountGranted':0,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':0,'currency':'USD','bMonthlyCommitment':0,'label':'USD','gNoOfFacilities':0,'bOutstanding':0,'gOutstanding':0},{'bType':'Indirect','gType':'Indirect','bAmountGranted':19810005,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':216,'currency':'USD','bMonthlyCommitment':19751872,'label':'USD','gNoOfFacilities':0,'bOutstanding':525453,'gOutstanding':0},{'bType':'Direct','gType':'Direct','bAmountGranted':0,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':0,'currency':'EUR','bMonthlyCommitment':0,'label':'EUR','gNoOfFacilities':0,'bOutstanding':0,'gOutstanding':0},{'bType':'Indirect','gType':'Indirect','bAmountGranted':4893911,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':37,'currency':'EUR','bMonthlyCommitment':4893911,'label':'EUR','gNoOfFacilities':0,'bOutstanding':30773,'gOutstanding':0},{'bType':'Direct','gType':'Direct','bAmountGranted':0,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':0,'currency':'SGD','bMonthlyCommitment':0,'label':'SGD','gNoOfFacilities':0,'bOutstanding':0,'gOutstanding':0},{'bType':'Indirect','gType':'Indirect','bAmountGranted':1133323,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':6,'currency':'SGD','bMonthlyCommitment':1133323,'label':'SGD','gNoOfFacilities':0,'bOutstanding':3900,'gOutstanding':0},{'bType':'Direct','gType':'Direct','bAmountGranted':0,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':0,'currency':'GBP','bMonthlyCommitment':0,'label':'GBP','gNoOfFacilities':0,'bOutstanding':0,'gOutstanding':0},{'bType':'Indirect','gType':'Indirect','bAmountGranted':6014502,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':6,'currency':'GBP','bMonthlyCommitment':6014502,'label':'GBP','gNoOfFacilities':0,'bOutstanding':129539,'gOutstanding':0},{'bType':'Direct','gType':'Direct','bAmountGranted':0,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':0,'currency':'JPY','bMonthlyCommitment':0,'label':'JPY','gNoOfFacilities':0,'bOutstanding':0,'gOutstanding':0},{'bType':'Indirect','gType':'Indirect','bAmountGranted':70057000,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':12,'currency':'JPY','bMonthlyCommitment':70057000,'label':'JPY','gNoOfFacilities':0,'bOutstanding':0,'gOutstanding':0},{'bType':'Direct','gType':'Direct','bAmountGranted':1166000000,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':20,'currency':'LKR','bMonthlyCommitment':1166000000,'label':'Total in LKR','gNoOfFacilities':0,'bOutstanding':1166208680,'gOutstanding':0},{'bType':'Indirect','gType':'Indirect','bAmountGranted':6990194404.070201,'gAmountGranted':0,'gMonthlyCommitment':0,'bNoOfFacilities':289,'currency':'LKR','bMonthlyCommitment':6894053365.766001,'label':'Total in LKR','gNoOfFacilities':0,'bOutstanding':241404129.2941,'gOutstanding':0}],'overdueFacilities4':[{'amountGranted':0,'arrearsDays':0,'arrearsAmount':0,'installmentAmount':0,'amountWrittenOff':0,'currentBalance':0,'currency':'LKR','label':'Grand Total LKR'}],'overdueFacilities3':[{'amountGranted':0,'arrearsDays':0,'arrearsAmount':0,'installmentAmount':0,'amountWrittenOff':0,'currentBalance':0,'currency':'LKR','label':'Grand Total LKR'}],'overdueFacilities2':[{'amountGranted':0,'arrearsDays':0,'arrearsAmount':0,'installmentAmount':0,'amountWrittenOff':0,'currentBalance':0,'currency':'LKR','label':'Grand Total LKR'}],'overdueFacilities1':[{'amountGranted':0,'arrearsDays':0,'arrearsAmount':0,'installmentAmount':0,'amountWrittenOff':0,'currentBalance':0,'currency':'LKR','label':'Grand Total LKR'}],'creditFacilityAtAGlanceCondensed':[{'_0':0,'_61_90':0,'_90':0,'_1_30':0,'label':'Total Number of Facilities','_31_60':0}],'creditFacilitySummaryBasedLendingIns':[{'cNoOfFacilities':48,'cOutstanding':5158064838,'oOutstanding':0,'oMonthlyCommitment':0,'fAmountGranted':0,'cAmountGranted':6588047039,'cMonthlyCommitment':2000067494248,'fType':'Direct','cType':'Direct','label':'LKR','oType':'Direct','fMonthlyCommitment':0,'oNoOfFacilities':0,'fNoOfFacilities':0,'currency':'LKR','oAmountGranted':0,'fOutstanding':0},{'cNoOfFacilities':0,'cOutstanding':0,'oOutstanding':0,'oMonthlyCommitment':0,'fAmountGranted':3500000,'cAmountGranted':0,'cMonthlyCommitment':0,'fType':'Indirect','cType':'Indirect','label':'LKR','oType':'Indirect','fMonthlyCommitment':116666,'oNoOfFacilities':0,'fNoOfFacilities':1,'currency':'LKR','oAmountGranted':0,'fOutstanding':650000},{'cNoOfFacilities':25,'cOutstanding':2396553,'oOutstanding':0,'oMonthlyCommitment':0,'fAmountGranted':0,'cAmountGranted':6882276,'cMonthlyCommitment':3595005,'fType':'Direct','cType':'Direct','label':'USD','oType':'Direct','fMonthlyCommitment':0,'oNoOfFacilities':0,'fNoOfFacilities':0,'currency':'USD','oAmountGranted':0,'fOutstanding':0},{'cNoOfFacilities':0,'cOutstanding':0,'oOutstanding':0,'oMonthlyCommitment':0,'fAmountGranted':0,'cAmountGranted':0,'cMonthlyCommitment':0,'fType':'Indirect','cType':'Indirect','label':'USD','oType':'Indirect','fMonthlyCommitment':0,'oNoOfFacilities':0,'fNoOfFacilities':0,'currency':'USD','oAmountGranted':0,'fOutstanding':0},{'cNoOfFacilities':5,'cOutstanding':0,'oOutstanding':0,'oMonthlyCommitment':0,'fAmountGranted':0,'cAmountGranted':304190,'cMonthlyCommitment':272680,'fType':'Direct','cType':'Direct','label':'EUR','oType':'Direct','fMonthlyCommitment':0,'oNoOfFacilities':0,'fNoOfFacilities':0,'currency':'EUR','oAmountGranted':0,'fOutstanding':0},{'cNoOfFacilities':0,'cOutstanding':0,'oOutstanding':0,'oMonthlyCommitment':0,'fAmountGranted':0,'cAmountGranted':0,'cMonthlyCommitment':0,'fType':'Indirect','cType':'Indirect','label':'EUR','oType':'Indirect','fMonthlyCommitment':0,'oNoOfFacilities':0,'fNoOfFacilities':0,'currency':'EUR','oAmountGranted':0,'fOutstanding':0},{'cNoOfFacilities':5,'cOutstanding':0,'oOutstanding':0,'oMonthlyCommitment':0,'fAmountGranted':0,'cAmountGranted':3622785,'cMonthlyCommitment':3622785,'fType':'Direct','cType':'Direct','label':'SGD','oType':'Direct','fMonthlyCommitment':0,'oNoOfFacilities':0,'fNoOfFacilities':0,'currency':'SGD','oAmountGranted':0,'fOutstanding':0},{'cNoOfFacilities':0,'cOutstanding':0,'oOutstanding':0,'oMonthlyCommitment':0,'fAmountGranted':0,'cAmountGranted':0,'cMonthlyCommitment':0,'fType':'Indirect','cType':'Indirect','label':'SGD','oType':'Indirect','fMonthlyCommitment':0,'oNoOfFacilities':0,'fNoOfFacilities':0,'currency':'SGD','oAmountGranted':0,'fOutstanding':0},{'cNoOfFacilities':4,'cOutstanding':0,'oOutstanding':0,'oMonthlyCommitment':0,'fAmountGranted':0,'cAmountGranted':17790145,'cMonthlyCommitment':15801145,'fType':'Direct','cType':'Direct','label':'JPY','oType':'Direct','fMonthlyCommitment':0,'oNoOfFacilities':0,'fNoOfFacilities':0,'currency':'JPY','oAmountGranted':0,'fOutstanding':0},{'cNoOfFacilities':0,'cOutstanding':0,'oOutstanding':0,'oMonthlyCommitment':0,'fAmountGranted':0,'cAmountGranted':0,'cMonthlyCommitment':0,'fType':'Indirect','cType':'Indirect','label':'JPY','oType':'Indirect','fMonthlyCommitment':0,'oNoOfFacilities':0,'fNoOfFacilities':0,'currency':'JPY','oAmountGranted':0,'fOutstanding':0},{'cNoOfFacilities':87,'cOutstanding':5397720138,'oOutstanding':0,'oMonthlyCommitment':0,'fAmountGranted':0,'cAmountGranted':9447986639,'cMonthlyCommitment':2002396655748,'fType':'Direct','cType':'Direct','label':'Total in LKR','oType':'Direct','fMonthlyCommitment':0,'oNoOfFacilities':0,'fNoOfFacilities':0,'currency':'LKR','oAmountGranted':0,'fOutstanding':0},{'cNoOfFacilities':0,'cOutstanding':0,'oOutstanding':0,'oMonthlyCommitment':0,'fAmountGranted':3500000,'cAmountGranted':0,'cMonthlyCommitment':0,'fType':'Indirect','cType':'Indirect','label':'Total in LKR','oType':'Indirect','fMonthlyCommitment':116666,'oNoOfFacilities':0,'fNoOfFacilities':1,'currency':'LKR','oAmountGranted':0,'fOutstanding':650000}]}";
    		//System.out.println(asd.generatePdf(1, consjson, true, "asdf-9kjh-qwe56"));
    		System.out.println(asd.generatePdf(0, corpjson, true, "asdf-9kjh-qwe56"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}

}
