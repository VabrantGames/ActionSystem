package com.vabrant.actionsystem.test.colortests;

import com.vabrant.testbase.TestBaseApplicationListener;
import com.vabrant.testbase.ActionSystemTestSelector;

public class ColorTestsSelectScreen extends ActionSystemTestSelector {
	
	Class[] tests = {
			ChangeColorRGBTest.class,
			ChangeColorHSBTest.class,
			ChangeHueTest.class,
			ChangeAlphaTest.class,
			ChangeSaturationTest.class,
			ChangeBrightnessTest.class
	};
	
	public ColorTestsSelectScreen(TestBaseApplicationListener app) {
		super(app);
		addTests(tests);
	}

}
