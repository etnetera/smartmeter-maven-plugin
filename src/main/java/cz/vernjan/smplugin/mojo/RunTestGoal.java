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

	private static final String PARAM_CREATE_REPORT_AFTER_TEST = "-Jetn_create_report_after_test_default=true";
	private static final String PARAM_CLOSE_CONTROLLER_AFTER_TEST = "-Jetn_shutdown_after_test=true";
	private static final String PARAM_CLOSE_MONITOR_AFTER_TEST = "-Jetn_close_monitor_after_test=true";

	// JVe pouze docasne - pro profi verzi 1.1.0 (jakmile bude 1.2.0 venku, tak zde zrusime)
	private static final String PARAM_DIST_CREATE_REPORT_AFTER_TEST = "-Jetnc_create_report_after_test_default=true";
	private static final String PARAM_DIST_CLOSE_CONTROLLER_AFTER_TEST = "-Jetnc_shutdown_after_test=true";

	
	@Parameter(property="runTest.smartMeterHome", required=true)
	private File smartMeterHome;

	@Parameter(property="runTest.monitorPath")
	private String monitorPath;
	@Parameter(property="runTest.testPath")
	private String testPath;

	@Parameter(property="runTest.distributed", defaultValue="false")
	private boolean distributed;
	@Parameter(property="runTest.gui", defaultValue="false")
	private boolean gui;
	
	@Parameter(property="runTest.extraParams")
	private String extraParams;


	/**
	 * Runs SmartMeter test based on configuration read from pom.xml.
	 */
	@Override
	public void execute() throws MojoFailureException, MojoExecutionException {
		if (!smartMeterHome.exists())
			throw new MojoFailureException("SmartMeter home not found: " + smartMeterHome);
		if (distributed && monitorPath == null)
			throw new MojoFailureException("monitor path required");
		if (!distributed && testPath == null)
			throw new MojoFailureException("test path required");

		runTest();
		readResult();
	}

	void setSmartMeterHome(File smartMeterHome) {
		this.smartMeterHome = smartMeterHome;
	}


	// PRIVATE METHODS >>

	private void runTest() throws MojoExecutionException {
		Process p;
		try {
			p = buildRunTestCommand().start();
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

		if (distributed) {
			pb.command().add(gui ? "runDistTest" : "runDistTestNonGui");
			pb.command().add(monitorPath);
			if (testPath != null) {
				pb.command().add(testPath);
			}
			getLog().info("running distributed test " + testPath + " on monitor " + monitorPath);
		} else {
			pb.command().add(gui ? "runTest" : "runTestNonGui");
			pb.command().add(testPath);
			getLog().info("running test " + testPath);
		}

		
		// JVe takto pro nove verze
//		if (gui) { // make sure SmartMeter shuts down after test (in Non-Gui will shut down automatically)
//			if (distributed) {
//				pb.command().add(PARAM_CLOSE_MONITOR_AFTER_TEST);
//			}
//			pb.command().add(PARAM_CLOSE_CONTROLLER_AFTER_TEST); 
//		}
//		
//		pb.command().add(PARAM_CREATE_REPORT_AFTER_TEST);
		
		
		// JVe pouze docasne - pro profi verzi 1.1.0 (jakmile bude 1.2.0 venku, tak zde zrusime)
		if (gui) { // make sure SmartMeter shuts down after test (in Non-Gui will shut down automatically)
			if (distributed) {
				pb.command().add(PARAM_CLOSE_MONITOR_AFTER_TEST);
				pb.command().add(PARAM_DIST_CLOSE_CONTROLLER_AFTER_TEST);
			}
			pb.command().add(PARAM_CLOSE_CONTROLLER_AFTER_TEST); 
		}
		
		if (distributed) {
			pb.command().add(PARAM_DIST_CREATE_REPORT_AFTER_TEST);
		}
		pb.command().add(PARAM_CREATE_REPORT_AFTER_TEST);
		
		if (extraParams != null) {
			pb.command().add(extraParams);
		}
		
		pb.redirectErrorStream(true);
		
		pb.directory(smartMeterHome);
		
		getLog().debug("running " + pb.command());

		return pb;
	}

	private void readResult() {
		// TODO JVe 1T implement
	}

}