package org.mule.tooling.jubula.xmlparser;

import java.io.File;
import java.io.FilenameFilter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.mule.tooling.jubula.results.TestCaseResult;
import org.mule.tooling.jubula.results.TestResultError;
import org.mule.tooling.jubula.results.TestResultSkipped;
import org.mule.tooling.jubula.results.TestResultSuccessful;
import org.mule.tooling.jubula.results.TestRunResult;
import org.mule.tooling.jubula.results.TestSuiteResult;

public class XMLJubulaParser {
	
	private File folder;
	private Document currentDocument;
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
	
	private FilenameFilter fileFilter = new FilenameFilter(){
		
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".xml");
		}};

	
	public XMLJubulaParser(String folder){
		if(folder == null){
			throw new IllegalArgumentException();
		}
		
		this.folder = new File(folder);
	}		

	public List<TestSuiteResult> generateSuites() {
		List<TestSuiteResult> suites = new ArrayList<TestSuiteResult>();;
		
		try {
			File [] files = folder.listFiles(fileFilter);
			
			for(File file : files){
				currentDocument = this.openAndPrepareXML(file);
			
				String suitName = getTestSuitName();
				List<Node> listOfTestsResults = getListOfResults();

				TestSuiteResult testSuite = new TestSuiteResult(suitName, getProjectName(), getTestSuitDuration());
				
				int sequence = 1;

				while (sequence < listOfTestsResults.size()) {

					TestCaseResult testCase = new TestCaseResult(getTestNameByID(sequence), getTestTestDurationById(sequence),
							generateRithgResult(getTestResultById(sequence)));
					testSuite.addTestCaseResult(testCase);
					sequence++;
				}
				
				suites.add(testSuite);
			}

		} catch (DocumentException e) {
		}
		
		return suites;
	}
	
	private Document getHandlerDocument(){
		return currentDocument;
	}
	
	public Document openAndPrepareXML(File file) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(file.getAbsolutePath());
		return document;
	}
	
	public List<Node> getListOfResults() {

		@SuppressWarnings("unchecked")
		List<Node> nodes = getHandlerDocument().selectNodes("//testsuite/test-run/testcase");
		return nodes;
	}
	
	public String getTestName() {
		Node node = getHandlerDocument().selectSingleNode("//testsuite/test-run/testcase/name");
		Node nodeAtribute = getHandlerDocument().selectSingleNode("//testsuite/test-run/testcase/parameter/parameter-value");

		return node.getStringValue() + " (projectName=" + nodeAtribute.getStringValue() + ")";
	}

	public String getTestNameByID(int secuencialID) {
		Node node = getHandlerDocument().selectSingleNode("//testsuite/test-run/testcase[" + secuencialID + "]/name");
		Node nodeAtribute = getHandlerDocument().selectSingleNode("//testsuite/test-run/testcase[" + secuencialID + "]/parameter/parameter-value");

		if (nodeAtribute == null) {
			return "Not Provided";
		}

		return node.getStringValue() + " (projectName=" + nodeAtribute.getStringValue() + ")";
	}

	public String getTestSuitName() {
		Node node = getHandlerDocument().selectSingleNode("//testsuite/name");
		return node.getStringValue();
	}

	public String getProjectName() {
		Node node = getHandlerDocument().selectSingleNode("//project/name");
		return node.getStringValue();
	}

	public String getTestSuitResult() {
		Node node = getHandlerDocument().selectSingleNode("//testsuite/status");
		return mapOfResult.get(node.getStringValue());
	}

	public long getTestSuitDuration() {
		Node node = getHandlerDocument().selectSingleNode("//test-length");
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
		Node node = getHandlerDocument().selectSingleNode("//testsuite/test-run/testcase[" + secuencialID + "]/status");

		if (node != null) {
			return node.getStringValue();
		}

		return "2";
	}
	
	public long getTestTestDurationById(int secuencialID) {
		Node node = getHandlerDocument().selectSingleNode("//testsuite/test-run/testcase[" + secuencialID + "]/@duration");
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
	
	public TestRunResult generateRithgResult(String status) {

		if (mapOfResult.get(status).equals("ERROR")) {
			return new TestResultSuccessful();
		}

		if (mapOfResult.get(status).equals("SKIP")) {
			return new TestResultSkipped();
		}

		if (mapOfResult.get(status).equals("SUCCESS")) {
			return new TestResultError();
		} else
			return new TestResultError();

	}

}
