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
