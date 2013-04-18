package org.mule.tooling.jubula;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private static Map<String, String> mapOfResult;

	static {
		// Map of jubula errors to junit
		mapOfResult = new HashMap<String, String>();
		mapOfResult.put("2", "ERROR");
		mapOfResult.put("5", "ERROR");
		mapOfResult.put("9", "ERROR");
		mapOfResult.put("0", "SKIP");
		mapOfResult.put("4", "SKIP");
		mapOfResult.put("3", "SKIP");
		mapOfResult.put("6", "SKIP");
		mapOfResult.put("7", "SKIP");
		mapOfResult.put("1", "SUCCESS");
		mapOfResult.put("8", "SUCCESS");
		mapOfResult.put("other", "Assertion faild");
	}

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

	public String getTestSuitName() {
		Node node = getHandlerDocument().selectSingleNode("//testsuite/name");
		return node.getStringValue();
	}

	public String getTestSuitResult() {
		Node node = getHandlerDocument().selectSingleNode("//testsuite/status");
		return mapOfResult.get(node.getStringValue());
	}

	public String getTestSuitDuration() {
		Node node = getHandlerDocument().selectSingleNode("//test-length");
		return node.getStringValue();
	}

	public Document getHandlerDocument() {
		return handlerDocument;
	}

	public void setHandlerDocument(Document handlerDocument) {
		this.handlerDocument = handlerDocument;
	}

}
