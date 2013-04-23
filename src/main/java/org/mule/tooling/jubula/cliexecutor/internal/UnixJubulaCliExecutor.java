/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

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
