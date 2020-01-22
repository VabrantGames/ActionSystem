package com.vabrant.actionsystem.test.colortests;

import com.vabrant.actionsystem.test.ActionSystemTestSelector;
import com.vabrant.testbase.TestBaseApplicationListener;

public class RGBTestsScreen extends ActionSystemTestSelector{
	
	Class[] tests = {
			ChangeColorRGBTest.class,
	};
	
	public RGBTestsScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
