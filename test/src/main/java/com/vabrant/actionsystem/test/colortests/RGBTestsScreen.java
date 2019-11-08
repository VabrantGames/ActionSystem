package com.vabrant.actionsystem.test.colortests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class RGBTestsScreen extends TestSelectScreen{
	
	Class[] tests = {
			ChangeColorRGBTest.class,
	};
	
	public RGBTestsScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
