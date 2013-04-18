package org.mule.tooling.jubula.results;

import org.apache.maven.surefire.report.ReportEntry;

public abstract class TestRunResult {
	
	public abstract void reportResult(XMLSurefireReporter reporter, ReportEntry report, Long duration);

}
