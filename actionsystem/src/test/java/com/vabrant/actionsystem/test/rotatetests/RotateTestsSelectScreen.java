package com.vabrant.actionsystem.test.rotatetests;

import com.vabrant.actionsystem.test.ActionSystemTestSelector;
import com.vabrant.testbase.TestBaseApplicationListener;

public class RotateTestsSelectScreen extends ActionSystemTestSelector {

	Class[] tests = {
			RotateToTest.class,
			RotateByTest.class,
			SetRotationTest.class,
			RestartRotateByFromEndTest.class
	};
	
	public RotateTestsSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
