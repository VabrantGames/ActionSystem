
package com.vabrant.actionsystem.test.performancetests;

import com.badlogic.gdx.graphics.Color;
import com.vabrant.actionsystem.actions.coloraction.HSBColorLogic;

public class HSBToRGBTest {

	public static void main (String[] args) {
		HSBToRGBTest h = new HSBToRGBTest();
		h.runTest();
	}

	private final float h = 300;
	private final float s = 1f;
	private final float v = 0.5f;
	private final int iterations = 10000;
	private final PerformanceTimer timer = new PerformanceTimer(2);
	private final Color testColor = new Color(1f, 0.5f, 0.25f, 1);

	public HSBToRGBTest () {
	}

	public void runTest () {
		timer.clear();
		testOne();
		testTwo();
		System.out.println(timer.toString());
	}

	private void testOne () {
		Color color = new Color(testColor);
		timer.start();
		for (int i = 0; i < iterations; i++) {
			color.fromHsv(h, s, v);
		}
		timer.end();
	}

	private void testTwo () {
		Color color = new Color(testColor);
		HSBColorLogic logic = HSBColorLogic.INSTANCE;
		timer.start();
		for (int i = 0; i < iterations; i++) {
			logic.HSBToRGB(testColor, h, s, v);
		}
		timer.end();
	}
}
