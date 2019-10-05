package com.vabrant.actionsystem.test.scaletests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class ScaleTestsSelectScreen extends TestSelectScreen {

	Class[] tests = {
			ScaleXByTest.class,
			ScaleYByTest.class,
			ScaleXToTest.class,
			ScaleYToTest.class,
			SetScaleXTest.class,
			SetScaleYTest.class,
			RestartScaleXByFromEndTest.class,
			RestartScaleYByFromEndTest.class
	};
	
	public ScaleTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
