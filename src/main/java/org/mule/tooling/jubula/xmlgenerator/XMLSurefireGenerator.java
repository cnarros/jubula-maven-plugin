/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tooling.jubula.xmlgenerator;

import java.io.File;
import java.util.List;

import org.apache.maven.surefire.report.ReporterException;
import org.mule.tooling.jubula.results.TestSuiteResult;

public class XMLSurefireGenerator {
	
	private XMLSurefireReporter reporter;
	
	public XMLSurefireGenerator(String folder){
		if(folder == null){
			throw new IllegalArgumentException();
		}
		
		this.reporter = new XMLSurefireReporter(new File(folder));
	}
	
	public XMLSurefireGenerator(String folder, Boolean printProperties){
		this(folder);
		
		if(printProperties != null){
			reporter.setPrintProperties(printProperties);
		}
	}
	
	public void generateXML(TestSuiteResult suite) throws XMLSurefireGeneratorException{
		try {
			suite.report(reporter);
		} catch (ReporterException e) {
			throw new XMLSurefireGeneratorException("XML Surefire generation failed", e);
		}
	}
	
	public void generateXML(List<TestSuiteResult> suites) throws XMLSurefireGeneratorException{
		for(TestSuiteResult suite : suites){
			this.generateXML(suite);			
		}
	}

}
