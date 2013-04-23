/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tooling.jubula.cliexecutor.internal;

import java.io.File;
import java.io.IOException;

import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.ExecuteResultHandler;
import org.mule.tooling.jubula.cliexecutor.Callback;

public class DefaultCliExecutor implements CliExecutor {

	@Override
	public int run(File executable, String... params) {
		try {
			DefaultExecutor executor = new DefaultExecutor();
			CommandLine command = CommandLine.parse(executable.getAbsolutePath());
			command.addArguments(params, true);
			return executor.execute(command);
		} catch (ExecuteException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void runAsync(File executable, final Callback callback, String... params) {
		try {
			DefaultExecutor executor = new DefaultExecutor();
			CommandLine command = CommandLine.parse(executable.getAbsolutePath());
			command.addArguments(params, true);
			executor.execute(command, new ExecuteResultHandler() {

				@Override
				public void onProcessFailed(ExecuteException returnCode) {
					callback.failure(returnCode.getExitValue());
				}

				@Override
				public void onProcessComplete(int returnCode) {
					callback.success(returnCode);
				}
			});
		} catch (ExecuteException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
