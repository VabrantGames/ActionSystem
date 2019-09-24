package com.vabrant.actionsystem.test.actiontests;

import com.badlogic.gdx.Gdx;
import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class ActionTestsSelectScreen extends TestSelectScreen {

	Class[] tests = {
			UnmanagedTest.class
	};
	
	public ActionTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
