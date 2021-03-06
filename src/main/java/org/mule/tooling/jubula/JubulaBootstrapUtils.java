/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tooling.jubula;

import java.io.File;
import java.text.MessageFormat;

public abstract class JubulaBootstrapUtils {

	public static final String RCPWORKSPACE_DIRECTORY_NAME = "rcpworkspace";
	public static final String RESULTS_DIRECTORY_NAME = "results";
	public static final String JUBULA_BOOTSTRAP_VERSION = "1.3";
	public static final String SUREFIRE_RESULTS_DIRECTORY_NAME = "surefire-reports";
	
	public static String pathToServerPluginsDirectory(File buildDirectory) {
		return MessageFormat.format( //
				"{2}{1}jubula-bootstrap-{0}{1}server{1}plugins", //
				JUBULA_BOOTSTRAP_VERSION, //
				File.separator, //
				buildDirectory.getAbsolutePath());
	}

	public static String pathToJubulaPluginsDirectory(File buildDirectory) {
		return MessageFormat.format( //
				"{2}{1}jubula-bootstrap-{0}{1}jubula{1}plugins", //
				JUBULA_BOOTSTRAP_VERSION, //
				File.separator, //
				buildDirectory.getAbsolutePath());
	}

	public static String pathToJubulaInstallationDirectory(File buildDirectory) {
		return MessageFormat.format( //
				"{2}{1}jubula-bootstrap-{0}", //
				JUBULA_BOOTSTRAP_VERSION, //
				File.separator, //
				buildDirectory.getAbsolutePath());
	}

}
