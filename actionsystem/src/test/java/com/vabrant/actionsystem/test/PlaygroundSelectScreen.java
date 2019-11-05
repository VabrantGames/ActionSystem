package com.vabrant.actionsystem.test;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class PlaygroundSelectScreen extends TestSelectScreen {
	
	Class[] tests = {
			TestingPlayground.class,
	};

	public PlaygroundSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
