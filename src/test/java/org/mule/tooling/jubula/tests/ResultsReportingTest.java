package org.mule.tooling.jubula.tests;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.junit.Test;
import org.mule.tooling.jubula.results.TestSuiteResult;
import org.mule.tooling.jubula.xmlgenerator.XMLSurefireGenerator;
import org.mule.tooling.jubula.xmlgenerator.XMLSurefireGeneratorException;
import org.mule.tooling.jubula.xmlparser.XMLJubulaParser;
import org.mule.tooling.jubula.xmlparser.XMLJubulaParserException;

public class ResultsReportingTest {

	@Test
	public void testCompleteFlowOfReporting() throws MojoExecutionException {
		String jubulaResultsFolder = getClass().getClassLoader().getResource("results-jubula-test").getPath();
		String surefireResultsFolder = new File("target", "surefire-results").getAbsolutePath();

		XMLJubulaParser jubulaParser = new XMLJubulaParser();
		XMLSurefireGenerator surefireGenerator = new XMLSurefireGenerator(surefireResultsFolder);

		try {
			List<TestSuiteResult> testSuites = jubulaParser.generateSuitesFromFolder(jubulaResultsFolder);
			surefireGenerator.generateXML(testSuites);
		} catch (XMLJubulaParserException e) {
			throw new MojoExecutionException("There was a problem parsing the Jubula results", e);
		} catch (XMLSurefireGeneratorException e) {
			throw new MojoExecutionException("There was a problem generating the surefire results", e);
		}
	}

}
