package org.mule.tooling.jubula.cliexecutor;

import org.mule.tooling.jubula.cliexecutor.internal.UnixJubulaCliExecutor;
import org.mule.tooling.jubula.cliexecutor.internal.WindowsJubulaCliExecutor;

public class JubulaCliExecutorFactory {

	public JubulaCliExecutor getNewInstance(String jubulaInstallationPath) {
		String osName = System.getProperty("os.name");

		JubulaCliExecutor cliExecutor;
		if (osName.startsWith("Windows")) {
			cliExecutor = new WindowsJubulaCliExecutor(jubulaInstallationPath);
		} else if (osName.startsWith("Linux")) {
			cliExecutor = new UnixJubulaCliExecutor();
		} else if (osName.startsWith("Mac")) {
			cliExecutor = new UnixJubulaCliExecutor();
		} else
			throw new RuntimeException("unsupported OS: " + osName);
		
		return cliExecutor;
	}

}
