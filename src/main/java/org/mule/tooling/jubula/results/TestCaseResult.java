/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((group == null) ? 0 : group.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((testRunResult == null) ? 0 : testRunResult.hashCode());
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
		TestCaseResult other = (TestCaseResult) obj;
		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;
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
		if (testRunResult == null) {
			if (other.testRunResult != null)
				return false;
		} else if (!testRunResult.equals(other.testRunResult))
			return false;
		return true;
	}
	
}
