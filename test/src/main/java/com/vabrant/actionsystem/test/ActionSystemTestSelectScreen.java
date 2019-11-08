package com.vabrant.actionsystem.test;

import com.vabrant.actionsystem.test.actiontests.ActionTestsSelectScreen;
import com.vabrant.actionsystem.test.colortests.ColorTestsSelectScreen;
import com.vabrant.actionsystem.test.grouptests.GroupTestsSelectScreen;
import com.vabrant.actionsystem.test.misctests.MiscTestSelector;
import com.vabrant.actionsystem.test.movetests.MoveTestsSelectScreen;
import com.vabrant.actionsystem.test.percentactiontests.PercentTestsSelectScreen;
import com.vabrant.actionsystem.test.performancetests.PerformanceTestsSelectScreen;
import com.vabrant.actionsystem.test.repeattests.RepeatTestsSelectScreen;
import com.vabrant.actionsystem.test.rotatetests.RotateTestsSelectScreen;
import com.vabrant.actionsystem.test.scaletests.ScaleTestsSelectScreen;
import com.vabrant.actionsystem.test.shaketests.ShakeTestsSelectScreen;
import com.vabrant.actionsystem.test.zoomtests.ZoomTestsSelectScreen;
import com.vabrant.testbase.TestApplication;
import com.vabrant.testbase.TestSelectScreen;

public class ActionSystemTestSelectScreen extends TestSelectScreen{
	
	Class[] testScreens = {
			ColorTestsSelectScreen.class,
			MoveTestsSelectScreen.class,
			ActionTestsSelectScreen.class,
			RepeatTestsSelectScreen.class,
			RotateTestsSelectScreen.class,
			ScaleTestsSelectScreen.class,
			PerformanceTestsSelectScreen.class,
			GroupTestsSelectScreen.class,
			ZoomTestsSelectScreen.class,
			ShakeTestsSelectScreen.class,
			PercentTestsSelectScreen.class,
			PlaygroundSelectScreen.class,
			MiscTestSelector.class,
	};
	
	public ActionSystemTestSelectScreen(TestApplication app) {
		super(app);
		setRoot(this);
		addTests(testScreens);
	}

}
