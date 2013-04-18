package org.mule.tooling.jubula.results;

import org.apache.maven.surefire.report.ReportEntry;

public class TestResultError extends TestRunResult {
	
	private String errorMessage;
	
	private String outMessage;
	
	@Override
	public void reportResult(XMLSurefireReporter reporter, ReportEntry report,
			Long duration) {
		reporter.testFailed(report, outMessage, errorMessage);
	}

}
