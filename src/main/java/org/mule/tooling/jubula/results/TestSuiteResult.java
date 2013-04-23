/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tooling.jubula.results;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.ReporterException;
import org.mule.tooling.jubula.xmlgenerator.XMLSurefireReporter;

public class TestSuiteResult {

	private String name;

	private String group;

	private Long duration;

	private List<TestCaseResult> testCaseResults;

	public TestSuiteResult(String name, String group, Long duration) {
		if (name == null || group == null || duration == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.group = group;
		this.duration = duration;
		this.testCaseResults = new ArrayList<TestCaseResult>();
	}
	
	public TestSuiteResult(String name, String group, Long duration, List<TestCaseResult> testCaseResults) {
		if (name == null || group == null || duration == null || testCaseResults == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.group = group;
		this.duration = duration;
		this.testCaseResults = testCaseResults;
	}

	public String getName() {
		return name;
	}

	public String getGroup() {
		return group;
	}

	public Long getDuration() {
		return duration;
	}

	public List<TestCaseResult> getTestCaseResults() {
		return testCaseResults;
	}
	
	public void setTestCaseResults(List<TestCaseResult> testCaseResults) {
		if (testCaseResults == null) {
			throw new IllegalArgumentException();
		}
		this.testCaseResults = testCaseResults;
	}
	
	public void addTestCaseResult(TestCaseResult testCase){
		if(testCase == null){
			throw new IllegalArgumentException();
		}
		
		if (testCaseResults == null) {
			throw new IllegalStateException();
		}
		
		testCase.setGroup(this.name);
		this.testCaseResults.add(testCase);
	}
	
	public void report(XMLSurefireReporter reporter) throws ReporterException{
		reporter.reset();
		reporter.testSetStarting(this.getReportEntry());
		
		for(TestCaseResult testCase : testCaseResults){
			testCase.report(reporter);
		}
		
		reporter.testSetCompleted(this.getReportEntry(), duration);
	}
	
	private ReportEntry getReportEntry(){
		return new ReportEntry("",this.name,this.group,"");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((testCaseResults == null) ? 0 : testCaseResults.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TestSuiteResult other = (TestSuiteResult) obj;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (group == null) {
			if (other.group != null)
				return false;
		} else if (!group.equals(other.group))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (testCaseResults == null) {
			if (other.testCaseResults != null)
				return false;
		} else if (!testCaseResults.equals(other.testCaseResults))
			return false;
		return true;
	}
	
}
