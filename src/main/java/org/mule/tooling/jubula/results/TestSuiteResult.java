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
	
}
