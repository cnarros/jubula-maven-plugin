package org.mule.tooling.jubula.cliexecutor.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.mule.tooling.jubula.cliexecutor.Callback;
import org.mule.tooling.jubula.cliexecutor.JubulaCliExecutor;

public class WindowsJubulaCliExecutor extends JubulaCliExecutor {

	private static String START_AUT_AGENT_FILENAME = "autagent.exe";
	private static String START_AUT_FILENAME = "autrun.exe";
	private static String TEST_EXEC_FILENAME = "testexec.exe";
	private String jubulaInstallationPath;

	public WindowsJubulaCliExecutor(String jubulaInstallationPath) {
		this.jubulaInstallationPath = jubulaInstallationPath;
		setCliExecutor(new DefaultCliExecutor());
	}

	@Override
	public void startAutAgent(Callback callback) {
		CliExecutor cliExecutor = getCliExecutor();
		File serverFolder = new File(jubulaInstallationPath, SERVER_FOLDER);
		File startAutAgentFile = new File(serverFolder, START_AUT_AGENT_FILENAME);
		cliExecutor.runAsync(startAutAgentFile, callback, new String[] {});
	}

	@Override
	public void startAut(String autId, String rcpWorkingDir, String executableFileName, String workspacePath, String keyboardLayout, String autAgentHost, String autAgentPort,
			Callback callback) {
		// START ./target/jubula-bootstrap-%8/server/autrun.exe -rcp
		// --workingdir %1 --exec %2 -data %7 --autid %3 --kblayout %4
		// --autagenthost %5 --autagentport %6
		CliExecutor cliExecutor = getCliExecutor();
		File serverFolder = new File(jubulaInstallationPath, SERVER_FOLDER);
		File startAutAgentFile = new File(serverFolder, START_AUT_FILENAME);
		List<String> params = new ArrayList<String>();
		params.add("--rcp");
		params.add(" --workingdir");
		params.add(rcpWorkingDir);
		params.add("--exec");
		params.add(executableFileName);
		params.add("-data");
		params.add(workspacePath);
		params.add("--autid");
		params.add(autId);
		params.add("--kblayout");
		params.add(keyboardLayout);
		params.add("--autagenthost");
		params.add(autAgentHost);
		params.add("--autagentport");
		params.add(autAgentPort);

		cliExecutor.runAsync(startAutAgentFile, callback, params.toArray(new String[] {}));
	}

	@Override
	public boolean runTests(String projectName, String projectVersion, String autId, String databaseUrl, String databaseUser, String databasePassword, String autAgentHost,
			String autAgentPort, String language, String testJob, String datadir, String minusData, String resultsDir) {
		CliExecutor cliExecutor = getCliExecutor();
		File jubulaFolder = new File(jubulaInstallationPath, JUBULA_FOLDER);
		File testExecFile = new File(jubulaFolder, TEST_EXEC_FILENAME);

		List<String> params = new ArrayList<String>();
		params.add("-project");
		params.add(projectName);
		params.add("-version");
		params.add(projectVersion);
		params.add("-autid");
		params.add(autId);
		params.add("-dburl");
		params.add(databaseUrl);
		params.add("-dbuser");
		params.add(databaseUser);
		params.add("-dbpw");
		params.add(databasePassword);
		params.add("-server");
		params.add(autAgentHost);
		params.add("-port");
		params.add(autAgentPort);
		params.add("-language");
		params.add(language);
		params.add("-testjob");
		params.add(testJob);
		params.add("-datadir");
		params.add(datadir);
		params.add("-data");
		params.add(minusData);
		params.add("-resultdir");
		params.add(resultsDir);

		return cliExecutor.run(testExecFile, params.toArray(new String[] {})) == 0;
	}
}
