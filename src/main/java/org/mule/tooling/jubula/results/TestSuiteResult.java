package org.mule.tooling.jubula.results;

import java.util.ArrayList;
import java.util.List;

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

}
