/**
 * @author Anup Ladha
 *
 * @date 09/06/2022
 * 
 */

package com.lexmark;

import org.testng.IClassListener;
import org.testng.ITestClass;

public class CustomListener implements IClassListener {
	long start;
	private int m_count = 0;

	public void onBeforeClass(ITestClass testClass) {
		start = System.currentTimeMillis(); 
	}

	public void onAfterClass(ITestClass testClass) {
		log(testClass.getName() + ": " + (System.currentTimeMillis() - start) + " ms\n\n");
	}

	private void log(String string) {
		System.err.print(string);
		if (++m_count % 40 == 0) {
			System.out.println("");
		}
	}
}