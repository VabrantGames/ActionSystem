package com.vabrant.actionsystem.test;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class ActionTestSelectScreen extends TestSelectScreen{
	
	Class[] testScreens = {
			ActionTest.class,
			ColorActionTestScreen.class,
			MoveActionTestScreen.class,
			};
	
	
	public ActionTestSelectScreen(TestApplication app) {
		super(app);
		addTests(testScreens);
	}

}