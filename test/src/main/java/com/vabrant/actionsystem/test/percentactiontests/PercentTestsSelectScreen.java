package com.vabrant.actionsystem.test.percentactiontests;

import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.ActionSystemTestSelector;

public class PercentTestsSelectScreen extends ActionSystemTestSelector {

	Class<?>[] tests = {
		KillTest.class,
		EndTest.class,
		ReverseTest.class,
		ReverseBackToStartTest.class,
		MoveToPercentTest.class
	};
	
	public PercentTestsSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
