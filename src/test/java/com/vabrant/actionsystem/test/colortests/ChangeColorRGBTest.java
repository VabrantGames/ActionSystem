package com.vabrant.actionsystem.test.colortests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.vabrant.actionsystem.ColorAction;
import com.vabrant.actionsystem.test.ActionSystemTestScreen;
import com.vabrant.testbase.TestSelectScreen;

public class ChangeColorRGBTest extends ActionSystemTestScreen {
	
	Color startColor = new Color(Color.BLACK);
	Color endColor = new Color(Color.RED);
	
	public ChangeColorRGBTest(TestSelectScreen screen) {
		super(screen);
		createTestObject();
		reset();
	}
	
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		testObject.setX(screenCenterX - (testObject.width / 2));
		testObject.setY(screenCenterY - (testObject.height / 2));
	}

	@Override
	public void runTest() {
		actionManager.addAction(ColorAction.changeColor(testObject, endColor, 1f, false, Interpolation.linear));
	}
	
	@Override
	public void reset() {
		testObject.setColor(startColor);
	}

}
