package cz.vernjan.smplugin.mojo;

import java.io.File;
import java.io.IOException;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import cz.vernjan.smplugin.utils.SmartMeterFiles;
import cz.vernjan.smplugin.utils.StreamRedirector;

/**
 * Maven goal which executes SmartMeter test.
 *
 * @author VERNER Jan
 *
 */
@Mojo(name = "runTest")
public class RunTestGoal extends AbstractMojo {

	@Parameter(property="runTest.smartMeterHome", required=true)
	private File smartMeterHome;

	@Parameter(property="runTest.monitorPath")
	private String monitorPath;

	@Parameter(property="runTest.testPath")
	private String testPath;

	@Parameter(property="runTest.gui", defaultValue="false")
	private boolean gui;


	/**
	 * Runs SmartMeter test based on configuration read from pom.xml.
	 */
	public void execute() throws MojoFailureException, MojoExecutionException {
		if (!smartMeterHome.exists()) {
			throw new MojoFailureException("SmartMeter home not found: " + smartMeterHome);
		}

		runTest();
		readResult();
	}

	void setSmartMeterHome(File smartMeterHome) {
		this.smartMeterHome = smartMeterHome;
	}


	// PRIVATE METHODS >>

	private void runTest() throws MojoExecutionException {
		ProcessBuilder pb = buildRunTestCommand();
		pb.redirectErrorStream(true);
		
		getLog().info("running test " + testPath);
		getLog().debug("running " + pb.command());

		Process p;
		try {
			p = pb.directory(smartMeterHome).start();
		} catch (IOException e) {
			throw new MojoExecutionException("error", e);
		}
		
		StreamRedirector.start(p.getInputStream(), getLog());

		try {
			p.waitFor();
		} catch (InterruptedException e) {
			getLog().error("interrupted", e);
			Thread.currentThread().interrupt();
		}
	}

	private ProcessBuilder buildRunTestCommand() {
		ProcessBuilder pb = new ProcessBuilder(SmartMeterFiles.JAVA_BIN, "-jar", SmartMeterFiles.LAUNCHER_JAR);

		if (monitorPath != null) {
			pb.command().add((gui ? "runDistTest" : "runDistTestNonGui"));
			pb.command().add(monitorPath);
			if (testPath != null) {
				pb.command().add(testPath);
			}
		} else {
			pb.command().add((gui ? "runTest" : "runTestNonGui"));
			pb.command().add(testPath);
		}

		if (gui) {
			pb.command().add("-Jetn_shutdown_after_test=true");
		}

		return pb;
	}

	private void readResult() {
		// TODO JVe 1T implement
	}

}