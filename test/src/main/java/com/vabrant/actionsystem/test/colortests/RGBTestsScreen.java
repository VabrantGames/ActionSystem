package com.vabrant.actionsystem.test.colortests;

import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.ActionSystemTestSelector;

public class RGBTestsScreen extends ActionSystemTestSelector{
	
	Class[] tests = {
			ChangeColorRGBTest.class,
	};
	
	public RGBTestsScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
