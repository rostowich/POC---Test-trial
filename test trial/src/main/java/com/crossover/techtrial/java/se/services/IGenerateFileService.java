package com.crossover.techtrial.java.se.services;

import java.io.File;

import org.springframework.stereotype.Component;

/**
 * This interface groups all the business operations concerning the file export
 * @author 
 *
 */
@Component
public interface IGenerateFileService {

	/**
	 * This method generate the specified file 
	 * @param filename
	 * @return
	 */
	public File generateFile(String htmlTemplate) throws Exception;
}
