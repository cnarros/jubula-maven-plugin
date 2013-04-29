/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tooling.jubula.utils;

import org.apache.maven.model.Dependency;

public class MavenDependencyUtils {

	public static String toString(Dependency dep) {
		String gav = dep.getGroupId() + ":" + dep.getArtifactId() + ":" + dep.getVersion();
		if (dep.getType() != null) {
			gav += dep.getType();
		}
		if (dep.getClassifier() != null) {
			gav += dep.getClassifier();
		}
		return gav;
	}
}
