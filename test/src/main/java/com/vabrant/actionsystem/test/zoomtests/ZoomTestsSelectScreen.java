package com.vabrant.actionsystem.test.zoomtests;

import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.ActionSystemTestSelector;

public class ZoomTestsSelectScreen extends ActionSystemTestSelector {

	Class[] tests = {
			ZoomToTest.class,
			ZoomByTest.class,
			SetZoomTest.class,
			RestartZoomByFromEndTest.class,
	};
	
	public ZoomTestsSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
