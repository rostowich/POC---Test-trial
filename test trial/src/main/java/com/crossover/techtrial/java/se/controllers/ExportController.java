package com.crossover.techtrial.java.se.controllers;

import java.io.File;
import java.io.FileInputStream;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.crossover.techtrial.java.se.domain.Ticket;
import com.crossover.techtrial.java.se.exceptions.TicketNotFoundException;
import com.crossover.techtrial.java.se.services.IGenerateFileService;
import com.crossover.techtrial.java.se.services.ITemplatingService;
import com.crossover.techtrial.java.se.services.ITicketService;

@RestController
public class ExportController {

	private static final Logger logger = LoggerFactory.getLogger(ExportController.class);
	
	/**
	 * Name of the file template for PDF export
	 */
	private final String PDF_TEMPLATE="pdfTemplate";

	@Autowired
	private IGenerateFileService generateFileService;
	
	@Autowired
	private ITicketService ticketService;
	
	@Autowired
	private ITemplatingService templatingService;
	
	/**
	 * Service to download the ticket on pdf
	 * @param ticketId
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/pdf/{ticketId}", method=RequestMethod.GET, produces = "application/pdf")
	public @ResponseBody void downloadPdf(@PathVariable Long ticketId, HttpServletResponse response) throws Exception{
		
		logger.info("Getting the ticket");
		Ticket ticket=ticketService.getTicket(ticketId);
		if(ticket==null)
			throw new TicketNotFoundException();
		
		//Build the template
        String htmlTemplate=templatingService.buildHtmlTemplating(ticket, this.PDF_TEMPLATE);
		
		//get the file 
		File file=generateFileService.generateFile(htmlTemplate);
		FileInputStream fileInputStream =new FileInputStream(file);
		response.setHeader("Content-Disposition", "attachment; filename="+file.getCanonicalPath());
		response.setHeader("Content-Length", String.valueOf(file.length()));
		response.setContentType("application/pdf");
		
		FileCopyUtils.copy(fileInputStream, response.getOutputStream());
		response.flushBuffer();
		
	}
}
