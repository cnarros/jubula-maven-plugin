package org.mule.tooling.jubula.results;

import java.io.File;
import java.util.List;

import org.apache.maven.surefire.report.ReporterException;

public class XMLSurefireGenerator {
	
	private XMLSurefireReporter reporter;
	
	public XMLSurefireGenerator(String folder){
		if(folder == null){
			throw new IllegalArgumentException();
		}
		
		this.reporter = new XMLSurefireReporter(new File(folder));
	}
	
	public void generateXML(TestSuiteResult suite){
		try {
			suite.report(reporter);
		} catch (ReporterException e) {
			e.printStackTrace();
		}
	}
	
	public void generateXML(List<TestSuiteResult> suites){
		for(TestSuiteResult suite : suites){
			this.generateXML(suite);			
		}
	}

}
