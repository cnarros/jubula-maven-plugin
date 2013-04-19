package org.mule.tooling.jubula.cliexecutor.internal;

import org.mule.tooling.jubula.cliexecutor.Callback;
import org.mule.tooling.jubula.cliexecutor.JubulaCliExecutor;

public class UnixJubulaCliExecutor extends JubulaCliExecutor {

	@Override
	public void startAutAgent(Callback callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public void startAut(String autId, String rcpWorkingDir, String executableFileName, String workspacePath, String keyboardLayout, String autAgentHost, String autAgentPort,
			Callback callback) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean runTests(String projectName, String projectVersion, String autId, String databaseUrl, String databaseUser, String databasePassword, String autAgentHost,
			String autAgentPort, String language, String testJob, String datadir, String resultsDir) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean stopAutAgent() {
		// TODO Auto-generated method stub
		return false;
	}

}
