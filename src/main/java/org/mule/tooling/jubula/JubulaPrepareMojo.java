package org.mule.tooling.jubula;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.Format;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.archiver.zip.ZipUnArchiver;
import org.codehaus.plexus.util.FileUtils;
import org.sonatype.aether.RepositorySystem;
import org.sonatype.aether.RepositorySystemSession;
import org.sonatype.aether.artifact.Artifact;
import org.sonatype.aether.repository.RemoteRepository;
import org.sonatype.aether.resolution.ArtifactRequest;
import org.sonatype.aether.resolution.ArtifactResolutionException;
import org.sonatype.aether.resolution.ArtifactResult;
import org.sonatype.aether.util.artifact.DefaultArtifact;

import static org.mule.tooling.jubula.JubulaMavenPluginContext.*;

/**
 * Goal that runs Jubula Functional Tests.
 * 
 * @goal prepare
 * @phase pre-integration-test
 */
public class JubulaPrepareMojo extends AbstractMojo {

	/**
	 * 
	 * Project being built.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 */
	private MavenProject project;

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
	 * The project's remote repositories to use for the resolution of plugins
	 * and their dependencies.
	 * 
	 * @parameter default-value="${project.remotePluginRepositories}"
	 * @readonly
	 */
	private List<RemoteRepository> remoteRepos;

	/**
	 * Where the .js files will be located for running.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @readonly
	 * @required
	 */
	private File buildDirectory;

	/**
	 * RCP target directory.
	 * 
	 * @parameter expression="${myRcp}" default-value="asdf"
	 * @required
	 */
	private String rcpTargetDirectory;

	/**
	 * @parameter
	 */
	private List<Dependency> jubulaPlugins;

	/**
	 * ZipUnArchiver
	 * 
	 * @component
	 */
	private ZipUnArchiver zipUnArchiver;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		getLog().info("Prepare Jubula Mojo starting");

		initializeContext(buildDirectory);

		getLog().info("JUBULA INST = " + pathToJubulaInstallationDirectory());
		getLog().info("JUBULA PLUG = " + pathToJubulaPluginsDirectory());
		getLog().info("SERVER PLUG = " + pathToServerPluginsDirectory());

		// createTestWorkingDirectories();
		// prepareJubulaInstallation();
		// copyUserDefinedPluginsToRcp();
	}

	private void prepareJubulaInstallation() throws MojoExecutionException {
		final File jubula = fetchArtifact(getJubulaBootsrapDependency());
		extract(jubula, new File(pathToJubulaInstallationDirectory()));

		copyServerJubulaPluginsToJubulaPlugins();
	}

	private void createTestWorkingDirectories() {
		createFile(RCPWORKSPACE_DIRECTORY_NAME);
		createFile(RESULTS_DIRECTORY_NAME);
	}

	private void copyServerJubulaPluginsToJubulaPlugins()
			throws MojoExecutionException {
		try {
			FileUtils.copyDirectory( //
					new File(pathToServerPluginsDirectory()), //
					new File(pathToJubulaPluginsDirectory()));

		} catch (final IOException e) {
			throw new MojoExecutionException(
					"Error copying plugins. Verify that the RCP target directory is correct.",
					e);
		}
	}

	private void copyUserDefinedPluginsToRcp() throws MojoExecutionException {
		try {
			final File pluginsDirectory = new File(rcpTargetDirectory,
					"plugins");

			// TODO - Until the RC is uploaded to Maven or shipped with Jubula
			// bootstrap, we are incluiding it here - miguel
			FileUtils.copyFileToDirectory( //
					new File(getRemoteControlArtifactUrl().toURI()), //
					pluginsDirectory);

			for (final File artifact : fetchArtifacts()) {
				FileUtils.copyFileToDirectory(artifact, pluginsDirectory);
				getLog().info(
						"Wrote artifact " + artifact.getAbsolutePath()
								+ " to plugins directory");
			}

		} catch (final IOException e) {
			throw new MojoExecutionException(
					"Error copying plugins. Verify that the RCP target directory is correct.",
					e);
		} catch (final URISyntaxException e) {
			throw new MojoExecutionException(
					"Error when converting RC file URL to URI.", e);
		}
	}

	protected URL getRemoteControlArtifactUrl() {
		return getClass().getClassLoader().getResource(
				"org.eclipse.jubula.rc.rcp_1.2.1.201206131127.jar");
	}

	private List<File> fetchArtifacts() throws MojoExecutionException {
		final List<Dependency> allPlugins = getAllPlugins();

		final List<File> artifacts = new ArrayList<File>(allPlugins.size());
		for (final Dependency dependency : allPlugins) {
			final File file = fetchArtifact(dependency);
			artifacts.add(file);
		}

		return artifacts;
	}

	protected List<Dependency> getAllPlugins() {
		final ArrayList<Dependency> allPlugins = new ArrayList<Dependency>(
				jubulaPlugins);
		// TODO - Someone should upload this dependency to Maven or at least
		// copy it to bootstrap distribution. - miguel
		// allPlugins.add(remoteControlDependency());
		return allPlugins;
	}

	@SuppressWarnings("unused")
	private Dependency getRemoteControlDependency() {
		// TODO initialize dependency bean
		return createDependency("", "", "", "");
	}

	private Dependency getJubulaBootsrapDependency() {
		return createDependency( //
				"org.mule.tooling", //
				"jubula-bootstrap", //
				JUBULA_BOOTSTRAP_VERSION, //
				"zip");
	}

	protected Dependency createDependency(final String groupId,
			final String artifactId, final String version, final String type) {
		final Dependency dependency = new Dependency();
		dependency.setGroupId(groupId);
		dependency.setArtifactId(artifactId);
		dependency.setVersion(version);
		dependency.setType(type);
		return dependency;
	}

	private File fetchArtifact(final Dependency dependency)
			throws MojoExecutionException {

		return fetchArtifact(makeMavenCoordinates(dependency));
	}

	private String makeMavenCoordinates(final Dependency dependency) {
		return String.format("%s:%s:%s:%s", //
				dependency.getGroupId(), //
				dependency.getArtifactId(), //
				dependency.getVersion(), //
				dependency.getType());
	}

	public File fetchArtifact(final String mavenCoordinates)
			throws MojoExecutionException {
		final ArtifactRequest request = new ArtifactRequest();
		request.setArtifact(new DefaultArtifact(mavenCoordinates));
		request.setRepositories(remoteRepos);

		getLog().info(
				"Resolving artifact " + mavenCoordinates + " from "
						+ remoteRepos);

		ArtifactResult result;
		try {
			result = repoSystem.resolveArtifact(repoSession, request);
		} catch (final ArtifactResolutionException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}

		final Artifact artifact = result.getArtifact();

		getLog().info(
				"Resolved artifact " + artifact + " to "
						+ result.getArtifact().getFile() + " from "
						+ result.getRepository());

		return artifact.getFile();
	}

	public void extract(final File file, final File extractDirectory) {
		createFile(extractDirectory);

		final ZipUnArchiver zipUnArchiver = new ZipUnArchiver(file);
		zipUnArchiver.setDestDirectory(extractDirectory);
		zipUnArchiver.extract();
	}

	protected void createFile(final String fileToBeCreatedName) {
		final File fileToBeCreated = new File(buildDirectory,
				fileToBeCreatedName);

		createFile(fileToBeCreated);
	}

	protected void createFile(final File fileToBeCreated) {
		if (!fileToBeCreated.exists())
			fileToBeCreated.mkdirs();
	}
}
