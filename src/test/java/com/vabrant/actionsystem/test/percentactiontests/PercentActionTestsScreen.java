package com.vabrant.actionsystem.test.percentactiontests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class PercentActionTestsScreen extends TestSelectScreen {

	Class<?>[] tests = {
		KillTest.class,
	};
	
	public PercentActionTestsScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
