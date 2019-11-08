package com.vabrant.actionsystem.test.shaketests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class ShakeTestsSelectScreen extends TestSelectScreen {
	
	Class[] tests = {
			ShakeXTest.class,
			ShakeYTest.class,
			ShakeAngleTest.class
	};

	public ShakeTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
