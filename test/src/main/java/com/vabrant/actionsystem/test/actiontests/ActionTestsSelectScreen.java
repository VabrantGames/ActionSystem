package com.vabrant.actionsystem.test.actiontests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class ActionTestsSelectScreen extends TestSelectScreen {

	Class[] tests = {
			UnmanagedTest.class,
			ChainingTest.class,
			PreActionsTest.class,
			PreActionsEarlyEndTest.class,
			PostActionsTest.class,
			ActionListenerTest.class,
			NotLastCycleEndTest.class,
	};
	
	public ActionTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
