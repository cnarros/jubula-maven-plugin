package org.mule.tooling.jubula.xmlparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.List;

import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.tooling.jubula.results.TestSuiteResult;

public class XMLJubulaParserTest {
	private static XMLJubulaParser parser;
	
	@BeforeClass
	public static void setUp(){
		parser = new XMLJubulaParser();
	}

	@Test
	public void generateSuiteTest() throws DocumentException {
		TestSuiteResult suite = parser.generateSuite(new File("testFiles" + File.separator + "results" + File.separator + "jubulaTestResult1.xml"));
		
		TestSuiteResult suiteExpected = new TestSuiteResult("Sanity Tests", "MuleStudio 1.0", 11088000L);
		assertEquals(suiteExpected.getName(), suite.getName());
		assertEquals(suiteExpected.getGroup(), suite.getGroup());
		assertEquals(suiteExpected.getDuration(), suite.getDuration());
	}
	
	@Test
	public void generateSuitesTest() throws XMLJubulaParserException {
		List<TestSuiteResult> suites = parser.generateSuitesFromFolder("testFiles" + File.separator + "results");
		
		TestSuiteResult suite;
		TestSuiteResult suiteExpected;
		
		suite = suites.get(0);
		suiteExpected = new TestSuiteResult("Sanity Tests", "MuleStudio 1.0", 11088000L);
		
		assertEquals(suiteExpected.getName(), suite.getName());
		assertEquals(suiteExpected.getGroup(), suite.getGroup());
		assertEquals(suiteExpected.getDuration(), suite.getDuration());
		
		suiteExpected = new TestSuiteResult("Sanity Tests 2", "MuleStudio 1.0", 11088000L);
		suite = suites.get(1);
		
		assertEquals(suiteExpected.getName(), suite.getName());
		assertEquals(suiteExpected.getGroup(), suite.getGroup());
		assertEquals(suiteExpected.getDuration(), suite.getDuration());
	}

}
