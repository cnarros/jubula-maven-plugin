package org.mule.tooling.jubula.xmlparser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.DocumentException;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mule.tooling.jubula.results.TestCaseResult;
import org.mule.tooling.jubula.results.TestResultSkipped;
import org.mule.tooling.jubula.results.TestResultSuccessful;
import org.mule.tooling.jubula.results.TestSuiteResult;

public class XMLJubulaParserTest {
	private static XMLJubulaParser parser;
	private static String folder;
	
	@BeforeClass
	public static void setUp(){
		parser = new XMLJubulaParser();
		folder = XMLJubulaParserTest.class.getClassLoader().getResource("results-jubula-test").getPath();
	}

	@Test
	public void generateSuiteTest() throws DocumentException {
		TestSuiteResult suite = parser.generateSuite(new File(folder + File.separator + "jubulaTestResult1.xml"));
		
		TestSuiteResult suiteExpected = new TestSuiteResult("Sanity Tests", "MuleStudio 1.0", 288000L);
		
		TestCaseResult testCase;

		testCase = new TestCaseResult("Create Project via Menu (projectName=loremipsum)", 41000L, new TestResultSuccessful());
		suiteExpected.addTestCaseResult(testCase);

		testCase = new TestCaseResult("Basic Usage (projectName=basicusage)", 51000L, new TestResultSuccessful());
		suiteExpected.addTestCaseResult(testCase);
		
		testCase = new TestCaseResult("Create Templates", 0L, new TestResultSkipped());
		suiteExpected.addTestCaseResult(testCase);
		
		assertEquals(suiteExpected, suite);
	}
	
	@Test
	public void generateSuitesTest() throws XMLJubulaParserException {
		List<TestSuiteResult> suites = parser.generateSuitesFromFolder(folder);
		
		List<TestSuiteResult> suitesExpected = new ArrayList<TestSuiteResult>();
		
		TestSuiteResult suiteExpected;
		TestCaseResult testCase;
		
		suiteExpected = new TestSuiteResult("Sanity Tests", "MuleStudio 1.0", 288000L);

		testCase = new TestCaseResult("Create Project via Menu (projectName=loremipsum)", 41000L, new TestResultSuccessful());
		suiteExpected.addTestCaseResult(testCase);

		testCase = new TestCaseResult("Basic Usage (projectName=basicusage)", 51000L, new TestResultSuccessful());
		suiteExpected.addTestCaseResult(testCase);
		
		testCase = new TestCaseResult("Create Templates", 0L, new TestResultSkipped());
		suiteExpected.addTestCaseResult(testCase);
		
		suitesExpected.add(suiteExpected);
		
		suiteExpected = new TestSuiteResult("Sanity Tests 2", "MuleStudio 1.0", 288000L);

		testCase = new TestCaseResult("Create Project via Menu (projectName=loremipsum)", 41000L, new TestResultSuccessful());
		suiteExpected.addTestCaseResult(testCase);

		testCase = new TestCaseResult("Basic Usage (projectName=basicusage)", 51000L, new TestResultSuccessful());
		suiteExpected.addTestCaseResult(testCase);
		
		testCase = new TestCaseResult("Create Templates", 0L, new TestResultSkipped());
		suiteExpected.addTestCaseResult(testCase);
		
		testCase = new TestCaseResult("Create Templates", 0L, new TestResultSkipped());
		suiteExpected.addTestCaseResult(testCase);
		
		suitesExpected.add(suiteExpected);
		
		assertEquals(suitesExpected, suites);
	}

}
