package com.crossover.techtrial.java.se.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * This class is used to  test GeneratePdfFileService class
 * @author
 *
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class GeneratePdfFileServiceTest {

	@Autowired
	private IGenerateFileService generateFileService;
	
	@Test
	public void generateFileReturnAFile() throws Exception{
		File file=generateFileService.generateFile("HtmlTemplate");
		assertThat(file.getPath()).contains(".pdf");
		assertThat(file.getClass()).isEqualTo(File.class);
		assertThat(file.isFile());
		assertThat(file.exists());
	}
}
