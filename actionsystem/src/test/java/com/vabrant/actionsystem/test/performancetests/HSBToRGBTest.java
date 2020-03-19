package com.vabrant.actionsystem.test.performancetests;

import com.badlogic.gdx.graphics.Color;
import com.vabrant.actionsystem.actions.ColorAction;

public class HSBToRGBTest {

	private final float h = 300;
	private final float s = 0f;
	private final float v = 0.5f;
	private final int iterations = 10000;
	private final PerformanceTimer timer = new PerformanceTimer(2);
	private final Color testColor = new Color();
	
	public HSBToRGBTest() {
	}

	public void runTest() {
		timer.clear();
		testOne();
		testTwo();
		System.out.println(timer.toString());
	}
	
	private void testOne() {
		timer.start();
		for(int i = 0; i < iterations; i++) {
			testColor.fromHsv(h, s, v);
		}
		timer.end();
	}
	
	private void testTwo() {
		timer.start();
		for(int i = 0; i < iterations; i++) {
			ColorAction.HSBToRGB(testColor, h, s, v, 1f);
		}
		timer.end();
	}


}
