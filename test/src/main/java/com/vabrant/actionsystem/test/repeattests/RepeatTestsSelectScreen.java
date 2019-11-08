package com.vabrant.actionsystem.test.repeattests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class RepeatTestsSelectScreen extends TestSelectScreen {

	Class[] tests = {
			ContinuousTest.class,
			RepeatTest.class,
			NestedRepeatTest.class,
	};
	
	public RepeatTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
