package org.mule.tooling.jubula.results;

import org.apache.maven.surefire.report.ReportEntry;
import org.mule.tooling.jubula.xmlgenerator.XMLSurefireReporter;

public class TestResultError extends TestRunResult {
	private String errorMessage;
	
	public TestResultError(){
		super("Undefined Error");
		this.errorMessage = this.outMessage;
	}
	
	public TestResultError(String outMessage, String errorMessage){
		super(outMessage);
		this.errorMessage = errorMessage;
	}
	
	@Override
	public void reportResult(XMLSurefireReporter reporter, ReportEntry report,
			Long duration) {
		reporter.testError(report, outMessage, errorMessage, duration);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((errorMessage == null) ? 0 : errorMessage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestResultError other = (TestResultError) obj;
		if (errorMessage == null) {
			if (other.errorMessage != null)
				return false;
		} else if (!errorMessage.equals(other.errorMessage))
			return false;
		return true;
	}

}
