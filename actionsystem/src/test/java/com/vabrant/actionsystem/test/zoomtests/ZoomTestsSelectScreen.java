package com.vabrant.actionsystem.test.zoomtests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class ZoomTestsSelectScreen extends TestSelectScreen {

	Class[] tests = {
			ZoomToTest.class,
			ZoomByTest.class,
			SetZoomTest.class,
			RestartZoomByFromEndTest.class,
	};
	
	public ZoomTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
