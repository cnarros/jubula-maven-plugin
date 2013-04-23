/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tooling.jubula;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.mule.tooling.jubula.results.TestSuiteResult;
import org.mule.tooling.jubula.xmlgenerator.XMLSurefireGenerator;
import org.mule.tooling.jubula.xmlgenerator.XMLSurefireGeneratorException;
import org.mule.tooling.jubula.xmlparser.XMLJubulaParser;
import org.mule.tooling.jubula.xmlparser.XMLJubulaParserException;

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

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		String jubulaResultsFolder = buildDirectory + File.separator + JubulaMavenPluginContext.RESULTS_DIRECTORY_NAME;
		String surefireResultsFolder = buildDirectory + File.separator + JubulaMavenPluginContext.SUREFIRE_RESULTS_DIRECTORY_NAME;
		
		XMLJubulaParser jubulaParser = new XMLJubulaParser();		
		XMLSurefireGenerator surefireGenerator = new XMLSurefireGenerator(surefireResultsFolder);
		
		try {
			List<TestSuiteResult> testSuites = jubulaParser.generateSuitesFromFolder(jubulaResultsFolder);
			surefireGenerator.generateXML(testSuites);
		} catch (XMLJubulaParserException e) {
			e.printStackTrace();
		} catch (XMLSurefireGeneratorException e) {
			e.printStackTrace();
		}
	}

}
