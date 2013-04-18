package org.mule.tooling.jubula.results;

import java.io.File;

import org.apache.maven.surefire.report.ReporterException;
import org.junit.Before;
import org.junit.Test;

public class XMLSurefireReporterTest {

	private XMLSurefireReporter reporter;
	private TestSuiteResult testSuite;
	
	@Before
	public void setUp() {
		reporter = new XMLSurefireReporter(new File("example"));
		testSuite = new TestSuiteResult("TestSuite Name", "Project Name", 4000L);
		TestCaseResult testCase = new TestCaseResult("TestCase Name", "TestSuite Name", 3000L, new TestResultSuccessful());
		testSuite.addTestCaseResult(testCase);
	}
	
	@Test
	public void testReporter() throws ReporterException {
		testSuite.report(reporter);
	}

}
