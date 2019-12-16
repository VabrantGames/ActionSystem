package com.vabrant.actionsystem.test.percentactiontests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class PercentTestsSelectScreen extends TestSelectScreen {

	Class<?>[] tests = {
		KillTest.class,
		EndTest.class,
		ReverseTest.class,
		ReverseBackToStartTest.class,
		MoveToPercentTest.class
	};
	
	public PercentTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
