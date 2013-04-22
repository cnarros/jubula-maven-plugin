package org.mule.tooling.jubula.xmlparser;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
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
	
	private Map<String, String> mapOfResult;
	
	private FilenameFilter fileFilter = new FilenameFilter(){
		
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".xml");
		}};

	
	public XMLJubulaParser(){
		this.mapOfResult = this.generateMap();
	}

	private Map<String, String> generateMap(){
		Map<String, String> mapOfResult = new HashMap<String, String>();
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
		
		return mapOfResult;
	}
	
	public List<TestSuiteResult> generateSuitesFromFolder(String folderName) throws XMLJubulaParserException {
		File folder = this.obtainFolder(folderName);
		List<TestSuiteResult> suites = new ArrayList<TestSuiteResult>();
		
		try {
			if(folder.isDirectory()){
				File [] files = folder.listFiles(fileFilter);
				suites = this.generateSuitesList(files);
			}
		} catch (DocumentException e) {
			throw new XMLJubulaParserException("XML Jubula parsing failed",e);
		}
		
		return suites;
	}
	
	private File obtainFolder(String folderName) {
		if(folderName == null){
			throw new IllegalArgumentException();
		}
		return new File(folderName);
	}

	public TestSuiteResult generateSuite(File file) throws DocumentException {
		Document document = this.openAndPrepareXML(file);
		JubulaDocumentParser documentParser =  new JubulaDocumentParser(document);
		
		String suitName = documentParser.getTestSuitName();
		List<Node> listOfTestsResults = documentParser.getListOfResults();

		TestSuiteResult testSuite = new TestSuiteResult(suitName, documentParser.getProjectName(), documentParser.getTestSuitDuration());
		
		int sequence = 1;
		
		while (sequence <= listOfTestsResults.size()) {
			TestCaseResult testCase = new TestCaseResult(documentParser.getTestNameByID(sequence), documentParser.getTestTestDurationById(sequence),
					generateRightResult(documentParser.getTestResultById(sequence)));
			testSuite.addTestCaseResult(testCase);
			sequence++;
		}
		
		return testSuite;
	}
	
	private List<TestSuiteResult> generateSuitesList(File [] files) throws DocumentException {
		List<TestSuiteResult> testSuites = new ArrayList<TestSuiteResult>();
		
		if(files != null){
			for(File file : files){
				TestSuiteResult testSuite = this.generateSuite(file);
				testSuites.add(testSuite);
			}
		}
		
		return testSuites;
	}
	
	private Document openAndPrepareXML(File file) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(file.getAbsolutePath());
		return document;
	}
	
	private TestRunResult generateRightResult(String status) {
		String code = mapOfResult.get(status);

		if(code == null){
			return new TestResultError();
		}
		
		if (mapOfResult.get(status).equals("SKIP")) {
			return new TestResultSkipped();
		}
		
		if (mapOfResult.get(status).equals("SUCCESS")) {
			return new TestResultSuccessful();
		}

		return new TestResultError();
	}

}
