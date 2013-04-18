package org.mule.tooling.jubula.results;

import org.apache.maven.surefire.report.ReportEntry;

public class TestResultError extends TestRunResult {

	private String outMessage;
	private String errorMessage;
	
	public TestResultError(String outMessage, String errorMessage){
		this.outMessage = outMessage;
		this.errorMessage = errorMessage;
	}
	
	@Override
	public void reportResult(XMLSurefireReporter reporter, ReportEntry report,
			Long duration) {
		reporter.testError(report, outMessage, errorMessage, duration);
	}

}
