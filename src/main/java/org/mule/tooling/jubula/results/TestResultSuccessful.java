package org.mule.tooling.jubula.results;

import org.apache.maven.surefire.report.ReportEntry;

public class TestResultSuccessful extends TestRunResult {

	@Override
	public void reportResult(XMLSurefireReporter reporter, ReportEntry report,
			Long duration) {
		reporter.testSucceeded(report);
	}

}
