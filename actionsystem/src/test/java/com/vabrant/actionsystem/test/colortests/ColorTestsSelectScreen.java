package com.vabrant.actionsystem.test.colortests;

import com.vabrant.actionsystem.test.ActionSystemTestSelector;
import com.vabrant.testbase.TestBaseApplicationListener;

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
