/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

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
