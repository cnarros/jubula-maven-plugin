package org.mule.tooling.jubula.results;

import org.apache.maven.surefire.report.ReportEntry;
import org.mule.tooling.jubula.xmlgenerator.XMLSurefireReporter;

public class TestResultSkipped extends TestRunResult {

	@Override
	public void reportResult(XMLSurefireReporter reporter, ReportEntry report,
			Long duration) {
		reporter.testSkipped(report, duration);
	}

}
