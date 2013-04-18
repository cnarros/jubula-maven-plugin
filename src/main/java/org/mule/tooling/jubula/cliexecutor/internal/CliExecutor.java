package org.mule.tooling.jubula.cliexecutor.internal;

import java.io.File;

import org.mule.tooling.jubula.cliexecutor.Callback;

public interface CliExecutor {

	public int run(File executable, String... params);
	
	public void runAsync(File executable, Callback callback, String... params);
}