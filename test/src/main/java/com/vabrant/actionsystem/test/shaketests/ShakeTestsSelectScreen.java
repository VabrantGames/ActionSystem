package com.vabrant.actionsystem.test.shaketests;

import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.ActionSystemTestSelector;

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
