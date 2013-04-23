/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tooling.jubula.cliexecutor.internal;

import java.io.File;

import org.mule.tooling.jubula.cliexecutor.Callback;

public interface CliExecutor {

	public int run(File executable, String... params);
	
	public void runAsync(File executable, Callback callback, String... params);
}