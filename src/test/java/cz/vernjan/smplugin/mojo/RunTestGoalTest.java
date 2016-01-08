/* 
 * Created on Nov 3, 2015 
 * 
 * Copyright (c) 2015 Etnetera, a.s. All rights reserved. 
 * Intended for internal use only. 
 * http://www.etnetera.cz 
 */
package cz.vernjan.smplugin.mojo;

import java.io.File;

/**
 *
 * @author VERNER Jan
 *
 */
public class RunTestGoalTest {

	public static void main(String... args) throws Exception {
		RunTestGoal goal = new RunTestGoal();
		goal.setSmartMeterHome(new File("/home/vernjan/apps/smartmeter-light-1.0.5"));
		goal.execute();
	}

}
