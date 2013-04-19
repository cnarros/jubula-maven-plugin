package org.mule.tooling.jubula.xmlparser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Node;

public class JubulaDocumentParser {
	private Document document;
	
	public JubulaDocumentParser(Document document){
		if(document == null){
			throw new IllegalArgumentException();
		}
		this.document = document;
	}
	
	public List<Node> getListOfResults() {
		@SuppressWarnings("unchecked")
		List<Node> nodes = document.selectNodes("//testsuite/test-run/testcase");
		return nodes;
	}
	
	public String getTestName() {
		Node node = document.selectSingleNode("//testsuite/test-run/testcase/name");
		Node nodeAtribute = document.selectSingleNode("//testsuite/test-run/testcase/parameter/parameter-value");

		return node.getStringValue() + " (projectName=" + nodeAtribute.getStringValue() + ")";
	}

	public String getTestNameByID(int secuencialID) {
		Node node = document.selectSingleNode("//testsuite/test-run/testcase[" + secuencialID + "]/name");
		Node nodeAtribute = document.selectSingleNode("//testsuite/test-run/testcase[" + secuencialID + "]/parameter/parameter-value");

		if (nodeAtribute == null) {
			return "Not Provided";
		}

		return node.getStringValue() + " (projectName=" + nodeAtribute.getStringValue() + ")";
	}

	public String getTestSuitName() {
		Node node = document.selectSingleNode("//testsuite/name");
		return node.getStringValue();
	}

	public String getProjectName() {
		Node node = document.selectSingleNode("//project/name");
		return node.getStringValue();
	}

	public long getTestSuitDuration() {
		Node node = document.selectSingleNode("//test-length");
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		Date date;
		try {
			date = sdf.parse(node.getStringValue());
			return date.getTime();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
	}
	
	public String getTestResultById(int secuencialID) {
		Node node = document.selectSingleNode("//testsuite/test-run/testcase[" + secuencialID + "]/status");

		if (node != null) {
			return node.getStringValue();
		}
		return "2";
	}
	
	public long getTestTestDurationById(int secuencialID) {
		Node node = document.selectSingleNode("//testsuite/test-run/testcase[" + secuencialID + "]/@duration");
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
		Date date;
		try {
			if (node != null) {
				date = sdf.parse(node.getStringValue());
				return date.getTime();
			}

			return 0;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}
	
}
