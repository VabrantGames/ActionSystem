package com.vabrant.actionsystem.test.colortests;

import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class ColorTestsSelectScreen extends TestSelectScreen {
	
	Class[] tests = {
			ChangeColorRGBTest.class,
			ChangeColorHSBTest.class,
			ChangeHueTest.class,
			ChangeAlphaTest.class,
			ChangeSaturationTest.class,
			ChangeBrightnessTest.class
	};
	
	public ColorTestsSelectScreen(TestApplication app) {
		super(app);
		addTests(tests);
	}

}
