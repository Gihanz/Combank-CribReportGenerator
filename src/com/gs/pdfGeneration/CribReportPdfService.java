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

import com.gs.cribObj.ConsumerCribObject;
import com.gs.cribObj.CorporateCribObject;
import com.gs.scoreObj.ConsumerScoreObject;
import com.gs.scoreObj.CorporateScoreObject;
import com.gs.util.DbOperations;
import com.gs.util.PropertyReader;
import com.lowagie.text.DocumentException;

import static org.thymeleaf.templatemode.TemplateMode.HTML;

public class CribReportPdfService {
	
	private static Properties prop;
	public static Logger log = Logger.getLogger(CribReportPdfService.class);
		
	public CribReportPdfService(){
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
	
	public String generatePdf(int requestDetailId, int subjectType, boolean scoreRequired, String encryptionKey) throws IOException, DocumentException {

		Context context = null;
		String html = null;
		DbOperations dbOperations = new DbOperations();
		Connection conn = dbOperations.getConnection(encryptionKey);
		
		try {			
			if(subjectType == 1) {				
				GetConsumerCribData consCrib = new GetConsumerCribData();
				ConsumerCribObject consCribObj = consCrib.getConsumerCribDataByDetailId(conn, requestDetailId);			
				ConsumerScoreObject consScoreObj = new ConsumerScoreObject();
				if(scoreRequired) {
					GetConsumerScoreData consScore = new GetConsumerScoreData();
					consScoreObj = consScore.getConsumerScoreDataByDetailId(conn, requestDetailId);
				}
						
				context = getConsumerContext(consCribObj, scoreRequired, consScoreObj);
		        html = loadAndFillTemplate(context, "cons_crib_report_pdf_template");
		        return renderPdf(html);
				
			} else {
				GetCorporateCribData corpCrib = new GetCorporateCribData();
				CorporateCribObject corpCribObj = corpCrib.getCorporateCribDataByDetailId(conn, requestDetailId);
				CorporateScoreObject corpScoreObj = new CorporateScoreObject();
				if(scoreRequired) {				
					GetCorporateScoreData corpScore = new GetCorporateScoreData();
					corpScoreObj = corpScore.getCorporateScoreDataByDetailId(conn, requestDetailId);
				}
				
				context = getCorporateContext(corpCribObj, scoreRequired, corpScoreObj);
		        html = loadAndFillTemplate(context, "corp_crib_report_pdf_template");
		        return renderPdf(html);
			}
			
		} catch (Exception e) {
			final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            
            e.printStackTrace();
            log.info("Exception occured : " + sw.toString());
            return "Crib Report Generation Error";
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

    private Context getConsumerContext(ConsumerCribObject cribData, Boolean scoreRequired, ConsumerScoreObject scoreData) {
        Context context = new Context();
        context.setVariable("requestMaster", cribData.getRequestMaster());
        context.setVariable("demographicDetails", cribData.getDemographicDetails());
        context.setVariable("mailingAddresses", cribData.getMailingAddresses());
        context.setVariable("permanentAddresses", cribData.getPermanentAddresses());
        context.setVariable("reportedNames", cribData.getReportedNames());
        context.setVariable("employmentDetails", cribData.getEmploymentDetails());
        context.setVariable("relationshipDetails", cribData.getRelationshipDetails());
        context.setVariable("settledCFDetails", cribData.getSettledCFDetails());
        context.setVariable("settledCFSummary", cribData.getSettledCFSummary());
        context.setVariable("lendingInstInquiries", cribData.getLendingInstInquiries());
        context.setVariable("inquiriesBySubject", cribData.getInquiriesBySubject());
        context.setVariable("creditFacilityDetails", cribData.getCreditFacilityDetails());
        context.setVariable("cFForLast24Months", cribData.getcFForLast24Months());
        context.setVariable("disputeDetails", cribData.getDisputeDetails());
        context.setVariable("potAndCurrLiabilities", cribData.getPotAndCurrLiabilities());
        context.setVariable("cFOfGlanceStatus", cribData.getcFOfGlanceStatus());
        context.setVariable("dishonChequeSummary", cribData.getDishonChequeSummary());
        context.setVariable("dishonChequeDetails", cribData.getDishonChequeDetails());
        context.setVariable("catalogueDescription", cribData.getCatalogueDescription());	
	    context.setVariable("pastMonths", cribData.getLast24Months());
	    context.setVariable("pastYears", cribData.getLast5Years());   

	    context.setVariable("scoreRequired", scoreRequired);
	    context.setVariable("scoreGeneralDetail", scoreData.getScoreGeneralDetails()); 
	    context.setVariable("cribScore", scoreData.getCribScore()); 
	    context.setVariable("scoreObservations", scoreData.getScoreObservations()); 
            
        return context;
    }
    
    private Context getCorporateContext(CorporateCribObject cribData, Boolean scoreRequired, CorporateScoreObject scoreData) {
        Context context = new Context();
        context.setVariable("requestMaster", cribData.getRequestMaster());
        context.setVariable("firmographicDetails", cribData.getFirmographicDetails());
        context.setVariable("mailingAddresses", cribData.getMailingAddresses());
        context.setVariable("permanentAddresses", cribData.getPermanentAddresses());
        context.setVariable("reportedNames", cribData.getReportedNames());
        context.setVariable("relationshipDetails", cribData.getRelationshipDetails());
        context.setVariable("settledCFDetails", cribData.getSettledCFDetails());
        context.setVariable("settledCFSummary", cribData.getSettledCFSummary());
        context.setVariable("lendingInstInquiries", cribData.getLendingInstInquiries());
        context.setVariable("inquiriesBySubject", cribData.getInquiriesBySubject());
        context.setVariable("creditFacilityDetails", cribData.getCreditFacilityDetails());
        context.setVariable("cFForLast24Months", cribData.getcFForLast24Months());
        context.setVariable("disputeDetails", cribData.getDisputeDetails());
        context.setVariable("potAndCurrLiabilities", cribData.getPotAndCurrLiabilities());
        context.setVariable("cFOfGlanceStatus", cribData.getcFOfGlanceStatus());
        context.setVariable("dishonChequeSummary", cribData.getDishonChequeSummary());
        context.setVariable("dishonChequeDetails", cribData.getDishonChequeDetails());
        context.setVariable("econActivityHistory", cribData.getEconActivityHistory());
        context.setVariable("catalogueDescription", cribData.getCatalogueDescription());
        context.setVariable("pastMonths", cribData.getLast24Months());
	    context.setVariable("pastYears", cribData.getLast5Years());
	    
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
    		CribReportPdfService asd= new CribReportPdfService();
    		System.out.println(asd.generatePdf(10000060, 1, true, "asdf-9kjh-qwe56"));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}

}
