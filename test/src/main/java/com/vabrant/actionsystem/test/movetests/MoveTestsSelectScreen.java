package com.vabrant.actionsystem.test.movetests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class MoveTestsSelectScreen extends TestSelectScreen {

	Class[] tests = {
		MoveXByTest.class,	
		RestartMoveXByFromEndTest.class,
		MoveYByTest.class,
		RestartMoveYByFromEndTest.class,
		MoveByAngleTest.class,
		MoveConflictTest.class,
	};
	
	public MoveTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
