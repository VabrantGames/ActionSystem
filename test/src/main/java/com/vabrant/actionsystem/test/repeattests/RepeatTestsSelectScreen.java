package com.vabrant.actionsystem.test.repeattests;

import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.ActionSystemTestSelector;

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
