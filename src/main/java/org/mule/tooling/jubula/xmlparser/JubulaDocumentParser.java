/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tooling.jubula.xmlparser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

		String name = "Not Provided";
		
		if(node != null){
			name = node.getStringValue();
			if (nodeAtribute != null) {
				name += " (projectName=" + nodeAtribute.getStringValue() + ")";
			}
		}

		return name;
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
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date;
		try {
			date = sdf.parse(node.getStringValue());
			return date.getTime();
		} catch (ParseException e) {
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
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
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
