package com.vabrant.actionsystem.test.performancetests;

import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.ActionSystemTestSelector;

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
