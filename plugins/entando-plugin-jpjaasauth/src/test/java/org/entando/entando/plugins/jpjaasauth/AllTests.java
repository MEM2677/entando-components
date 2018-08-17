package org.entando.entando.plugins.jpjaasauth;

import org.entando.entando.plugins.jpjaasauth.aps.TestApsSample;
import org.entando.entando.plugins.jpjaasauth.apsadmin.TestApsAdminSample;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {
	
	public static Test suite() {
		TestSuite suite = new TestSuite("Change me with a suitable description");

		suite.addTestSuite(TestApsSample.class);
		suite.addTestSuite(TestApsAdminSample.class);
		
		return suite;
	}
	
}
