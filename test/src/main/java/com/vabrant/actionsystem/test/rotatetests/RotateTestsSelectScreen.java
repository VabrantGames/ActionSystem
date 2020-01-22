package com.vabrant.actionsystem.test.rotatetests;

import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.ActionSystemTestSelector;

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
