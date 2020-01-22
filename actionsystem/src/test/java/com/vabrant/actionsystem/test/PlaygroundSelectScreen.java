package com.vabrant.actionsystem.test;

import com.vabrant.testbase.TestBaseApplicationListener;

public class PlaygroundSelectScreen extends ActionSystemTestSelector {
	
	Class[] tests = {
			TestingPlayground.class,
	};

	public PlaygroundSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
