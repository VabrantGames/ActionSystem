package com.vabrant.actionsystem.test.performancetests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class PerformanceTestsSelectScreen extends TestSelectScreen {

	Class[] tests = {
			BooleanVSEmptyCompute.class,
			HSBToRGBTest.class,
			ActionUpdateTest.class,
	};
	
	public PerformanceTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
