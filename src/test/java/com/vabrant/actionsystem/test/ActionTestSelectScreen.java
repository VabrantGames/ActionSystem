package com.vabrant.actionsystem.test;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class ActionTestSelectScreen extends TestSelectScreen{
	
	Class[] testScreens = {
			ColorActionTestScreen.class,
			MoveActionTestScreen.class,
			ZoomActionTestScreen.class,
			ShakeActionTestScreen.class,
			RotateActionTestScreen.class,
			ScaleActionTestScreen.class,
			ActionTestScreen.class,
			ActionPoolsTestScreen.class,
			ActionManagerTestScreen.class,
			};
	
	public ActionTestSelectScreen(TestApplication app) {
		super(app);
		addTests(testScreens);
	}

}
