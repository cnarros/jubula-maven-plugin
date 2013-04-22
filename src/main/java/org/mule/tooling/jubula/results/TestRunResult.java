package org.mule.tooling.jubula.results;

import org.apache.maven.surefire.report.ReportEntry;
import org.mule.tooling.jubula.xmlgenerator.XMLSurefireReporter;

public abstract class TestRunResult {
	protected String outMessage;
	
	public TestRunResult(){
	}
	
	public TestRunResult(String outMessage){
		this.outMessage = outMessage;
	}
	
	public abstract void reportResult(XMLSurefireReporter reporter, ReportEntry report, Long duration);
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((outMessage == null) ? 0 : outMessage.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestRunResult other = (TestRunResult) obj;
		if (outMessage == null) {
			if (other.outMessage != null)
				return false;
		} else if (!outMessage.equals(other.outMessage))
			return false;
		return true;
	}
	
}
