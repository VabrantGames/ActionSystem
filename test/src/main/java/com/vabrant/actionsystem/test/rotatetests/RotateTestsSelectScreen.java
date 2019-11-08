package com.vabrant.actionsystem.test.rotatetests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class RotateTestsSelectScreen extends TestSelectScreen {

	Class[] tests = {
			RotateToTest.class,
			RotateByTest.class,
			SetRotationTest.class,
			RestartRotateByFromEndTest.class
	};
	
	public RotateTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
