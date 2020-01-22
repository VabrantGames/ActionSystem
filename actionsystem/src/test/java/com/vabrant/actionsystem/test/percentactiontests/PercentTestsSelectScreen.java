package com.vabrant.actionsystem.test.percentactiontests;

import com.vabrant.actionsystem.test.ActionSystemTestSelector;
import com.vabrant.testbase.TestBaseApplicationListener;

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
