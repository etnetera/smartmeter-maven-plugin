/* 
 * Created on Nov 4, 2015 
 * 
 * Copyright (c) 2015 Etnetera, a.s. All rights reserved. 
 * Intended for internal use only. 
 * http://www.etnetera.cz 
 */
package cz.vernjan.smplugin.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.maven.plugin.logging.Log;

/**
 * Redirects and logs the output from executed process.
 * 
 * @author VERNER Jan
 * 
 */
public class StreamRedirector implements Runnable {
	
	/**
	 * Starts stream redirector in a new daemon thread.
	 * @param input redirected input
	 * @param output Maven log
	 */
	public static void start(InputStream input, Log output) {
		final Thread t = new Thread(new StreamRedirector(input, output));
		t.setDaemon(true);
		t.start();
	}
	
	private final InputStream input;
	private final Log output;
	
	StreamRedirector(InputStream input, Log output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public void run() { 
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
			while (true) {
				String line;
				while ((line = reader.readLine()) != null && !line.trim().equals("--EOF--")) {
					output.info("  SmartMeter: " + line);
				}
				break;
			}
		}
		catch (Exception e) {
			output.warn("failed to redirect output from test", e);
		}
	}
	
}