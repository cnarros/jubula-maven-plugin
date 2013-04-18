package org.mule.tooling.jubula.tests;

/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mule.tooling.jubula.JubulaResultsMojo;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResult;

@RunWith(JUnit4.class)
public class JubulaMojoResultsTest extends AbstractMojoTestCase {

	private JubulaResultsMojo jubulaMojo;
	private String product = "org.mule.tooling:my-product:1.0";
	private RepositorySystem repoSystem;
	private RepositorySystemSession repoSession;
	private List<RemoteRepository> remoteRepos;
	private File workingDirectory;

	@Rule
	public TemporaryFolder folder = new TemporaryFolder();

	@Before
	public void setUp() throws Exception {
		jubulaMojo = new JubulaResultsMojo();
		MavenProject project = mock(MavenProject.class);
		repoSession = mock(RepositorySystemSession.class);
		repoSystem = mock(RepositorySystem.class);
		workingDirectory = folder.getRoot();
		remoteRepos = Arrays.asList(mock(RemoteRepository.class));

		ArtifactResult artifactResult = mock(ArtifactResult.class);

		Artifact artifact = mock(Artifact.class);

		URL resource = getClass().getClassLoader().getResource("hello.zip");

		if (resource == null) {
			throw new IllegalStateException("hello.zip cannot be null");
		}

		URI zipUri = resource.toURI();

		when(artifact.getFile()).thenReturn(new File(zipUri.getPath()));

		when(artifactResult.getArtifact()).thenReturn(artifact);

		when(repoSystem.resolveArtifact(any(RepositorySystemSession.class), any(ArtifactRequest.class))).thenReturn(artifactResult);

		// setVariableValueToObject(jubulaMojo, "project", project);
		// setVariableValueToObject(jubulaMojo, "repoSystem", repoSystem);
		// setVariableValueToObject(jubulaMojo, "repoSession", repoSession);
		// setVariableValueToObject(jubulaMojo, "remoteRepos", remoteRepos);
		// setVariableValueToObject(jubulaMojo, "workingDirectory",
		// workingDirectory);
	}

	@Test
	public void testOpenDocument() throws Exception {
		jubulaMojo.OpenAndPrepareXML();
	}

	@Test
	public void getNameOfTest() throws Exception {
		jubulaMojo.handResults();
		assertEquals("Create Project via Menu (projectName=loremipsum)", jubulaMojo.getTestName());
	}

	@Test
	public void getSuiteNameTest() throws Exception {
		jubulaMojo.handResults();
		assertEquals("Sanity Tests", jubulaMojo.getTestSuitName());
	}

	@Test
	public void getSuiteResultTest() throws Exception {
		jubulaMojo.handResults();
		assertEquals("ERROR", jubulaMojo.getTestSuitResult());
	}

	@Test
	public void getSuiteDurationTest() throws Exception {
		jubulaMojo.handResults();
		assertEquals("00:04:48", jubulaMojo.getTestSuitDuration());
	}
}
