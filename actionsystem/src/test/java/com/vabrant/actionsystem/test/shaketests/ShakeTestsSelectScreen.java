package com.vabrant.actionsystem.test.shaketests;

import com.vabrant.actionsystem.test.ActionSystemTestSelector;
import com.vabrant.testbase.TestBaseApplicationListener;

public class ShakeTestsSelectScreen extends ActionSystemTestSelector {
	
	Class[] tests = {
			ShakeXTest.class,
			ShakeYTest.class,
			ShakeAngleTest.class
	};

	public ShakeTestsSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
