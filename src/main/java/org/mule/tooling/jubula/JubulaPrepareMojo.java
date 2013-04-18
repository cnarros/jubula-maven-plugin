package org.mule.tooling.jubula;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
	 * Where the resulting css files must be saved.
	 * 
	 * @parameter expression="${jubula.product}"
	 * @required
	 */
	private String product;

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
	 * @parameter expression="${project.build.directory}/jubula"
	 * @readonly
	 * @required
	 */
	private File workingDirectory;

	/**
	 * ZipUnArchiver
	 * 
	 * @component
	 */
	private ZipUnArchiver zipUnArchiver;

	@Override
	public void execute() throws MojoExecutionException, MojoFailureException {
		// <execution>
		// <id>copy-jubula-plugins</id>
		// <phase>initialize</phase>
		// <configuration>
		// <target>
		// <copy
		// todir="target/jubula-bootstrap-${jubula.version}/jubula/plugins">
		// <fileset
		// dir="target/jubula-bootstrap-${jubula.version}/server/plugins">
		// <include name="**/*.*"/>
		// </fileset>
		// </copy>
		// </target>
		// </configuration>
		// <goals>
		// <goal>run</goal>
		// </goals>
		// </execution>

		if (product == null) {
			throw new IllegalArgumentException(
					"Product argument cannot be null");
		}

		final File jubula = fetchArtifact("org.mule.tooling:org.mule.tooling.studio.product:1.3.1-SNAPSHOT");
		final File jubulaExtractDirectory = new File(workingDirectory, "jubula");
		extract(jubula, jubulaExtractDirectory);
		final File product = fetchArtifact(getProduct());
		final File productFileExtractDir = new File(workingDirectory, "product");
		File productDirectory = null;
		extract(product, productFileExtractDir);

		final File[] files = productFileExtractDir.listFiles();

		if (files == null) {
			throw new IllegalArgumentException(
					"Problem occurred while listing: " + productFileExtractDir);
		}

		// unhack me!
		for (final File file : files) {
			if (file.isDirectory()) {
				productDirectory = file;
			}
		}

		if (productDirectory == null) {
			throw new MojoExecutionException(
					"Directory not found in extracted directory.");
		}

		try {
			for (final String s : pluginsToCopy) {
				FileUtils.copyFileToDirectory(fetchArtifact(s), new File(
						productDirectory, "plugins"));
			}

		} catch (final IOException e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}

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
		extractDirectory.mkdirs();
		final ZipUnArchiver zipUnArchiver = new ZipUnArchiver(file);
		zipUnArchiver.setDestDirectory(extractDirectory);
		zipUnArchiver.extract();
	}

	public String getProduct() {
		return product;
	}

}
