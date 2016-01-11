/* 
 * Created on Nov 4, 2015 
 * 
 * Copyright (c) 2015 Etnetera, a.s. All rights reserved. 
 * Intended for internal use only. 
 * http://www.etnetera.cz 
 */
package cz.etnetera.smplugin.utils;

import java.io.File;

/**
 * Helper class representing SmartMeter internal structure on file system.
 *
 * @author VERNER Jan
 *
 */
public final class SmartMeterFiles {

	private static final String FS = File.separator;

	/** programs/[jre]/bin/java */
	public static final String JAVA_BIN = "programs" + FS + getJavaHome() + FS + "bin" + FS + "java";
	/** programs/SmartMeter/lib/Launcher.jar */
	public static final String LAUNCHER_JAR = "programs" + FS + "SmartMeter" + FS + "lib" + FS + "Launcher.jar";


	// PRIVATE METHODS >>

	private static String getJavaHome() {
		final String os = System.getProperty("os.name").toLowerCase();
		if (os.contains("win"))
			return "jre-win";
		if (os.contains("mac"))
			return "jre-macos/Contents/Home";
		if (os.contains("nix") || os.contains("nux") || os.contains("aix"))
			return "jre-linux";

		throw new IllegalStateException("unknown operating system: " + os);
	}


	private SmartMeterFiles() { /* no instance */ }

}
