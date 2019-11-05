package com.vabrant.actionsystem.test.misctests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class MiscTestSelector extends TestSelectScreen {

	Class[] tests = {
			ActionWatcherTest.class,
			CountDownActionTest.class
	};
	
	public MiscTestSelector(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
