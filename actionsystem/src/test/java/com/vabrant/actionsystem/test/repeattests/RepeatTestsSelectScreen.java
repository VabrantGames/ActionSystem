package com.vabrant.actionsystem.test.repeattests;

import com.vabrant.actionsystem.test.ActionSystemTestSelector;
import com.vabrant.testbase.TestBaseApplicationListener;

public class RepeatTestsSelectScreen extends ActionSystemTestSelector {

	Class[] tests = {
			ContinuousTest.class,
			RepeatTest.class,
			NestedRepeatTest.class,
	};
	
	public RepeatTestsSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
