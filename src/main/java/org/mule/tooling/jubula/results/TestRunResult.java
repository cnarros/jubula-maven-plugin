package org.mule.tooling.jubula.results;

import org.apache.maven.surefire.report.ReportEntry;
import org.mule.tooling.jubula.xmlgenerator.XMLSurefireReporter;

public abstract class TestRunResult {
	
	public abstract void reportResult(XMLSurefireReporter reporter, ReportEntry report, Long duration);

}
