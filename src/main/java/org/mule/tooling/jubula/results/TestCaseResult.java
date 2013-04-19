package org.mule.tooling.jubula.results;

import org.apache.maven.surefire.report.ReportEntry;
import org.mule.tooling.jubula.xmlgenerator.XMLSurefireReporter;

public class TestCaseResult {
	
	private String name;

	private String group;
	
	private String className;

	private Long duration;

	private TestRunResult testRunResult;

	public TestCaseResult(String name, String group, String className, Long duration, TestRunResult testRunResult) {
		if (name == null || duration == null || testRunResult == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.duration = duration;
		this.testRunResult = testRunResult;
		
		if(className == null){
			this.className = "";
		} else {
			this.className = className;
		}
		
		if(group == null){
			this.group = "";
		} else {
			this.group = group;
		}
	}
	
	public TestCaseResult(String name, String group, Long duration, TestRunResult testRunResult) {
		this(name,group, null, duration, testRunResult);
	}
	
	public TestCaseResult(String name, Long duration, TestRunResult testRunResult) {
		this(name, null, null, duration, testRunResult);
	}

	public String getName() {
		return name;
	}

	public String getGroup() {
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}

	public String getClassName() {
		return className;
	}

	public Long getDuration() {
		return duration;
	}

	public TestRunResult getTestRunResult() {
		return testRunResult;
	}

	public void report(XMLSurefireReporter reporter) {
		ReportEntry report = this.getReportEntry();
		
		reporter.testStarting(report);
		testRunResult.reportResult(reporter, report, duration);
	}
	
	private ReportEntry getReportEntry(){
		return new ReportEntry(this.className,this.name,this.group,"");
	}
	
}
