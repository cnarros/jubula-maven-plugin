package org.mule.tooling.jubula.xmlgenerator;

import java.io.File;

import org.apache.maven.surefire.report.ReporterException;
import org.junit.Before;
import org.junit.Test;
import org.mule.tooling.jubula.JubulaMavenPluginContext;
import org.mule.tooling.jubula.results.TestCaseResult;
import org.mule.tooling.jubula.results.TestResultError;
import org.mule.tooling.jubula.results.TestResultSkipped;
import org.mule.tooling.jubula.results.TestResultSuccessful;
import org.mule.tooling.jubula.results.TestSuiteResult;

public class XMLSurefireGeneratorTest {
	private XMLSurefireGenerator generator;
	private TestSuiteResult testSuite;
	
	@Before
	public void setUp() {
		generator = new XMLSurefireGenerator("testFiles" + File.separator + JubulaMavenPluginContext.SUREFIRE_RESULTS_DIRECTORY_NAME, false);
		testSuite = new TestSuiteResult("TestGenerator", "Project Name", 4000L);
		
		TestCaseResult testCase;
		
		testCase = new TestCaseResult("TestCase Name1", 3000L, new TestResultSuccessful());
		testSuite.addTestCaseResult(testCase);

		testCase = new TestCaseResult("TestCase Name2", 3000L, new TestResultError());
		testSuite.addTestCaseResult(testCase);
		
		testCase = new TestCaseResult("TestCase Name3", 3000L, new TestResultSkipped());
		testSuite.addTestCaseResult(testCase);
	}
	
	@Test
	public void testGenerator() throws ReporterException, XMLSurefireGeneratorException {
		generator.generateXML(testSuite);
	}

}
