package com.vabrant.actionsystem.test.movetests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class MoveTestsSelectScreen extends TestSelectScreen {

	Class[] tests = {
		MoveXByTest.class,	
		MoveXByRepeatTest.class,
		MoveYByTest.class,
		MoveYByRepeatTest.class,
		MoveByAngleTest.class
	};
	
	public MoveTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
