package org.mule.tooling.jubula.cliexecutor.internal;

import java.io.File;

import org.mule.tooling.jubula.cliexecutor.Callback;
import org.mule.tooling.jubula.cliexecutor.JubulaCliExecutor;

public class WindowsJubulaCliExecutor extends JubulaCliExecutor {

	private static String START_AUT_AGENT_FILENAME = "autagent.exe";
	private static String START_AUT_FILENAME = "autrun.exe";
	private String jubulaInstallationPath;
	
	public WindowsJubulaCliExecutor(String jubulaInstallationPath){
		this.jubulaInstallationPath = jubulaInstallationPath;
		setCliExecutor(new DefaultCliExecutor());
	}
	
	@Override
	public void startAutAgent(Callback callback) {
		CliExecutor cliExecutor = getCliExecutor();
		File startAutAgentFile = new File(jubulaInstallationPath, START_AUT_AGENT_FILENAME);
		cliExecutor.runAsync(startAutAgentFile, callback, null);
	}

	@Override
	public void startAut(String autId) {
		// TODO Auto-generated method stub
		
	}




}
