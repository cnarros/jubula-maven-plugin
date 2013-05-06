/**
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */

package org.mule.tooling.jubula;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.util.FileUtils;
import org.mule.tooling.jubula.utils.MavenDependencyUtils;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;

/**
 * Goal that runs Jubula Functional Tests.
 * 
 * @goal prepare
 * @phase pre-integration-test
 */
public class JubulaPrepareMojo extends AbstractMojo {

	/**
	 * This exists in this version of the plugin because the existing
	 * implementation of the Bootstrap does not have the rc plugin bundled. It
	 * should be included in the bootstrap and taken from there.
	 */
	private static final String BUNDLED_JUBULA_RC_PLUGIN_FILENAME = "org.eclipse.jubula.rc.rcp_1.2.1.201206131127.jar";

	/**
	 * The entry point to Aether, i.e. the component doing all the work.
	 * 
	 * @component
	 */
	private RepositorySystem repoSystem;

	/**
	 * The current repository/network configuration of Maven.
	 * 
	 * @parameter default-value="${repositorySystemSession}"
	 * @readonly
	 */
	private RepositorySystemSession repoSession;

	/**
	 * The project's remote repositories to use for the resolution of
	 * dependencies.
	 * 
	 * @parameter default-value="${project.remoteArtifactRepositories}"
	 * @readonly
	 */
	private List<MavenArtifactRepository> remoteRepos;

	/**
	 * Project's target folder.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @readonly
	 * @required
	 */
	private File buildDirectory;

	/**
	 * RCP target directory.
	 * 
	 * @parameter
	 * @required
	 */
	private String rcpTargetDirectory;

	/**
	 * A list of plugins (intended for jubula accessibility plugins) to install
	 * into the RCP app being tested. These dependencies should resolve to jar files
	 * that are copied into the plugins folder of the RCP.
	 * 
	 * @parameter
	 */
	private List<AdditionalPlugin> additionalPlugins;

