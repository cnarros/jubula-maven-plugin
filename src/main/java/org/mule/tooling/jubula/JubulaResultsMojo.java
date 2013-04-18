package org.mule.tooling.jubula;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import org.mule.tooling.jubula.results.TestCaseResult;
import org.mule.tooling.jubula.results.TestResultSuccessful;
import org.mule.tooling.jubula.results.TestSuiteResult;
import org.mule.tooling.jubula.results.XMLSurefireGenerator;

/**
 * Goal that runs Jubula Functional Tests.
 * 
 * @goal report-results
 * @phase post-integration-test
 */
public class JubulaResultsMojo extends AbstractMojo {

	/**
	 * Where the .js files will be located for running.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @readonly
	 * @required
	 */
	private File buildDirectory;

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

		String jubulaResultsFolder = buildDirectory + JubulaMavenPluginContext.RESULTS_DIRECTORY_NAME;
		String surefireResultsFolder = buildDirectory + JubulaMavenPluginContext.SUREFIRE_RESULTS_DIRECTORY_NAME;
		XMLSurefireGenerator generator;
		TestSuiteResult testSuite;

		try {
			handResults();
			String suitName = getTestSuitName();
			List<Node> listOfTestsResults = getListOfResults();
			// Create the output folder
			generator = new XMLSurefireGenerator("surefireResultsFolder");

			// Create the output folder
			testSuite = new TestSuiteResult(suitName, getProjectName(), getTestSuitDuration());
			int sequence = 0;
			System.out.println("size:" + listOfTestsResults.size());

			while (sequence < listOfTestsResults.size()) {

				TestCaseResult testCase = new TestCaseResult(getTestNameByID(sequence), getTestTestDurationById(sequence), new TestResultSuccessful());
				testSuite.addTestCaseResult(testCase);
				sequence++;

				System.out.println("result number:" + sequence);
			}

			generator.generateXML(testSuite);

		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public long getTestTestDurationById(int secuencialID) {
		Node node = getHandlerDocument().selectSingleNode("//testsuite/test-run/testcase[" + secuencialID + "]/@duration");
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

	public Document getHandlerDocument() {
		return handlerDocument;
	}

	public void setHandlerDocument(Document handlerDocument) {
		this.handlerDocument = handlerDocument;
	}

}
