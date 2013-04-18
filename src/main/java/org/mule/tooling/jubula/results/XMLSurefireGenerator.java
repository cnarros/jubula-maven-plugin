package org.mule.tooling.jubula.results;

import java.io.File;

public class XMLSurefireGenerator {
	
	private File file;
	
	public XMLSurefireGenerator(String folder){
		if(folder == null){
			throw new IllegalArgumentException();
		}
		
		this.file = new File(folder);
	}
	
	public void generateXML(){
		
	}

}
