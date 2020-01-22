package com.vabrant.actionsystem.test;

import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.ActionSystemTestSelector;

public class PlaygroundSelectScreen extends ActionSystemTestSelector {
	
	Class[] tests = {
			TestingPlayground.class,
	};

	public PlaygroundSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
