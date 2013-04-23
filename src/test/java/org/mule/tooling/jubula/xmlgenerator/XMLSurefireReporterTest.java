package org.mule.tooling.jubula.xmlgenerator;

import static junitx.framework.FileAssert.assertBinaryEquals;

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

public class XMLSurefireReporterTest {

	private XMLSurefireReporter reporter;
	private TestSuiteResult testSuite;

	@Before
	public void setUp() {
		reporter = new XMLSurefireReporter(new File("testFiles" + File.separator + JubulaMavenPluginContext.SUREFIRE_RESULTS_DIRECTORY_NAME));
		reporter.setPrintProperties(false);
		
		testSuite = new TestSuiteResult("TestReporter", "Project Name", 4000L);
		
		TestCaseResult testCase;

		testCase = new TestCaseResult("TestCase Name1", 11088000L, new TestResultSuccessful());
		testSuite.addTestCaseResult(testCase);

		testCase = new TestCaseResult("TestCase Name2", 3000L, new TestResultError());
		testSuite.addTestCaseResult(testCase);
		
		testCase = new TestCaseResult("TestCase Name3", 3000L, new TestResultSkipped());
		testSuite.addTestCaseResult(testCase);
	}

	@Test
	public void testReporter() throws ReporterException {
		testSuite.report(reporter);
		String filespath = "testFiles" + File.separator + JubulaMavenPluginContext.SUREFIRE_RESULTS_DIRECTORY_NAME;
		File actual = new File(filespath + File.separator + "TEST-TestReporter.xml");
		File expected = new File(filespath + File.separator + "expected" + File.separator + "TEST-TestReporter-expected.xml");
		
		assertBinaryEquals(expected, actual);
	}

}
