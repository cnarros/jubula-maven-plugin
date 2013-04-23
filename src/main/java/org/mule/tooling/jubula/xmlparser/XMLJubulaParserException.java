/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tooling.jubula.xmlparser;

import org.dom4j.DocumentException;

public class XMLJubulaParserException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8559741366497712247L;
	
	public XMLJubulaParserException(String message, DocumentException e) {
		super(message, e);
	}

}
