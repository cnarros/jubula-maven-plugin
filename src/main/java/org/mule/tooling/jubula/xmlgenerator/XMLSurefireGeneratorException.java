package org.mule.tooling.jubula.xmlgenerator;

import org.apache.maven.surefire.report.ReporterException;

public class XMLSurefireGeneratorException extends Exception {

	public XMLSurefireGeneratorException(String message, ReporterException e) {
		super(message,e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 5139841127481825538L;

}
