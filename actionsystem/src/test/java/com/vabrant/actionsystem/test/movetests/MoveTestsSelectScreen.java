package com.vabrant.actionsystem.test.movetests;

import com.vabrant.actionsystem.test.ActionSystemTestSelector;
import com.vabrant.testbase.TestBaseApplicationListener;

public class MoveTestsSelectScreen extends ActionSystemTestSelector {

	Class[] tests = {
		MoveXByTest.class,	
		RestartMoveXByFromEndTest.class,
		MoveYByTest.class,
		RestartMoveYByFromEndTest.class,
		MoveByAngleTest.class,
		MoveConflictTest.class,
	};
	
	public MoveTestsSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
