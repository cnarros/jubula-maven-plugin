package org.mule.tooling.jubula.results;

public class TestCaseResult {
	
	private String name;

	private String group;
	
	private String className;

	private Long duration;

	private TestRunResult testRunResult;

	public TestCaseResult(String name, String group, String className, Long duration, TestRunResult testRunResult) {
		if (name == null || group == null || duration == null || testRunResult == null) {
			throw new IllegalArgumentException();
		}

		this.name = name;
		this.className = className;
 		this.group = group;
		this.duration = duration;
		this.testRunResult = testRunResult;
	}
	
	public TestCaseResult(String name, String group, Long duration, TestRunResult testRunResult) {
		this(name,group, null, duration, testRunResult);
	}

	public String getName() {
		return name;
	}

	public String getGroup() {
		return group;
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
	
}
