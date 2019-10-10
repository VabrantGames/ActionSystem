package com.vabrant.actionsystem.test.performancetests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.vabrant.actionsystem.ColorAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ActionUpdateTest extends ActionSystemTestScreen {
	
	private final PerformanceTimer timer = new PerformanceTimer(2);
	private final int iterations = 10000;
	private final Color colorOne = new Color();
	private final Color colorTwo = new Color();
	
	public ActionUpdateTest(TestSelectScreen screen) {
		super(screen);
		ColorAction.HSBToRGB(colorOne, 100, 1, 1, 1);
		ColorAction.HSBToRGB(colorTwo, 100, 1, 1, 1);
	}

	@Override
	public void runTest() {
		timer.clear();
		testOne();
		testTwo();
		System.out.println(timer.toString());
	}
	
	private void testOne() {
		timer.start();
		for(int i = 0; i < iterations; i++) {
			ColorAction.HSBToRGB(colorOne, 100, 1, 1, 1);
			ColorAction.HSBToRGB(colorOne, 100, 1, 1, 1);
			ColorAction.HSBToRGB(colorOne, 100, 1, 1, 1);
		}
		timer.end();
	}
	
	private void testTwo() {
		int limit = MathUtils.random() > 0.5f ? 100 : 1000;
		int amount = iterations / limit;
		timer.start();
		for(int ii = 0; ii < limit; ii++) {
			for(int j = 0; j < amount; j++) {
				ColorAction.HSBToRGB(colorTwo, 100, 1, 1, 1);
				ColorAction.HSBToRGB(colorTwo, 100, 1, 1, 1);
				ColorAction.HSBToRGB(colorTwo, 100, 1, 1, 1);
			}
		}
		timer.end();
	}

}
