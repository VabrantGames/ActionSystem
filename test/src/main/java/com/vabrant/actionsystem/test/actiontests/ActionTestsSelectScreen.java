package com.vabrant.actionsystem.test.actiontests;

import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.ActionSystemTestSelector;

public class ActionTestsSelectScreen extends ActionSystemTestSelector {

	Class[] tests = {
			UnmanagedTest.class,
			ChainingTest.class,
			PreActionsTest.class,
			PreActionsEarlyEndTest.class,
			PostActionsTest.class,
			ActionListenerTest.class,
			NotLastCycleEndTest.class,
	};
	
	public ActionTestsSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