	/**
	 * An artifact that provides a jubula-bootstrap zip package. If none
	 * provided, it will default to the tooling implementation
	 * (org.mule.tooling:jubula-bootstrap:<version>)
	 * 
	 * @parameter
	 */
	private JubulaBootstrapArtifact jubulaBootstrap;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		createTestWorkingDirectories();
		prepareJubulaInstallation();
		copyUserDefinedPluginsToRcp();
	}

	private void prepareJubulaInstallation() throws MojoExecutionException {
		Dependency jubulaBootsrapDependency = getJubulaBootsrapDependency();

		getLog().info("Preparing Jubula installation, using artifact: " + MavenDependencyUtils.toString(jubulaBootsrapDependency));
		final File jubula = fetchArtifact(jubulaBootsrapDependency);

		getLog().info("Extracting Jubula installation");
		extract(jubula, buildDirectory);

		getLog().info("Jubula expanded, now copying redundant plugins");
		copyServerJubulaPluginsToJubulaPlugins();
	}

	private void createTestWorkingDirectories() {
		createFile(JubulaBootstrapUtils.RCPWORKSPACE_DIRECTORY_NAME);
		createFile(JubulaBootstrapUtils.RESULTS_DIRECTORY_NAME);
	}

	private void copyServerJubulaPluginsToJubulaPlugins() throws MojoExecutionException {
		try {
			String pathToServerPluginsDirectory = JubulaBootstrapUtils.pathToServerPluginsDirectory(buildDirectory);
			String pathToJubulaPluginsDirectory = JubulaBootstrapUtils.pathToJubulaPluginsDirectory(buildDirectory);
			getLog().info("Copying everything from " + pathToServerPluginsDirectory + " to " + pathToJubulaPluginsDirectory);

			FileUtils.copyDirectory( //
					new File(pathToServerPluginsDirectory), //
					new File(pathToJubulaPluginsDirectory));

		} catch (final IOException e) {
			throw new MojoExecutionException("Error copying jubula plugins. Verify that the Jubula bootstrap provided is well-formed.", e);
		}
	}

	private void copyUserDefinedPluginsToRcp() throws MojoExecutionException {
		try {
			final File pluginsDirectory = new File(rcpTargetDirectory, "plugins");

			// TODO - Until the RC is uploaded to Maven or shipped with Jubula
			// bootstrap, we are incluiding it here - miguel
			final File out = new File(pluginsDirectory, BUNDLED_JUBULA_RC_PLUGIN_FILENAME);
			getLog().info("Copying RC plugin (" + BUNDLED_JUBULA_RC_PLUGIN_FILENAME + ") to " + out.getAbsolutePath());
			IOUtils.copy(getRemoteControlArtifactStream(), new FileOutputStream(out));

			if (additionalPlugins != null && additionalPlugins.size() > 0) {
				for (final Dependency additionalPlugin : additionalPlugins) {
					getLog().info("Fetching additional plugin for RCP app: " + MavenDependencyUtils.toString(additionalPlugin));
					File artifact = fetchArtifact(additionalPlugin);
					getLog().info("Copying " + artifact.getName() + " to " + pluginsDirectory.getAbsolutePath());
					FileUtils.copyFileToDirectory(artifact, pluginsDirectory);
				}
				getLog().info("Finished copying additional plugins to RCP app");
			} else {
				getLog().info("No additional plugins to copy to RCP app");
			}

		} catch (final IOException e) {
			throw new MojoExecutionException("Error copying plugins. Verify that the RCP target directory is correct.", e);
		}
	}

	protected InputStream getRemoteControlArtifactStream() {
		return getClass().getClassLoader().getResourceAsStream(BUNDLED_JUBULA_RC_PLUGIN_FILENAME);
	}

	private Dependency getJubulaBootsrapDependency() {
		Dependency dependency;
		if (jubulaBootstrap != null) {
			dependency = jubulaBootstrap;
		} else {
			dependency = createDependency("org.mule.tooling", "jubula-bootstrap", JubulaBootstrapUtils.JUBULA_BOOTSTRAP_VERSION, "zip");
		}
		return dependency;
	}

	protected Dependency createDependency(final String groupId, final String artifactId, final String version, final String type) {
		final Dependency dependency = new Dependency();
		dependency.setGroupId(groupId);
		dependency.setArtifactId(artifactId);
		dependency.setVersion(version);
		dependency.setType(type);
		return dependency;
	}

	private File fetchArtifact(final Dependency dependency) throws MojoExecutionException {

		final ArtifactRequest request = new ArtifactRequest();
		request.setArtifact(dependencyToArtifact(dependency));

		final List<RemoteRepository> aetherRepos = mavenArtifactRepositories2aetherRepositories();

		request.setRepositories(aetherRepos);

		getLog().debug("Resolving artifact " + dependency + " from " + aetherRepos);

		ArtifactResult result;
		try {
			result = repoSystem.resolveArtifact(repoSession, request);
		} catch (final ArtifactResolutionException e) {
			getLog().error("There was a problem fetching artifact: " + MavenDependencyUtils.toString(dependency));
			throw new MojoExecutionException(e.getMessage(), e);
		}

		final Artifact artifact = result.getArtifact();

		getLog().debug("Resolved artifact " + artifact + " to " + result.getArtifact().getFile() + " from " + result.getRepository());

		return artifact.getFile();
	}

	protected List<RemoteRepository> mavenArtifactRepositories2aetherRepositories() {
		final List<RemoteRepository> aetherRepos = new ArrayList<RemoteRepository>();
		for (final MavenArtifactRepository repo : remoteRepos) {
			final RemoteRepository remoteRepository = new RemoteRepository(repo.getId(), repo.getLayout().getId(), repo.getUrl());
			aetherRepos.add(remoteRepository);
		}
		return aetherRepos;
	}

	protected DefaultArtifact dependencyToArtifact(final Dependency dependency) {
		return new DefaultArtifact( //
				dependency.getGroupId(), //
				dependency.getArtifactId(), //
				dependency.getType(), //
				dependency.getVersion());
	}

	public void extract(final File file, final File extractDirectory) {
		createFile(extractDirectory);

		final ZipUnArchiver zipUnArchiver = new ZipUnArchiver(file);
		// need to set a logger, because if not, a NPE will be raised by
		// extract() (shame on Plexus)
		zipUnArchiver.enableLogging(new org.codehaus.plexus.logging.console.ConsoleLogger(0, "Unzipping"));

		zipUnArchiver.setDestDirectory(extractDirectory);
		getLog().info("Extracting " + file.getName() + " into " + extractDirectory.getAbsolutePath());
		zipUnArchiver.extract();
	}

	protected void createFile(final String fileToBeCreatedName) {
		final File fileToBeCreated = new File(buildDirectory, fileToBeCreatedName);
		getLog().info("Preparing directory: " + fileToBeCreatedName);
		createFile(fileToBeCreated);
	}

	protected void createFile(final File fileToBeCreated) {
		if (!fileToBeCreated.exists())
			fileToBeCreated.mkdirs();
	}
}
