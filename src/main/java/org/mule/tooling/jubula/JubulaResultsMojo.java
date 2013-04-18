package org.mule.tooling.jubula;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * Goal that runs Jubula Functional Tests.
 * 
 * @goal report-results
 * @phase post-integration-test
 */
public class JubulaResultsMojo extends AbstractMojo {
	private String archiveSource = getClass().getClassLoader().getResource("executionLog.xml").getFile();
	private Document handlerDocument;
    
	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		// TODO Auto-generated method stub
        
	}
    
	public void handResults() throws DocumentException {
		Document document = OpenAndPrepareXML();
	}
    
	public Document OpenAndPrepareXML() throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(archiveSource);
		setHandlerDocument(document);
		return document;
	}
    
	private void forExample(Document document) {
		// using Xpath
		List list = document.selectNodes("//foo/bar");
        
		Node node = document.selectSingleNode("//foo/bar/author");
        
		node.getStringValue();
        
		String name = node.valueOf("@name");
        
	}
    
	public String getTestName() {
		Node node = getHandlerDocument().selectSingleNode("//testsuite/test-run/testcase/name");
		Node nodeAtribute = getHandlerDocument().selectSingleNode("//testsuite/test-run/testcase/parameter/parameter-value");
        
		return node.getStringValue() + " (projectName=" + nodeAtribute.getStringValue() + ")";
	}
    
	public String getTestByName() {
		Node node = getHandlerDocument().selectSingleNode("//testsuite/test-run/testcase/name");
        
		return node.getStringValue();
	}
    
	public Document getHandlerDocument() {
		return handlerDocument;
	}
    
	public void setHandlerDocument(Document handlerDocument) {
		this.handlerDocument = handlerDocument;
	}
    
}

