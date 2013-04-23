package org.mule.tooling.jubula.xmlparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.junit.BeforeClass;
import org.junit.Test;

public class JubulaDocumentParserTest {
	private static JubulaDocumentParser parser; 
	
	@BeforeClass
	public static void setUp() throws DocumentException{
		SAXReader reader = new SAXReader();
		
		URL fileURL = JubulaDocumentParserTest.class.getClassLoader().getResource("results-jubula-test" + File.separator + "jubulaTestResult1.xml");
		
		Document document = reader.read(fileURL.getPath());
		parser = new JubulaDocumentParser(document);
	}

	@Test
	public void getNameOfTest() throws Exception {
		assertEquals("Create Project via Menu (projectName=loremipsum)", parser.getTestName());
	}

	@Test
	public void getSuiteNameTest() throws Exception {
		assertEquals("Sanity Tests", parser.getTestSuitName());
	}

	@Test
	public void getSuiteResultTest() throws Exception {
		assertEquals("1", parser.getTestResultById(1));
	}

	@Test
	public void getSuiteDurationTest() throws Exception {
		assertEquals(288000, parser.getTestSuitDuration());
	}

	@Test
	public void getResultByNumberTest() throws Exception {
		assertEquals("Create Project via Menu (projectName=loremipsum)", parser.getTestNameByID(1));
	}

	@Test
	public void getResultWithErrorTest() throws Exception {
		assertEquals("Not Provided", parser.getTestNameByID(4));
	}

}
