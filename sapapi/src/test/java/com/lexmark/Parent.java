/**
 * @author Anup Ladha
 *
 * @date 09/06/2022
 * 
 * @description	To capture time taken by a Test in minutes
 */

package com.lexmark;

import java.text.DecimalFormat;

import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class Parent {
	private static final DecimalFormat df = new DecimalFormat("0.00");
	double start;

    @BeforeClass(alwaysRun = true)
    public void beforeClass() {
        start = System.currentTimeMillis();
    }

    @AfterClass(alwaysRun = true)
    public void afterClass() {
    	Reporter.log(this.getClass().getSimpleName()
                + ": " + (df.format((System.currentTimeMillis() - start)/(1000*60)) + " m\n"), 2);
        System.err.println(this.getClass().getSimpleName()
                + ": " + (df.format((System.currentTimeMillis() - start)/(1000*60)) + " m\n"));
    }
}