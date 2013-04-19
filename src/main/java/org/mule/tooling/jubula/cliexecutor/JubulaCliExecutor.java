package org.mule.tooling.jubula.cliexecutor;

import org.mule.tooling.jubula.cliexecutor.internal.CliExecutor;

public abstract class JubulaCliExecutor {
	private CliExecutor cliExecutor;
	protected static final String SERVER_FOLDER = "server";
	protected static final String JUBULA_FOLDER = "jubula";

	public CliExecutor getCliExecutor() {
		return cliExecutor;
	}

	protected void setCliExecutor(CliExecutor cliExecutor) {
		this.cliExecutor = cliExecutor;
	}

	public abstract void startAutAgent(Callback callback);

	public abstract void startAut(String autId, String rcpWorkingDir, String executableFileName, String workspacePath, String keyboardLayout, String autAgentHost,
			String autAgentPort, Callback callback);

	public abstract boolean runTests(String projectName, String projectVersion, String autId, String databaseUrl, String databaseUser, String databasePassword,
			String autAgentHost, String autAgentPort, String language, String testJob, String datadir, String resultsDir);

	public abstract boolean stopAutAgent(); 
}
