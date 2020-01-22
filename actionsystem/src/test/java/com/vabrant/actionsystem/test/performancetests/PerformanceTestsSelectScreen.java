package com.vabrant.actionsystem.test.performancetests;

import com.vabrant.actionsystem.test.ActionSystemTestSelector;
import com.vabrant.testbase.TestBaseApplicationListener;

public class PerformanceTestsSelectScreen extends ActionSystemTestSelector {

	Class[] tests = {
			BooleanVSEmptyCompute.class,
			HSBToRGBTest.class,
			ActionUpdateTest.class,
	};
	
	public PerformanceTestsSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
