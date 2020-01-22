package com.vabrant.actionsystem.test.scaletests;

import com.vabrant.actionsystem.test.ActionSystemTestSelector;
import com.vabrant.testbase.TestBaseApplicationListener;

public class ScaleTestsSelectScreen extends ActionSystemTestSelector {

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
	
	public ScaleTestsSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
