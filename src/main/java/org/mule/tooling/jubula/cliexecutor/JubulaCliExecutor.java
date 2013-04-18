package org.mule.tooling.jubula.cliexecutor;

import org.mule.tooling.jubula.cliexecutor.internal.CliExecutor;

public abstract class JubulaCliExecutor {
	private CliExecutor cliExecutor;

	public CliExecutor getCliExecutor() {
		return cliExecutor;
	}

	protected void setCliExecutor(CliExecutor cliExecutor) {
		this.cliExecutor = cliExecutor;
	}

	public abstract void startAutAgent(Callback callback);
	
	public abstract void startAut(String autId);
}
