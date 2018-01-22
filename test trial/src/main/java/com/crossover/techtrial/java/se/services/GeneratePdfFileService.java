package com.crossover.techtrial.java.se.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * This class implement the IGenerateFileService to implement the pdf generate file to export
 * If we have to generate for instance the excel file, we need to create a service implementing
 * this IGenerateFileService and redefine the generateFile method corresponding to excel file
 * @author
 *
 */
@Service
public class GeneratePdfFileService implements IGenerateFileService{

	/**
	 * Path where the pdf file will be created in order to download them
	 */
	private final String FILE_UPLOAD_PATH="src/main/resources/static/upload/ticket";
	
	private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);
	

	/*
	 * (non-Javadoc)
	 * @see com.crossover.techtrial.java.se.services.IGenerateFileService#generateFile(java.lang.String)
	 */
	@Override
	public File generateFile(String htmlTemplate) throws Exception {
		logger.info("Generating the pdf file");
		
		//Build the file name
		Long longDate=new Date().getTime();
		String fileName=this.FILE_UPLOAD_PATH+"_"+longDate+".pdf";
				
		Document myPdf = new Document(PageSize.A4,10, 10, 10, 15);
		File file=new File(fileName);
		FileOutputStream outputStream=new FileOutputStream(file);
		//Creation of the pdf document
		PdfWriter pdfWriter=PdfWriter.getInstance(myPdf, outputStream);
		myPdf.open();
		HTMLWorker htmlWorker=new HTMLWorker(myPdf);
		//Convert the html template to pdf
		htmlWorker.parse(new StringReader(htmlTemplate));
		myPdf.close();
		pdfWriter.close();
		return file;
	}
}
