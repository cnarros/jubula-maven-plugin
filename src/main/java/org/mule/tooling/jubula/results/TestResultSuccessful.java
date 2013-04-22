package org.mule.tooling.jubula.results;

import org.apache.maven.surefire.report.ReportEntry;
import org.mule.tooling.jubula.xmlgenerator.XMLSurefireReporter;

public class TestResultSuccessful extends TestRunResult {

	public TestResultSuccessful() {
	}

	public TestResultSuccessful(String outMessage) {
		super(outMessage);
	}

	@Override
	public void reportResult(XMLSurefireReporter reporter, ReportEntry report,
			Long duration) {
		reporter.testSucceeded(report, duration);
	}

}
