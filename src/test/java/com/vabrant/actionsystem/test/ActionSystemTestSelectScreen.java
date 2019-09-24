package com.vabrant.actionsystem.test;

import com.vabrant.actionsystem.test.actiontests.ActionTestsSelectScreen;
import com.vabrant.actionsystem.test.colortests.ColorTestsSelectScreen;
import com.vabrant.actionsystem.test.movetests.MoveTestsSelectScreen;
import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class ActionSystemTestSelectScreen extends TestSelectScreen{
	
	Class[] testScreens = {
			ColorTestsSelectScreen.class,
			MoveTestsSelectScreen.class,
			ActionTestsSelectScreen.class
	};
	
	public ActionSystemTestSelectScreen(TestApplication app) {
		super(app);
		setRoot(this);
		addTests(testScreens);
	}

}
